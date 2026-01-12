package com.hbm.inventory.recipes.loader;

import api.hbm.recipe.IRecipeRegisterListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.CommonEvents;
import com.hbm.HBMsNTM;
import com.hbm.items.ModItems;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.util.Tuple.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.*;
import java.util.*;

//the anti-spaghetti. this class provides so much functionality and saves so much time, i just love you, SerializableRecipe <3
public abstract class SerializableRecipe {

    public static final Gson gson = new Gson();
    public static List<SerializableRecipe> recipeHandlers = new ArrayList<>();
    public static List<IRecipeRegisterListener> additionalListeners = new ArrayList<>();

    public static Map<String, InputStream> recipeSyncHandlers = new HashMap<>();

    public boolean modified = false;

    /*
     * INIT
     */

    public static void registerAllHandlers() {

    }

    public static void initialize() {
        File recDir = new File(CommonEvents.configDir.getAbsolutePath() + File.separatorChar + "hbmRecipes");

        if (!recDir.exists()) {
            if (!recDir.mkdir()) {
                throw new IllegalStateException("Unable to make recipe directory " + recDir.getAbsolutePath());
            }
        }

        File info = new File(recDir.getAbsolutePath() + File.separatorChar + "REMOVE UNDERSCORE TO ENABLE RECIPE LOADING - RECIPES WILL RESET TO DEFAULT OTHERWISE");
        try { info.createNewFile(); } catch(IOException ignored) { }

        HBMsNTM.LOGGER.info("Starting recipe init!");

        GenericRecipes.clearPools();

        for (SerializableRecipe recipe : recipeHandlers) {

            recipe.deleteRecipes();

            File recFile = new File(recDir.getAbsolutePath() + File.separatorChar + recipe.getFileName());
            if (recipeSyncHandlers.containsKey(recipe.getFileName())) {
                HBMsNTM.LOGGER.info("Reading synced recipe file {}", recipe.getFileName());
                InputStream stream = recipeSyncHandlers.get(recipe.getFileName());

                try {
                    stream.reset();
                    Reader reader = new InputStreamReader(stream);
                    recipe.readRecipeStream(reader);
                    recipe.modified = true;
                } catch (Throwable ex) {
                    HBMsNTM.LOGGER.error("Failed to reset synced recipe stream", ex);
                }
            } else if (recFile.exists() && recFile.isFile()) {
                HBMsNTM.LOGGER.info("Reading recipe file " + recFile.getName());
                recipe.readRecipeFile(recFile);
                recipe.modified = true;
            } else {
                HBMsNTM.LOGGER.info("No recipe file found, registering defaults for " + recipe.getFileName());
                recipe.registerDefaults();

                for(IRecipeRegisterListener listener : additionalListeners) {
                    listener.onRecipeLoad(recipe.getClass().getSimpleName());
                }

                File recTemplate = new File(recDir.getAbsolutePath() + File.separatorChar + "_" + recipe.getFileName());
                HBMsNTM.LOGGER.info("Writing template file " + recTemplate.getName());
                recipe.writeTemplateFile(recTemplate);
                recipe.modified = false;
            }

            recipe.registerPost();
        }

        HBMsNTM.LOGGER.info("Finished recipe init!");
    }

    public static void receiveRecipes(String filename, byte[] data) {
        recipeSyncHandlers.put(filename, new ByteArrayInputStream(data));
    }

    public static void clearReceivedRecipes() {
        boolean hasCleared = !recipeSyncHandlers.isEmpty();
        recipeSyncHandlers.clear();

        if (hasCleared) initialize();
    }

    /*
     * ABSTRACT
     */

    /** The machine's (or process') name used for the recipe file */
    public abstract String getFileName();
    /** Return the list object holding all the recipes, usually an ArrayList or HashMap */
    public abstract Object getRecipeObject();
    /** Will use the supplied JsonElement (usually casts to JsonArray) from the over arching recipe array and adds the recipe to the recipe list object */
    public abstract void readRecipe(JsonElement recipe);
    /** Is given a single recipe from the recipe list object (a wrapper, Tuple, array, HashMap Entry, etc) and writes it to the current ongoing GSON stream
     * @throws IOException very scary */
    public abstract void writeRecipe(Object recipe, JsonWriter writer) throws IOException;
    /** Registers the default recipes */
    public abstract void registerDefaults();
    /** Deletes all existing recipes, currently unused */
    public abstract void deleteRecipes();
    /** A routine called after registering all recipes, whether it's a template or not. Good for IMC functionality. */
    public void registerPost() { }
    /** Returns a string to be printed as info at the top of the JSON file */
    public String getComment() {
        return null;
    }

    /*
     * JSON R/W WRAPPERS
     */

