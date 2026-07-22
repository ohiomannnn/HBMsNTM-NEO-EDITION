package com.hbm.world;

import com.hbm.blocks.NtmBlocks;
import com.hbm.config.NtmConfig;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.registry.NtmDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MeteoriteStructure {

    private static final Set<Block> REPLACEABLES = new HashSet<>();
    private static boolean safeMode;

    public void generate(Level level, RandomSource random, int x, int y, int z, boolean safe, boolean special, boolean doDamage) {
        safeMode = safe;

        if (REPLACEABLES.isEmpty()) {
            generateReplaceables();
        }

        if (doDamage && level instanceof ServerLevel serverLevel) {
            for (Entity entity : serverLevel.getEntities((Entity) null, new net.minecraft.world.phys.AABB(x - 15, y - 15, z - 15, x + 15, y + 15, z + 15))) {
                entity.hurt(serverLevel.damageSources().source(NtmDamageTypes.METEORITE), 1000.0F);
            }
        }

        if (special && NtmConfig.COMMON.ENABLE_SPECIAL_METEORS.get()) {
            if (tryGenerateSpecial(level, random, x, y, z)) {
                return;
            }
        }

        switch (random.nextInt(3)) {
            case 0 -> generateLarge(level, random, x, y, z);
            case 1 -> generateMedium(level, random, x, y, z);
            default -> generateSmall(level, random, x, y, z);
        }
    }

    private boolean tryGenerateSpecial(Level level, RandomSource random, int x, int y, int z) {
        return switch (random.nextInt(300)) {
            case 0 -> {
                generateBox(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR.get()));
                yield true;
            }
            case 1 -> {
                generateSphere7x7(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_BROKEN.get(), NtmBlocks.BLOCK_METEOR_COBBLE.get()));
                yield true;
            }
            case 2 -> {
                generateSphere5x5(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_BROKEN.get(), NtmBlocks.BLOCK_METEOR_COBBLE.get()));
                yield true;
            }
            case 3 -> {
                generateBox(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_COBBLE.get(), NtmBlocks.BLOCK_METEOR_BROKEN.get()));
                yield true;
            }
            case 4 -> {
                if (level instanceof ServerLevel serverLevel) {
                    ExplosionLarge.spawnRubble(serverLevel, x + 0.5, y, z + 0.5, 25);
                }
                yield true;
            }
            case 5 -> {
                generateSphere7x7(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_TREASURE.get(), NtmBlocks.BLOCK_METEOR_BROKEN.get()));
                yield true;
            }
            case 6 -> {
                generateSphere5x5(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_TREASURE.get(), NtmBlocks.BLOCK_METEOR_TREASURE.get(), NtmBlocks.BLOCK_METEOR_BROKEN.get()));
                yield true;
            }
            case 7 -> {
                generateBox(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_TREASURE.get()));
                yield true;
            }
            case 8 -> {
                generateSphere7x7(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_TREASURE.get()));
                generateSphere5x5(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_MOLTEN.get()));
                yield true;
            }
            case 9 -> {
                generateSphere9x9(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_BROKEN.get()));
                generateSphere7x7(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_COBBLE.get(), NtmBlocks.BLOCK_METEOR_MOLTEN.get()));
                yield true;
            }
            case 10 -> {
                generateSphere5x5(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_BROKEN.get()));
                setBlock(level, new BlockPos(x, y, z), NtmBlocks.TAINT.get().defaultBlockState());
                yield true;
            }
            default -> false;
        };
    }

    private void generateLarge(Level level, RandomSource random, int x, int y, int z) {
        int shellType = random.nextInt(4);
        int midType = 0;
        if (shellType == 2) {
            midType = 1 + random.nextInt(2);
        } else if (shellType == 3) {
            midType = 2;
        }
        int coreType = random.nextInt(shellType == 0 ? 3 : 2);

        List<Block> shell = switch (shellType) {
            case 0 -> List.of(NtmBlocks.BLOCK_METEOR_MOLTEN.get());
            case 1 -> List.of(NtmBlocks.BLOCK_METEOR_COBBLE.get());
            case 2 -> weightedList(NtmBlocks.BLOCK_METEOR_BROKEN.get(), 99, NtmBlocks.BLOCK_METEOR_TREASURE.get(), 1);
            default -> List.of(NtmBlocks.BLOCK_METEOR_MOLTEN.get(), NtmBlocks.BLOCK_METEOR_BROKEN.get());
        };

        List<Block> mid = switch (midType) {
            case 0 -> List.of(NtmBlocks.BLOCK_METEOR_COBBLE.get());
            case 1 -> weightedList(NtmBlocks.BLOCK_METEOR_BROKEN.get(), 99, NtmBlocks.BLOCK_METEOR_TREASURE.get(), 1);
            default -> List.of(NtmBlocks.BLOCK_METEOR_COBBLE.get(), NtmBlocks.BLOCK_METEOR_BROKEN.get());
        };

        List<Block> core = switch (coreType) {
            case 0 -> weightedList(NtmBlocks.BLOCK_METEOR_BROKEN.get(), 99, NtmBlocks.BLOCK_METEOR_TREASURE.get(), 1);
            case 1 -> List.of(NtmBlocks.BLOCK_METEOR_BROKEN.get());
            default -> List.of(NtmBlocks.BLOCK_METEOR_COBBLE.get());
        };

        generateSphere9x9(level, random, x, y, z, shell);
        generateSphere7x7(level, random, x, y, z, mid);
        generateSphere5x5(level, random, x, y, z, core);

        switch (random.nextInt(3)) {
            case 0 -> generateStar3x3(level, random, x, y, z, getRandomMeteorOres());
            case 1 -> setBlock(level, new BlockPos(x, y, z), NtmBlocks.BLOCK_METEOR_TREASURE.get().defaultBlockState());
            default -> setBlock(level, new BlockPos(x, y, z), NtmBlocks.BLOCK_METEOR.get().defaultBlockState());
        }
    }

    private void generateMedium(Level level, RandomSource random, int x, int y, int z) {
        List<Block> shell = switch (random.nextInt(4)) {
            case 0 -> List.of(NtmBlocks.BLOCK_METEOR_MOLTEN.get());
            case 1 -> List.of(NtmBlocks.BLOCK_METEOR_COBBLE.get());
            case 2 -> weightedList(NtmBlocks.BLOCK_METEOR_BROKEN.get(), 99, NtmBlocks.BLOCK_METEOR_TREASURE.get(), 1);
            default -> List.of(NtmBlocks.BLOCK_METEOR_MOLTEN.get(), NtmBlocks.BLOCK_METEOR_BROKEN.get());
        };

        generateSphere7x7(level, random, x, y, z, shell);

        switch (random.nextInt(3)) {
            case 0 -> generateStar5x5(level, random, x, y, z, getRandomMeteorOres());
            case 1 -> {
                generateStar5x5(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_BROKEN.get()));
                setBlock(level, new BlockPos(x, y, z), NtmBlocks.BLOCK_METEOR_TREASURE.get().defaultBlockState());
            }
            default -> {
                generateStar5x5(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_BROKEN.get()));
                setBlock(level, new BlockPos(x, y, z), NtmBlocks.BLOCK_METEOR.get().defaultBlockState());
            }
        }
    }

    private void generateSmall(Level level, RandomSource random, int x, int y, int z) {
        List<Block> shell = switch (random.nextInt(4)) {
            case 0 -> List.of(NtmBlocks.BLOCK_METEOR_MOLTEN.get());
            case 1 -> List.of(NtmBlocks.BLOCK_METEOR_COBBLE.get());
            case 2 -> weightedList(NtmBlocks.BLOCK_METEOR_BROKEN.get(), 99, NtmBlocks.BLOCK_METEOR_TREASURE.get(), 1);
            default -> List.of(NtmBlocks.BLOCK_METEOR_MOLTEN.get(), NtmBlocks.BLOCK_METEOR_BROKEN.get());
        };

        generateSphere5x5(level, random, x, y, z, shell);

        switch (random.nextInt(3)) {
            case 0 -> generateStar3x3(level, random, x, y, z, getRandomMeteorOres());
            case 1 -> setBlock(level, new BlockPos(x, y, z), NtmBlocks.BLOCK_METEOR_TREASURE.get().defaultBlockState());
            default -> {
                generateStar3x3(level, random, x, y, z, List.of(NtmBlocks.BLOCK_METEOR_TREASURE.get()));
                setBlock(level, new BlockPos(x, y, z), NtmBlocks.BLOCK_METEOR.get().defaultBlockState());
            }
        }
    }

    private void generateSphere9x9(Level level, RandomSource random, int x, int y, int z, List<Block> blocks) {
        generateSphere(level, random, x, y, z, 4.5D, blocks);
    }

    private void generateSphere7x7(Level level, RandomSource random, int x, int y, int z, List<Block> blocks) {
        generateSphere(level, random, x, y, z, 3.5D, blocks);
    }

    private void generateSphere5x5(Level level, RandomSource random, int x, int y, int z, List<Block> blocks) {
        generateSphere(level, random, x, y, z, 2.5D, blocks);
    }

    private void generateSphere(Level level, RandomSource random, int x, int y, int z, double radius, List<Block> blocks) {
        int range = (int) Math.ceil(radius);
        double radiusSq = radius * radius;

        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    double distSq = dx * dx + dy * dy + dz * dz;
                    if (distSq <= radiusSq) {
                        setRandomBlock(level, new BlockPos(x + dx, y + dy, z + dz), random, blocks);
                    }
                }
            }
        }
    }

    private void generateBox(Level level, RandomSource random, int x, int y, int z, List<Block> blocks) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    setRandomBlock(level, new BlockPos(x + dx, y + dy, z + dz), random, blocks);
                }
            }
        }
    }

    private void generateStar5x5(Level level, RandomSource random, int x, int y, int z, List<Block> blocks) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    setRandomBlock(level, new BlockPos(x + dx, y + dy, z + dz), random, blocks);
                }
            }
        }

        setRandomBlock(level, new BlockPos(x + 2, y, z), random, blocks);
        setRandomBlock(level, new BlockPos(x - 2, y, z), random, blocks);
        setRandomBlock(level, new BlockPos(x, y + 2, z), random, blocks);
        setRandomBlock(level, new BlockPos(x, y - 2, z), random, blocks);
        setRandomBlock(level, new BlockPos(x, y, z + 2), random, blocks);
        setRandomBlock(level, new BlockPos(x, y, z - 2), random, blocks);
    }

    private void generateStar3x3(Level level, RandomSource random, int x, int y, int z, List<Block> blocks) {
        setRandomBlock(level, new BlockPos(x, y, z), random, blocks);
        setRandomBlock(level, new BlockPos(x + 1, y, z), random, blocks);
        setRandomBlock(level, new BlockPos(x - 1, y, z), random, blocks);
        setRandomBlock(level, new BlockPos(x, y + 1, z), random, blocks);
        setRandomBlock(level, new BlockPos(x, y - 1, z), random, blocks);
        setRandomBlock(level, new BlockPos(x, y, z + 1), random, blocks);
        setRandomBlock(level, new BlockPos(x, y, z - 1), random, blocks);
    }

    private void setRandomBlock(Level level, BlockPos pos, RandomSource random, List<Block> blocks) {
        setBlock(level, pos, blocks.get(random.nextInt(blocks.size())).defaultBlockState());
    }

    private void setBlock(Level level, BlockPos pos, BlockState newState) {
        BlockState oldState = level.getBlockState(pos);
        if (safeMode && !oldState.isAir() && !REPLACEABLES.contains(oldState.getBlock())) {
            return;
        }

        if (oldState.getDestroySpeed(level, pos) < 10000.0F) {
            level.setBlock(pos, newState, 18);
        }
    }

    private static void generateReplaceables() {
        REPLACEABLES.add(NtmBlocks.BLOCK_METEOR.get());
        REPLACEABLES.add(NtmBlocks.BLOCK_METEOR_BROKEN.get());
        REPLACEABLES.add(NtmBlocks.BLOCK_METEOR_COBBLE.get());
        REPLACEABLES.add(NtmBlocks.BLOCK_METEOR_MOLTEN.get());
        REPLACEABLES.add(NtmBlocks.BLOCK_METEOR_TREASURE.get());
        REPLACEABLES.add(NtmBlocks.ORE_METEOR_IRON.get());
        REPLACEABLES.add(NtmBlocks.ORE_METEOR_COBALT.get());
        REPLACEABLES.add(NtmBlocks.ORE_METEOR_ALUMINIUM.get());
        REPLACEABLES.add(NtmBlocks.ORE_METEOR_COPPER.get());
        REPLACEABLES.add(NtmBlocks.ORE_METEOR_RARE.get());
        REPLACEABLES.add(Blocks.AIR);
    }

    private List<Block> getRandomMeteorOres() {
        return List.of(
                NtmBlocks.ORE_METEOR_IRON.get(),
                NtmBlocks.ORE_METEOR_COBALT.get(),
                NtmBlocks.ORE_METEOR_ALUMINIUM.get(),
                NtmBlocks.ORE_METEOR_COPPER.get(),
                NtmBlocks.ORE_METEOR_RARE.get()
        );
    }

    private List<Block> weightedList(Block primary, int primaryCount, Block secondary, int secondaryCount) {
        java.util.ArrayList<Block> blocks = new java.util.ArrayList<>(primaryCount + secondaryCount);
        for (int i = 0; i < primaryCount; i++) blocks.add(primary);
        for (int i = 0; i < secondaryCount; i++) blocks.add(secondary);
        return blocks;
    }
}
