package com.hbm.datagen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.generic.BarbedWireBlock.BarbedWireType;
import com.hbm.blocks.generic.PlushieBlock.PlushieType;
import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Locale;

public class NtmLanguageProvider extends LanguageProvider {

    // helper keys
    private static final String DESC = ".desc";
    private static final String P11 = ".p11";
    private static final String CONTAINER = "container.";

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

        this.add(NtmItems.PELLET_RTG, "Ingot");

        this.add(NtmItems.CELL_EMPTY, "Ingot");
        this.add(NtmItems.CELL_UF6, "Ingot");
        this.add(NtmItems.CELL_PUF6, "Ingot");
        this.add(NtmItems.CELL_ANTIMATTER, "Ingot");
        this.add(NtmItems.CELL_DEUTERIUM, "Ingot");
        this.add(NtmItems.CELL_TRITIUM, "Ingot");
        this.add(NtmItems.CELL_SAS3, "Ingot");
        this.add(NtmItems.CELL_ANTI_SCHARBIDIUM, "Ingot");
        this.add(NtmItems.CELL_BALEFIRE, "Ingot");

        this.add(NtmItems.PARTICLE_DIGAMMA, "Ingot");
        this.add(NtmItems.PARTICLE_LUTECE, "Ingot");

        this.add(NtmItems.SINGULARITY, "Ingot");
        this.add(NtmItems.SINGULARITY_COUNTER_RESONANT, "Ingot");
        this.add(NtmItems.SINGULARITY_SUPER_HEATED, "Ingot");
        this.add(NtmItems.BLACK_HOLE, "Ingot");
        this.add(NtmItems.SINGULARITY_SPARK, "Ingot");
        this.add(NtmItems.PELLET_ANTIMATTER, "Ingot");

        this.add(NtmItems.INF_WATER, "Ingot");
        this.add(NtmItems.INF_WATER_MK2, "Ingot");

        this.add(NtmItems.FLUID_TANK_EMPTY, "Ingot");
        this.add(NtmItems.FLUID_TANK_FULL, "Ingot");
        this.add(NtmItems.FLUID_TANK_LEAD_EMPTY, "Ingot");
        this.add(NtmItems.FLUID_TANK_LEAD_FULL, "Ingot");
        this.add(NtmItems.FLUID_BARREL_EMPTY, "Ingot");
        this.add(NtmItems.FLUID_BARREL_FULL, "Ingot");
        this.add(NtmItems.FLUID_BARREL_INFINITE, "Ingot");

        this.add(NtmItems.FLUID_PACK_EMPTY, "Ingot");
        this.add(NtmItems.FLUID_PACK_FULL, "Ingot");

        this.add(NtmItems.BATTERY_SPARK, "Ingot");
        this.add(NtmItems.BATTERY_TRIXITE, "Ingot");

        this.add(NtmItems.BATTERY_PACK, "Ingot");
        this.add(NtmItems.BATTERY_SC, "Ingot");
        this.add(NtmItems.BATTERY_CREATIVE, "Ingot");

        this.add(NtmItems.BLUEPRINTS, "Ingot");

        this.add(NtmItems.FLUID_ICON, "Ingot");
        this.add(NtmItems.FLUID_IDENTIFIER_MULTI, "Ingot");

        this.add(NtmItems.SCREWDRIVER, "Ingot");
        this.add(NtmItems.SCREWDRIVER_DESH, "Ingot");
        this.add(NtmItems.BLOWTORCH, "Ingot");
        this.add(NtmItems.ACETYLENE_TORCH, "Ingot");

        this.add(NtmItems.ROD_EMPTY, "Ingot");
        this.add(NtmItems.ROD, "Ingot");
        this.add(NtmItems.ROD_DUAL_EMPTY, "Ingot");
        this.add(NtmItems.ROD_DUAL, "Ingot");
        this.add(NtmItems.ROD_QUAD_EMPTY, "Ingot");
        this.add(NtmItems.ROD_QUAD, "Ingot");

        this.add(NtmItems.DUCK_SPAWN_EGG, "Ingot");

