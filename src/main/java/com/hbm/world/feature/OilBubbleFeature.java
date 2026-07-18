package com.hbm.world.feature;

import com.hbm.blocks.NtmBlocks;
import com.hbm.main.NuclearTechMod;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class OilBubbleFeature extends Feature<NoneFeatureConfiguration> {

    private static final int BUBBLE_MIN_RADIUS = 10;
    private static final int BUBBLE_RADIUS_VARIATION = 7;
    private static final int BUBBLE_BASE_SPAWN_RATE = 100;
    private static final int HOT_DRY_SPAWN_DIVISOR = 3;

    public OilBubbleFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        int spawnRate = getSpawnRate(level, origin);
        if(spawnRate > 0 && random.nextInt(spawnRate) != 0) {
            return false;
        }

        int radius = BUBBLE_MIN_RADIUS + random.nextInt(BUBBLE_RADIUS_VARIATION);
        spawnOil(level, origin.getX(), origin.getY(), origin.getZ(), radius);
        addSurfaceSpot(level, random, origin.getX(), origin.getZ());

        if(NuclearTechMod.LOGGER.isDebugEnabled()) {
            NuclearTechMod.LOGGER.debug("[OilBubble] Generated oil bubble at {} {} {} (radius {})", origin.getX(), origin.getY(), origin.getZ(), radius);
        }

        return true;
    }

    private int getSpawnRate(WorldGenLevel level, BlockPos pos) {
        Holder<Biome> biomeHolder = level.getBiome(pos);
        Biome biome = biomeHolder.value();
        int spawnRate = BUBBLE_BASE_SPAWN_RATE;

        if(biome.getBaseTemperature() >= 2.0F && biome.getPrecipitationAt(pos) == Biome.Precipitation.NONE) {
            spawnRate /= HOT_DRY_SPAWN_DIVISOR;
        }

        return Math.max(spawnRate, 1);
    }

    private void spawnOil(WorldGenLevel level, int x, int y, int z, int radius) {
        int r2 = radius * radius;
        int r22 = r2 / 2;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for(int xx = -radius; xx < radius; xx++) {
            int xPos = xx + x;
            int xx2 = xx * xx;
            for(int yy = -radius; yy < radius; yy++) {
                int yPos = yy + y;
                int yy2 = xx2 + yy * yy * 3;
                for(int zz = -radius; zz < radius; zz++) {
                    int zPos = zz + z;
                    int distSq = yy2 + zz * zz;
                    if(distSq < r22) {
                        pos.set(xPos, yPos, zPos);
                        if(level.getBlockState(pos).is(Blocks.STONE)) {
                            level.setBlock(pos, NtmBlocks.ORE_OIL.get().defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }

    private void addSurfaceSpot(WorldGenLevel level, RandomSource random, int xCoord, int zCoord) {
        int spotCount = 150;
        int spotWidth = 7;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for(int i = 0; i < spotCount; i++) {
            int offX = (int) (random.nextGaussian() * spotWidth);
            int offZ = (int) (random.nextGaussian() * spotWidth);
            int absX = xCoord + offX;
            int absZ = zCoord + offZ;
            int innerRadiusSq = (spotWidth / 2) * (spotWidth / 2);
            boolean inner = offX * offX + offZ * offZ < innerRadiusSq;

            for(int y = level.getMaxBuildHeight() - 1; y >= level.getMinBuildHeight(); y--) {
                pos.set(absX, y, absZ);
                if(!level.getBlockState(pos).isSolidRender(level, pos)) {
                    continue;
                }

                for(int oy = 1; oy > -3; oy--) {
                    pos.set(absX, y + oy, absZ);
                    BlockPos targetPos = pos.immutable();
                    var targetState = level.getBlockState(targetPos);

                    if(targetState.is(Blocks.GRASS_BLOCK) || targetState.is(Blocks.DIRT)) {
                        level.setBlock(targetPos, inner ? NtmBlocks.DIRT_OILY.get().defaultBlockState() : NtmBlocks.DIRT_DEAD.get().defaultBlockState(), 3);
                        if(!inner && oy == 0 && random.nextInt(20) == 0) {
                            BlockPos above = targetPos.above();
                            if(level.getBlockState(above).canBeReplaced()) {
                                level.setBlock(above, Blocks.DEAD_BUSH.defaultBlockState(), 3);
                            }
                        }
                        break;
                    }

                    if(targetState.is(Blocks.SAND) || targetState.is(Blocks.RED_SAND) || targetState.is(NtmBlocks.ORE_OIL_SAND.get())) {
                        level.setBlock(targetPos, targetState.is(Blocks.RED_SAND) ? NtmBlocks.SAND_RED_OILY.get().defaultBlockState() : NtmBlocks.SAND_OILY.get().defaultBlockState(), 3);
                        break;
                    }

                    if(targetState.is(Blocks.STONE)) {
                        level.setBlock(targetPos, NtmBlocks.STONE_CRACKED.get().defaultBlockState(), 3);
                        break;
                    }

                    if(targetState.is(BlockTags.LEAVES)) {
                        level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                        break;
                    }
                }
                break;
            }
        }

        for(int i = 1; i < 6; i++) {
            Direction facing = Direction.from3DDataValue(i);
            int x = xCoord + facing.getStepX();
            int z = zCoord + facing.getStepZ();
            int solids = 0;

            for(int y = level.getMaxBuildHeight() - 1; y >= level.getMinBuildHeight(); y--) {
                pos.set(x, y, z);
                var state = level.getBlockState(pos);
                if(state.isAir()) {
                    continue;
                }
                if(!state.getFluidState().isEmpty()) {
                    break;
                }

                if(state.isSolidRender(level, pos)) {
                    solids++;

                    if(i > 1) {
                        level.setBlock(pos, NtmBlocks.STONE_CRACKED.get().defaultBlockState(), 3);
                        if(solids >= 4) {
                            break;
                        }
                    } else {
                        if(solids < 3) {
                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        }
                        if(solids == 3) {
                            level.setBlock(pos, NtmBlocks.OIL_SPILL.get().defaultBlockState(), 3);
                        }
                        if(solids > 3 && solids < 7) {
                            level.setBlock(pos, NtmBlocks.STONE_CRACKED.get().defaultBlockState(), 3);
                        }
                        if(solids == 7) {
                            break;
                        }
                    }
                }
            }
        }
    }
}
