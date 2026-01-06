package com.hbm.blocks.bomb;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;

public interface IDetConnectible {
    default boolean canConnectToDetCord(BlockGetter level, BlockPos pos, Direction dir) {
        return true;
    }

    static boolean isConnectible(BlockGetter level, BlockPos pos, Direction dir) {
        Block b = level.getBlockState(pos).getBlock();
        if (b instanceof IDetConnectible con) {
            return con.canConnectToDetCord(level, pos, dir);
        }
        return false;
    }
}
