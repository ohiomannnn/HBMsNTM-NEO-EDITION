package com.hbm.datagen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.generic.BarbedWireBlock;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.BoltItem;
import com.hbm.items.CastPlateItem;
import com.hbm.items.NtmItems;
import com.hbm.items.component.NtmDataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

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
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING, 3)
                .requires(NtmBlocks.PLANT_FLOWER.get())
                .unlockedBy("has_plant_flower", has(NtmBlocks.PLANT_FLOWER.get()))
                .save(recipeOutput);
        // this 2 below, to delete later
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.GOLD_BLOCK, 1)
                .pattern(" B ")
                .pattern("BGB")
                .pattern(" B ")
                .define('B', DataComponentIngredient.of(false, NtmDataComponents.META, BoltItem.Type.DURA_STEEL.meta, NtmItems.BOLT.get()))
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_bolt", has(NtmItems.BOLT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "example_gold_block_from_dura_steel_bolts"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.GOLD_BLOCK, 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', DataComponentIngredient.of(false, NtmDataComponents.META, CastPlateItem.Type.IRON.ordinal(), NtmItems.CAST_PLATE.get()))
                .unlockedBy("has_iron_cast_plate", has(NtmItems.CAST_PLATE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "gold_block_from_iron_cast_plates"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CAST_PLATE.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "iron_cast_plate_from_iron_ingots"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MetaHelper.newStack(NtmBlocks.BARBED_WIRE.asItem(), 1, BarbedWireBlock.BarbedWireType.FIRE.ordinal()))
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', DataComponentIngredient.of(false, NtmDataComponents.META, BoltItem.Type.TUNGSTEN.meta, NtmItems.BOLT.get()))
                .unlockedBy("has_tungsten_bolt", has(NtmItems.BOLT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "flaming_barbed_wire_from_tungsten_bolts"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MetaHelper.newStack(NtmItems.BOLT.get(), 1, BoltItem.Type.TUNGSTEN.meta))
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', DataComponentIngredient.of(false, NtmDataComponents.META, BarbedWireBlock.BarbedWireType.FIRE.ordinal(), NtmBlocks.BARBED_WIRE.asItem()))
                .unlockedBy("has_flaming_barbed_wire", has(NtmBlocks.BARBED_WIRE.asItem()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "tungsten_bolt_from_flaming_barbed_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.UPGRADE_TEMPLATE.get(), 1)
                .pattern("ACA")
                .pattern("BDB")
                .pattern("ACA")
                .define('A', NtmItems.WIRE_COPPER.get())
                .define('B', NtmItems.PLATE_POLYMER.get())
                .define('C', NtmItems.PLATE_IRON.get())
                .define('D', NtmItems.CIRCUIT_ANALOG_BOARD.get())
                .unlockedBy("has_plate_iron", has(NtmItems.PLATE_IRON.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "upgrade_template_from_iron"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.UPGRADE_TEMPLATE.get(), 1)
                .pattern("ACA")
                .pattern("BDB")
                .pattern("ACA")
                .define('A', NtmItems.WIRE_COPPER.get())
                .define('B', NtmItems.PLATE_POLYMER.get())
                .define('C', NtmItems.INGOT_POLYMER.get())
                .define('D', NtmItems.CIRCUIT_INTEGRATED_BOARD.get())
                .unlockedBy("has_polymer", has(NtmItems.INGOT_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "upgrade_template_from_polymer"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_PRINTED_BOARD.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern("   ")
                .define('B', NtmItems.PLATE_COPPER.get())
                .define('A', NtmItems.PLATE_POLYMER.get())
                .unlockedBy("has_insulator", has(NtmItems.PLATE_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_printed_board_from_copper_plate"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_PRINTED_BOARD.get(), 4)
                .pattern(" A ")
                .pattern(" B ")
                .pattern("   ")
                .define('B', NtmItems.PLATE_GOLD.get())
                .define('A', NtmItems.PLATE_POLYMER.get())
                .unlockedBy("has_insulator", has(NtmItems.PLATE_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_printed_board_from_gold_plate"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CAPACITOR.get(), 2)
                .pattern("   ")
                .pattern("ABA")
                .pattern("C C")
                .define('B', NtmItems.POWDER_ALUMINIUM.get())
                .define('A', NtmItems.PLATE_POLYMER.get())
                .define('C', NtmItems.WIRE_COPPER.get())
                .unlockedBy("has_insulator", has(NtmItems.PLATE_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_capacitor_from_aluminium_powder_and_copper_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CAPACITOR.get(), 2)
                .pattern("   ")
                .pattern("ABA")
                .pattern("C C")
                .define('B', NtmItems.POWDER_ALUMINIUM.get())
                .define('A', NtmItems.PLATE_POLYMER.get())
                .define('C', NtmItems.WIRE_ALUMINIUM.get())
                .unlockedBy("has_insulator", has(NtmItems.PLATE_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_capacitor_from_aluminium_powder_and_aluminium_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CAPACITOR.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('B', NtmItems.NUGGET_NIOBIUM.get())
                .define('A', NtmItems.PLATE_POLYMER.get())
                .define('C', NtmItems.WIRE_COPPER.get())
                .unlockedBy("has_insulator", has(NtmItems.PLATE_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_capacitor_from_niobium_nugget_and_copper_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CAPACITOR.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('B', NtmItems.FRAGMENT_NIOBIUM.get())
                .define('A', NtmItems.PLATE_POLYMER.get())
                .define('C', NtmItems.WIRE_COPPER.get())
                .unlockedBy("has_insulator", has(NtmItems.PLATE_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_capacitor_from_niobium_fragment_and_copper_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CAPACITOR.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('B', NtmItems.NUGGET_NIOBIUM.get())
                .define('A', NtmItems.PLATE_POLYMER.get())
                .define('C', NtmItems.WIRE_ALUMINIUM.get())
                .unlockedBy("has_insulator", has(NtmItems.PLATE_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_capacitor_from_niobium_nugget_and_aluminium_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CAPACITOR.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('B', NtmItems.FRAGMENT_NIOBIUM.get())
                .define('A', NtmItems.PLATE_POLYMER.get())
                .define('C', NtmItems.WIRE_ALUMINIUM.get())
                .unlockedBy("has_insulator", has(NtmItems.PLATE_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_capacitor_from_niobium_fragment_and_aluminium_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_VACUUM_TUBE.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('B', NtmItems.WIRE_TUNGSTEN.get())
                .define('A', Items.GLASS_PANE)
                .define('C', NtmItems.PLATE_POLYMER.get())
                .unlockedBy("has_insulator", has(NtmItems.PLATE_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_vacuum_tube_from_tungsten_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_VACUUM_TUBE.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('B', NtmItems.WIRE_CARBON.get())
                .define('A', Items.GLASS_PANE)
                .define('C', NtmItems.PLATE_POLYMER.get())
                .unlockedBy("has_insulator", has(NtmItems.PLATE_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "circuit_vacuum_tube_from_carbon_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.MOTOR.get(), 2)
                .pattern(" A ")
                .pattern("BCB")
                .pattern("BDB")
                .define('A', NtmItems.WIRE_RED_COPPER.get())
                .define('B', NtmItems.PLATE_IRON.get())
                .define('C', NtmItems.COIL_COPPER.get())
                .define('D', NtmItems.COIL_COPPER_RING.get())
                .unlockedBy("has_insulator", has(NtmItems.PLATE_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "motor_from_iron_plates"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.MOTOR.get(), 2)
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" D ")
                .define('A', NtmItems.WIRE_RED_COPPER.get())
                .define('B', NtmItems.PLATE_STEEL.get())
                .define('C', NtmItems.COIL_COPPER.get())
                .define('D', NtmItems.COIL_COPPER_RING.get())
                .unlockedBy("has_insulator", has(NtmItems.PLATE_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "motor_from_steel_plates"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.PLATE_POLYMER.get(), 16)
                .pattern("   ")
                .pattern("AA ")
                .pattern("   ")
                .define('A', NtmItems.INGOT_FIBERGLASS.get())
                .unlockedBy("has_ingot_fiberglass", has(NtmItems.INGOT_FIBERGLASS.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "plate_polymer_from_fiberglass"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.PLATE_POLYMER.get(), 16)
                .pattern("   ")
                .pattern("AA ")
                .pattern("   ")
                .define('A', NtmItems.INGOT_ASBESTOS.get())
                .unlockedBy("has_ingot_asbestos", has(NtmItems.INGOT_ASBESTOS.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "plate_polymer_from_asbestos"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.PLATE_POLYMER.get(), 4)
                .pattern("   ")
                .pattern("AA ")
                .pattern("   ")
                .define('A', Items.BRICK)
                .unlockedBy("has_brick", has(Items.BRICK))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "plate_polymer_from_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.PLATE_POLYMER.get(), 4)
                .pattern("   ")
                .pattern("ABA")
                .pattern("   ")
                .define('A', Items.STRING)
                .define('B', Items.WHITE_WOOL)
                .unlockedBy("has_string", has(Items.STRING))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "plate_polymer_from_string_and_wool"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.PLATE_POLYMER.get(), 8)
                .pattern("   ")
                .pattern("AA ")
                .pattern("   ")
                .define('A', NtmItems.INGOT_POLYMER.get())
                .unlockedBy("has_ingot_polymer", has(NtmItems.INGOT_POLYMER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "plate_polymer_from_polymer"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.PLATE_POLYMER.get(), 8)
                .pattern("   ")
                .pattern("AA ")
                .pattern("   ")
                .define('A', NtmItems.INGOT_BAKELITE.get())
                .unlockedBy("has_ingot_bakelite", has(NtmItems.INGOT_BAKELITE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "plate_polymer_from_bakelite"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.PLATE_POLYMER.get(), 8)
                .pattern("   ")
                .pattern("AA ")
                .pattern("   ")
                .define('A', NtmItems.INGOT_RUBBER.get())
                .unlockedBy("has_ingot_rubber", has(NtmItems.INGOT_RUBBER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "plate_polymer_from_rubber"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.PLATE_POLYMER.get(), 8)
                .pattern("   ")
                .pattern("AA ")
                .pattern("   ")
                .define('A', NtmItems.INGOT_BIORUBBER.get())
                .unlockedBy("has_ingot_biorubber", has(NtmItems.INGOT_BIORUBBER.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "plate_polymer_from_biorubber"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.PIPES_STEEL.get(), 1)
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" A ")
                .define('A', NtmBlocks.BLOCK_STEEL.get())
                .unlockedBy("has_ingot_steel", has(NtmItems.INGOT_STEEL.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "1"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_TANTALIUM_CAPACITOR.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('A', NtmItems.PLATE_POLYMER.get())
                .define('B', NtmItems.NUGGET_TANTALIUM.get())
                .define('C', NtmItems.WIRE_COPPER.get())
                .unlockedBy("has_nugget_tantalium", has(NtmItems.NUGGET_TANTALIUM.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "2"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_TANTALIUM_CAPACITOR.get(), 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" C ")
                .define('A', NtmItems.PLATE_POLYMER.get())
                .define('B', NtmItems.NUGGET_TANTALIUM.get())
                .define('C', NtmItems.WIRE_ALUMINIUM.get())
                .unlockedBy("has_nugget_tantalium", has(NtmItems.NUGGET_TANTALIUM.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "3"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CONTROL_UNIT_CASING.get(), 1)
                .pattern("AAA")
                .pattern("BCC")
                .pattern("AAA")
                .define('A', NtmItems.INGOT_POLYMER.get())
                .define('B', NtmItems.CRT_DISPLAY.get())
                .define('C', NtmItems.CIRCUIT_PRINTED_BOARD.get())
                .unlockedBy("has_crt_display", has(NtmItems.CRT_DISPLAY.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "4"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CIRCUIT_CONTROL_UNIT_CASING.get(), 1)
                .pattern("AAA")
                .pattern("BCC")
                .pattern("AAA")
                .define('A', NtmItems.INGOT_BAKELITE.get())
                .define('B', NtmItems.CRT_DISPLAY.get())
                .define('C', NtmItems.CIRCUIT_PRINTED_BOARD.get())
                .unlockedBy("has_crt_display", has(NtmItems.CRT_DISPLAY.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "5"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CRT_DISPLAY.get(), 4)
                .pattern(" C ")
                .pattern("ADA")
                .pattern(" B ")
                .define('A', NtmItems.PLATE_STEEL.get())
                .define('B', NtmItems.CIRCUIT_VACUUM_TUBE.get())
                .define('C', NtmItems.POWDER_ALUMINIUM.get())
                .define('D', Items.GLASS_PANE)
                .unlockedBy("has_vacuum_tube", has(NtmItems.CIRCUIT_VACUUM_TUBE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "6"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CELL_EMPTY.get(), 6)
                .pattern(" A ")
                .pattern("D D")
                .pattern(" A ")
                .define('A', NtmItems.PLATE_STEEL.get())
                .define('D', Items.GLASS_PANE)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "7"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmBlocks.MACHINE_WOOD_BURNER.get(), 1)
                .pattern("AAA")
                .pattern("BDB")
                .pattern("C C")
                .define('A', NtmItems.PLATE_STEEL.get())
                .define('B', NtmItems.COIL_COPPER.get())
                .define('C', Items.IRON_INGOT)
                .define('D', Items.FURNACE)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "8"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmBlocks.MACHINE_BATTERY_SOCKET.get(), 1)
                .pattern("A A")
                .pattern("A A")
                .pattern("ABA")
                .define('A', NtmItems.PLATE_POLYMER.get())
                .define('B', NtmItems.COIL_COPPER.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "9"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmBlocks.MACHINE_BATTERY_SOCKET.get(), 1)
                .pattern("   ")
                .pattern("ABA")
                .pattern("   ")
                .define('A', NtmItems.PLATE_STEEL.get())
                .define('B', NtmItems.INGOT_RED_COPPER.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "10"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.RING_STARMETAL.get(), 1)
                .pattern(" A ")
                .pattern("A A")
                .pattern(" A ")
                .define('A', NtmItems.INGOT_STARMETAL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "11"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.INGOT_COPPER.get(), 1)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_COPPER.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "12"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.INGOT_GRAPHITE.get(), 1)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_CARBON.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "13"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.GOLD_INGOT, 1)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_GOLD.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "14"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.INGOT_SCHRABIDIUM.get(), 1)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_SCHRABIDIUM.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "15"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.INGOT_TUNGSTEN.get(), 1)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_TUNGSTEN.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "16"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.INGOT_ALUMINIUM.get(), 1)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_ALUMINIUM.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "17"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.INGOT_RED_COPPER.get(), 1)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_RED_COPPER.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "18"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.INGOT_MAGNETIZED_TUNGSTEN.get(), 1)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_MAGNETIZED_TUNGSTEN.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "19"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.COIL_MAGNETIZED_TUNGSTEN.get(), 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_MAGNETIZED_TUNGSTEN.get())
                .define('B', NtmItems.INGOT_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "20"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.COIL_MAGNETIZED_TUNGSTEN.get(), 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_MAGNETIZED_TUNGSTEN.get())
                .define('B', Items.IRON_INGOT)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "21"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.COIL_COPPER.get(), 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_RED_COPPER.get())
                .define('B', Items.IRON_INGOT)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "22"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.COIL_COPPER.get(), 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_RED_COPPER.get())
                .define('B', NtmItems.INGOT_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "23"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.COIL_GOLD.get(), 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_GOLD.get())
                .define('B', NtmItems.INGOT_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "24"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.COIL_GOLD.get(), 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_GOLD.get())
                .define('B', Items.IRON_INGOT)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "25"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.COIL_GOLD_RING.get(), 2)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', NtmItems.COIL_GOLD.get())
                .define('B', NtmItems.PLATE_IRON.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "26"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.COIL_GOLD_RING.get(), 2)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', NtmItems.COIL_GOLD.get())
                .define('B', NtmItems.PLATE_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "27"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.COIL_COPPER_RING.get(), 2)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', NtmItems.COIL_COPPER.get())
                .define('B', NtmItems.PLATE_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "28"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.COIL_COPPER_RING.get(), 2)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', NtmItems.COIL_COPPER.get())
                .define('B', NtmItems.PLATE_IRON.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "29"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.COIL_TUNGSTEN.get(), 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_TUNGSTEN.get())
                .define('B', NtmItems.INGOT_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "30"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.COIL_TUNGSTEN.get(), 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', NtmItems.WIRE_TUNGSTEN.get())
                .define('B', Items.IRON_INGOT)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "31"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.TANK_STEEL.get(), 2)
                .pattern("ABA")
                .pattern("A A")
                .pattern("ABA")
                .define('A', NtmItems.PLATE_STEEL.get())
                .define('B', NtmItems.PLATE_TITANIUM.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "32"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.CATALYST_CLAY.get(), 1)
                .pattern("   ")
                .pattern("AB ")
                .pattern("   ")
                .define('A', NtmItems.POWDER_IRON.get())
                .define('B', Items.CLAY)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "33"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.DEUTERIUM_FILTER.get(), 1)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', NtmItems.INGOT_TCALLOY.get())
                .define('B', NtmItems.SULFUR.get())
                .define('C', NtmItems.CATALYST_CLAY.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "34"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.DEUTERIUM_FILTER.get(), 1)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', NtmItems.INGOT_CDALLOY.get())
                .define('B', NtmItems.SULFUR.get())
                .define('C', NtmItems.CATALYST_CLAY.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "35"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.FINS_FLAT.get(), 1)
                .pattern("BA ")
                .pattern("AA ")
                .pattern("BA ")
                .define('A', NtmItems.PLATE_STEEL.get())
                .define('B', NtmItems.INGOT_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "36"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.FINS_SMALL_STEEL.get(), 1)
                .pattern(" AA")
                .pattern("ABB")
                .pattern(" AA")
                .define('A', NtmItems.PLATE_STEEL.get())
                .define('B', NtmItems.INGOT_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "37"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.FINS_BIG_STEEL.get(), 1)
                .pattern(" AB")
                .pattern("BBB")
                .pattern(" AB")
                .define('A', NtmItems.PLATE_STEEL.get())
                .define('B', NtmItems.INGOT_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "38"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.FINS_TRI_STEEL.get(), 1)
                .pattern(" AB")
                .pattern("BBC")
                .pattern(" AB")
                .define('A', NtmItems.PLATE_STEEL.get())
                .define('B', NtmItems.INGOT_STEEL.get())
                .define('C', NtmBlocks.BLOCK_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "39"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.FINS_QUAD_TITANIUM.get(), 1)
                .pattern(" AA")
                .pattern("BBB")
                .pattern(" AA")
                .define('A', NtmItems.PLATE_TITANIUM.get())
                .define('B', NtmItems.INGOT_TITANIUM.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "40"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.SPHERE_STEEL.get(), 1)
                .pattern("ABA")
                .pattern("B B")
                .pattern("ABA")
                .define('A', NtmItems.PLATE_STEEL.get())
                .define('B', NtmItems.INGOT_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "41"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.PEDESTAL_STEEL.get(), 1)
                .pattern("A A")
                .pattern("A A")
                .pattern("BBB")
                .define('A', NtmItems.PLATE_STEEL.get())
                .define('B', NtmItems.INGOT_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "42"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.BLADE_TITANIUM.get(), 2)
                .pattern("BA ")
                .pattern("BA ")
                .pattern("BB ")
                .define('A', NtmItems.PLATE_TITANIUM.get())
                .define('B', NtmItems.INGOT_TITANIUM.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "43"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.TURBINE_TITANIUM.get(), 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', NtmItems.BLADE_TITANIUM.get())
                .define('B', NtmItems.INGOT_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "44"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.TURBINE_TUNGSTEN.get(), 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', NtmItems.BLADE_TUNGSTEN.get())
                .define('B', NtmItems.INGOT_DURA_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "45"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.FLYWHEEL_BERYLLIUM.get(), 1)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', DataComponentIngredient.of(false, NtmDataComponents.META, CastPlateItem.Type.IRON.ordinal(), NtmItems.CAST_PLATE.get()))
                .define('B', NtmBlocks.BLOCK_BERYLLIUM.get())
                .define('C', NtmItems.PIPE_DURA_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "46"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.BLADES_STEEL.get(), 1)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', NtmItems.PLATE_STEEL.get())
                .define('B', NtmItems.INGOT_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "47"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.BLADES_TITANIUM.get(), 1)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', NtmItems.PLATE_TITANIUM.get())
                .define('B', NtmItems.INGOT_TITANIUM.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "48"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.BLADES_DESH.get(), 1)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', NtmItems.PLATE_DESH.get())
                .define('B', NtmItems.BLADES_TITANIUM.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "49"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.DUCTTAPE.get(), 4)
                .pattern(" C ")
                .pattern(" B ")
                .pattern(" A ")
                .define('A', Items.SLIME_BALL)
                .define('B', Items.PAPER)
                .define('C', Items.STRING)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "50"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.TOOTHPICKS.get(), 3)
                .pattern("A  ")
                .pattern("AA ")
                .pattern("   ")
                .define('A', Items.STICK)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "51"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.PLANT_ITEM.get(), 1)
                .pattern("AA ")
                .pattern("A  ")
                .pattern("   ")
                .define('A', Items.STRING)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "52"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.PLANT_ITEM.get(), 4)
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" A ")
                .define('A', NtmBlocks.PLANT_FLOWER.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "53"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MetaHelper.newStack(NtmBlocks.BARBED_WIRE.asItem(), 16, BarbedWireBlock.BarbedWireType.STANDARD.ordinal()))
                .pattern("ABA")
                .pattern("B B")
                .pattern("ABA")
                .define('A', NtmItems.WIRE_STEEL.get())
                .define('B', Items.IRON_INGOT)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "54"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MetaHelper.newStack(NtmBlocks.BARBED_WIRE.asItem(), 8, BarbedWireBlock.BarbedWireType.FIRE.ordinal()))
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', DataComponentIngredient.of(false, NtmDataComponents.META, BarbedWireBlock.BarbedWireType.STANDARD.ordinal(), NtmBlocks.BARBED_WIRE.asItem()))
                .define('B', NtmItems.POWDER_FIRE)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "55"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MetaHelper.newStack(NtmBlocks.BARBED_WIRE.asItem(), 8, BarbedWireBlock.BarbedWireType.POISON.ordinal()))
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', DataComponentIngredient.of(false, NtmDataComponents.META, BarbedWireBlock.BarbedWireType.STANDARD.ordinal(), NtmBlocks.BARBED_WIRE.asItem()))
                .define('B', NtmItems.POWDER_POISON)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "56"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MetaHelper.newStack(NtmBlocks.BARBED_WIRE.asItem(), 8, BarbedWireBlock.BarbedWireType.ACID.ordinal()))
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', DataComponentIngredient.of(false, NtmDataComponents.META, BarbedWireBlock.BarbedWireType.STANDARD.ordinal(), NtmBlocks.BARBED_WIRE.asItem()))
                .define('B', DataComponentIngredient.of(false, NtmDataComponents.META, Fluids.PEROXIDE.getID(), NtmItems.FLUID_TANK_FULL.get()))
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "57"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MetaHelper.newStack(NtmBlocks.BARBED_WIRE.asItem(), 8, BarbedWireBlock.BarbedWireType.WITHER.ordinal()))
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', DataComponentIngredient.of(false, NtmDataComponents.META, BarbedWireBlock.BarbedWireType.STANDARD.ordinal(), NtmBlocks.BARBED_WIRE.asItem()))
                .define('B', Items.WITHER_SKELETON_SKULL)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "58"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MetaHelper.newStack(NtmBlocks.BARBED_WIRE.asItem(), 4, BarbedWireBlock.BarbedWireType.ULTRADEATH.ordinal()))
                .pattern("ACA")
                .pattern("CBC")
                .pattern("ACA")
                .define('A', DataComponentIngredient.of(false, NtmDataComponents.META, BarbedWireBlock.BarbedWireType.STANDARD.ordinal(), NtmBlocks.BARBED_WIRE.asItem()))
                .define('B', NtmItems.NUCLEAR_WASTE)
                .define('C', NtmItems.POWDER_YELLOWCAKE.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "59"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MetaHelper.newStack(NtmItems.BOLT.get(), 16, BoltItem.Type.TUNGSTEN.meta))
                .pattern(" A ")
                .pattern(" A ")
                .pattern("   ")
                .define('A', NtmItems.INGOT_TUNGSTEN.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "60"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MetaHelper.newStack(NtmItems.BOLT.get(), 16, BoltItem.Type.LEAD.meta))
                .pattern(" A ")
                .pattern(" A ")
                .pattern("   ")
                .define('A', NtmItems.INGOT_LEAD.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "61"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MetaHelper.newStack(NtmItems.BOLT.get(), 16, BoltItem.Type.STEEL.meta))
                .pattern(" A ")
                .pattern(" A ")
                .pattern("   ")
                .define('A', NtmItems.INGOT_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "62"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MetaHelper.newStack(NtmItems.BOLT.get(), 16, BoltItem.Type.DURA_STEEL.meta))
                .pattern(" A ")
                .pattern(" A ")
                .pattern("   ")
                .define('A', NtmItems.INGOT_DURA_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "63"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.REACHER.get(), 1)
                .pattern("CAC")
                .pattern("B B")
                .pattern("C C")
                .define('A', NtmItems.INGOT_TUNGSTEN.get())
                .define('B', NtmItems.INGOT_RUBBER.get())
                .define('C', DataComponentIngredient.of(false, NtmDataComponents.META, BoltItem.Type.TUNGSTEN.meta, NtmItems.BOLT.get()))
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "64"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.OIL_DETECTOR.get(), 1)
                .pattern("A B")
                .pattern("ADB")
                .pattern("CCC")
                .define('A', NtmItems.WIRE_GOLD.get())
                .define('B', NtmItems.INGOT_COPPER.get())
                .define('C', NtmItems.PLATE_STEEL.get())
                .define('D', NtmItems.CIRCUIT_ANALOG_BOARD.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "65"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmBlocks.MACHINE_PRESS.get(), 1)
                .pattern("ADA")
                .pattern("ACA")
                .pattern("ABA")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.IRON_BLOCK)
                .define('C', Items.PISTON)
                .define('D', Items.FURNACE)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "66"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.STAMP_STONE_FLAT.get(), 1)
                .pattern("   ")
                .pattern("AAA")
                .pattern("BBB")
                .define('A', Items.BRICK)
                .define('B', Items.STONE)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "67"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.STAMP_IRON_FLAT.get(), 1)
                .pattern("   ")
                .pattern("AAA")
                .pattern("BBB")
                .define('A', Items.BRICK)
                .define('B', Items.IRON_INGOT)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "68"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.STAMP_STEEL_FLAT.get(), 1)
                .pattern("   ")
                .pattern("AAA")
                .pattern("BBB")
                .define('A', Items.BRICK)
                .define('B', NtmItems.INGOT_STEEL.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "69"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.STAMP_TITANIUM_FLAT.get(), 1)
                .pattern("   ")
                .pattern("AAA")
                .pattern("BBB")
                .define('A', Items.BRICK)
                .define('B', NtmItems.INGOT_TITANIUM.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "70"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.STAMP_OBSIDIAN_FLAT.get(), 1)
                .pattern("   ")
                .pattern("AAA")
                .pattern("BBB")
                .define('A', Items.BRICK)
                .define('B', Items.OBSIDIAN)
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "71"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NtmItems.STAMP_DESH_FLAT.get(), 1)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Items.BRICK)
                .define('B', NtmItems.INGOT_DESH.get())
                .define('C', NtmItems.INGOT_FERROURANIUM.get())
                .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", "72"));

        //
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
        addOreSmelting(recipeOutput, NtmBlocks.ORE_ASBESTOS.get(), NtmBlocks.ORE_ASBESTOS_DEEPSLATE.get(), NtmItems.INGOT_ASBESTOS.get(), 0.7F);
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

        addPickaxeRecipe(recipeOutput, NtmItems.STEEL_PICKAXE.get(), NtmItems.INGOT_STEEL.get(), "steel_pickaxe");
        addAxeRecipe(recipeOutput, NtmItems.STEEL_AXE.get(), NtmItems.INGOT_STEEL.get(), "steel_axe");
        addShovelRecipe(recipeOutput, NtmItems.STEEL_SHOVEL.get(), NtmItems.INGOT_STEEL.get(), "steel_shovel");
        addHoeRecipe(recipeOutput, NtmItems.STEEL_HOE.get(), NtmItems.INGOT_STEEL.get(), "steel_hoe");

        addPickaxeRecipe(recipeOutput, NtmItems.TITANIUM_PICKAXE.get(), NtmItems.INGOT_TITANIUM.get(), "titanium_pickaxe");
        addAxeRecipe(recipeOutput, NtmItems.TITANIUM_AXE.get(), NtmItems.INGOT_TITANIUM.get(), "titanium_axe");
        addShovelRecipe(recipeOutput, NtmItems.TITANIUM_SHOVEL.get(), NtmItems.INGOT_TITANIUM.get(), "titanium_shovel");
        addHoeRecipe(recipeOutput, NtmItems.TITANIUM_HOE.get(), NtmItems.INGOT_TITANIUM.get(), "titanium_hoe");

        addPickaxeRecipe(recipeOutput, NtmItems.DESH_PICKAXE.get(), NtmItems.INGOT_DESH.get(), "desh_pickaxe");
        addAxeRecipe(recipeOutput, NtmItems.DESH_AXE.get(), NtmItems.INGOT_DESH.get(), "desh_axe");
        addShovelRecipe(recipeOutput, NtmItems.DESH_SHOVEL.get(), NtmItems.INGOT_DESH.get(), "desh_shovel");
        addHoeRecipe(recipeOutput, NtmItems.DESH_HOE.get(), NtmItems.INGOT_DESH.get(), "desh_hoe");

        addPickaxeRecipe(recipeOutput, NtmItems.COBALT_PICKAXE.get(), NtmItems.INGOT_COBALT.get(), "cobalt_pickaxe");
        addAxeRecipe(recipeOutput, NtmItems.COBALT_AXE.get(), NtmItems.INGOT_COBALT.get(), "cobalt_axe");
        addShovelRecipe(recipeOutput, NtmItems.COBALT_SHOVEL.get(), NtmItems.INGOT_COBALT.get(), "cobalt_shovel");
        addHoeRecipe(recipeOutput, NtmItems.COBALT_HOE.get(), NtmItems.INGOT_COBALT.get(), "cobalt_hoe");

        addPickaxeRecipe(recipeOutput, NtmItems.CMB_PICKAXE.get(), NtmItems.INGOT_COMBINE_STEEL.get(), "cmb_pickaxe");
        addAxeRecipe(recipeOutput, NtmItems.CMB_AXE.get(), NtmItems.INGOT_COMBINE_STEEL.get(), "cmb_axe");
        addShovelRecipe(recipeOutput, NtmItems.CMB_SHOVEL.get(), NtmItems.INGOT_COMBINE_STEEL.get(), "cmb_shovel");
        addHoeRecipe(recipeOutput, NtmItems.CMB_HOE.get(), NtmItems.INGOT_COMBINE_STEEL.get(), "cmb_hoe");

        addPickaxeRecipe(recipeOutput, NtmItems.BISMUTH_PICKAXE.get(), NtmItems.INGOT_BISMUTH.get(), "bismuth_pickaxe");
        addAxeRecipe(recipeOutput, NtmItems.BISMUTH_AXE.get(), NtmItems.INGOT_BISMUTH.get(), "bismuth_axe");

        addPickaxeRecipe(recipeOutput, NtmItems.STARMETAL_PICKAXE.get(), NtmItems.INGOT_STARMETAL.get(), "starmetal_pickaxe");
        addAxeRecipe(recipeOutput, NtmItems.STARMETAL_AXE.get(), NtmItems.INGOT_STARMETAL.get(), "starmetal_axe");
        addShovelRecipe(recipeOutput, NtmItems.STARMETAL_SHOVEL.get(), NtmItems.INGOT_STARMETAL.get(), "starmetal_shovel");
        addHoeRecipe(recipeOutput, NtmItems.STARMETAL_HOE.get(), NtmItems.INGOT_STARMETAL.get(), "starmetal_hoe");

        addPickaxeRecipe(recipeOutput, NtmItems.SCHRABIDIUM_PICKAXE.get(), NtmItems.INGOT_SCHRABIDIUM.get(), "schrabidium_pickaxe");
        addAxeRecipe(recipeOutput, NtmItems.SCHRABIDIUM_AXE.get(), NtmItems.INGOT_SCHRABIDIUM.get(), "schrabidium_axe");
        addShovelRecipe(recipeOutput, NtmItems.SCHRABIDIUM_SHOVEL.get(), NtmItems.INGOT_SCHRABIDIUM.get(), "schrabidium_shovel");
        addHoeRecipe(recipeOutput, NtmItems.SCHRABIDIUM_HOE.get(), NtmItems.INGOT_SCHRABIDIUM.get(), "schrabidium_hoe");

        registerMaterialConversionRecipes(recipeOutput);
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

    private void addPickaxeRecipe(RecipeOutput recipeOutput, Item result, Item ingredient, String baseName) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result, 1)
                .pattern("XXX")
                .pattern(" # ")
                .pattern(" # ")
                .define('X', ingredient)
                .define('#', Items.STICK)
                .unlockedBy("has_" + BuiltInRegistries.ITEM.getKey(ingredient).getPath(), has(ingredient))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", baseName));
    }

    private void addAxeRecipe(RecipeOutput recipeOutput, Item result, Item ingredient, String baseName) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result, 1)
                .pattern("XX ")
                .pattern("X# ")
                .pattern(" # ")
                .define('X', ingredient)
                .define('#', Items.STICK)
                .unlockedBy("has_" + BuiltInRegistries.ITEM.getKey(ingredient).getPath(), has(ingredient))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", baseName));
    }

    private void addShovelRecipe(RecipeOutput recipeOutput, Item result, Item ingredient, String baseName) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result, 1)
                .pattern(" X ")
                .pattern(" # ")
                .pattern(" # ")
                .define('X', ingredient)
                .define('#', Items.STICK)
                .unlockedBy("has_" + BuiltInRegistries.ITEM.getKey(ingredient).getPath(), has(ingredient))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", baseName));
    }

    private void addHoeRecipe(RecipeOutput recipeOutput, Item result, Item ingredient, String baseName) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result, 1)
                .pattern("XX ")
                .pattern(" # ")
                .pattern(" # ")
                .define('X', ingredient)
                .define('#', Items.STICK)
                .unlockedBy("has_" + BuiltInRegistries.ITEM.getKey(ingredient).getPath(), has(ingredient))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", baseName));
    }

    private void registerMaterialConversionRecipes(RecipeOutput recipeOutput) {
        registerMaterialBlockRecipes(recipeOutput);
        registerBilletRecipes(recipeOutput);
        registerPowderSmeltingRecipes(recipeOutput);
        registerTinyPowderRecipes(recipeOutput);
        registerCrystalSmeltingRecipes(recipeOutput);
    }

    private void registerMaterialBlockRecipes(RecipeOutput recipeOutput) {
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_ACTINIUM.get(), NtmItems.INGOT_ACTINIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_ALUMINIUM.get(), NtmItems.INGOT_ALUMINIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_ASBESTOS.get(), NtmItems.INGOT_ASBESTOS.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_AUSTRALIUM.get(), NtmItems.INGOT_AUSTRALIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_BERYLLIUM.get(), NtmItems.INGOT_BERYLLIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_BISMUTH.get(), NtmItems.INGOT_BISMUTH.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_CADMIUM.get(), NtmItems.INGOT_CADMIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_CDALLOY.get(), NtmItems.INGOT_CDALLOY.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_COLTAN.get(), NtmItems.POWDER_COLTAN.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_COMBINE_STEEL.get(), NtmItems.INGOT_COMBINE_STEEL.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_COPPER.get(), NtmItems.INGOT_COPPER.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_DESH.get(), NtmItems.INGOT_DESH.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_DINEUTRONIUM.get(), NtmItems.INGOT_DINEUTRONIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_DURA_STEEL.get(), NtmItems.INGOT_DURA_STEEL.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_EUPHEMIUM.get(), NtmItems.INGOT_EUPHEMIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_FIBERGLASS.get(), NtmItems.INGOT_FIBERGLASS.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_INSULATOR.get(), NtmItems.PLATE_POLYMER.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_LANTHANIUM.get(), NtmItems.INGOT_LANTHANIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_LEAD.get(), NtmItems.INGOT_LEAD.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_LITHIUM.get(), NtmItems.LITHIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_MAGNETIZED_TUNGSTEN.get(), NtmItems.INGOT_MAGNETIZED_TUNGSTEN.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_MOX_FUEL.get(), NtmItems.INGOT_MOX_FUEL.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_NEPTUNIUM.get(), NtmItems.INGOT_NEPTUNIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_NIOBIUM.get(), NtmItems.INGOT_NIOBIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_NITER.get(), NtmItems.NITER.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_PLUTONIUM.get(), NtmItems.INGOT_PLUTONIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_PLUTONIUM_FUEL.get(), NtmItems.INGOT_PLUTONIUM_FUEL.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_POLONIUM.get(), NtmItems.INGOT_POLONIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_PU238.get(), NtmItems.INGOT_PU238.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_PU239.get(), NtmItems.INGOT_PU239.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_PU240.get(), NtmItems.INGOT_PU240.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_PU_MIX.get(), NtmItems.INGOT_PU_MIX.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_RA226.get(), NtmItems.INGOT_RA226.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_RED_COPPER.get(), NtmItems.INGOT_RED_COPPER.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_SATURNITE.get(), NtmItems.INGOT_SATURNITE.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_SCHRABIDATE.get(), NtmItems.INGOT_SCHRABIDATE.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_SCHRABIDIUM.get(), NtmItems.INGOT_SCHRABIDIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_SCHRABIDIUM_FUEL.get(), NtmItems.INGOT_SCHRABIDIUM_FUEL.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_SCHRARANIUM.get(), NtmItems.INGOT_SCHRARANIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_SMORE.get(), NtmItems.INGOT_SMORE.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_SOLINIUM.get(), NtmItems.INGOT_SOLINIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_SULFUR.get(), NtmItems.SULFUR.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_TANTALIUM.get(), NtmItems.INGOT_TANTALIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_TCALLOY.get(), NtmItems.INGOT_TCALLOY.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_THORIUM.get(), NtmItems.INGOT_TH232.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_THORIUM_FUEL.get(), NtmItems.INGOT_THORIUM_FUEL.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_TITANIUM.get(), NtmItems.INGOT_TITANIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_TUNGSTEN.get(), NtmItems.INGOT_TUNGSTEN.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_U233.get(), NtmItems.INGOT_U233.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_U235.get(), NtmItems.INGOT_U235.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_U238.get(), NtmItems.INGOT_U238.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_URANIUM.get(), NtmItems.INGOT_URANIUM.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_URANIUM_FUEL.get(), NtmItems.INGOT_URANIUM_FUEL.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_WASTE.get(), NtmItems.NUCLEAR_WASTE.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_WASTE_VITRIFIED.get(), NtmItems.NUCLEAR_WASTE_VITRIFIED.get());
        addMaterialBlockRecipe(recipeOutput, NtmBlocks.BLOCK_YELLOWCAKE.get(), NtmItems.POWDER_YELLOWCAKE.get());
    }

    private void registerBilletRecipes(RecipeOutput recipeOutput) {
        addBilletRecipe(recipeOutput, NtmItems.BILLET_ACTINIUM.get(), NtmItems.NUGGET_ACTINIUM.get(), NtmItems.INGOT_ACTINIUM.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_AM241.get(), NtmItems.NUGGET_AM241.get(), NtmItems.INGOT_AM241.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_AM242.get(), NtmItems.NUGGET_AM242.get(), NtmItems.INGOT_AM242.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_AM_MIX.get(), NtmItems.NUGGET_AM_MIX.get(), NtmItems.INGOT_AM_MIX.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_AMERICIUM_FUEL.get(), NtmItems.NUGGET_AMERICIUM_FUEL.get(), NtmItems.INGOT_AMERICIUM_FUEL.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_AU198.get(), NtmItems.NUGGET_AU198.get(), NtmItems.INGOT_AU198.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_AUSTRALIUM.get(), NtmItems.NUGGET_AUSTRALIUM.get(), NtmItems.INGOT_AUSTRALIUM.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_BERYLLIUM.get(), NtmItems.NUGGET_BERYLLIUM.get(), NtmItems.INGOT_BERYLLIUM.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_BISMUTH.get(), NtmItems.NUGGET_BISMUTH.get(), NtmItems.INGOT_BISMUTH.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_CO60.get(), NtmItems.NUGGET_CO60.get(), NtmItems.INGOT_CO60.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_COBALT.get(), NtmItems.NUGGET_COBALT.get(), NtmItems.INGOT_COBALT.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_GH336.get(), NtmItems.NUGGET_GH336.get(), NtmItems.INGOT_GH336.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_HES.get(), NtmItems.NUGGET_HES.get(), NtmItems.INGOT_HES.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_LES.get(), NtmItems.NUGGET_LES.get(), NtmItems.INGOT_LES.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_MOX_FUEL.get(), NtmItems.NUGGET_MOX_FUEL.get(), NtmItems.INGOT_MOX_FUEL.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_NEPTUNIUM.get(), NtmItems.NUGGET_NEPTUNIUM.get(), NtmItems.INGOT_NEPTUNIUM.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_NEPTUNIUM_FUEL.get(), NtmItems.NUGGET_NEPTUNIUM_FUEL.get(), NtmItems.INGOT_NEPTUNIUM_FUEL.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_PB209.get(), NtmItems.NUGGET_PB209.get(), NtmItems.INGOT_PB209.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_PLUTONIUM.get(), NtmItems.NUGGET_PLUTONIUM.get(), NtmItems.INGOT_PLUTONIUM.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_PLUTONIUM_FUEL.get(), NtmItems.NUGGET_PLUTONIUM_FUEL.get(), NtmItems.INGOT_PLUTONIUM_FUEL.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_POLONIUM.get(), NtmItems.NUGGET_POLONIUM.get(), NtmItems.INGOT_POLONIUM.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_PU238.get(), NtmItems.NUGGET_PU238.get(), NtmItems.INGOT_PU238.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_PU239.get(), NtmItems.NUGGET_PU239.get(), NtmItems.INGOT_PU239.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_PU240.get(), NtmItems.NUGGET_PU240.get(), NtmItems.INGOT_PU240.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_PU241.get(), NtmItems.NUGGET_PU241.get(), NtmItems.INGOT_PU241.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_PU_MIX.get(), NtmItems.NUGGET_PU_MIX.get(), NtmItems.INGOT_PU_MIX.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_RA226.get(), NtmItems.NUGGET_RA226.get(), NtmItems.INGOT_RA226.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_SCHRABIDIUM.get(), NtmItems.NUGGET_SCHRABIDIUM.get(), NtmItems.INGOT_SCHRABIDIUM.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_SCHRABIDIUM_FUEL.get(), NtmItems.NUGGET_SCHRABIDIUM_FUEL.get(), NtmItems.INGOT_SCHRABIDIUM_FUEL.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_SOLINIUM.get(), NtmItems.NUGGET_SOLINIUM.get(), NtmItems.INGOT_SOLINIUM.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_SR90.get(), NtmItems.NUGGET_SR90.get(), NtmItems.INGOT_SR90.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_TECHNETIUM.get(), NtmItems.NUGGET_TECHNETIUM.get(), NtmItems.INGOT_TECHNETIUM.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_TH232.get(), NtmItems.NUGGET_TH232.get(), NtmItems.INGOT_TH232.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_THORIUM_FUEL.get(), NtmItems.NUGGET_THORIUM_FUEL.get(), NtmItems.INGOT_THORIUM_FUEL.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_U233.get(), NtmItems.NUGGET_U233.get(), NtmItems.INGOT_U233.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_U235.get(), NtmItems.NUGGET_U235.get(), NtmItems.INGOT_U235.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_U238.get(), NtmItems.NUGGET_U238.get(), NtmItems.INGOT_U238.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_UNOBTAINIUM.get(), NtmItems.NUGGET_UNOBTAINIUM.get(), NtmItems.INGOT_UNOBTAINIUM.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_URANIUM.get(), NtmItems.NUGGET_URANIUM.get(), NtmItems.INGOT_URANIUM.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_URANIUM_FUEL.get(), NtmItems.NUGGET_URANIUM_FUEL.get(), NtmItems.INGOT_URANIUM_FUEL.get());
        addBilletRecipe(recipeOutput, NtmItems.BILLET_ZIRCONIUM.get(), NtmItems.NUGGET_ZIRCONIUM.get(), NtmItems.INGOT_ZIRCONIUM.get());
    }

    private void registerPowderSmeltingRecipes(RecipeOutput recipeOutput) {
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_ACTINIUM.get(), NtmItems.INGOT_ACTINIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_ALUMINIUM.get(), NtmItems.INGOT_ALUMINIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_ASBESTOS.get(), NtmItems.INGOT_ASBESTOS.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_ASTATINE.get(), NtmItems.INGOT_ASTATINE.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_AU198.get(), NtmItems.INGOT_AU198.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_AUSTRALIUM.get(), NtmItems.INGOT_AUSTRALIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_BAKELITE.get(), NtmItems.INGOT_BAKELITE.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_BERYLLIUM.get(), NtmItems.INGOT_BERYLLIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_BISMUTH.get(), NtmItems.INGOT_BISMUTH.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_BORON.get(), NtmItems.INGOT_BORON.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_BROMINE.get(), NtmItems.INGOT_BROMINE.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_CADMIUM.get(), NtmItems.INGOT_CADMIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_CAESIUM.get(), NtmItems.INGOT_CAESIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_CALCIUM.get(), NtmItems.INGOT_CALCIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_CDALLOY.get(), NtmItems.INGOT_CDALLOY.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_CERIUM.get(), NtmItems.INGOT_CERIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_CO60.get(), NtmItems.INGOT_CO60.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_COBALT.get(), NtmItems.INGOT_COBALT.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_COMBINE_STEEL.get(), NtmItems.INGOT_COMBINE_STEEL.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_COPPER.get(), NtmItems.INGOT_COPPER.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_DAFFERGON.get(), NtmItems.INGOT_DAFFERGON.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_DESH.get(), NtmItems.INGOT_DESH.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_DINEUTRONIUM.get(), NtmItems.INGOT_DINEUTRONIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_DURA_STEEL.get(), NtmItems.INGOT_DURA_STEEL.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_EUPHEMIUM.get(), NtmItems.INGOT_EUPHEMIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_GOLD.get(), Items.GOLD_INGOT);
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_I131.get(), NtmItems.INGOT_I131.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_IODINE.get(), NtmItems.INGOT_IODINE.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_IRON.get(), Items.IRON_INGOT);
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_LANTHANIUM.get(), NtmItems.INGOT_LANTHANIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_LEAD.get(), NtmItems.INGOT_LEAD.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_MAGNETIZED_TUNGSTEN.get(), NtmItems.INGOT_MAGNETIZED_TUNGSTEN.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_METEORITE.get(), NtmItems.INGOT_METEORITE.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_NEPTUNIUM.get(), NtmItems.INGOT_NEPTUNIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_NIOBIUM.get(), NtmItems.INGOT_NIOBIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_OSMIRIDIUM.get(), NtmItems.INGOT_OSMIRIDIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_PB209.get(), NtmItems.INGOT_PB209.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_PLUTONIUM.get(), NtmItems.INGOT_PLUTONIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_POLONIUM.get(), NtmItems.INGOT_POLONIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_POLYMER.get(), NtmItems.INGOT_POLYMER.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_RA226.get(), NtmItems.INGOT_RA226.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_RED_COPPER.get(), NtmItems.INGOT_RED_COPPER.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_REIIUM.get(), NtmItems.INGOT_REIIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_SCHRABIDATE.get(), NtmItems.INGOT_SCHRABIDATE.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_SCHRABIDIUM.get(), NtmItems.INGOT_SCHRABIDIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_SR90.get(), NtmItems.INGOT_SR90.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_STEEL.get(), NtmItems.INGOT_STEEL.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_TANTALIUM.get(), NtmItems.INGOT_TANTALIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_TCALLOY.get(), NtmItems.INGOT_TCALLOY.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_TENNESSINE.get(), NtmItems.INGOT_TENNESSINE.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_TITANIUM.get(), NtmItems.INGOT_TITANIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_TUNGSTEN.get(), NtmItems.INGOT_TUNGSTEN.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_UNOBTAINIUM.get(), NtmItems.INGOT_UNOBTAINIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_URANIUM.get(), NtmItems.INGOT_URANIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_VERTICIUM.get(), NtmItems.INGOT_VERTICIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_WEIDANIUM.get(), NtmItems.INGOT_WEIDANIUM.get());
        addMaterialSmelting(recipeOutput, NtmItems.POWDER_ZIRCONIUM.get(), NtmItems.INGOT_ZIRCONIUM.get());
    }

    private void registerTinyPowderRecipes(RecipeOutput recipeOutput) {
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_METEORITE_TINY.get(), NtmItems.POWDER_METEORITE.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_ACTINIUM_TINY.get(), NtmItems.POWDER_ACTINIUM.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_AT209_TINY.get(), NtmItems.POWDER_AT209.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_AU198_TINY.get(), NtmItems.POWDER_AU198.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_BORON_TINY.get(), NtmItems.POWDER_BORON.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_CERIUM_TINY.get(), NtmItems.POWDER_CERIUM.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_CO60_TINY.get(), NtmItems.POWDER_CO60.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_COAL_TINY.get(), NtmItems.POWDER_COAL.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_COBALT_TINY.get(), NtmItems.POWDER_COBALT.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_CS137_TINY.get(), NtmItems.POWDER_CS137.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_I131_TINY.get(), NtmItems.POWDER_I131.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_IODINE_TINY.get(), NtmItems.POWDER_IODINE.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_LANTHANIUM_TINY.get(), NtmItems.POWDER_LANTHANIUM.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_LITHIUM_TINY.get(), NtmItems.POWDER_LITHIUM.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_NEODYMIUM_TINY.get(), NtmItems.POWDER_NEODYMIUM.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_NIOBIUM_TINY.get(), NtmItems.POWDER_NIOBIUM.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_PALEOGENITE_TINY.get(), NtmItems.POWDER_PALEOGENITE.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_PB209_TINY.get(), NtmItems.POWDER_PB209.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_SR90_TINY.get(), NtmItems.POWDER_SR90.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_STEEL_TINY.get(), NtmItems.POWDER_STEEL.get());
        addTinyPowderRecipe(recipeOutput, NtmItems.POWDER_XE135_TINY.get(), NtmItems.POWDER_XE135.get());
    }

    private void registerCrystalSmeltingRecipes(RecipeOutput recipeOutput) {
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_ALUMINIUM.get(), NtmItems.INGOT_ALUMINIUM.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_ASBESTOS.get(), NtmItems.INGOT_ASBESTOS.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_BERYLLIUM.get(), NtmItems.INGOT_BERYLLIUM.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_COBALT.get(), NtmItems.INGOT_COBALT.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_COPPER.get(), NtmItems.INGOT_COPPER.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_GOLD.get(), Items.GOLD_INGOT);
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_IRON.get(), Items.IRON_INGOT);
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_LEAD.get(), NtmItems.INGOT_LEAD.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_OSMIRIDIUM.get(), NtmItems.INGOT_OSMIRIDIUM.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_PHOSPHORUS.get(), NtmItems.POWDER_FIRE.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_PLUTONIUM.get(), NtmItems.INGOT_PLUTONIUM.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_SCHRABIDIUM.get(), NtmItems.INGOT_SCHRABIDIUM.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_SCHRARANIUM.get(), NtmItems.INGOT_SCHRARANIUM.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_STARMETAL.get(), NtmItems.INGOT_STARMETAL.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_TITANIUM.get(), NtmItems.INGOT_TITANIUM.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_TUNGSTEN.get(), NtmItems.INGOT_TUNGSTEN.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_URANIUM.get(), NtmItems.INGOT_URANIUM.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_SULFUR.get(), NtmItems.SULFUR.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_REDSTONE.get(), Items.REDSTONE);
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_LAPIS.get(), Items.LAPIS_LAZULI);
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_DIAMOND.get(), Items.DIAMOND);
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_THORIUM.get(), NtmItems.INGOT_TH232.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_NITER.get(), NtmItems.NITER.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_FLUORITE.get(), NtmItems.FLUORITE.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_CINNABAR.get(), NtmItems.CINNABAR.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_LITHIUM.get(), NtmItems.LITHIUM.get());
        addCrystalSmelting(recipeOutput, NtmItems.CRYSTAL_TRIXITE.get(), NtmItems.INGOT_PLUTONIUM.get());

    }

    private void addMaterialBlockRecipe(RecipeOutput recipeOutput, ItemLike blockItem, ItemLike counterpart) {
        Item result = blockItem.asItem();
        Item input = counterpart.asItem();
        String resultName = BuiltInRegistries.ITEM.getKey(result).getPath();
        String inputName = BuiltInRegistries.ITEM.getKey(input).getPath();

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 1)
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', input)
                .unlockedBy("has_" + inputName, has(input))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", resultName + "_from_9_" + inputName));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, input, 9)
                .requires(result)
                .unlockedBy("has_" + resultName, has(result))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", inputName + "_from_" + resultName));
    }

    private void addBilletRecipe(RecipeOutput recipeOutput, ItemLike billetItem, ItemLike nuggetItem, ItemLike ingotItem) {
        Item billet = billetItem.asItem();
        Item nugget = nuggetItem.asItem();
        Item ingot = ingotItem.asItem();
        String billetName = BuiltInRegistries.ITEM.getKey(billet).getPath();
        String nuggetName = BuiltInRegistries.ITEM.getKey(nugget).getPath();
        String ingotName = BuiltInRegistries.ITEM.getKey(ingot).getPath();

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, billet, 1)
                .pattern("nnn")
                .pattern("nnn")
                .define('n', nugget)
                .unlockedBy("has_" + nuggetName, has(nugget))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", billetName + "_from_6_" + nuggetName));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget, 6)
                .requires(billet)
                .unlockedBy("has_" + billetName, has(billet))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", nuggetName + "_from_" + billetName));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, billet, 3)
                .pattern("XX")
                .define('X', ingot)
                .unlockedBy("has_" + ingotName, has(ingot))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", billetName + "_from_2_" + ingotName));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ingot, 2)
                .requires(billet)
                .requires(billet)
                .requires(billet)
                .unlockedBy("has_" + billetName, has(billet))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", ingotName + "_from_3_" + billetName));
    }

    private void addMaterialSmelting(RecipeOutput recipeOutput, ItemLike inputItem, ItemLike outputItem) {
        Item input = inputItem.asItem();
        Item output = outputItem.asItem();
        String inputName = BuiltInRegistries.ITEM.getKey(input).getPath();
        String outputName = BuiltInRegistries.ITEM.getKey(output).getPath();

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC, output, 0.7F, 200)
                .unlockedBy("has_" + inputName, has(input))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", inputName + "_to_" + outputName));
    }

    private void addTinyPowderRecipe(RecipeOutput recipeOutput, ItemLike tinyPowderItem, ItemLike powderItem) {
        Item tinyPowder = tinyPowderItem.asItem();
        Item powder = powderItem.asItem();
        String tinyName = BuiltInRegistries.ITEM.getKey(tinyPowder).getPath();
        String powderName = BuiltInRegistries.ITEM.getKey(powder).getPath();

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, powder, 1)
                .pattern("TTT")
                .pattern("TTT")
                .pattern("TTT")
                .define('T', tinyPowder)
                .unlockedBy("has_" + tinyName, has(tinyPowder))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", powderName + "_from_9_" + tinyName));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, tinyPowder, 9)
                .requires(powder)
                .unlockedBy("has_" + powderName, has(powder))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", tinyName + "_from_" + powderName));
    }

    private void addCrystalSmelting(RecipeOutput recipeOutput, ItemLike inputItem, ItemLike outputItem) {
        Item input = inputItem.asItem();
        Item output = outputItem.asItem();
        String inputName = BuiltInRegistries.ITEM.getKey(input).getPath();
        String outputName = BuiltInRegistries.ITEM.getKey(output).getPath();

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC, new ItemStack(output, 2), 0.7F, 200)
                .unlockedBy("has_" + inputName, has(input))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath("hbmsntm", outputName + "_from_" + inputName));
    }
}
