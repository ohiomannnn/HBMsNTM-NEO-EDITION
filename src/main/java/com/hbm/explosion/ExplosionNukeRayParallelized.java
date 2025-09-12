package com.hbm.explosion;

import com.hbm.HBMsNTM;
import com.hbm.config.ServerConfig;
import com.hbm.interfaces.IExplosionRay;
import com.hbm.util.ConcurrentBitSet;
import com.hbm.util.SubChunkKey;
import com.hbm.util.SubChunkSnapshot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.DoubleAdder;


/**
 * idk why did i port this class. for future ig
 */
public class ExplosionNukeRayParallelized implements IExplosionRay {

    private static final int WORLD_HEIGHT = 256;
    private static final int BITSET_SIZE = 16 * WORLD_HEIGHT * 16;
    private static final int SUBCHUNK_PER_CHUNK = WORLD_HEIGHT >> 4;
    private static final float NUKE_RESISTANCE_CUTOFF = 2_000_000F;
    private static final float INITIAL_ENERGY_FACTOR = 0.3F;
    private static final double RESOLUTION_FACTOR = 1.0;

    protected final Level level;
    private final double explosionX, explosionY, explosionZ;
    private final int originX, originY, originZ;
    private final int strength;
    private final int radius;

    private final CompletableFuture<List<Vec3>> directionsFuture;
    private final ConcurrentMap<ChunkPos, ConcurrentBitSet> destructionMap;
    private final ConcurrentMap<ChunkPos, ConcurrentMap<Integer, DoubleAdder>> damageMap;
    private final ConcurrentMap<SubChunkKey, SubChunkSnapshot> snapshots;
    private final ConcurrentMap<SubChunkKey, ConcurrentLinkedQueue<RayTask>> waitingRoom;
    private final BlockingQueue<RayTask> rayQueue;
    private final ExecutorService pool;
    private final CountDownLatch latch;
    private final Thread latchWatcherThread;
    private final List<ChunkPos> orderedChunks;
    private final BlockingQueue<SubChunkKey> highPriorityReactiveQueue;
    private final Iterator<SubChunkKey> lowPriorityProactiveIterator;
    private volatile List<Vec3> directions;
    private volatile boolean collectFinished = false;
    private volatile boolean consolidationFinished = false;
    private volatile boolean destroyFinished = false;

