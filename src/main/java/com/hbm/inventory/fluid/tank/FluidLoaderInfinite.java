package com.hbm.inventory.fluid.tank;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FluidLoaderInfinite extends FluidLoadingHandler {
    @Override
    public boolean fillItem(Level level, NonNullList<ItemStack> slots, int in, int out, FluidTank tank) {
        return false;
    }

    @Override
    public boolean emptyItem(Level level, NonNullList<ItemStack> slots, int in, int out, FluidTank tank) {
        return false;
    }
}
