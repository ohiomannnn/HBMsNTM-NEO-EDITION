package com.hbm.inventory.fluid.tank;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class FluidLoadingHandler {
    public abstract boolean fillItem(Level level, ItemStack[] slots, int in, int out, FluidTank tank);
    public abstract boolean emptyItem(Level level, ItemStack[] slots, int in, int out, FluidTank tank);
}
