package com.hbm.util;

import api.hbm.entity.IResistanceProvider;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.CommonEvents;
import com.hbm.HBMsNTM;
import com.hbm.util.Tuple.Quartet;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Basic handling/registry class for our custom resistance stats.
 * Handles resistances for individual armor pieces, full sets as well as entity classes for innate damage resistance
 *
 * @author hbm
 */
@EventBusSubscriber(modid = HBMsNTM.MODID)
public class DamageResistanceHandler {

    /** Currently cached DT reduction */
    public static float currentPDT = 0F;
    /** Currently cached armor piercing % */
    public static float currentPDR = 0F;

    public static final String CATEGORY_EXPLOSION = "EXPLOSION";
    public static final String CATEGORY_FIRE = "IN_FIRE";
    public static final String CATEGORY_PHYSICAL = "PHYSICAL";
    public static final String CATEGORY_ENERGY = "ENERGY";

    public static final Gson gson = new Gson();

    public static HashMap<Item, ResistanceStats> itemStats = new HashMap<>();
    public static HashMap<Quartet<Item, Item, Item, Item>, ResistanceStats> setStats = new HashMap<>();
    public static HashMap<Class<? extends Entity>, ResistanceStats> entityStats = new HashMap<>();

    public static HashMap<Item, List<Quartet<Item, Item, Item, Item>>> itemInfoSet = new HashMap<>();

    public static void init() {
        File folder = CommonEvents.configHbmDir;

        File config = new File(folder.getAbsolutePath() + File.separatorChar + "hbmArmor.json");
        File template = new File(folder.getAbsolutePath() + File.separatorChar + "_hbmArmor.json");

        clearSystem();

        if(!config.exists()) {
            initDefaults();
            writeDefault(template);
        } else {
            readConfig(config);
        }
    }

    private static void clearSystem() {
        itemStats.clear();
        setStats.clear();
        entityStats.clear();
        itemInfoSet.clear();
    }

