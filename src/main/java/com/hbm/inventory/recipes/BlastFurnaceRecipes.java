package com.hbm.inventory.recipes;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.NtmItems;
import com.hbm.items.RawIngotItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class BlastFurnaceRecipes extends GenericRecipes<BlastFurnaceRecipe> {

    public static final BlastFurnaceRecipes INSTANCE = new BlastFurnaceRecipes();

    @Override public int inputItemLimit() { return 2; }
    @Override public int inputFluidLimit() { return 0; }
    @Override public int outputItemLimit() { return 2; }
    @Override public int outputFluidLimit() { return 0; }
    @Override public boolean hasPower() { return false; }
    @Override public String getFileName() { return "hbmBlastFurnace.json"; }
    @Override public BlastFurnaceRecipe instantiateRecipe(String name) { return new BlastFurnaceRecipe(name); }

    @Override
    public void registerDefaults() {
        if(!this.recipeOrderedList.isEmpty()) return;

        this.register(new BlastFurnaceRecipe("blast.steelFromIngot").setDuration(800)
                .inputItems(new ComparableStack(Items.IRON_INGOT, 2), new ComparableStack(Blocks.SAND))
                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get(), 2), slag(1)));
        this.register(new BlastFurnaceRecipe("blast.steelFromIngotRedSand").setDuration(800)
                .inputItems(new ComparableStack(Items.IRON_INGOT, 2), new ComparableStack(Blocks.RED_SAND))
                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get(), 2), slag(1)));
        this.register(new BlastFurnaceRecipe("blast.steelFromDust").setDuration(800)
                .inputItems(new ComparableStack(NtmItems.POWDER_IRON.get(), 2), new ComparableStack(Blocks.SAND))
                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get(), 2), slag(1)));
        this.register(new BlastFurnaceRecipe("blast.steelFromDustRedSand").setDuration(800)
                .inputItems(new ComparableStack(NtmItems.POWDER_IRON.get(), 2), new ComparableStack(Blocks.RED_SAND))
                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get(), 2), slag(1)));
        this.register(new BlastFurnaceRecipe("blast.steelFromOre").setDuration(800)
                .inputItems(new ComparableStack(Blocks.IRON_ORE), new ComparableStack(Blocks.SAND))
                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get(), 2), slag(2)));
        this.register(new BlastFurnaceRecipe("blast.steelFromDeepslateOre").setDuration(800)
                .inputItems(new ComparableStack(Blocks.DEEPSLATE_IRON_ORE), new ComparableStack(Blocks.SAND))
                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get(), 2), slag(2)));
        this.register(new BlastFurnaceRecipe("blast.steelWithFlux").setDuration(1200)
                .inputItems(new ComparableStack(Blocks.IRON_ORE), new ComparableStack(NtmItems.POWDER_FLUX.get()))
                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get(), 3), slag(2)));
        this.register(new BlastFurnaceRecipe("blast.steelWithFluxDeepslate").setDuration(1200)
                .inputItems(new ComparableStack(Blocks.DEEPSLATE_IRON_ORE), new ComparableStack(NtmItems.POWDER_FLUX.get()))
                .outputItems(new ItemStack(NtmItems.INGOT_STEEL.get(), 3), slag(2)));

        this.register(new BlastFurnaceRecipe("blast.mingrade").setDuration(400)
                .inputItems(new ComparableStack(NtmItems.INGOT_COPPER.get()), new ComparableStack(Items.REDSTONE))
                .outputItems(new ItemStack(NtmItems.INGOT_RED_COPPER.get(), 2)));
        this.register(new BlastFurnaceRecipe("blast.mingradeDust").setDuration(400)
                .inputItems(new ComparableStack(NtmItems.POWDER_COPPER.get()), new ComparableStack(Items.REDSTONE))
                .outputItems(new ItemStack(NtmItems.INGOT_RED_COPPER.get(), 2)));
        this.register(new BlastFurnaceRecipe("blast.mingradeIngot").setDuration(400)
                .inputItems(new ComparableStack(NtmItems.INGOT_COPPER.get()), new ComparableStack(redstoneIngot()))
                .outputItems(new ItemStack(NtmItems.INGOT_RED_COPPER.get(), 2)));
        this.register(new BlastFurnaceRecipe("blast.mingradeCursed").setDuration(400)
                .inputItems(new ComparableStack(NtmItems.POWDER_COPPER.get()), new ComparableStack(redstoneIngot()))
                .outputItems(new ItemStack(NtmItems.INGOT_RED_COPPER.get(), 2)));
        this.register(new BlastFurnaceRecipe("blast.mingradeOre").setDuration(1200)
                .inputItems(new ComparableStack(Blocks.COPPER_ORE), new ComparableStack(Items.REDSTONE, 6))
                .outputItems(new ItemStack(NtmItems.INGOT_RED_COPPER.get(), 6), slag(1)));
        this.register(new BlastFurnaceRecipe("blast.mingradeDeepslateOre").setDuration(1200)
                .inputItems(new ComparableStack(Blocks.DEEPSLATE_COPPER_ORE), new ComparableStack(Items.REDSTONE, 6))
                .outputItems(new ItemStack(NtmItems.INGOT_RED_COPPER.get(), 6), slag(1)));

        this.register(new BlastFurnaceRecipe("blast.meteor").setDuration(600)
                .inputItems(new ComparableStack(NtmItems.INGOT_COBALT.get()), new ComparableStack(NtmItems.POWDER_METEORITE.get()))
                .outputItems(new ItemStack(NtmItems.INGOT_METEORITE.get())));
        this.register(new BlastFurnaceRecipe("blast.starmetal").setDuration(600)
                .inputItems(new ComparableStack(NtmItems.INGOT_SATURNITE.get()), new ComparableStack(NtmItems.INGOT_METEORITE.get()))
                .outputItems(new ItemStack(NtmItems.INGOT_STARMETAL.get())));
        this.register(new BlastFurnaceRecipe("blast.paa").setDuration(600)
                .inputItems(new ComparableStack(Items.GOLD_INGOT), new ComparableStack(NtmItems.PLATE_MIXED.get()))
                .outputItems(new ItemStack(NtmItems.PLATE_PAA.get())));
        this.register(new BlastFurnaceRecipe("blast.firebrick").setDuration(800)
                .inputItems(new ComparableStack(NtmItems.POWDER_ALUMINIUM.get()), new ComparableStack(Items.CLAY_BALL, 7))
                .outputItems(new ItemStack(NtmItems.INGOT_FIREBRICK.get(), 8)));
        this.register(new BlastFurnaceRecipe("blast.firebrickLimestone").setDuration(800)
                .inputItems(new ComparableStack(NtmBlocks.RESOURCE_LIMESTONE.get()), new ComparableStack(Items.CLAY_BALL, 6))
                .outputItems(new ItemStack(NtmItems.INGOT_FIREBRICK.get(), 8)));
    }

    public BlastFurnaceRecipe getRecipe(ItemStack first, ItemStack second) {
        this.ensureDefaults();
        for(BlastFurnaceRecipe recipe : this.recipeOrderedList) {
            if(matchesInputs(recipe, first, second, true)) return recipe;
        }
        return null;
    }

    public boolean isItemValid(ItemStack stack) {
        this.ensureDefaults();
        if(stack.isEmpty()) return false;
        for(BlastFurnaceRecipe recipe : this.recipeOrderedList) {
            if(recipe.inputItem == null) continue;
            for(AStack input : recipe.inputItem) {
                if(input.matchesRecipe(stack, true)) return true;
            }
        }
        return false;
    }

    private void ensureDefaults() {
        if(this.recipeOrderedList.isEmpty()) {
            this.registerDefaults();
        }
    }

    public static boolean matchesInputs(BlastFurnaceRecipe recipe, ItemStack first, ItemStack second, boolean ignoreSize) {
        if(recipe.inputItem == null) return false;
        if(recipe.inputItem.length == 1) {
            return first.isEmpty() != second.isEmpty()
                    && (recipe.inputItem[0].matchesRecipe(first, ignoreSize) || recipe.inputItem[0].matchesRecipe(second, ignoreSize));
        }
        if(recipe.inputItem.length != 2 || first.isEmpty() || second.isEmpty()) return false;
        return recipe.inputItem[0].matchesRecipe(first, ignoreSize) && recipe.inputItem[1].matchesRecipe(second, ignoreSize)
                || recipe.inputItem[1].matchesRecipe(first, ignoreSize) && recipe.inputItem[0].matchesRecipe(second, ignoreSize);
    }

    private static ItemStack slag(int count) {
        return MetaHelper.newStack(NtmItems.INGOT_RAW.get(), count, RawIngotItem.Type.SLAG.meta);
    }

    private static ItemStack redstoneIngot() {
        return MetaHelper.newStack(NtmItems.INGOT_RAW.get(), 1, RawIngotItem.Type.REDSTONE.meta);
    }
}