    public void writeTemplateFile(File template) {

        try {
            /* Get the recipe list object */
            Object recipeObject = this.getRecipeObject();
            List recipeList = new ArrayList();

            /* Try to pry all recipes from our list */
            if (recipeObject instanceof Collection) {
                recipeList.addAll((Collection) recipeObject);

            } else if (recipeObject instanceof HashMap) {
                recipeList.addAll(((HashMap) recipeObject).entrySet());
            }

            if (recipeList.isEmpty())
                throw new IllegalStateException("Error while writing recipes for " + this.getClass().getSimpleName() + ": Recipe list is either empty or in an unsupported format!");

            JsonWriter writer = new JsonWriter(new FileWriter(template));
            writer.setIndent("  ");					//pretty formatting
            writer.beginObject();					//initial '{'

            if (this.getComment() != null) {
                writer.name("comment").value(this.getComment());
            }

            writer.name("recipes").beginArray();	//all recipes are stored in an array called "recipes"

            for (Object recipe : recipeList) {
                writer.beginObject();				//begin object for a single recipe
                this.writeRecipe(recipe, writer);	//serialize here
                writer.endObject();					//end recipe object
            }

            writer.endArray();						//end recipe array
            writer.endObject();						//final '}'
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void readRecipeFile(File file) {
        try {
            readRecipeStream(new FileReader(file));
        } catch (FileNotFoundException ignored) { }
    }

    public void readRecipeStream(Reader reader) {
        JsonObject json = gson.fromJson(reader, JsonObject.class);
        JsonArray recipes = json.get("recipes").getAsJsonArray();
        for (JsonElement recipe : recipes) {
            if (recipe != null) this.readRecipe(recipe);
        }
    }

    /*
     * JSON IO UTIL
     */

//    public static AStack readAStack(JsonArray array) {
//        try {
//            String type = array.get(0).getAsString();
//            int stacksize = array.size() > 2 ? array.get(2).getAsInt() : 1;
//            if ("nbt".equals(type)) {
//                Item item = (Item) Item.itemRegistry.getObject(array.get(1).getAsString());
//                int meta = array.size() > 3 ? array.get(3).getAsInt() : 0;
//                NBTBase nbt = JsonToNBT.func_150315_a(array.get(array.size() - 1).getAsString());
//                return new NBTStack(item, stacksize, meta).withNBT(nbt instanceof NBTTagCompound ? (NBTTagCompound) nbt : null);
//            }
//            if ("item".equals(type)) {
//                Item item = (Item) Item.itemRegistry.getObject(array.get(1).getAsString());
//                int meta = array.size() > 3 ? array.get(3).getAsInt() : 0;
//                return new ComparableStack(item, stacksize, meta);
//            }
//        } catch (Exception ignored) { }
//        MainRegistry.logger.error("Error reading stack array " + array.toString());
//        return new ComparableStack(ModItems.nothing);
//    }
//
//    public static AStack[] readAStackArray(JsonArray array) {
//        try {
//            AStack[] items = new AStack[array.size()];
//            for(int i = 0; i < items.length; i++) { items[i] = readAStack((JsonArray) array.get(i)); }
//            return items;
//        } catch(Exception ex) { }
//        MainRegistry.logger.error("Error reading stack array " + array.toString());
//        return new AStack[0];
//    }
//
//    public static void writeAStack(AStack astack, JsonWriter writer) throws IOException {
//        writer.beginArray();
//        writer.setIndent("");
//        if(astack instanceof NBTStack) {
//            NBTStack comp = (NBTStack) astack;
//            writer.value(comp.nbt != null ? "nbt" : "item");							//NBT  identifier
//            writer.value(Item.itemRegistry.getNameForObject(comp.toStack().getItem()));	//item name
//            if(comp.stacksize != 1 || comp.meta > 0) writer.value(comp.stacksize);		//stack size
//            if(comp.meta > 0) writer.value(comp.meta);									//metadata
//            if(comp.nbt != null) writer.value(comp.nbt.toString());						//NBT
//        } else if(astack instanceof ComparableStack) {
//            ComparableStack comp = (ComparableStack) astack;
//            writer.value("item");														//ITEM  identifier
//            writer.value(Item.itemRegistry.getNameForObject(comp.toStack().getItem()));	//item name
//            if(comp.stacksize != 1 || comp.meta > 0) writer.value(comp.stacksize);		//stack size
//            if(comp.meta > 0) writer.value(comp.meta);									//metadata
//        } else if(astack instanceof OreDictStack) {
//            OreDictStack ore = (OreDictStack) astack;
//            writer.value("dict");														//DICT identifier
//            writer.value(ore.name);														//dict name
//            if(ore.stacksize != 1) writer.value(ore.stacksize);							//stacksize
//        }
//        writer.endArray();
//        writer.setIndent("  ");
//    }

    public static ItemStack readItemStack(JsonArray array) {
        try {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(array.get(0).getAsString()));
            int stackSize = array.size() > 1 ? array.get(1).getAsInt() : 1;
            return new ItemStack(item, stackSize);
        } catch(Exception ignored) { }
        HBMsNTM.LOGGER.error("Error reading stack array {} - defaulting to NOTHING item!", array.toString());
        return new ItemStack(ModItems.NOTHING.get());
    }

    public static Pair<ItemStack, Float> readItemStackChance(JsonArray array) {
        try {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(array.get(0).getAsString()));
            int stackSize = array.size() > 2 ? array.get(1).getAsInt() : 1;
            float chance = array.get(array.size() - 1).getAsFloat();
            return new Pair(new ItemStack(item, stackSize), chance);
        } catch (Exception ignored) { }
        HBMsNTM.LOGGER.error("Error reading stack array {} - defaulting to NOTHING item!", array.toString());
        return new Pair(new ItemStack(ModItems.NOTHING.get()), 1F);
    }

    public static void writeItemStack(ItemStack stack, JsonWriter writer) throws IOException {
        writer.beginArray();
        writer.setIndent("");
        writer.value(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());  //item name
        if (stack.getCount() != 1) { writer.value(stack.getCount()); }            //stack size
        writer.endArray();
        writer.setIndent("  ");
    }

    public static void writeItemStackChance(Pair<ItemStack, Float> stack, JsonWriter writer) throws IOException {
        writer.beginArray();
        writer.setIndent("");
        writer.value(BuiltInRegistries.ITEM.getKey(stack.getKey().getItem()).toString());           //item name
        if (stack.getKey().getCount() != 1) { writer.value(stack.getKey().getCount()); }            //stack size
        writer.value(stack.value);
        writer.endArray();
        writer.setIndent("  ");
    }
}
