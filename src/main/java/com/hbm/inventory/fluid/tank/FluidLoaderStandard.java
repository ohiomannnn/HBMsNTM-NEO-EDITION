package com.hbm.inventory.fluid.tank;

import com.hbm.inventory.FluidContainer;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.datacomps.FluidTypeComponent;
import com.hbm.items.datacomps.ModDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class FluidLoaderStandard extends FluidLoadingHandler {

    @Override
    public boolean fillItem(Level level, NonNullList<ItemStack> slots, int in, int out, FluidTank tank) {

        if (tank.pressure != 0) return false;
        if (slots.get(in).isEmpty()) return true;

        FluidType type = tank.getTankType();
        FluidContainer container = FluidContainerRegistry.getContainer(type, slots.get(in));

        if (!container.isEmpty() && !container.fullContainer.isEmpty() && tank.getFill() - container.content >= 0) {
            Component name = slots.get(in).has(DataComponents.CUSTOM_NAME) ? slots.get(in).getDisplayName() : null;

            if (slots.get(out).isEmpty()) {
                tank.setFill(tank.getFill() - container.content);
                slots.set(out, container.fullContainer.copy());
                slots.get(in).shrink(1);
                if (slots.get(in).getCount() <= 0) {
                    slots.set(in, ItemStack.EMPTY);
                }

                if (name != null) slots.get(out).set(DataComponents.CUSTOM_NAME, name);
            } else if (slots.get(out).getItem() == container.fullContainer.getItem() &&
                    Objects.equals(slots.get(out).get(ModDataComponents.FLUID_TYPE.get()), new FluidTypeComponent(container.type.getID())) &&
                    slots.get(out).getCount() < slots.get(out).getMaxStackSize()
            ) {
                tank.setFill(tank.getFill() - container.content);
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

            FluidContainer container = FluidContainerRegistry.getContainer(type, slots.get(in));

            Component name = slots.get(in).has(DataComponents.CUSTOM_NAME) ? slots.get(in).getDisplayName() : null;

            if (slots.get(out).isEmpty()) {

                tank.setFill(tank.getFill() + amount);
                slots.set(out, container.emptyContainer);

                if (!container.emptyContainer.isEmpty()) {
                    if (name != null) slots.get(out).set(DataComponents.CUSTOM_NAME, name);
                }

                slots.get(in).shrink(1);
                if (slots.get(in).getCount() <= 0) {
                    slots.set(in, ItemStack.EMPTY);
                }
            } else if ((container.emptyContainer.isEmpty() || (slots.get(out).getItem() == container.emptyContainer.getItem() &&
                    Objects.equals(slots.get(out).get(ModDataComponents.FLUID_TYPE.get()), new FluidTypeComponent(container.type.getID())) &&
                    slots.get(out).getCount() < slots.get(out).getMaxStackSize()))
            ) {
                tank.setFill(tank.getFill() + amount);
                slots.get(in).shrink(1);

                if (slots.get(in).getCount() <= 0) {
                    slots.set(in, ItemStack.EMPTY);
                }

                if (!container.emptyContainer.isEmpty()) {
                    slots.get(out).grow(1);
                    if (name != null) slots.get(out).set(DataComponents.CUSTOM_NAME, name);
                }
            }

            return true;
        }

        return false;
    }
}
