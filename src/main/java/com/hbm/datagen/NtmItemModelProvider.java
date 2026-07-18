package com.hbm.datagen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.items.ICustomItemModelRegister;
import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class NtmItemModelProvider extends ItemModelProvider {

    public NtmItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NuclearTechMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        NtmItems.ITEMS.getEntries().forEach(holder -> {
            Item item = holder.get();

            if(item instanceof ICustomItemModelRegister icimr) {
                ResourceLocation loc = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
                icimr.registerItemModel(this, loc);
            }
        });

        this.basicItem(NtmItems.INGOT_URANIUM.get());
        this.basicItem(NtmItems.INGOT_U233.get());
        this.basicItem(NtmItems.INGOT_U235.get());
        this.basicItem(NtmItems.INGOT_U238.get());
        this.basicItem(NtmItems.INGOT_U238M2.get());
        this.basicItem(NtmItems.INGOT_PLUTONIUM.get());
        this.basicItem(NtmItems.INGOT_PU238.get());
        this.basicItem(NtmItems.INGOT_PU239.get());
        this.basicItem(NtmItems.INGOT_PU240.get());
        this.basicItem(NtmItems.INGOT_PU241.get());
        this.basicItem(NtmItems.INGOT_PU_MIX.get());
        this.basicItem(NtmItems.INGOT_AM241.get());
        this.basicItem(NtmItems.INGOT_AM242.get());
        this.basicItem(NtmItems.INGOT_AM_MIX.get());
        this.basicItem(NtmItems.INGOT_NEPTUNIUM.get());
        this.basicItem(NtmItems.INGOT_POLONIUM.get());
        this.basicItem(NtmItems.INGOT_TECHNETIUM.get());
        this.basicItem(NtmItems.INGOT_CO60.get());
        this.basicItem(NtmItems.INGOT_SR90.get());
        this.basicItem(NtmItems.INGOT_AU198.get());
        this.basicItem(NtmItems.INGOT_PB209.get());
        this.basicItem(NtmItems.INGOT_RA226.get());
        this.basicItem(NtmItems.INGOT_TITANIUM.get());
        this.basicItem(NtmItems.INGOT_GRADE_COPPER.get());
        this.basicItem(NtmItems.INGOT_TUNGSTEN.get());
        this.basicItem(NtmItems.INGOT_ALUMINIUM.get());
        this.basicItem(NtmItems.INGOT_STEEL.get());
        this.basicItem(NtmItems.INGOT_TCALLOY.get());
        this.basicItem(NtmItems.INGOT_CDALLOY.get());
        this.basicItem(NtmItems.INGOT_BISMUTH_BRONZE.get());
        this.basicItem(NtmItems.INGOT_ARSENIC_BRONZE.get());
        this.basicItem(NtmItems.INGOT_BSCCO.get());
        this.basicItem(NtmItems.INGOT_LEAD.get());
        this.basicItem(NtmItems.INGOT_BISMUTH.get());
        this.basicItem(NtmItems.INGOT_ARSENIC.get());
        this.basicItem(NtmItems.INGOT_CALCIUM.get());
        this.basicItem(NtmItems.INGOT_CADMIUM.get());
        this.basicItem(NtmItems.INGOT_TANTALIUM.get());
        this.basicItem(NtmItems.INGOT_SILICON.get());
        this.basicItem(NtmItems.INGOT_NIOBIUM.get());
        this.basicItem(NtmItems.INGOT_BERYLLIUM.get());
        this.basicItem(NtmItems.INGOT_COBALT.get());
        this.basicItem(NtmItems.INGOT_BORON.get());
        this.basicItem(NtmItems.INGOT_GRAPHITE.get());
        this.basicItem(NtmItems.INGOT_FIREBRICK.get());
        this.basicItem(NtmItems.INGOT_DURA_STEEL.get());
        this.basicItem(NtmItems.INGOT_POLYMER.get());
        this.basicItem(NtmItems.INGOT_BAKELITE.get());
        this.basicItem(NtmItems.INGOT_BIORUBBER.get());
        this.basicItem(NtmItems.INGOT_RUBBER.get());
        this.basicItem(NtmItems.INGOT_PC.get());
        this.basicItem(NtmItems.INGOT_PVC.get());
        this.basicItem(NtmItems.INGOT_MUD.get());
        this.basicItem(NtmItems.INGOT_CTF.get());
        this.basicItem(NtmItems.INGOT_SCHRARANIUM.get());
        this.basicItem(NtmItems.INGOT_SCHRABIDIUM.get());
        this.basicItem(NtmItems.INGOT_SCHRABIDATE.get());
        this.basicItem(NtmItems.INGOT_MAGNETIZED_TUNGSTEN.get());
        this.basicItem(NtmItems.INGOT_COMBINE_STEEL.get());
        this.basicItem(NtmItems.INGOT_SOLINIUM.get());
        this.basicItem(NtmItems.INGOT_GH336.get());
        this.basicItem(NtmItems.INGOT_URANIUM_FUEL.get());
        this.basicItem(NtmItems.INGOT_THORIUM_FUEL.get());
        this.basicItem(NtmItems.INGOT_PLUTONIUM_FUEL.get());
        this.basicItem(NtmItems.INGOT_NEPTUNIUM_FUEL.get());
        this.basicItem(NtmItems.INGOT_MOX_FUEL.get());
        this.basicItem(NtmItems.INGOT_AMERICIUM_FUEL.get());
        this.basicItem(NtmItems.INGOT_SCHRABIDIUM_FUEL.get());
        this.basicItem(NtmItems.INGOT_HES.get());
        this.basicItem(NtmItems.INGOT_LES.get());
        this.basicItem(NtmItems.INGOT_AUSTRALIUM.get());
        this.basicItem(NtmItems.INGOT_LANTHANIUM.get());
        this.basicItem(NtmItems.INGOT_ACTINIUM.get());
        this.basicItem(NtmItems.INGOT_DESH.get());
        this.basicItem(NtmItems.INGOT_FERROURANIUM.get());
        this.basicItem(NtmItems.INGOT_STARMETAL.get());
        this.basicItem(NtmItems.INGOT_GUNMETAL.get());
        this.basicItem(NtmItems.INGOT_WEAPON_STEEL.get());
        this.basicItem(NtmItems.INGOT_SATURNITE.get());
        this.basicItem(NtmItems.INGOT_EUPHEMIUM.get());
        this.basicItem(NtmItems.INGOT_DINEUTRONIUM.get());
        this.basicItem(NtmItems.INGOT_ELECTRONIUM.get());
        this.basicItem(NtmItems.INGOT_SMORE.get());
        this.basicItem(NtmItems.INGOT_OSMIRIDIUM.get());
        this.basicItem(NtmItems.INGOT_ZIRCONIUM.get());
        this.basicItem(NtmItems.NUGGET_URANIUM.get());
        this.basicItem(NtmItems.NUGGET_U233.get());
        this.basicItem(NtmItems.NUGGET_U235.get());
        this.basicItem(NtmItems.NUGGET_U238.get());
        this.basicItem(NtmItems.NUGGET_U238M2.get());
        this.basicItem(NtmItems.NUGGET_PLUTONIUM.get());
        this.basicItem(NtmItems.NUGGET_PU238.get());
        this.basicItem(NtmItems.NUGGET_PU239.get());
        this.basicItem(NtmItems.NUGGET_PU240.get());
        this.basicItem(NtmItems.NUGGET_PU241.get());
        this.basicItem(NtmItems.NUGGET_PU_MIX.get());
        this.basicItem(NtmItems.NUGGET_AM241.get());
        this.basicItem(NtmItems.NUGGET_AM242.get());
        this.basicItem(NtmItems.NUGGET_AM_MIX.get());
        this.basicItem(NtmItems.NUGGET_TECHNETIUM.get());
        this.basicItem(NtmItems.NUGGET_NEPTUNIUM.get());
        this.basicItem(NtmItems.NUGGET_POLONIUM.get());
        this.basicItem(NtmItems.NUGGET_THORIUM_FUEL.get());
        this.basicItem(NtmItems.NUGGET_URANIUM_FUEL.get());
        this.basicItem(NtmItems.NUGGET_MOX_FUEL.get());
        this.basicItem(NtmItems.NUGGET_PLUTONIUM_FUEL.get());
        this.basicItem(NtmItems.NUGGET_NEPTUNIUM_FUEL.get());
        this.basicItem(NtmItems.NUGGET_AMERICIUM_FUEL.get());
        this.basicItem(NtmItems.NUGGET_SCHRABIDIUM_FUEL.get());
        this.basicItem(NtmItems.NUGGET_HES.get());
        this.basicItem(NtmItems.NUGGET_LES.get());
        this.basicItem(NtmItems.NUGGET_LEAD.get());
        this.basicItem(NtmItems.NUGGET_BERYLLIUM.get());
        this.basicItem(NtmItems.NUGGET_CADMIUM.get());
        this.basicItem(NtmItems.NUGGET_BISMUTH.get());
        this.basicItem(NtmItems.NUGGET_ARSENIC.get());
        this.basicItem(NtmItems.NUGGET_ZIRCONIUM.get());
        this.basicItem(NtmItems.NUGGET_TANTALIUM.get());
        this.basicItem(NtmItems.NUGGET_DESH.get());
        this.basicItem(NtmItems.NUGGET_OSMIRIDIUM.get());
        this.basicItem(NtmItems.NUGGET_SCHRABIDIUM.get());
        this.basicItem(NtmItems.NUGGET_SOLINIUM.get());
        this.basicItem(NtmItems.NUGGET_EUPHEMIUM.get());
        this.basicItem(NtmItems.NUGGET_DINEUTRONIUM.get());
        this.basicItem(NtmItems.NUGGET_NIOBIUM.get());
        this.basicItem(NtmItems.NUGGET_SILICON.get());
        this.basicItem(NtmItems.NUGGET_ACTINIUM.get());
        this.basicItem(NtmItems.NUGGET_COBALT.get());
        this.basicItem(NtmItems.NUGGET_CO60.get());
        this.basicItem(NtmItems.NUGGET_SR90.get());
        this.basicItem(NtmItems.NUGGET_PB209.get());
        this.basicItem(NtmItems.NUGGET_GH336.get());
        this.basicItem(NtmItems.NUGGET_AU198.get());
        this.basicItem(NtmItems.NUGGET_RA226.get());
        this.basicItem(NtmItems.POWDER_IRON.get());
        this.basicItem(NtmItems.POWDER_GOLD.get());
        this.basicItem(NtmItems.POWDER_DIAMOND.get());
        this.basicItem(NtmItems.POWDER_EMERALD.get());
        this.basicItem(NtmItems.POWDER_LAPIS.get());
        this.basicItem(NtmItems.POWDER_TITANIUM.get());
        this.basicItem(NtmItems.POWDER_TUNGSTEN.get());
        this.basicItem(NtmItems.POWDER_COPPER.get());
        this.basicItem(NtmItems.POWDER_BERYLLIUM.get());
        this.basicItem(NtmItems.POWDER_ALUMINIUM.get());
        this.basicItem(NtmItems.POWDER_LEAD.get());
        this.basicItem(NtmItems.POWDER_STEEL.get());
        this.basicItem(NtmItems.POWDER_COMBINE_STEEL.get());
        this.basicItem(NtmItems.POWDER_QUARTZ.get());
        this.basicItem(NtmItems.POWDER_SCHRABIDIUM.get());
        this.basicItem(NtmItems.POWDER_ASBESTOS.get());
        this.basicItem(NtmItems.POWDER_GRADE_COPPER.get());
        this.basicItem(NtmItems.POWDER_DURA_STEEL.get());
        this.basicItem(NtmItems.POWDER_POLYMER.get());
        this.basicItem(NtmItems.POWDER_BAKELITE.get());
        this.basicItem(NtmItems.POWDER_DESH.get());
        this.basicItem(NtmItems.POWDER_LITHIUM.get());
        this.basicItem(NtmItems.POWDER_COBALT.get());
        this.basicItem(NtmItems.POWDER_DINEUTRONIUM.get());
        this.basicItem(NtmItems.POWDER_COAL.get());
        this.basicItem(NtmItems.POWDER_BISMUTH.get());
        this.basicItem(NtmItems.POWDER_LIGNITE.get());
        this.basicItem(NtmItems.POWDER_ZIRCONIUM.get());
        this.basicItem(NtmItems.POWDER_URANIUM.get());
        this.basicItem(NtmItems.POWDER_NIOBIUM.get());
        this.basicItem(NtmItems.FRAGMENT_NIOBIUM.get());
        this.basicItem(NtmItems.FRAGMENT_COBALT.get());
        this.basicItem(NtmItems.FRAGMENT_NEODYMIUM.get());
        this.basicItem(NtmItems.FRAGMENT_CERIUM.get());
        this.basicItem(NtmItems.FRAGMENT_BORON.get());
        this.basicItem(NtmItems.FRAGMENT_LANTHANIUM.get());
        this.basicItem(NtmItems.FRAGMENT_ACTINIUM.get());
        this.basicItem(NtmItems.FRAGMENT_METEORITE.get());

        this.basicItem(NtmItems.OIL_TAR_CRUDE.get());
        this.basicItem(NtmItems.OIL_TAR_CRACK.get());
        this.basicItem(NtmItems.OIL_TAR_COAL.get());
        this.basicItem(NtmItems.OIL_TAR_PARAFFIN.get());
        this.basicItem(NtmItems.SULFUR.get());
        this.basicItem(NtmItems.ASBESTOS_SHEET.get());
        this.basicItem(NtmItems.CINNABAR.get());
        this.basicItem(NtmItems.FRAGMENT_COBALT.get());
        this.basicItem(NtmItems.FLUORITE.get());
        this.basicItem(NtmItems.LIGNITE.get());
        this.basicItem(NtmItems.NITER.get());
        this.basicItem(NtmItems.RARE_EARTH_ORE_CHUNK.get());
        this.basicItem(NtmItems.PLATE_IRON.get());
        this.basicItem(NtmItems.PLATE_GOLD.get());
        this.basicItem(NtmItems.PLATE_TITANIUM.get());
        this.basicItem(NtmItems.PLATE_ALUMINIUM.get());
        this.basicItem(NtmItems.PLATE_STEEL.get());
        this.basicItem(NtmItems.PLATE_LEAD.get());
        this.basicItem(NtmItems.PLATE_COPPER.get());
        this.basicItem(NtmItems.PLATE_GUNMETAL.get());
        this.basicItem(NtmItems.PLATE_WEAPON_STEEL.get());
        this.basicItem(NtmItems.PLATE_SATURNITE.get());
        this.basicItem(NtmItems.PLATE_DURA_STEEL.get());
        this.basicItem(NtmItems.PLATE_SCHRABIDIUM.get());
        this.basicItem(NtmItems.PLATE_COMBINE_STEEL.get());
        this.basicItem(NtmItems.PLATE_BISMUTH.get());
        this.basicItem(NtmItems.INSULATOR.get());
        this.basicItem(NtmItems.WIRE_GOLD.get());
        this.basicItem(NtmItems.WIRE_COPPER.get());
        this.basicItem(NtmItems.WIRE_ALUMINIUM.get());
        this.basicItem(NtmItems.WIRE_ZIRCONIUM.get());
        this.basicItem(NtmItems.WIRE_LEAD.get());
        this.basicItem(NtmItems.WIRE_TUNGSTEN.get());
        this.basicItem(NtmItems.WIRE_SCHRABIDIUM.get());
        this.basicItem(NtmItems.WIRE_STEEL.get());
        this.basicItem(NtmItems.WIRE_MAGNETIZED_TUNGSTEN.get());
        this.basicItem(NtmItems.WIRE_CARBON.get());
        this.basicItem(NtmItems.WIRE_GRADE_COPPER.get());
        this.basicItem(NtmItems.SHELL_TITANIUM.get());
        this.basicItem(NtmItems.SHELL_ALUMINIUM.get());
        this.basicItem(NtmItems.SHELL_COPPER.get());
        this.basicItem(NtmItems.SHELL_STEEL.get());
        this.basicItem(NtmItems.SHELL_WEAPON_STEEL.get());
        this.basicItem(NtmItems.SHELL_SATURNITE.get());
        this.basicItem(NtmItems.PIPE_IRON.get());
        this.basicItem(NtmItems.PIPE_COPPER.get());
        this.basicItem(NtmItems.PIPE_ALUMINIUM.get());
        this.basicItem(NtmItems.PIPE_LEAD.get());
        this.basicItem(NtmItems.PIPE_STEEL.get());
        this.basicItem(NtmItems.PIPE_DURA_STEEL.get());
        this.basicItem(NtmItems.PIPE_RUBBER.get());
        this.basicItem(NtmItems.CIRCUIT_PRINTED_BOARD.get());
        this.basicItem(NtmItems.CIRCUIT_ANALOG_BOARD.get());
        this.basicItem(NtmItems.CIRCUIT_INTEGRATED_BOARD.get());
        this.basicItem(NtmItems.CIRCUIT_MILITARY_GRADE_BOARD.get());
        this.basicItem(NtmItems.CIRCUIT_VERSATILE_INTEGRATED.get());
        this.basicItem(NtmItems.CIRCUIT_VERSATILE_BOARD.get());
        this.basicItem(NtmItems.CIRCUIT_CAPACITOR.get());
        this.basicItem(NtmItems.CIRCUIT_TANTALIUM_CAPACITOR.get());
        this.basicItem(NtmItems.CIRCUIT_CAPACITOR_BOARD.get());
        this.basicItem(NtmItems.CIRCUIT_VACUUM_TUBE.get());
        this.basicItem(NtmItems.CIRCUIT_PRINTED_SILICON_WAFER.get());
        this.basicItem(NtmItems.CIRCUIT_MICROCHIP.get());
        this.basicItem(NtmItems.CIRCUIT_CONTROL_UNIT_CASING.get());
        this.basicItem(NtmItems.CIRCUIT_CONTROL_UNIT.get());
        this.basicItem(NtmItems.CIRCUIT_ADVANCED_CONTROL_UNIT.get());
        this.basicItem(NtmItems.CIRCUIT_SOLID_STATE_QUANTUM_PROCESSOR.get());
        this.basicItem(NtmItems.CIRCUIT_QUANTUM_PROCESSING_UNIT.get());
        this.basicItem(NtmItems.CIRCUIT_QUANTUM_COMPUTER.get());
        this.basicItem(NtmItems.CIRCUIT_ATOMIC_CLOCK.get());
        this.basicItem(NtmItems.COIL_COPPER.get());
        this.basicItem(NtmItems.COIL_COPPER_RING.get());
        this.basicItem(NtmItems.COIL_GOLD.get());
        this.basicItem(NtmItems.COIL_GOLD_RING.get());
        this.basicItem(NtmItems.MOTOR.get());
        this.basicItem(NtmItems.CAST_PLATE_WELDED.get());
        this.basicItem(NtmItems.CAST_PLATE.get());

        this.basicItem(NtmItems.PELLET_RTG.get());

        this.basicItem(NtmItems.CELL_EMPTY.get());
        this.basicItem(NtmItems.CELL_UF6.get());
        this.basicItem(NtmItems.CELL_PUF6.get());
        this.basicItem(NtmItems.CELL_ANTIMATTER.get());
        this.basicItem(NtmItems.CELL_DEUTERIUM.get());
        this.basicItem(NtmItems.CELL_TRITIUM.get());
        this.basicItem(NtmItems.CELL_SAS3.get());
        this.basicItem(NtmItems.CELL_ANTI_SCHARBIDIUM.get());
        this.basicItem(NtmItems.CELL_BALEFIRE.get());

        this.basicItem(NtmItems.PARTICLE_DIGAMMA.get());
        this.basicItem(NtmItems.PARTICLE_LUTECE.get());

        this.basicItem(NtmItems.SINGULARITY.get());
        this.basicItem(NtmItems.SINGULARITY_COUNTER_RESONANT.get());
        this.basicItem(NtmItems.SINGULARITY_SUPER_HEATED.get());
        this.basicItem(NtmItems.BLACK_HOLE.get());
        this.basicItem(NtmItems.SINGULARITY_SPARK.get());
        this.basicItem(NtmItems.PELLET_ANTIMATTER.get());

        this.basicItem(NtmItems.INF_WATER.get());
        this.basicItem(NtmItems.INF_WATER_MK2.get());

        this.basicCustomLayerItem(NtmItems.FLUID_TANK_EMPTY.get(), "fluid_tank");
        this.layeredItem(NtmItems.FLUID_TANK_FULL.get(), "fluid_tank", "fluid_tank_overlay");
        this.basicCustomLayerItem(NtmItems.FLUID_TANK_LEAD_EMPTY.get(), "fluid_tank_lead");
        this.layeredItem(NtmItems.FLUID_TANK_LEAD_FULL.get(), "fluid_tank_lead", "fluid_tank_lead_overlay");
        this.basicCustomLayerItem(NtmItems.FLUID_BARREL_EMPTY.get(), "fluid_barrel_empty");
        this.layeredItem(NtmItems.FLUID_BARREL_FULL.get(), "fluid_barrel", "fluid_barrel_overlay");
        this.basicItem(NtmItems.FLUID_BARREL_INFINITE.get());

        this.basicCustomLayerItem(NtmItems.FLUID_PACK_EMPTY.get(), "fluid_pack");
        this.layeredItem(NtmItems.FLUID_PACK_FULL.get(), "fluid_pack", "fluid_pack_overlay");

        this.basicItem(NtmItems.BATTERY_SPARK.get());
        this.basicItem(NtmItems.BATTERY_TRIXITE.get());

        this.entityItem(NtmItems.BATTERY_PACK.get(), false);
        // BATTERY_SC uses ICustomItemModelRegister
        this.basicItem(NtmItems.BATTERY_CREATIVE.get());

        // todo register BLUEPRINTS

        this.basicItem(NtmItems.STAMP_STONE_FLAT.get());
        this.basicItem(NtmItems.STAMP_STONE_PLATE.get());
        this.basicItem(NtmItems.STAMP_STONE_WIRE.get());
        this.basicItem(NtmItems.STAMP_STONE_CIRCUIT.get());
        this.basicItem(NtmItems.STAMP_IRON_FLAT.get());
        this.basicItem(NtmItems.STAMP_IRON_PLATE.get());
        this.basicItem(NtmItems.STAMP_IRON_WIRE.get());
        this.basicItem(NtmItems.STAMP_IRON_CIRCUIT.get());
        this.basicItem(NtmItems.STAMP_STEEL_FLAT.get());
        this.basicItem(NtmItems.STAMP_STEEL_PLATE.get());
        this.basicItem(NtmItems.STAMP_STEEL_WIRE.get());
        this.basicItem(NtmItems.STAMP_STEEL_CIRCUIT.get());
        this.basicItem(NtmItems.STAMP_TITANIUM_FLAT.get());
        this.basicItem(NtmItems.STAMP_TITANIUM_PLATE.get());
        this.basicItem(NtmItems.STAMP_TITANIUM_WIRE.get());
        this.basicItem(NtmItems.STAMP_TITANIUM_CIRCUIT.get());
        this.basicItem(NtmItems.STAMP_OBSIDIAN_FLAT.get());
        this.basicItem(NtmItems.STAMP_OBSIDIAN_PLATE.get());
        this.basicItem(NtmItems.STAMP_OBSIDIAN_WIRE.get());
        this.basicItem(NtmItems.STAMP_OBSIDIAN_CIRCUIT.get());
        this.basicItem(NtmItems.STAMP_DESH_FLAT.get());
        this.basicItem(NtmItems.STAMP_DESH_PLATE.get());
        this.basicItem(NtmItems.STAMP_DESH_WIRE.get());
        this.basicItem(NtmItems.STAMP_DESH_CIRCUIT.get());

        this.basicItem(NtmItems.FLUID_ICON.get());
        this.layeredItem(NtmItems.FLUID_IDENTIFIER_MULTI.get(), "fluid_identifier_multi", "fluid_identifier_overlay");

        this.handheldItem(NtmItems.SCREWDRIVER.get());
        this.handheldItem(NtmItems.SCREWDRIVER_DESH.get());
        this.handheldItem(NtmItems.BLOWTORCH.get());
        this.handheldItem(NtmItems.ACETYLENE_TORCH.get());

        this.basicItem(NtmItems.ROD_EMPTY.get());
        this.basicItem(NtmItems.ROD_DUAL_EMPTY.get());
        this.basicItem(NtmItems.ROD_QUAD_EMPTY.get());

        this.basicItem(NtmItems.SPAWN_DUCK.get());

        this.basicItem(NtmItems.DESIGNATOR.get());
        this.handheldItem(NtmItems.DESIGNATOR_RANGE.get());
        this.basicItem(NtmItems.DOSIMETER.get());
        this.basicItem(NtmItems.GEIGER_COUNTER.get());
        this.basicItem(NtmItems.DIGAMMA_DIAGNOSTIC.get());

        this.basicItem(NtmItems.PIN.get());
        this.basicItem(NtmItems.KEY.get());
        this.basicItem(NtmItems.KEY_RED.get());
        this.basicItem(NtmItems.KEY_RED_CRACKED.get());
        this.basicItem(NtmItems.KEY_KIT.get());
        this.basicItem(NtmItems.KEY_FAKE.get());
        this.basicItem(NtmItems.LAUNCH_CODE_PIECE.get());
        this.basicItem(NtmItems.LAUNCH_CODE.get());
        this.basicItem(NtmItems.LAUNCH_KEY.get());

        this.entityItem(NtmItems.MISSILE_TAINT.get(), true);
        this.entityItem(NtmItems.MISSILE_MICRO.get(), true);
        this.entityItem(NtmItems.MISSILE_BHOLE.get(), true);
        this.entityItem(NtmItems.MISSILE_SCHRABIDIUM.get(), true);
        this.entityItem(NtmItems.MISSILE_EMP.get(), true);
        this.entityItem(NtmItems.MISSILE_GENERIC.get(), true);
        this.entityItem(NtmItems.MISSILE_INCENDIARY.get(), true);
        this.entityItem(NtmItems.MISSILE_CLUSTER.get(), true);
        this.entityItem(NtmItems.MISSILE_BUSTER.get(), true);
        this.entityItem(NtmItems.MISSILE_DECOY.get(), true);
        this.entityItem(NtmItems.MISSILE_STRONG.get(), true);
        this.entityItem(NtmItems.MISSILE_INCENDIARY_STRONG.get(), true);
        this.entityItem(NtmItems.MISSILE_CLUSTER_STRONG.get(), true);
        this.entityItem(NtmItems.MISSILE_BUSTER_STRONG.get(), true);
        this.entityItem(NtmItems.MISSILE_EMP_STRONG.get(), true);
        this.entityItem(NtmItems.MISSILE_BURST.get(), true);
        this.entityItem(NtmItems.MISSILE_INFERNO.get(), true);
        this.entityItem(NtmItems.MISSILE_RAIN.get(), true);
        this.entityItem(NtmItems.MISSILE_DRILL.get(), true);
        this.entityItem(NtmItems.MISSILE_NUCLEAR.get(), true);
        this.entityItem(NtmItems.MISSILE_NUCLEAR_CLUSTER.get(), true);
        this.entityItem(NtmItems.MISSILE_VOLCANO.get(), true);
        this.entityItem(NtmItems.MISSILE_DOOMSDAY.get(), true);
        this.entityItem(NtmItems.MISSILE_DOOMSDAY_RUSTED.get(), true);
        this.entityItem(NtmItems.MISSILE_SHUTTLE.get(), true);
        this.entityItem(NtmItems.MISSILE_STEALTH.get(), true);
        this.entityItem(NtmItems.MISSILE_ANTI_BALLISTIC.get(), true);

        // MISSILE_SOYUZ uses ICustomItemModelRegister

        this.basicItem(NtmItems.SATELLITE_RADAR.get());
        this.basicItem(NtmItems.SATELLITE_LASER.get());
        this.basicItem(NtmItems.SATELLITE_INTERFACE.get());

        this.basicItem(NtmItems.BALEFIRE_AND_STEEL.get());

        // DRINK uses ICustomItemModelRegister
        this.handheldItem(NtmItems.BOTTLE_OPENER.get());

        // CANNED_CONSERVE uses ICustomItemModelRegister

        // CAP uses ICustomItemModelRegister
        this.basicItem(NtmItems.RING_PULL.get());
        this.basicItem(NtmItems.CAN_KEY.get());

        this.basicItem(NtmItems.CHOCOLATE_MILK.get());
        this.handheldItem(NtmItems.CIGARETTE.get());
        this.handheldItem(NtmItems.CRACKPIPE.get());

        this.basicItem(NtmItems.EARLY_EXPLOSIVE_LENSES.get());
        this.basicItem(NtmItems.EXPLOSIVE_LENSES.get());

        this.basicItem(NtmItems.GADGET_WIREING.get());
        this.basicItem(NtmItems.GADGET_CORE.get());

        this.basicItem(NtmItems.LITTLE_BOY_SHIELDING.get());
        this.basicItem(NtmItems.LITTLE_BOY_TARGET.get());
        this.basicItem(NtmItems.LITTLE_BOY_BULLET.get());
        this.basicItem(NtmItems.LITTLE_BOY_PROPELLANT.get());
        this.basicItem(NtmItems.LITTLE_BOY_IGNITER.get());

        this.basicItem(NtmItems.FAT_MAN_IGNITER.get());
        this.basicItem(NtmItems.FAT_MAN_CORE.get());

        this.basicItem(NtmItems.IVY_MIKE_CORE.get());
        this.basicItem(NtmItems.IVY_MIKE_DEUT.get());
        this.basicItem(NtmItems.IVY_MIKE_COOLING_UNIT.get());

        this.basicItem(NtmItems.TSAR_BOMBA_CORE.get());

        this.basicItem(NtmItems.FLEIJA_IGNITER.get());
        this.basicItem(NtmItems.FLEIJA_PROPELLANT.get());
        this.basicItem(NtmItems.FLEIJA_CORE.get());

        this.basicItem(NtmItems.SOLINIUM_IGNITER.get());
        this.basicItem(NtmItems.SOLINIUM_PROPELLANT.get());
        this.basicItem(NtmItems.SOLINIUM_CORE.get());

        this.basicItem(NtmItems.N2_CHARGE.get());

        this.basicItem(NtmItems.EGG_BALEFIRE_SHARD.get());
        this.basicItem(NtmItems.EGG_BALEFIRE.get());

        this.handheldItem(NtmItems.IGNITER.get());
        this.handheldItem(NtmItems.DETONATOR.get());
        this.handheldItem(NtmItems.DETONATOR_MULTI.get());
        this.entityItem(NtmItems.DETONATOR_LASER.get(), true);
        this.handheldItem(NtmItems.DETONATOR_DEADMAN.get());
        this.handheldItem(NtmItems.DETONATOR_DE.get());
        this.handheldItem(NtmItems.BOMB_CALLER.get());
        this.handheldItem(NtmItems.DEFUSER.get());
        this.handheldItem(NtmItems.REACHER.get());
        this.handheldItem(NtmItems.DRILL_TITANIUM.get());

        this.handheldItem(NtmItems.UPGRADE_TEMPLATE.get());
        this.handheldItem(NtmItems.UPGRADE_SPEED_1.get());
        this.handheldItem(NtmItems.UPGRADE_SPEED_2.get());
        this.handheldItem(NtmItems.UPGRADE_SPEED_3.get());
        this.handheldItem(NtmItems.UPGRADE_EFFECT_1.get());
        this.handheldItem(NtmItems.UPGRADE_EFFECT_2.get());
        this.handheldItem(NtmItems.UPGRADE_EFFECT_3.get());
        this.handheldItem(NtmItems.UPGRADE_POWER_1.get());
        this.handheldItem(NtmItems.UPGRADE_POWER_2.get());
        this.handheldItem(NtmItems.UPGRADE_POWER_3.get());
        this.handheldItem(NtmItems.UPGRADE_FORTUNE_1.get());
        this.handheldItem(NtmItems.UPGRADE_FORTUNE_2.get());
        this.handheldItem(NtmItems.UPGRADE_FORTUNE_3.get());
        this.handheldItem(NtmItems.UPGRADE_AFTERBURN_1.get());
        this.handheldItem(NtmItems.UPGRADE_AFTERBURN_2.get());
        this.handheldItem(NtmItems.UPGRADE_AFTERBURN_3.get());
        this.handheldItem(NtmItems.UPGRADE_RADIUS.get());
        this.handheldItem(NtmItems.UPGRADE_HEALTH.get());
        this.handheldItem(NtmItems.UPGRADE_OVERDRIVE_1.get());
        this.handheldItem(NtmItems.UPGRADE_OVERDRIVE_2.get());
        this.handheldItem(NtmItems.UPGRADE_OVERDRIVE_3.get());

        this.registerPolaroid();
        this.basicItem(NtmItems.BURNT_BARK.get());

        // STARTER_KIT uses ICustomItemModelRegister

        this.basicItem(NtmItems.TEMPLATE_FOLDER.get());
        this.basicItem(NtmItems.NOTHING.get());

        // welp
        this.getBuilder(NtmBlocks.BASALT.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/basalt")));
        this.getBuilder(NtmBlocks.LEAVES_LAYER.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/leaves_layer_1")));
        this.getBuilder(NtmBlocks.OIL_SPILL.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/oil_spill_1")));
        this.getBuilder(NtmBlocks.FALLOUT.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/fallout")));
        this.getBuilder(NtmBlocks.WASTE_LOG.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/waste_log")));
        this.getBuilder(NtmBlocks.FROZEN_LOG.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/frozen_log")));
    }

    private void registerPolaroid() {
        ItemModelBuilder builder = getBuilder("polaroid");
        for (int i = 1; i <= 18; i++) {
            builder.override()
                    .predicate(NuclearTechMod.withDefaultNamespace("polaroid_id"), i)
                    .model(getBuilder("polaroid_" + i)
                            .parent(getExistingFile(mcLoc("item/generated")))
                            .texture("layer0", modLoc("item/polaroid_" + i)))
                    .end();
        }
    }

    private ItemModelBuilder layeredItem(Item item, String layer0, String layer1) {
        return this.getBuilder(BuiltInRegistries.ITEM.getKey(item).toString()).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/" + layer0))
                .texture("layer1", modLoc("item/" + layer1));
    }

    private ItemModelBuilder basicCustomLayerItem(Item item, String layer0) {
        return this.getBuilder(BuiltInRegistries.ITEM.getKey(item).toString()).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/" + layer0));
    }

    private void entityItem(Item item, boolean frontLight) {
        this.getBuilder(BuiltInRegistries.ITEM.getKey(item).toString()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).guiLight(frontLight ? BlockModel.GuiLight.FRONT : BlockModel.GuiLight.SIDE);
    }

}
