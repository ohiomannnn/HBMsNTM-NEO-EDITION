package com.hbm.inventory.fluid;

import api.hbm.fluidmk2.IFluidRegisterListener;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.fluid.trait.FT_Corrosive;
import com.hbm.inventory.fluid.trait.FT_Flammable;
import com.hbm.inventory.fluid.trait.FluidTrait;
import com.hbm.inventory.fluid.trait.FluidTraitSimple.*;
import com.hbm.main.NuclearTechMod;
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
    public static FluidType STEAM;
    public static FluidType HOTSTEAM;
    public static FluidType SUPERHOTSTEAM;
    public static FluidType ULTRAHOTSTEAM;
    public static FluidType HELIUM_3;
    public static FluidType HELIUM_4;
    public static FluidType LAVA;
    public static FluidType KEROSENE;
    public static FluidType GAS;
    public static FluidType AROMATICS;			//anything from benzene to phenol and toluene
    public static FluidType UNSATURATEDS;		//collection of various basic unsaturated compounds like ethylene, acetylene and whatnot
    public static FluidType PEROXIDE;
    public static FluidType SULFURIC_ACID;
    public static FluidType NITRIC_ACID;
    public static FluidType SCHRABIDIC_ACID;
    public static FluidType SOLVENT;
    public static FluidType PERFLUOROMETHYL;
    public static FluidType COLD_PERFLUOROMETHYL;
    public static FluidType OXYGEN;
    public static FluidType OIL_CRUDE;
    public static FluidType OIL_CRUDE_CRACKED;
    public static FluidType OIL_CRUDE_DESULFURIZED;
    public static FluidType OIL_CRUDE_DESULFURIZED_CRACKED;
    public static FluidType OIL_CRUDE_HOT;
    public static FluidType OIL_CRUDE_DESULFURIZED_HOT;
    public static FluidType OIL_CRUDE_CRACKED_HOT;
    public static FluidType OIL_CRUDE_DESULFURIZED_CRACKED_HOT;
    public static FluidType OIL_HEAVY;
    public static FluidType OIL_HEAVY_VACUUM;
    public static FluidType OIL_LIGHT;
    public static FluidType OIL_LIGHT_DESULFURIZED;
    public static FluidType OIL_LIGHT_CRACKED;
    public static FluidType OIL_LIGHT_VACUUM;
    public static FluidType NAPHTHA;
    public static FluidType NAPHTHA_DESULFURIZED;
    public static FluidType NAPHTHA_CRACKED;
    public static FluidType NAPHTHA_COKER;
    public static FluidType PETROLEUM_GAS;
    public static FluidType OIL_INDUSTRIAL;
    public static FluidType OIL_INDUSTRIAL_RECLAIMED;
    public static FluidType OIL_HEATING;
    public static FluidType OIL_HEATING_HEAVY;
    public static FluidType PETROIL;
    public static FluidType PETROIL_LEADED;

    public static FluidType BALEFIRE;
    public static FluidType SPENTSTEAM;
    public static FluidType ETHANOL;
    public static FluidType KEROSENE_REFORM;

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

        NONE =					new FluidType("NONE",				    0x888888, 0, 0, 0, EnumSymbol.NONE);
        AIR =					new FluidType("AIR",				    0xE7EAEB, 0, 0, 0, EnumSymbol.NONE).addTraits(GASEOUS);
        WATER =					new FluidType("WATER",			    0x3333FF, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, UNSIPHONABLE);
        STEAM =					new FluidType("STEAM",			    0xe5e5e5, 3, 0, 0, EnumSymbol.NONE).setTemp(100).addTraits(GASEOUS, UNSIPHONABLE);
        HOTSTEAM =				new FluidType("HOTSTEAM",			    0xE7D6D6, 4, 0, 0, EnumSymbol.NONE).setTemp(300).addTraits(GASEOUS, UNSIPHONABLE);
        SUPERHOTSTEAM =			new FluidType("SUPERHOTSTEAM",	    0xE7B7B7, 4, 0, 0, EnumSymbol.NONE).setTemp(450).addTraits(GASEOUS, UNSIPHONABLE);
        ULTRAHOTSTEAM =			new FluidType("ULTRAHOTSTEAM",	    0xE39393, 4, 0, 0, EnumSymbol.NONE).setTemp(600).addTraits(GASEOUS, UNSIPHONABLE);
        SPENTSTEAM =			new FluidType("SPENTSTEAM",		    0x445772, 2, 0, 0, EnumSymbol.NONE).addTraits(NOCON, GASEOUS);
        HELIUM_3 =		    	new FluidType("HELIUM_3",		        0xFCF0C4, 2, 0, 0, EnumSymbol.NONE).addTraits(GASEOUS);
        HELIUM_4 =			    new FluidType("HELIUM_4",		        0xE54B0A, 2, 0, 0, EnumSymbol.NONE).addTraits(GASEOUS);
        LAVA =					new FluidType("LAVA",				    0xFF3300, 4, 0, 0, EnumSymbol.NOWATER).setTemp(1200).addTraits(LIQUID, VISCOUS);
        KEROSENE =				new FluidType("KEROSENE",			    0xffa5d2, 1, 2, 0, EnumSymbol.NONE).addContainers(new CD_Canister(0xFF377D)).addTraits(new FT_Flammable(300_000),/*new FT_Combustible(FuelGrade.AERO, 1_250_000), LIQUID, P_FUEL*/ LIQUID);
        GAS =					new FluidType("GAS",				    0xfffeed, 1, 4, 1, EnumSymbol.NONE).addContainers(new CD_Gastank(0xFF4545, 0xFFE97F)).addTraits(new FT_Flammable(10_000), GASEOUS /*P_GAS*/);
        AROMATICS =				new FluidType("AROMATICS",		    0x68A09A, 1, 4, 1, EnumSymbol.NONE).addContainers(new CD_Gastank(0x68A09A, 0xEDCF27)).addTraits(new FT_Flammable(25_000), LIQUID, VISCOUS /*P_GAS*/);
        UNSATURATEDS =			new FluidType("UNSATURATEDS",		    0x628FAE, 1, 4, 1, EnumSymbol.NONE).addContainers(new CD_Gastank(0x628FAE, 0xEDCF27)).addTraits(new FT_Flammable(1_000_000), GASEOUS /*P_GAS*/); //acetylene burns as hot as satan's asshole
        PEROXIDE =				new FluidType("PEROXIDE",			    0xfff7aa, 3, 0, 3, EnumSymbol.OXIDIZER).addTraits(new FT_Corrosive(40), LIQUID);
        SULFURIC_ACID =			new FluidType("SULFURIC_ACID",	    0xB0AA64, 3, 0, 3, EnumSymbol.OXIDIZER).addTraits(new FT_Corrosive(50), LIQUID);
        NITRIC_ACID =			new FluidType("NITRIC_ACID",		    0xBB7A1E, 3, 0, 3, EnumSymbol.OXIDIZER).addTraits(new FT_Corrosive(60), LIQUID);
        SCHRABIDIC_ACID =		new FluidType("SCHRABIDIC_ACID",	    0x006B6B, 3, 0, 3, EnumSymbol.OXIDIZER).addTraits(new FT_Corrosive(75), LIQUID);
        SOLVENT =				new FluidType("SOLVENT",			    0xE4E3EF, 3, 0, 3, EnumSymbol.OXIDIZER).addTraits(new FT_Corrosive(30), LIQUID);
        OXYGEN =				new FluidType("OXYGEN",			    0x98bdf9, 3, 0, 0, EnumSymbol.CROYGENIC).setTemp(-100).addContainers(new CD_Gastank(0x98bdf9, 0xffffff)).addTraits(LIQUID, EVAP);
        OIL_CRUDE =				new FluidType("OIL_CRUDE",		    0x020202, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_CRUDE_CRACKED =		new FluidType("OIL_CRUDE_CRACKED",    0x020202, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_CRUDE_DESULFURIZED = new FluidType("OIL_CRUDE_DESULFURIZED",	0x121212, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_CRUDE_DESULFURIZED_CRACKED = new FluidType("OIL_CRUDE_DESULFURIZED_CRACKED",	0x2A1C11, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_CRUDE_HOT =				new FluidType("OIL_CRUDE_HOT",		    0x300900, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_CRUDE_DESULFURIZED_HOT = new FluidType("OIL_CRUDE_DESULFURIZED_HOT",	0x3F180F, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_CRUDE_CRACKED_HOT =		new FluidType("OIL_CRUDE_CRACKED_HOT",    0x300900, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_CRUDE_DESULFURIZED_CRACKED_HOT = new FluidType("OIL_CRUDE_DESULFURIZED_CRACKED_HOT",	0x3A1A28, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_HEAVY =				new FluidType("OIL_HEAVY",		    0x141312, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_HEAVY_VACUUM =		new FluidType("OIL_HEAVY_VACUUM",		0x131214, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_LIGHT =				new FluidType("OIL_LIGHT",		    0x8c7451, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        OIL_LIGHT_DESULFURIZED =    new FluidType("OIL_LIGHT_DESULFURIZED",	0x63543E, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        OIL_LIGHT_CRACKED =		new FluidType("OIL_LIGHT_CRACKED",	0x8c7451, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        OIL_LIGHT_VACUUM =		new FluidType("OIL_LIGHT_VACUUM",		0x8C8851, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        NAPHTHA =				new FluidType("NAPHTHA",		        0x595744, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        NAPHTHA_DESULFURIZED =	new FluidType("NAPHTHA_DESULFURIZED", 0x63614E, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        NAPHTHA_CRACKED =		new FluidType("NAPHTHA_CRACKED",		0x595744, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        NAPHTHA_COKER =			new FluidType("NAPHTHA_COKER",		0x495944, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        PETROLEUM_GAS =			new FluidType("PETROLEUM_GAS",		0x7cb7c9, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        OIL_INDUSTRIAL =		new FluidType("OIL_INDUSTRIAL",	    0x020202, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_INDUSTRIAL_RECLAIMED =	new FluidType("OIL_INDUSTRIAL_RECLAIMED",	0x332b22, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_HEATING =		    new FluidType("OIL_HEATING",	        0x332b22, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_HEATING_HEAVY =		new FluidType("OIL_HEATING_HEAVY",	0x211D06, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        PETROIL =		        new FluidType("PETROIL",	            0x44413d, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        PETROIL_LEADED =		new FluidType("PETROIL_LEADED",	    0x44413d, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);

        PERFLUOROMETHYL =		new FluidType("PERFLUOROMETHYL",	    0xBDC8DC, 4, 0, 0, EnumSymbol.NONE).setTemp(15).addTraits(LIQUID, VISCOUS);
        COLD_PERFLUOROMETHYL =	new FluidType("COLD_PERFLUOROMETHYL",	0x99DADE, 4, 0, 0, EnumSymbol.NONE).setTemp(-150).addTraits(LIQUID, VISCOUS);
        ETHANOL =				new FluidType("ETHANOL",			    0xe0ffff, 2, 3, 0, EnumSymbol.NONE).addContainers(new CD_Canister(0xEAFFF3)).addTraits(new FT_Flammable(75_000),/*new FT_Combustible(FuelGrade.HIGH, 200_000), LIQUID, P_FUEL*/ LIQUID);
        KEROSENE_REFORM =		new FluidType("KEROSENE_REFORM",	    0xFFA5F3, 1, 2, 0, EnumSymbol.NONE).addTraits(LIQUID/*, P_FUEL*/).addContainers(new CD_Canister(0xFF377D));

        // ^ ^ ^ ^ ^ ^ ^ ^
        //ADD NEW FLUIDS HERE

        File folder = NuclearTechMod.configHbmDir;
        File customTypes = new File(folder.getAbsolutePath() + File.separatorChar + "hbmFluidTypes.json");
        if(!customTypes.exists()) initDefaultFluids(customTypes);
        readCustomFluids(customTypes);

        for(IFluidRegisterListener listener : additionalListeners) listener.onFluidsLoad();

        //AND DON'T FORGET THE META DOWN HERE
        // V V V V V V V V

        //null
        metaOrder.add(NONE);
        //vanilla
        metaOrder.add(AIR);
        metaOrder.add(WATER);
        metaOrder.add(LAVA);
        //steams
        metaOrder.add(STEAM);
        metaOrder.add(HOTSTEAM);
        metaOrder.add(SUPERHOTSTEAM);
        metaOrder.add(ULTRAHOTSTEAM);
        metaOrder.add(SPENTSTEAM);
        //pure elements, cryogenic gasses
        metaOrder.add(OXYGEN);
        metaOrder.add(HELIUM_3);
        metaOrder.add(HELIUM_4);
        //oils, fuels
        metaOrder.add(OIL_CRUDE);
        metaOrder.add(OIL_CRUDE_CRACKED);
        metaOrder.add(OIL_CRUDE_DESULFURIZED);
        metaOrder.add(OIL_CRUDE_DESULFURIZED_CRACKED);
        metaOrder.add(OIL_CRUDE_HOT);
        metaOrder.add(OIL_CRUDE_DESULFURIZED_HOT);
        metaOrder.add(OIL_CRUDE_CRACKED_HOT);
        metaOrder.add(OIL_CRUDE_DESULFURIZED_CRACKED_HOT);
        metaOrder.add(OIL_HEAVY);
        metaOrder.add(OIL_HEAVY_VACUUM);
        metaOrder.add(OIL_LIGHT);
        metaOrder.add(OIL_LIGHT_DESULFURIZED);
        metaOrder.add(OIL_LIGHT_CRACKED);
        metaOrder.add(OIL_LIGHT_VACUUM);
        metaOrder.add(NAPHTHA);
        metaOrder.add(NAPHTHA_DESULFURIZED);
        metaOrder.add(NAPHTHA_CRACKED);
        metaOrder.add(NAPHTHA_COKER);
        metaOrder.add(PETROLEUM_GAS);
        metaOrder.add(OIL_INDUSTRIAL);
        metaOrder.add(OIL_INDUSTRIAL_RECLAIMED);
        metaOrder.add(OIL_HEATING);
        metaOrder.add(OIL_HEATING_HEAVY);
        metaOrder.add(PETROIL);
        metaOrder.add(PETROIL_LEADED);
        metaOrder.add(GAS);
        metaOrder.add(AROMATICS);
        metaOrder.add(UNSATURATEDS);
        metaOrder.add(KEROSENE);
        metaOrder.add(KEROSENE_REFORM);
        metaOrder.add(ETHANOL);
        //processing fluids
        metaOrder.add(PEROXIDE);
        metaOrder.add(SULFURIC_ACID);
        metaOrder.add(NITRIC_ACID);
        metaOrder.add(SCHRABIDIC_ACID);
        metaOrder.add(SOLVENT);
        metaOrder.add(PERFLUOROMETHYL);
        metaOrder.add(COLD_PERFLUOROMETHYL);

        //do not forget about this thingy
        addMetaOrderIfAbsent(customFluids);
        addMetaOrderIfAbsent(foreignFluids);
        syncMetaOrderWithMappings();

        if(idMapping.size() != metaOrder.size()) {
            throw new IllegalStateException("A severe error has occurred during NTM's fluid registering process! The MetaOrder and Mappings are inconsistent! Mapping stacksize: " + idMapping.size()+ " / MetaOrder stacksize: " + metaOrder.size());
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
        File folder = NuclearTechMod.configHbmDir;
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
        addMetaOrderIfAbsent(customFluids);
        File config = new File(NuclearTechMod.configHbmDir.getAbsolutePath() + File.separatorChar + "hbmFluidTraits.json");
        File template = new File(NuclearTechMod.configHbmDir.getAbsolutePath() + File.separatorChar + "_hbmFluidTraits.json");

        if (!config.exists()) {
            writeDefaultTraits(template);
        } else {
            readTraits(config);
        }

        for (IFluidRegisterListener listener : additionalListeners) listener.onFluidsLoad();
        syncMetaOrderWithMappings();
    }

    private static void addMetaOrderIfAbsent(List<FluidType> fluids) {
        for(FluidType fluid : fluids) {
            if(!metaOrder.contains(fluid)) {
                metaOrder.add(fluid);
            }
        }
    }

    private static void syncMetaOrderWithMappings() {
        for(FluidType fluid : registerOrder) {
            if(!metaOrder.contains(fluid)) {
                metaOrder.add(fluid);
            }
        }
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
