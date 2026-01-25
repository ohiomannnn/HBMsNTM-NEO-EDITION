package com.hbm.inventory;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ModItems;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FluidContainerRegistry {

    // TODO: continue incorporating hashmaps into this
    public static List<FluidContainer> allContainers = new ArrayList<>();
    private static HashMap<FluidType, List<FluidContainer>> containerMap = new HashMap<>();

    public static void clearRegistry() {
        allContainers.clear();
        containerMap.clear();
    }

    public static void register() {

        FluidType[] fluids = Fluids.getAll();
        for (int i = 1; i < fluids.length; i++) {

            FluidType type = fluids[i];

            if (type.hasNoContainer()) continue;

            FluidContainerRegistry.registerContainer(new FluidContainer(new ItemStack(ModItems.FLUID_TANK_LEAD_FULL.get(), 1), new ItemStack(ModItems.FLUID_TANK_EMPTY.get()), type, 1000));

            if (type.needsLeadContainer()) continue;

            FluidContainerRegistry.registerContainer(new FluidContainer(new ItemStack(ModItems.FLUID_TANK_FULL.get(), 1), new ItemStack(ModItems.FLUID_TANK_EMPTY.get()), type, 1000));
            FluidContainerRegistry.registerContainer(new FluidContainer(new ItemStack(ModItems.FLUID_BARREL_FULL.get(), 1), new ItemStack(ModItems.FLUID_BARREL_EMPTY.get()), type, 16000));
        }
    }

    public static void registerContainer(FluidContainer container) {
        allContainers.add(container);

        if (!containerMap.containsKey(container.type)) containerMap.put(container.type, new ArrayList<>());

        List<FluidContainer> items = containerMap.get(container.type);
        items.add(container);
    }

    public static List<FluidContainer> getContainers(FluidType type) {
        return containerMap.get(type);
    }

    @Nullable
    public static FluidContainer getContainer(FluidType type, ItemStack stack) {
        if (stack.isEmpty()) return null;

        ItemStack sta = stack.copy();
        sta.setCount(1);

        if (!containerMap.containsKey(type)) return null;

        for (FluidContainer container : getContainers(type)) {
            if (container.emptyContainer != null && container.emptyContainer.equals(sta)) return container;
        }

        return null;
    }

    public static int getFluidContent(ItemStack stack, FluidType type) {
        if (stack.isEmpty()) return 0;

        ItemStack sta = stack.copy();
        sta.setCount(1);

        if (!containerMap.containsKey(type)) return 0;

        for (FluidContainer container : containerMap.get(type)) {
            if (container.fullContainer.equals(sta)) return container.content;
        }

        return 0;
    }

    public static FluidType getFluidType(ItemStack stack) {
        if (stack.isEmpty()) return Fluids.NONE;

        ItemStack sta = stack.copy();
        sta.setCount(1);

        for (FluidContainer container : allContainers) {
            if (container.fullContainer.equals(sta)) return container.type;
        }

        return Fluids.NONE;
    }

    public static ItemStack getFullContainer(ItemStack stack, FluidType type) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        ItemStack sta = stack.copy();
        sta.setCount(1);

        if (!containerMap.containsKey(type)) return ItemStack.EMPTY;

        for (FluidContainer container : containerMap.get(type)) {
            if (!container.emptyContainer.isEmpty() && container.emptyContainer.equals(sta)) return container.fullContainer.copy();
        }

        return ItemStack.EMPTY;
    }

    public static ItemStack getEmptyContainer(ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        ItemStack sta = stack.copy();
        sta.setCount(1);

        for (FluidContainer container : allContainers) {
            if (container.fullContainer.equals(sta)) return container.emptyContainer.isEmpty() ? ItemStack.EMPTY : container.emptyContainer.copy();
        }

        return ItemStack.EMPTY;
    }
}
