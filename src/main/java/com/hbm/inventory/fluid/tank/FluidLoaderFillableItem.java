package com.hbm.inventory.fluid.tank;

import api.hbm.fluidmk2.IFillableItem;
import com.hbm.handler.ArmorModHandler;
import com.hbm.inventory.fluid.FluidType;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FluidLoaderFillableItem extends FluidLoadingHandler {

    @Override
    public boolean fillItem(Level level, NonNullList<ItemStack> slots, int in, int out, FluidTank tank) {
        return fill(level, slots.get(in), tank);
    }

    public boolean fill(Level level, ItemStack stack, FluidTank tank) {

        if (tank.pressure != 0) return false;

        if (stack.isEmpty()) return false;

        FluidType type = tank.getTankType();

        if (stack.getItem() instanceof ArmorItem && ArmorModHandler.hasMods(stack)) {
            for (ItemStack mod : ArmorModHandler.pryMods(level, stack)) {

                if (mod != null && mod.getItem() instanceof IFillableItem) {
                    fill(level, mod, tank);
                }
            }
        }

        if (!(stack.getItem() instanceof IFillableItem fillable)) return false;

        if (fillable.acceptsFluid(type, stack)) {
            tank.setFill(fillable.tryFill(type, tank.getFill(), stack));
        }

        return true;
    }


    @Override
    public boolean emptyItem(Level level, NonNullList<ItemStack> slots, int in, int out, FluidTank tank) {
        return empty(level, slots.get(in), tank);
    }

    public boolean empty(Level level, ItemStack stack, FluidTank tank) {

        FluidType type = tank.getTankType();

        if (stack.getItem() instanceof ArmorItem && ArmorModHandler.hasMods(stack)) {
            for (ItemStack mod : ArmorModHandler.pryMods(level, stack)) {

                if (mod != null && mod.getItem() instanceof IFillableItem) {
                    empty(level, mod, tank);
                }
            }
        }

        if (!(stack.getItem() instanceof IFillableItem fillable)) return false;

        if (fillable.providesFluid(type, stack)) {
            tank.setFill(tank.getFill() + fillable.tryEmpty(type, tank.getMaxFill() - tank.getFill(), stack));
        }

        return tank.getFill() == tank.getMaxFill();
    }
}
