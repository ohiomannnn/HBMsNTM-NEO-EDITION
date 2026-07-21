package com.hbm.inventory;

import com.hbm.items.NtmItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class NtmTiers {

    // pro tip
    // tier 0 - BlockTags.INCORRECT_FOR_WOODEN_TOOL
    // tier 1 - BlockTags.INCORRECT_FOR_STONE_TOOL
    // tier 2 - BlockTags.INCORRECT_FOR_IRON_TOOL
    // tier 3 - BlockTags.INCORRECT_FOR_DIAMOND_TOOL
    // tier 4 - BlockTags.INCORRECT_FOR_NETHERITE_TOOL

    public static Tier STEEL = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 500, 7.5F, 2.0F, 10, () -> Ingredient.of(NtmItems.NOTHING.get()));
    public static Tier TITANIUM = new SimpleTier(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 750, 9.0F, 2.5F, 15, () -> Ingredient.of(NtmItems.NOTHING.get()));

    public static Tier BOTTLE_OPENER = new SimpleTier(BlockTags.INCORRECT_FOR_STONE_TOOL, 250, 6.0F, 0.5F, 200, () -> Ingredient.of(NtmItems.INGOT_STEEL.get()));

    // Tool tiers mirroring the old 1.12.2 tool set.
    public static Tier TOOL_STEEL = new SimpleTier(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 750, 8.0F, 0.0F, 10, () -> Ingredient.of(NtmItems.INGOT_STEEL.get()));
    public static Tier TOOL_TITANIUM = new SimpleTier(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1000, 9.0F, 0.0F, 15, () -> Ingredient.of(NtmItems.INGOT_TITANIUM.get()));
    public static Tier TOOL_DESH = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 0, 7.5F, 0.0F, 10, () -> Ingredient.of(NtmItems.INGOT_DESH.get()));
    public static Tier TOOL_COBALT = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 750, 9.0F, 0.0F, 15, () -> Ingredient.of(NtmItems.INGOT_COBALT.get()));
    public static Tier TOOL_DECORATED_COBALT = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1000, 15.0F, 0.0F, 25, () -> Ingredient.of(NtmItems.INGOT_COBALT.get()));
    public static Tier TOOL_CMB = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 8500, 40.0F, 0.0F, 100, () -> Ingredient.of(NtmItems.INGOT_COMBINE_STEEL.get()));
    public static Tier TOOL_BISMUTH = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 0, 50.0F, 0.0F, 200, () -> Ingredient.of(NtmItems.INGOT_BISMUTH.get()));
    public static Tier TOOL_ZERO_POWER = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 0, 50.0F, 0.0F, 200, () -> Ingredient.of(NtmItems.INGOT_BISMUTH.get()));
    public static Tier TOOL_STARMETAL = new SimpleTier(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1000, 20.0F, 0.0F, 30, () -> Ingredient.of(NtmItems.INGOT_STARMETAL.get()));
    public static Tier TOOL_SCHRABIDIUM = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 10000, 50.0F, 0.0F, 200, () -> Ingredient.of(NtmItems.INGOT_SCHRABIDIUM.get()));
}