        this.add(NtmItems.DESIGNATOR, "Ingot");
        this.add(NtmItems.DESIGNATOR_RANGE, "Ingot");
        this.add(NtmItems.DOSIMETER, "Ingot");
        this.add(NtmItems.GEIGER_COUNTER, "Ingot");
        this.add(NtmItems.DIGAMMA_DIAGNOSTIC, "Ingot");

        this.add(NtmItems.PIN, "Ingot");
        this.add(NtmItems.KEY, "Ingot");
        this.add(NtmItems.KEY_RED, "Ingot");
        this.add(NtmItems.KEY_KIT, "Ingot");
        this.add(NtmItems.KEY_FAKE, "Ingot");

        this.add(NtmItems.MISSILE_TAINT, "Ingot");
        this.add(NtmItems.MISSILE_MICRO, "Ingot");
        this.add(NtmItems.MISSILE_BHOLE, "Ingot");
        this.add(NtmItems.MISSILE_SCHRABIDIUM, "Ingot");
        this.add(NtmItems.MISSILE_EMP, "Ingot");

        this.add(NtmItems.MISSILE_GENERIC, "Ingot");
        this.add(NtmItems.MISSILE_INCENDIARY, "Ingot");
        this.add(NtmItems.MISSILE_CLUSTER, "Ingot");
        this.add(NtmItems.MISSILE_BUSTER, "Ingot");
        this.add(NtmItems.MISSILE_STEALTH, "Ingot");
        this.add(NtmItems.MISSILE_DECOY, "Ingot");

        this.add(NtmItems.MISSILE_STRONG, "Ingot");
        this.add(NtmItems.MISSILE_INCENDIARY_STRONG, "Ingot");
        this.add(NtmItems.MISSILE_CLUSTER_STRONG, "Ingot");
        this.add(NtmItems.MISSILE_BUSTER_STRONG, "Ingot");
        this.add(NtmItems.MISSILE_EMP_STRONG, "Ingot");

        this.add(NtmItems.MISSILE_BURST, "Ingot");
        this.add(NtmItems.MISSILE_INFERNO, "Ingot");
        this.add(NtmItems.MISSILE_RAIN, "Ingot");
        this.add(NtmItems.MISSILE_DRILL, "Ingot");
        this.add(NtmItems.MISSILE_SHUTTLE, "Ingot");

        this.add(NtmItems.MISSILE_NUCLEAR, "Ingot");
        this.add(NtmItems.MISSILE_NUCLEAR_CLUSTER, "Ingot");
        this.add(NtmItems.MISSILE_VOLCANO, "Ingot");
        this.add(NtmItems.MISSILE_DOOMSDAY, "Ingot");
        this.add(NtmItems.MISSILE_DOOMSDAY_RUSTED, "Ingot");

        this.add(NtmItems.SATELLITE_RADAR, "Ingot");
        this.add(NtmItems.SATELLITE_LASER, "Ingot");
        this.add(NtmItems.SATELLITE_INTERFACE, "Ingot");

        this.add(NtmItems.BALEFIRE_AND_STEEL, "Ingot");

        this.add(NtmItems.DRINK, "Ingot");
        this.add(NtmItems.BOTTLE_OPENER, "Ingot");

        this.add(NtmItems.CANNED_CONSERVE, "Ingot");

        this.add(NtmItems.CAP, "Ingot");
        this.add(NtmItems.RING_PULL, "Ingot");
        this.add(NtmItems.CAN_KEY, "Ingot");

        this.add(NtmItems.CHOCOLATE_MILK, "Ingot");
        this.add(NtmItems.CIGARETTE, "Ingot");
        this.add(NtmItems.CRACKPIPE, "Ingot");

        this.add(NtmItems.EARLY_EXPLOSIVE_LENSES, "Ingot");
        this.add(NtmItems.EXPLOSIVE_LENSES, "Ingot");

        this.add(NtmItems.GADGET_WIREING, "Ingot");
        this.add(NtmItems.GADGET_CORE, "Ingot");

