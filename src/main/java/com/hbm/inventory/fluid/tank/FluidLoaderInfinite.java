package com.hbm.inventory.fluid.tank;

import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.machine.InfiniteFluidItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FluidLoaderInfinite extends FluidLoadingHandler {

    @Override
    public boolean fillItem(Level level, NonNullList<ItemStack> slots, int in, int out, FluidTank tank) {

        if (!(slots.get(in).getItem() instanceof InfiniteFluidItem item)) return false;

        if (!item.allowPressure(tank.pressure)) return false;
        if (item.getType() != null && tank.type != item.getType()) return false;

        if (item.getChance() <= 1 || level.random.nextInt(item.getChance()) == 0) {
            tank.setFill(Math.max(tank.getFill() - item.getAmount(), 0));
        }

        return true;
    }

    @Override
    public boolean emptyItem(Level level, NonNullList<ItemStack> slots, int in, int out, FluidTank tank) {

        if (!(slots.get(in).getItem() instanceof InfiniteFluidItem item) || tank.getTankType() == Fluids.NONE) return false;

        if (item.getType() != null && tank.type != item.getType()) return false;

        if (item.getChance() <= 1 || level.random.nextInt(item.getChance()) == 0) {
            tank.setFill(Math.min(tank.getFill() + item.getAmount(), tank.getMaxFill()));
        }

        return true;
    }
}
