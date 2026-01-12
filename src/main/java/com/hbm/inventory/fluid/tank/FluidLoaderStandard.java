package com.hbm.inventory.fluid.tank;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FluidLoaderStandard extends FluidLoadingHandler {
    @Override
    public boolean fillItem(Level level, ItemStack[] slots, int in, int out, FluidTank tank) {
        return false;
    }

    @Override
    public boolean emptyItem(Level level, ItemStack[] slots, int in, int out, FluidTank tank) {
        return false;
    }
}