    private static void writeDefault(File file) {

        HBMsNTM.LOGGER.info("No armor file found, registering defaults for {}", file.getName());

        try {
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("comment").value("Template file, remove the underscore ('_') from the name to enable the config.");

            serialize(writer);

            writer.endObject();
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void readConfig(File file) {

        HBMsNTM.LOGGER.info("Reading armor file {}", file.getName());

        try {
            JsonObject json = gson.fromJson(new FileReader(file), JsonObject.class);
            deserialize(json);

        } catch(FileNotFoundException ex) {
            clearSystem();
            initDefaults();
            ex.printStackTrace();
        }
    }

    public static void initDefaults() {
        entityStats.put(Creeper.class, new ResistanceStats().addCategory(CATEGORY_EXPLOSION, 2F, 0.25F));

        registerSet(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS, new ResistanceStats()
                .addCategory(CATEGORY_PHYSICAL, 2F, 0.15F)
                .addCategory(CATEGORY_FIRE, 0F, 0.25F)
                .addCategory(CATEGORY_EXPLOSION, 0F, 0.25F)
                .addExact(DamageTypes.FALL.location().toString(), 4F, 0.5F)
                .setOther(2F, 0.1F));

        registerSet(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS, new ResistanceStats()
                .addCategory(CATEGORY_PHYSICAL, 5F, 0.5F)
                .addCategory(CATEGORY_FIRE, 5F, 0.5F)
                .addCategory(CATEGORY_EXPLOSION, 5F, 0.25F)
                .addExact(DamageClass.LASER.name(), 15F, 0.9F)
                .addExact(DamageTypes.FALL.location().toString(), 10F, 0.5F)
                .setOther(5F, 0.25F));
    }

    public static void registerSet(Item helmet, Item plate, Item legs, Item boots, ResistanceStats stats) {
        Quartet<Item, Item, Item, Item> set = new Quartet<>(helmet, plate, legs, boots);
        setStats.put(set, stats);
        if (helmet != null) addToListInHashMap(helmet, itemInfoSet, set);
        if (plate != null) addToListInHashMap(plate, itemInfoSet, set);
        if (legs != null) addToListInHashMap(legs, itemInfoSet, set);
        if (boots != null) addToListInHashMap(boots, itemInfoSet, set);
    }

    public static void addToListInHashMap(Item item, HashMap<Item, List<Quartet<Item, Item, Item, Item>>> map, Quartet<Item, Item, Item, Item> listElement) {
        List<Quartet<Item, Item, Item, Item>> list = map.computeIfAbsent(item, k -> new ArrayList<>());
        list.add(listElement);
    }

    public static void addInfo(ItemStack stack, List<Component> components) {

        Item item = stack.getItem();

        if (itemInfoSet.containsKey(item)) {
            List<Quartet<Item, Item, Item, Item>> sets = itemInfoSet.get(item);

            for (Quartet<Item, Item, Item, Item> set : sets) {
                ResistanceStats stats = setStats.get(set);
                if (stats == null) continue;

                List<Component> toAdd = new ArrayList<>();

                for (Entry<String, Resistance> entry : stats.categoryResistances.entrySet()) {
                    String key = "damage.category." + entry.getKey();
                    Resistance res = entry.getValue();
                    toAdd.add(Component.literal(I18nUtil.resolveKey(key) + ": " + res.threshold + "/" + (int)(res.resistance * 100) + "%").withStyle(ChatFormatting.GRAY));
                }

                for (Entry<String, Resistance> entry : stats.exactResistances.entrySet()) {
                    String key = "damage.exact." + entry.getKey();
                    Resistance res = entry.getValue();
                    toAdd.add(Component.literal(I18nUtil.resolveKey(key) + ": " + res.threshold + "/" + (int)(res.resistance * 100) + "%").withStyle(ChatFormatting.GRAY));
                }

                if (stats.otherResistance != null) {
                    Resistance res = stats.otherResistance;
                    toAdd.add(Component.literal(I18nUtil.resolveKey("damage.other") + ": " + res.threshold + "/" + (int)(res.resistance * 100) + "%").withStyle(ChatFormatting.GRAY));
                }

                if (!toAdd.isEmpty()) {
                    components.add(Component.literal(I18nUtil.resolveKey("damage.inset")).withStyle(ChatFormatting.DARK_PURPLE));

                    if (set.getW() != null)
                        components.add(Component.literal("  ")
                                .append(new ItemStack(set.getW()).getHoverName())
                                .withStyle(ChatFormatting.DARK_PURPLE));
                    if (set.getX() != null)
                        components.add(Component.literal("  ")
                                .append(new ItemStack(set.getX()).getHoverName())
                                .withStyle(ChatFormatting.DARK_PURPLE));
                    if (set.getY() != null)
                        components.add(Component.literal("  ")
                                .append(new ItemStack(set.getY()).getHoverName())
                                .withStyle(ChatFormatting.DARK_PURPLE));
                    if (set.getZ() != null)
                        components.add(Component.literal("  ")
                                .append(new ItemStack(set.getZ()).getHoverName())
                                .withStyle(ChatFormatting.DARK_PURPLE));

                    components.addAll(toAdd);
                }

                break; // TEMP, only show one set for now
            }
        }

        if (itemStats.containsKey(item)) {
            ResistanceStats stats = itemStats.get(item);

            List<Component> toAdd = new ArrayList<>();

            for (Entry<String, Resistance> entry : stats.categoryResistances.entrySet()) {
                String key = "damage.category." + entry.getKey();
                Resistance res = entry.getValue();
                toAdd.add(Component.literal(I18nUtil.resolveKey(key) + ": " + res.threshold + "/" + (int)(res.resistance * 100) + "%").withStyle(ChatFormatting.GRAY));
            }

            for (Entry<String, Resistance> entry : stats.exactResistances.entrySet()) {
                String key = "damage.exact." + entry.getKey();
                Resistance res = entry.getValue();
                toAdd.add(Component.literal(I18nUtil.resolveKey(key) + ": " + res.threshold + "/" + (int)(res.resistance * 100) + "%").withStyle(ChatFormatting.GRAY));
            }

            if (stats.otherResistance != null) {
                Resistance res = stats.otherResistance;
                toAdd.add(Component.literal(I18nUtil.resolveKey("damage.other") + ": " + res.threshold + "/" + (int)(res.resistance * 100) + "%").withStyle(ChatFormatting.GRAY));
            }

            if (!toAdd.isEmpty()) {
                components.add(Component.literal(I18nUtil.resolveKey("damage.item")).withStyle(ChatFormatting.DARK_PURPLE));
                components.addAll(toAdd);
            }
        }
    }


    public static void serialize(JsonWriter writer) throws IOException {
        /// ITEMS ///
        writer.name("itemStats").beginArray();
        for (Entry<Item, ResistanceStats> entry : itemStats.entrySet()) {
            writer.beginArray().setIndent("");
            writer.value(BuiltInRegistries.ITEM.getKey(entry.getKey()).toString()).setIndent("  ");
            writer.beginObject();
            entry.getValue().serialize(writer);
            writer.setIndent("");
            writer.endObject().endArray().setIndent("  ");
        }
        writer.endArray();

        /// SETS ///
        writer.name("setStats").beginArray();
        for (Entry<Quartet<Item, Item, Item, Item>, ResistanceStats> entry : setStats.entrySet()) {
            writer.beginArray().setIndent("");
            writer.value(BuiltInRegistries.ITEM.getKey(entry.getKey().getW()).toString())
                    .value(BuiltInRegistries.ITEM.getKey(entry.getKey().getX()).toString())
                    .value(BuiltInRegistries.ITEM.getKey(entry.getKey().getY()).toString())
                    .value(BuiltInRegistries.ITEM.getKey(entry.getKey().getZ()).toString()).setIndent("  ");
            writer.beginObject();
            entry.getValue().serialize(writer);
            writer.setIndent("");
            writer.endObject().endArray().setIndent("  ");
        }
        writer.endArray();

        /// ENTITIES ///
        writer.name("entityStats").beginArray();
        for (Entry<Class<? extends Entity>, ResistanceStats> entry : entityStats.entrySet()) {
            writer.beginArray().setIndent("");
            writer.value(entry.getKey().getName()).setIndent("  ");
            writer.beginObject();
            entry.getValue().serialize(writer);
            writer.setIndent("");
            writer.endObject().endArray().setIndent("  ");
        }
        writer.endArray();
    }

    public static void deserialize(JsonObject json) {
        /// ITEMS ///
        JsonArray itemStatsArray = json.get("itemStats").getAsJsonArray();
        for (JsonElement element : itemStatsArray) {
            JsonArray statArray = element.getAsJsonArray();
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(statArray.get(0).getAsString()));
            JsonObject stats = statArray.get(1).getAsJsonObject();
            itemStats.put(item, ResistanceStats.deserialize(stats));
        }

        /// SETS ///
        JsonArray setStatsArray = json.get("setStats").getAsJsonArray();
        for(JsonElement element : setStatsArray) {
            JsonArray statArray = element.getAsJsonArray();
            Item helmet =	statArray.get(0).isJsonNull() ? null : BuiltInRegistries.ITEM.get(ResourceLocation.parse(statArray.get(0).getAsString()));
            Item plate =	statArray.get(1).isJsonNull() ? null : BuiltInRegistries.ITEM.get(ResourceLocation.parse(statArray.get(1).getAsString()));
            Item legs =		statArray.get(2).isJsonNull() ? null : BuiltInRegistries.ITEM.get(ResourceLocation.parse(statArray.get(2).getAsString()));
            Item boots =	statArray.get(3).isJsonNull() ? null : BuiltInRegistries.ITEM.get(ResourceLocation.parse(statArray.get(3).getAsString()));
            JsonObject stats = statArray.get(4).getAsJsonObject();
            registerSet(helmet, plate, legs, boots, ResistanceStats.deserialize(stats));
        }

        /// ENTITIES ///
        JsonArray entityStatsArray = json.get("entityStats").getAsJsonArray();
        for (JsonElement element : entityStatsArray) {
            JsonArray statArray = element.getAsJsonArray();
            try {
                Class clazz = Class.forName(statArray.get(0).getAsString());
                JsonObject stats = statArray.get(1).getAsJsonObject();
                entityStats.put(clazz, ResistanceStats.deserialize(stats));
            } catch(ClassNotFoundException ignored) { }
        }
    }


    public enum DamageClass {
        PHYSICAL,
        IN_FIRE,
        EXPLOSION,
        ELECTRIC,
        LASER,
        MICROWAVE,
        SUBATOMIC,
        OTHER
    }

    public static void setup(float dt, float dr) {
        currentPDT = dt;
        currentPDR = dr;
    }

    public static void reset() {
        currentPDT = 0;
        currentPDR = 0;
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingIncomingDamageEvent event) {
        DamageSource source = event.getSource();

        if (source.is(DamageTypeTags.BYPASSES_ARMOR)) return;

        LivingEntity entity = event.getEntity();
        float amount = event.getOriginalAmount();

        float[] vals = getDTDR(entity, source, amount, currentPDT, currentPDR);
        float dt = vals[0] - currentPDT;
        float dr = vals[1] - currentPDR;

        if ((dt > 0 && dt >= amount) || dr >= 1.0F) {
            event.setCanceled(true);
            EntityDamageUtil.damageArmorNT(entity, amount);
        }
    }

    @SubscribeEvent
    public static void onEntityDamaged(LivingDamageEvent.Pre event) {
        event.setNewDamage(calculateDamage(event.getEntity(), event.getSource(), event.getOriginalDamage(), currentPDT, currentPDR));
        if(event.getEntity() instanceof IResistanceProvider irp) {
            irp.onDamageDealt(event.getSource(), event.getNewDamage());
        }
    }

    public static String typeToCategory(DamageSource source) {
        if (source.is(DamageTypeTags.IS_EXPLOSION)) return CATEGORY_EXPLOSION;
        if (source.is(DamageTypeTags.IS_FIRE)) return CATEGORY_FIRE;
        if (source.is(DamageTypeTags.IS_PROJECTILE)) return CATEGORY_PHYSICAL;
        if (source.is(DamageTypes.CACTUS)) return CATEGORY_PHYSICAL;
        if (source.getEntity() != null) return CATEGORY_PHYSICAL;
        String typeId = source.getMsgId();
        if (typeId.equals(DamageClass.LASER.name().toLowerCase())) return CATEGORY_ENERGY;
        if (typeId.equals(DamageClass.MICROWAVE.name().toLowerCase())) return CATEGORY_ENERGY;
        if (typeId.equals(DamageClass.SUBATOMIC.name().toLowerCase())) return CATEGORY_ENERGY;
        return typeId;
    }

    public static float calculateDamage(LivingEntity entity, DamageSource damage, float amount, float pierceDT, float pierce) {
        //if (damage.is(DamageTypeTags.BYPASSES_ARMOR)) return amount;
        // for now armor going to block all damage types

        float[] vals = getDTDR(entity, damage, amount, pierceDT, pierce);
        float dt = vals[0];
        float dr = vals[1];

        dt = Math.max(0F, dt - pierceDT);
        if(dt >= amount) return 0F;
        amount -= dt;
        dr *= Mth.clamp(1F - pierce, 0F, 2F /* we allow up to -1 armor piercing, which can double effective armor values */);

        return amount *= (1F - dr);
    }

    public static float[] getDTDR(LivingEntity entity, DamageSource damage, float amount, float pierceDT, float pierce) {

        float dt = 0;
        float dr = 0;

        if (entity instanceof IResistanceProvider irp) {
            float[] res = irp.getCurrentDTDR(damage, amount, pierceDT, pierce);
            dt += res[0];
            dr += res[1];
        }

        /// SET HANDLING ///
        Quartet<Item, Item, Item, Item> wornSet = new Quartet<>(
                entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty() ? null : entity.getItemBySlot(EquipmentSlot.HEAD).getItem(),
                entity.getItemBySlot(EquipmentSlot.CHEST).isEmpty() ? null : entity.getItemBySlot(EquipmentSlot.CHEST).getItem(),
                entity.getItemBySlot(EquipmentSlot.LEGS).isEmpty() ? null : entity.getItemBySlot(EquipmentSlot.LEGS).getItem(),
                entity.getItemBySlot(EquipmentSlot.FEET).isEmpty() ? null : entity.getItemBySlot(EquipmentSlot.FEET).getItem()
        );

        ResistanceStats setResistance = setStats.get(wornSet);
        if (setResistance != null) {
            Resistance res = setResistance.getResistance(damage, entity);
            if (res != null) {
                dt += res.threshold;
                dr += res.resistance;
            }
        }

        /// ARMOR ///
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmor()) continue;
            ItemStack armor = entity.getItemBySlot(slot);
            if (armor.isEmpty()) continue;
            ResistanceStats stats = itemStats.get(armor.getItem());
            if (stats == null) continue;
            Resistance res = stats.getResistance(damage, entity);
            if (res == null) continue;
            dt += res.threshold;
            dr += res.resistance;
        }

