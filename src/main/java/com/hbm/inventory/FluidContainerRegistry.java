package com.hbm.inventory;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.NtmItems;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FluidContainerRegistry {

    // TODO: continue incorporating hashmaps into this
    public static final List<FluidContainer> allContainers = new ArrayList<>();
    private static final HashMap<FluidType, List<FluidContainer>> containerMap = new HashMap<>();

    public static void clearRegistry() {
        allContainers.clear();
        containerMap.clear();
    }

    public static void register() {

        FluidType[] fluids = Fluids.getAll();
        for (int i = 1; i < fluids.length; i++) {

            FluidType type = fluids[i];
            int id = type.getID();

            if (type.hasNoContainer()) continue;

            FluidContainerRegistry.registerContainer(new FluidContainer(MetaHelper.newStack(NtmItems.FLUID_TANK_LEAD_FULL.get(), 1, id), new ItemStack(NtmItems.FLUID_TANK_LEAD_EMPTY.get()), type, 1000));

            if (type.needsLeadContainer()) continue;

            FluidContainerRegistry.registerContainer(new FluidContainer(MetaHelper.newStack(NtmItems.FLUID_TANK_FULL.get(), 1, id), new ItemStack(NtmItems.FLUID_TANK_EMPTY.get()), type, 1000));
            FluidContainerRegistry.registerContainer(new FluidContainer(MetaHelper.newStack(NtmItems.FLUID_BARREL_FULL.get(), 1, id), new ItemStack(NtmItems.FLUID_BARREL_EMPTY.get()), type, 16000));
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
        int meta = MetaHelper.getMeta(sta);

        if (!containerMap.containsKey(type)) {
            return 0;
        }

        for (FluidContainer container : containerMap.get(type)) {
            int containerMeta = MetaHelper.getMeta(container.fullContainer);

            if (container.fullContainer.is(sta.getItem()) && meta == containerMeta) {
                return container.content;
            }
        }

        return 0;
    }

    public static ItemStack getFullContainer(ItemStack stack, FluidType type) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        ItemStack sta = stack.copyWithCount(1);

        if (!containerMap.containsKey(type)) return ItemStack.EMPTY;

        for (FluidContainer container : containerMap.get(type)) {
            if (!container.emptyContainer.isEmpty() && container.emptyContainer.is(sta.getItem())) {
                return container.fullContainer.copy();
            }
        }

        return ItemStack.EMPTY;
    }

    public static ItemStack getEmptyContainer(ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        ItemStack sta = stack.copyWithCount(1);
        int meta = MetaHelper.getMeta(sta);

        for (FluidContainer container : allContainers) {
            int containerMeta = MetaHelper.getMeta(container.fullContainer);

            if (container.fullContainer.is(sta.getItem()) && meta == containerMeta) {
                return container.emptyContainer.isEmpty() ? ItemStack.EMPTY : container.emptyContainer.copy();
            }
        }

        return ItemStack.EMPTY;
    }
}
