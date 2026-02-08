package com.hbm.entity.effect;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.FalloutBlock;
import com.hbm.config.FalloutConfigJSON;
import com.hbm.config.MainConfig;
import com.hbm.entity.item.FallingBlockEntityNT;
import com.hbm.entity.logic.ChunkloadingEntity;
import com.hbm.world.WorldUtil;
import com.hbm.world.biome.ModBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class FalloutRain extends ChunkloadingEntity {

    private boolean firstTick = true;
    private final Level level;

    private static final EntityDataAccessor<Integer> SCALE = SynchedEntityData.defineId(FalloutRain.class, EntityDataSerializers.INT);

    public FalloutRain(EntityType<?> type, Level level) {
        super(type, level);
        this.level = level;
    }

    private final Map<ResourceKey<Biome>, Holder<Biome>> biomeCache = new HashMap<>();

    private Holder<Biome> getCachedHolder(ResourceKey<Biome> key) {
        return biomeCache.computeIfAbsent(key, k -> level.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(k));
    }

    private int tickDelay = MainConfig.COMMON.FALLOUT_DELAY.get();
    private int mk5 = MainConfig.COMMON.MK5.get();

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {

            long start = System.currentTimeMillis();

            if (firstTick) {
                if (chunksToProcess.isEmpty() && outerChunksToProcess.isEmpty()) gatherChunks();

                if (MainConfig.COMMON.ENABLE_CRATER_BIOMES.get()) {
                    biomeCache.put(ModBiomes.CRATER_INNER, getCachedHolder(ModBiomes.CRATER_INNER));
                    biomeCache.put(ModBiomes.CRATER, getCachedHolder(ModBiomes.CRATER));
                    biomeCache.put(ModBiomes.CRATER_OUTER, getCachedHolder(ModBiomes.CRATER_OUTER));
                }

                firstTick = false;
            }

            if (tickDelay == 0) {
                tickDelay = MainConfig.COMMON.FALLOUT_DELAY.get();;

                while (System.currentTimeMillis() < start + mk5) {
                    if (!chunksToProcess.isEmpty()) {
                        long chunkPos = chunksToProcess.removeLast();
                        int chunkPosX = ChunkPos.getX(chunkPos);
                        int chunkPosZ = ChunkPos.getZ(chunkPos);

                        LevelChunk chunk = level.getChunk(chunkPosX, chunkPosZ);

                        for (int x = chunkPosX << 4; x < (chunkPosX << 4) + 16; x++) {
                            for (int z = chunkPosZ << 4; z < (chunkPosZ << 4) + 16; z++) {
                                double percent = Math.hypot(x - this.getX(), z - this.getZ()) * 100 / getScale();
                                stomp(x, z, percent);
                                ResourceKey<Biome> biomeKey = getBiomeChange(percent, getScale(), level.getBiome(new BlockPos(x, 64, z)).getKey());
                                if (biomeKey != null) {
                                    Holder<Biome> biomeHolder = biomeCache.get(biomeKey);
                                    if (biomeHolder != null) {
                                        WorldUtil.setBiomeColumn((ServerLevel) level, x, z, biomeHolder);
                                    }
                                }
                            }
                        }
                        WorldUtil.flushChunk((ServerLevel) level, chunk);
                    } else if (!outerChunksToProcess.isEmpty()) {
                        long chunkPos = outerChunksToProcess.removeLast();
                        int chunkPosX = ChunkPos.getX(chunkPos);
                        int chunkPosZ = ChunkPos.getZ(chunkPos);

                        LevelChunk chunk = level.getChunk(chunkPosX, chunkPosZ);

                        for (int x = chunkPosX << 4; x < (chunkPosX << 4) + 16; x++) {
                            for (int z = chunkPosZ << 4; z < (chunkPosZ << 4) + 16; z++) {
                                double distance = Math.hypot(x - this.getX(), z - this.getZ());
                                if (distance <= getScale()) {
                                    double percent = distance * 100 / getScale();
                                    stomp(x, z, percent);
                                    ResourceKey<Biome> biomeKey = getBiomeChange(percent, getScale(), level.getBiome(new BlockPos(x, 64, z)).getKey());
                                    if (biomeKey != null) {
                                        Holder<Biome> biomeHolder = biomeCache.get(biomeKey);
                                        if (biomeHolder != null) {
                                            WorldUtil.setBiomeColumn((ServerLevel) level, x, z, biomeHolder);
                                        }
                                    }
                                }
                            }
                        }
                        WorldUtil.flushChunk((ServerLevel) level, chunk);
                    } else {
                        this.discard();
                    }
                }
            }

            tickDelay--;
        }
    }

    public static ResourceKey<Biome> getBiomeChange(double dist, int scale, ResourceKey<Biome> original) {
        if (!MainConfig.COMMON.ENABLE_CRATER_BIOMES.get()) return null;

        if (scale >= 150 && dist < 15) {
            return ModBiomes.CRATER_INNER;
        }
        if (scale >= 100 && dist < 55 && original != ModBiomes.CRATER_INNER) {
            return ModBiomes.CRATER;
        }
        if (scale >= 25 && original != ModBiomes.CRATER_INNER && original != ModBiomes.CRATER) {
            return ModBiomes.CRATER_OUTER;
        }
        return null;
    }

    private final List<Long> chunksToProcess = new ArrayList<>();
    private final List<Long> outerChunksToProcess = new ArrayList<>();

    // Is it worth the effort to split this into a method that can be called over multiple ticks? I'd say it's fast enough anyway...
    private void gatherChunks() {
        Set<Long> chunks = new LinkedHashSet<>(); // LinkedHashSet preserves insertion order
        Set<Long> outerChunks = new LinkedHashSet<>();
        int outerRange = getScale();
        // Basically defines something like the step size, but as indirect proportion. The actual angle used for rotation will always end up at 360° for angle == adjustedMaxAngle
        // So yea, I mathematically worked out that 20 is a good value for this, with the minimum possible being 18 in order to reach all chunks
        int adjustedMaxAngle = 20 * outerRange / 32; // step size = 20 * chunks / 2
        for (int angle = 0; angle <= adjustedMaxAngle; angle++) {
            Vec3 vector = new Vec3(outerRange, 0, 0)
                    .yRot((float) (angle * Math.PI / 180.0 / (adjustedMaxAngle / 360.0))); // Ugh, mutable data classes (also, ugh, radians; it uses degrees in 1.18; took me two hours to debug)
            outerChunks.add(ChunkPos.asLong((int) (this.getX() + vector.x) >> 4, (int) (this.getZ() + vector.z) >> 4));
        }

        for (int distance = 0; distance <= outerRange; distance += 8) {
            for (int angle = 0; angle <= adjustedMaxAngle; angle++) {
                Vec3 vector = new Vec3(distance, 0, 0)
                        .yRot((float) (angle * Math.PI / 180.0 / (adjustedMaxAngle / 360.0)));
                long chunkCoord = ChunkPos.asLong((int) (this.getX() + vector.x) >> 4, (int) (this.getZ() + vector.z) >> 4);
                if (!outerChunks.contains(chunkCoord)) chunks.add(chunkCoord);
            }
        }

        chunksToProcess.addAll(chunks);
        outerChunksToProcess.addAll(outerChunks);
        Collections.reverse(chunksToProcess); // So it starts nicely from the middle
        Collections.reverse(outerChunksToProcess);
    }

    private void stomp(int x, int z, double dist) {

        int depth = 0;

        for (int y = 320; y >= -64; y--) {

            if (depth >= 3) return;

            BlockPos pos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(pos);

            if (state.isAir() || state.is(ModBlocks.FALLOUT.get())) continue;

//            if (block == ModBlocks.VOLCANO_CORE.get()) {
//                level.setBlock(pos, ModBlocks.VOLCANO_RAD_CORE.get().defaultBlockState(), 3);
//                continue;
//            }

            BlockPos above = pos.above();
            BlockState aboveState = level.getBlockState(above);

            if (depth == 0 && !state.is(ModBlocks.FALLOUT.get()) && (aboveState.isAir() || (aboveState.canBeReplaced() && !aboveState.getFluidState().isEmpty()))) {

                double d = dist / 100;
                double chance = 0.1 - Math.pow((d - 0.7), 2);

                if (chance >= random.nextDouble() && FalloutBlock.canPlaceBlockAt(level, above)) {
                    level.setBlock(above, ModBlocks.FALLOUT.get().defaultBlockState(), 3);
                }
            }

            if (dist < 65 && state.isFlammable(level, pos, Direction.UP)) {
                if (random.nextInt(5) == 0 && level.getBlockState(above).isAir()) {
                    level.setBlock(above, Blocks.FIRE.defaultBlockState(), 3);
                }
            }

            boolean eval = false;

            for (FalloutConfigJSON.FalloutEntry entry : FalloutConfigJSON.entries) {
                if (entry.eval(level, pos, state, dist)) {
                    if (entry.isSolid()) depth++;
                    eval = true;
                    break;
                }
            }

            float hardness = state.getDestroySpeed(level, pos);
            if (y > level.getMinBuildHeight() && dist < 65 && hardness <= Blocks.STONE_BRICKS.getExplosionResistance(state, level, pos, null) && hardness >= 0) {

                if (level.getBlockState(pos.below()).isAir()) {
                    for (int i = 0; i <= depth; i++) {
                        BlockPos fallingPos = pos.offset(0, i, 0);
                        BlockState fallingState = level.getBlockState(fallingPos);
                        float h = fallingState.getDestroySpeed(level, fallingPos);
                        if (h <= Blocks.STONE_BRICKS.getExplosionResistance(state, level, pos, null) && h >= 0) {
                            FallingBlockEntityNT fallingBlockEntity = FallingBlockEntityNT.fall(level, pos, fallingState);
                            fallingBlockEntity.dropItem = false;
                        }
                    }
                }
            }

            if (!eval && state.isSolidRender(level, pos)) {
                depth++;
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SCALE, 1);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        setScale(compoundTag.getInt("scale"));
        chunksToProcess.addAll(readChunksFromIntArray(compoundTag.getIntArray("chunks")));
        outerChunksToProcess.addAll(readChunksFromIntArray(compoundTag.getIntArray("outerChunks")));
    }

    private Collection<Long> readChunksFromIntArray(int[] data) {
        List<Long> coords = new ArrayList<>();
        for (int i = 0; i < data.length; i += 2) {
            int x = data[i];
            int z = data[i + 1];
            coords.add(ChunkPos.asLong(x, z));
        }
        return coords;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("scale", getScale());
        compoundTag.putIntArray("chunks", writeChunksToIntArray(chunksToProcess));
        compoundTag.putIntArray("outerChunks", writeChunksToIntArray(outerChunksToProcess));
    }

    private int[] writeChunksToIntArray(Collection<Long> coords) {
        int[] data = new int[coords.size() * 2];
        int i = 0;
        for (long packed : coords) {
            data[i++] = ChunkPos.getX(packed);
            data[i++] = ChunkPos.getZ(packed);
        }
        return data;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    public void setScale(int i) {
        this.entityData.set(SCALE, i);
    }

    public int getScale() {
        int scale = this.entityData.get(SCALE);
        return scale == 0 ? 1 : scale;
    }
}

