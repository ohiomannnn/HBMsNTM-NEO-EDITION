package com.hbm.inventory;

import com.hbm.items.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.SimpleTier;

public class ModTiers {

    // pro tip
    // tier 0 - BlockTags.INCORRECT_FOR_WOODEN_TOOL
    // tier 1 - BlockTags.INCORRECT_FOR_STONE_TOOL
    // tier 2 - BlockTags.INCORRECT_FOR_IRON_TOOL
    // tier 3 - BlockTags.INCORRECT_FOR_DIAMOND_TOOL
    // tier 4 - BlockTags.INCORRECT_FOR_NETHERITE_TOOL

    public static Tier STEEL = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 500, 7.5F, 2.0F, 10, () -> Ingredient.of(ModItems.NOTHING.get()));
    public static Tier TITANIUM = new SimpleTier(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 750, 9.0F, 2.5F, 15, () -> Ingredient.of(ModItems.NOTHING.get()));
    public static Tier ALLOY = new SimpleTier(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 2000, 15.0F, 5.0F, 5, () -> Ingredient.of(ModItems.NOTHING.get()));
}
