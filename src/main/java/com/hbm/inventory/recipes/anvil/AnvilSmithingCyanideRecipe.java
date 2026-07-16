package com.hbm.inventory.recipes.anvil;

import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.items.NtmItems;
import com.hbm.util.TagsUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AnvilSmithingCyanideRecipe extends AnvilSmithingRecipe {

    public AnvilSmithingCyanideRecipe() {
        super(1, new ItemStack(Items.BREAD), new ComparableStack(Items.BREAD), new ComparableStack(NtmItems.PLAN_C.get()));
    }

    @Override
    public boolean matches(ItemStack left, ItemStack right) {
        return doesStackMatch(right, this.right) && left.getItem().getFoodProperties(left, null) != null;
    }

    @Override
    public int matchesInt(ItemStack left, ItemStack right) {
        return matches(left, right) ? 0 : -1;
    }

    @Override
    public ItemStack getOutput(ItemStack left, ItemStack right) {
        ItemStack out = left.copy();
        out.setCount(1);

        CompoundTag tag = TagsUtil.getCustomData(out);
        tag.putBoolean("ntmCyanide", true);
        TagsUtil.putCustomData(out, tag);

        return out;
    }
}
