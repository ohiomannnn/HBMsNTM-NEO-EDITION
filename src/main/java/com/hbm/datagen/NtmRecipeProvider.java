package com.hbm.datagen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.items.NtmItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class NtmRecipeProvider extends RecipeProvider {

    public NtmRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.EGG_BALEFIRE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', NtmItems.EGG_BALEFIRE_SHARD.get())
                .unlockedBy("has_balefire_shard", has(NtmItems.EGG_BALEFIRE_SHARD.get()))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NtmItems.EGG_BALEFIRE_SHARD.get(), 9)
                .requires(NtmItems.EGG_BALEFIRE.get())
                .unlockedBy("has_balefire_egg", has(NtmItems.EGG_BALEFIRE.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.UPGRADE_TEMPLATE.get(), 1)
                .pattern("ACA")
                .pattern("BDB")
                .pattern("ACA")
                .define('A', NtmItems.WIRE_COPPER.get())
                .define('B', NtmItems.INSULATOR.get())
                .define('C', NtmItems.PLATE_IRON.get())
                .define('D', NtmItems.CIRCUIT_ANALOG_BOARD.get())
                .unlockedBy("has_plate_iron", has(NtmItems.PLATE_IRON.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "upgrade_template_from_iron"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.UPGRADE_TEMPLATE.get(), 1)
                .pattern("ACA")
                .pattern("BDB")
                .pattern("ACA")
                .define('A', NtmItems.WIRE_COPPER.get())
                .define('B', NtmItems.INSULATOR.get())
                .define('C', NtmItems.INGOT_POLYMER.get())
                .define('D', NtmItems.CIRCUIT_INTEGRATED_BOARD.get())
                .unlockedBy("has_polymer", has(NtmItems.INGOT_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "upgrade_template_from_polymer"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_PRINTED_BOARD.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern("   ")
                .define('B', NtmItems.PLATE_COPPER.get())
                .define('A', NtmItems.INSULATOR.get())
                .unlockedBy("has_insulator", has(NtmItems.INSULATOR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_printed_board_from_copper_plate"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_PRINTED_BOARD.get(), 4)
                .pattern(" A ")
                .pattern(" B ")
                .pattern("   ")
                .define('B', NtmItems.PLATE_GOLD.get())
                .define('A', NtmItems.INSULATOR.get())
                .unlockedBy("has_insulator", has(NtmItems.INSULATOR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_printed_board_from_gold_plate"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CAPACITOR.get(), 2)
                .pattern("   ")
                .pattern("ABA")
                .pattern("C C")
                .define('B', NtmItems.POWDER_ALUMINIUM.get())
                .define('A', NtmItems.INSULATOR.get())
                .define('C', NtmItems.WIRE_COPPER.get())
                .unlockedBy("has_insulator", has(NtmItems.INSULATOR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_capacitor_from_aluminium_powder_and_copper_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CAPACITOR.get(), 2)
                .pattern("   ")
                .pattern("ABA")
                .pattern("C C")
                .define('B', NtmItems.POWDER_ALUMINIUM.get())
                .define('A', NtmItems.INSULATOR.get())
                .define('C', NtmItems.WIRE_ALUMINIUM.get())
                .unlockedBy("has_insulator", has(NtmItems.INSULATOR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_capacitor_from_aluminium_powder_and_aluminium_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CAPACITOR.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('B', NtmItems.NUGGET_NIOBIUM.get())
                .define('A', NtmItems.INSULATOR.get())
                .define('C', NtmItems.WIRE_COPPER.get())
                .unlockedBy("has_insulator", has(NtmItems.INSULATOR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_capacitor_from_niobium_nugget_and_copper_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CAPACITOR.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('B', NtmItems.FRAGMENT_NIOBIUM.get())
                .define('A', NtmItems.INSULATOR.get())
                .define('C', NtmItems.WIRE_COPPER.get())
                .unlockedBy("has_insulator", has(NtmItems.INSULATOR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_capacitor_from_niobium_fragment_and_copper_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CAPACITOR.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('B', NtmItems.NUGGET_NIOBIUM.get())
                .define('A', NtmItems.INSULATOR.get())
                .define('C', NtmItems.WIRE_ALUMINIUM.get())
                .unlockedBy("has_insulator", has(NtmItems.INSULATOR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_capacitor_from_niobium_nugget_and_aluminium_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CAPACITOR.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('B', NtmItems.FRAGMENT_NIOBIUM.get())
                .define('A', NtmItems.INSULATOR.get())
                .define('C', NtmItems.WIRE_ALUMINIUM.get())
                .unlockedBy("has_insulator", has(NtmItems.INSULATOR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_capacitor_from_niobium_fragment_and_aluminium_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_VACUUM_TUBE.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('B', NtmItems.WIRE_TUNGSTEN.get())
                .define('A', Items.GLASS_PANE)
                .define('C', NtmItems.INSULATOR.get())
                .unlockedBy("has_insulator", has(NtmItems.INSULATOR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_vacuum_tube_from_tungsten_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_VACUUM_TUBE.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('B', NtmItems.WIRE_CARBON.get())
                .define('A', Items.GLASS_PANE)
                .define('C', NtmItems.INSULATOR.get())
                .unlockedBy("has_insulator", has(NtmItems.INSULATOR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_vacuum_tube_from_carbon_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.MOTOR.get(), 2)
                .pattern(" A ")
                .pattern("BCB")
                .pattern("BDB")
                .define('A', NtmItems.WIRE_GRADE_COPPER.get())
                .define('B', NtmItems.PLATE_IRON.get())
                .define('C', NtmItems.COIL_COPPER.get())
                .define('D', NtmItems.COIL_COPPER_RING.get())
                .unlockedBy("has_insulator", has(NtmItems.INSULATOR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "motor_from_iron_plates"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.MOTOR.get(), 2)
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" D ")
                .define('A', NtmItems.WIRE_GRADE_COPPER.get())
                .define('B', NtmItems.PLATE_STEEL.get())
                .define('C', NtmItems.COIL_COPPER.get())
                .define('D', NtmItems.COIL_COPPER_RING.get())
                .unlockedBy("has_insulator", has(NtmItems.INSULATOR.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "motor_from_steel_plates"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_MOSSY.get(), 8)
                .pattern("BBB")
                .pattern("BVB")
                .pattern("BBB")
                .define('B', NtmBlocks.BRICK_CONCRETE.get())
                .define('V', Items.VINE)
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_CRACKED.get(), 6)
                .pattern(" B ")
                .pattern("B B")
                .pattern(" B ")
                .define('B', NtmBlocks.BRICK_CONCRETE.get())
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_BROKEN.get(), 6)
                .pattern(" B ")
                .pattern("B B")
                .pattern(" B ")
                .define('B', NtmBlocks.BRICK_CONCRETE_CRACKED.get())
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        stairBuilder(NtmBlocks.BRICK_CONCRETE_STAIRS.get(), Ingredient.of(NtmBlocks.BRICK_CONCRETE))
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        stairBuilder(NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get(), Ingredient.of(NtmBlocks.BRICK_CONCRETE_MOSSY))
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        stairBuilder(NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get(), Ingredient.of(NtmBlocks.BRICK_CONCRETE_CRACKED))
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);
        stairBuilder(NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get(), Ingredient.of(NtmBlocks.BRICK_CONCRETE_BROKEN))
                .unlockedBy("can_craft_bricks", has(NtmBlocks.BRICK_CONCRETE))
                .save(recipeOutput);

        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_SLAB.get(), NtmBlocks.BRICK_CONCRETE.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB.get(), NtmBlocks.BRICK_CONCRETE_MOSSY.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB.get(), NtmBlocks.BRICK_CONCRETE_CRACKED.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB.get(), NtmBlocks.BRICK_CONCRETE_BROKEN.get());

        addOreSmelting(recipeOutput, NtmBlocks.ORE_BERYLLIUM.get(), NtmBlocks.ORE_BERYLLIUM_DEEPSLATE.get(), NtmItems.INGOT_BERYLLIUM.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_URANIUM.get(), NtmBlocks.ORE_URANIUM_DEEPSLATE.get(), NtmItems.INGOT_URANIUM.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_TUNGSTEN.get(), NtmBlocks.ORE_TUNGSTEN_DEEPSLATE.get(), NtmItems.INGOT_TUNGSTEN.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_TITANIUM.get(), NtmBlocks.ORE_TITANIUM_DEEPSLATE.get(), NtmItems.INGOT_TITANIUM.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_LEAD.get(), NtmBlocks.ORE_LEAD_DEEPSLATE.get(), NtmItems.INGOT_LEAD.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_ALUMINIUM.get(), NtmBlocks.ORE_ALUMINIUM_DEEPSLATE.get(), NtmItems.INGOT_ALUMINIUM.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_ASBESTOS.get(), NtmBlocks.ORE_ASBESTOS_DEEPSLATE.get(), NtmItems.ASBESTOS_SHEET.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_THORIUM.get(), NtmBlocks.ORE_THORIUM_DEEPSLATE.get(), NtmItems.INGOT_THORIUM_FUEL.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_NITER.get(), NtmBlocks.ORE_NITER_DEEPSLATE.get(), NtmItems.NITER.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_COBALT.get(), NtmBlocks.ORE_COBALT_DEEPSLATE.get(), NtmItems.FRAGMENT_COBALT.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_CINNABAR.get(), NtmBlocks.ORE_CINNABAR_DEEPSLATE.get(), NtmItems.CINNABAR.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_FLUORITE.get(), NtmBlocks.ORE_FLUORITE_DEEPSLATE.get(), NtmItems.FLUORITE.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_RARE.get(), NtmBlocks.ORE_RARE_DEEPSLATE.get(), NtmItems.RARE_EARTH_ORE_CHUNK.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_SULFUR.get(), NtmBlocks.ORE_SULFUR_DEEPSLATE.get(), NtmItems.SULFUR.get(), 0.7F);
        addOreSmelting(recipeOutput, NtmBlocks.ORE_LIGNITE.get(), NtmItems.LIGNITE.get(), 0.1F);

        addNuggetCrafting(recipeOutput, NtmItems.INGOT_URANIUM.get(), NtmItems.NUGGET_URANIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_U233.get(), NtmItems.NUGGET_U233.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_U235.get(), NtmItems.NUGGET_U235.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_U238.get(), NtmItems.NUGGET_U238.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_U238M2.get(), NtmItems.NUGGET_U238M2.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_PLUTONIUM.get(), NtmItems.NUGGET_PLUTONIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_PU238.get(), NtmItems.NUGGET_PU238.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_PU239.get(), NtmItems.NUGGET_PU239.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_PU240.get(), NtmItems.NUGGET_PU240.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_PU241.get(), NtmItems.NUGGET_PU241.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_PU_MIX.get(), NtmItems.NUGGET_PU_MIX.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_AM241.get(), NtmItems.NUGGET_AM241.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_AM242.get(), NtmItems.NUGGET_AM242.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_AM_MIX.get(), NtmItems.NUGGET_AM_MIX.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_TECHNETIUM.get(), NtmItems.NUGGET_TECHNETIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_NEPTUNIUM.get(), NtmItems.NUGGET_NEPTUNIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_POLONIUM.get(), NtmItems.NUGGET_POLONIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_THORIUM_FUEL.get(), NtmItems.NUGGET_THORIUM_FUEL.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_URANIUM_FUEL.get(), NtmItems.NUGGET_URANIUM_FUEL.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_MOX_FUEL.get(), NtmItems.NUGGET_MOX_FUEL.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_PLUTONIUM_FUEL.get(), NtmItems.NUGGET_PLUTONIUM_FUEL.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_NEPTUNIUM_FUEL.get(), NtmItems.NUGGET_NEPTUNIUM_FUEL.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_AMERICIUM_FUEL.get(), NtmItems.NUGGET_AMERICIUM_FUEL.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_SCHRABIDIUM_FUEL.get(), NtmItems.NUGGET_SCHRABIDIUM_FUEL.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_HES.get(), NtmItems.NUGGET_HES.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_LES.get(), NtmItems.NUGGET_LES.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_LEAD.get(), NtmItems.NUGGET_LEAD.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_BERYLLIUM.get(), NtmItems.NUGGET_BERYLLIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_CADMIUM.get(), NtmItems.NUGGET_CADMIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_BISMUTH.get(), NtmItems.NUGGET_BISMUTH.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_ARSENIC.get(), NtmItems.NUGGET_ARSENIC.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_ZIRCONIUM.get(), NtmItems.NUGGET_ZIRCONIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_TANTALIUM.get(), NtmItems.NUGGET_TANTALIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_DESH.get(), NtmItems.NUGGET_DESH.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_OSMIRIDIUM.get(), NtmItems.NUGGET_OSMIRIDIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_SCHRABIDIUM.get(), NtmItems.NUGGET_SCHRABIDIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_SOLINIUM.get(), NtmItems.NUGGET_SOLINIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_EUPHEMIUM.get(), NtmItems.NUGGET_EUPHEMIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_DINEUTRONIUM.get(), NtmItems.NUGGET_DINEUTRONIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_NIOBIUM.get(), NtmItems.NUGGET_NIOBIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_SILICON.get(), NtmItems.NUGGET_SILICON.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_ACTINIUM.get(), NtmItems.NUGGET_ACTINIUM.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_COBALT.get(), NtmItems.NUGGET_COBALT.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_CO60.get(), NtmItems.NUGGET_CO60.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_SR90.get(), NtmItems.NUGGET_SR90.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_PB209.get(), NtmItems.NUGGET_PB209.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_GH336.get(), NtmItems.NUGGET_GH336.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_AU198.get(), NtmItems.NUGGET_AU198.get());
        addNuggetCrafting(recipeOutput, NtmItems.INGOT_RA226.get(), NtmItems.NUGGET_RA226.get());

        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_URANIUM.get(), NtmItems.INGOT_URANIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_U233.get(), NtmItems.INGOT_U233.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_U235.get(), NtmItems.INGOT_U235.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_U238.get(), NtmItems.INGOT_U238.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_U238M2.get(), NtmItems.INGOT_U238M2.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_PLUTONIUM.get(), NtmItems.INGOT_PLUTONIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_PU238.get(), NtmItems.INGOT_PU238.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_PU239.get(), NtmItems.INGOT_PU239.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_PU240.get(), NtmItems.INGOT_PU240.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_PU241.get(), NtmItems.INGOT_PU241.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_PU_MIX.get(), NtmItems.INGOT_PU_MIX.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_AM241.get(), NtmItems.INGOT_AM241.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_AM242.get(), NtmItems.INGOT_AM242.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_AM_MIX.get(), NtmItems.INGOT_AM_MIX.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_TECHNETIUM.get(), NtmItems.INGOT_TECHNETIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_NEPTUNIUM.get(), NtmItems.INGOT_NEPTUNIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_POLONIUM.get(), NtmItems.INGOT_POLONIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_THORIUM_FUEL.get(), NtmItems.INGOT_THORIUM_FUEL.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_URANIUM_FUEL.get(), NtmItems.INGOT_URANIUM_FUEL.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_MOX_FUEL.get(), NtmItems.INGOT_MOX_FUEL.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_PLUTONIUM_FUEL.get(), NtmItems.INGOT_PLUTONIUM_FUEL.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_NEPTUNIUM_FUEL.get(), NtmItems.INGOT_NEPTUNIUM_FUEL.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_AMERICIUM_FUEL.get(), NtmItems.INGOT_AMERICIUM_FUEL.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_SCHRABIDIUM_FUEL.get(), NtmItems.INGOT_SCHRABIDIUM_FUEL.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_HES.get(), NtmItems.INGOT_HES.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_LES.get(), NtmItems.INGOT_LES.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_LEAD.get(), NtmItems.INGOT_LEAD.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_BERYLLIUM.get(), NtmItems.INGOT_BERYLLIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_CADMIUM.get(), NtmItems.INGOT_CADMIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_BISMUTH.get(), NtmItems.INGOT_BISMUTH.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_ARSENIC.get(), NtmItems.INGOT_ARSENIC.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_ZIRCONIUM.get(), NtmItems.INGOT_ZIRCONIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_TANTALIUM.get(), NtmItems.INGOT_TANTALIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_DESH.get(), NtmItems.INGOT_DESH.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_OSMIRIDIUM.get(), NtmItems.INGOT_OSMIRIDIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_SCHRABIDIUM.get(), NtmItems.INGOT_SCHRABIDIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_SOLINIUM.get(), NtmItems.INGOT_SOLINIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_EUPHEMIUM.get(), NtmItems.INGOT_EUPHEMIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_DINEUTRONIUM.get(), NtmItems.INGOT_DINEUTRONIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_NIOBIUM.get(), NtmItems.INGOT_NIOBIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_SILICON.get(), NtmItems.INGOT_SILICON.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_ACTINIUM.get(), NtmItems.INGOT_ACTINIUM.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_COBALT.get(), NtmItems.INGOT_COBALT.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_CO60.get(), NtmItems.INGOT_CO60.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_SR90.get(), NtmItems.INGOT_SR90.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_PB209.get(), NtmItems.INGOT_PB209.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_GH336.get(), NtmItems.INGOT_GH336.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_AU198.get(), NtmItems.INGOT_AU198.get());
        addNuggetDeCrafting(recipeOutput, NtmItems.NUGGET_RA226.get(), NtmItems.INGOT_RA226.get());
    }

    private void addNuggetCrafting(RecipeOutput recipeOutput, Item result, Item nugget) {
        String resultName = BuiltInRegistries.ITEM.getKey(result).getPath();
        String nuggetName = BuiltInRegistries.ITEM.getKey(nugget).getPath();
        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath("hbmsntm", resultName + "_from_" + nuggetName);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 1)
                .pattern("nnn")
                .pattern("nnn")
                .pattern("nnn")
                .define('n', nugget)
                .unlockedBy("has_" + nuggetName, has(nugget))
                .save(recipeOutput, recipeId);
    }

    private void addNuggetDeCrafting(RecipeOutput recipeOutput, Item nugget, Item result) {
        String nuggetName = BuiltInRegistries.ITEM.getKey(nugget).getPath();
        String resultName = BuiltInRegistries.ITEM.getKey(result).getPath();
        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath("hbmsntm", nuggetName + "_from_" + resultName);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget, 9)
                .requires(result)
                .unlockedBy("has_" + resultName, has(result))
                .save(recipeOutput, recipeId);
    }

    private void addOreSmelting(RecipeOutput recipeOutput, Block input, Block deepslateInput, Item result, float experience) {
        addOreSmelting(recipeOutput, input, result, experience);
        addOreSmelting(recipeOutput, deepslateInput, result, experience);
    }

    private void addOreSmelting(RecipeOutput recipeOutput, Block input, Item result, float experience) {
        String inputName = BuiltInRegistries.BLOCK.getKey(input).getPath();
        String resultName = BuiltInRegistries.ITEM.getKey(result).getPath();
        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath("hbmsntm", resultName + "_from_smelting_" + inputName);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC, result, experience, 200)
                .unlockedBy("has_" + inputName, has(input))
                .save(recipeOutput, recipeId);
    }
}
