package com.hbm.inventory.recipes;

import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes.IOutput;
import net.minecraft.world.item.ItemStack;

public class BlastFurnaceRecipe extends GenericRecipe {

    public BlastFurnaceRecipe(String name) {
        super(name);
    }

    @Override
    public BlastFurnaceRecipe setDuration(int duration) {
        super.setDuration(duration);
        return this;
    }

    @Override
    public BlastFurnaceRecipe inputItems(AStack... input) {
        super.inputItems(input);
        return this;
    }

    @Override
    public BlastFurnaceRecipe outputItems(ItemStack... output) {
        super.outputItems(output);
        return this;
    }

    @Override
    public BlastFurnaceRecipe outputItems(IOutput... output) {
        super.outputItems(output);
        return this;
    }
}
