package com.hbm.inventory.fluid.tank;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class FluidLoadingHandler {
    public abstract boolean fillItem(Level level, NonNullList<ItemStack> slots, int in, int out, FluidTank tank);
    public abstract boolean emptyItem(Level level, NonNullList<ItemStack> slots, int in, int out, FluidTank tank);
}
