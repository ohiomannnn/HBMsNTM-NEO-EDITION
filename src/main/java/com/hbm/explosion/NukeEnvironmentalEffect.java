package com.hbm.explosion;

import java.util.Random;

import com.hbm.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class NukeEnvironmentalEffect {

    private static final Random rand = new Random();

    public static void applyStandardAOE(Level level, int x, int y, int z, int r, int j) {
        int r2 = r * r;
        int r22 = r2 / 2;

        for (int xx = -r; xx < r; xx++) {
            int X = xx + x;
            int XX = xx * xx;
            for (int yy = -r; yy < r; yy++) {
                int Y = yy + y;
                int YY = XX + yy * yy;
                for (int zz = -r; zz < r; zz++) {
                    int Z = zz + z;
                    int ZZ = YY + zz * zz;
                    if (ZZ < r22 + rand.nextInt(j)) {
                        applyStandardEffect(level, new BlockPos(X, Y, Z));
                    }
                }
            }
        }
    }

    public static void applyStandardEffect(Level level, BlockPos pos) {
        int chance = 100;
        Block replacement = null;

        BlockState state = level.getBlockState(pos);
        Block in = state.getBlock();

        if (in == Blocks.AIR) return;

        // how to add things
        /*
        if (in == Blocks.GRASS_BLOCK) {
            replacement = ModBlocks.WASTE_EARTH.get();
            chance = insert ur chance to mutate or don't if u want to
        }
        */

        if (in == Blocks.SAND) {
            if (state.is(Blocks.RED_SAND)) {
                replacement = ModBlocks.WASTE_TRINITITE_RED.get();
            } else {
                replacement = ModBlocks.WASTE_TRINITITE.get();
            }
            chance = 20;

        } else if (in == Blocks.MYCELIUM) {
            replacement = ModBlocks.WASTE_MYCELIUM.get();

        } else if (in == Blocks.OAK_LOG || in == Blocks.SPRUCE_LOG || in == Blocks.BIRCH_LOG
                || in == Blocks.JUNGLE_LOG || in == Blocks.ACACIA_LOG || in == Blocks.DARK_OAK_LOG) {
            replacement = ModBlocks.WASTE_LOG.get();

        } else if (in == Blocks.OAK_PLANKS || in == Blocks.SPRUCE_PLANKS || in == Blocks.BIRCH_PLANKS
                || in == Blocks.JUNGLE_PLANKS || in == Blocks.ACACIA_PLANKS || in == Blocks.DARK_OAK_PLANKS) {
            replacement = ModBlocks.WASTE_PLANKS.get();

        } else if (in == Blocks.MOSSY_COBBLESTONE) {
            replacement = ModBlocks.ORE_OIL.get();
            chance = 50;

        } else if (in == Blocks.COAL_ORE || in == Blocks.DEEPSLATE_COAL_ORE) {
            replacement = Blocks.DIAMOND_ORE;
            chance = 10;

        } else if (in == ModBlocks.ORE_URANIUM.get()) {
            replacement = ModBlocks.ORE_SCHRABIDIUM.get();
            chance = 10;

        } else if (in == ModBlocks.ORE_NETHER_URANIUM.get()) {
            replacement = ModBlocks.ORE_NETHER_SCHRABIDIUM.get();
            chance = 10;

        } else if (in == ModBlocks.ORE_NETHER_PLUTONIUM.get()) {
            replacement = ModBlocks.ORE_NETHER_SCHRABIDIUM.get();
            chance = 25;

        } else if (in == Blocks.BROWN_MUSHROOM_BLOCK) {
            replacement = ModBlocks.WASTE_PLANKS.get();

        } else if (in == Blocks.RED_MUSHROOM_BLOCK) {
            replacement = ModBlocks.WASTE_PLANKS.get();

        } else if (in == Blocks.END_STONE) {
            replacement = ModBlocks.ORE_TIKITE.get();
            chance = 1;

        } else if (in == Blocks.CLAY) {
            replacement = Blocks.TERRACOTTA;

        } else if (state.ignitedByLava() || state.isFlammable(level, pos, null)) {
            replacement = Blocks.FIRE;
            chance = 100;
        }

        if (replacement != null && rand.nextInt(1000) < chance) {
            level.setBlock(pos, replacement.defaultBlockState(), 2);
        }
    }
}
