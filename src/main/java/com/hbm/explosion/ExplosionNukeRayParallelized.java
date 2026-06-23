package com.hbm.explosion;

import com.hbm.interfaces.IExplosionRay;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Threaded DDA raytracer for mk5 explosion.
 *
 * @author mlbv
 */
public class ExplosionNukeRayParallelized implements IExplosionRay {

    private static final AtomicInteger THREAD_COUNTER = new AtomicInteger();

    private static final int WORKER_COUNT = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(WORKER_COUNT, runnable -> {
        Thread thread = new Thread(
                runnable,
                "NukeRayWorker-" + THREAD_COUNTER.incrementAndGet()
        );

        thread.setDaemon(true);
        return thread;
    });

    private static final float NUKE_RESISTANCE_CUTOFF = 2_000_000F;
    private static final float INITIAL_ENERGY_FACTOR = 0.3F; // Scales crater, no impact on performance
    private static final double RESOLUTION_FACTOR = 1.0; // Scales ray density, no impact on crater radius

    private static final double RAY_DIRECTION_EPSILON = 1.0E-6;
    private static final double PROCESSING_EPSILON = 1.0E-9;
    private static final float MIN_EFFECTIVE_DIST_FOR_ENERGY_CALC = 0.01F;

    private final Level level;

    private final double explosionX;
    private final double explosionY;
    private final double explosionZ;

    private final int originX;
    private final int originY;
    private final int originZ;

    private final int strength;
    private final int radius;

    private final int minBuildHeight;
    private final int maxBuildHeight;

    private final int bitsetSize;

    private final List<RayDirection> directions;

    private final List<SectionKey> proactiveSectionKeys;

    private int proactiveIndex = 0;

    private final BlockingQueue<SectionKey> highPrioritySectionQueue = new LinkedBlockingQueue<>();

    private final ConcurrentMap<SectionKey, SectionSnapshot> snapshots;

    private final ConcurrentMap<SectionKey, ConcurrentLinkedQueue<RayTask>> waitingRoom = new ConcurrentHashMap<>();

    private final BlockingQueue<RayTask> rayQueue;

    private final ConcurrentMap<Long, ConcurrentBitSet> destructionMap = new ConcurrentHashMap<>();

    private final List<Future<?>> workerFutures = new ArrayList<>();

    private final AtomicInteger remainingRays;

    private final Deque<Long> orderedChunks = new ArrayDeque<>();

    private volatile boolean chunksOrdered = false;

    private volatile boolean collectFinished = false;
    private volatile boolean destroyFinished = false;
    private volatile boolean cancelled = false;

    public ExplosionNukeRayParallelized(Level level, double x, double y, double z, int strength, int ignored, int radius) {
        this.level = level;

        this.explosionX = x;
        this.explosionY = y;
        this.explosionZ = z;

        this.originX = (int) Math.floor(x);
        this.originY = (int) Math.floor(y);
        this.originZ = (int) Math.floor(z);

        this.strength = strength;
        this.radius = radius;

        this.minBuildHeight = level.getMinBuildHeight();
        this.maxBuildHeight = level.getMaxBuildHeight();
        int worldHeight = maxBuildHeight - minBuildHeight;

        this.bitsetSize = 16 * worldHeight * 16;

        int rayCount = Math.max(0, (int) (2.5D * Math.PI * strength * strength * RESOLUTION_FACTOR));

        this.directions = generateSphereRays(rayCount);
        this.remainingRays = new AtomicInteger(rayCount);

        this.proactiveSectionKeys = getAllPotentialSections();
        this.snapshots = new ConcurrentHashMap<>(Math.max(16, proactiveSectionKeys.size()));

        List<RayTask> initialTasks = new ArrayList<>(rayCount);

        for(int i = 0; i < rayCount; i++) initialTasks.add(new RayTask(i));

        this.rayQueue = new LinkedBlockingQueue<>(initialTasks);

        if(rayCount <= 0) {
            this.collectFinished = true;
            this.destroyFinished = true;
            return;
        }

        for(int i = 0; i < WORKER_COUNT; i++) {
            Future<?> future = EXECUTOR.submit(new Worker());
            workerFutures.add(future);
        }
    }