        this.add(NtmItems.LITTLE_BOY_SHIELDING, "Ingot");
        this.add(NtmItems.LITTLE_BOY_TARGET, "Ingot");
        this.add(NtmItems.LITTLE_BOY_BULLET, "Ingot");
        this.add(NtmItems.LITTLE_BOY_PROPELLANT, "Ingot");
        this.add(NtmItems.LITTLE_BOY_IGNITER, "Ingot");

        this.add(NtmItems.FAT_MAN_IGNITER, "Ingot");
        this.add(NtmItems.FAT_MAN_CORE, "Ingot");

        this.add(NtmItems.IVY_MIKE_CORE, "Ingot");
        this.add(NtmItems.IVY_MIKE_DEUT, "Ingot");
        this.add(NtmItems.IVY_MIKE_COOLING_UNIT, "Ingot");

        this.add(NtmItems.TSAR_BOMBA_CORE, "Ingot");

        this.add(NtmItems.FLEIJA_IGNITER, "Ingot");
        this.add(NtmItems.FLEIJA_PROPELLANT, "Ingot");
        this.add(NtmItems.FLEIJA_CORE, "Ingot");

        this.add(NtmItems.SOLINIUM_IGNITER, "Ingot");
        this.add(NtmItems.SOLINIUM_PROPELLANT, "Ingot");
        this.add(NtmItems.SOLINIUM_CORE, "Ingot");

        this.add(NtmItems.N2_CHARGE, "Ingot");

        this.add(NtmItems.EGG_BALEFIRE_SHARD, "Ingot");
        this.add(NtmItems.EGG_BALEFIRE, "Ingot");

        this.add(NtmItems.IGNITER, "Ingot");
        this.add(NtmItems.DETONATOR, "Ingot");
        this.add(NtmItems.DETONATOR_MULTI, "Ingot");
        this.add(NtmItems.DETONATOR_LASER, "Ingot");
        this.add(NtmItems.DETONATOR_DEADMAN, "Ingot");
        this.add(NtmItems.DETONATOR_DE, "Ingot");
        this.add(NtmItems.BOMB_CALLER, "Ingot");
        this.add(NtmItems.DEFUSER, "Ingot");
        this.add(NtmItems.REACHER, "Ingot");

        this.add(NtmItems.POLAROID, "Ingot");
        this.add(NtmItems.BURNT_BARK, "Ingot");

        this.add(NtmItems.STARTER_KIT, "Ingot");

        this.add(NtmItems.NOTHING, "Ingot");

        // BLOCKS //
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
        this.add(NtmBlocks.WASTE_LOG, "Broken Concrete Brick Stairs");
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

        this.add(NtmBlocks.NUKE_GADGET, "The Gadget");
        this.add(NtmBlocks.NUKE_LITTLE_BOY, "Little Boy");
        this.add(NtmBlocks.NUKE_FAT_MAN, "Fat Man");
        this.add(NtmBlocks.NUKE_IVY_MIKE, "Ivy Mike");
        this.add(NtmBlocks.NUKE_TSAR_BOMBA, "Tsar Bomba");
        this.add(NtmBlocks.NUKE_PROTOTYPE, "The Prototype");
        this.add(NtmBlocks.NUKE_PROTOTYPE, DESC, "It didn't have to be like this.$ $You monster.");
        this.add(NtmBlocks.NUKE_FLEIJA, "F.L.E.I.J.A.");
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

        /*
         * CONTAINERS
         */
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
        this.add("container.nuke_n2", "N² Mine");
        this.add("container.nuke_fstbmb", "Balefire Bomb");
    }

    // HELPERS //
    public void add(DeferredBlock<? extends Block> key, String name) { this.add(key.get(), name); }
    public void add(DeferredItem<? extends Item> key, String name) { this.add(key.get(), name); }

    public void add(DeferredBlock<? extends Block> key, String toAppend, String name) { this.add(key.get().getDescriptionId() + toAppend, name); }
    public void add(DeferredItem<? extends Item> key, String toAppend, String name) { this.add(key.get().getDescriptionId() + toAppend, name); }

    public String getName(Enum<?> theEnum) { return "." + theEnum.name().toLowerCase(Locale.US); }
}