    public ExplosionNukeRayParallelized(Level level, double x, double y, double z, int strength, int speed, int radius) {
        this.level = level;
        this.explosionX = x;
        this.explosionY = y;
        this.explosionZ = z;

        this.originX = (int) Math.floor(x);
        this.originY = (int) Math.floor(y);
        this.originZ = (int) Math.floor(z);

        this.strength = strength;
        this.radius = radius;

        int rayCount = Math.max(0, (int) (2.5 * Math.PI * strength * strength * RESOLUTION_FACTOR));
        this.latch = new CountDownLatch(rayCount);
        List<SubChunkKey> sortedSubChunks = getAllSubChunks();
        this.lowPriorityProactiveIterator = sortedSubChunks.iterator();
        this.highPriorityReactiveQueue = new LinkedBlockingQueue<>();

        int initialChunkCapacity = (int) sortedSubChunks.stream().map(SubChunkKey::getPos).distinct().count();

        this.destructionMap = new ConcurrentHashMap<>(initialChunkCapacity);
        this.damageMap = new ConcurrentHashMap<>(initialChunkCapacity);

        int subChunkCount = sortedSubChunks.size();
        this.snapshots = new ConcurrentHashMap<>(subChunkCount);
        this.waitingRoom = new ConcurrentHashMap<>(subChunkCount);
        this.orderedChunks = new ArrayList<>();

        List<RayTask> initialRayTasks = new ArrayList<>(rayCount);
        for (int i = 0; i < rayCount; i++) initialRayTasks.add(new RayTask(i));
        this.rayQueue = new LinkedBlockingQueue<>(initialRayTasks);

        int workers = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
        this.pool = Executors.newWorkStealingPool(workers);
        this.directionsFuture = CompletableFuture.supplyAsync(() -> generateSphereRays(rayCount));

        for (int i = 0; i < workers; i++) pool.submit(new Worker());

        this.latchWatcherThread = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                collectFinished = true;
                if (ServerConfig.EXPLOSION_ALGORITHM.getAsInt() == 2) pool.submit(this::runConsolidation);
                else consolidationFinished = true;
            }
        }, "ExplosionNuke-LatchWatcher-" + System.nanoTime());
        this.latchWatcherThread.setDaemon(true);
        this.latchWatcherThread.start();
    }

    private static float getNukeResistance(Block b) {
        try {
            if (b.defaultBlockState().getFluidState().isEmpty()) return 0.1F;
        } catch (Throwable ignored) {}
        if (b == Blocks.SANDSTONE) return Blocks.STONE.getExplosionResistance();
        if (b == Blocks.OBSIDIAN) return Blocks.STONE.getExplosionResistance() * 3.0F;
        return b.getExplosionResistance();
    }

    private List<SubChunkKey> getAllSubChunks() {
        List<SubChunkKey> keys = new ArrayList<>();
        int cr = (radius + 15) >> 4;
        int minCX = (originX >> 4) - cr;
        int maxCX = (originX >> 4) + cr;
        int minCZ = (originZ >> 4) - cr;
        int maxCZ = (originZ >> 4) + cr;
        int minSubY = Math.max(0, (originY - radius) >> 4);
        int maxSubY = Math.min(SUBCHUNK_PER_CHUNK - 1, (originY + radius) >> 4);
        int originSubY = originY >> 4;

        for (int cx = minCX; cx <= maxCX; cx++) {
            for (int cz = minCZ; cz <= maxCZ; cz++) {
                for (int subY = minSubY; subY <= maxSubY; subY++) {
                    int chunkCenterX = (cx << 4) + 8;
                    int chunkCenterY = (subY << 4) + 8;
                    int chunkCenterZ = (cz << 4) + 8;
                    double dx = chunkCenterX - explosionX;
                    double dy = chunkCenterY - explosionY;
                    double dz = chunkCenterZ - explosionZ;
                    if (dx * dx + dy * dy + dz * dz <= (radius + 14) * (radius + 14)) {
                        keys.add(new SubChunkKey(cx, cz, subY));
                    }
                }
            }
        }
        keys.sort(Comparator.comparingInt(key -> {
            int distCX = key.getPos().x - (originX >> 4);
            int distCZ = key.getPos().z - (originZ >> 4);
            int distSubY = key.getSubY() - originSubY;
            return distCX * distCX + distCZ * distCZ + distSubY * distSubY;
        }));
        return keys;
    }

    @Override
    public void cacheChunksTick(int timeBudgetMs) {
        if (collectFinished) return;
        final long deadline = System.nanoTime() + (timeBudgetMs * 1_000_000L);
        while (System.nanoTime() < deadline) {
            SubChunkKey ck = highPriorityReactiveQueue.poll();
            if (ck == null) break;
            processCacheKey(ck);
        }
        while (System.nanoTime() < deadline && lowPriorityProactiveIterator.hasNext()) {
            SubChunkKey ck = lowPriorityProactiveIterator.next();
            processCacheKey(ck);
        }
    }

    private void processCacheKey(SubChunkKey ck) {
        if (snapshots.containsKey(ck)) return;
        snapshots.put(ck, SubChunkSnapshot.getSnapshot(level, ck, ServerConfig.ENABLE_CHUNK_LOADING.getAsBoolean()));
        ConcurrentLinkedQueue<RayTask> waiters = waitingRoom.remove(ck);
        if (waiters != null) rayQueue.addAll(waiters);
    }

    @Override
    public void destructionTick(int timeBudgetMs) {
        if (!collectFinished || !consolidationFinished || destroyFinished) return;

        final long deadline = System.nanoTime() + timeBudgetMs * 1_000_000L;

        if (orderedChunks.isEmpty() && !destructionMap.isEmpty()) {
            orderedChunks.addAll(destructionMap.keySet());
            orderedChunks.sort(Comparator.comparingInt(c ->
                    Math.abs((originX >> 4) - c.x) + Math.abs((originZ >> 4) - c.z)));
        }

        Iterator<ChunkPos> it = orderedChunks.iterator();
        while (it.hasNext() && System.nanoTime() < deadline) {
            ChunkPos cp = it.next();
            ConcurrentBitSet bs = destructionMap.get(cp);
            if (bs == null) {
                it.remove();
                continue;
            }

            LevelChunk chunk;
            try {
                chunk = level.getChunk(cp.x, cp.z);
            } catch (Throwable t) {
                chunk = level.getChunkSource().getChunkNow(cp.x, cp.z);
                if (chunk == null) {
                    HBMsNTM.LOGGER.warn("Cannot access chunk {} for explosion destruction.", cp);
                    continue;
                }
            }
            LevelChunkSection[] sections;
            try {
                sections = chunk.getSections();
            } catch (Throwable t) {
                sections = new LevelChunkSection[SUBCHUNK_PER_CHUNK];
            }

            boolean chunkModified = false;

            for (int subY = 0; subY < sections.length; subY++) {
                LevelChunkSection section = sections[subY];
                if (section == null) continue;

                int startBit = (WORLD_HEIGHT - 1 - ((subY << 4) + 15)) << 8;
                int endBit = ((WORLD_HEIGHT - 1 - (subY << 4)) << 8) | 0xFF;

                int bit = bs.nextSetBit(startBit);

                while (bit >= 0 && bit <= endBit && System.nanoTime() < deadline) {
                    int yGlobal = WORLD_HEIGHT - 1 - (bit >>> 8);
                    int xGlobal = (cp.x << 4) | ((bit >>> 4) & 0xF);
                    int zGlobal = (cp.z << 4) | (bit & 0xF);
                    int xLocal = xGlobal & 0xF;
                    int yLocal = yGlobal & 0xF;
                    int zLocal = zGlobal & 0xF;

                    BlockPos pos = new BlockPos(xGlobal, yGlobal, zGlobal);

                    boolean notAir;
                    try {
                        notAir = !chunk.getBlockState(pos).isAir();
                    } catch (Throwable t) {
                        notAir = true;
                    }

                    if (notAir) {
                        try {
                            if (level.getBlockEntity(pos) != null) {
                                level.removeBlockEntity(pos);
                            }
                        } catch (Throwable ignored) {}

                        try {
                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        } catch (Throwable t) {
                            HBMsNTM.LOGGER.warn("Failed to set block to air at {} : {}", pos, t.getMessage());
                        }

                        chunkModified = true;

                        try {
                            level.updateNeighborsAt(pos, Blocks.AIR);
                            level.sendBlockUpdated(pos, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), 3);
                        } catch (Throwable ignored) {}

                        try {
                            level.getChunkSource().getLightEngine().checkBlock(pos);
                        } catch (Throwable ignored) {}
                    }
                    bs.clear(bit);
                    bit = bs.nextSetBit(bit + 1);
                }
            }

            if (chunkModified) {
                try {
                    chunk.setUnsaved(true);
                } catch (Throwable ignored) {}

                try {
                    level.setBlocksDirty(
                            new BlockPos(cp.x << 4, 0, cp.z << 4),
                            Blocks.AIR.defaultBlockState(),
                            Blocks.AIR.defaultBlockState()
                    );
                } catch (Throwable ignored) {}
            }
            if (bs.isEmpty()) {
                destructionMap.remove(cp);
                for (int subY = 0; subY < SUBCHUNK_PER_CHUNK; subY++)
                    snapshots.remove(new SubChunkKey(cp, subY));
                it.remove();
            }
        }

        if (orderedChunks.isEmpty() && destructionMap.isEmpty()) {
            destroyFinished = true;
            if (pool != null) pool.shutdown();
        }
    }

    @Override
    public boolean isComplete() {
        return collectFinished && consolidationFinished && destroyFinished;
    }

    @Override
    public void cancel() {
        this.collectFinished = true;
        this.consolidationFinished = true;
        this.destroyFinished = true;

        if (this.rayQueue != null) this.rayQueue.clear();
        if (this.waitingRoom != null) this.waitingRoom.clear();

        if (this.latch != null) while (this.latch.getCount() > 0) this.latch.countDown();
        if (this.latchWatcherThread != null && this.latchWatcherThread.isAlive()) this.latchWatcherThread.interrupt();

        if (this.pool != null && !this.pool.isShutdown()) {
            this.pool.shutdownNow();
            try {
                if (!this.pool.awaitTermination(100, TimeUnit.MILLISECONDS))
                    HBMsNTM.LOGGER.error("ExplosionNukeRayParallelized thread pool did not terminate promptly on cancel.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                if (!this.pool.isShutdown()) this.pool.shutdownNow();
            }
        }
        if (this.destructionMap != null) this.destructionMap.clear();
        if (this.damageMap != null) this.damageMap.clear();
        if (this.snapshots != null) this.snapshots.clear();
        if (this.orderedChunks != null) this.orderedChunks.clear();
    }

    private List<Vec3> generateSphereRays(int count) {
        List<Vec3> list = new ArrayList<>(count);
        if (count == 0) return list;
        if (count == 1) {
            list.add(new Vec3(1, 0, 0));
            return list;
        }
        double phi = Math.PI * (3.0 - Math.sqrt(5.0));
        for (int i = 0; i < count; i++) {
            double y = 1.0 - (i / (double) (count - 1)) * 2.0;
            double r = Math.sqrt(1.0 - y * y);
            double t = phi * i;
            list.add(new Vec3(Math.cos(t) * r, y, Math.sin(t) * r));
        }
        return list;
    }

    private void runConsolidation() {
        damageMap.forEach((cp, innerDamageMap) -> {
            if (innerDamageMap.isEmpty()) {
                damageMap.remove(cp);
                return;
            }
            ConcurrentBitSet chunkDestructionBitSet = destructionMap.computeIfAbsent(cp, k -> new ConcurrentBitSet(BITSET_SIZE));
            innerDamageMap.forEach((bitIndex, accumulatedDamageAdder) -> {
                float accumulatedDamage = (float) accumulatedDamageAdder.sum();
                if (accumulatedDamage <= 0.0f) {
                    innerDamageMap.remove(bitIndex);
                    return;
                }
                int yGlobal = WORLD_HEIGHT - 1 - (bitIndex >>> 8);
                int subY = yGlobal >> 4;
                if (subY < 0) {
                    innerDamageMap.remove(bitIndex);
                    return;
                }
                SubChunkKey snapshotKey = new SubChunkKey(cp, subY);
                SubChunkSnapshot snap = snapshots.get(snapshotKey);
                if (snap == null || snap == SubChunkSnapshot.EMPTY) {
                    innerDamageMap.remove(bitIndex);
                    return;
                }
                int xLocal = (bitIndex >>> 4) & 0xF;
                int zLocal = bitIndex & 0xF;
                Block originalBlock = snap.getBlock(xLocal, yGlobal & 0xF, zLocal);
                if (originalBlock == Blocks.AIR) {
                    innerDamageMap.remove(bitIndex);
                    return;
                }
                float resistance = getNukeResistance(originalBlock);
                if (accumulatedDamage >= resistance * RESOLUTION_FACTOR) chunkDestructionBitSet.set(bitIndex);
                innerDamageMap.remove(bitIndex);
            });
            if (innerDamageMap.isEmpty()) damageMap.remove(cp);
        });
        damageMap.clear();
        consolidationFinished = true;
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            try {
                while (!collectFinished && !Thread.currentThread().isInterrupted()) {
                    RayTask task = rayQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (task != null) task.trace();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private class RayTask {
        private static final double RAY_DIRECTION_EPSILON = 1e-6;
        private static final double PROCESSING_EPSILON = 1e-9;
        private static final float MIN_EFFECTIVE_DIST_FOR_ENERGY_CALC = 0.01f;

        final int dirIndex;
        double px, py, pz;
        int x, y, z;
        float energy;
        double tMaxX, tMaxY, tMaxZ, tDeltaX, tDeltaY, tDeltaZ;
        int stepX, stepY, stepZ;
        boolean initialised = false;
        double currentRayPosition;

        private int lastCX = Integer.MIN_VALUE, lastCZ = Integer.MIN_VALUE, lastSubY = Integer.MIN_VALUE;
        private SubChunkKey currentSubChunkKey = null;

        RayTask(int dirIdx) {
            this.dirIndex = dirIdx;
        }

        void init() {
            if (directions == null) directions = directionsFuture.join();
            Vec3 dir = directions.get(this.dirIndex);
            this.energy = strength * INITIAL_ENERGY_FACTOR;
            this.px = explosionX;
            this.py = explosionY;
            this.pz = explosionZ;
            this.x = originX;
            this.y = originY;
            this.z = originZ;
            this.currentRayPosition = 0.0;

            double dirX = dir.x;
            double dirY = dir.y;
            double dirZ = dir.z;

            double absDirX = Math.abs(dirX);
            this.stepX = (absDirX < RAY_DIRECTION_EPSILON) ? 0 : (dirX > 0 ? 1 : -1);
            this.tDeltaX = (stepX == 0) ? Double.POSITIVE_INFINITY : 1.0 / absDirX;
            this.tMaxX = (stepX == 0) ? Double.POSITIVE_INFINITY : ((stepX > 0 ? (this.x + 1 - this.px) : (this.px - this.x)) * this.tDeltaX);

            double absDirY = Math.abs(dirY);
            this.stepY = (absDirY < RAY_DIRECTION_EPSILON) ? 0 : (dirY > 0 ? 1 : -1);
            this.tDeltaY = (stepY == 0) ? Double.POSITIVE_INFINITY : 1.0 / absDirY;
            this.tMaxY = (stepY == 0) ? Double.POSITIVE_INFINITY : ((stepY > 0 ? (this.y + 1 - this.py) : (this.py - this.y)) * this.tDeltaY);

            double absDirZ = Math.abs(dirZ);
            this.stepZ = (absDirZ < RAY_DIRECTION_EPSILON) ? 0 : (dirZ > 0 ? 1 : -1);
            this.tDeltaZ = (stepZ == 0) ? Double.POSITIVE_INFINITY : 1.0 / absDirZ;
            this.tMaxZ = (stepZ == 0) ? Double.POSITIVE_INFINITY : ((stepZ > 0 ? (this.z + 1 - this.pz) : (this.pz - this.z)) * this.tDeltaZ);

            this.initialised = true;
        }

        void trace() {
            if (!initialised) init();
            if (energy <= 0) {
                latch.countDown();
                return;
            }

            while (energy > 0) {
                if (y < 0 || y >= WORLD_HEIGHT || Thread.currentThread().isInterrupted()) break;
                if (currentRayPosition >= radius - PROCESSING_EPSILON) break;

                int cx = x >> 4;
                int cz = z >> 4;
                int subY = y >> 4;
                if (cx != lastCX || cz != lastCZ || subY != lastSubY) {
                    currentSubChunkKey = new SubChunkKey(cx, cz, subY);
                    lastCX = cx;
                    lastCZ = cz;
                    lastSubY = subY;
                }

                SubChunkSnapshot snap = snapshots.get(currentSubChunkKey);
                if (snap == null) {
                    final boolean[] amFirst = {false};
                    ConcurrentLinkedQueue<RayTask> waiters = waitingRoom.computeIfAbsent(currentSubChunkKey, k -> {
                        amFirst[0] = true;
                        return new ConcurrentLinkedQueue<>();
                    });
                    if (amFirst[0]) highPriorityReactiveQueue.add(currentSubChunkKey);
                    waiters.add(this);
                    return;
                }
                double t_exit_voxel = Math.min(tMaxX, Math.min(tMaxY, tMaxZ));
                double segmentLenInVoxel = t_exit_voxel - this.currentRayPosition;
                double segmentLenForProcessing;
                boolean stopAfterThisSegment = false;

                if (this.currentRayPosition + segmentLenInVoxel > radius - PROCESSING_EPSILON) {
                    segmentLenForProcessing = Math.max(0.0, radius - this.currentRayPosition);
                    stopAfterThisSegment = true;
                } else segmentLenForProcessing = segmentLenInVoxel;

                if (snap != SubChunkSnapshot.EMPTY && segmentLenForProcessing > PROCESSING_EPSILON) {
                    Block block = snap.getBlock(x & 0xF, y & 0xF, z & 0xF);
                    if (block != Blocks.AIR) {
                        float resistance = getNukeResistance(block);
                        if (resistance >= NUKE_RESISTANCE_CUTOFF) {
                            energy = 0;
                        } else {
                            double energyLossFactor = getEnergyLossFactor(resistance);
                            float damageDealt = (float) (energyLossFactor * segmentLenForProcessing);
                            energy -= damageDealt;
                            if (damageDealt > 0) {
                                int bitIndex = ((WORLD_HEIGHT - 1 - y) << 8) | ((x & 0xF) << 4) | (z & 0xF);
                                ChunkPos chunkPos = currentSubChunkKey.getPos();
                                if (ServerConfig.EXPLOSION_ALGORITHM.getAsInt() == 2) {
                                    damageMap.computeIfAbsent(chunkPos, cp -> new ConcurrentHashMap<>(256))
                                            .computeIfAbsent(bitIndex, k -> new DoubleAdder()).add(damageDealt);
                                } else if (energy > 0) destructionMap.computeIfAbsent(chunkPos, posKey -> new ConcurrentBitSet(BITSET_SIZE)).set(bitIndex);
                            }
                        }
                    }
                }
                this.currentRayPosition = t_exit_voxel;
                if (energy <= 0 || stopAfterThisSegment) break;

                if (tMaxX < tMaxY) {
                    if (tMaxX < tMaxZ) {
                        x += stepX;
                        tMaxX += tDeltaX;
                    } else {
                        z += stepZ;
                        tMaxZ += tDeltaZ;
                    }
                } else {
                    if (tMaxY < tMaxZ) {
                        y += stepY;
                        tMaxY += tDeltaY;
                    } else {
                        z += stepZ;
                        tMaxZ += tDeltaZ;
                    }
                }
            }
            latch.countDown();
        }

        private double getEnergyLossFactor(float resistance) {
            double effectiveDist = Math.max(this.currentRayPosition, MIN_EFFECTIVE_DIST_FOR_ENERGY_CALC);
            return (Math.pow(resistance + 1.0, 3.0 * (effectiveDist / radius)) - 1.0);
        }
    }
}
