package com.hbm.handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.CommonEvents;
import com.hbm.HBMsNTM;
import com.hbm.items.armor.ItemModCladding;
import com.hbm.lib.ModEffect;
import com.hbm.util.ShadyUtil;
import com.hbm.util.TagsUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class HazmatRegistry {
    public static void initDefault() {

        //assuming coefficient of 10
        //real coefficient turned out to be 5
        //oops

        double helmet = 0.2D;
        double chest = 0.4D;
        double legs = 0.3D;
        double boots = 0.1D;

        double gold = 0.0225D; // 5%

        HazmatRegistry.registerHazmat(Items.GOLDEN_HELMET, gold * helmet);
        HazmatRegistry.registerHazmat(Items.GOLDEN_CHESTPLATE, gold * chest);
        HazmatRegistry.registerHazmat(Items.GOLDEN_LEGGINGS, gold * legs);
        HazmatRegistry.registerHazmat(Items.GOLDEN_BOOTS, gold * boots);
    }

    private static final HashMap<Item, Double> ENTRIES = new HashMap<>();

    public static void registerHazmat(Item item, double resistance) {
        ENTRIES.put(item, resistance);
    }

    public static void addInfo(List<Component> components, Level level, ItemStack stack) {
        double rad = HazmatRegistry.getResistance(level, stack);
        rad = ((int)(rad * 1000)) / 1000D;
        if (rad > 0) components.add(Component.translatable("trait.radResistance", rad).withStyle(ChatFormatting.YELLOW));
    }

    public static double getResistance(Level level, ItemStack stack) {

        if (stack.isEmpty()) return 0;

        double cladding = getCladding(level, stack);

        Double f = ENTRIES.get(stack.getItem());

        if (f != null)
            return f + cladding;

        return cladding;
    }

    public static double getCladding(Level level, ItemStack stack) {

        if (TagsUtil.getTag(stack).getFloat("hfr_cladding") > 0)
            return TagsUtil.getTag(stack).getFloat("hfr_cladding");

        if (ArmorModHandler.hasMods(stack)) {

            ItemStack[] mods = ArmorModHandler.pryMods(level, stack);
            ItemStack cladding = mods[ArmorModHandler.CLADDING];

            if (cladding != null && cladding.getItem() instanceof ItemModCladding modCladding) {
                return modCladding.rad;
            }
        }

        return 0;
    }

    public static float getResistance(Player player) {

        float res = 0.0F;

        if (player.getUUID().toString().equals(ShadyUtil.Pu_238)) res += 0.4F;

        for (int i = 0; i < 4; i++) {
            res += (float) getResistance(player.level(), player.getInventory().armor.get(i));
        }

        if (player.hasEffect(ModEffect.RADX))
            res += 0.2F;

        return res;
    }

    public static final Gson gson = new Gson();

    public static void registerHazmats() {
        File folder = CommonEvents.configHbmDir;

        File config = new File(folder.getAbsolutePath() + File.separatorChar + "hbmRadResist.json");
        File template = new File(folder.getAbsolutePath() + File.separatorChar + "_hbmRadResist.json");

        initDefault();

        if (!config.exists()) {
            writeDefault(template);
        } else {
            HashMap<Item, Double> conf = readConfig(config);

            if (conf != null) {
                ENTRIES.clear();
                ENTRIES.putAll(conf);
            }
        }
    }

    private static void writeDefault(File file) {

        try {
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("comment").value("Template file, remove the underscore ('_') from the name to enable the config.");
            writer.name("entries").beginArray();

            for (Entry<Item, Double> entry : ENTRIES.entrySet()) {
                writer.beginObject();
                writer.name("item").value(BuiltInRegistries.ITEM.getKey(entry.getKey()).toString());
                writer.name("resistance").value(entry.getValue());
                writer.endObject();
            }

            writer.endArray();
            writer.endObject();
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static HashMap<Item, Double> readConfig(File config) {

        try {
            JsonObject json = gson.fromJson(new FileReader(config), JsonObject.class);
            JsonArray array = json.get("entries").getAsJsonArray();
            HashMap<Item, Double> conf = new HashMap<>();

            for (JsonElement element : array) {
                JsonObject object = (JsonObject) element;

                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(object.get("item").getAsString()));
                double resistance = object.get("resistance").getAsDouble();
                conf.put(item, resistance);
            }

            return conf;

        } catch(IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
