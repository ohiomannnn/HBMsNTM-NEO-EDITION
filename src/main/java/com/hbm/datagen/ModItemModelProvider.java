package com.hbm.datagen;

import com.hbm.HBMsNTM;
import com.hbm.blocks.ModBlocks;
import com.hbm.items.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HBMsNTM.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.NOTHING.get());
        basicItem(ModItems.GEIGER_COUNTER.get());
        basicItem(ModItems.DOSIMETER.get());
        basicItem(ModItems.DIGAMMA_DIAGNOSTIC.get());
        basicItem(ModItems.DETONATOR_DE.get());
        basicItem(ModItems.DETONATOR_DEADMAN.get());
        basicItem(ModItems.FLINT_AND_BALEFIRE.get());
        handheldItem(ModItems.REACHER.get());
        handheldItem(ModItems.DETONATOR.get());
        handheldItem(ModItems.MULTI_DETONATOR.get());
        handheldItem(ModItems.SCHRABIDIUM_PICKAXE.get());
        bombCallerItem(ModItems.BOMB_CALLER_ATOMIC.get());
        bombCallerItem(ModItems.BOMB_CALLER_CARPET.get());
        bombCallerItem(ModItems.BOMB_CALLER_NAPALM.get());
        basicItem(ModItems.DUCK_SPAWN_EGG.get());

        this.basicItem(ModItems.PELLET_RTG.get());

        this.basicItem(ModItems.PARTICLE_DIGAMMA.get());
        this.basicItem(ModItems.PARTICLE_LUTECE.get());

        this.basicItem(ModItems.CELL_ANTIMATTER.get());

        this.basicItem(ModItems.BATTERY_CREATIVE.get());

        this.basicItem(ModItems.SINGULARITY.get());
        this.basicItem(ModItems.SINGULARITY_COUNTER_RESONANT.get());
        this.basicItem(ModItems.SINGULARITY_SUPER_HEATED.get());
        this.basicItem(ModItems.BLACK_HOLE.get());
        this.basicItem(ModItems.SINGULARITY_SPARK.get());
        this.basicItem(ModItems.PELLET_ANTIMATTER.get());

        basicItem(ModItems.BOTTLE_OPENER.get());

        basicItem(ModItems.FLUID_ICON.get());

        this.layeredItem(ModItems.FLUID_IDENTIFIER_MULTI.get(), "fluid_identifier_multi", "fluid_identifier_overlay");
        this.basicCustomLayerItem(ModItems.FLUID_TANK_EMPTY.get(), "fluid_tank");
        this.layeredItem(ModItems.FLUID_TANK_FULL.get(), "fluid_tank", "fluid_tank_overlay");
        this.basicCustomLayerItem(ModItems.FLUID_TANK_LEAD_EMPTY.get(), "fluid_tank_lead");
        this.layeredItem(ModItems.FLUID_TANK_LEAD_FULL.get(), "fluid_tank_lead", "fluid_tank_lead_overlay");
        this.basicCustomLayerItem(ModItems.FLUID_BARREL_EMPTY.get(), "fluid_barrel_empty");
        this.layeredItem(ModItems.FLUID_BARREL_FULL.get(), "fluid_barrel", "fluid_barrel_overlay");
        this.basicCustomLayerItem(ModItems.FLUID_PACK_EMPTY.get(), "fluid_pack");
        this.layeredItem(ModItems.FLUID_PACK_FULL.get(), "fluid_pack", "fluid_pack_overlay");
        this.basicItem(ModItems.FLUID_BARREL_INFINITE.get());
        this.basicItem(ModItems.INF_WATER.get());
        this.basicItem(ModItems.INF_WATER_MK2.get());

        basicItem(ModItems.CAP_NUKA.get());
        basicItem(ModItems.CAP_QUANTUM.get());
        basicItem(ModItems.CAP_SPARKLE.get());

        basicItem(ModItems.BOTTLE_EMPTY.get());
        basicItem(ModItems.BOTTLE_NUKA.get());
        basicItem(ModItems.BOTTLE_CHERRY.get());
        basicItem(ModItems.BOTTLE_QUANTUM.get());
        basicItem(ModItems.BOTTLE_SPARKLE.get());

        basicItem(ModItems.KEY.get());
        basicItem(ModItems.KEY_RED.get());
        basicItem(ModItems.KEY_KIT.get());
        basicItem(ModItems.KEY_FAKE.get());
        basicItem(ModItems.PIN.get());

        basicItem(ModItems.ALLOY_HELMET.get());
        basicItem(ModItems.ALLOY_CHESTPLATE.get());
        basicItem(ModItems.ALLOY_LEGGINGS.get());
        basicItem(ModItems.ALLOY_BOOTS.get());

        this.basicItem(ModItems.SATELLITE_INTERFACE.get());

        this.basicItem(ModItems.SATELLITE_RADAR.get());
        this.basicItem(ModItems.SATELLITE_LASER.get());

        handheldItem(ModItems.ALLOY_PICKAXE.get());

        withExistingParent(ModBlocks.GAS_RADON.getId().getPath(), "item/generated").texture("layer0", modLoc("block/gas_radon"));
        withExistingParent(ModBlocks.GAS_RADON_DENSE.getId().getPath(), "item/generated").texture("layer0", modLoc("block/gas_radon_dense"));
        withExistingParent(ModBlocks.GAS_RADON_TOMB.getId().getPath(), "item/generated").texture("layer0", modLoc("block/gas_radon_tomb"));
        withExistingParent(ModBlocks.GAS_MELTDOWN.getId().getPath(), "item/generated").texture("layer0", modLoc("block/gas_meltdown"));
        withExistingParent(ModBlocks.GAS_MONOXIDE.getId().getPath(), "item/generated").texture("layer0", modLoc("block/gas_monoxide"));
        withExistingParent(ModBlocks.GAS_ASBESTOS.getId().getPath(), "item/generated").texture("layer0", modLoc("block/gas_asbestos"));
        withExistingParent(ModBlocks.GAS_COAL.getId().getPath(), "item/generated").texture("layer0", modLoc("block/gas_coal"));
        withExistingParent(ModBlocks.GAS_FLAMMABLE.getId().getPath(), "item/generated").texture("layer0", modLoc("block/gas_flammable"));
        withExistingParent(ModBlocks.GAS_EXPLOSIVE.getId().getPath(), "item/generated").texture("layer0", modLoc("block/gas_explosive"));

        getBuilder(ModBlocks.LEAVES_LAYER.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/layering_1")));
        getBuilder(ModBlocks.FALLOUT.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/fallout")));

        getBuilder(ModBlocks.WASTE_LOG.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/waste_log")));

        ItemModelBuilder builder = getBuilder("polaroid")
                .parent(getExistingFile(mcLoc("item/generated")));
        for (int i = 1; i <= 18; i++) {
            builder.override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "polaroid_id"), i)
                    .model(getBuilder("polaroid_" + i)
                            .parent(getExistingFile(mcLoc("item/generated")))
                            .texture("layer0", modLoc("item/polaroids/polaroid_" + i)))
                    .end();
        }

        this.basicItem(ModItems.EARLY_EXPLOSIVE_LENSES.get());

        this.basicItem(ModItems.GADGET_WIREING.get());
        this.basicItem(ModItems.GADGET_CORE.get());

        this.basicItem(ModItems.LITTLE_BOY_SHIELDING.get());
        this.basicItem(ModItems.LITTLE_BOY_TARGET.get());
        this.basicItem(ModItems.LITTLE_BOY_BULLET.get());
        this.basicItem(ModItems.LITTLE_BOY_PROPELLANT.get());
        this.basicItem(ModItems.LITTLE_BOY_IGNITER.get());

        this.basicItem(ModItems.FAT_MAN_IGNITER.get());
        this.basicItem(ModItems.FAT_MAN_CORE.get());
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


    public ItemModelBuilder bombCallerItem(Item item) {
        ResourceLocation loc = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        return this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/handheld")).texture("layer0", ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), "item/bomb_caller"));
    }

}