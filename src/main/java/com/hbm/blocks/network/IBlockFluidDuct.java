package com.hbm.blocks.network;

import com.hbm.inventory.fluid.FluidType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IBlockFluidDuct {
    void changeTypeRecursively(Level level, BlockPos pos, FluidType type, int loopsRemaining);
}
