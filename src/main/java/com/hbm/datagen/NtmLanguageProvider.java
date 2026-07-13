package com.hbm.datagen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.generic.BarbedWireBlock.BarbedWireType;
import com.hbm.blocks.generic.OreBasaltBlock.BasaltOreType;
import com.hbm.blocks.generic.PlushieBlock.PlushieType;
import com.hbm.fluids.NtmFluidTypes;
import com.hbm.items.ItemEnums.CapType;
import com.hbm.items.NtmItems;
import com.hbm.items.food.ConserveItem.ConserveType;
import com.hbm.items.food.DrinkItem.DrinkType;
import com.hbm.items.machine.BatteryPackItem.BatteryPackType;
import com.hbm.items.machine.BatterySCItem.BatterySCType;
import com.hbm.items.machine.BreedingRodItem.BreedingRodType;
import com.hbm.items.special.StarterKitItem.KitType;
import com.hbm.main.NuclearTechMod;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Locale;

public class NtmLanguageProvider extends LanguageProvider {

    // helper keys
    private static final String DESC = ".desc";
    private static final String P11 = ".p11";

    public NtmLanguageProvider(PackOutput output) {
        super(output, NuclearTechMod.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        // ITEMS //
        this.add(NtmItems.INGOT_URANIUM, "Uranium Ingot");
        this.add(NtmItems.INGOT_U233, "Uranium-233 Ingot");
        this.add(NtmItems.INGOT_U235, "Uranium-235 Ingot");
        this.add(NtmItems.INGOT_U238, "Uranium-238 Ingot");
        this.add(NtmItems.INGOT_U238M2, "Metastable Uranium-238M2 Ingot");
        this.add(NtmItems.INGOT_PLUTONIUM, "Plutonium Ingot");
        this.add(NtmItems.INGOT_PU238, "Plutonium-238 Ingot");
        this.add(NtmItems.INGOT_PU239, "Plutonium-239 Ingot");
        this.add(NtmItems.INGOT_PU240, "Plutonium-240 Ingot");
        this.add(NtmItems.INGOT_PU241, "Plutonium-241 Ingot");
        this.add(NtmItems.INGOT_PU_MIX, "Reactor Grade Plutonium Ingot");
        this.add(NtmItems.INGOT_AM241, "Americium-241 Ingot");
        this.add(NtmItems.INGOT_AM242, "Americium-242 Ingot");
        this.add(NtmItems.INGOT_AM_MIX, "Reactor Grade Americium Ingot");
        this.add(NtmItems.INGOT_NEPTUNIUM, "Neptunium Ingot");
        this.add(NtmItems.INGOT_NEPTUNIUM, DESC,"That one's my favourite!");
        this.add(NtmItems.INGOT_NEPTUNIUM, DESC + P11,"Woo, scary!");
        this.add(NtmItems.INGOT_POLONIUM, "Polonium-210 Ingot");
        this.add(NtmItems.INGOT_TECHNETIUM, "Technetium-99 Ingot");
        this.add(NtmItems.INGOT_CO60, "Cobalt-60 Ingot");
        this.add(NtmItems.INGOT_SR90, "Strontium-90 Ingot");
        this.add(NtmItems.INGOT_AU198, "Gold-198 Ingot");
        this.add(NtmItems.INGOT_PB209, "Lead-209 Ingot");
        this.add(NtmItems.INGOT_RA226, "Radium-226 Ingot");
        this.add(NtmItems.INGOT_TITANIUM, "Titanium Ingot");
        this.add(NtmItems.INGOT_INDUSTRIAL_COPPER, "Industrial Grade Copper Ingot");
        this.add(NtmItems.INGOT_RED_COPPER, "Minecraft Grade Copper Ingot");
        this.add(NtmItems.INGOT_TUNGSTEN, "Tungsten Ingot");
        this.add(NtmItems.INGOT_ALUMINIUM, "Aluminium Ingot");
        this.add(NtmItems.INGOT_STEEL, "Steel Ingot");
        this.add(NtmItems.INGOT_TCALLOY, "Technetium Steel Ingot");
        this.add(NtmItems.INGOT_CDALLOY, "Steel Ingot");
        this.add(NtmItems.INGOT_BISMUTH_BRONZE, "Bismuth Bronze Ingot");
        this.add(NtmItems.INGOT_ARSENIC_BRONZE, "Arsenic Bronze Ingot");
        this.add(NtmItems.INGOT_BSCCO, "BSCCO Ingot");
        this.add(NtmItems.INGOT_LEAD, "Lead Ingot");
        this.add(NtmItems.INGOT_BISMUTH, "Bismuth Ingot");
        this.add(NtmItems.INGOT_ARSENIC, "Arsenic Ingot");
        this.add(NtmItems.INGOT_CALCIUM, "Calcium Ingot");
        this.add(NtmItems.INGOT_CADMIUM, "Cadmium Ingot");
        this.add(NtmItems.INGOT_TANTALIUM, "Tantalum Ingot");
        this.add(NtmItems.INGOT_TANTALIUM, DESC, "'Tantalum'");
        this.add(NtmItems.INGOT_TANTALIUM, DESC + P11, "AKA Tantalum.");
        this.add(NtmItems.INGOT_SILICON, "Silicon Boule");
        this.add(NtmItems.INGOT_NIOBIUM, "Niobium Ingot");
        this.add(NtmItems.INGOT_BERYLLIUM, "Beryllium Ingot");
        this.add(NtmItems.INGOT_COBALT, "Cobalt Ingot");
        this.add(NtmItems.INGOT_BORON, "Boron Ingot");
        this.add(NtmItems.INGOT_GRAPHITE, "Graphite Ingot");
        this.add(NtmItems.INGOT_FIREBRICK, "Firebrick");
        this.add(NtmItems.INGOT_DURA_STEEL, "High-Speed Steel Ingot");
        this.add(NtmItems.INGOT_POLYMER, "Polymer Bar");
        this.add(NtmItems.INGOT_BAKELITE, "Bakelite Bar");
        this.add(NtmItems.INGOT_BIORUBBER, "Latex Bar");
        this.add(NtmItems.INGOT_RUBBER, "Rubber Bar");
        this.add(NtmItems.INGOT_PC, "Hard Plastic Bar");
        this.add(NtmItems.INGOT_PVC, "PVC Bar");
        this.add(NtmItems.INGOT_MUD, "Solid Mud Brick");
        this.add(NtmItems.INGOT_CTF, "Crystalline Fullerite");
        this.add(NtmItems.INGOT_SCHRARANIUM, "Schraranium Ingot");
        this.add(NtmItems.INGOT_SCHRARANIUM, DESC, "Made from uranium in a schrabidium transmutator");
        this.add(NtmItems.INGOT_SCHRABIDIUM, "Schrabidium Ingot");
        this.add(NtmItems.INGOT_SCHRABIDATE, "Ferric Schrabidate Ingot");
        this.add(NtmItems.INGOT_MAGNETIZED_TUNGSTEN, "Magnetized Tungsten Ingot");
        this.add(NtmItems.INGOT_COMBINE_STEEL, "CMB Steel Ingot");
        this.add(NtmItems.INGOT_COMBINE_STEEL, DESC, "CMB Steel Ingot");
        this.add(NtmItems.INGOT_SOLINIUM, "Solinium Ingot");
        this.add(NtmItems.INGOT_GH336, "Ghiorsium-336 Ingot");
        this.add(NtmItems.INGOT_GH336, DESC, "Seaborgium's colleague.");
        this.add(NtmItems.INGOT_URANIUM_FUEL, "Ingot of Uranium Fuel");
        this.add(NtmItems.INGOT_THORIUM_FUEL, "Ingot of Thorium Fuel");
        this.add(NtmItems.INGOT_PLUTONIUM_FUEL, "Ingot of Plutonium Fuel");
        this.add(NtmItems.INGOT_NEPTUNIUM_FUEL, "Neptunium Fuel Ingot");
        this.add(NtmItems.INGOT_MOX_FUEL, "Ingot of MOX Fuel");
        this.add(NtmItems.INGOT_AMERICIUM_FUEL, "Ingot of Americium Fuel");
        this.add(NtmItems.INGOT_SCHRABIDIUM_FUEL, "Ingot of Schrabidium Fuel");
        this.add(NtmItems.INGOT_HES, "Highly Enriched Schrabidium Fuel Ingot");
        this.add(NtmItems.INGOT_LES, "Low Enriched Schrabidium Fuel Ingot");
        this.add(NtmItems.INGOT_AUSTRALIUM, "Australium Ingot");
        this.add(NtmItems.INGOT_LANTHANIUM, "Semi-Stable Lanthanium Ingot");
        this.add(NtmItems.INGOT_LANTHANIUM, DESC, "'Lanthanum'");
        this.add(NtmItems.INGOT_LANTHANIUM, DESC + P11, "Actually Lanthanum, but whatever.");
        this.add(NtmItems.INGOT_ACTINIUM, "Actinium-227 Ingot");
        this.add(NtmItems.INGOT_DESH, "Desh Ingot");
        this.add(NtmItems.INGOT_FERROURANIUM, "Ferrouranium Ingot");
        this.add(NtmItems.INGOT_STARMETAL, "§9Starmetal Ingot§r");
        this.add(NtmItems.INGOT_GUNMETAL, "Gunmetal Ingot");
        this.add(NtmItems.INGOT_WEAPONSTEEL, "Weapon Steel Ingot");
        this.add(NtmItems.INGOT_SATURNITE, "Saturnite Ingot");
        this.add(NtmItems.INGOT_EUPHEMIUM, "Euphemium Ingot");
        this.add(NtmItems.INGOT_EUPHEMIUM, DESC, "A very special and yet strange element.");
        this.add(NtmItems.INGOT_DINEUTRONIUM, "Dineutronium Ingot");
        this.add(NtmItems.INGOT_ELECTRONIUM, "Electronium Ingot");
        this.add(NtmItems.INGOT_SMORE, "S'more Ingot");
        this.add(NtmItems.INGOT_OSMIRIDIUM, "Osmiridium Ingot");

        this.add(NtmItems.PELLET_RTG, "Plutonium-238 RTG Pellet");

        this.add(NtmItems.CELL_EMPTY, "Empty Cell");
        this.add(NtmItems.CELL_UF6, "Uranium Hexafluoride Cell");
        this.add(NtmItems.CELL_PUF6, "Plutonium Hexafluoride Cell");
        this.add(NtmItems.CELL_ANTIMATTER, "Antimatter Cell");
        this.add(NtmItems.CELL_ANTIMATTER, DESC, "Warning: Exposure to matter will$lead to violent annihilation!");
        this.add(NtmItems.CELL_DEUTERIUM, "Deuterium Cell");
        this.add(NtmItems.CELL_TRITIUM, "Tritium Cell");
        this.add(NtmItems.CELL_SAS3, "Schrabidium Trisulfide Cell");
        this.add(NtmItems.CELL_ANTI_SCHARBIDIUM, "Antischrabidium Cell");
        this.add(NtmItems.CELL_ANTI_SCHARBIDIUM, DESC, "Warning: Exposure to matter will$create a fólkvangr field!");
        this.add(NtmItems.CELL_BALEFIRE, "Gaseous Balefire Cell");

        this.add(NtmItems.PARTICLE_DIGAMMA, "§cThe Digamma Particle§r");
        this.add(NtmItems.PARTICLE_LUTECE, "Lutece Quasiparticle");

        this.add(NtmItems.SINGULARITY, "Singularity");
        this.add(NtmItems.SINGULARITY, DESC, "You may be asking:$\"But HBM, a manifold with an undefined$state of spacetime? How is this possible?\"$Long answer short:$\"I have no idea!\"");
        this.add(NtmItems.SINGULARITY_COUNTER_RESONANT, "Contained Counter-Resonant Singularity");
        this.add(NtmItems.SINGULARITY_COUNTER_RESONANT, DESC, "Nullifies resonance of objects in$non-euclidean space, creates variable$gravity well. Spontaneously spawns$tesseracts. If a tesseract happens to$appear near you, do not look directly$at it.");
        this.add(NtmItems.SINGULARITY_SUPER_HEATED, "Superheated Resonating Singularity");
        this.add(NtmItems.SINGULARITY_SUPER_HEATED, DESC, "Continuously heats up matter by$resonating every planck second.$Tends to catch fire or to create$small plasma arcs. Not edible.");
        this.add(NtmItems.BLACK_HOLE, "Miniature Black Hole");
        this.add(NtmItems.BLACK_HOLE, DESC, "Contains a regular singularity$in the center. Large enough to$stay stable. It's not the end$of the world as we know it,$and I don't feel fine.");
        this.add(NtmItems.SINGULARITY_SPARK, "Spark Singularity");
        this.add(NtmItems.PELLET_ANTIMATTER, "Antimatter Cluster");
        this.add(NtmItems.PELLET_ANTIMATTER, DESC, "Very heavy antimatter cluster.$Gets rid of black holes.");

        this.add(NtmItems.INF_WATER, "Infinite Water Tank");
        this.add(NtmItems.INF_WATER_MK2, "Large Infinite Water Tank");

        this.add(NtmItems.FLUID_TANK_EMPTY, "Empty Universal Fluid Tank");
        this.add(NtmItems.FLUID_TANK_FULL, "Universal Fluid Tank: %s");
        this.add(NtmItems.FLUID_TANK_LEAD_EMPTY, "Empty Hazardous Material Tank");
        this.add(NtmItems.FLUID_TANK_LEAD_FULL, "Hazardous Material Tank: %s");
        this.add(NtmItems.FLUID_BARREL_EMPTY, "Empty Fluid Barrel");
        this.add(NtmItems.FLUID_BARREL_FULL, "Fluid Barrel: %s");
        this.add(NtmItems.FLUID_BARREL_INFINITE, "Infinite Fluid Barrel");

        this.add(NtmItems.FLUID_PACK_EMPTY, "Large Fluid Container");
        this.add(NtmItems.FLUID_PACK_FULL, "Packaged %s");

        this.add(NtmItems.BATTERY_SPARK, "Spark Battery");
        this.add(NtmItems.BATTERY_TRIXITE, "Off-Brand Spark Battery");

        this.add("item.hbmsntm.obj_battery_pack.desc0", "Energy stored: %s");
        this.add("item.hbmsntm.obj_battery_pack.desc1", "Charge rate: %s");
        this.add("item.hbmsntm.obj_battery_pack.desc2", "Discharge rate: %s");
        this.add("item.hbmsntm.obj_battery_pack.desc3", "Time for full charge: %s");
        this.add("item.hbmsntm.obj_battery_pack.desc4", "Charge lasts for: %s");
        this.add(NtmItems.BATTERY_PACK, this.getName(BatteryPackType.BATTERY_REDSTONE), "Redstone Battery");
        this.add(NtmItems.BATTERY_PACK, this.getName(BatteryPackType.BATTERY_LEAD), "Lead-Acid Battery");
        this.add(NtmItems.BATTERY_PACK, this.getName(BatteryPackType.BATTERY_LITHIUM), "Lithium-Ion Battery");
        this.add(NtmItems.BATTERY_PACK, this.getName(BatteryPackType.BATTERY_SODIUM), "Sodium-Iron Battery");
        this.add(NtmItems.BATTERY_PACK, this.getName(BatteryPackType.BATTERY_SCHRABIDIUM), "Schrabidium Battery");
        this.add(NtmItems.BATTERY_PACK, this.getName(BatteryPackType.BATTERY_QUANTUM), "Quantum Battery");
        this.add(NtmItems.BATTERY_PACK, this.getName(BatteryPackType.CAPACITOR_COPPER), "Copper Capacitor");
        this.add(NtmItems.BATTERY_PACK, this.getName(BatteryPackType.CAPACITOR_GOLD), "Gold Capacitor");
        this.add(NtmItems.BATTERY_PACK, this.getName(BatteryPackType.CAPACITOR_NIOBIUM), "Niobium Capacitor");
        this.add(NtmItems.BATTERY_PACK, this.getName(BatteryPackType.CAPACITOR_TANTALUM), "Tantalum Capacitor");
        this.add(NtmItems.BATTERY_PACK, this.getName(BatteryPackType.CAPACITOR_BISMUTH), "Bismuth Capacitor");
        this.add(NtmItems.BATTERY_PACK, this.getName(BatteryPackType.CAPACITOR_SPARK), "Spark Capacitor");
        this.add("item.hbmsntm.obj_battery_sc.desc", "Discharge rate: %s");
        this.add(NtmItems.BATTERY_SC, this.getName(BatterySCType.EMPTY), "Empty Self-Charging Battery");
        this.add(NtmItems.BATTERY_SC, this.getName(BatterySCType.WASTE), "Spent Fuel Self-Charging Battery");
        this.add(NtmItems.BATTERY_SC, this.getName(BatterySCType.RA226), "Radium-226 Self-Charging Battery");
        this.add(NtmItems.BATTERY_SC, this.getName(BatterySCType.TC99), "Technetium-99 Self-Charging Battery");
        this.add(NtmItems.BATTERY_SC, this.getName(BatterySCType.CO60), "Cobalt-60 Self-Charging Battery");
        this.add(NtmItems.BATTERY_SC, this.getName(BatterySCType.PU238), "Plutonium-238 Self-Charging Battery");
        this.add(NtmItems.BATTERY_SC, this.getName(BatterySCType.PO210), "Polonium-210 Self-Charging Battery");
        this.add(NtmItems.BATTERY_SC, this.getName(BatterySCType.AU198), "Gold-198 Self-Charging Battery");
        this.add(NtmItems.BATTERY_SC, this.getName(BatterySCType.PB209), "Lead-209 Self-Charging Battery");
        this.add(NtmItems.BATTERY_SC, this.getName(BatterySCType.AM241), "Americium-241 Self-Charging Battery");
        this.add(NtmItems.BATTERY_CREATIVE, "Infinite Battery");

        this.add(NtmItems.BLUEPRINTS, "Blueprints");

        //this.add(NtmItems.FLUID_ICON, "");
        this.add("item.hbmsntm.obj_fluid_id_multi.desc0", "Universal fluid identifier for:");
        this.add("item.hbmsntm.obj_fluid_id_multi.desc1", "Secondary type:");
        this.add(NtmItems.FLUID_IDENTIFIER_MULTI, "Multi Fluid Identifier");

        this.add(NtmItems.SCREWDRIVER, "Screwdriver");
        this.add(NtmItems.SCREWDRIVER_DESH, "Desh Screwdriver");
        this.add(NtmItems.BLOWTORCH, "Blowtorch");
        this.add(NtmItems.ACETYLENE_TORCH, "Acetylene Welding Torch");

        this.add(NtmItems.ROD_EMPTY, "Empty Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.LITHIUM), "Lithium Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.TRITIUM), "Tritium Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.CO), "Cobalt Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.CO60), "Cobalt-60 Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.TH232), "Thorium-232 Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.THF), "Thorium Fuel Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.U235), "Uranium-235 Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.NP237), "Neptunium-237 Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.U238), "Uranium-238 Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.PU238), "Plutonium-238 Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.PU239), "Plutonium-239 Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.RGP), "Reactor-Grade Plutonium Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.WASTE), "Nuclear Waste Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.LEAD), "Lead Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.URANIUM), "Uranium Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.RA226), "Radium-226 Rod");
        this.add(NtmItems.ROD, this.getName(BreedingRodType.AC227), "Actinium-227 Rod");
        this.add(NtmItems.ROD_DUAL_EMPTY, "Empty Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.LITHIUM), "Lithium Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.TRITIUM), "Tritium Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.CO), "Cobalt Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.CO60), "Cobalt-60 Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.TH232), "Thorium-232 Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.THF), "Thorium Fuel Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.U235), "Uranium-235 Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.NP237), "Neptunium-237 Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.U238), "Uranium-238 Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.PU238), "Plutonium-238 Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.PU239), "Plutonium-239 Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.RGP), "Reactor-Grade Plutonium Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.WASTE), "Nuclear Waste Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.LEAD), "Lead Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.URANIUM), "Uranium Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.RA226), "Radium-226 Dual Rod");
        this.add(NtmItems.ROD_DUAL, this.getName(BreedingRodType.AC227), "Actinium-227 Dual Rod");
        this.add(NtmItems.ROD_QUAD_EMPTY, "Empty Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.LITHIUM), "Lithium Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.TRITIUM), "Tritium Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.CO), "Cobalt Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.CO60), "Cobalt-60 Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.TH232), "Thorium-232 Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.THF), "Thorium Fuel Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.U235), "Uranium-235 Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.NP237), "Neptunium-237 Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.U238), "Uranium-238 Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.PU238), "Plutonium-238 Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.PU239), "Plutonium-239 Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.RGP), "Reactor-Grade Plutonium Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.WASTE), "Nuclear Waste Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.LEAD), "Lead Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.URANIUM), "Uranium Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.RA226), "Radium-226 Quad Rod");
        this.add(NtmItems.ROD_QUAD, this.getName(BreedingRodType.AC227), "Actinium-227 Quad Rod");

        this.add(NtmItems.SPAWN_DUCK, "Golden Egg");

        this.add("item.hbmsntm.obj_designator.pos_set", "Position set!");
        this.add("item.hbmsntm.obj_designator.pos_target", "Target Coordinates:");
        this.add("item.hbmsntm.obj_designator.pos_select", "Please select a target.");
        this.add(NtmItems.DESIGNATOR, "Short Range Target Designator");
        this.add("item.hbmsntm.obj_designator_range.pos_set", "Position set to X: %s, Z: %s");
        this.add(NtmItems.DESIGNATOR_RANGE, "Long Range Target Designator");
        this.add("geiger.title.dosimeter", "===== ☢ DOSIMETER ☢ =====");
        this.add(NtmItems.DOSIMETER, "Dosimeter");
        this.add("geiger.title", "===== ☢ GEIGER COUNTER ☢ =====");
        this.add("geiger.chunkRad", "Current chunk radiation: %s");
        this.add("geiger.envRad", "Total environmental radiation: %s");
        this.add("geiger.playerRad", "Player contamination: %s");
        this.add("geiger.playerRes", "Player resistance: %s");
        this.add(NtmItems.GEIGER_COUNTER, "Handheld Geiger Counter");
        this.add("digamma.title", "===== Ϝ DIGAMMA DIAGNOSTIC Ϝ =====");
        this.add("digamma.playerDigamma", "Digamma exposure: %s");
        this.add("digamma.playerHealth", "Digamma influence: %s");
        this.add("digamma.playerRes", "Digamma resistance: %s");
        this.add(NtmItems.DIGAMMA_DIAGNOSTIC, "Digamma Diagnostic");

        this.add(NtmItems.PIN, "Bobby Pin");
        this.add(NtmItems.PIN, DESC, "Standard success rate of picking a regular lock is ~10%%.");
        this.add(NtmItems.KEY, "Key");
        this.add(NtmItems.KEY_RED, "Red Key");
        this.add(NtmItems.KEY_RED, DESC, "Explore the other side.");
        this.add(NtmItems.KEY_RED, DESC + P11, "§4e§r");
        this.add(NtmItems.KEY_RED_CRACKED, "Red Key");
        this.add(NtmItems.KEY_RED_CRACKED, DESC, "???");
        this.add(NtmItems.KEY_RED_CRACKED, DESC + P11,"§4???§r");
        this.add(NtmItems.KEY_KIT, "Key Imitation Kit");
        this.add(NtmItems.KEY_FAKE, "Counterfeit Key");
        this.add(NtmItems.LAUNCH_CODE_PIECE, "Silo Launch Code Piece");
        this.add(NtmItems.LAUNCH_CODE, "Silo Launch Code");
        this.add(NtmItems.LAUNCH_KEY, "Silo Launch Key");

        this.add("item.hbmsntm.obj_missile.tier0", "Tier 0");
        this.add("item.hbmsntm.obj_missile.tier1", "Tier 1");
        this.add("item.hbmsntm.obj_missile.tier2", "Tier 2");
        this.add("item.hbmsntm.obj_missile.tier3", "Tier 3");
        this.add("item.hbmsntm.obj_missile.tier4", "Tier 4");
        this.add("item.hbmsntm.obj_missile.not_launchable", "Not launchable!");
        this.add("item.hbmsntm.obj_missile.desc.fuel", "Fuel");
        this.add("item.hbmsntm.obj_missile.desc.fuel_cap", "Fuel capacity");
        this.add("item.hbmsntm.obj_missile.fuel.solid", "Solid Fuel");
        this.add("item.hbmsntm.obj_missile.fuel.solid.prefueled", "Solid Fuel (pre-fueled)");
        this.add("item.hbmsntm.obj_missile.fuel.ethanol_peroxide", "Ethanol / Hydrogen Peroxide");
        this.add("item.hbmsntm.obj_missile.fuel.kerosene_peroxide", "Kerosene / Hydrogen Peroxide");
        this.add("item.hbmsntm.obj_missile.fuel.kerosene_loxy", "Kerosene / Liquid Oxygen");
        this.add("item.hbmsntm.obj_missile.fuel.jetfuel_loxy", "Jet Fuel / Liquid Oxygen");
        this.add(NtmItems.MISSILE_TAINT, "Taint-Tipped Missile");
        this.add(NtmItems.MISSILE_MICRO, "Micro-Nuclear Missile");
        this.add(NtmItems.MISSILE_BHOLE, "Black Hole Missile");
        this.add(NtmItems.MISSILE_SCHRABIDIUM, "Schrabidium Missile");
        this.add(NtmItems.MISSILE_EMP, "EMP Missile");

        this.add(NtmItems.MISSILE_GENERIC, "High Explosive Missile");
        this.add(NtmItems.MISSILE_INCENDIARY, "Incendiary Missile");
        this.add(NtmItems.MISSILE_CLUSTER, "Cluster Missile");
        this.add(NtmItems.MISSILE_BUSTER, "Bunker Buster");
        this.add(NtmItems.MISSILE_DECOY, "Decoy Missile");
        this.add(NtmItems.MISSILE_STEALTH, "Stealth Missile");
        this.add(NtmItems.MISSILE_ANTI_BALLISTIC, "Anti-Ballistic Missile");

        this.add(NtmItems.MISSILE_STRONG, "Strong HE Missile");
        this.add(NtmItems.MISSILE_INCENDIARY_STRONG, "Strong Incendiary Missile");
        this.add(NtmItems.MISSILE_CLUSTER_STRONG, "Strong Cluster Missile");
        this.add(NtmItems.MISSILE_BUSTER_STRONG, "Enhanced Bunker Buster");
        this.add(NtmItems.MISSILE_EMP_STRONG, "Strong EMP Missile");

        this.add(NtmItems.MISSILE_BURST, "Spare Missile");
        this.add(NtmItems.MISSILE_INFERNO, "Inferno Missile G.R.N. Mk.II");
        this.add(NtmItems.MISSILE_RAIN, "Bomblet Rain");
        this.add(NtmItems.MISSILE_DRILL, "The Concrete Cracker");
        this.add(NtmItems.MISSILE_SHUTTLE, "Reliant Robin Space Shuttle");

        this.add(NtmItems.MISSILE_NUCLEAR, "Nuclear Missile");
        this.add(NtmItems.MISSILE_NUCLEAR_CLUSTER, "Thermonuclear Missile");
        this.add(NtmItems.MISSILE_VOLCANO, "Tectonic Missile");
        this.add(NtmItems.MISSILE_VOLCANO, DESC, "Using the power of nuclear explosives, we can summon a volcano!");
        this.add(NtmItems.MISSILE_DOOMSDAY, "Doomsday Missile");
        this.add(NtmItems.MISSILE_DOOMSDAY_RUSTED, "Damaged Doomsday Missile");

        this.add("item.hbmsntm.obj_soyuz.skin", "Skin:");
        this.add("item.hbmsntm.obj_soyuz.skin.original", "Original");
        this.add("item.hbmsntm.obj_soyuz.skin.luna", "Luna Space Center");
        this.add("item.hbmsntm.obj_soyuz.skin.post", "Post War");
        this.add(NtmItems.MISSILE_SOYUZ, "Soyuz-FG");

        this.add("item.hbmsntm.obj_sat_chip.frequency", "Satellite frequency: %s");
        this.add(NtmItems.SATELLITE_RADAR, "Radar Survey Satellite");
        this.add(NtmItems.SATELLITE_RADAR, DESC, "Shows a map of active entities.");
        this.add(NtmItems.SATELLITE_LASER, "Orbital Death Ray");
        this.add(NtmItems.SATELLITE_LASER, DESC, "Allows to summon lasers with a 15 second cooldown.");
        this.add(NtmItems.SATELLITE_INTERFACE, "Satellite Control Interface");

        this.add(NtmItems.BALEFIRE_AND_STEEL, "Balefire and Steel");

        this.add("item.hbmsntm.obj_drink.requires_opener", "[Requires bottle opener]");
        this.add(NtmItems.DRINK, this.getName(DrinkType.CAN_EMPTY), "Empty Can");
        this.add(NtmItems.DRINK, this.getName(DrinkType.SMART), "'Smart' Energy Drink");
        this.add(NtmItems.DRINK, this.getName(DrinkType.SMART) + DESC, "Cheap and full of bubbles");
        this.add(NtmItems.DRINK, this.getName(DrinkType.CREATURE), "'Creature' Energy Drink");
        this.add(NtmItems.DRINK, this.getName(DrinkType.CREATURE) + DESC, "Basically gasoline in a tin can");
        this.add(NtmItems.DRINK, this.getName(DrinkType.REDBOMB), "'Red Bomb' Energy Drink");
        this.add(NtmItems.DRINK, this.getName(DrinkType.REDBOMB) + DESC, "Liquefied explosives");
        this.add(NtmItems.DRINK, this.getName(DrinkType.MRSUGAR), "'Dr. Sugar' Soft Drink");
        this.add(NtmItems.DRINK, this.getName(DrinkType.MRSUGAR) + DESC, "An intellectual drink, for the chosen ones!");
        this.add(NtmItems.DRINK, this.getName(DrinkType.OVERCHARGE), "Overcharge Delirium XT");
        this.add(NtmItems.DRINK, this.getName(DrinkType.OVERCHARGE) + DESC, "Possible side effects include heart attacks, seizures or zombification");
        this.add(NtmItems.DRINK, this.getName(DrinkType.LUNA), "Black Mesa Luna - Dark Cola");
        this.add(NtmItems.DRINK, this.getName(DrinkType.LUNA) + DESC, "Contains actual selenium and star metal. Tastes like night.");
        this.add(NtmItems.DRINK, this.getName(DrinkType.BEPIS), "Bepis");
        this.add(NtmItems.DRINK, this.getName(DrinkType.BEPIS) + DESC, "beppp");
        this.add(NtmItems.DRINK, this.getName(DrinkType.BREEN), "Dr>Breens Private Reserve");
        this.add(NtmItems.DRINK, this.getName(DrinkType.BREEN) + DESC, "Don't drink the water. They put something in it, to make you forget.$I don't even know how I got here.");
        this.add(NtmItems.DRINK, this.getName(DrinkType.MUG), "MUG Root Beer");
        this.add(NtmItems.DRINK, this.getName(DrinkType.COFFEE), "Coffee");
        this.add(NtmItems.DRINK, this.getName(DrinkType.COFFEE_RADIUM), "Radium Coffee");
        this.add(NtmItems.DRINK, this.getName(DrinkType.BOTTLE_EMPTY), "Empty Bomb-Shaped Bottle");
        this.add(NtmItems.DRINK, this.getName(DrinkType.NUKA), "Bottle of Nuka Cola");
        this.add(NtmItems.DRINK, this.getName(DrinkType.NUKA) + DESC, "Contains about 210 kcal and 1500 mSv.");
        this.add(NtmItems.DRINK, this.getName(DrinkType.CHERRY), "Bottle of Nuka Cherry");
        this.add(NtmItems.DRINK, this.getName(DrinkType.CHERRY) + DESC, "Now with severe radiation poisoning in every seventh bottle!");
        this.add(NtmItems.DRINK, this.getName(DrinkType.QUANTUM), "Bottle of Nuka Cola Quantum");
        this.add(NtmItems.DRINK, this.getName(DrinkType.QUANTUM) + DESC, "Comes with a colorful mix of over 70 isotopes!");
        this.add(NtmItems.DRINK, this.getName(DrinkType.SPARKLE), "Bottle of S~Cola");
        this.add(NtmItems.DRINK, this.getName(DrinkType.SPARKLE) + DESC, "The most delicious beverage in the wasteland!");
        this.add(NtmItems.DRINK, this.getName(DrinkType.SPARKLE) + DESC + P11, "Contains trace amounts of taint.");
        this.add(NtmItems.DRINK, this.getName(DrinkType.RAD), "Bottle of S~Cola RAD");
        this.add(NtmItems.DRINK, this.getName(DrinkType.RAD) + DESC, "Tastes like radish and radiation.");
        this.add(NtmItems.DRINK, this.getName(DrinkType.RAD) + DESC + P11, "Now with 400% more radiation!");
        this.add(NtmItems.DRINK, this.getName(DrinkType.BOTTLE2_EMPTY), "Empty Bottle");
        this.add(NtmItems.DRINK, this.getName(DrinkType.KORL), "Korl");
        this.add(NtmItems.DRINK, this.getName(DrinkType.KORL) + DESC, "Contains actual orange juice!");
        this.add(NtmItems.DRINK, this.getName(DrinkType.FRITZ), "Fritz Cola");
        this.add(NtmItems.DRINK, this.getName(DrinkType.FRITZ) + DESC, "moremore caffeine");
        this.add(NtmItems.BOTTLE_OPENER, "Hbm's Own Self-Made Bottle Opener");
        this.add(NtmItems.BOTTLE_OPENER, DESC, "My very own bottle opener.$Use with caution!");

        this.add("item.hbmsntm.canned_" + ConserveType.BEEF.name().toLowerCase(Locale.US), "Canned Beef");
        this.add("item.hbmsntm.canned_" + ConserveType.BEEF.name().toLowerCase(Locale.US) + DESC, "A few centuries ago, a cow died for this.");
        this.add("item.hbmsntm.canned_" + ConserveType.TUNA.name().toLowerCase(Locale.US), "Canned Tuna");
        this.add("item.hbmsntm.canned_" + ConserveType.TUNA.name().toLowerCase(Locale.US) + DESC, "I can't tell if that's actually tuna or dried cement.");
        this.add("item.hbmsntm.canned_" + ConserveType.MYSTERY.name().toLowerCase(Locale.US), "Canned Mystery Meat");
        this.add("item.hbmsntm.canned_" + ConserveType.MYSTERY.name().toLowerCase(Locale.US) + DESC, "What's inside? Only one way to find out!");
        this.add("item.hbmsntm.canned_" + ConserveType.PASHTET.name().toLowerCase(Locale.US), "Паштет");
        this.add("item.hbmsntm.canned_" + ConserveType.PASHTET.name().toLowerCase(Locale.US) + DESC, "услуги перевода недоступны!");
        this.add("item.hbmsntm.canned_" + ConserveType.CHEESE.name().toLowerCase(Locale.US), "Canned Melted Cheese");
        this.add("item.hbmsntm.canned_" + ConserveType.CHEESE.name().toLowerCase(Locale.US) + DESC, "Is it cheese? Is it rubber cement? Who knows, who cares.");
        this.add("item.hbmsntm.canned_" + ConserveType.SLIME.name().toLowerCase(Locale.US), "Condensed Horse Slime");
        this.add("item.hbmsntm.canned_" + ConserveType.SLIME.name().toLowerCase(Locale.US) + DESC, "Now with extra bone marrow.");
        this.add("item.hbmsntm.canned_" + ConserveType.MILK.name().toLowerCase(Locale.US), "Canned Evaporated Milk");
        this.add("item.hbmsntm.canned_" + ConserveType.MILK.name().toLowerCase(Locale.US) + DESC, "Milk 2: More solid than ever before!");
        this.add("item.hbmsntm.canned_" + ConserveType.ASS.name().toLowerCase(Locale.US), "Canned Ass");
        this.add("item.hbmsntm.canned_" + ConserveType.ASS.name().toLowerCase(Locale.US) + DESC, "100%% quality donkey meat!*");
        this.add("item.hbmsntm.canned_" + ConserveType.PIZZA.name().toLowerCase(Locale.US), "Canned Pepperoni Pizza");
        this.add("item.hbmsntm.canned_" + ConserveType.PIZZA.name().toLowerCase(Locale.US) + DESC, "A crime against humanity.");
        this.add("item.hbmsntm.canned_" + ConserveType.TUBE.name().toLowerCase(Locale.US), "Astronaut Food Tube");
        this.add("item.hbmsntm.canned_" + ConserveType.TUBE.name().toLowerCase(Locale.US) + DESC, "Tasty mush.");
        this.add("item.hbmsntm.canned_" + ConserveType.TOMATO.name().toLowerCase(Locale.US), "Canned Tomato Soup");
        this.add("item.hbmsntm.canned_" + ConserveType.TOMATO.name().toLowerCase(Locale.US) + DESC, "Who wants some thick red paste?");
        this.add("item.hbmsntm.canned_" + ConserveType.ASBESTOS.name().toLowerCase(Locale.US), "Canned Asbestos");
        this.add("item.hbmsntm.canned_" + ConserveType.ASBESTOS.name().toLowerCase(Locale.US) + DESC, "TASTE the asbestosis!");
        this.add("item.hbmsntm.canned_" + ConserveType.BHOLE.name().toLowerCase(Locale.US), "Canned Black Hole");
        this.add("item.hbmsntm.canned_" + ConserveType.BHOLE.name().toLowerCase(Locale.US) + DESC, "Made from actual singularities. No, really.");
        this.add("item.hbmsntm.canned_" + ConserveType.HOTDOGS.name().toLowerCase(Locale.US), "Canned Hotdogs");
        this.add("item.hbmsntm.canned_" + ConserveType.HOTDOGS.name().toLowerCase(Locale.US) + DESC, "Not to be confused with cool cats.");
        this.add("item.hbmsntm.canned_" + ConserveType.LEFTOVERS.name().toLowerCase(Locale.US), "Leftover Conserve");
        this.add("item.hbmsntm.canned_" + ConserveType.LEFTOVERS.name().toLowerCase(Locale.US) + DESC, "ur 2 slow");
        this.add("item.hbmsntm.canned_" + ConserveType.YOGURT.name().toLowerCase(Locale.US), "Canned Yogurt");
        this.add("item.hbmsntm.canned_" + ConserveType.YOGURT.name().toLowerCase(Locale.US) + DESC, "Probably spoiled, but whatever.");
        this.add("item.hbmsntm.canned_" + ConserveType.STEW.name().toLowerCase(Locale.US), "Canned \"Mushroom Stew\"");
        this.add("item.hbmsntm.canned_" + ConserveType.STEW.name().toLowerCase(Locale.US) + DESC, "...");
        this.add("item.hbmsntm.canned_" + ConserveType.CHINESE.name().toLowerCase(Locale.US), "Canned Chinese Food");
        this.add("item.hbmsntm.canned_" + ConserveType.CHINESE.name().toLowerCase(Locale.US) + DESC, "In China, Chinese food is just called food.");
        this.add("item.hbmsntm.canned_" + ConserveType.OIL.name().toLowerCase(Locale.US), "Canned Engine Oil");
        this.add("item.hbmsntm.canned_" + ConserveType.OIL.name().toLowerCase(Locale.US) + DESC, "It makes motors go, so why not humans?");
        this.add("item.hbmsntm.canned_" + ConserveType.FIST.name().toLowerCase(Locale.US), "Canned Fist");
        this.add("item.hbmsntm.canned_" + ConserveType.FIST.name().toLowerCase(Locale.US) + DESC, "ow");
        this.add("item.hbmsntm.canned_" + ConserveType.SPAM.name().toLowerCase(Locale.US), "Canned Spam");
        this.add("item.hbmsntm.canned_" + ConserveType.SPAM.name().toLowerCase(Locale.US) + DESC, "The three-and-a-half-minute sketch is set in the fictional Green Midget Cafe in Bromley.$An argument develops between the waitress, who recites a menu in which nearly$every dish contains Spam, and Mrs. Bun, who does not like Spam. She asks for a$dish without Spam, much to the amazement of her Spam-loving husband. The waitress$responds to this request with disgust. Mr. Bun offers to take her Spam instead,$and asks for a dish containing a lot of Spam and baked beans. The waitress says$no since they are out of baked beans; when Mr. Bun asks for a substitution of Spam,$the waitress again responds with disgust. At several points, a group of Vikings in$the restaurant interrupts conversation by loudly singing about Spam.$The irate waitress orders them to shut up, but they resume singing more loudly.$A Hungarian tourist comes to the counter, trying to order by using a wholly$inaccurate Hungarian/English phrasebook (a reference to a previous sketch).$He is rapidly escorted away by a police constable. The sketch abruptly cuts to a$historian in a television studio talking about the origin of the Vikings in the café.$As he goes on, he begins to increasingly insert the word \"Spam\" into every$sentence, and the backdrop is raised to reveal the restaurant set behind.$The historian joins the Vikings in their song, and Mr. and Mrs. Bun are lifted by$wires out of the scene while the singing continues. In the original televised performance,$the closing credits begin to scroll with the singing still audible in the background.");
        this.add("item.hbmsntm.canned_" + ConserveType.FRIED.name().toLowerCase(Locale.US), "Canned Fried Chicken");
        this.add("item.hbmsntm.canned_" + ConserveType.FRIED.name().toLowerCase(Locale.US) + DESC, "Even the can is deep fried!");
        this.add("item.hbmsntm.canned_" + ConserveType.NAPALM.name().toLowerCase(Locale.US), "Canned Napalm");
        this.add("item.hbmsntm.canned_" + ConserveType.NAPALM.name().toLowerCase(Locale.US) + DESC, "I love the smell of old memes in the morning!");
        this.add("item.hbmsntm.canned_" + ConserveType.DIESEL.name().toLowerCase(Locale.US), "Canned Diesel");
        this.add("item.hbmsntm.canned_" + ConserveType.DIESEL.name().toLowerCase(Locale.US) + DESC, "I'm slowly running out of jokes for these.");
        this.add("item.hbmsntm.canned_" + ConserveType.KEROSENE.name().toLowerCase(Locale.US), "Canned Kerosene");
        this.add("item.hbmsntm.canned_" + ConserveType.KEROSENE.name().toLowerCase(Locale.US) + DESC, "Just imagine a witty line here.");
        this.add("item.hbmsntm.canned_" + ConserveType.RECURSION.name().toLowerCase(Locale.US), "Canned Recursion");
        this.add("item.hbmsntm.canned_" + ConserveType.RECURSION.name().toLowerCase(Locale.US) + DESC, "Canned Recursion");
        this.add("item.hbmsntm.canned_" + ConserveType.BARK.name().toLowerCase(Locale.US), "Canned Pine Bark Jerky");
        this.add("item.hbmsntm.canned_" + ConserveType.BARK.name().toLowerCase(Locale.US) + DESC, "Extra cronchy!");

        this.add(NtmItems.CAP, this.getName(CapType.NUKA), "Nuka Cola Bottle Cap");
        this.add(NtmItems.CAP, this.getName(CapType.QUANTUM), "Nuka Cola Quantum Bottle Cap");
        this.add(NtmItems.CAP, this.getName(CapType.SPARKLE), "S~Cola Bottle Cap");
        this.add(NtmItems.CAP, this.getName(CapType.RAD), "S~Cola RAD Bottle Cap");
        this.add(NtmItems.CAP, this.getName(CapType.KORL), "Korl Bottle Cap");
        this.add(NtmItems.CAP, this.getName(CapType.FRITZ), "Fritz Cola Bottle Cap");
        this.add(NtmItems.RING_PULL, "Ring Pull");
        this.add(NtmItems.CAN_KEY, "Winding Key");

        this.add(NtmItems.CHOCOLATE_MILK, "Chocolate Milk");
        this.add(NtmItems.CHOCOLATE_MILK, DESC, "Regular chocolate milk. Safe to drink.$Totally not made from nitroglycerine.");
        this.add("item.hbmsntm.obj_cigarette.desc0", "✓ Asbestos filter$✓ High in tar$✓ Tobacco contains 100%% Polonium-210$✓ Yum");
        this.add("item.hbmsntm.obj_cigarette.desc1", "This can't be good for me, but I feel ");
        this.add("item.hbmsntm.obj_cigarette.desc2", "GREAT");
        this.add(NtmItems.CIGARETTE, "FFI-Brand Cigarette");
        this.add(NtmItems.CRACKPIPE, "Health Pipe");

        this.add(NtmItems.EARLY_EXPLOSIVE_LENSES, "Array of First-Generation High-Explosive Lenses");
        this.add(NtmItems.EARLY_EXPLOSIVE_LENSES, DESC, "Assembly of 8 high-explosive lenses with an aluminium$pusher, duraluminium shell, and bridgewire detonators.");
        this.add(NtmItems.EXPLOSIVE_LENSES, "Array of High-Explosive Lenses");
        this.add(NtmItems.EXPLOSIVE_LENSES, DESC, "Assembly of 8 PBX lenses with a thin$aluminium pusher, duraluminium shell, and$miniaturized bridgewire detonators.");

        this.add(NtmItems.GADGET_WIREING, "Wiring");
        this.add(NtmItems.GADGET_CORE, "Large Plutonium Core");

        this.add(NtmItems.LITTLE_BOY_SHIELDING, "Neutron Shielding");
        this.add(NtmItems.LITTLE_BOY_TARGET, "Subcritical U235 Target");
        this.add(NtmItems.LITTLE_BOY_BULLET, "U235 Projectile");
        this.add(NtmItems.LITTLE_BOY_PROPELLANT, "Propellant");
        this.add(NtmItems.LITTLE_BOY_IGNITER, "Bomb Igniter");

        this.add(NtmItems.FAT_MAN_IGNITER, "Bomb Firing Unit");
        this.add(NtmItems.FAT_MAN_CORE, "Plutonium Core");

        this.add(NtmItems.IVY_MIKE_CORE, "Uranium Coated Deuterium Tank");
        this.add(NtmItems.IVY_MIKE_DEUT, "Deuterium Tank");
        this.add(NtmItems.IVY_MIKE_COOLING_UNIT, "Deuterium Cooling Unit");

        this.add(NtmItems.TSAR_BOMBA_CORE, "Tsar Bomba Core");

        this.add("item.hbmsntm.obj_used_in.desc", "Used in:");
        this.add(NtmItems.FLEIJA_IGNITER, "Pulse Igniter");
        this.add(NtmItems.FLEIJA_PROPELLANT, "Schrabidium Propellant");
        this.add(NtmItems.FLEIJA_CORE, "F.L.E.I.J.A. Uranium 235 Charge");

        this.add(NtmItems.SOLINIUM_IGNITER, "SOL Pulse Igniter");
        this.add(NtmItems.SOLINIUM_PROPELLANT, "SOL Compression Charge");
        this.add(NtmItems.SOLINIUM_CORE, "Semi-Stable Solinium Core");

        this.add(NtmItems.N2_CHARGE, "Large Explosive Charge");

        this.add(NtmItems.EGG_BALEFIRE_SHARD, "Balefire Shard");
        this.add(NtmItems.EGG_BALEFIRE, "Balefire Egg");

        this.add(NtmItems.IGNITER, "Igniter");
        this.add(NtmItems.IGNITER, DESC, "(Used by right-clicking the Prototype)$It's a green metal handle with a$bright red button and a small lid.$At the bottom, the initials N.E. are$engraved. Whoever N.E. was, he had$a great taste in shades of green.");
        this.add("item.hbmsntm.obj_detonator.pos_set", "Position set!");
        this.add("item.hbmsntm.obj_detonator.pos_none", "No position set!");
        this.add("item.hbmsntm.obj_detonator.pos_linked", "Linked to %s, %s, %s");
        this.add(NtmItems.DETONATOR, "Detonator");
        this.add(NtmItems.DETONATOR, DESC, "Shift right-click to set position,$right-click to detonate!");
        this.add("item.hbmsntm.obj_detonator.pos_added", "Position added!");
        this.add(NtmItems.DETONATOR_MULTI, "Multi Detonator");
        this.add(NtmItems.DETONATOR_MULTI, DESC, "Shift right-click block to add position,$right-click to detonate!$Shift right-click in the air to clear positions.");
        this.add(NtmItems.DETONATOR_LASER, "Laser Detonator");
        this.add(NtmItems.DETONATOR_LASER, DESC,"Aim & click to detonate!");
        this.add(NtmItems.DETONATOR_DEADMAN, "Dead Man's Detonator");
        this.add(NtmItems.DETONATOR_DEADMAN, DESC, "Shift right-click to set position,$drop to detonate!");
        this.add(NtmItems.DETONATOR_DE, "Dead Man's Explosive");
        this.add(NtmItems.DETONATOR_DE, DESC, "Explodes when dropped!");
        this.add("item.hbmsntm.obj_bomb_caller.desc0", "Type: Carpet bombing");
        this.add("item.hbmsntm.obj_bomb_caller.desc1", "Type: Napalm");
        this.add("item.hbmsntm.obj_bomb_caller.desc2", "Type: Atomic bomb");
        this.add("item.hbmsntm.obj_bomb_caller.call", "Called in airstrike!");
        this.add("item.hbmsntm.obj_bomb_caller.call.unloaded", "Airstrike can't be called in unloaded chunks!");
        this.add(NtmItems.BOMB_CALLER, "Airstrike Designator");
        this.add(NtmItems.BOMB_CALLER, DESC, "Aim & click to call an airstrike!");
        this.add(NtmItems.DEFUSER, "High-Tech Bomb Defusing Device");
        this.add(NtmItems.REACHER, "Tungsten Reacher");

        this.add("item.hbmsntm.obj_polaroid.desc", "Fate chosen");
        this.add("item.hbmsntm.obj_polaroid.fate1", "...");
        this.add("item.hbmsntm.obj_polaroid.fate2", "Clear as glass.");
        this.add("item.hbmsntm.obj_polaroid.fate3", "'M");
        this.add("item.hbmsntm.obj_polaroid.fate4", "It's about time.");
        this.add("item.hbmsntm.obj_polaroid.fate5", "If you stare long into the abyss, the abyss stares back.");
        this.add("item.hbmsntm.obj_polaroid.fate6", "public Party celebration = new Party();");
        this.add("item.hbmsntm.obj_polaroid.fate7", "V urnerq lbh yvxr EBG13!");
        this.add("item.hbmsntm.obj_polaroid.fate8", "11011100");
        this.add("item.hbmsntm.obj_polaroid.fate9", "Vg'f nobhg gvzr.");
        this.add("item.hbmsntm.obj_polaroid.fate10", "Schrabidium dislikes the breeding reactor.");
        this.add("item.hbmsntm.obj_polaroid.fate11", "yss stares back.6public Party cel");
        this.add("item.hbmsntm.obj_polaroid.fate12", "Red streaks.");
        this.add("item.hbmsntm.obj_polaroid.fate13", "Q1");
        this.add("item.hbmsntm.obj_polaroid.fate14", "Q4");
        this.add("item.hbmsntm.obj_polaroid.fate15", "Q3");
        this.add("item.hbmsntm.obj_polaroid.fate16", "Q2");
        this.add("item.hbmsntm.obj_polaroid.fate17", "Two friends before christmas.");
        this.add("item.hbmsntm.obj_polaroid.fate18", "Duchess of the boxcars.$$\"P.S.: Thirty-one.\"$\"Huh, what does thirty-one mean?\"");
        this.add(NtmItems.POLAROID, "The Polaroid");
        this.add(NtmItems.BURNT_BARK, "Burnt Bark");
        this.add(NtmItems.BURNT_BARK, DESC, "A piece of bark from an exploded golden oak tree.");

        this.add("item.hbmsntm.obj_starter_kit.empty_inventory", "Please empty inventory before opening!");
        this.add(NtmItems.STARTER_KIT, this.getName(KitType.GADGET), "The Gadget Kit");
        this.add(NtmItems.STARTER_KIT, this.getName(KitType.LITTLE_BOY), "Little Boy Kit");
        this.add(NtmItems.STARTER_KIT, this.getName(KitType.FAT_MAN), "Fat Man Kit");
        this.add(NtmItems.STARTER_KIT, this.getName(KitType.IVY_MIKE), "Ivy Mike Kit");
        this.add(NtmItems.STARTER_KIT, this.getName(KitType.TSAR_BOMBA), "Tsar Bomba Kit");
        this.add(NtmItems.STARTER_KIT, this.getName(KitType.PROTOTYPE), "Prototype Kit");
        this.add(NtmItems.STARTER_KIT, this.getName(KitType.FLEIJA), "F.L.E.I.J.A. Kit");
        this.add(NtmItems.STARTER_KIT, this.getName(KitType.SOLINIUM), "Solinium Kit");

        this.add(NtmItems.TEMPLATE_FOLDER, "Machine Template Folder");
        this.add(NtmItems.NOTHING, "Nothing");

        // BLOCKS //
        this.add(NtmBlocks.ORE_BASALT, this.getName(BasaltOreType.SULFUR), "Sulfur-Rich Basalt");
        this.add(NtmBlocks.ORE_BASALT, this.getName(BasaltOreType.FLUORITE), "Fluorite-Rich Basalt");
        this.add(NtmBlocks.ORE_BASALT, this.getName(BasaltOreType.ASBESTOS), "Asbestos-Rich Basalt");
        this.add(NtmBlocks.ORE_BASALT, this.getName(BasaltOreType.GEM), "Gem-Rich Basalt");
        this.add(NtmBlocks.ORE_BASALT, this.getName(BasaltOreType.MOLYSITE), "Molysite-Rich Basalt");

        this.add(NtmBlocks.BASALT, "Basalt");
        this.add(NtmBlocks.BASALT_SMOOTH, "Smooth Basalt");
        this.add(NtmBlocks.BASALT_BRICK, "Basalt Bricks");
        this.add(NtmBlocks.BASALT_POLISHED, "Basalt Tiles");
        this.add(NtmBlocks.BASALT_TILES, "Basalt Tiles");

        this.add(NtmBlocks.BOBBLEHEAD, "Bobblehead");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.YOMI), "Yomi Plushie");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.YOMI) + DESC, "Hi! Can I be your rabbit friend");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.NUMBERNINE), "Number Nine Plushie");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.NUMBERNINE) + DESC, "None of y'all deserve coal.");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.HUNDUN), "Hundun Plushie");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.HUNDUN) + DESC, "混沌");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.DERG), "Dragon Plushie");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.DERG) + DESC, "Squeeze him.");

        this.add(NtmBlocks.GRAVEL_OBSIDIAN, "Crushed Obsidian");
        this.add(NtmBlocks.GRAVEL_DIAMOND, "Diamond Gravel");
        this.add(NtmBlocks.GRAVEL_DIAMOND, DESC, "There is some kind of joke here,$but I can't quite tell what it is.$$Update, 2020-07-04:$We deny any implications of a joke on$the basis that it was so severely unfunny$that people started stabbing their eyes out.$$Update, 2020-17-04:$As it turns out, \"Diamond Gravel\" was$never really a thing, rendering what might$have been a joke as totally nonsensical.$We apologize for getting your hopes up with$this non-joke that hasn't been made.$$i added an item for a joke that isn't even here, what am i, stupid? can't even tell the difference between gravel and a gavel, how did i not forget how to breathe yet?");

        this.add("block.hbmsntm.obj_blast_info.desc", "Blast Resistance: %s");
        this.add("block.hbmsntm.obj_speedy.desc", "Increases speed by %s%%");
        this.add(NtmBlocks.ASPHALT, "Asphalt");
        this.add(NtmBlocks.ASPHALT_LIGHT, "Glowing Asphalt");

        this.add("block.hbmsntm.obj_no_spawn.desc", "Mobs cannot spawn on this block!");
        this.add(NtmBlocks.BRICK_CONCRETE, "Concrete Bricks");
        this.add(NtmBlocks.BRICK_CONCRETE_MOSSY, "Mossy Concrete Bricks");
        this.add(NtmBlocks.BRICK_CONCRETE_CRACKED, "Cracked Concrete Bricks");
        this.add(NtmBlocks.BRICK_CONCRETE_BROKEN, "Broken Concrete Bricks");
        this.add(NtmBlocks.BRICK_CONCRETE_MARKED, "Marked Concrete Bricks");
        this.add(NtmBlocks.BRICK_OBSIDIAN, "Obsidian Bricks");
        this.add(NtmBlocks.BRICK_LIGHT, "Light Bricks");
        this.add(NtmBlocks.BRICK_ASBESTOS, "Asbestos Bricks");
        this.add(NtmBlocks.BRICK_FIRE, "Firebricks");

        this.add(NtmBlocks.BRICK_CONCRETE_SLAB, "Concrete Brick Slab");
        this.add(NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB, "Mossy Concrete Brick Slab");
        this.add(NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB, "Cracked Concrete Brick Slab");
        this.add(NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB, "Broken Concrete Brick Slab");

        this.add(NtmBlocks.BRICK_CONCRETE_STAIRS, "Concrete Brick Stairs");
        this.add(NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS, "Mossy Concrete Brick Stairs");
        this.add(NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS, "Cracked Concrete Brick Stairs");
        this.add(NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS, "Broken Concrete Brick Stairs");

        this.add(NtmBlocks.BARBED_WIRE, this.getName(BarbedWireType.STANDARD),   "Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, this.getName(BarbedWireType.FIRE),       "Flaming Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, this.getName(BarbedWireType.POISON),     "Poisoned Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, this.getName(BarbedWireType.ACID),       "Caustic Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, this.getName(BarbedWireType.WITHER),     "Withered Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, this.getName(BarbedWireType.ULTRADEATH), "Radioactive Barbed Wire");
        this.add(NtmBlocks.SPIKES, "Spikes");

        this.add(NtmBlocks.WASTE_EARTH, "Dead Grass");
        this.add(NtmBlocks.WASTE_MYCELIUM, "Glowing Mycelium");
        this.add(NtmBlocks.WASTE_TRINITITE, "Trinitite Ore");
        this.add(NtmBlocks.WASTE_TRINITITE_RED, "Red Trinitite Ore");
        this.add(NtmBlocks.WASTE_LOG, "Charred Log");
        this.add(NtmBlocks.WASTE_LEAVES, "Dead Leaves");
        this.add(NtmBlocks.WASTE_PLANKS, "Charred Wooden Planks");
        this.add(NtmBlocks.FROZEN_DIRT, "Frozen Dirt");
        this.add(NtmBlocks.FROZEN_GRASS, "Frozen Grass");
        this.add(NtmBlocks.FROZEN_LOG, "Frozen Log");
        this.add(NtmBlocks.FROZEN_PLANKS, "Frozen Planks");
        this.add(NtmBlocks.LEAVES_LAYER, "Fallen Leaves");
        this.add(NtmBlocks.FALLOUT, "Fallout");
        this.add(NtmBlocks.SELLAFIELD_SLAKED, "Slaked Sellafite");
        this.add(NtmBlocks.ORE_SELLAFIELD_DIAMOND, "Sellafite Diamond Ore");
        this.add(NtmBlocks.ORE_SELLAFIELD_EMERALD, "Sellafite Emerald Ore");
        this.add(NtmBlocks.SELLAFIELD_BEDROCK, "Bedrock Sellafite");
        this.add(NtmBlocks.ORE_URANIUM, "Uranium Ore");
        this.add(NtmBlocks.ORE_URANIUM_DEEPSLATE, "Deepslate Uranium Ore");
        this.add(NtmBlocks.ORE_URANIUM_SCORCHED, "Scorched Uranium Ore");
        this.add(NtmBlocks.ORE_BERYLLIUM, "Beryllium Ore");
        this.add(NtmBlocks.ORE_BERYLLIUM_DEEPSLATE, "Deepslate Beryllium Ore");
        this.add(NtmBlocks.ORE_TUNGSTEN, "Tungsten Ore");
        this.add(NtmBlocks.ORE_TUNGSTEN_DEEPSLATE, "Deepslate Tungsten Ore");
        this.add(NtmBlocks.ORE_TITANIUM, "Titanium Ore");
        this.add(NtmBlocks.ORE_TITANIUM_DEEPSLATE, "Deepslate Titanium Ore");
        this.add(NtmBlocks.ORE_LEAD, "Lead Ore");
        this.add(NtmBlocks.ORE_LEAD_DEEPSLATE, "Deepslate Lead Ore");
        this.add(NtmBlocks.ORE_ALUMINIUM, "Aluminium Ore");
        this.add(NtmBlocks.ORE_ALUMINIUM_DEEPSLATE, "Deepslate Aluminium Ore");
        this.add(NtmBlocks.ORE_ASBESTOS, "Asbestos Ore");
        this.add(NtmBlocks.ORE_ASBESTOS_DEEPSLATE, "Deepslate Asbestos Ore");
        this.add(NtmBlocks.ORE_THORIUM, "Thorium Ore");
        this.add(NtmBlocks.ORE_THORIUM_DEEPSLATE, "Deepslate Thorium Ore");
        this.add(NtmBlocks.ORE_NITER, "Niter Ore");
        this.add(NtmBlocks.ORE_NITER_DEEPSLATE, "Deepslate Niter Ore");
        this.add(NtmBlocks.ORE_COBALT, "Cobalt Ore");
        this.add(NtmBlocks.ORE_COBALT_DEEPSLATE, "Deepslate Cobalt Ore");
        this.add(NtmBlocks.ORE_CINNABAR, "Cinnabar Ore");
        this.add(NtmBlocks.ORE_CINNABAR_DEEPSLATE, "Deepslate Cinnabar Ore");
        this.add(NtmBlocks.ORE_FLUORITE, "Fluorite Ore");
        this.add(NtmBlocks.ORE_FLUORITE_DEEPSLATE, "Deepslate Fluorite Ore");
        this.add(NtmBlocks.ORE_RARE, "Rare Ore");
        this.add(NtmBlocks.ORE_RARE_DEEPSLATE, "Deepslate Rare Ore");
        this.add(NtmBlocks.ORE_SULFUR, "Sulfur Ore");
        this.add(NtmBlocks.ORE_SULFUR_DEEPSLATE, "Deepslate Sulfur Ore");
        this.add(NtmBlocks.ORE_LIGNITE, "Lignite Ore");

        this.add(NtmBlocks.NUKE_GADGET, "The Gadget");
        this.add(NtmBlocks.NUKE_LITTLE_BOY, "Little Boy");
        this.add(NtmBlocks.NUKE_FAT_MAN, "Fat Man");
        this.add(NtmBlocks.NUKE_IVY_MIKE, "Ivy Mike");
        this.add(NtmBlocks.NUKE_TSAR_BOMBA, "Tsar Bomba");
        this.add(NtmBlocks.NUKE_PROTOTYPE, "The Prototype");
        this.add(NtmBlocks.NUKE_PROTOTYPE, DESC, "It didn't have to be like this.$ $You monster.");
        this.add(NtmBlocks.NUKE_FLEIJA, "F.L.E.I.J.A.");
        this.add(NtmBlocks.NUKE_SOLINIUM, "The Blue Rinse");
        this.add(NtmBlocks.NUKE_N2, "N² Mine");
        this.add(NtmBlocks.NUKE_FSTBMB, "Balefire Bomb");

        this.add(NtmBlocks.CRASHED_BOMB, "Dud");
        this.add(NtmBlocks.DYNAMITE, "Dynamite");
        this.add(NtmBlocks.TNT, "Actual TNT");
        this.add(NtmBlocks.SEMTEX, "Semtex");
        this.add(NtmBlocks.C4, "C-4");
        this.add(NtmBlocks.FISSURE_BOMB, "Fissure Bomb");

        this.add(NtmBlocks.MINE_AP, "Anti-Personell Mine");
        this.add(NtmBlocks.MINE_HE, "Anti-Tank Mine");
        this.add(NtmBlocks.MINE_SHRAP, "Shrapnel Mine");
        this.add(NtmBlocks.MINE_FAT, "Fat Mine");
        this.add(NtmBlocks.MINE_NAVAL, "Naval Mine");

        this.add(NtmBlocks.DET_CHARGE, "Explosive Charge");
        this.add(NtmBlocks.DET_CORD, "Det Cord");
        this.add(NtmBlocks.DET_NUKE, "Nuclear Charge");
        this.add(NtmBlocks.DET_MINER, "Mining Charge");
        this.add("block.hbmsntm.obj_red_barrel.desc", "Static Fluid Barrel");
        this.add(NtmBlocks.BARREL_RED, "Explosive Barrel");
        this.add(NtmBlocks.BARREL_PINK, "Kerosene Barrel");
        this.add(NtmBlocks.BARREL_LOX, "LOX Barrel");
        this.add(NtmBlocks.BARREL_TAINT, "IMP Residue Barrel");

        this.add(NtmBlocks.GEIGER, "Geiger Counter");

        this.add(NtmBlocks.MACHINE_PRESS, "Burner Press");

        this.add(NtmBlocks.RED_CABLE, "Red Copper Cable");

        this.add(NtmBlocks.FLUID_DUCT_NEO, "Universal Fluid Duct");

        this.add(NtmBlocks.MACHINE_BATTERY_SOCKET, "Battery Socket");
        this.add(NtmBlocks.MACHINE_BATTERY_SOCKET, DESC,"Allows battery items to be connected$to the power grid directly.$Acts as a cable, all ports are connected$to the same network.");
        this.add(NtmBlocks.MACHINE_BATTERY_REDD, "FEnSU");
        this.add(NtmBlocks.MACHINE_ASSEMBLY_MACHINE, "Assembly Machine");
        this.add(NtmBlocks.MACHINE_FLUID_TANK, "Tank");

        this.add(NtmBlocks.MACHINE_SATLINKER, "SatLink Device");

        this.add(NtmBlocks.DECONTAMINATOR, "Player Decontaminator");

        this.add(NtmBlocks.PWR_CONTROLLER, "PWR Controller");

        this.add(NtmBlocks.BALEFIRE, "Balefire");
        this.add(NtmBlocks.FIRE_DIGAMMA, "Lingering Digamma");
        this.add("block.hbmsntm.obj_volcano.desc0", "SHIELD VOLCANO");
        this.add("block.hbmsntm.obj_volcano.desc1", "DOES GROW");
        this.add("block.hbmsntm.obj_volcano.desc2", "DOES NOT GROW");
        this.add("block.hbmsntm.obj_volcano.desc3", "DOES EXTINGUISH");
        this.add("block.hbmsntm.obj_volcano.desc4", "DOES NOT EXTINGUISH");
        this.add(NtmBlocks.VOLCANO_CORE, "Volcano Core");
        this.add(NtmBlocks.VOLCANO_RAD_CORE, "Rad Volcano Core");

        this.add(NtmBlocks.LAUNCH_PAD, "Silo Launch Pad");
        this.add(NtmBlocks.LAUNCH_PAD_LARGE, "Launch Pad");
        this.add(NtmBlocks.SOYUZ_LAUNCHER, "Soyuz Launch Platform");

        this.add(NtmBlocks.VOLCANIC_LAVA, "Volcanic Lava");
        this.add(NtmBlocks.RAD_LAVA, "Radioactive Volcanic Lava");

        this.add(NtmBlocks.GAS_RADON, "Radon Gas");
        this.add(NtmBlocks.GAS_RADON_DENSE, "Dense Radon Gas");
        this.add(NtmBlocks.GAS_RADON_TOMB, "Tomb Gas");
        this.add(NtmBlocks.GAS_MELTDOWN, "Meltdown Gas");
        this.add(NtmBlocks.GAS_MONOXIDE, "Carbon Monoxide");
        this.add(NtmBlocks.GAS_ASBESTOS, "Airborne Asbestos Particles");
        this.add(NtmBlocks.GAS_COAL, "Airborne Coal Dust");
        this.add(NtmBlocks.GAS_FLAMMABLE, "Flammable Gas");
        this.add(NtmBlocks.GAS_EXPLOSIVE, "Explosive Gas");

        this.add(NtmBlocks.TAINT, "Taint");
        this.add(NtmBlocks.TAINT, DESC,"DO NOT TOUCH, BREATHE OR STARE AT.");

        // CONTAINERS //
        this.add("container.nuke_gadget", "The Gadget");
        this.add("container.nuke_gadget.desc", "§1Requires:§r$ * 4 Arrays of First-Generation$   High-Explosive Lenses$ * Large Plutonium Core$ * Wiring");
        this.add("container.nuke_fat_man", "Fat Man");
        this.add("container.nuke_fat_man.desc", "§1Requires:§r$ * 4 Arrays of First-Generation$   High-Explosive Lenses$ * Plutonium Core$ * Bomb Firing Unit");
        this.add("container.nuke_little_boy", "Little Boy");
        this.add("container.nuke_little_boy.desc", "§1Requires:§r$ * Neutron Shielding$ * U235 Projectile$ * Subcritical U235 Target$ * Propellant$ * Bomb Igniter");
        this.add("container.nuke_ivy_mike", "Ivy Mike");
        this.add("container.nuke_ivy_mike.desc", "§1Requires:§r$ * 4 Arrays of High-Explosive Lenses$ * Plutonium Core$ * Deuterium Cooling Unit$ * Uranium Coated Deuterium Tank$ * Deuterium Tank");
        this.add("container.nuke_tsar_bomba", "Tsar Bomba");
        this.add("container.nuke_tsar_bomba.desc", "§1Requires:§r$ * 4 Arrays of High-Explosive Lenses$ * Plutonium Core$§9Optional:§r$ * Tsar Bomba Core");
        this.add("container.nuke_prototype", "The Prototype");
        this.add("container.nuke_fleija", "F.L.E.I.J.A.");
        this.add("container.nuke_solinium", "The Blue Rinse");
        this.add("container.nuke_n2", "N² Mine");
        this.add("container.nuke_fstbmb", "Balefire Bomb");
        this.add("container.battery_socket", "Battery Socket");
        this.add("container.battery_redd", "FEnSU");
        this.add("container.machine_assembly_machine", "Assembly Machine");
        this.add("container.fluidtank", "Tank");
        this.add("container.sat_linker", "SatLink Device");
        this.add("container.sat_linker.desc.copy", "The first slot will copy the satellite/chip's$frequency and paste it to the second slot.");
        this.add("container.sat_linker.desc.rand", "The third slot will randomize the$satellite/chip's frequency.");
        this.add("container.launch_pad", "Launch Pad");
        this.add("container.launch_pad.not_ready", "Not ready");
        this.add("container.launch_pad.loading", "Loading...");
        this.add("container.launch_pad.ready", "Ready");
        this.add("container.soyuz_launcher", "Soyuz Launch Platform");
        this.add("container.soyuz_launcher.soyuz_here", "The Soyuz goes here");
        this.add("container.soyuz_launcher.designator_here", "Designator only for CARGO MODE");
        this.add("container.soyuz_launcher.payload_here", "The payload for SATELLITE MODE");
        this.add("container.soyuz_launcher.module_here", "The orbital module for special payloads");
        this.add("container.soyuz_launcher.sat_mode", "SATELLITE MODE");
        this.add("container.soyuz_launcher.cargo_mode", "CARGO MODE");
        this.add("container.recipe_selector.close", "Close");
        this.add("container.recipe_selector.close_search", "Clear search");
        this.add("container.recipe_selector.toggle_focus", "Press ENTER to toggle focus");
        this.add("container.recipe.duration", "Duration");
        this.add("container.recipe.consumption", "Consumption");
        this.add("container.recipe.input", "Input");
        this.add("container.recipe.output", "Output");
        this.add("container.recipe.at_pressure", "at");
        this.add("container.recipe.set_recipe", "Click to set recipe");

        // COMMANDS //
        this.add("commands.props.get", "Living property %s of %s is %s");
        this.add("commands.props.set", "Living property %s of %s set to %s");
        this.add("commands.props.not_living", "Target is not a living entity!");
        this.add("commands.satellite.no_active_satellites", "No active satellites found!");
        this.add("commands.satellite.no_satellite", "No satellite using this frequency found!");
        this.add("commands.satellite.not_a_satellite", "The held item is not a satellite!");
        this.add("commands.satellite.satellite_descended", "Satellite successfully descended.");
        this.add("commands.satellite.satellite_orbited", "Satellite launched.");
        this.add("commands.satellite.should_be_run_as_player", "This command should be run by a player!");
        this.add("commands.chunkrad.setrad", "Chunk radiation set to %s");

        // FLUIDS //
        this.add(NtmFluidTypes.VOLCANIC_LAVA_TYPE, "Volcanic Lava");
        this.add(NtmFluidTypes.RAD_LAVA_TYPE, "Radioactive Volcanic Lava");

        this.add("hbmfluid.air", "Compressed Air");
        this.add("hbmfluid.alumina", "Alumina");
        this.add("hbmfluid.amat", "Antimatter");
        this.add("hbmfluid.aromatics", "Aromatic Hydrocarbons");
        this.add("hbmfluid.aschrab", "Antischrabidium");
        this.add("hbmfluid.balefire", "BF Rocket Fuel");
        this.add("hbmfluid.bauxite_solution", "Bauxite Solution");
        this.add("hbmfluid.biofuel", "Biofuel");
        this.add("hbmfluid.biogas", "Biogas");
        this.add("hbmfluid.bitumen", "Bitumen");
        this.add("hbmfluid.blood", "Blood");
        this.add("hbmfluid.blood_hot", "Hot Blood");
        this.add("hbmfluid.calcium_chloride", "Calciumchloride Solution");
        this.add("hbmfluid.calcium_solution", "Calcium Solution");
        this.add("hbmfluid.carbondioxide", "Carbon Dioxide");
        this.add("hbmfluid.chlorine", "Chlorine Gas");
        this.add("hbmfluid.chlorocalcite_cleaned", "Cleaned Chlorocalcite Solution");
        this.add("hbmfluid.chlorocalcite_mix", "Mixed Chlorocalcite Solution");
        this.add("hbmfluid.chlorocalcite_solution", "Chlorocalcite Solution");
        this.add("hbmfluid.cholesterol", "Cholesterol Solution");
        this.add("hbmfluid.coalcreosote", "Coal Tar Creosote");
        this.add("hbmfluid.coalgas", "Coal Gasoline");
        this.add("hbmfluid.coalgas_leaded", "Leaded Coal Gasoline");
        this.add("hbmfluid.coaloil", "Coal Oil");
        this.add("hbmfluid.colloid", "Colloid");
        this.add("hbmfluid.concrete", "Liquid Concrete");
        this.add("hbmfluid.coolant", "Coolant");
        this.add("hbmfluid.coolant_hot", "Hot Coolant");
        this.add("hbmfluid.crackoil", "Cracked Oil");
        this.add("hbmfluid.crackoil_ds", "Desulfurized Cracked Oil");
        this.add("hbmfluid.cryogel", "Cryogel");
        this.add("hbmfluid.death", "Osmiridic Solution");
        this.add("hbmfluid.deuterium", "Deuterium");
        this.add("hbmfluid.diesel", "Diesel");
        this.add("hbmfluid.diesel_crack", "Cracked Diesel");
        this.add("hbmfluid.diesel_crack_reform", "High-Cetane Cracked Diesel");
        this.add("hbmfluid.diesel_reform", "High-Cetane Diesel");
        this.add("hbmfluid.egg", "Dissolved Egg");
        this.add("hbmfluid.estradiol", "Estradiol Solution");
        this.add("hbmfluid.ethanol", "Ethanol");
        this.add("hbmfluid.enderjuice", "Ender Juice");
        this.add("hbmfluid.fishoil", "Fish Oil");
        this.add("hbmfluid.fracksol", "Fracking Solution");
        this.add("hbmfluid.fullerene", "Fullerene Solution");
        this.add("hbmfluid.gas", "Natural Gas");
        this.add("hbmfluid.gas_coker", "Coker Gas");
        this.add("hbmfluid.gasoline", "Gasoline");
        this.add("hbmfluid.gasoline_leaded", "Leaded Gasoline");
        this.add("hbmfluid.heatingoil", "Heating Oil");
        this.add("hbmfluid.heatingoil_vacuum", "Heavy Heating Oil");
        this.add("hbmfluid.heavyoil", "Heavy Oil");
        this.add("hbmfluid.heavyoil_vacuum", "Vacuum Heavy Oil");
        this.add("hbmfluid.heavywater", "Heavy Water");
        this.add("hbmfluid.heavywater_hot", "Hot Heavy Water");
        this.add("hbmfluid.helium3", "Helium-3");
        this.add("hbmfluid.helium4", "Helium-4");
        this.add("hbmfluid.hotcrackoil", "Hot Cracked Oil");
        this.add("hbmfluid.hotcrackoil_ds", "Desulfurized Hot Cracked Oil");
        this.add("hbmfluid.hotoil", "Hot Crude Oil");
        this.add("hbmfluid.hotoil_ds", "Desulfurized Hot Crude Oil");
        this.add("hbmfluid.hotsteam", "Dense Steam");
        this.add("hbmfluid.hydrogen", "Liquid Hydrogen");
        this.add("hbmfluid.iongel", "Ionic Gel");
        this.add("hbmfluid.kerosene", "Kerosene");
        this.add("hbmfluid.kerosene_reform", "Jet Fuel");
        this.add("hbmfluid.lava", "Lava");
        this.add("hbmfluid.lead", "Liquid Lead");
        this.add("hbmfluid.lead_hot", "Hot Liquid Lead");
        this.add("hbmfluid.lightoil", "Light Oil");
        this.add("hbmfluid.lightoil_crack", "Cracked Light Oil");
        this.add("hbmfluid.lightoil_ds", "Desulfurized Light Oil");
        this.add("hbmfluid.lightoil_vacuum", "Vacuum Light Oil");
        this.add("hbmfluid.lpg", "LPG");
        this.add("hbmfluid.lubricant", "Engine Lubricant");
        this.add("hbmfluid.lye", "Lye");
        this.add("hbmfluid.mercury", "Mercury");
        this.add("hbmfluid.mug", "Mug Root Beer");
        this.add("hbmfluid.mug_hot", "Hot Mug Root Beer");
        this.add("hbmfluid.mustardgas", "Mustard Gas");
        this.add("hbmfluid.naphtha", "Naphtha");
        this.add("hbmfluid.naphtha_coker", "Coker Naphtha");
        this.add("hbmfluid.naphtha_crack", "Cracked Naphtha");
        this.add("hbmfluid.naphtha_ds", "Desulfurized Naphtha");
        this.add("hbmfluid.nitan", "NITAN© 100 Octane Super Fuel");
        this.add("hbmfluid.nitric_acid", "Nitric Acid");
        this.add("hbmfluid.nitroglycerin", "Nitroglycerin");
        this.add("hbmfluid.none", "None");
        this.add("hbmfluid.oil", "Crude Oil");
        this.add("hbmfluid.oil_coker", "Coker Oil");
        this.add("hbmfluid.oil_ds", "Desulfurized Crude Oil");
        this.add("hbmfluid.oxygen", "Liquid Oxygen");
        this.add("hbmfluid.oxyhydrogen", "Oxyhydrogen");
        this.add("hbmfluid.pain", "Pandemonium(III)tantalite Solution");
        this.add("hbmfluid.perfluoromethyl", "Perfluoromethyl");
        this.add("hbmfluid.perfluoromethyl_cold", "Cold Perfluoromethyl");
        this.add("hbmfluid.perfluoromethyl_hot", "Hot Perfluoromethyl");
        this.add("hbmfluid.peroxide", "Hydrogen Peroxide");
        this.add("hbmfluid.petroil", "Petroil");
        this.add("hbmfluid.petroil_leaded", "Leaded Petroil");
        this.add("hbmfluid.petroleum", "Petroleum Gas");
        this.add("hbmfluid.pheromone", "Booster Pheromone");
        this.add("hbmfluid.pheromone_m", "Modified Booster Pheromone");
        this.add("hbmfluid.phosgene", "Phosgene");
        this.add("hbmfluid.plasma_bf", "Balefire Plasma");
        this.add("hbmfluid.plasma_dh3", "Deuterium-Helium-3 Plasma");
        this.add("hbmfluid.plasma_dt", "Deuterium-Tritium Plasma");
        this.add("hbmfluid.plasma_hd", "Hydrogen-Deuterium Plasma");
        this.add("hbmfluid.plasma_ht", "Hydrogen-Tritium Plasma");
        this.add("hbmfluid.plasma_xm", "Helium-4-Oxygen Plasma");
        this.add("hbmfluid.potassium_chloride", "Potassiumchloride Solution");
        this.add("hbmfluid.puf6", "Plutonium Hexafluoride");
        this.add("hbmfluid.radiosolvent", "High-Performance Solvent");
        this.add("hbmfluid.reclaimed", "Reclaimed Industrial Oil");
        this.add("hbmfluid.redmud", "Red Mud");
        this.add("hbmfluid.reformate", "Reformate");
        this.add("hbmfluid.reformgas", "Reformate Gas");
        this.add("hbmfluid.salient", "Salient Green");
        this.add("hbmfluid.sas3", "Schrabidium Trisulfide");
        this.add("hbmfluid.schrabidic", "Schrabidic Acid");
        this.add("hbmfluid.seedslurry", "Seeding Slurry");
        this.add("hbmfluid.slop", "Ore Slop");
        this.add("hbmfluid.smear", "Industrial Oil");
        this.add("hbmfluid.smoke", "Smoke");
        this.add("hbmfluid.smoke_leaded", "Leaded Smoke");
        this.add("hbmfluid.smoke_poison", "Poison Smoke");
        this.add("hbmfluid.sodium", "Liquid Sodium");
        this.add("hbmfluid.sodium_hot", "Hot Liquid Sodium");
        this.add("hbmfluid.sodium_aluminate", "Sodium Aluminate");
        this.add("hbmfluid.solvent", "Solvent");
        this.add("hbmfluid.sourgas", "Sour Gas");
        this.add("hbmfluid.spentsteam", "Low-Pressure Steam");
        this.add("hbmfluid.steam", "Steam");
        this.add("hbmfluid.stellar_flux", "Stellar Flux");
        this.add("hbmfluid.sulfuric_acid", "Sulfuric Acid");
        this.add("hbmfluid.sunfloweroil", "Sunflower Seed Oil");
        this.add("hbmfluid.superhotsteam", "Super Dense Steam");
        this.add("hbmfluid.syngas", "Syngas");
        this.add("hbmfluid.thorium_salt", "Liquid Thorium Salt");
        this.add("hbmfluid.thorium_salt_depleted", "Depleted Liquid Thorium Salt");
        this.add("hbmfluid.thorium_salt_hot", "Hot Liquid Thorium Salt");
        this.add("hbmfluid.tritium", "Tritium");
        this.add("hbmfluid.uf6", "Uranium Hexafluoride");
        this.add("hbmfluid.ultrahotsteam", "Ultra Dense Steam");
        this.add("hbmfluid.unsaturateds", "Unsaturated Hydrocarbons");
        this.add("hbmfluid.vitriol", "Vitriol");
        this.add("hbmfluid.wastefluid", "Liquid Nuclear Waste");
        this.add("hbmfluid.wastegas", "Gaseous Nuclear Waste");
        this.add("hbmfluid.water", "Water");
        this.add("hbmfluid.watz", "Poisonous Mud");
        this.add("hbmfluid.woodoil", "Wood Oil");
        this.add("hbmfluid.xenon", "Xenon Gas");
        this.add("hbmfluid.xpjuice", "Experience Juice");
        this.add("hbmfluid.xylene", "BTX");

        this.add("fluid.trait.highly_corrosive", "[Strongly Corrosive]");
        this.add("fluid.trait.corrosive", "[Corrosive]");
        this.add("fluid.trait.flammable", "[Flammable]");
        this.add("fluid.trait.flammable_provides", "Provides %s TU per bucket");
        this.add("fluid.trait.gaseous", "[Gaseous]");
        this.add("fluid.trait.gaseous_atr", "[Gaseous at Room Temperature]");
        this.add("fluid.trait.liquid", "[Liquid]");
        this.add("fluid.trait.viscous", "[Viscous]");
        this.add("fluid.trait.plasma", "[Plasma]");
        this.add("fluid.trait.amat", "[Antimatter]");
        this.add("fluid.trait.lead_container", "[Requires hazardous material tank to hold]");
        this.add("fluid.trait.delicious", "[Delicious]");
        this.add("fluid.trait.unsiphonable", "[Ignored by siphon]");

        // MISC //
        this.add("message.hbmsntm.loaded", "Loaded world with Hbm's Nuclear Tech Mod: %s for Minecraft 1.21.1!");
        this.add("message.hbmsntm.neo", "Neo Edition");
        this.add("message.hbmsntm.new_version", "New version %s is available! Click %s to download!");
        this.add("message.hbmsntm.click_here", "[here]");

        this.add("itemGroup.parts", "NTM Resources and Parts");
        this.add("itemGroup.control", "NTM Machine Items and Fuel");
        this.add("itemGroup.blocks", "NTM Ores and Blocks");
        this.add("itemGroup.machine", "NTM Machines");
        this.add("itemGroup.nuke", "NTM Bombs");
        this.add("itemGroup.missile", "NTM Missiles and Satellites");
        this.add("itemGroup.consumable", "NTM Consumables and Gear");

        this.add("fluid.pressurized", "Pressurized, use compressor!");

        this.add("info.duck", "Press O to Duck!");
        this.add("info.asbestos", "My lungs are burning.");
        this.add("info.coaldust", "It's hard to breathe here.");

        this.add("trait.asbestos", "Asbestos");
        this.add("trait.blinding", "Blinding");
        this.add("trait.coal", "Coal Dust");
        this.add("trait.digamma", "Digamma Radiation");
        this.add("trait.drop", "Dangerous Drop");
        this.add("trait.explosive", "Flammable / Explosive");
        this.add("trait.furnace", "Worth %s operations in nuclear furnace");
        this.add("trait.heat", "Provides %s HEAT");
        this.add("trait.hlParticle", "Particle Half-Life: %s");
        this.add("trait.hlPlayer", "Player Half-Life: %s");
        this.add("trait.hot", "Pyrophoric / Hot");
        this.add("trait.hydro", "Hydroreactive");
        this.add("trait.radioactive", "Radioactive");
        this.add("trait.radResistance", "Radiation resistance: %s");

        this.add("bomb.detonated", "Detonated successfully!");
        this.add("bomb.triggered", "Triggered successfully!");
        this.add("bomb.launched", "Launched successfully!");
        this.add("bomb.missing_component", "Component missing!");
        this.add("bomb.incompatible", "Device can not be triggered!");
        this.add("bomb.nobomb", "Linked position incompatible or unloaded!");

        this.add("he", "HE"); // hbm energy
        this.add("mb", "mB"); // milibuckets
        this.add("pu", "PU"); // pressure units
        this.add("rad", "RAD"); // radiation
        this.add("drx", "DRX"); // digamma thing
        this.add("t", "t"); // ticks
        this.add("s", "s"); // seconds
        this.add("h", "h"); // hours
        this.add("min", "min");
        this.add("max", "max");

        this.add("autoswitch", "Part of auto switch group \"%s\"$Recipe changes based on first ingredient");
    }

    // HELPERS //
    public void add(DeferredBlock<? extends Block> key, String name) { this.add(key.get(), name); }
    public void add(DeferredItem<? extends Item> key, String name) { this.add(key.get(), name); }

    public void add(DeferredHolder<FluidType, FluidType> key, String name) { this.add(key.get().getDescriptionId(), name); }

    public void add(DeferredBlock<? extends Block> key, String toAppend, String name) { this.add(key.get().getDescriptionId() + toAppend, name); }
    public void add(DeferredItem<? extends Item> key, String toAppend, String name) { this.add(key.get().getDescriptionId() + toAppend, name); }

    public String getName(Enum<?> theEnum) { return "." + theEnum.name().toLowerCase(Locale.US); }
}
