package com.hbm.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IOverpressurable {
    void explode(Level level, BlockPos pos);
}
