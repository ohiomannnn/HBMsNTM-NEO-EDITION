package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.NtmItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class CentrifugeRecipes extends SerializableRecipe {

    private static final LinkedHashMap<ComparableStack, ItemStack[]> RECIPES = new LinkedHashMap<>();

    public static class CentrifugeRecipe {
        public final ItemStack input;
        public final ItemStack[] outputs;

        public CentrifugeRecipe(ItemStack input, ItemStack[] outputs) {
            this.input = input.copy();
            this.outputs = copyOutputs(outputs);
        }
    }

    public static List<CentrifugeRecipe> getJeiRecipes() {
        List<CentrifugeRecipe> list = new ArrayList<>(RECIPES.size());
        for(Entry<ComparableStack, ItemStack[]> entry : RECIPES.entrySet()) {
            ItemStack input = entry.getKey().toStack();
            input.setCount(1);
            list.add(new CentrifugeRecipe(input, entry.getValue()));
        }
        return list;
    }

    public static boolean hasRecipe(ItemStack stack) {
        if(stack.isEmpty()) return false;
        return RECIPES.containsKey(new ComparableStack(stack).makeSingular());
    }

    public static ItemStack[] getOutput(ItemStack stack) {
        if(stack.isEmpty()) return new ItemStack[0];

        ItemStack[] output = RECIPES.get(new ComparableStack(stack).makeSingular());
        return output == null ? new ItemStack[0] : copyOutputs(output);
    }

    public static void setRecipe(ItemStack input, ItemStack... outputs) {
        RECIPES.put(new ComparableStack(input).makeSingular(), copyOutputs(outputs));
    }

    public static void setRecipe(Block input, ItemStack... outputs) {
        RECIPES.put(new ComparableStack(input).makeSingular(), copyOutputs(outputs));
    }

    private static ItemStack stack(ItemLike item, int count) {
        return new ItemStack(item, count);
    }

    private static ItemStack[] copyOutputs(ItemStack[] outputs) {
        ItemStack[] copy = new ItemStack[Math.min(outputs.length, 4)];
        for(int i = 0; i < copy.length; i++) {
            copy[i] = outputs[i].copy();
        }
        return copy;
    }

    private static void registerOreRecipes() {
        setRecipe(Blocks.COAL_ORE, stack(NtmItems.POWDER_COAL.get(), 2), stack(NtmItems.POWDER_COAL.get(), 2), stack(NtmItems.POWDER_COAL.get(), 2), stack(Blocks.STONE, 1));
        setRecipe(Blocks.DEEPSLATE_COAL_ORE, stack(NtmItems.POWDER_COAL.get(), 2), stack(NtmItems.POWDER_COAL.get(), 2), stack(NtmItems.POWDER_COAL.get(), 2), stack(Blocks.STONE, 1));
        setRecipe(Blocks.IRON_ORE, stack(NtmItems.POWDER_IRON.get(), 1), stack(NtmItems.POWDER_IRON.get(), 1), stack(NtmItems.POWDER_IRON.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.DEEPSLATE_IRON_ORE, stack(NtmItems.POWDER_IRON.get(), 1), stack(NtmItems.POWDER_IRON.get(), 1), stack(NtmItems.POWDER_IRON.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.GOLD_ORE, stack(NtmItems.POWDER_GOLD.get(), 1), stack(NtmItems.POWDER_GOLD.get(), 1), stack(NtmItems.POWDER_GOLD.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.DEEPSLATE_GOLD_ORE, stack(NtmItems.POWDER_GOLD.get(), 1), stack(NtmItems.POWDER_GOLD.get(), 1), stack(NtmItems.POWDER_GOLD.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.DIAMOND_ORE, stack(NtmItems.POWDER_DIAMOND.get(), 1), stack(NtmItems.POWDER_DIAMOND.get(), 1), stack(NtmItems.POWDER_DIAMOND.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.DEEPSLATE_DIAMOND_ORE, stack(NtmItems.POWDER_DIAMOND.get(), 1), stack(NtmItems.POWDER_DIAMOND.get(), 1), stack(NtmItems.POWDER_DIAMOND.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.EMERALD_ORE, stack(NtmItems.POWDER_EMERALD.get(), 1), stack(NtmItems.POWDER_EMERALD.get(), 1), stack(NtmItems.POWDER_EMERALD.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.DEEPSLATE_EMERALD_ORE, stack(NtmItems.POWDER_EMERALD.get(), 1), stack(NtmItems.POWDER_EMERALD.get(), 1), stack(NtmItems.POWDER_EMERALD.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.COPPER_ORE, stack(NtmItems.POWDER_COPPER.get(), 1), stack(NtmItems.POWDER_COPPER.get(), 1), stack(NtmItems.POWDER_GOLD.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.DEEPSLATE_COPPER_ORE, stack(NtmItems.POWDER_COPPER.get(), 1), stack(NtmItems.POWDER_COPPER.get(), 1), stack(NtmItems.POWDER_GOLD.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.REDSTONE_ORE, stack(Items.REDSTONE, 3), stack(Items.REDSTONE, 3), stack(NtmItems.NUGGET_MERCURY.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.DEEPSLATE_REDSTONE_ORE, stack(Items.REDSTONE, 3), stack(Items.REDSTONE, 3), stack(NtmItems.NUGGET_MERCURY.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.LAPIS_ORE, stack(NtmItems.POWDER_LAPIS.get(), 6), stack(NtmItems.POWDER_COBALT_TINY.get(), 1), stack(NtmItems.GEM_SODALITE.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.DEEPSLATE_LAPIS_ORE, stack(NtmItems.POWDER_LAPIS.get(), 6), stack(NtmItems.POWDER_COBALT_TINY.get(), 1), stack(NtmItems.GEM_SODALITE.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(Blocks.NETHER_QUARTZ_ORE, stack(NtmItems.POWDER_QUARTZ.get(), 1), stack(NtmItems.POWDER_QUARTZ.get(), 1), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1), stack(Blocks.NETHERRACK, 1));
        setRecipe(Blocks.NETHER_GOLD_ORE, stack(NtmItems.POWDER_GOLD.get(), 1), stack(NtmItems.POWDER_GOLD.get(), 1), stack(NtmItems.POWDER_GOLD.get(), 1), stack(Blocks.NETHERRACK, 1));

        setRecipe(NtmBlocks.ORE_LIGNITE.get(), stack(NtmItems.POWDER_LIGNITE.get(), 2), stack(NtmItems.POWDER_LIGNITE.get(), 2), stack(NtmItems.POWDER_LIGNITE.get(), 2), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_DEEPSLATE_LIGNITE.get(), stack(NtmItems.POWDER_LIGNITE.get(), 2), stack(NtmItems.POWDER_LIGNITE.get(), 2), stack(NtmItems.POWDER_LIGNITE.get(), 2), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_TITANIUM.get(), stack(NtmItems.POWDER_TITANIUM.get(), 1), stack(NtmItems.POWDER_TITANIUM.get(), 1), stack(NtmItems.POWDER_IRON.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_TITANIUM_DEEPSLATE.get(), stack(NtmItems.POWDER_TITANIUM.get(), 1), stack(NtmItems.POWDER_TITANIUM.get(), 1), stack(NtmItems.POWDER_IRON.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_TUNGSTEN.get(), stack(NtmItems.POWDER_TUNGSTEN.get(), 1), stack(NtmItems.POWDER_TUNGSTEN.get(), 1), stack(NtmItems.POWDER_IRON.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_TUNGSTEN_DEEPSLATE.get(), stack(NtmItems.POWDER_TUNGSTEN.get(), 1), stack(NtmItems.POWDER_TUNGSTEN.get(), 1), stack(NtmItems.POWDER_IRON.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_LEAD.get(), stack(NtmItems.POWDER_LEAD.get(), 1), stack(NtmItems.POWDER_LEAD.get(), 1), stack(NtmItems.POWDER_GOLD.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_LEAD_DEEPSLATE.get(), stack(NtmItems.POWDER_LEAD.get(), 1), stack(NtmItems.POWDER_LEAD.get(), 1), stack(NtmItems.POWDER_GOLD.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_URANIUM.get(), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.NUGGET_RA226.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_URANIUM_DEEPSLATE.get(), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.NUGGET_RA226.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_URANIUM_SCORCHED.get(), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.NUGGET_RA226.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_NETHER_URANIUM.get(), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.NUGGET_RA226.get(), 1), stack(Blocks.NETHERRACK, 1));
        setRecipe(NtmBlocks.ORE_NETHER_URANIUM_SCORCHED.get(), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.NUGGET_RA226.get(), 1), stack(Blocks.NETHERRACK, 1));
        setRecipe(NtmBlocks.ORE_GNEISS_URANIUM.get(), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.NUGGET_RA226.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_GNEISS_URANIUM_SCORCHED.get(), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.NUGGET_RA226.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_THORIUM.get(), stack(NtmItems.POWDER_THORIUM.get(), 1), stack(NtmItems.POWDER_THORIUM.get(), 1), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_THORIUM_DEEPSLATE.get(), stack(NtmItems.POWDER_THORIUM.get(), 1), stack(NtmItems.POWDER_THORIUM.get(), 1), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_BERYLLIUM.get(), stack(NtmItems.POWDER_BERYLLIUM.get(), 1), stack(NtmItems.POWDER_BERYLLIUM.get(), 1), stack(NtmItems.POWDER_EMERALD.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_BERYLLIUM_DEEPSLATE.get(), stack(NtmItems.POWDER_BERYLLIUM.get(), 1), stack(NtmItems.POWDER_BERYLLIUM.get(), 1), stack(NtmItems.POWDER_EMERALD.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_ALUMINIUM.get(), stack(NtmItems.CHUNK_CRYOLITE.get(), 2), stack(NtmItems.POWDER_TITANIUM.get(), 1), stack(NtmItems.POWDER_IRON.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_ALUMINIUM_DEEPSLATE.get(), stack(NtmItems.CHUNK_CRYOLITE.get(), 2), stack(NtmItems.POWDER_TITANIUM.get(), 1), stack(NtmItems.POWDER_IRON.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_FLUORITE.get(), stack(NtmItems.FLUORITE.get(), 3), stack(NtmItems.FLUORITE.get(), 3), stack(NtmItems.GEM_SODALITE.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_FLUORITE_DEEPSLATE.get(), stack(NtmItems.FLUORITE.get(), 3), stack(NtmItems.FLUORITE.get(), 3), stack(NtmItems.GEM_SODALITE.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_COBALT.get(), stack(NtmItems.POWDER_COBALT.get(), 2), stack(NtmItems.POWDER_IRON.get(), 1), stack(NtmItems.POWDER_COPPER.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_COBALT_DEEPSLATE.get(), stack(NtmItems.POWDER_COBALT.get(), 2), stack(NtmItems.POWDER_IRON.get(), 1), stack(NtmItems.POWDER_COPPER.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_SCHRABIDIUM.get(), stack(NtmItems.POWDER_SCHRABIDIUM.get(), 1), stack(NtmItems.POWDER_SCHRABIDIUM.get(), 1), stack(NtmItems.NUGGET_SOLINIUM.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_NETHER_SCHRABIDIUM.get(), stack(NtmItems.POWDER_SCHRABIDIUM.get(), 1), stack(NtmItems.POWDER_SCHRABIDIUM.get(), 1), stack(NtmItems.NUGGET_SOLINIUM.get(), 1), stack(Blocks.NETHERRACK, 1));
        setRecipe(NtmBlocks.ORE_GNEISS_SCHRABIDIUM.get(), stack(NtmItems.POWDER_SCHRABIDIUM.get(), 1), stack(NtmItems.POWDER_SCHRABIDIUM.get(), 1), stack(NtmItems.NUGGET_SOLINIUM.get(), 1), stack(Blocks.STONE, 1));
        setRecipe(NtmBlocks.ORE_NETHER_PLUTONIUM.get(), stack(NtmItems.POWDER_PLUTONIUM.get(), 1), stack(NtmItems.POWDER_PLUTONIUM.get(), 1), stack(NtmItems.NUGGET_POLONIUM.get(), 3), stack(Blocks.NETHERRACK, 1));
        setRecipe(NtmBlocks.ORE_TIKITE.get(), stack(NtmItems.POWDER_PLUTONIUM.get(), 1), stack(NtmItems.POWDER_COBALT.get(), 2), stack(NtmItems.POWDER_NIOBIUM.get(), 2), stack(Blocks.NETHERRACK, 1));

        setRecipe(stack(Items.BLAZE_ROD, 1), stack(Items.BLAZE_POWDER, 1), stack(Items.BLAZE_POWDER, 1), stack(NtmItems.POWDER_FIRE.get(), 1), stack(NtmItems.POWDER_FIRE.get(), 1));
    }

    private static void registerCrystalRecipes() {
        setRecipe(stack(NtmItems.CRYSTAL_COAL.get(), 1), stack(NtmItems.POWDER_COAL.get(), 3), stack(NtmItems.POWDER_COAL.get(), 3), stack(NtmItems.POWDER_COAL.get(), 3), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_IRON.get(), 1), stack(NtmItems.POWDER_IRON.get(), 2), stack(NtmItems.POWDER_IRON.get(), 2), stack(NtmItems.POWDER_TITANIUM.get(), 1), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_GOLD.get(), 1), stack(NtmItems.POWDER_GOLD.get(), 2), stack(NtmItems.POWDER_GOLD.get(), 2), stack(NtmItems.NUGGET_MERCURY.get(), 1), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_REDSTONE.get(), 1), stack(Items.REDSTONE, 3), stack(Items.REDSTONE, 3), stack(Items.REDSTONE, 3), stack(NtmItems.NUGGET_MERCURY.get(), 3));
        setRecipe(stack(NtmItems.CRYSTAL_LAPIS.get(), 1), stack(NtmItems.POWDER_LAPIS.get(), 4), stack(NtmItems.POWDER_LAPIS.get(), 4), stack(NtmItems.POWDER_COBALT.get(), 1), stack(NtmItems.GEM_SODALITE.get(), 2));
        setRecipe(stack(NtmItems.CRYSTAL_DIAMOND.get(), 1), stack(NtmItems.POWDER_DIAMOND.get(), 1), stack(NtmItems.POWDER_DIAMOND.get(), 1), stack(NtmItems.POWDER_DIAMOND.get(), 1), stack(NtmItems.POWDER_DIAMOND.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_URANIUM.get(), 1), stack(NtmItems.POWDER_URANIUM.get(), 2), stack(NtmItems.POWDER_URANIUM.get(), 2), stack(NtmItems.NUGGET_RA226.get(), 2), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_THORIUM.get(), 1), stack(NtmItems.POWDER_THORIUM.get(), 2), stack(NtmItems.POWDER_THORIUM.get(), 2), stack(NtmItems.POWDER_URANIUM.get(), 1), stack(NtmItems.NUGGET_RA226.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_PLUTONIUM.get(), 1), stack(NtmItems.POWDER_PLUTONIUM.get(), 2), stack(NtmItems.POWDER_PLUTONIUM.get(), 2), stack(NtmItems.POWDER_POLONIUM.get(), 1), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_TITANIUM.get(), 1), stack(NtmItems.POWDER_TITANIUM.get(), 2), stack(NtmItems.POWDER_TITANIUM.get(), 2), stack(NtmItems.POWDER_IRON.get(), 1), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_SULFUR.get(), 1), stack(NtmItems.SULFUR.get(), 4), stack(NtmItems.SULFUR.get(), 4), stack(NtmItems.POWDER_IRON.get(), 1), stack(NtmItems.NUGGET_MERCURY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_NITER.get(), 1), stack(NtmItems.NITER.get(), 3), stack(NtmItems.NITER.get(), 3), stack(NtmItems.NITER.get(), 3), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_COPPER.get(), 1), stack(NtmItems.POWDER_COPPER.get(), 2), stack(NtmItems.POWDER_COPPER.get(), 2), stack(NtmItems.SULFUR.get(), 1), stack(NtmItems.POWDER_COBALT_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_TUNGSTEN.get(), 1), stack(NtmItems.POWDER_TUNGSTEN.get(), 2), stack(NtmItems.POWDER_TUNGSTEN.get(), 2), stack(NtmItems.POWDER_IRON.get(), 1), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_ALUMINIUM.get(), 1), stack(NtmItems.CHUNK_CRYOLITE.get(), 3), stack(NtmItems.POWDER_TITANIUM.get(), 1), stack(NtmItems.POWDER_IRON.get(), 1), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_FLUORITE.get(), 1), stack(NtmItems.FLUORITE.get(), 4), stack(NtmItems.FLUORITE.get(), 4), stack(NtmItems.GEM_SODALITE.get(), 2), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_BERYLLIUM.get(), 1), stack(NtmItems.POWDER_BERYLLIUM.get(), 2), stack(NtmItems.POWDER_BERYLLIUM.get(), 2), stack(NtmItems.POWDER_QUARTZ.get(), 1), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_LEAD.get(), 1), stack(NtmItems.POWDER_LEAD.get(), 2), stack(NtmItems.POWDER_LEAD.get(), 2), stack(NtmItems.POWDER_GOLD.get(), 1), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_SCHRARANIUM.get(), 1), stack(NtmItems.NUGGET_SCHRABIDIUM.get(), 2), stack(NtmItems.NUGGET_SCHRABIDIUM.get(), 2), stack(NtmItems.NUGGET_URANIUM.get(), 2), stack(NtmItems.NUGGET_NEPTUNIUM.get(), 2));
        setRecipe(stack(NtmItems.CRYSTAL_SCHRABIDIUM.get(), 1), stack(NtmItems.POWDER_SCHRABIDIUM.get(), 2), stack(NtmItems.POWDER_SCHRABIDIUM.get(), 2), stack(NtmItems.POWDER_PLUTONIUM.get(), 1), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_RARE.get(), 1), stack(NtmItems.POWDER_DESH_MIX.get(), 1), stack(NtmItems.POWDER_DESH_MIX.get(), 1), stack(NtmItems.NUGGET_ZIRCONIUM.get(), 2), stack(NtmItems.NUGGET_ZIRCONIUM.get(), 2));
        setRecipe(stack(NtmItems.CRYSTAL_PHOSPHORUS.get(), 1), stack(NtmItems.POWDER_FIRE.get(), 3), stack(NtmItems.POWDER_FIRE.get(), 3), stack(NtmItems.INGOT_PHOSPHORUS.get(), 2), stack(Items.BLAZE_POWDER, 2));
        setRecipe(stack(NtmItems.CRYSTAL_TRIXITE.get(), 1), stack(NtmItems.POWDER_PLUTONIUM.get(), 2), stack(NtmItems.POWDER_COBALT.get(), 3), stack(NtmItems.POWDER_NIOBIUM.get(), 2), stack(NtmItems.POWDER_NITAN_MIX.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_LITHIUM.get(), 1), stack(NtmItems.POWDER_LITHIUM.get(), 2), stack(NtmItems.POWDER_LITHIUM.get(), 2), stack(NtmItems.POWDER_QUARTZ.get(), 1), stack(NtmItems.FLUORITE.get(), 1));
        setRecipe(stack(NtmItems.CRYSTAL_STARMETAL.get(), 1), stack(NtmItems.POWDER_DURA_STEEL.get(), 3), stack(NtmItems.POWDER_COBALT.get(), 3), stack(NtmItems.POWDER_ASTATINE.get(), 2), stack(NtmItems.NUGGET_MERCURY.get(), 5));
        setRecipe(stack(NtmItems.CRYSTAL_COBALT.get(), 1), stack(NtmItems.POWDER_COBALT.get(), 2), stack(NtmItems.POWDER_IRON.get(), 3), stack(NtmItems.POWDER_COPPER.get(), 3), stack(NtmItems.POWDER_LITHIUM_TINY.get(), 1));
    }

    @Override
    public String getFileName() {
        return "hbmCentrifuge.json";
    }

    @Override
    public Object getRecipeObject() {
        return RECIPES;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        var obj = recipe.getAsJsonObject();
        ItemStack input = readItemStack(obj.get("input").getAsJsonArray());
        ItemStack[] output = readItemStackArray(obj.get("outputs").getAsJsonArray());
        setRecipe(input, output);
    }

    @Override
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        @SuppressWarnings("unchecked")
        Entry<ComparableStack, ItemStack[]> entry = (Entry<ComparableStack, ItemStack[]>) recipe;
        writer.name("input");
        writeItemStack(entry.getKey().toStack(), writer);
        writer.name("outputs").beginArray();
        for(ItemStack stack : entry.getValue()) {
            writeItemStack(stack, writer);
        }
        writer.endArray();
    }

    @Override
    public void registerDefaults() {
        registerOreRecipes();
        registerCrystalRecipes();
    }

    @Override
    public void deleteRecipes() {
        RECIPES.clear();
    }

    @Override
    public String getComment() {
        return "Centrifuge recipes ported from 1.12.2, limited to ore_ blocks, crystal_ items and vanilla ore blocks.";
    }
}
