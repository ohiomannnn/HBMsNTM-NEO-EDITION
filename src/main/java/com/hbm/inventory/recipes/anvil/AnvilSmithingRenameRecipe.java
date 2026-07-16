package com.hbm.inventory.recipes.anvil;

import com.hbm.inventory.RecipesCommon.ComparableStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AnvilSmithingRenameRecipe extends AnvilSmithingRecipe {

    public AnvilSmithingRenameRecipe() {
        super(1, new ItemStack(Items.IRON_SWORD), new ComparableStack(Items.IRON_SWORD), new ComparableStack(Items.NAME_TAG, 1));
    }

    @Override
    public boolean matches(ItemStack left, ItemStack right) {
        return doesStackMatch(right, this.right) && right.has(DataComponents.CUSTOM_NAME);
    }

    @Override
    public int matchesInt(ItemStack left, ItemStack right) {
        return matches(left, right) ? 0 : -1;
    }

    @Override
    public ItemStack getOutput(ItemStack left, ItemStack right) {
        ItemStack out = left.copy();
        out.setCount(1);

        Component name = right.getHoverName();
        if (name != null) {
            out.set(DataComponents.CUSTOM_NAME, name);
        }

        return out;
    }

    @Override
    public int amountConsumed(int index, boolean mirrored) {
        if (index == 0) {
            return mirrored ? 0 : left.stacksize;
        }

        if (index == 1) {
            return mirrored ? left.stacksize : 0;
        }

        return 0;
    }
}
