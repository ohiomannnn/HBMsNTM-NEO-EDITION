package com.hbm.inventory.fluid.tank;

import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.datacomps.ModDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FluidLoaderStandard extends FluidLoadingHandler {

    @Override
    public boolean fillItem(Level level, NonNullList<ItemStack> slots, int in, int out, FluidTank tank) {

        if (tank.pressure != 0) return false;
        if (slots.get(in).isEmpty()) return true;

        FluidType type = tank.getTankType();
        ItemStack full = FluidContainerRegistry.getFullContainer(slots.get(in), type);

        if (!full.isEmpty() && tank.getFill() - FluidContainerRegistry.getFluidContent(full, type) >= 0) {
            Component name = slots.get(in).has(DataComponents.CUSTOM_NAME) ? slots.get(in).getHoverName() : null;

            if (slots.get(out).isEmpty()) {
                tank.setFill(tank.getFill() - FluidContainerRegistry.getFluidContent(full, type));
                slots.set(out, full.copy());
                slots.get(in).shrink(1);
                if (slots.get(in).getCount() <= 0) {
                    slots.set(in, ItemStack.EMPTY);
                }

                if (name != null) slots.get(out).set(DataComponents.CUSTOM_NAME, name);

            } else if (slots.get(out).getItem() == full.getItem() &&
                    slots.get(out).get(ModDataComponents.FLUID_TYPE.get()) == full.get(ModDataComponents.FLUID_TYPE.get()) &&
                    slots.get(out).getCount() < slots.get(out).getMaxStackSize()
            ) {
                tank.setFill(tank.getFill() - FluidContainerRegistry.getFluidContent(full, type));
                slots.get(in).shrink(1);

                if (slots.get(in).getCount() <= 0) {
                    slots.set(in, ItemStack.EMPTY);
                }
                slots.get(out).grow(1);

                if (name != null) slots.get(out).set(DataComponents.CUSTOM_NAME, name);
            }
        }

        return false;
    }

    @Override
    public boolean emptyItem(Level level, NonNullList<ItemStack> slots, int in, int out, FluidTank tank) {
        if (slots.get(in).isEmpty()) return true;

        FluidType type = tank.getTankType();
        int amount = FluidContainerRegistry.getFluidContent(slots.get(in), type);

        if (amount > 0 && tank.getFill() + amount <= tank.maxFluid) {
            ItemStack emptyContainer = FluidContainerRegistry.getEmptyContainer(slots.get(in));

            Component name = slots.get(in).has(DataComponents.CUSTOM_NAME) ? slots.get(in).getHoverName() : null;

            if (slots.get(out).isEmpty()) {

                tank.setFill(tank.getFill() + amount);
                slots.set(out, emptyContainer);

                if (!emptyContainer.isEmpty()) {
                    if (name != null) slots.get(out).set(DataComponents.CUSTOM_NAME, name);
                }

                slots.get(in).shrink(1);
                if (slots.get(in).getCount() <= 0) {
                    slots.set(in, ItemStack.EMPTY);
                }
            } else if (!slots.get(out).isEmpty() && (emptyContainer.isEmpty() || (slots.get(out).getItem() == emptyContainer.getItem() &&
                    slots.get(out).get(ModDataComponents.FLUID_TYPE.get()) == emptyContainer.get(ModDataComponents.FLUID_TYPE.get()) &&
                    slots.get(out).getCount() < slots.get(out).getMaxStackSize()))
            ) {
                tank.setFill(tank.getFill() + amount);
                slots.get(in).shrink(1);

                if (slots.get(in).getCount() <= 0) {
                    slots.set(in, ItemStack.EMPTY);
                }

                if (!emptyContainer.isEmpty()) {
                    slots.get(out).grow(1);
                    if (name != null) slots.get(out).set(DataComponents.CUSTOM_NAME, name);
                }
            }

            return true;
        }

        return false;
    }
}
