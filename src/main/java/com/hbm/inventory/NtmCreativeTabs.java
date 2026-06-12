package com.hbm.inventory;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.IMetaItem;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.FluidIDMultiItem;
import com.hbm.items.special.StarterKitItem.KitType;
import com.hbm.main.NuclearTechMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB;

@SuppressWarnings("unused")
public class NtmCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(CREATIVE_MODE_TAB, NuclearTechMod.MODID);

    // ingots, nuggets, wires, machine parts
    public static final Supplier<CreativeModeTab> PARTS = CREATIVE_MODE_TABS.register(
            "parts",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmItems.INGOT_URANIUM.get()))
                    .title(Component.translatable("creative_tab.hbmsntm.parts"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(NtmItems.INGOT_URANIUM.get());
                        output.accept(NtmItems.INGOT_U233.get());
                        output.accept(NtmItems.INGOT_U235.get());
                        output.accept(NtmItems.INGOT_U238.get());
                        output.accept(NtmItems.INGOT_PLUTONIUM.get());
                        output.accept(NtmItems.INGOT_PU238.get());
                        output.accept(NtmItems.INGOT_PU239.get());
                        output.accept(NtmItems.INGOT_PU240.get());
                        output.accept(NtmItems.INGOT_PU241.get());
                        output.accept(NtmItems.INGOT_PU_MIX.get());
                        output.accept(NtmItems.INGOT_AM241.get());
                        output.accept(NtmItems.INGOT_AM242.get());
                        output.accept(NtmItems.INGOT_AM_MIX.get());
                        output.accept(NtmItems.INGOT_NEPTUNIUM.get());
                        output.accept(NtmItems.INGOT_POLONIUM.get());
                        output.accept(NtmItems.INGOT_TECHNETIUM.get());
                        output.accept(NtmItems.INGOT_CO60.get());
                        output.accept(NtmItems.INGOT_SR90.get());
                        output.accept(NtmItems.INGOT_AU198.get());
                        output.accept(NtmItems.INGOT_PB209.get());
                        output.accept(NtmItems.INGOT_RA226.get());
                        output.accept(NtmItems.INGOT_TITANIUM.get());
                        output.accept(NtmItems.INGOT_INDUSTRIAL_COPPER.get());
                        output.accept(NtmItems.INGOT_RED_COPPER.get());
                        output.accept(NtmItems.INGOT_ADVANCED_ALLOY.get());
                        output.accept(NtmItems.INGOT_TUNGSTEN.get());
                        output.accept(NtmItems.INGOT_ALUMINIUM.get());
                        output.accept(NtmItems.INGOT_STEEL.get());
                        output.accept(NtmItems.INGOT_TCALLOY.get());
                        output.accept(NtmItems.INGOT_CDALLOY.get());
                        output.accept(NtmItems.INGOT_BISMUTH_BRONZE.get());
                        output.accept(NtmItems.INGOT_ARSENIC_BRONZE.get());
                        output.accept(NtmItems.INGOT_BSCCO.get());
                        output.accept(NtmItems.INGOT_LEAD.get());
                        output.accept(NtmItems.INGOT_BISMUTH.get());
                        output.accept(NtmItems.INGOT_ARSENIC.get());
                        output.accept(NtmItems.INGOT_CALCIUM.get());
                        output.accept(NtmItems.INGOT_CADMIUM.get());
                        output.accept(NtmItems.INGOT_TANTALIUM.get());
                        output.accept(NtmItems.INGOT_SILICON.get());
                        output.accept(NtmItems.INGOT_NIOBIUM.get());
                        output.accept(NtmItems.INGOT_BERYLLIUM.get());
                        output.accept(NtmItems.INGOT_COBALT.get());
                        output.accept(NtmItems.INGOT_BORON.get());
                        output.accept(NtmItems.INGOT_GRAPHITE.get());
                        output.accept(NtmItems.INGOT_FIREBRICK.get());
                        output.accept(NtmItems.INGOT_DURA_STEEL.get());
                        output.accept(NtmItems.INGOT_POLYMER.get());
                        output.accept(NtmItems.INGOT_BAKELITE.get());
                        output.accept(NtmItems.INGOT_BIORUBBER.get());
                        output.accept(NtmItems.INGOT_RUBBER.get());
                        output.accept(NtmItems.INGOT_PC.get());
                        output.accept(NtmItems.INGOT_PVC.get());
                        output.accept(NtmItems.INGOT_MUD.get());
                        output.accept(NtmItems.INGOT_CTF.get());
                        output.accept(NtmItems.INGOT_SCHRARANIUM.get());
                        output.accept(NtmItems.INGOT_SCHRABIDIUM.get());
                        output.accept(NtmItems.INGOT_SCHRABIDATE.get());
                        output.accept(NtmItems.INGOT_MAGNETIZED_TUNGSTEN.get());
                        output.accept(NtmItems.INGOT_COMBINE_STEEL.get());
                        output.accept(NtmItems.INGOT_SOLINIUM.get());
                        output.accept(NtmItems.INGOT_GH336.get());
                        output.accept(NtmItems.INGOT_URANIUM_FUEL.get());
                        output.accept(NtmItems.INGOT_THORIUM_FUEL.get());
                        output.accept(NtmItems.INGOT_PLUTONIUM_FUEL.get());
                        output.accept(NtmItems.INGOT_NEPTUNIUM_FUEL.get());
                        output.accept(NtmItems.INGOT_MOX_FUEL.get());
                        output.accept(NtmItems.INGOT_AMERICIUM_FUEL.get());
                        output.accept(NtmItems.INGOT_SCHRABIDIUM_FUEL.get());
                        output.accept(NtmItems.INGOT_HES.get());
                        output.accept(NtmItems.INGOT_LES.get());
                        output.accept(NtmItems.INGOT_AUSTRALIUM.get());
                        output.accept(NtmItems.INGOT_LANTHANIUM.get());
                        output.accept(NtmItems.INGOT_ACTINIUM.get());
                        output.accept(NtmItems.INGOT_DESH.get());
                        output.accept(NtmItems.INGOT_FERROURANIUM.get());
                        output.accept(NtmItems.INGOT_STARMETAL.get());
                        output.accept(NtmItems.INGOT_GUNMETAL.get());
                        output.accept(NtmItems.INGOT_WEAPONSTEEL.get());
                        output.accept(NtmItems.INGOT_SATURNITE.get());
                        output.accept(NtmItems.INGOT_EUPHEMIUM.get());
                        output.accept(NtmItems.INGOT_DINEUTRONIUM.get());
                        output.accept(NtmItems.INGOT_ELECTRONIUM.get());
                        output.accept(NtmItems.INGOT_SMORE.get());
                        output.accept(NtmItems.INGOT_OSMIRIDIUM.get());
                    }).build());

    // items that belong in machines, fuels, etc
    public static final Supplier<CreativeModeTab> CONTROL = CREATIVE_MODE_TABS.register(
            "control",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmItems.PELLET_RTG.get()))
                    .withTabsBefore(NuclearTechMod.withDefaultNamespace("parts"))
                    .title(Component.translatable("creative_tab.hbmsntm.control"))
                    .displayItems((itemDisplayParameters, output) -> {
                        addMetaItems(output, NtmItems.BLUEPRINTS.get());
                        output.accept(NtmItems.CELL_EMPTY);
                        output.accept(NtmItems.CELL_UF6);
                        output.accept(NtmItems.CELL_PUF6);
                        output.accept(NtmItems.CELL_DEUTERIUM);
                        output.accept(NtmItems.CELL_TRITIUM);
                        output.accept(NtmItems.CELL_SAS3);
                        output.accept(NtmItems.CELL_ANTIMATTER);
                        output.accept(NtmItems.CELL_ANTI_SCHARBIDIUM);
                        output.accept(NtmItems.CELL_BALEFIRE);

                        output.accept(NtmItems.PARTICLE_DIGAMMA);
                        output.accept(NtmItems.PARTICLE_LUTECE);

                        output.accept(NtmItems.SINGULARITY);
                        output.accept(NtmItems.SINGULARITY_COUNTER_RESONANT);
                        output.accept(NtmItems.SINGULARITY_SUPER_HEATED);
                        output.accept(NtmItems.BLACK_HOLE);
                        output.accept(NtmItems.SINGULARITY_SPARK);
                        output.accept(NtmItems.PELLET_ANTIMATTER);

                        FluidType[] types = Fluids.getInNiceOrder();
                        // tanks
                        output.accept(NtmItems.FLUID_TANK_EMPTY.get());
                        for (int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];
                            int id = type.getID();

                            if (type.hasNoContainer()) continue;
                            if (type.needsLeadContainer()) continue;
                            output.accept(MetaHelper.metaStack(new ItemStack(NtmItems.FLUID_TANK_FULL.get(), 1), id));
                        }
                        // lead tanks
                        output.accept(NtmItems.FLUID_TANK_LEAD_EMPTY.get());
                        for (int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];
                            int id = type.getID();

                            if (type.hasNoContainer()) continue;
                            output.accept(MetaHelper.metaStack(new ItemStack(NtmItems.FLUID_TANK_LEAD_FULL.get(), 1), id));
                        }
                        // barrels
                        output.accept(NtmItems.FLUID_BARREL_EMPTY.get());
                        for (int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];
                            int id = type.getID();

                            if (type.hasNoContainer()) continue;
                            if (type.needsLeadContainer()) continue;
                            output.accept(MetaHelper.metaStack(new ItemStack(NtmItems.FLUID_BARREL_FULL.get(), 1), id));
                        }
                        // fluid packs
                        output.accept(NtmItems.FLUID_PACK_EMPTY.get());
                        for (int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];
                            int id = type.getID();

                            if (type.hasNoContainer()) continue;
                            if (type.needsLeadContainer()) continue;
                            output.accept(MetaHelper.metaStack(new ItemStack(NtmItems.FLUID_PACK_FULL.get(), 1), id));
                        }
                        output.accept(NtmItems.FLUID_BARREL_INFINITE.get());
                        output.accept(NtmItems.INF_WATER.get());
                        output.accept(NtmItems.INF_WATER_MK2.get());

                        addMetaItems(output, NtmItems.BATTERY_PACK.get());
                        addMetaItems(output, NtmItems.BATTERY_SC.get());

                        output.accept(NtmItems.BATTERY_CREATIVE);

                        output.accept(NtmItems.ROD_EMPTY);
                        addMetaItems(output, NtmItems.ROD.get());
                        output.accept(NtmItems.ROD_DUAL_EMPTY);
                        addMetaItems(output, NtmItems.ROD_DUAL.get());
                        output.accept(NtmItems.ROD_QUAD_EMPTY);
                        addMetaItems(output, NtmItems.ROD_QUAD.get());

                        output.accept(NtmItems.REACHER);
                    }).build());

    // templates, siren tracks
    /** SKIP */

    // ore and mineral blocks
    public static final Supplier<CreativeModeTab> BLOCKS = CREATIVE_MODE_TABS.register(
            "blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmBlocks.ORE_URANIUM.get()))
                    .withTabsBefore(NuclearTechMod.withDefaultNamespace("control"))
                    .title(Component.translatable("creative_tab.hbmsntm.blocks"))
                    .displayItems((itemDisplayParameters, output) -> {

                        addMetaItems(output, NtmBlocks.BOBBLEHEAD.asItem());
                        addMetaItems(output, NtmBlocks.PLUSHIE.asItem());

                        output.accept(NtmBlocks.GRAVEL_OBSIDIAN);
                        output.accept(NtmBlocks.GRAVEL_DIAMOND);

                        output.accept(NtmBlocks.ASPHALT);
                        output.accept(NtmBlocks.ASPHALT_LIGHT);

                        output.accept(NtmBlocks.BRICK_CONCRETE);
                        output.accept(NtmBlocks.BRICK_CONCRETE_MOSSY);
                        output.accept(NtmBlocks.BRICK_CONCRETE_CRACKED);
                        output.accept(NtmBlocks.BRICK_CONCRETE_BROKEN);
                        output.accept(NtmBlocks.BRICK_CONCRETE_MARKED);
                        output.accept(NtmBlocks.BRICK_OBSIDIAN);
                        output.accept(NtmBlocks.BRICK_LIGHT);
                        output.accept(NtmBlocks.BRICK_ASBESTOS);
                        output.accept(NtmBlocks.BRICK_FIRE);

                        output.accept(NtmBlocks.BRICK_CONCRETE_SLAB);
                        output.accept(NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB);
                        output.accept(NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB);
                        output.accept(NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB);

                        output.accept(NtmBlocks.BRICK_CONCRETE_STAIRS);
                        output.accept(NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS);
                        output.accept(NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS);
                        output.accept(NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS);

                        addMetaItems(output, NtmBlocks.BARBED_WIRE.asItem());
                        output.accept(NtmBlocks.SPIKES);

                        output.accept(NtmBlocks.WASTE_EARTH);
                        output.accept(NtmBlocks.WASTE_MYCELIUM);
                        output.accept(NtmBlocks.WASTE_TRINITITE);
                        output.accept(NtmBlocks.WASTE_TRINITITE_RED);
                        output.accept(NtmBlocks.WASTE_LOG);
                        output.accept(NtmBlocks.WASTE_LEAVES);
                        output.accept(NtmBlocks.WASTE_PLANKS);
                        output.accept(NtmBlocks.FROZEN_GRASS);
                        output.accept(NtmBlocks.FROZEN_DIRT);
                        output.accept(NtmBlocks.FROZEN_PLANKS);
                        output.accept(NtmBlocks.FROZEN_LOG);
                        output.accept(NtmBlocks.FALLOUT);
                        output.accept(NtmBlocks.LEAVES_LAYER);

                        output.accept(NtmBlocks.SELLAFIELD_SLAKED);
                        output.accept(NtmBlocks.SELLAFIELD_BEDROCK);
                        output.accept(NtmBlocks.ORE_SELLAFIELD_DIAMOND);
                        output.accept(NtmBlocks.ORE_SELLAFIELD_EMERALD);
                    }).build());

    // machines, structure parts
    public static final Supplier<CreativeModeTab> MACHINE = CREATIVE_MODE_TABS.register(
            "machine",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmBlocks.PWR_CONTROLLER.get()))
                    .withTabsBefore(NuclearTechMod.withDefaultNamespace("blocks"))
                    .title(Component.translatable("creative_tab.hbmsntm.machine"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(NtmBlocks.GAS_RADON);
                        output.accept(NtmBlocks.GAS_RADON_DENSE);
                        output.accept(NtmBlocks.GAS_RADON_TOMB);
                        output.accept(NtmBlocks.GAS_MELTDOWN);
                        output.accept(NtmBlocks.GAS_MONOXIDE);
                        output.accept(NtmBlocks.GAS_ASBESTOS);
                        output.accept(NtmBlocks.GAS_COAL);
                        output.accept(NtmBlocks.GAS_FLAMMABLE);
                        output.accept(NtmBlocks.GAS_EXPLOSIVE);

                        output.accept(NtmBlocks.GEIGER);
                        output.accept(NtmBlocks.MACHINE_BATTERY_SOCKET);
                        output.accept(NtmBlocks.MACHINE_BATTERY_REDD);
                        output.accept(NtmBlocks.RED_CABLE);

                        addMetaItems(output, NtmBlocks.FLUID_DUCT_NEO.asItem());

                        output.accept(NtmBlocks.MACHINE_FLUID_TANK);

                        output.accept(NtmBlocks.DECONTAMINATOR);
                        output.accept(NtmBlocks.MACHINE_SATLINKER);

                        FluidType[] types = Fluids.getInNiceOrder();
                        // multi identifiers
                        for (int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];

                            output.accept(FluidIDMultiItem.createStack(type));
                        }
                    }).build());

    // bombs
    public static final Supplier<CreativeModeTab> NUKE = CREATIVE_MODE_TABS.register(
            "nuke",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmBlocks.NUKE_FAT_MAN.get()))
                    .withTabsBefore(NuclearTechMod.withDefaultNamespace("machine"))
                    .title(Component.translatable("creative_tab.hbmsntm.nuke"))
                    .backgroundTexture(ResourceLocation.fromNamespaceAndPath(NuclearTechMod.MODID, "textures/gui/nuke_tab.png"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(NtmBlocks.NUKE_GADGET);
                        output.accept(NtmBlocks.NUKE_LITTLE_BOY);
                        output.accept(NtmBlocks.NUKE_FAT_MAN);
                        output.accept(NtmBlocks.NUKE_IVY_MIKE);
                        output.accept(NtmBlocks.NUKE_TSAR_BOMBA);
                        output.accept(NtmBlocks.NUKE_PROTOTYPE);
                        output.accept(NtmBlocks.NUKE_FLEIJA);
                        output.accept(NtmBlocks.NUKE_N2);
                        output.accept(NtmBlocks.NUKE_FSTBMB);

                        addMetaItems(output, NtmBlocks.CRASHED_BOMB.asItem());

                        output.accept(NtmBlocks.DYNAMITE);
                        output.accept(NtmBlocks.TNT);
                        output.accept(NtmBlocks.SEMTEX);
                        output.accept(NtmBlocks.C4);
                        output.accept(NtmBlocks.FISSURE_BOMB);

                        output.accept(NtmBlocks.MINE_AP);
                        output.accept(NtmBlocks.MINE_SHRAP);
                        output.accept(NtmBlocks.MINE_HE);
                        output.accept(NtmBlocks.MINE_FAT);
                        output.accept(NtmBlocks.MINE_NAVAL);

                        output.accept(NtmBlocks.DET_CORD);
                        output.accept(NtmBlocks.DET_CHARGE);
                        output.accept(NtmBlocks.DET_NUKE);
                        output.accept(NtmBlocks.DET_MINER);

                        output.accept(NtmBlocks.BARREL_RED);
                        output.accept(NtmBlocks.BARREL_PINK);
                        output.accept(NtmBlocks.BARREL_LOX);
                        output.accept(NtmBlocks.BARREL_TAINT);

                        addMetaItems(output, NtmBlocks.VOLCANO_CORE.asItem());
                        addMetaItems(output, NtmBlocks.VOLCANO_RAD_CORE.asItem());

                        output.accept(NtmItems.BATTERY_SPARK);
                        output.accept(NtmItems.BATTERY_TRIXITE);

                        output.accept(NtmItems.EARLY_EXPLOSIVE_LENSES);
                        output.accept(NtmItems.EXPLOSIVE_LENSES);

                        output.accept(NtmItems.GADGET_WIREING);
                        output.accept(NtmItems.GADGET_CORE);

                        output.accept(NtmItems.LITTLE_BOY_SHIELDING);
                        output.accept(NtmItems.LITTLE_BOY_TARGET);
                        output.accept(NtmItems.LITTLE_BOY_BULLET);
                        output.accept(NtmItems.LITTLE_BOY_PROPELLANT);
                        output.accept(NtmItems.LITTLE_BOY_IGNITER);

                        output.accept(NtmItems.FAT_MAN_CORE);
                        output.accept(NtmItems.FAT_MAN_IGNITER);

                        output.accept(NtmItems.IVY_MIKE_CORE);
                        output.accept(NtmItems.IVY_MIKE_DEUT);
                        output.accept(NtmItems.IVY_MIKE_COOLING_UNIT);

                        output.accept(NtmItems.TSAR_BOMBA_CORE);

                        output.accept(NtmItems.FLEIJA_IGNITER);
                        output.accept(NtmItems.FLEIJA_PROPELLANT);
                        output.accept(NtmItems.FLEIJA_CORE);

                        output.accept(NtmItems.N2_CHARGE);

                        output.accept(NtmItems.EGG_BALEFIRE_SHARD);
                        output.accept(NtmItems.EGG_BALEFIRE);

                        output.accept(NtmItems.IGNITER);
                        output.accept(NtmItems.DETONATOR);
                        output.accept(NtmItems.DETONATOR_MULTI);
                        output.accept(NtmItems.DETONATOR_LASER);
                        output.accept(NtmItems.DETONATOR_DEADMAN);
                        output.accept(NtmItems.DETONATOR_DE);

                        // this is sucks
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.GADGET));
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.LITTLE_BOY));
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.FAT_MAN));
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.IVY_MIKE));
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.TSAR_BOMBA));
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.PROTOTYPE));
                        output.accept(MetaHelper.newStack(NtmItems.STARTER_KIT, KitType.FLEIJA));
                    }).build());

    // missiles, satellites
    public static final Supplier<CreativeModeTab> MISSILE = CREATIVE_MODE_TABS.register(
            "missile",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmItems.MISSILE_DOOMSDAY.get()))
                    .withTabsBefore(NuclearTechMod.withDefaultNamespace("nuke"))
                    .title(Component.translatable("creative_tab.hbmsntm.missile"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(NtmBlocks.LAUNCH_PAD);

                        output.accept(NtmItems.DESIGNATOR);
                        output.accept(NtmItems.DESIGNATOR_RANGE);

                        output.accept(NtmItems.MISSILE_TAINT);
                        output.accept(NtmItems.MISSILE_MICRO);
                        output.accept(NtmItems.MISSILE_BHOLE);
                        output.accept(NtmItems.MISSILE_SCHRABIDIUM);
                        output.accept(NtmItems.MISSILE_EMP);
                        output.accept(NtmItems.MISSILE_GENERIC);
                        output.accept(NtmItems.MISSILE_DECOY);
                        output.accept(NtmItems.MISSILE_INCENDIARY);
                        output.accept(NtmItems.MISSILE_CLUSTER);
                        output.accept(NtmItems.MISSILE_BUSTER);
                        output.accept(NtmItems.MISSILE_STEALTH);
                        output.accept(NtmItems.MISSILE_STRONG);
                        output.accept(NtmItems.MISSILE_INCENDIARY_STRONG);
                        output.accept(NtmItems.MISSILE_CLUSTER_STRONG);
                        output.accept(NtmItems.MISSILE_BUSTER_STRONG);
                        output.accept(NtmItems.MISSILE_EMP_STRONG);
                        output.accept(NtmItems.MISSILE_BURST);
                        output.accept(NtmItems.MISSILE_INFERNO);
                        output.accept(NtmItems.MISSILE_RAIN);
                        output.accept(NtmItems.MISSILE_DRILL);
                        output.accept(NtmItems.MISSILE_SHUTTLE);
                        output.accept(NtmItems.MISSILE_NUCLEAR);
                        output.accept(NtmItems.MISSILE_NUCLEAR_CLUSTER);
                        output.accept(NtmItems.MISSILE_VOLCANO);
                        output.accept(NtmItems.MISSILE_DOOMSDAY);
                        output.accept(NtmItems.MISSILE_DOOMSDAY_RUSTED);

                        output.accept(NtmItems.SATELLITE_RADAR);
                        output.accept(NtmItems.SATELLITE_LASER);
                        output.accept(NtmItems.SATELLITE_INTERFACE);
                    }).build());

    // turrets, weapons, ammo
    /** SKIP */

    // drinks, kits, tools
    public static final Supplier<CreativeModeTab> CONSUMABLE = CREATIVE_MODE_TABS.register(
            "consumable",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NtmItems.BOTTLE_NUKA.get()))
                    .withTabsBefore(NuclearTechMod.withDefaultNamespace("missile"))
                    .title(Component.translatable("creative_tab.hbmsntm.consumable"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(NtmItems.DUCK_SPAWN_EGG);

                        output.accept(NtmItems.DOSIMETER);
                        output.accept(NtmItems.GEIGER_COUNTER);
                        output.accept(NtmItems.DIGAMMA_DIAGNOSTIC);

                        output.accept(NtmItems.KEY);
                        output.accept(NtmItems.KEY_KIT);
                        output.accept(NtmItems.KEY_FAKE);
                        output.accept(NtmItems.PIN);
                        output.accept(NtmItems.FLINT_AND_BALEFIRE);
                        output.accept(NtmItems.POLAROID);

                        output.accept(NtmItems.BOTTLE_OPENER);
                        output.accept(NtmItems.BOTTLE_NUKA);
                        output.accept(NtmItems.BOTTLE_CHERRY);
                        output.accept(NtmItems.BOTTLE_QUANTUM);
                        output.accept(NtmItems.BOTTLE_SPARKLE);
                        output.accept(NtmItems.CAP_NUKA);
                        output.accept(NtmItems.CAP_QUANTUM);
                        output.accept(NtmItems.CAP_SPARKLE);

                        output.accept(NtmItems.CIGARETTE);
                        output.accept(NtmItems.CRACKPIPE);

                        output.accept(NtmItems.BOMB_CALLER_CARPET);
                        output.accept(NtmItems.BOMB_CALLER_NAPALM);
                        output.accept(NtmItems.BOMB_CALLER_ATOMIC);
                    }).build());

    private static void addMetaItems(CreativeModeTab.Output output, Item item) {
        if(item instanceof IMetaItem metaItem) {
            List<ItemStack> stacks = new ArrayList<>();
            metaItem.getSubItems(item, stacks);

            for(ItemStack stack : stacks) {
                output.accept(stack);
            }
        }
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
