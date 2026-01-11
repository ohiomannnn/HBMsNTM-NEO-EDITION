package com.hbm.inventory.fluid;

import api.hbm.fluidmk2.IFluidRegisterListener;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.CommonEvents;
import com.hbm.inventory.fluid.trait.FluidTrait;
import com.hbm.render.util.EnumSymbol;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Fluids {
    public static final Gson gson = new Gson();

    public static List<IFluidRegisterListener> additionalListeners = new ArrayList<>();

    public static FluidType NONE;

    public static final HashBiMap<String, FluidType> renameMapping = HashBiMap.create();

    public static List<FluidType> customFluids = new ArrayList<>();
    public static List<FluidType> foreignFluids = new ArrayList<>();

    private static final HashMap<Integer, FluidType> idMapping = new HashMap<>();
    private static final HashMap<String, FluidType> nameMapping = new HashMap<>();
    /** Inconsequential, only actually used when listing all fluids with niceOrder disabled */
    protected static final List<FluidType> registerOrder = new ArrayList<>();
    /** What's used to list fluids with niceOrder enabled */
    public static final List<FluidType> metaOrder = new ArrayList<>();

    public static void init() {

        // ##### ##### ##### ##### ##  # ##### #   # ##### ##  # #####
        // #   #   #     #   #     ##  # #     #   # #   # ##  # #
        // #####   #     #   ###   # # # ##### ##### #   # # # # ###
        // #   #   #     #   #     #  ##     # #   # #   # #  ## #
        // #   #   #     #   ##### #  ## ##### #   # ##### #  ## #####

        /*
         * The mapping ID is set in the CTOR, which is the static, never shifting ID that is used to save the fluid type.
         * Therefore, ALWAYS append new fluid entries AT THE BOTTOM to avoid unnecessary ID shifting.
         * In addition, you have to add your fluid to 'metaOrder' which is what is used to sort fluid identifiers and whatnot in the inventory.
         * You may screw with metaOrder as much as you like, as long as you keep all fluids in the list exactly once.
         */

        NONE =					new FluidType("NONE",				0x888888, 0, 0, 0, EnumSymbol.NONE);

        // ^ ^ ^ ^ ^ ^ ^ ^
        //ADD NEW FLUIDS HERE

        File folder = CommonEvents.configHbmDir;
        File customTypes = new File(folder.getAbsolutePath() + File.separatorChar + "hbmFluidTypes.json");
        if (!customTypes.exists()) initDefaultFluids(customTypes);
        readCustomFluids(customTypes);

        //AND DON'T FORGET THE META DOWN HERE
        // V V V V V V V V

        //null
        metaOrder.add(NONE);

        if (idMapping.size() != metaOrder.size()) {
            throw new IllegalStateException("A severe error has occoured during NTM's fluid registering process! The MetaOrder and Mappings are inconsistent! Mapping size: " + idMapping.size()+ " / MetaOrder size: " + metaOrder.size());
        }
    }

    private static void initDefaultFluids(File file) {

        try {
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("  ");
            writer.beginObject();

            writer.name("CUSTOM_DEMO").beginObject();
            writer.name("name").value("Custom Fluid Demo");
            writer.name("id").value(1000);
            writer.name("color").value(0xff0000);
            writer.name("tint").value(0xff0000);
            writer.name("p").value(1).name("f").value(2).name("r").value(0);
            writer.name("symbol").value(EnumSymbol.OXIDIZER.name());
            writer.name("texture").value("custom_water");
            writer.name("temperature").value(20);
            writer.endObject();

            writer.endObject();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readCustomFluids(File file) {

        try {
            JsonObject json = gson.fromJson(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8), JsonObject.class);

            for (Entry<String, JsonElement> entry : json.entrySet()) {

                JsonObject obj = (JsonObject) entry.getValue();

                String name = entry.getKey();
                int id = obj.get("id").getAsInt();
                String displayName = obj.get("name").getAsString();
                int color = obj.get("color").getAsInt();
                int tint = obj.get("tint").getAsInt();
                int p = obj.get("p").getAsInt();
                int f = obj.get("f").getAsInt();
                int r = obj.get("r").getAsInt();
                EnumSymbol symbol = EnumSymbol.valueOf(obj.get("symbol").getAsString());
                String texture = obj.get("texture").getAsString();
                int temperature = obj.get("temperature").getAsInt();

                FluidType type = fluidMigration.get(name);
                if (type == null) type = new FluidType(name, color, p, f, r, symbol, texture, tint, id, displayName).setTemp(temperature);
                else type.setupCustom(name, color, p, f, r, symbol, texture, tint, id, displayName).setTemp(temperature);
                customFluids.add(type);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void writeDefaultTraits(File file) {

        try {
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("  ");
            writer.beginObject();

            for (FluidType type : metaOrder) {
                writer.name(type.getName()).beginObject();

                for (Entry<Class<? extends FluidTrait>, FluidTrait> entry : type.traits.entrySet()) {
                    writer.name(FluidTrait.traitNameMap.inverse().get(entry.getKey())).beginObject();
                    entry.getValue().serializeJSON(writer);
                    writer.endObject();
                }

                writer.endObject();
            }

            writer.endObject();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readTraits(File config) {

        try {
            JsonObject json = gson.fromJson(new FileReader(config), JsonObject.class);

            for (FluidType type : metaOrder) {

                JsonElement element = json.get(type.getName());
                if (element != null) {
                    type.traits.clear();
                    JsonObject obj = element.getAsJsonObject();

                    for (Entry<String, JsonElement> entry : obj.entrySet()) {
                        Class<? extends FluidTrait> traitClass = FluidTrait.traitNameMap.get(entry.getKey());
                        try {
                            FluidTrait trait = traitClass.newInstance();
                            trait.deserializeJSON(entry.getValue().getAsJsonObject());
                            type.addTraits(trait);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static HashMap<String, FluidType> fluidMigration = new HashMap<>(); // since reloading would create new fluid instances, and those break existing machines

    public static void reloadFluids() {
        File folder = CommonEvents.configHbmDir;
        File customTypes = new File(folder.getAbsolutePath() + File.separatorChar + "hbmFluidTypes.json");
        if (!customTypes.exists()) initDefaultFluids(customTypes);

        for (FluidType type : customFluids) {
            fluidMigration.put(type.getName(), type);
            idMapping.remove(type.getID());
            registerOrder.remove(type);
            nameMapping.remove(type.getName());
            metaOrder.remove(type);
        }
        customFluids.clear();

        for (FluidType type : foreignFluids) {
            fluidMigration.put(type.getName(), type);
            idMapping.remove(type.getID());
            registerOrder.remove(type);
            nameMapping.remove(type.getName());
            metaOrder.remove(type);
        }
        foreignFluids.clear();

        readCustomFluids(customTypes);
        metaOrder.addAll(customFluids);
        File config = new File(CommonEvents.configHbmDir.getAbsolutePath() + File.separatorChar + "hbmFluidTraits.json");
        File template = new File(CommonEvents.configHbmDir.getAbsolutePath() + File.separatorChar + "_hbmFluidTraits.json");

        if (!config.exists()) {
            writeDefaultTraits(template);
        } else {
            readTraits(config);
        }

        for (IFluidRegisterListener listener : additionalListeners) listener.onFluidsLoad();
    }

    protected static int registerSelf(FluidType fluid) {
        int id = idMapping.size();
        idMapping.put(id, fluid);
        registerOrder.add(fluid);
        nameMapping.put(fluid.getName(), fluid);
        return id;
    }

    protected static void register(FluidType fluid, int id) {
        idMapping.put(id, fluid);
        registerOrder.add(fluid);
        nameMapping.put(fluid.getName(), fluid);
    }

    public static FluidType fromID(int id) {
        FluidType fluid = idMapping.get(id);

        if (fluid == null) fluid = Fluids.NONE;

        return fluid;
    }

    public static FluidType fromName(String name) {
        FluidType fluid = nameMapping.get(name);

        if (fluid == null) fluid = Fluids.NONE;

        return fluid;
    }

    /** for old worlds with types saved as name, do not use otherwise */
    public static FluidType fromNameCompat(String name) {
        if (renameMapping.containsKey(name)) {
            FluidType fluid = renameMapping.get(name);

            // null safety never killed nobody
            if (fluid == null) fluid = Fluids.NONE;

            return fluid;
        }

        return fromName(name);
    }

    /** basically the inverse of the above method */
    public static String toNameCompat(FluidType type) {
        if (renameMapping.containsValue(type)) {
            String name = renameMapping.inverse().get(type);

            //ditto
            if (name == null) name = Fluids.NONE.getName();

            return name;
        }

        return type.getName();
    }

    public static FluidType[] getAll() {
        return getInOrder(false);
    }

    public static FluidType[] getInNiceOrder() {
        return getInOrder(true);
    }

    private static FluidType[] getInOrder(final boolean nice) {
        FluidType[] all = new FluidType[idMapping.size()];

        for (int i = 0; i < all.length; i++) {
            FluidType type = nice ? metaOrder.get(i) : registerOrder.get(i);

            if (type == null) {
                throw new IllegalStateException("A severe error has occoured with NTM's fluid system! Fluid of the ID " + i + " has returned NULL in the registry!");
            }

            all[i] = type;
        }

        return all;
    }

    public static class CD_Canister {
        public int color;
        public CD_Canister(int color) { this.color = color; }
    }

    public static class CD_Gastank {
        public int bottleColor, labelColor;
        public CD_Gastank(int bottle, int label) { this.bottleColor = bottle; this.labelColor = label; }
    }
}
