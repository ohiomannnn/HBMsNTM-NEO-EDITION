package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.StampItem;
import com.hbm.items.machine.StampItem.StampType;
import com.hbm.util.Tuple.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

public class PressRecipes extends SerializableRecipe {

    public static HashMap<Pair<AStack, StampType>, ItemStack> recipes = new HashMap<>();

    public static ItemStack getOutput(ItemStack ingredient, ItemStack stamp) {

        if(ingredient.isEmpty() || stamp.isEmpty()) return ItemStack.EMPTY;
        if(!(stamp.getItem() instanceof StampItem stampItem)) return ItemStack.EMPTY;

        StampType type = stampItem.type;

        for(Entry<Pair<AStack, StampType>, ItemStack> recipe : recipes.entrySet()) {

            if(recipe.getKey().getValue() == type && recipe.getKey().getKey().matchesRecipe(ingredient, true)) return recipe.getValue();
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void registerDefaults() {

        makeRecipe(StampType.PLATE, new ComparableStack(Items.IRON_INGOT),			                NtmItems.PLATE_IRON.get());
        makeRecipe(StampType.PLATE, new ComparableStack(Items.GOLD_INGOT),			                NtmItems.PLATE_GOLD.get());
        makeRecipe(StampType.PLATE, new ComparableStack(NtmItems.INGOT_TITANIUM.get()),			    NtmItems.PLATE_TITANIUM.get());
        makeRecipe(StampType.PLATE, new ComparableStack(NtmItems.INGOT_ALUMINIUM.get()),			NtmItems.PLATE_ALUMINIUM.get());
        makeRecipe(StampType.PLATE, new ComparableStack(NtmItems.INGOT_STEEL.get()),		        NtmItems.PLATE_STEEL.get());
        makeRecipe(StampType.PLATE, new ComparableStack(NtmItems.INGOT_LEAD.get()),			        NtmItems.PLATE_LEAD.get());
        makeRecipe(StampType.PLATE, new ComparableStack(Items.COPPER_INGOT),			            NtmItems.PLATE_COPPER.get());
        makeRecipe(StampType.PLATE, new ComparableStack(NtmItems.INGOT_SCHRABIDIUM.get()),		    NtmItems.PLATE_SCHRABIDIUM.get());
        makeRecipe(StampType.PLATE, new ComparableStack(NtmItems.INGOT_COMBINE_STEEL.get()),		NtmItems.PLATE_COMBINE_STEEL.get());
        makeRecipe(StampType.PLATE, new ComparableStack(NtmItems.INGOT_GUNMETAL.get()),		        NtmItems.PLATE_GUNMETAL.get());
        makeRecipe(StampType.PLATE, new ComparableStack(NtmItems.INGOT_WEAPON_STEEL.get()),     	NtmItems.PLATE_WEAPON_STEEL.get());
        makeRecipe(StampType.PLATE, new ComparableStack(NtmItems.INGOT_SATURNITE.get()),		    NtmItems.PLATE_SATURNITE.get());
        makeRecipe(StampType.PLATE, new ComparableStack(NtmItems.INGOT_DURA_STEEL.get()),			NtmItems.PLATE_DURA_STEEL.get());

        makeRecipe(StampType.WIRE, new ComparableStack(Items.GOLD_INGOT),			                new ItemStack(NtmItems.WIRE_GOLD.get(), 8));
        makeRecipe(StampType.WIRE, new ComparableStack(NtmItems.INGOT_ALUMINIUM.get()),			    new ItemStack(NtmItems.WIRE_ALUMINIUM.get(), 8));
        makeRecipe(StampType.WIRE, new ComparableStack(NtmItems.INGOT_STEEL.get()),		            new ItemStack(NtmItems.WIRE_STEEL.get(), 8));
        makeRecipe(StampType.WIRE, new ComparableStack(NtmItems.INGOT_LEAD.get()),			        new ItemStack(NtmItems.WIRE_LEAD.get(), 8));
        makeRecipe(StampType.WIRE, new ComparableStack(Items.COPPER_INGOT),			                new ItemStack(NtmItems.WIRE_COPPER.get(), 8));
        makeRecipe(StampType.WIRE, new ComparableStack(NtmItems.INGOT_SCHRABIDIUM.get()),		    new ItemStack(NtmItems.WIRE_SCHRABIDIUM.get(), 8));
    }

    public static void makeRecipe(StampType type, AStack in, Item out) {
        recipes.put(new Pair<>(in, type),  new ItemStack(out));
    }
    public static void makeRecipe(StampType type, AStack in, ItemStack out) {
        recipes.put(new Pair<>(in, type),  out);
    }

    @Override
    public String getFileName() {
        return "hbmPress.json";
    }

    @Override
    public Object getRecipeObject() {
        return recipes;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject obj = (JsonObject) recipe;

        AStack input = SerializableRecipe.readAStack(obj.get("input").getAsJsonArray());
        StampType stamp = StampType.valueOf(obj.get("stamp").getAsString().toUpperCase());
        ItemStack output = SerializableRecipe.readItemStack(obj.get("output").getAsJsonArray());

        makeRecipe(stamp, input, output);
    }

    @Override
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        Entry<Pair<AStack, StampType>, ItemStack> entry = (Entry<Pair<AStack, StampType>, ItemStack>) recipe;

        writer.name("input");
        SerializableRecipe.writeAStack(entry.getKey().getKey(), writer);
        writer.name("stamp").value(entry.getKey().getValue().name().toLowerCase(Locale.US));
        writer.name("output");
        SerializableRecipe.writeItemStack(entry.getValue(), writer);
    }

    @Override
    public void deleteRecipes() {
        recipes.clear();
    }
}
