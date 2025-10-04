package com.hbm.entity.effect;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.FalloutBlock;
import com.hbm.config.FalloutConfigJSON;
import com.hbm.config.ServerConfig;
import com.hbm.entity.logic.ChunkloadingEntity;
import com.hbm.world.WorldUtil;
import com.hbm.world.biome.ModBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public class EntityFalloutRain extends ChunkloadingEntity {

    private boolean firstTick = true;
    private final Level level;

    private static final EntityDataAccessor<Integer> SCALE =
            SynchedEntityData.defineId(EntityFalloutRain.class, EntityDataSerializers.INT);

    public EntityFalloutRain(EntityType<?> type, Level level) {
        super(type, level);
        this.level = level;
    }

    private int tickDelay = ServerConfig.F_DELAY.getAsInt();
    private final int mk5 = ServerConfig.MK5.getAsInt();

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {

            long start = System.currentTimeMillis();

            if (firstTick) {
                if (chunksToProcess.isEmpty() && outerChunksToProcess.isEmpty()) gatherChunks();
                firstTick = false;
            }

            if (tickDelay == 0) {
                tickDelay = 4;

                while (System.currentTimeMillis() < start + mk5) {
                    if (!chunksToProcess.isEmpty()) {
                        long chunkPos = chunksToProcess.removeLast();
                        int chunkPosX = ChunkPos.getX(chunkPos);
                        int chunkPosZ = ChunkPos.getZ(chunkPos);

                        LevelChunk chunk = level.getChunk(chunkPosX, chunkPosZ);

                        for (int x = chunkPosX << 4; x < (chunkPosX << 4) + 16; x++) {
                            for (int z = chunkPosZ << 4; z < (chunkPosZ << 4) + 16; z++) {
                                BlockPos pos = new BlockPos(x, level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z), z);
                                double percent = Math.hypot(x - this.getX(), z - this.getZ()) * 100 / getScale();
                                stomp(x, z, percent);
                                ResourceKey<Biome> biome = getBiomeChange(percent, getScale(), level.getBiome(pos).getKey());
                                if (biome != null) {
                                    WorldUtil.setBiomeColumn((ServerLevel) level, x, z, biome);
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
                                BlockPos pos = new BlockPos(x, level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z), z);
                                double distance = Math.hypot(x - this.getX(), z - this.getZ());
                                if (distance <= getScale()) {
                                    double percent = distance * 100 / getScale();
                                    stomp(x, z, percent);
                                    ResourceKey<Biome> biome = getBiomeChange(percent, getScale(), level.getBiome(pos).getKey());
                                    if (biome != null) {
                                        WorldUtil.setBiomeColumn((ServerLevel) level, x, z, biome);
                                    }
                                }
                            }
                        }
                        WorldUtil.flushChunk((ServerLevel) level, chunk);
                    } else {
                        this.discard();
                        break;
                    }
                }
            }

            tickDelay--;
        }
    }


    public static ResourceKey<Biome> getBiomeChange(double dist, int scale, ResourceKey<Biome> original) {
        if (!ServerConfig.ENABLE_CRATER_BIOMES.getAsBoolean()) return null;

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

    private void gatherChunks() {
        Set<Long> chunks = new LinkedHashSet<>();
        Set<Long> outerChunks = new LinkedHashSet<>();
        int outerRange = getScale();
        int adjustedMaxAngle = 20 * outerRange / 32;
        for (int angle = 0; angle <= adjustedMaxAngle; angle++) {
            Vec3 vector = new Vec3(outerRange, 0, 0)
                    .yRot((float) (angle * Math.PI / 180.0 / (adjustedMaxAngle / 360.0)));
            long chunkCoord = ChunkPos.asLong((int) (this.getX() + vector.x) >> 4, (int) (this.getZ() + vector.z) >> 4);
            outerChunks.add(chunkCoord);
        }

        for (int distance = 0; distance <= outerRange; distance += 8) {
            for (int angle = 0; angle <= adjustedMaxAngle; angle++) {
                Vec3 vector = new Vec3(distance, 0, 0)
                        .yRot((float) (angle * Math.PI / 180.0 / (adjustedMaxAngle / 360.0)));
                long chunkCoord = ChunkPos.asLong((int) (this.getX() + vector.x) >> 4, (int) (this.getZ() + vector.z) >> 4);
                if (!outerChunks.contains(chunkCoord)) {
                    chunks.add(chunkCoord);
                }
            }
        }

        chunksToProcess.addAll(chunks);
        outerChunksToProcess.addAll(outerChunks);
        Collections.reverse(chunksToProcess);
        Collections.reverse(outerChunksToProcess);
    }

    private void stomp(int x, int z, double dist) {

        int depth = 0;

        for (int y = level.getMaxBuildHeight() - 1; y >= level.getMinBuildHeight(); y--) {

            if (depth >= 3) return;

            BlockPos pos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();

            if (state.isAir() || block == ModBlocks.FALLOUT.get()) continue;

//            if (block == ModBlocks.VOLCANO_CORE.get()) {
//                level.setBlock(pos, ModBlocks.VOLCANO_RAD_CORE.get().defaultBlockState(), 3);
//                continue;
//            }

            BlockPos above = pos.above();
            BlockState aboveState = level.getBlockState(above);

            if (depth == 0 && block != ModBlocks.FALLOUT.get() && (aboveState.isAir() || (aboveState.canBeReplaced() && !aboveState.getFluidState().isEmpty()))) {

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
            if (y > level.getMinBuildHeight() && dist < 65 &&
                    hardness <= Blocks.STONE_BRICKS.getExplosionResistance(state, level, pos, null) && hardness >= 0) {

                BlockPos below = pos.below();
                if (level.getBlockState(below).isAir()) {
                    for (int i = 0; i <= depth; i++) {
                        BlockPos fallingPos = pos.offset(0, i, 0);
                        BlockState fallingState = level.getBlockState(fallingPos);
                        float h = fallingState.getDestroySpeed(level, fallingPos);
                        if (h <= Blocks.STONE_BRICKS.getExplosionResistance(state, level, pos, null) && h >= 0) {
                            FallingBlockEntity leaves = FallingBlockEntity.fall(level, pos, fallingState);
                            leaves.dropItem = false;
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

