package com.hbm.inventory.tiers;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.SimpleTier;

public class ModTiers {
    public static Tier ALLOY = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2000, 15.0F, 5.0F, 5, () -> Ingredient.of(new ItemLike[]{Items.NETHERITE_INGOT}));
}