    @Override
    public void cacheChunksTick(int processTimeMs) {
        if(cancelled || collectFinished) return;

        long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(processTimeMs);

        while(!cancelled && System.nanoTime() < deadline) {
            SectionKey key = highPrioritySectionQueue.poll();
            if(key == null) break;

            this.processCacheKey(key);
        }

        while(!cancelled && System.nanoTime() < deadline && proactiveIndex < proactiveSectionKeys.size()) {
            SectionKey key = proactiveSectionKeys.get(proactiveIndex++);
            this.processCacheKey(key);
        }

        this.checkCollectCompletion();
    }

    private void processCacheKey(SectionKey key) {
        if(snapshots.containsKey(key)) {
            this.wakeWaitingRays(key);
            return;
        }

        SectionSnapshot snapshot = SectionSnapshot.create(level, key, minBuildHeight, maxBuildHeight);

        snapshots.put(key, snapshot);

        this.wakeWaitingRays(key);
    }

    private void wakeWaitingRays(SectionKey key) {
        ConcurrentLinkedQueue<RayTask> waiters = waitingRoom.remove(key);

        if(waiters != null && !waiters.isEmpty()) rayQueue.addAll(waiters);
    }

    private void checkCollectCompletion() {
        if(!collectFinished && remainingRays.get() <= 0) {
            collectFinished = true;
        }
    }

    @Override
    public void destructionTick(int timeBudgetMs) {
        if(cancelled || destroyFinished) return;

        this.checkCollectCompletion();

        if(!collectFinished) return;

        long deadline = System.nanoTime() + Math.max(1, timeBudgetMs) * 1_000_000L;

        if(!chunksOrdered) this.orderChunksForDestruction();

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        while(!cancelled && System.nanoTime() < deadline && !orderedChunks.isEmpty()) {

            long chunkKey = orderedChunks.peekFirst();

            ConcurrentBitSet bitSet = destructionMap.get(chunkKey);

            if(bitSet == null || bitSet.isEmpty()) {
                orderedChunks.pollFirst();
                destructionMap.remove(chunkKey);
                this.cleanupSnapshotsForChunk(chunkKey);
                continue;
            }

            boolean chunkComplete = this.processChunkDestruction(chunkKey, bitSet, mutablePos, deadline);

            if(chunkComplete) {
                orderedChunks.pollFirst();
                destructionMap.remove(chunkKey);
                this.cleanupSnapshotsForChunk(chunkKey);
            }
        }

        if(orderedChunks.isEmpty() && destructionMap.isEmpty()) {
            destroyFinished = true;
            this.cleanupAfterFinish();
        }
    }

    private void orderChunksForDestruction() {
        List<Long> chunks = new ArrayList<>(destructionMap.keySet());

        chunks.sort(this::compareChunkKeys);

        orderedChunks.clear();
        orderedChunks.addAll(chunks);

        chunksOrdered = true;
    }

    private boolean processChunkDestruction(long chunkKey, ConcurrentBitSet bitSet, BlockPos.MutableBlockPos mutablePos, long deadline) {
        int chunkX = chunkKeyX(chunkKey);
        int chunkZ = chunkKeyZ(chunkKey);

        int bit = bitSet.nextSetBit(0);

        while(bit >= 0 && System.nanoTime() < deadline) {
            int y = minBuildHeight + (bit >>> 8);
            int x = (chunkX << 4) | ((bit >>> 4) & 15);
            int z = (chunkZ << 4) | (bit & 15);

            if(y >= minBuildHeight && y < maxBuildHeight) {
                mutablePos.set(x, y, z);

                BlockState state = level.getBlockState(mutablePos);

                boolean hasFluid = !state.getFluidState().isEmpty();

                if(!state.isAir() || hasFluid) {
                    level.setBlock(mutablePos, Blocks.AIR.defaultBlockState(), 3);

                    // todo make config val for this
                    if(hasFluid) {
                        clearFluidAcrossChunkBorder(x, y, z, mutablePos);
                    }
                }
            }

            bitSet.clear(bit);
            bit = bitSet.nextSetBit(bit + 1);
        }

        return bitSet.isEmpty();
    }

