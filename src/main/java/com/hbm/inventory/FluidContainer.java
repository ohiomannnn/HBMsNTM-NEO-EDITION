package com.hbm.inventory;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.datacomps.FluidTypeComponent;
import com.hbm.items.datacomps.ModDataComponents;
import net.minecraft.world.item.ItemStack;

public class FluidContainer {
    // Non-null container
    public static FluidContainer FLUID_CONTAINER_EMPTY = new FluidContainer(ItemStack.EMPTY, ItemStack.EMPTY, Fluids.NONE, 0);
    // The full container (e.g. deuterium cell)
    public ItemStack fullContainer;
    // The empty container (e.g. empty cell)
    public ItemStack emptyContainer;
    // The type of the contained liquid (e.g. deuterium)
    public FluidType type;
    // The amount of liquid stored in mB (e.g. 1000)
    public int content;

    public FluidContainer(ItemStack full, ItemStack empty, FluidType type, int amount) {
        full.set(ModDataComponents.FLUID_TYPE.get(), new FluidTypeComponent(type.getID()));
        empty.set(ModDataComponents.FLUID_TYPE.get(), new FluidTypeComponent(type.getID()));
        this.fullContainer = full;
        this.emptyContainer = empty;
        this.type = type;
        this.content = amount;
    }

    public boolean isEmpty() {
        return this == FLUID_CONTAINER_EMPTY;
    }
}