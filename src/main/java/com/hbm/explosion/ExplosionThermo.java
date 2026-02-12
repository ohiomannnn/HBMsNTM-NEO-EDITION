package com.hbm.explosion;

import com.hbm.blocks.ModBlocks;
import com.hbm.interfaces.Spaghetti;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

@Spaghetti("5 years of shitty code")
public class ExplosionThermo {

    public static void freeze(Level level, BlockPos pos, int bombStartStrength) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int r = bombStartStrength * 2;
        int r2 = r*r;
        int r22 = r2/2;
        for (int xx = -r; xx < r; xx++) {
            int X = xx+x;
            int XX = xx*xx;
            for (int yy = -r; yy < r; yy++) {
                int Y = yy+y;
                int YY = XX+yy*yy;
                for (int zz = -r; zz < r; zz++) {
                    int Z = zz+z;
                    int ZZ = YY+zz*zz;
                    if (ZZ<r22 + level.random.nextInt(r22/2)) {
                        freezeDest(level, X, Y, Z);
                    }
                }
            }
        }
    }

    public static void freezeDest(Level level, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = level.getBlockState(pos);
        Block b = state.getBlock();

        if (b == Blocks.GRASS_BLOCK) {
            level.setBlock(pos, ModBlocks.FROZEN_GRASS.get().defaultBlockState(), 3);
        }
        if (b == Blocks.DIRT) {
            level.setBlock(pos, ModBlocks.FROZEN_DIRT.get().defaultBlockState(), 3);
        }
        if (state.is(BlockTags.LOGS)) {
            level.setBlock(pos, ModBlocks.FROZEN_LOG.get().defaultBlockState(), 3);
        }
        if (state.is(BlockTags.PLANKS)) {
            level.setBlock(pos, ModBlocks.FROZEN_PLANKS.get().defaultBlockState(), 3);
        }
        if (b == Blocks.STONE) {
            level.setBlock(pos, Blocks.PACKED_ICE.defaultBlockState(), 3);
        }
        if (b == Blocks.COBBLESTONE) {
            level.setBlock(pos, Blocks.PACKED_ICE.defaultBlockState(), 3);
        }
        if (state.is(BlockTags.LEAVES)) {
            level.setBlock(pos, Blocks.SNOW_BLOCK.defaultBlockState(), 3);
        }
        if (state.getFluidState().is(FluidTags.LAVA)) {
            level.setBlock(pos, Blocks.SNOW_BLOCK.defaultBlockState(), 3);
        }
        if (state.getFluidState().is(FluidTags.WATER)) {
            level.setBlock(pos, Blocks.ICE.defaultBlockState(), 3);
        }
    }
}
