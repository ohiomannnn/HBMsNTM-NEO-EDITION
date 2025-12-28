package com.hbm.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class Compat {
    /** A standard implementation of safely grabbing a tile entity without loading chunks, might have more fluff added to it later on. */
    public static BlockEntity getBlockEntityStandard(Level level, BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();
        if (!level.hasChunk(x >> 4, z >> 4)) return null;
        return level.getBlockEntity(pos);
    }
}