    private void clearFluidAcrossChunkBorder(int x, int y, int z, BlockPos.MutableBlockPos mutablePos) {
        if((x & 15) == 0) this.clearFluidIfPresent(x - 1, y, z, mutablePos);
        if((x & 15) == 15) this.clearFluidIfPresent(x + 1, y, z, mutablePos);
        if((z & 15) == 0) this.clearFluidIfPresent(x, y, z - 1, mutablePos);
        if((z & 15) == 15) this.clearFluidIfPresent(x, y, z + 1, mutablePos);
    }

    private void clearFluidIfPresent(int x, int y, int z, BlockPos.MutableBlockPos mutablePos) {
        if(y < minBuildHeight || y >= maxBuildHeight) return;

        mutablePos.set(x, y, z);

        BlockState neighborState = level.getBlockState(mutablePos);

        if(!neighborState.getFluidState().isEmpty()) {
            level.setBlock(mutablePos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    private void cleanupSnapshotsForChunk(long chunkKey) {
        int chunkX = chunkKeyX(chunkKey);
        int chunkZ = chunkKeyZ(chunkKey);

        int minSectionY = floorDiv(minBuildHeight, 16);
        int maxSectionY = floorDiv(maxBuildHeight - 1, 16);

        for(int sectionY = minSectionY; sectionY <= maxSectionY; sectionY++) {
            snapshots.remove(new SectionKey(chunkX, chunkZ, sectionY));
        }
    }

    private void cleanupAfterFinish() {
        snapshots.clear();
        waitingRoom.clear();
        highPrioritySectionQueue.clear();
        rayQueue.clear();
        workerFutures.clear();
    }

    @Override public boolean isComplete() { return collectFinished && destroyFinished; }

    @Override
    public void cancel() {
        cancelled = true;
        collectFinished = true;
        destroyFinished = true;

        rayQueue.clear();
        waitingRoom.clear();
        highPrioritySectionQueue.clear();
        destructionMap.clear();
        orderedChunks.clear();
        snapshots.clear();

        for(Future<?> future : workerFutures) {
            future.cancel(true);
        }

        workerFutures.clear();
    }

    private final class Worker implements Runnable {

        @Override
        public void run() {
            try {
                while(!cancelled && !collectFinished && !Thread.currentThread().isInterrupted()) {
                    RayTask task = rayQueue.poll(100L, TimeUnit.MILLISECONDS);

                    if(task != null) task.trace();

                    if(remainingRays.get() <= 0) {
                        collectFinished = true;
                        break;
                    }
                }
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private final class RayTask {

        private final int directionIndex;

        private boolean initialized = false;
        private boolean finished = false;

        private double px;
        private double py;
        private double pz;

        private int x;
        private int y;
        private int z;

        private float energy;

        private double tMaxX;
        private double tMaxY;
        private double tMaxZ;

        private double tDeltaX;
        private double tDeltaY;
        private double tDeltaZ;

        private int stepX;
        private int stepY;
        private int stepZ;

        private double currentRayPosition;

        private int lastChunkX = Integer.MIN_VALUE;
        private int lastChunkZ = Integer.MIN_VALUE;
        private int lastSectionY = Integer.MIN_VALUE;

        private SectionKey currentSectionKey;

        RayTask(int directionIndex) {
            this.directionIndex = directionIndex;
        }

        private void init() {
            RayDirection dir = directions.get(directionIndex);

            this.energy = strength * INITIAL_ENERGY_FACTOR;

            this.px = explosionX;
            this.py = explosionY;
            this.pz = explosionZ;

            this.x = originX;
            this.y = originY;
            this.z = originZ;

            this.currentRayPosition = 0.0D;

            double dirX = dir.x;
            double dirY = dir.y;
            double dirZ = dir.z;

            double absDirX = Math.abs(dirX);
            this.stepX = absDirX < RAY_DIRECTION_EPSILON ? 0 : dirX > 0.0D ? 1 : -1;
            this.tDeltaX = stepX == 0 ? Double.POSITIVE_INFINITY : 1.0D / absDirX;
            this.tMaxX = stepX == 0 ? Double.POSITIVE_INFINITY : (stepX > 0 ? x + 1.0D - px : px - x) * tDeltaX;

            double absDirY = Math.abs(dirY);
            this.stepY = absDirY < RAY_DIRECTION_EPSILON ? 0 : dirY > 0.0D ? 1 : -1;
            this.tDeltaY = stepY == 0 ? Double.POSITIVE_INFINITY : 1.0D / absDirY;
            this.tMaxY = stepY == 0 ? Double.POSITIVE_INFINITY : (stepY > 0 ? y + 1.0D - py : py - y) * tDeltaY;

            double absDirZ = Math.abs(dirZ);
            this.stepZ = absDirZ < RAY_DIRECTION_EPSILON ? 0 : dirZ > 0.0D ? 1 : -1;
            this.tDeltaZ = stepZ == 0 ? Double.POSITIVE_INFINITY : 1.0D / absDirZ;
            this.tMaxZ = stepZ == 0 ? Double.POSITIVE_INFINITY : (stepZ > 0 ? z + 1.0D - pz : pz - z) * tDeltaZ;

            this.initialized = true;
        }

        void trace() {
            if(finished || cancelled) return;

            if(!initialized) init();

            while(!cancelled && energy > 0.0F) {
                if(Thread.currentThread().isInterrupted()) return;

                if(y < minBuildHeight || y >= maxBuildHeight) {
                    finish();
                    return;
                }

                if(currentRayPosition >= radius - PROCESSING_EPSILON) {
                    finish();
                    return;
                }

                int chunkX = x >> 4;
                int chunkZ = z >> 4;
                int sectionY = floorDiv(y, 16);

                if(chunkX != lastChunkX || chunkZ != lastChunkZ || sectionY != lastSectionY) {
                    currentSectionKey = new SectionKey(chunkX, chunkZ, sectionY);

                    lastChunkX = chunkX;
                    lastChunkZ = chunkZ;
                    lastSectionY = sectionY;
                }

                SectionSnapshot snapshot = snapshots.get(currentSectionKey);

                if(snapshot == null) {
                    parkUntilSnapshotReady(currentSectionKey);
                    return;
                }

                double tExitVoxel = Math.min(tMaxX, Math.min(tMaxY, tMaxZ));
                double segmentLenInVoxel = tExitVoxel - currentRayPosition;

                double segmentLenForProcessing;
                boolean stopAfterThisSegment = false;

                if(currentRayPosition + segmentLenInVoxel > radius - PROCESSING_EPSILON) {
                    segmentLenForProcessing = Math.max(0.0D, radius - currentRayPosition);
                    stopAfterThisSegment = true;
                } else {
                    segmentLenForProcessing = segmentLenInVoxel;
                }

                if(!snapshot.empty && segmentLenForProcessing > PROCESSING_EPSILON) {
                    processCurrentVoxel(snapshot, segmentLenForProcessing);
                }

                currentRayPosition = tExitVoxel;

                if(energy <= 0.0F || stopAfterThisSegment) {
                    finish();
                    return;
                }

                stepToNextVoxel();
            }

            finish();
        }

        private void processCurrentVoxel(SectionSnapshot snapshot, double segmentLenForProcessing) {
            int localX = x & 15;
            int localY = y & 15;
            int localZ = z & 15;

            if(snapshot.isEmpty(localX, localY, localZ)) return;

            float resistance = snapshot.getResistance(localX, localY, localZ);

            if(resistance >= NUKE_RESISTANCE_CUTOFF) {
                energy = 0.0F;
                return;
            }

            double energyLossFactor = getEnergyLossFactor(resistance);
            float damageDealt = (float) (energyLossFactor * segmentLenForProcessing);

            energy -= damageDealt;

            if(damageDealt > 0.0F && energy > 0.0F) {
                markBlockForDestruction(x, y, z);
            }
        }

        private void markBlockForDestruction(int x, int y, int z) {
            if(y < minBuildHeight || y >= maxBuildHeight) return;

            long chunkKey = chunkKey(x >> 4, z >> 4);
            int bitIndex = blockBitIndex(x, y, z);

            destructionMap.computeIfAbsent(chunkKey, ignored -> new ConcurrentBitSet(bitsetSize)).set(bitIndex);
        }

        private double getEnergyLossFactor(float resistance) {
            double effectiveDist = Math.max(currentRayPosition, MIN_EFFECTIVE_DIST_FOR_ENERGY_CALC);

            return Math.pow(resistance + 1.0D, 3.0D * (effectiveDist / radius)) - 1.0D;
        }

        private void stepToNextVoxel() {
            if(tMaxX < tMaxY) {
                if(tMaxX < tMaxZ) {
                    x += stepX;
                    tMaxX += tDeltaX;
                } else {
                    z += stepZ;
                    tMaxZ += tDeltaZ;
                }
            } else {
                if(tMaxY < tMaxZ) {
                    y += stepY;
                    tMaxY += tDeltaY;
                } else {
                    z += stepZ;
                    tMaxZ += tDeltaZ;
                }
            }
        }

        private void parkUntilSnapshotReady(SectionKey key) {
            ConcurrentLinkedQueue<RayTask> waiters = waitingRoom.computeIfAbsent(key, k -> {
                highPrioritySectionQueue.offer(k);
                return new ConcurrentLinkedQueue<>();
            });

            waiters.add(this);

            if(snapshots.containsKey(key)) {
                ConcurrentLinkedQueue<RayTask> queue = waitingRoom.remove(key);

                if(queue != null) rayQueue.addAll(queue);
            }
        }

        private void finish() {
            if(finished) return;
            finished = true;
            if(remainingRays.decrementAndGet() <= 0) collectFinished = true;
        }
    }

    private static class SectionSnapshot {

        private static final byte FLAG_AIR = 1;
        private static final byte FLAG_HAS_FLUID = 2;

        static final SectionSnapshot EMPTY = new SectionSnapshot(true, null, null);

        final boolean empty;
        final byte[] flags;
        final float[] resistance;

        private SectionSnapshot(boolean empty, byte[] flags, float[] resistance) {
            this.empty = empty;
            this.flags = flags;
            this.resistance = resistance;
        }

        private static SectionSnapshot create(Level level, SectionKey key, int minBuildHeight, int maxBuildHeight) {
            byte[] flags = new byte[4096];
            float[] resistance = new float[4096];

            boolean allEmpty = true;

            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            int baseX = key.chunkX << 4;
            int baseY = key.sectionY << 4;
            int baseZ = key.chunkZ << 4;

            for(int localY = 0; localY < 16; localY++) {
                int y = baseY + localY;

                if(y < minBuildHeight || y >= maxBuildHeight) continue;

                for(int localZ = 0; localZ < 16; localZ++) {
                    int z = baseZ + localZ;

                    for(int localX = 0; localX < 16; localX++) {
                        int x = baseX + localX;

                        mutablePos.set(x, y, z);

                        BlockState state = level.getBlockState(mutablePos);

                        boolean air = state.isAir();
                        boolean hasFluid = !state.getFluidState().isEmpty();

                        boolean empty = air && !hasFluid;

                        byte f = 0;

                        if(air) f |= FLAG_AIR;
                        if(hasFluid) f |= FLAG_HAS_FLUID;

                        int index = index(localX, localY, localZ);
                        flags[index] = f;

                        if(!empty) {
                            allEmpty = false;

                            resistance[index] = masqueradeResistance(level, state, mutablePos);
                        }
                    }
                }
            }

            if(allEmpty) return EMPTY;

            return new SectionSnapshot(false, flags, resistance);
        }

        boolean isEmpty(int localX, int localY, int localZ) {
            if(empty) return true;

            int index = index(localX, localY, localZ);

            boolean air = (flags[index] & FLAG_AIR) != 0;
            boolean hasFluid = (flags[index] & FLAG_HAS_FLUID) != 0;

            return air && !hasFluid;
        }


        float getResistance(int localX, int localY, int localZ) {
            if(empty) {
                return 0.0F;
            }

            return resistance[index(localX, localY, localZ)];
        }

        private static int index(int localX, int localY, int localZ) {
            return (localY << 8) | (localZ << 4) | localX;
        }
    }

    private static final class SectionKey {

        final int chunkX;
        final int chunkZ;
        final int sectionY;

        SectionKey(int chunkX, int chunkZ, int sectionY) {
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.sectionY = sectionY;
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj) return true;
            if(!(obj instanceof SectionKey other)) return false;
            return this.chunkX == other.chunkX && this.chunkZ == other.chunkZ && this.sectionY == other.sectionY;
        }

        @Override
        public int hashCode() {
            int result = Integer.hashCode(chunkX);
            result = 31 * result + Integer.hashCode(chunkZ);
            result = 31 * result + Integer.hashCode(sectionY);
            return result;
        }
    }

    private static final class RayDirection {

        final double x;
        final double y;
        final double z;

        RayDirection(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private static final class ConcurrentBitSet {

        private final AtomicLongArray words;
        private final int size;

        ConcurrentBitSet(int size) {
            this.size = size;
            int wordCount = (size + 63) >>> 6;
            this.words = new AtomicLongArray(wordCount);
        }

        void set(int bitIndex) {
            if(bitIndex < 0 || bitIndex >= size) return;

            int wordIndex = bitIndex >>> 6;
            long mask = 1L << bitIndex;

            while(true) {
                long oldValue = words.get(wordIndex);
                long newValue = oldValue | mask;

                if(oldValue == newValue || words.compareAndSet(wordIndex, oldValue, newValue)) return;
            }
        }

        void clear(int bitIndex) {
            if(bitIndex < 0 || bitIndex >= size) return;

            int wordIndex = bitIndex >>> 6;
            long mask = ~(1L << bitIndex);

            while(true) {
                long oldValue = words.get(wordIndex);
                long newValue = oldValue & mask;

                if(oldValue == newValue || words.compareAndSet(wordIndex, oldValue, newValue)) return;
            }
        }

        int nextSetBit(int fromIndex) {
            if(fromIndex < 0) fromIndex = 0;

            if(fromIndex >= size) return -1;

            int wordIndex = fromIndex >>> 6;
            int bitOffset = fromIndex & 63;

            long word = words.get(wordIndex) & (-1L << bitOffset);

            while(true) {
                if(word != 0L) {
                    int result = (wordIndex << 6) + Long.numberOfTrailingZeros(word);
                    return result < size ? result : -1;
                }

                wordIndex++;

                if(wordIndex >= words.length()) {
                    return -1;
                }

                word = words.get(wordIndex);
            }
        }

        boolean isEmpty() {
            for(int i = 0; i < words.length(); i++) {
                if(words.get(i) != 0L) {
                    return false;
                }
            }

            return true;
        }
    }

    private List<SectionKey> getAllPotentialSections() {
        List<SectionKey> keys = new ArrayList<>();

        int chunkRadius = (radius + 15) >> 4;

        int originChunkX = originX >> 4;
        int originChunkZ = originZ >> 4;
        int originSectionY = floorDiv(originY, 16);

        int minChunkX = originChunkX - chunkRadius;
        int maxChunkX = originChunkX + chunkRadius;

        int minChunkZ = originChunkZ - chunkRadius;
        int maxChunkZ = originChunkZ + chunkRadius;

        int minY = Math.max(minBuildHeight, originY - radius);
        int maxY = Math.min(maxBuildHeight - 1, originY + radius);

        int minSectionY = floorDiv(minY, 16);
        int maxSectionY = floorDiv(maxY, 16);

        double maxDist = radius + 14.0D;
        double maxDistSq = maxDist * maxDist;

        for(int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for(int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                for(int sectionY = minSectionY; sectionY <= maxSectionY; sectionY++) {
                    int centerX = (chunkX << 4) + 8;
                    int centerY = (sectionY << 4) + 8;
                    int centerZ = (chunkZ << 4) + 8;

                    double dx = centerX - explosionX;
                    double dy = centerY - explosionY;
                    double dz = centerZ - explosionZ;

                    if(dx * dx + dy * dy + dz * dz <= maxDistSq) keys.add(new SectionKey(chunkX, chunkZ, sectionY));
                }
            }
        }

        keys.sort(Comparator.comparingInt(key -> {
            int dx = key.chunkX - originChunkX;
            int dy = key.sectionY - originSectionY;
            int dz = key.chunkZ - originChunkZ;

            return dx * dx + dy * dy + dz * dz;
        }));

        return keys;
    }

    private List<RayDirection> generateSphereRays(int count) {
        List<RayDirection> result = new ArrayList<>(count);

        if(count <= 0) return result;

        if(count == 1) {
            result.add(new RayDirection(1.0D, 0.0D, 0.0D));
            return result;
        }

        double phi = Math.PI * (3.0D - Math.sqrt(5.0D));

        for(int i = 0; i < count; i++) {
            double y = 1.0D - (i / (double) (count - 1)) * 2.0D;
            double r = Math.sqrt(1.0D - y * y);
            double t = phi * i;

            double x = Math.cos(t) * r;
            double z = Math.sin(t) * r;

            result.add(new RayDirection(x, y, z));
        }

        return result;
    }

    public static float masqueradeResistance(Level level, BlockState state, BlockPos pos) {
        if(!state.getFluidState().isEmpty()) return 0.1F;

        Block block = state.getBlock();

        if(block == Blocks.SANDSTONE) return Blocks.STONE.defaultBlockState().getExplosionResistance(level, pos, null);
        if(block == Blocks.OBSIDIAN) return Blocks.STONE.defaultBlockState().getExplosionResistance(level, pos, null) * 3.0F;

        return state.getExplosionResistance(level, pos, null);
    }

    private int blockBitIndex(int x, int y, int z) {
        int localY = y - minBuildHeight;

        return (localY << 8) | ((x & 15) << 4) | (z & 15);
    }

    private int compareChunkKeys(long key1, long key2) {
        int centerChunkX = originX >> 4;
        int centerChunkZ = originZ >> 4;

        int x1 = chunkKeyX(key1);
        int z1 = chunkKeyZ(key1);

        int x2 = chunkKeyX(key2);
        int z2 = chunkKeyZ(key2);

        int dist1 = Math.abs(centerChunkX - x1) + Math.abs(centerChunkZ - z1);
        int dist2 = Math.abs(centerChunkX - x2) + Math.abs(centerChunkZ - z2);

        return Integer.compare(dist1, dist2);
    }

    private static long chunkKey(int chunkX, int chunkZ) {
        return ((long) chunkX & 0xffffffffL) | (((long) chunkZ & 0xffffffffL) << 32);
    }

    private static int chunkKeyX(long key) {
        return (int) key;
    }

    private static int chunkKeyZ(long key) {
        return (int) (key >> 32);
    }

    private static int floorDiv(int value, int divisor) {
        return Math.floorDiv(value, divisor);
    }
}