package com.hbm.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FuelItem extends Item {

    private final int burnTime;

    public FuelItem(Properties properties, int burnTime) {
        super(properties);
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack stack, net.minecraft.world.item.crafting.RecipeType<?> recipeType) {
        return this.burnTime;
    }
}
