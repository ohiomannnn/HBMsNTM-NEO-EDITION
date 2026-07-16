package com.hbm.inventory.recipes.anvil;

import com.hbm.inventory.RecipesCommon.AStack;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AnvilSmithingRecipe {

    protected final int tier;
    protected final ItemStack output;
    protected final AStack left;
    protected final AStack right;
    private boolean shapeless;

    public AnvilSmithingRecipe(int tier, ItemStack output, AStack left, AStack right) {
        this.tier = tier;
        this.output = output;
        this.left = left;
        this.right = right;
    }

    public AnvilSmithingRecipe makeShapeless() {
        this.shapeless = true;
        return this;
    }

    public int getTier() {
        return this.tier;
    }

    public boolean isShapeless() {
        return this.shapeless;
    }

    public boolean matches(ItemStack left, ItemStack right) {
        return this.matchesInt(left, right) != -1;
    }

    public int matchesInt(ItemStack left, ItemStack right) {
        if(this.doesStackMatch(left, this.left) && this.doesStackMatch(right, this.right)) {
            return 0;
        }

        if(this.shapeless && this.doesStackMatch(left, this.right) && this.doesStackMatch(right, this.left)) {
            return 1;
        }

        return -1;
    }

    public boolean doesStackMatch(ItemStack input, AStack recipe) {
        return recipe.matchesRecipe(input, false);
    }

    public List<ItemStack> getLeft() {
        return this.left.extractForJEI();
    }

    public List<ItemStack> getRight() {
        return this.right.extractForJEI();
    }

    public ItemStack getSimpleOutput() {
        return this.output.copy();
    }

    public ItemStack getOutput(ItemStack left, ItemStack right) {
        return this.getSimpleOutput();
    }

    public int amountConsumed(int index, boolean mirrored) {
        if(index == 0) {
            return mirrored ? this.right.stacksize : this.left.stacksize;
        }

        if(index == 1) {
            return mirrored ? this.left.stacksize : this.right.stacksize;
        }

        return 0;
    }
}
