package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.StampItem.StampType;
import com.hbm.util.Tuple.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

public class PressRecipes extends SerializableRecipe {

    public static HashMap<Pair<AStack, StampType>, ItemStack> recipes = new HashMap<>();

    @Override
    public void registerDefaults() {
        makeRecipe(StampType.FLAT, new ComparableStack(Blocks.OAK_LOG.asItem(), 1, 4), NtmItems.NOTHING.get());
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
