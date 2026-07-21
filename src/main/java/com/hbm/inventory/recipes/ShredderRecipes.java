package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.generic.OreBasaltBlock;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.NtmItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ShredderRecipes extends SerializableRecipe {

    private static final LinkedHashMap<ComparableStack, ItemStack> RECIPES = new LinkedHashMap<>();

    public static class ShredderRecipe {
        public final ItemStack input;
        public final ItemStack output;

        public ShredderRecipe(ItemStack input, ItemStack output) {
            this.input = input.copy();
            this.output = output.copy();
        }
    }

    public static List<ShredderRecipe> getJeiRecipes() {
        List<ShredderRecipe> list = new ArrayList<>(RECIPES.size());
        for(Entry<ComparableStack, ItemStack> entry : RECIPES.entrySet()) {
            ItemStack input = entry.getKey().toStack();
            input.setCount(1);
            list.add(new ShredderRecipe(input, entry.getValue()));
        }
        return list;
    }

    public static List<ItemStack> getBlades() {
        return List.of(
                new ItemStack(NtmItems.BLADES_STEEL.get()),
                new ItemStack(NtmItems.BLADES_TITANIUM.get()),
                new ItemStack(NtmItems.BLADES_DESH.get())
        );
    }

    public static ItemStack getShredderResult(ItemStack stack) {
        if(stack.isEmpty()) return new ItemStack(NtmItems.DUST.get());

        ComparableStack key = new ComparableStack(stack).makeSingular();
        ItemStack result = RECIPES.get(key);
        if(result == null) {
            key.meta = MetaHelper.WILDCARD_VALUE;
            result = RECIPES.get(key);
        }

        return result == null ? new ItemStack(NtmItems.DUST.get()) : result.copy();
    }

    public static boolean hasRecipe(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        ComparableStack key = new ComparableStack(stack).makeSingular();
        if (RECIPES.containsKey(key)) {
            return true;
        }

        key.meta = MetaHelper.WILDCARD_VALUE;
        return RECIPES.containsKey(key);
    }

    public static void setRecipe(ComparableStack input, ItemStack output) {
        RECIPES.put(input.copy().makeSingular(), output.copy());
    }

    private static void setRecipe(ItemStack input, ItemStack output) {
        setRecipe(new ComparableStack(input).makeSingular(), output);
    }

    private static void setRecipe(Block input, ItemStack output) {
        setRecipe(new ComparableStack(input), output);
    }

    private static void registerRawOreRecipe(Block block, ItemStack output) {
        if(output.isEmpty()) return;
        ComparableStack key = new ComparableStack(block).makeSingular();
        RECIPES.putIfAbsent(key, output.copy());
    }

    private static ItemStack stack(ItemStack stack, int count) {
        ItemStack copy = stack.copy();
        copy.setCount(count);
        return copy;
    }

    private static ItemStack output(ItemStack stack, int count) {
        return stack(stack, count);
    }

    private static ItemStack output(ItemStack stack) {
        return stack(stack, 2);
    }

    private static ItemStack getRawOreOutput(String material) {
        return switch(material) {
            case "coal" -> output(new ItemStack(NtmItems.POWDER_COAL.get()));
            case "copper" -> output(new ItemStack(NtmItems.POWDER_COPPER.get()));
            case "iron" -> output(new ItemStack(NtmItems.POWDER_IRON.get()));
            case "gold" -> output(new ItemStack(NtmItems.POWDER_GOLD.get()));
            case "lapis" -> output(new ItemStack(NtmItems.POWDER_LAPIS.get()));
            case "redstone" -> output(new ItemStack(Items.REDSTONE));
            case "emerald" -> output(new ItemStack(NtmItems.POWDER_EMERALD.get()));
            case "diamond" -> output(new ItemStack(NtmBlocks.GRAVEL_DIAMOND.get().asItem()));
            case "quartz" -> output(new ItemStack(NtmItems.POWDER_QUARTZ.get()));
            case "uranium" -> output(new ItemStack(NtmItems.POWDER_URANIUM.get()));
            case "titanium" -> output(new ItemStack(NtmItems.POWDER_TITANIUM.get()));
            case "tungsten" -> output(new ItemStack(NtmItems.POWDER_TUNGSTEN.get()));
            case "lead" -> output(new ItemStack(NtmItems.POWDER_LEAD.get()));
            case "aluminium" -> output(new ItemStack(NtmItems.POWDER_ALUMINIUM.get()));
            case "beryllium" -> output(new ItemStack(NtmItems.POWDER_BERYLLIUM.get()));
            case "asbestos" -> output(new ItemStack(NtmItems.POWDER_ASBESTOS.get()));
            case "cobalt" -> output(new ItemStack(NtmItems.POWDER_COBALT.get()));
            case "sulfur" -> output(new ItemStack(NtmItems.SULFUR.get()));
            case "cinnabar" -> output(new ItemStack(NtmItems.CINNABAR.get()));
            case "fluorite" -> output(new ItemStack(NtmItems.FLUORITE.get()));
            case "niter" -> output(new ItemStack(NtmItems.NITER.get()));
            case "lignite" -> output(new ItemStack(NtmItems.POWDER_LIGNITE.get()));
            case "schrabidium" -> output(new ItemStack(NtmItems.POWDER_SCHRABIDIUM.get()));
            default -> ItemStack.EMPTY;
        };
    }

    private static String normalizeOreName(String path) {
        String material = path.substring("ore_".length());
        boolean changed;

        do {
            changed = false;

            for(String prefix : new String[] { "nether_", "gneiss_" }) {
                if(material.startsWith(prefix)) {
                    material = material.substring(prefix.length());
                    changed = true;
                }
            }

            for(String suffix : new String[] { "_deepslate", "_scorched" }) {
                if(material.endsWith(suffix)) {
                    material = material.substring(0, material.length() - suffix.length());
                    changed = true;
                }
            }
        } while(changed);

        return material;
    }

    private static void registerRawOreRecipes() {
        for(var holder : NtmBlocks.BLOCKS.getEntries()) {
            Block block = holder.get();
            String path = BuiltInRegistries.BLOCK.getKey(block).getPath();
            if(!path.startsWith("ore_")) continue;
            if(path.equals("ore_basalt")) continue;

            ItemStack output = getRawOreOutput(normalizeOreName(path));
            registerRawOreRecipe(block, output);
        }

        registerBasaltRecipe(OreBasaltBlock.BasaltOreType.SULFUR, new ItemStack(NtmItems.SULFUR.get()));
        registerBasaltRecipe(OreBasaltBlock.BasaltOreType.FLUORITE, new ItemStack(NtmItems.FLUORITE.get()));
        registerBasaltRecipe(OreBasaltBlock.BasaltOreType.ASBESTOS, new ItemStack(NtmItems.POWDER_ASBESTOS.get()));
    }

    private static void registerMaterialRecipes() {
        registerIfAbsent(new ItemStack(NtmBlocks.ORE_RARE.get()), new ItemStack(NtmItems.POWDER_DESH_MIX.get(), 1));
        registerIfAbsent(new ItemStack(NtmBlocks.ORE_RARE_DEEPSLATE.get()), new ItemStack(NtmItems.POWDER_DESH_MIX.get(), 1));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_ACTINIUM.get()), new ItemStack(NtmItems.POWDER_ACTINIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_ALUMINIUM.get()), new ItemStack(NtmItems.POWDER_ALUMINIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_ASBESTOS.get()), new ItemStack(NtmItems.POWDER_ASBESTOS.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_AUSTRALIUM.get()), new ItemStack(NtmItems.POWDER_AUSTRALIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_BERYLLIUM.get()), new ItemStack(NtmItems.POWDER_BERYLLIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_BISMUTH.get()), new ItemStack(NtmItems.POWDER_BISMUTH.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_CADMIUM.get()), new ItemStack(NtmItems.POWDER_CADMIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_CDALLOY.get()), new ItemStack(NtmItems.POWDER_CDALLOY.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_COMBINE_STEEL.get()), new ItemStack(NtmItems.POWDER_COMBINE_STEEL.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_COPPER.get()), new ItemStack(NtmItems.POWDER_COPPER.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_DESH.get()), new ItemStack(NtmItems.POWDER_DESH.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_DINEUTRONIUM.get()), new ItemStack(NtmItems.POWDER_DINEUTRONIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_DURA_STEEL.get()), new ItemStack(NtmItems.POWDER_DURA_STEEL.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_EUPHEMIUM.get()), new ItemStack(NtmItems.POWDER_EUPHEMIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_LANTHANIUM.get()), new ItemStack(NtmItems.POWDER_LANTHANIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_LEAD.get()), new ItemStack(NtmItems.POWDER_LEAD.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_MAGNETIZED_TUNGSTEN.get()), new ItemStack(NtmItems.POWDER_MAGNETIZED_TUNGSTEN.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_NEPTUNIUM.get()), new ItemStack(NtmItems.POWDER_NEPTUNIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_NIOBIUM.get()), new ItemStack(NtmItems.POWDER_NIOBIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_PLUTONIUM.get()), new ItemStack(NtmItems.POWDER_PLUTONIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_POLONIUM.get()), new ItemStack(NtmItems.POWDER_POLONIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_RA226.get()), new ItemStack(NtmItems.POWDER_RA226.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_RED_COPPER.get()), new ItemStack(NtmItems.POWDER_RED_COPPER.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_SCHRABIDATE.get()), new ItemStack(NtmItems.POWDER_SCHRABIDATE.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_SCHRABIDIUM.get()), new ItemStack(NtmItems.POWDER_SCHRABIDIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_TANTALIUM.get()), new ItemStack(NtmItems.POWDER_TANTALIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_TCALLOY.get()), new ItemStack(NtmItems.POWDER_TCALLOY.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_TITANIUM.get()), new ItemStack(NtmItems.POWDER_TITANIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_TUNGSTEN.get()), new ItemStack(NtmItems.POWDER_TUNGSTEN.get(), 9));
        registerIfAbsent(new ItemStack(NtmBlocks.BLOCK_URANIUM.get()), new ItemStack(NtmItems.POWDER_URANIUM.get(), 9));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_ALUMINIUM.get()), new ItemStack(NtmItems.POWDER_ALUMINIUM.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_ASBESTOS.get()), new ItemStack(NtmItems.POWDER_ASBESTOS.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_BERYLLIUM.get()), new ItemStack(NtmItems.POWDER_BERYLLIUM.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_COAL.get()), new ItemStack(NtmItems.POWDER_COAL.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_COBALT.get()), new ItemStack(NtmItems.POWDER_COBALT.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_COPPER.get()), new ItemStack(NtmItems.POWDER_COPPER.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_GOLD.get()), new ItemStack(NtmItems.POWDER_GOLD.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_IRON.get()), new ItemStack(NtmItems.POWDER_IRON.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_LEAD.get()), new ItemStack(NtmItems.POWDER_LEAD.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_OSMIRIDIUM.get()), new ItemStack(NtmItems.POWDER_OSMIRIDIUM.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_PLUTONIUM.get()), new ItemStack(NtmItems.POWDER_PLUTONIUM.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_SCHRABIDIUM.get()), new ItemStack(NtmItems.POWDER_SCHRABIDIUM.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_TITANIUM.get()), new ItemStack(NtmItems.POWDER_TITANIUM.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_TUNGSTEN.get()), new ItemStack(NtmItems.POWDER_TUNGSTEN.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_URANIUM.get()), new ItemStack(NtmItems.POWDER_URANIUM.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_SULFUR.get()), new ItemStack(NtmItems.SULFUR.get(), 8));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_REDSTONE.get()), new ItemStack(Items.REDSTONE, 8));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_LAPIS.get()), new ItemStack(Items.LAPIS_LAZULI, 8));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_DIAMOND.get()), new ItemStack(NtmItems.POWDER_DIAMOND.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_THORIUM.get()), new ItemStack(NtmItems.POWDER_THORIUM.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_NITER.get()), new ItemStack(NtmItems.NITER.get(), 8));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_FLUORITE.get()), new ItemStack(NtmItems.FLUORITE.get(), 8));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_RARE.get()), new ItemStack(NtmItems.POWDER_DESH_MIX.get(), 2));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_PHOSPHORUS.get()), new ItemStack(NtmItems.POWDER_FIRE.get(), 8));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_CINNABAR.get()), new ItemStack(NtmItems.CINNABAR.get(), 4));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_LITHIUM.get()), new ItemStack(NtmItems.POWDER_LITHIUM.get(), 3));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_TRIXITE.get()), new ItemStack(NtmItems.POWDER_PLUTONIUM.get(), 6));
        registerIfAbsent(new ItemStack(NtmItems.CRYSTAL_STARMETAL.get()), new ItemStack(NtmItems.POWDER_DURA_STEEL.get(), 6));

        registerIfAbsent(new ItemStack(NtmItems.INGOT_ACTINIUM.get()), new ItemStack(NtmItems.POWDER_ACTINIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_ALUMINIUM.get()), new ItemStack(NtmItems.POWDER_ALUMINIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_ASBESTOS.get()), new ItemStack(NtmItems.POWDER_ASBESTOS.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_ASTATINE.get()), new ItemStack(NtmItems.POWDER_ASTATINE.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_AU198.get()), new ItemStack(NtmItems.POWDER_AU198.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_AUSTRALIUM.get()), new ItemStack(NtmItems.POWDER_AUSTRALIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_BAKELITE.get()), new ItemStack(NtmItems.POWDER_BAKELITE.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_BERYLLIUM.get()), new ItemStack(NtmItems.POWDER_BERYLLIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_BISMUTH.get()), new ItemStack(NtmItems.POWDER_BISMUTH.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_BORON.get()), new ItemStack(NtmItems.POWDER_BORON.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_BROMINE.get()), new ItemStack(NtmItems.POWDER_BROMINE.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_CADMIUM.get()), new ItemStack(NtmItems.POWDER_CADMIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_CAESIUM.get()), new ItemStack(NtmItems.POWDER_CAESIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_CALCIUM.get()), new ItemStack(NtmItems.POWDER_CALCIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_CDALLOY.get()), new ItemStack(NtmItems.POWDER_CDALLOY.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_CERIUM.get()), new ItemStack(NtmItems.POWDER_CERIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_CO60.get()), new ItemStack(NtmItems.POWDER_CO60.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_COBALT.get()), new ItemStack(NtmItems.POWDER_COBALT.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_COMBINE_STEEL.get()), new ItemStack(NtmItems.POWDER_COMBINE_STEEL.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_COPPER.get()), new ItemStack(NtmItems.POWDER_COPPER.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_DAFFERGON.get()), new ItemStack(NtmItems.POWDER_DAFFERGON.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_DESH.get()), new ItemStack(NtmItems.POWDER_DESH.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_DINEUTRONIUM.get()), new ItemStack(NtmItems.POWDER_DINEUTRONIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_DURA_STEEL.get()), new ItemStack(NtmItems.POWDER_DURA_STEEL.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_EUPHEMIUM.get()), new ItemStack(NtmItems.POWDER_EUPHEMIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_I131.get()), new ItemStack(NtmItems.POWDER_I131.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_IODINE.get()), new ItemStack(NtmItems.POWDER_IODINE.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_LANTHANIUM.get()), new ItemStack(NtmItems.POWDER_LANTHANIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_LEAD.get()), new ItemStack(NtmItems.POWDER_LEAD.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_MAGNETIZED_TUNGSTEN.get()), new ItemStack(NtmItems.POWDER_MAGNETIZED_TUNGSTEN.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_NEPTUNIUM.get()), new ItemStack(NtmItems.POWDER_NEPTUNIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_NIOBIUM.get()), new ItemStack(NtmItems.POWDER_NIOBIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_OSMIRIDIUM.get()), new ItemStack(NtmItems.POWDER_OSMIRIDIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_PB209.get()), new ItemStack(NtmItems.POWDER_PB209.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_PLUTONIUM.get()), new ItemStack(NtmItems.POWDER_PLUTONIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_POLONIUM.get()), new ItemStack(NtmItems.POWDER_POLONIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_POLYMER.get()), new ItemStack(NtmItems.POWDER_POLYMER.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_RA226.get()), new ItemStack(NtmItems.POWDER_RA226.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_RED_COPPER.get()), new ItemStack(NtmItems.POWDER_RED_COPPER.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_REIIUM.get()), new ItemStack(NtmItems.POWDER_REIIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_SCHRABIDATE.get()), new ItemStack(NtmItems.POWDER_SCHRABIDATE.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_SCHRABIDIUM.get()), new ItemStack(NtmItems.POWDER_SCHRABIDIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_SR90.get()), new ItemStack(NtmItems.POWDER_SR90.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_STEEL.get()), new ItemStack(NtmItems.POWDER_STEEL.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_TANTALIUM.get()), new ItemStack(NtmItems.POWDER_TANTALIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_TCALLOY.get()), new ItemStack(NtmItems.POWDER_TCALLOY.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_TENNESSINE.get()), new ItemStack(NtmItems.POWDER_TENNESSINE.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_TITANIUM.get()), new ItemStack(NtmItems.POWDER_TITANIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_TUNGSTEN.get()), new ItemStack(NtmItems.POWDER_TUNGSTEN.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_UNOBTAINIUM.get()), new ItemStack(NtmItems.POWDER_UNOBTAINIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_URANIUM.get()), new ItemStack(NtmItems.POWDER_URANIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_VERTICIUM.get()), new ItemStack(NtmItems.POWDER_VERTICIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_WEIDANIUM.get()), new ItemStack(NtmItems.POWDER_WEIDANIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.INGOT_ZIRCONIUM.get()), new ItemStack(NtmItems.POWDER_ZIRCONIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.PLATE_ALUMINIUM.get()), new ItemStack(NtmItems.POWDER_ALUMINIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.PLATE_COMBINE_STEEL.get()), new ItemStack(NtmItems.POWDER_COMBINE_STEEL.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.PLATE_COPPER.get()), new ItemStack(NtmItems.POWDER_COPPER.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.PLATE_DURA_STEEL.get()), new ItemStack(NtmItems.POWDER_DURA_STEEL.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.PLATE_GOLD.get()), new ItemStack(NtmItems.POWDER_GOLD.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.PLATE_IRON.get()), new ItemStack(NtmItems.POWDER_IRON.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.PLATE_LEAD.get()), new ItemStack(NtmItems.POWDER_LEAD.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.PLATE_SCHRABIDIUM.get()), new ItemStack(NtmItems.POWDER_SCHRABIDIUM.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.PLATE_STEEL.get()), new ItemStack(NtmItems.POWDER_STEEL.get(), 1));
        registerIfAbsent(new ItemStack(NtmItems.PLATE_TITANIUM.get()), new ItemStack(NtmItems.POWDER_TITANIUM.get(), 1));
    }

    private static void registerIfAbsent(ItemStack input, ItemStack output) {
        if(input.isEmpty() || output.isEmpty()) {
            return;
        }

        ComparableStack key = new ComparableStack(input).makeSingular();
        RECIPES.putIfAbsent(key, output.copy());
    }


    private static void registerVanillaOreRecipes() {
        setRecipe(Blocks.COAL_ORE, output(new ItemStack(NtmItems.POWDER_COAL.get())));
        setRecipe(Blocks.DEEPSLATE_COAL_ORE, output(new ItemStack(NtmItems.POWDER_COAL.get())));
        setRecipe(Blocks.COPPER_ORE, output(new ItemStack(NtmItems.POWDER_COPPER.get())));
        setRecipe(Blocks.DEEPSLATE_COPPER_ORE, output(new ItemStack(NtmItems.POWDER_COPPER.get())));
        setRecipe(Blocks.IRON_ORE, output(new ItemStack(NtmItems.POWDER_IRON.get())));
        setRecipe(Blocks.DEEPSLATE_IRON_ORE, output(new ItemStack(NtmItems.POWDER_IRON.get())));
        setRecipe(Blocks.GOLD_ORE, output(new ItemStack(NtmItems.POWDER_GOLD.get())));
        setRecipe(Blocks.DEEPSLATE_GOLD_ORE, output(new ItemStack(NtmItems.POWDER_GOLD.get())));
        setRecipe(Blocks.LAPIS_ORE, output(new ItemStack(NtmItems.POWDER_LAPIS.get())));
        setRecipe(Blocks.DEEPSLATE_LAPIS_ORE, output(new ItemStack(NtmItems.POWDER_LAPIS.get())));
        setRecipe(Blocks.REDSTONE_ORE, output(new ItemStack(Items.REDSTONE)));
        setRecipe(Blocks.DEEPSLATE_REDSTONE_ORE, output(new ItemStack(Items.REDSTONE)));
        setRecipe(Blocks.EMERALD_ORE, output(new ItemStack(NtmItems.POWDER_EMERALD.get())));
        setRecipe(Blocks.DEEPSLATE_EMERALD_ORE, output(new ItemStack(NtmItems.POWDER_EMERALD.get())));
        setRecipe(Blocks.DIAMOND_ORE, output(new ItemStack(NtmBlocks.GRAVEL_DIAMOND.get().asItem())));
        setRecipe(Blocks.DEEPSLATE_DIAMOND_ORE, output(new ItemStack(NtmBlocks.GRAVEL_DIAMOND.get().asItem())));
        setRecipe(Blocks.NETHER_GOLD_ORE, output(new ItemStack(NtmItems.POWDER_GOLD.get())));
        setRecipe(Blocks.NETHER_QUARTZ_ORE, output(new ItemStack(NtmItems.POWDER_QUARTZ.get())));
    }

    private static void registerManualRecipes() {
        setRecipe(Blocks.GLOWSTONE, new ItemStack(Items.GLOWSTONE_DUST, 4));
        setRecipe(Blocks.STONE, new ItemStack(Blocks.GRAVEL));
        setRecipe(Blocks.COBBLESTONE, new ItemStack(Blocks.GRAVEL));
        setRecipe(Blocks.STONE_BRICKS, new ItemStack(Blocks.GRAVEL));
        setRecipe(Blocks.GRAVEL, new ItemStack(Blocks.SAND));
        setRecipe(Blocks.DIRT, new ItemStack(NtmItems.DUST.get()));
        setRecipe(Blocks.SAND, new ItemStack(NtmItems.DUST.get(), 2));
        setRecipe(Blocks.TNT, new ItemStack(Items.GUNPOWDER, 5));
        setRecipe(Blocks.BRICKS, new ItemStack(Items.CLAY_BALL, 4));
        setRecipe(Blocks.BRICK_STAIRS, new ItemStack(Items.CLAY_BALL, 3));
        setRecipe(Blocks.CLAY, new ItemStack(Items.CLAY_BALL, 4));
        setRecipe(Blocks.TERRACOTTA, new ItemStack(Items.CLAY_BALL, 4));
        setRecipe(new ItemStack(Items.BRICK), new ItemStack(Items.CLAY_BALL));
        setRecipe(new ItemStack(Items.FLOWER_POT), new ItemStack(Items.CLAY_BALL, 3));
        setRecipe(Blocks.SANDSTONE, new ItemStack(Blocks.SAND, 4));
        setRecipe(Blocks.SANDSTONE_STAIRS, new ItemStack(Blocks.SAND, 6));
        setRecipe(Blocks.QUARTZ_BLOCK, new ItemStack(NtmItems.POWDER_QUARTZ.get(), 4));
        setRecipe(Blocks.CHISELED_QUARTZ_BLOCK, new ItemStack(NtmItems.POWDER_QUARTZ.get(), 4));
        setRecipe(Blocks.SMOOTH_QUARTZ, new ItemStack(NtmItems.POWDER_QUARTZ.get(), 4));
        setRecipe(Blocks.QUARTZ_PILLAR, new ItemStack(NtmItems.POWDER_QUARTZ.get(), 4));
        setRecipe(Blocks.QUARTZ_BRICKS, new ItemStack(NtmItems.POWDER_QUARTZ.get(), 4));
        setRecipe(Blocks.QUARTZ_STAIRS, new ItemStack(NtmItems.POWDER_QUARTZ.get(), 3));
        setRecipe(Blocks.QUARTZ_SLAB, new ItemStack(NtmItems.POWDER_QUARTZ.get(), 2));
        setRecipe(Blocks.OBSIDIAN, new ItemStack(NtmBlocks.GRAVEL_OBSIDIAN.get().asItem()));
        setRecipe(Blocks.ANVIL, new ItemStack(NtmItems.POWDER_IRON.get(), 31));
    }

    private static void registerBasaltRecipe(OreBasaltBlock.BasaltOreType type, ItemStack output) {
        ComparableStack input = new ComparableStack(NtmBlocks.ORE_BASALT.asItem(), 1, type.ordinal());
        RECIPES.putIfAbsent(input, output.copy().copy());
    }

    @Override
    public String getFileName() {
        return "hbmShredder.json";
    }

    @Override
    public Object getRecipeObject() {
        return RECIPES;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        var obj = recipe.getAsJsonObject();
        ItemStack input = readItemStack(obj.get("input").getAsJsonArray());
        ItemStack output = readItemStack(obj.get("output").getAsJsonArray());
        RECIPES.put(new ComparableStack(input).makeSingular(), output);
    }

    @Override
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        @SuppressWarnings("unchecked")
        Entry<ComparableStack, ItemStack> entry = (Entry<ComparableStack, ItemStack>) recipe;
        writer.name("input");
        writeItemStack(entry.getKey().toStack(), writer);
        writer.name("output");
        writeItemStack(entry.getValue(), writer);
    }

    @Override
    public void registerDefaults() {
        setRecipe(new ItemStack(NtmItems.DUST.get()), new ItemStack(NtmItems.DUST.get()));
        registerManualRecipes();
        registerVanillaOreRecipes();
        registerRawOreRecipes();
        registerMaterialRecipes();
    }

    @Override
    public void registerPost() {
        registerRawOreRecipes();
        registerMaterialRecipes();
    }

    @Override
    public void deleteRecipes() {
        RECIPES.clear();
    }

    @Override
    public String getComment() {
        return "Raw ore shredder recipes are auto-generated. Material shredder recipes are listed explicitly.";
    }
}
