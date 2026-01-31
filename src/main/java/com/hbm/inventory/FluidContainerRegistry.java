package com.hbm.inventory;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ModItems;
import com.hbm.items.datacomps.FluidTypeComponent;
import com.hbm.items.datacomps.ModDataComponents;
import net.minecraft.world.item.ItemStack;

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

            FluidContainerRegistry.registerContainer(new FluidContainer(new ItemStack(ModItems.FLUID_TANK_LEAD_FULL.get(), 1), new ItemStack(ModItems.FLUID_TANK_LEAD_EMPTY.get()), type, 1000));

            if (type.needsLeadContainer()) continue;

            FluidContainerRegistry.registerContainer(new FluidContainer(new ItemStack(ModItems.FLUID_TANK_FULL.get(), 1), new ItemStack(ModItems.FLUID_TANK_EMPTY.get()), type, 1000));
            FluidContainerRegistry.registerContainer(new FluidContainer(new ItemStack(ModItems.FLUID_BARREL_FULL.get(), 1), new ItemStack(ModItems.FLUID_BARREL_EMPTY.get()), type, 16000));
        }
    }

    public static void registerContainer(FluidContainer con) {
        allContainers.add(con);

        if (!containerMap.containsKey(con.type)) {
            containerMap.put(con.type, new ArrayList<>());
        }

        List<FluidContainer> items = containerMap.get(con.type);
        items.add(con);
    }

    public static int getFluidContent(ItemStack stack, FluidType type) {
        if (stack.isEmpty()) {
            return 0;
        }

        ItemStack sta = stack.copyWithCount(1);
        FluidTypeComponent staC = sta.get(ModDataComponents.FLUID_TYPE.get());
        if (staC == null) return 0;

        if (!containerMap.containsKey(type)) {
            return 0;
        }

        for (FluidContainer container : containerMap.get(type)) {
            if (container.fullContainer.is(sta.getItem()) &&
                    container.fullContainer.get(ModDataComponents.FLUID_TYPE.get()).fluidId() == staC.fluidId()) {
                return container.content;
            }
        }
        return 0;
    }

    public static ItemStack getFullContainer(ItemStack stack, FluidType type) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        ItemStack sta = stack.copyWithCount(1);
        FluidTypeComponent staC = sta.get(ModDataComponents.FLUID_TYPE.get());
        if (staC == null) return ItemStack.EMPTY;

        if (!containerMap.containsKey(type)) return ItemStack.EMPTY;

        for (FluidContainer container : containerMap.get(type)) {
            if (!container.emptyContainer.isEmpty() && container.emptyContainer.is(sta.getItem())
                    && container.emptyContainer.get(ModDataComponents.FLUID_TYPE.get()).fluidId() == staC.fluidId()) {
                return container.fullContainer.copy();
            }
        }

        return ItemStack.EMPTY;
    }

    public static ItemStack getEmptyContainer(ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        ItemStack sta = stack.copyWithCount(1);
        FluidTypeComponent staC = sta.get(ModDataComponents.FLUID_TYPE.get());
        if (staC == null) return ItemStack.EMPTY;

        for (FluidContainer container : allContainers) {
            if (container.fullContainer.is(sta.getItem()) &&
                    container.fullContainer.get(ModDataComponents.FLUID_TYPE.get()).fluidId() == staC.fluidId()) {
                return container.emptyContainer.isEmpty() ? ItemStack.EMPTY : container.emptyContainer.copy();
            }
        }

        return ItemStack.EMPTY;
    }
}
