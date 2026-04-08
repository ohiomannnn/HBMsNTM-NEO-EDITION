package com.hbm.datagen;

import com.hbm.blocks.ModBlocks;
import com.hbm.items.ICustomModelRegister;
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
        this.basicItem(NtmItems.INGOT_INDUSTRIAL_COPPER.get());
        this.basicItem(NtmItems.INGOT_RED_COPPER.get());
        this.basicItem(NtmItems.INGOT_ADVANCED_ALLOY.get());
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
        this.basicItem(NtmItems.INGOT_WEAPONSTEEL.get());
        this.basicItem(NtmItems.INGOT_SATURNITE.get());
        this.basicItem(NtmItems.INGOT_EUPHEMIUM.get());
        this.basicItem(NtmItems.INGOT_DINEUTRONIUM.get());
        this.basicItem(NtmItems.INGOT_ELECTRONIUM.get());
        this.basicItem(NtmItems.INGOT_SMORE.get());
        this.basicItem(NtmItems.INGOT_OSMIRIDIUM.get());

        this.basicItem(NtmItems.ROD_EMPTY.get());
        this.registerMetaItem(NtmItems.ROD.get());
        this.basicItem(NtmItems.ROD_DUAL_EMPTY.get());
        this.registerMetaItem(NtmItems.ROD_DUAL.get());
        this.basicItem(NtmItems.ROD_QUAD_EMPTY.get());
        this.registerMetaItem(NtmItems.ROD_QUAD.get());

        basicItem(NtmItems.GEIGER_COUNTER.get());
        basicItem(NtmItems.DOSIMETER.get());
        basicItem(NtmItems.DIGAMMA_DIAGNOSTIC.get());
        basicItem(NtmItems.DETONATOR_DE.get());
        basicItem(NtmItems.DETONATOR_DEADMAN.get());
        basicItem(NtmItems.FLINT_AND_BALEFIRE.get());
        handheldItem(NtmItems.REACHER.get());
        handheldItem(NtmItems.DETONATOR.get());
        handheldItem(NtmItems.DETONATOR_ITEM.get());
        handheldItem(NtmItems.SCHRABIDIUM_PICKAXE.get());
        bombCallerItem(NtmItems.BOMB_CALLER_ATOMIC.get());
        bombCallerItem(NtmItems.BOMB_CALLER_CARPET.get());
        bombCallerItem(NtmItems.BOMB_CALLER_NAPALM.get());
        basicItem(NtmItems.DUCK_SPAWN_EGG.get());

        this.basicItem(NtmItems.PELLET_RTG.get());

        this.basicItem(NtmItems.PARTICLE_DIGAMMA.get());
        this.basicItem(NtmItems.PARTICLE_LUTECE.get());

        this.basicItem(NtmItems.CELL_ANTIMATTER.get());

        this.basicItem(NtmItems.BATTERY_CREATIVE.get());

        this.basicItem(NtmItems.SINGULARITY.get());
        this.basicItem(NtmItems.SINGULARITY_COUNTER_RESONANT.get());
        this.basicItem(NtmItems.SINGULARITY_SUPER_HEATED.get());
        this.basicItem(NtmItems.BLACK_HOLE.get());
        this.basicItem(NtmItems.SINGULARITY_SPARK.get());
        this.basicItem(NtmItems.PELLET_ANTIMATTER.get());

        basicItem(NtmItems.BOTTLE_OPENER.get());

        basicItem(NtmItems.FLUID_ICON.get());

        this.layeredItem(NtmItems.FLUID_IDENTIFIER_MULTI.get(), "fluid_identifier_multi", "fluid_identifier_overlay");
        this.basicCustomLayerItem(NtmItems.FLUID_TANK_EMPTY.get(), "fluid_tank");
        this.layeredItem(NtmItems.FLUID_TANK_FULL.get(), "fluid_tank", "fluid_tank_overlay");
        this.basicCustomLayerItem(NtmItems.FLUID_TANK_LEAD_EMPTY.get(), "fluid_tank_lead");
        this.layeredItem(NtmItems.FLUID_TANK_LEAD_FULL.get(), "fluid_tank_lead", "fluid_tank_lead_overlay");
        this.basicCustomLayerItem(NtmItems.FLUID_BARREL_EMPTY.get(), "fluid_barrel_empty");
        this.layeredItem(NtmItems.FLUID_BARREL_FULL.get(), "fluid_barrel", "fluid_barrel_overlay");
        this.basicCustomLayerItem(NtmItems.FLUID_PACK_EMPTY.get(), "fluid_pack");
        this.layeredItem(NtmItems.FLUID_PACK_FULL.get(), "fluid_pack", "fluid_pack_overlay");
        this.basicItem(NtmItems.FLUID_BARREL_INFINITE.get());
        this.basicItem(NtmItems.INF_WATER.get());
        this.basicItem(NtmItems.INF_WATER_MK2.get());

        this.handheldItem(NtmItems.CIGARETTE.get());
        this.handheldItem(NtmItems.CRACKPIPE.get());

        basicItem(NtmItems.CAP_NUKA.get());
        basicItem(NtmItems.CAP_QUANTUM.get());
        basicItem(NtmItems.CAP_SPARKLE.get());

        basicItem(NtmItems.BOTTLE_EMPTY.get());
        basicItem(NtmItems.BOTTLE_NUKA.get());
        basicItem(NtmItems.BOTTLE_CHERRY.get());
        basicItem(NtmItems.BOTTLE_QUANTUM.get());
        basicItem(NtmItems.BOTTLE_SPARKLE.get());

        basicItem(NtmItems.KEY.get());
        basicItem(NtmItems.KEY_RED.get());
        basicItem(NtmItems.KEY_KIT.get());
        basicItem(NtmItems.KEY_FAKE.get());
        basicItem(NtmItems.PIN.get());

        basicItem(NtmItems.ALLOY_HELMET.get());
        basicItem(NtmItems.ALLOY_CHESTPLATE.get());
        basicItem(NtmItems.ALLOY_LEGGINGS.get());
        basicItem(NtmItems.ALLOY_BOOTS.get());

        this.basicItem(NtmItems.SATELLITE_INTERFACE.get());

        this.basicItem(NtmItems.SATELLITE_RADAR.get());
        this.basicItem(NtmItems.SATELLITE_LASER.get());

        handheldItem(NtmItems.ALLOY_PICKAXE.get());

        this.basicItem(NtmItems.DESIGNATOR.get());
        this.handheldItem(NtmItems.DESIGNATOR_RANGE.get());

        this.entityItem(NtmItems.BATTERY_PACK.get(), false);

        this.entityItem(NtmItems.MISSILE_TAINT.get(), true);
        this.entityItem(NtmItems.MISSILE_MICRO.get(), true);
        this.entityItem(NtmItems.MISSILE_BHOLE.get(), true);
        this.entityItem(NtmItems.MISSILE_SCHRABIDIUM.get(), true);
        this.entityItem(NtmItems.MISSILE_EMP.get(), true);
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

        this.basicItem(ModBlocks.GAS_RADON.asItem());
        this.basicItem(ModBlocks.GAS_RADON_DENSE.asItem());
        this.basicItem(ModBlocks.GAS_RADON_TOMB.asItem());
        this.basicItem(ModBlocks.GAS_MELTDOWN.asItem());
        this.basicItem(ModBlocks.GAS_MONOXIDE.asItem());
        this.basicItem(ModBlocks.GAS_ASBESTOS.asItem());
        this.basicItem(ModBlocks.GAS_COAL.asItem());
        this.basicItem(ModBlocks.GAS_FLAMMABLE.asItem());
        this.basicItem(ModBlocks.GAS_EXPLOSIVE.asItem());

        getBuilder(ModBlocks.LEAVES_LAYER.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/layering_1")));
        getBuilder(ModBlocks.FALLOUT.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/fallout")));

        getBuilder(ModBlocks.WASTE_LOG.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/waste_log")));
        getBuilder(ModBlocks.FROZEN_LOG.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/frozen_log")));

        this.registerPolaroid();

        this.registerMetaItem(NtmItems.BATTERY_SC.get());

        this.basicItem(NtmItems.BATTERY_SPARK.get());
        this.basicItem(NtmItems.BATTERY_TRIXITE.get());

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

        this.basicItem(NtmItems.N2_CHARGE.get());

        this.basicItem(NtmItems.EGG_BALEFIRE_SHARD.get());
        this.basicItem(NtmItems.EGG_BALEFIRE.get());

        this.basicItem(NtmItems.NOTHING.get());
    }

    private void registerPolaroid() {
        ItemModelBuilder builder = getBuilder("polaroid");
        for (int i = 1; i <= 18; i++) {
            builder.override()
                    .predicate(NuclearTechMod.withDefaultNamespace("polaroid_id"), i)
                    .model(getBuilder("polaroid_" + i)
                            .parent(getExistingFile(mcLoc("item/generated")))
                            .texture("layer0", modLoc("item/polaroids/polaroid_" + i)))
                    .end();
        }
    }

    private void registerMetaItem(Item item) {
        if (item instanceof ICustomModelRegister modelRegister) {
            ResourceLocation loc = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
            modelRegister.registerModel(this, loc);
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

    public ItemModelBuilder bombCallerItem(Item item) {
        ResourceLocation loc = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        return this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/handheld")).texture("layer0", ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), "item/bomb_caller"));
    }

}