        /// ENTITY CLASS HANDLING ///
        ResistanceStats innateResistance = entityStats.get(entity.getClass());
        if (innateResistance != null) {
            Resistance res = innateResistance.getResistance(damage, entity);
            if (res != null) {
                dt += res.threshold;
                dr += res.resistance;
            }
        }

        return new float[] {dt, dr};
    }

    public static class ResistanceStats {
        public HashMap<String, Resistance> exactResistances = new HashMap<>();
        public HashMap<String, Resistance> categoryResistances = new HashMap<>();
        public Resistance otherResistance;

        public Resistance getResistance(DamageSource source, LivingEntity entity) {
            Registry<DamageType> registry = entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
            String damageTypeId = String.valueOf(registry.getKey(source.type()));
            HBMsNTM.LOGGER.info("{}", source.type());
            Resistance exact = exactResistances.get(damageTypeId);
            if (exact != null) return exact;
            Resistance category = categoryResistances.get(typeToCategory(source));
            if (category != null) return category;
            return otherResistance;
        }

        public ResistanceStats addExact(String damageTypeId, float threshold, float resistance) { exactResistances.put(damageTypeId, new Resistance(threshold, resistance)); return this; }
        public ResistanceStats addCategory(String type, float threshold, float resistance) { categoryResistances.put(type, new Resistance(threshold, resistance)); return this; }
        public ResistanceStats setOther(float threshold, float resistance) { otherResistance = new Resistance(threshold, resistance); return this; }

        public void serialize(JsonWriter writer) throws IOException {
            if (!exactResistances.isEmpty()) {
                writer.name("exact").beginArray();
                for(Entry<String, Resistance> entry : exactResistances.entrySet()) {
                    writer.beginArray().setIndent("");
                    writer.value(entry.getKey()).value(entry.getValue().threshold).value(entry.getValue().resistance).endArray().setIndent("  ");
                }
                writer.endArray();
            }

            if (!categoryResistances.isEmpty()) {
                writer.name("category").beginArray();
                for(Entry<String, Resistance> entry : categoryResistances.entrySet()) {
                    writer.beginArray().setIndent("");
                    writer.value(entry.getKey()).value(entry.getValue().threshold).value(entry.getValue().resistance).endArray().setIndent("  ");
                }
                writer.endArray();
            }

            if (otherResistance != null) {
                writer.name("other").beginArray().setIndent("");
                writer.value(otherResistance.threshold).value(otherResistance.resistance).endArray().setIndent("  ");
            }
        }

        public static ResistanceStats deserialize(JsonObject json) {
            ResistanceStats stats = new ResistanceStats();

            if (json.has("exact")) {
                JsonArray exact = json.get("exact").getAsJsonArray();
                for(JsonElement element : exact) {
                    JsonArray array = element.getAsJsonArray();
                    stats.exactResistances.put(array.get(0).getAsString(), new Resistance(array.get(1).getAsFloat(), array.get(2).getAsFloat()));
                }
            }

            if (json.has("category")) {
                JsonArray category = json.get("category").getAsJsonArray();
                for(JsonElement element : category) {
                    JsonArray array = element.getAsJsonArray();
                    stats.categoryResistances.put(array.get(0).getAsString(), new Resistance(array.get(1).getAsFloat(), array.get(2).getAsFloat()));
                }
            }

            if (json.has("other")) {
                JsonArray other = json.get("other").getAsJsonArray();
                stats.otherResistance = new Resistance(other.get(0).getAsFloat(), other.get(1).getAsFloat());
            }

            return stats;
        }
    }

    public static class Resistance {

        public float threshold;
        public float resistance;

        public Resistance(float threshold, float resistance) {
            this.threshold = threshold;
            this.resistance = resistance;
        }
    }
}
