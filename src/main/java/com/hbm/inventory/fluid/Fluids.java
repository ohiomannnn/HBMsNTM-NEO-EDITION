package com.hbm.inventory.fluid;

import api.hbm.fluidmk2.IFluidRegisterListener;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.fluid.trait.FT_Corrosive;
import com.hbm.inventory.fluid.trait.FT_Flammable;
import com.hbm.inventory.fluid.trait.FT_Heatable;
import com.hbm.inventory.fluid.trait.FT_Coolable;
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
    public static FluidType AIRBLAST;
    public static FluidType WATER;
    public static FluidType STEAM;
    public static FluidType HOTSTEAM;
    public static FluidType SUPERHOTSTEAM;
    public static FluidType ULTRAHOTSTEAM;
    public static FluidType COOLANT;
    public static FluidType COOLANT_HOT;
    public static FluidType HELIUM_3;
    public static FluidType HELIUM_4;
    public static FluidType LAVA;
    public static FluidType DEUTERIUM;
    public static FluidType TRITIUM;
    public static FluidType KEROSENE;
    public static FluidType GAS;
    public static FluidType AROMATICS;			//anything from benzene to phenol and toluene
    public static FluidType UNSATURATEDS;		//collection of various basic unsaturated compounds like ethylene, acetylene and whatnot
    public static FluidType BIOGAS;
    public static FluidType BIOFUEL;
    public static FluidType NITAN;
    public static FluidType DIESEL;
    public static FluidType DIESEL_CRACK;
    public static FluidType DIESEL_REFORM;
    public static FluidType DIESEL_CRACK_REFORM;
    public static FluidType GASOLINE;
    public static FluidType GASOLINE_LEADED;
    public static FluidType COALGAS;
    public static FluidType COALGAS_LEADED;
    public static FluidType LPG;
    public static FluidType REFORMATE;
    public static FluidType REFORMGAS;
    public static FluidType SOURGAS;
    public static FluidType XYLENE;
    public static FluidType GAS_COKER;
    public static FluidType PEROXIDE;
    public static FluidType SULFURIC_ACID;
    public static FluidType NITRIC_ACID;
    public static FluidType SCHRABIDIC;
    public static FluidType SOLVENT;
    public static FluidType PERFLUOROMETHYL;
    public static FluidType COLD_PERFLUOROMETHYL;
    public static FluidType PERFLUOROMETHYL_HOT;
    public static FluidType OXYGEN;
    public static FluidType HYDROGEN;
    public static FluidType XENON;
    public static FluidType CARBONDIOXIDE;
    public static FluidType UF6;
    public static FluidType PUF6;
    public static FluidType SAS3;
    public static FluidType AMAT;
    public static FluidType ASCHRAB;
    public static FluidType WATZ;
    public static FluidType CRYOGEL;
    public static FluidType MERCURY;
    public static FluidType PAIN;
    public static FluidType WASTEFLUID;
    public static FluidType WASTEGAS;
    public static FluidType FRACKSOL;
    public static FluidType PLASMA_DT;
    public static FluidType PLASMA_HD;
    public static FluidType PLASMA_HT;
    public static FluidType PLASMA_DH3;
    public static FluidType PLASMA_XM;
    public static FluidType PLASMA_BF;
    public static FluidType DEATH;
    public static FluidType HEAVYWATER;
    public static FluidType HEAVYWATER_HOT;
    public static FluidType SALIENT;
    public static FluidType XPJUICE;
    public static FluidType ENDERJUICE;
    public static FluidType MUG;
    public static FluidType MUG_HOT;
    public static FluidType COALCREOSOTE;
    public static FluidType SEEDSLURRY;
    public static FluidType BLOOD;
    public static FluidType BLOOD_HOT;
    public static FluidType PHEROMONE;
    public static FluidType PHEROMONE_M;
    public static FluidType SYNGAS;
    public static FluidType OXYHYDROGEN;
    public static FluidType RADIOSOLVENT;
    public static FluidType HYDRAZINE;
    public static FluidType CHLORINE;
    public static FluidType COLLOID;
    public static FluidType PHOSGENE;
    public static FluidType MUSTARDGAS;
    public static FluidType IONGEL;
    public static FluidType EGG;
    public static FluidType CHOLESTEROL;
    public static FluidType ESTRADIOL;
    public static FluidType FISHOIL;
    public static FluidType SUNFLOWEROIL;
    public static FluidType NITROGLYCERIN;
    public static FluidType REDMUD;
    public static FluidType CHLOROCALCITE_SOLUTION;
    public static FluidType CHLOROCALCITE_MIX;
    public static FluidType CHLOROCALCITE_CLEANED;
    public static FluidType POTASSIUM_CHLORIDE;
    public static FluidType CALCIUM_CHLORIDE;
    public static FluidType CALCIUM_SOLUTION;
    public static FluidType SMOKE;
    public static FluidType SMOKE_LEADED;
    public static FluidType SMOKE_POISON;
    public static FluidType SODIUM;
    public static FluidType SODIUM_HOT;
    public static FluidType LEAD;
    public static FluidType LEAD_HOT;
    public static FluidType THORIUM_SALT;
    public static FluidType THORIUM_SALT_HOT;
    public static FluidType THORIUM_SALT_DEPLETED;
    public static FluidType FULLERENE;
    public static FluidType STELLAR_FLUX;
    public static FluidType VITRIOL;
    public static FluidType SLOP;
    public static FluidType LYE;
    public static FluidType SODIUM_ALUMINATE;
    public static FluidType BAUXITE_SOLUTION;
    public static FluidType ALUMINA;
    public static FluidType CONCRETE;
    public static FluidType DHC;
    public static FluidType ACID;
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
    public static FluidType PETROLEUM;
    public static FluidType OIL_INDUSTRIAL;
    public static FluidType OIL_INDUSTRIAL_RECLAIMED;
    public static FluidType BITUMEN;
    public static FluidType LUBRICANT;
    public static FluidType OIL_HEATING;
    public static FluidType OIL_HEATING_HEAVY;
    public static FluidType WOODOIL;
    public static FluidType PETROIL;
    public static FluidType PETROIL_LEADED;

    public static FluidType BALEFIRE;
    public static FluidType SPENTSTEAM;
    public static FluidType ETHANOL;
    public static FluidType KEROSENE_REFORM;
    public static FluidType FLUE;

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
        AIRBLAST =				new FluidType("AIRBLAST",			    0xFFE95A, 0, 3, 0, EnumSymbol.NONE).setTemp(1200).addTraits(GASEOUS);
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
        PETROLEUM =			    new FluidType("PETROLEUM",	    	0x7cb7c9, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        OIL_INDUSTRIAL =		new FluidType("OIL_INDUSTRIAL",	    0x020202, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_INDUSTRIAL_RECLAIMED =	new FluidType("OIL_INDUSTRIAL_RECLAIMED",	0x332b22, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        BITUMEN =               new FluidType("BITUMEN",               0x141312, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        LUBRICANT =             new FluidType("LUBRICANT",             0xB48B39, 2, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        OIL_HEATING =		    new FluidType("OIL_HEATING",	        0x332b22, 4, 0, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(150_000), LIQUID, VISCOUS);
        OIL_HEATING_HEAVY =		new FluidType("OIL_HEATING_HEAVY",	0x211D06, 4, 0, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(50_000), LIQUID, VISCOUS);
        WOODOIL =               new FluidType("WOODOIL",              0x7D4A1F, 1, 3, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(100_000), LIQUID, VISCOUS);
        PETROIL =		        new FluidType("PETROIL",	            0x44413d, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        PETROIL_LEADED =		new FluidType("PETROIL_LEADED",	    0x44413d, 4, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);

        PERFLUOROMETHYL =		new FluidType("PERFLUOROMETHYL",	    0xBDC8DC, 4, 0, 0, EnumSymbol.NONE).setTemp(15).addTraits(LIQUID, VISCOUS);
        COLD_PERFLUOROMETHYL =	new FluidType("COLD_PERFLUOROMETHYL",	0x99DADE, 4, 0, 0, EnumSymbol.NONE).setTemp(-150).addTraits(LIQUID, VISCOUS);
        ETHANOL =				new FluidType("ETHANOL",			    0xe0ffff, 2, 3, 0, EnumSymbol.NONE).addContainers(new CD_Canister(0xEAFFF3)).addTraits(new FT_Flammable(75_000),/*new FT_Combustible(FuelGrade.HIGH, 200_000), LIQUID, P_FUEL*/ LIQUID);
        KEROSENE_REFORM =		new FluidType("KEROSENE_REFORM",	    0xFFA5F3, 1, 2, 0, EnumSymbol.NONE).addTraits(LIQUID/*, P_FUEL*/).addContainers(new CD_Canister(0xFF377D));
        FLUE =					new FluidType("FLUE",				    0x131313, 1, 4, 1, EnumSymbol.NONE).addTraits(GASEOUS);

        COOLANT =               new FluidType("COOLANT",               14220543, 1, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        COOLANT_HOT =           new FluidType("COOLANT_HOT",           10048094, 1, 0, 0, EnumSymbol.NONE).setTemp(600).addTraits(LIQUID);
        DEUTERIUM =             new FluidType("DEUTERIUM",             0xFFFFC9, 2, 0, 0, EnumSymbol.NONE).addTraits(GASEOUS);
        TRITIUM =               new FluidType("TRITIUM",               0xD6FFD6, 2, 0, 0, EnumSymbol.NONE).addTraits(GASEOUS);
        BIOGAS =                new FluidType("BIOGAS",                12571516, 1, 4, 1, EnumSymbol.NONE).addTraits(new FT_Flammable(25_000), GASEOUS);
        BIOFUEL =               new FluidType("BIOFUEL",               15659636, 1, 2, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(150_000), LIQUID);
        NITAN =                 new FluidType("NITAN",                 8394925, 2, 4, 1, EnumSymbol.NONE).addTraits(new FT_Flammable(2_000_000), LIQUID);
        DIESEL =                new FluidType("DIESEL",                15920853, 1, 2, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(125_000), LIQUID);
        DIESEL_CRACK =          new FluidType("DIESEL_CRACK",          15920853, 1, 2, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(125_000), LIQUID);
        DIESEL_REFORM =         new FluidType("DIESEL_REFORM",         13484998, 1, 2, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(125_000), LIQUID);
        DIESEL_CRACK_REFORM =   new FluidType("DIESEL_CRACK_REFORM",   13485004, 1, 2, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(125_000), LIQUID);
        GASOLINE =              new FluidType("GASOLINE",              4478834, 1, 2, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(400_000), LIQUID);
        GASOLINE_LEADED =       new FluidType("GASOLINE_LEADED",       4478834, 1, 2, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(400_000), LIQUID);
        COALGAS =               new FluidType("COALGAS",               4478834, 1, 2, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(400_000), LIQUID);
        COALGAS_LEADED =        new FluidType("COALGAS_LEADED",        4478834, 1, 2, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(400_000), LIQUID);
        LPG =                   new FluidType("LPG",                   4671466, 1, 3, 1, EnumSymbol.NONE).addTraits(new FT_Flammable(200_000), LIQUID);
        REFORMATE =             new FluidType("REFORMATE",             8606834, 2, 2, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(200_000), LIQUID, VISCOUS);
        REFORMGAS =             new FluidType("REFORMGAS",             6513326, 1, 4, 1, EnumSymbol.NONE).addTraits(new FT_Flammable(25_000), GASEOUS);
        SOURGAS =               new FluidType("SOURGAS",               13221389, 4, 4, 0, EnumSymbol.ACID).addTraits(GASEOUS);
        XYLENE =                new FluidType("XYLENE",                6049398, 2, 3, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(200_000), LIQUID, VISCOUS);
        GAS_COKER =             new FluidType("GAS_COKER",             14611658, 1, 4, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(25_000), GASEOUS);
        SCHRABIDIC =            new FluidType("SCHRABIDIC",            27499, 5, 0, 5, EnumSymbol.ACID).addTraits(LIQUID);
        PERFLUOROMETHYL_HOT =   new FluidType("PERFLUOROMETHYL_HOT",   12098014, 1, 0, 1, EnumSymbol.NONE).setTemp(250).addTraits(LIQUID);
        HYDROGEN =              new FluidType("HYDROGEN",              4359924, 3, 4, 0, EnumSymbol.CROYGENIC).setTemp(-260).addContainers(new CD_Gastank(0x428c74, 0xffffff)).addTraits(LIQUID, EVAP);
        XENON =                 new FluidType("XENON",                 12207592, 0, 0, 0, EnumSymbol.ASPHYXIANT).addTraits(GASEOUS);
        CARBONDIOXIDE =         new FluidType("CARBONDIOXIDE",         4210752, 3, 0, 0, EnumSymbol.ASPHYXIANT).addTraits(GASEOUS);
        UF6 =                   new FluidType("UF6",                   13749950, 4, 0, 2, EnumSymbol.RADIATION).addTraits(GASEOUS);
        PUF6 =                  new FluidType("PUF6",                  5000268, 4, 0, 4, EnumSymbol.RADIATION).addTraits(GASEOUS);
        SAS3 =                  new FluidType("SAS3",                  5242876, 5, 0, 4, EnumSymbol.RADIATION).addTraits(LIQUID);
        AMAT =                  new FluidType("AMAT",                  65793, 5, 0, 5, EnumSymbol.ANTIMATTER).addTraits(ANTI, GASEOUS);
        ASCHRAB =               new FluidType("ASCHRAB",               11862016, 5, 0, 5, EnumSymbol.ANTIMATTER).addTraits(ANTI, GASEOUS);
        WATZ =                  new FluidType("WATZ",                  8807742, 4, 0, 3, EnumSymbol.ACID).addTraits(LIQUID, VISCOUS);
        CRYOGEL =               new FluidType("CRYOGEL",               3342335, 2, 0, 0, EnumSymbol.CROYGENIC).setTemp(-170).addTraits(LIQUID, VISCOUS);
        MERCURY =               new FluidType("MERCURY",               8421504, 2, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        PAIN =                  new FluidType("PAIN",                  9667905, 2, 0, 1, EnumSymbol.ACID).setTemp(300).addTraits(LIQUID, VISCOUS);
        WASTEFLUID =            new FluidType("WASTEFLUID",            5522432, 2, 0, 1, EnumSymbol.RADIATION).addTraits(LIQUID, NOCON, VISCOUS);
        WASTEGAS =              new FluidType("WASTEGAS",              12105912, 2, 0, 1, EnumSymbol.RADIATION).addTraits(GASEOUS, NOCON);
        FRACKSOL =              new FluidType("FRACKSOL",              7965291, 1, 3, 3, EnumSymbol.ACID).addTraits(LIQUID, VISCOUS);
        PLASMA_DT =             new FluidType("PLASMA_DT",             16232414, 0, 4, 0, EnumSymbol.RADIATION).setTemp(3250).addTraits(NOCON, NOID, PLASMA);
        PLASMA_HD =             new FluidType("PLASMA_HD",             15773172, 0, 4, 0, EnumSymbol.RADIATION).setTemp(2500).addTraits(NOCON, NOID, PLASMA);
        PLASMA_HT =             new FluidType("PLASMA_HT",             13741042, 0, 4, 0, EnumSymbol.RADIATION).setTemp(3000).addTraits(NOCON, NOID, PLASMA);
        PLASMA_DH3 =            new FluidType("PLASMA_DH3",            16745386, 0, 4, 0, EnumSymbol.RADIATION).setTemp(3480).addTraits(NOCON, NOID, PLASMA);
        PLASMA_XM =             new FluidType("PLASMA_XM",             13018623, 0, 4, 1, EnumSymbol.RADIATION).setTemp(4250).addTraits(NOCON, NOID, PLASMA);
        PLASMA_BF =             new FluidType("PLASMA_BF",             11006371, 4, 5, 4, EnumSymbol.ANTIMATTER).setTemp(8500).addTraits(NOCON, NOID, PLASMA);
        DEATH =                 new FluidType("DEATH",                 7436936, 2, 0, 1, EnumSymbol.ACID).setTemp(300).addTraits(LEADCON, LIQUID, VISCOUS);
        HEAVYWATER =            new FluidType("HEAVYWATER",            41136, 1, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        HEAVYWATER_HOT =        new FluidType("HEAVYWATER_HOT",        5046395, 1, 0, 0, EnumSymbol.NONE).setTemp(600).addTraits(LIQUID, VISCOUS);
        SALIENT =               new FluidType("SALIENT",               4554541, 0, 0, 0, EnumSymbol.NONE).addTraits(DELICIOUS, LIQUID, VISCOUS);
        XPJUICE =               new FluidType("XPJUICE",               12320521, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        ENDERJUICE =            new FluidType("ENDERJUICE",            1210214, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        MUG =                   new FluidType("MUG",                   4926760, 0, 0, 0, EnumSymbol.NONE).addTraits(DELICIOUS, LIQUID);
        MUG_HOT =               new FluidType("MUG_HOT",               7023136, 0, 0, 0, EnumSymbol.NONE).setTemp(500).addTraits(DELICIOUS, LIQUID);
        COALCREOSOTE =          new FluidType("COALCREOSOTE",          5335375, 3, 2, 0, EnumSymbol.NONE).addTraits(new FT_Flammable(150_000), LIQUID, VISCOUS);
        SEEDSLURRY =            new FluidType("SEEDSLURRY",            8176478, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        BLOOD =                 new FluidType("BLOOD",                 11674660, 0, 0, 0, EnumSymbol.NONE).addTraits(DELICIOUS, LIQUID, VISCOUS);
        BLOOD_HOT =             new FluidType("BLOOD_HOT",             15868953, 3, 0, 0, EnumSymbol.NONE).setTemp(666).addTraits(LIQUID, VISCOUS);
        PHEROMONE =             new FluidType("PHEROMONE",             6268648, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        PHEROMONE_M =           new FluidType("PHEROMONE_M",           4770224, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        SYNGAS =                new FluidType("SYNGAS",                1250067, 1, 4, 2, EnumSymbol.NONE).addTraits(new FT_Flammable(25_000), GASEOUS);
        OXYHYDROGEN =           new FluidType("OXYHYDROGEN",           4734913, 0, 4, 2, EnumSymbol.NONE).addTraits(new FT_Flammable(1_000_000), GASEOUS);
        RADIOSOLVENT =          new FluidType("RADIOSOLVENT",          10803165, 3, 3, 0, EnumSymbol.NONE).addTraits(LIQUID);
        HYDRAZINE =             new FluidType("HYDRAZINE",             3232125, 2, 3, 2, EnumSymbol.NONE).addTraits(new FT_Flammable(750_000), LIQUID);
        CHLORINE =              new FluidType("CHLORINE",              12236146, 3, 0, 0, EnumSymbol.OXIDIZER).addTraits(GASEOUS);
        COLLOID =               new FluidType("COLLOID",               7895160, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        PHOSGENE =              new FluidType("PHOSGENE",              13616292, 4, 0, 1, EnumSymbol.NONE).addTraits(GASEOUS);
        MUSTARDGAS =            new FluidType("MUSTARDGAS",            12236146, 4, 1, 1, EnumSymbol.NONE).addTraits(GASEOUS);
        IONGEL =                new FluidType("IONGEL",                12124159, 1, 0, 4, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        EGG =                   new FluidType("EGG",                   13812339, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        CHOLESTEROL =           new FluidType("CHOLESTEROL",           14078653, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        ESTRADIOL =             new FluidType("ESTRADIOL",             13489624, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        FISHOIL =               new FluidType("FISHOIL",               4934213, 0, 1, 0, EnumSymbol.NONE).addTraits(LIQUID);
        SUNFLOWEROIL =          new FluidType("SUNFLOWEROIL",          13348165, 0, 1, 0, EnumSymbol.NONE).addTraits(LIQUID);
        NITROGLYCERIN =         new FluidType("NITROGLYCERIN",         9612454, 0, 4, 0, EnumSymbol.NONE).addTraits(LIQUID);
        REDMUD =                new FluidType("REDMUD",                14177848, 3, 0, 4, EnumSymbol.NONE).addTraits(LEADCON, LIQUID, VISCOUS);
        CHLOROCALCITE_SOLUTION = new FluidType("CHLOROCALCITE_SOLUTION", 8421504, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, NOCON);
        CHLOROCALCITE_MIX =     new FluidType("CHLOROCALCITE_MIX",     8421504, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, NOCON);
        CHLOROCALCITE_CLEANED = new FluidType("CHLOROCALCITE_CLEANED", 8421504, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, NOCON);
        POTASSIUM_CHLORIDE =    new FluidType("POTASSIUM_CHLORIDE",    8421504, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, NOCON);
        CALCIUM_CHLORIDE =      new FluidType("CALCIUM_CHLORIDE",      8421504, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, NOCON);
        CALCIUM_SOLUTION =      new FluidType("CALCIUM_SOLUTION",      8421504, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, NOCON);
        SMOKE =                 new FluidType("SMOKE",                 8421504, 0, 0, 0, EnumSymbol.NONE).addTraits(GASEOUS, NOCON, NOID);
        SMOKE_LEADED =          new FluidType("SMOKE_LEADED",          8421504, 0, 0, 0, EnumSymbol.NONE).addTraits(GASEOUS, NOCON, NOID);
        SMOKE_POISON =          new FluidType("SMOKE_POISON",          8421504, 0, 0, 0, EnumSymbol.NONE).addTraits(GASEOUS, NOCON, NOID);
        SODIUM =                new FluidType("SODIUM",                13423829, 1, 2, 3, EnumSymbol.NONE).setTemp(400).addTraits(LIQUID, VISCOUS);
        SODIUM_HOT =            new FluidType("SODIUM_HOT",            14855617, 1, 2, 3, EnumSymbol.NONE).setTemp(1200).addTraits(LIQUID, VISCOUS);
        LEAD =                  new FluidType("LEAD",                  6710898, 4, 0, 0, EnumSymbol.NONE).setTemp(350).addTraits(LIQUID, VISCOUS);
        LEAD_HOT =              new FluidType("LEAD_HOT",              7824739, 4, 0, 0, EnumSymbol.NONE).setTemp(1500).addTraits(LIQUID, VISCOUS);
        THORIUM_SALT =          new FluidType("THORIUM_SALT",          8017218, 2, 0, 3, EnumSymbol.NONE).setTemp(800).addTraits(LIQUID, VISCOUS);
        THORIUM_SALT_HOT =      new FluidType("THORIUM_SALT_HOT",      4077095, 2, 0, 3, EnumSymbol.NONE).setTemp(1600).addTraits(LIQUID, VISCOUS);
        THORIUM_SALT_DEPLETED = new FluidType("THORIUM_SALT_DEPLETED", 3157276, 2, 0, 3, EnumSymbol.NONE).setTemp(800).addTraits(LIQUID, VISCOUS);
        FULLERENE =             new FluidType("FULLERENE",             16744429, 3, 3, 3, EnumSymbol.NONE).addTraits(LIQUID);
        STELLAR_FLUX =          new FluidType("STELLAR_FLUX",          14876927, 0, 4, 4, EnumSymbol.ANTIMATTER).addTraits(ANTI, GASEOUS);
        VITRIOL =               new FluidType("VITRIOL",               7229986, 2, 0, 1, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        SLOP =                  new FluidType("SLOP",                  9608517, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID, VISCOUS);
        LYE =                   new FluidType("LYE",                   16772300, 3, 0, 1, EnumSymbol.ACID).addTraits(LIQUID);
        SODIUM_ALUMINATE =      new FluidType("SODIUM_ALUMINATE",      16765329, 3, 0, 1, EnumSymbol.ACID).addTraits(LIQUID);
        BAUXITE_SOLUTION =      new FluidType("BAUXITE_SOLUTION",      14833167, 3, 0, 3, EnumSymbol.ACID).addTraits(LIQUID, VISCOUS);
        ALUMINA =               new FluidType("ALUMINA",               14548991, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        CONCRETE =              new FluidType("CONCRETE",              10658466, 0, 0, 0, EnumSymbol.NONE).addTraits(LIQUID);
        DHC =                   new FluidType("DHC",                   13807615, 0, 0, 0, EnumSymbol.NONE).addTraits(GASEOUS);
        ACID =                  new FluidType("ACID",                  0x8FAE27, 3, 0, 3, EnumSymbol.ACID).addTraits(LIQUID);
        BALEFIRE =              new FluidType("BALEFIRE",              2678830, 4, 4, 3, EnumSymbol.RADIATION).setTemp(1500).addTraits(new FT_Flammable(1_000_000), LIQUID, VISCOUS);

        AIR.addTraits(new FT_Heatable()
                .setEff(FT_Heatable.HeatingType.BOILER, 1.0D)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, 1.0D)
                .addStep(5, 1, AIRBLAST, 1));

        FT_Heatable boilerWater = new FT_Heatable()
                .setEff(FT_Heatable.HeatingType.BOILER, 1.0D)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, 1.0D)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1);
        WATER.addTraits(boilerWater);

        STEAM.addTraits(new FT_Heatable()
                .setEff(FT_Heatable.HeatingType.BOILER, 1.0D)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, 1.0D)
                .addStep(2, 10, HOTSTEAM, 1));

        HOTSTEAM.addTraits(new FT_Heatable()
                .setEff(FT_Heatable.HeatingType.BOILER, 1.0D)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, 1.0D)
                .addStep(18, 10, SUPERHOTSTEAM, 1));

        SUPERHOTSTEAM.addTraits(new FT_Heatable()
                .setEff(FT_Heatable.HeatingType.BOILER, 1.0D)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, 1.0D)
                .addStep(120, 10, ULTRAHOTSTEAM, 1));

        OIL_CRUDE.addTraits(new FT_Heatable()
                .setEff(FT_Heatable.HeatingType.BOILER, 1.0D)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, 1.0D)
                .addStep(10, 1, OIL_CRUDE_HOT, 1));

        OIL_CRUDE_DESULFURIZED.addTraits(new FT_Heatable()
                .setEff(FT_Heatable.HeatingType.BOILER, 1.0D)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, 1.0D)
                .addStep(10, 1, OIL_CRUDE_DESULFURIZED_HOT, 1));

        OIL_CRUDE_CRACKED.addTraits(new FT_Heatable()
                .setEff(FT_Heatable.HeatingType.BOILER, 1.0D)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, 1.0D)
                .addStep(10, 1, OIL_CRUDE_CRACKED_HOT, 1));

        OIL_CRUDE_DESULFURIZED_CRACKED.addTraits(new FT_Heatable()
                .setEff(FT_Heatable.HeatingType.BOILER, 1.0D)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, 1.0D)
                .addStep(10, 1, OIL_CRUDE_DESULFURIZED_CRACKED_HOT, 1));

        OIL_CRUDE_HOT.addTraits(new FT_Coolable()
                .setEff(FT_Coolable.CoolingType.HEATEXCHANGER, 1.0D)
                .addStep(10, 1, OIL_CRUDE, 1));

        OIL_CRUDE_DESULFURIZED_HOT.addTraits(new FT_Coolable()
                .setEff(FT_Coolable.CoolingType.HEATEXCHANGER, 1.0D)
                .addStep(10, 1, OIL_CRUDE_DESULFURIZED, 1));

        OIL_CRUDE_CRACKED_HOT.addTraits(new FT_Coolable()
                .setEff(FT_Coolable.CoolingType.HEATEXCHANGER, 1.0D)
                .addStep(10, 1, OIL_CRUDE_CRACKED, 1));

        OIL_CRUDE_DESULFURIZED_CRACKED_HOT.addTraits(new FT_Coolable()
                .setEff(FT_Coolable.CoolingType.HEATEXCHANGER, 1.0D)
                .addStep(10, 1, OIL_CRUDE_DESULFURIZED_CRACKED, 1));

        HOTSTEAM.addTraits(new FT_Coolable()
                .setEff(FT_Coolable.CoolingType.HEATEXCHANGER, 1.0D)
                .addStep(2, 10, STEAM, 1));

        SUPERHOTSTEAM.addTraits(new FT_Coolable()
                .setEff(FT_Coolable.CoolingType.HEATEXCHANGER, 1.0D)
                .addStep(18, 10, HOTSTEAM, 1));

        ULTRAHOTSTEAM.addTraits(new FT_Coolable()
                .setEff(FT_Coolable.CoolingType.HEATEXCHANGER, 1.0D)
                .addStep(120, 10, SUPERHOTSTEAM, 1));

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
        metaOrder.add(AIRBLAST);
        metaOrder.add(WATER);
        metaOrder.add(LAVA);
        //steams
        metaOrder.add(STEAM);
        metaOrder.add(HOTSTEAM);
        metaOrder.add(SUPERHOTSTEAM);
        metaOrder.add(ULTRAHOTSTEAM);
        metaOrder.add(SPENTSTEAM);
        metaOrder.add(COOLANT);
        metaOrder.add(COOLANT_HOT);
        //pure elements, cryogenic gasses
        metaOrder.add(OXYGEN);
        metaOrder.add(HYDROGEN);
        metaOrder.add(DEUTERIUM);
        metaOrder.add(TRITIUM);
        metaOrder.add(HELIUM_3);
        metaOrder.add(HELIUM_4);
        metaOrder.add(XENON);
        metaOrder.add(CARBONDIOXIDE);
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
        metaOrder.add(PETROLEUM);
        metaOrder.add(OIL_INDUSTRIAL);
        metaOrder.add(OIL_INDUSTRIAL_RECLAIMED);
        metaOrder.add(BITUMEN);
        metaOrder.add(LUBRICANT);
        metaOrder.add(OIL_HEATING);
        metaOrder.add(OIL_HEATING_HEAVY);
        metaOrder.add(WOODOIL);
        metaOrder.add(FISHOIL);
        metaOrder.add(SUNFLOWEROIL);
        metaOrder.add(PETROIL);
        metaOrder.add(PETROIL_LEADED);
        metaOrder.add(GAS);
        metaOrder.add(AROMATICS);
        metaOrder.add(UNSATURATEDS);
        metaOrder.add(BIOGAS);
        metaOrder.add(BIOFUEL);
        metaOrder.add(NITAN);
        metaOrder.add(DIESEL);
        metaOrder.add(DIESEL_CRACK);
        metaOrder.add(DIESEL_REFORM);
        metaOrder.add(DIESEL_CRACK_REFORM);
        metaOrder.add(GASOLINE);
        metaOrder.add(GASOLINE_LEADED);
        metaOrder.add(COALGAS);
        metaOrder.add(COALGAS_LEADED);
        metaOrder.add(LPG);
        metaOrder.add(REFORMATE);
        metaOrder.add(REFORMGAS);
        metaOrder.add(SOURGAS);
        metaOrder.add(XYLENE);
        metaOrder.add(GAS_COKER);
        metaOrder.add(COALCREOSOTE);
        metaOrder.add(SYNGAS);
        metaOrder.add(OXYHYDROGEN);
        metaOrder.add(HYDRAZINE);
        metaOrder.add(BALEFIRE);
        metaOrder.add(KEROSENE);
        metaOrder.add(KEROSENE_REFORM);
        metaOrder.add(ETHANOL);
        metaOrder.add(FLUE);
        //processing fluids
        metaOrder.add(PEROXIDE);
        metaOrder.add(SULFURIC_ACID);
        metaOrder.add(NITRIC_ACID);
        metaOrder.add(SCHRABIDIC);
        metaOrder.add(SOLVENT);
        metaOrder.add(PERFLUOROMETHYL);
        metaOrder.add(COLD_PERFLUOROMETHYL);
        metaOrder.add(PERFLUOROMETHYL_HOT);
        metaOrder.add(UF6);
        metaOrder.add(PUF6);
        metaOrder.add(SAS3);
        metaOrder.add(AMAT);
        metaOrder.add(ASCHRAB);
        metaOrder.add(WATZ);
        metaOrder.add(CRYOGEL);
        metaOrder.add(MERCURY);
        metaOrder.add(PAIN);
        metaOrder.add(WASTEFLUID);
        metaOrder.add(WASTEGAS);
        metaOrder.add(FRACKSOL);
        metaOrder.add(PLASMA_DT);
        metaOrder.add(PLASMA_HD);
        metaOrder.add(PLASMA_HT);
        metaOrder.add(PLASMA_DH3);
        metaOrder.add(PLASMA_XM);
        metaOrder.add(PLASMA_BF);
        metaOrder.add(DEATH);
        metaOrder.add(HEAVYWATER);
        metaOrder.add(HEAVYWATER_HOT);
        metaOrder.add(SALIENT);
        metaOrder.add(XPJUICE);
        metaOrder.add(ENDERJUICE);
        metaOrder.add(MUG);
        metaOrder.add(MUG_HOT);
        metaOrder.add(SEEDSLURRY);
        metaOrder.add(BLOOD);
        metaOrder.add(BLOOD_HOT);
        metaOrder.add(PHEROMONE);
        metaOrder.add(PHEROMONE_M);
        metaOrder.add(RADIOSOLVENT);
        metaOrder.add(CHLORINE);
        metaOrder.add(COLLOID);
        metaOrder.add(PHOSGENE);
        metaOrder.add(MUSTARDGAS);
        metaOrder.add(IONGEL);
        metaOrder.add(EGG);
        metaOrder.add(CHOLESTEROL);
        metaOrder.add(ESTRADIOL);
        metaOrder.add(NITROGLYCERIN);
        metaOrder.add(REDMUD);
        metaOrder.add(CHLOROCALCITE_SOLUTION);
        metaOrder.add(CHLOROCALCITE_MIX);
        metaOrder.add(CHLOROCALCITE_CLEANED);
        metaOrder.add(POTASSIUM_CHLORIDE);
        metaOrder.add(CALCIUM_CHLORIDE);
        metaOrder.add(CALCIUM_SOLUTION);
        metaOrder.add(SMOKE);
        metaOrder.add(SMOKE_LEADED);
        metaOrder.add(SMOKE_POISON);
        metaOrder.add(SODIUM);
        metaOrder.add(SODIUM_HOT);
        metaOrder.add(LEAD);
        metaOrder.add(LEAD_HOT);
        metaOrder.add(THORIUM_SALT);
        metaOrder.add(THORIUM_SALT_HOT);
        metaOrder.add(THORIUM_SALT_DEPLETED);
        metaOrder.add(FULLERENE);
        metaOrder.add(STELLAR_FLUX);
        metaOrder.add(VITRIOL);
        metaOrder.add(SLOP);
        metaOrder.add(LYE);
        metaOrder.add(SODIUM_ALUMINATE);
        metaOrder.add(BAUXITE_SOLUTION);
        metaOrder.add(ALUMINA);
        metaOrder.add(CONCRETE);
        metaOrder.add(DHC);
        metaOrder.add(ACID);

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
