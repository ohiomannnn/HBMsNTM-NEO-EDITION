package com.hbm.inventory.fluid;

import api.hbm.fluidmk2.IFluidRegisterListener;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.HBMsNTM;
import com.hbm.inventory.fluid.trait.FT_Flammable;
import com.hbm.inventory.fluid.trait.FluidTrait;
import com.hbm.inventory.fluid.trait.FluidTraitSimple.*;
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
    public static FluidType AIR;
    public static FluidType WATER;
    public static FluidType LAVA;

    public static FluidType GAS;

    public static FluidType AROMATICS;			//anything from benzene to phenol and toluene
    public static FluidType UNSATURATEDS;		//collection of various basic unsaturated compounds like ethylene, acetylene and whatnot

    public static FluidType OXYGEN;

    public static final HashBiMap<String, FluidType> renameMapping = HashBiMap.create();

    public static List<FluidType> customFluids = new ArrayList<>();
    public static List<FluidType> foreignFluids = new ArrayList<>();

    private static final HashMap<Integer, FluidType> idMapping = new HashMap<>();
    private static final HashMap<String, FluidType> nameMapping = new HashMap<>();
    /** Inconsequential, only actually used when listing all fluids with niceOrder disabled */
    protected static final List<FluidType> registerOrder = new ArrayList<>();
    /** What's used to list fluids with niceOrder enabled */
    public static final List<FluidType> metaOrder = new ArrayList<>();

    public static final FT_Liquid LIQUID = new FT_Liquid();
    public static final FT_Viscous VISCOUS = new FT_Viscous();
    public static final FT_Gaseous_ART EVAP = new FT_Gaseous_ART();
    public static final FT_Gaseous GASEOUS = new FT_Gaseous();
    public static final FT_Plasma PLASMA = new FT_Plasma();
    public static final FT_Amat ANTI = new FT_Amat();
    public static final FT_LeadContainer LEADCON = new FT_LeadContainer();
    public static final FT_NoContainer NOCON = new FT_NoContainer();
    public static final FT_NoID NOID = new FT_NoID();
    public static final FT_Delicious DELICIOUS = new FT_Delicious();
    public static final FT_Unsiphonable UNSIPHONABLE = new FT_Unsiphonable();

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
        AIR =					new FluidType("AIR",				0xE7EAEB, 0, 0, 0, EnumSymbol.NONE).addTraits(GASEOUS);
        WATER =					new FluidType("WATER",			0x3333FF, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, UNSIPHONABLE);
        LAVA =					new FluidType("LAVA",				0xFF3300, 4, 0, 0, EnumSymbol.NOWATER).setTemp(1200).addTraits(LIQUID, VISCOUS);

        GAS =					new FluidType("GAS",				0xfffeed, 1, 4, 1, EnumSymbol.NONE).addContainers(new CD_Gastank(0xFF4545, 0xFFE97F)).addTraits(new FT_Flammable(10_000), GASEOUS /*P_GAS*/);

        AROMATICS =				new FluidType("AROMATICS",		0x68A09A, 1, 4, 1, EnumSymbol.NONE).addContainers(new CD_Gastank(0x68A09A, 0xEDCF27)).addTraits(new FT_Flammable(25_000), LIQUID, VISCOUS /*P_GAS*/);
        UNSATURATEDS =			new FluidType("UNSATURATEDS",		0x628FAE, 1, 4, 1, EnumSymbol.NONE).addContainers(new CD_Gastank(0x628FAE, 0xEDCF27)).addTraits(new FT_Flammable(1_000_000), GASEOUS /*P_GAS*/); //acetylene burns as hot as satan's asshole

        OXYGEN =				new FluidType("OXYGEN",			0x98bdf9, 3, 0, 0, EnumSymbol.CROYGENIC).setTemp(-100).addContainers(new CD_Gastank(0x98bdf9, 0xffffff)).addTraits(LIQUID, EVAP);

        // ^ ^ ^ ^ ^ ^ ^ ^
        //ADD NEW FLUIDS HERE

        File folder = HBMsNTM.configHbmDir;
        File customTypes = new File(folder.getAbsolutePath() + File.separatorChar + "hbmFluidTypes.json");
        if (!customTypes.exists()) initDefaultFluids(customTypes);
        readCustomFluids(customTypes);

        for (IFluidRegisterListener listener : additionalListeners) listener.onFluidsLoad();

        //AND DON'T FORGET THE META DOWN HERE
        // V V V V V V V V

        //null
        metaOrder.add(NONE);
        //vanilla
        metaOrder.add(AIR);
        metaOrder.add(WATER);
        metaOrder.add(LAVA);
        //oils, fuels
        metaOrder.add(GAS);

        metaOrder.add(AROMATICS);
        metaOrder.add(UNSATURATEDS);
        //pure elements, cyogenic gasses
        metaOrder.add(OXYGEN);

        //do not forget about this thingy
        metaOrder.addAll(customFluids);
        metaOrder.addAll(foreignFluids);

        if (idMapping.size() != metaOrder.size()) {
            throw new IllegalStateException("A severe error has occurred during NTM's fluid registering process! The MetaOrder and Mappings are inconsistent! Mapping size: " + idMapping.size()+ " / MetaOrder size: " + metaOrder.size());
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
                writer.name(type.getInternalName()).beginObject();

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

                JsonElement element = json.get(type.getInternalName());
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
        File folder = HBMsNTM.configHbmDir;
        File customTypes = new File(folder.getAbsolutePath() + File.separatorChar + "hbmFluidTypes.json");
        if (!customTypes.exists()) initDefaultFluids(customTypes);

        for (FluidType type : customFluids) {
            fluidMigration.put(type.getInternalName(), type);
            idMapping.remove(type.getID());
            registerOrder.remove(type);
            nameMapping.remove(type.getInternalName());
            metaOrder.remove(type);
        }
        customFluids.clear();

        for (FluidType type : foreignFluids) {
            fluidMigration.put(type.getInternalName(), type);
            idMapping.remove(type.getID());
            registerOrder.remove(type);
            nameMapping.remove(type.getInternalName());
            metaOrder.remove(type);
        }
        foreignFluids.clear();

        readCustomFluids(customTypes);
        metaOrder.addAll(customFluids);
        File config = new File(HBMsNTM.configHbmDir.getAbsolutePath() + File.separatorChar + "hbmFluidTraits.json");
        File template = new File(HBMsNTM.configHbmDir.getAbsolutePath() + File.separatorChar + "_hbmFluidTraits.json");

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
        nameMapping.put(fluid.getInternalName(), fluid);
        return id;
    }

    protected static void register(FluidType fluid, int id) {
        idMapping.put(id, fluid);
        registerOrder.add(fluid);
        nameMapping.put(fluid.getInternalName(), fluid);
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
            if (name == null) name = Fluids.NONE.getInternalName();

            return name;
        }

        return type.getInternalName();
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
