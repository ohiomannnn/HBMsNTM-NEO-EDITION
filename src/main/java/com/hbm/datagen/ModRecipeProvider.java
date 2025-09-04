package com.hbm.datagen;

import com.hbm.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }
    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BRICK_CONCRETE_MOSSY.get(), 8)
                .pattern("BBB")
                .pattern("BVB")
                .pattern("BBB")
                .define('B', ModBlocks.BRICK_CONCRETE.get())
                .define('V', Items.VINE)
                .unlockedBy("can_craft_bricks", has(ModBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BRICK_CONCRETE_CRACKED.get(), 6)
                .pattern(" B ")
                .pattern("B B")
                .pattern(" B ")
                .define('B', ModBlocks.BRICK_CONCRETE.get())
                .unlockedBy("can_craft_bricks", has(ModBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BRICK_CONCRETE_BROKEN.get(), 6)
                .pattern(" B ")
                .pattern("B B")
                .pattern(" B ")
                .define('B', ModBlocks.BRICK_CONCRETE_CRACKED.get())
                .unlockedBy("can_craft_bricks", has(ModBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        stairBuilder(ModBlocks.BRICK_CONCRETE_STAIRS.get(), Ingredient.of(ModBlocks.BRICK_CONCRETE))
                .unlockedBy("can_craft_bricks", has(ModBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        stairBuilder(ModBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get(), Ingredient.of(ModBlocks.BRICK_CONCRETE_MOSSY))
                .unlockedBy("can_craft_bricks", has(ModBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        stairBuilder(ModBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get(), Ingredient.of(ModBlocks.BRICK_CONCRETE_CRACKED))
                .unlockedBy("can_craft_bricks", has(ModBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        stairBuilder(ModBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get(), Ingredient.of(ModBlocks.BRICK_CONCRETE_BROKEN))
                .unlockedBy("can_craft_bricks", has(ModBlocks.BRICK_CONCRETE))
                .save(recipeOutput);

        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BRICK_CONCRETE_SLAB.get(), ModBlocks.BRICK_CONCRETE.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BRICK_CONCRETE_MOSSY_SLAB.get(), ModBlocks.BRICK_CONCRETE_MOSSY.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BRICK_CONCRETE_CRACKED_SLAB.get(), ModBlocks.BRICK_CONCRETE_CRACKED.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BRICK_CONCRETE_BROKEN_SLAB.get(), ModBlocks.BRICK_CONCRETE_BROKEN.get());
    }
}
