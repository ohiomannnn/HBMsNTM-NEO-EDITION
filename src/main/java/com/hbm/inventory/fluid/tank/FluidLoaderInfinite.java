package com.hbm.inventory.fluid.tank;

import com.hbm.items.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FluidLoaderInfinite extends FluidLoadingHandler {

    @Override
    public boolean fillItem(Level level, NonNullList<ItemStack> slots, int in, int out, FluidTank tank) {

        if (!(slots.get(in).is(ModItems.FLUID_BARREL_INFINITE.get()))) return false;
        tank.setFill(Math.max(tank.getFill() - 1_000_000_000, 0));

        return true;
    }

    @Override
    public boolean emptyItem(Level level, NonNullList<ItemStack> slots, int in, int out, FluidTank tank) {

        if (!(slots.get(in).is(ModItems.FLUID_BARREL_INFINITE.get()))) return false;
        tank.setFill(Math.min(tank.getFill() + 1_000_000_000, 0));

        return true;
    }
}
