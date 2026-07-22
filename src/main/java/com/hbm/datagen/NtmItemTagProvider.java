package com.hbm.datagen;

import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.hbm.inventory.NtmTags.Items.*;

public class NtmItemTagProvider extends ItemTagsProvider {

    private static final TagKey<Item> ENCHANTABLE_DIGGER = ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "enchantable/digger"));
    private static final TagKey<Item> ENCHANTABLE_PICKAXE = ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "enchantable/pickaxe"));
    private static final TagKey<Item> ENCHANTABLE_AXE = ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "enchantable/axe"));
    private static final TagKey<Item> ENCHANTABLE_SHOVEL = ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "enchantable/shovel"));
    private static final TagKey<Item> ENCHANTABLE_HOE = ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "enchantable/hoe"));
    private static final TagKey<Item> PICKAXES = ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "pickaxes"));
    private static final TagKey<Item> AXES = ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "axes"));
    private static final TagKey<Item> SHOVELS = ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "shovels"));
    private static final TagKey<Item> HOES = ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "hoes"));
    private static final TagKey<Item> ENCHANTABLE_MINING = ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "enchantable/mining"));
    private static final TagKey<Item> ENCHANTABLE_MINING_LOOT = ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "enchantable/mining_loot"));

    public NtmItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper helper) {
        super(output, provider, blockTags, NuclearTechMod.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        /*
         * TANKS
         */
        this.tag(UNIVERSAL_TANK).add(NtmItems.FLUID_TANK_FULL.get());
        this.tag(HAZARD_TANK).add(NtmItems.FLUID_TANK_LEAD_FULL.get());
        this.tag(UNIVERSAL_BARREL).add(NtmItems.FLUID_BARREL_FULL.get());

        /*
         * TOOLS
         */
        this.tag(PICKAXES).add(
                NtmItems.STEEL_PICKAXE.get(),
                NtmItems.TITANIUM_PICKAXE.get(),
                NtmItems.DESH_PICKAXE.get(),
                NtmItems.COBALT_PICKAXE.get(),
                NtmItems.COBALT_DECORATED_PICKAXE.get(),
                NtmItems.CMB_PICKAXE.get(),
                NtmItems.BISMUTH_PICKAXE.get(),
                NtmItems.STARMETAL_PICKAXE.get(),
                NtmItems.SCHRABIDIUM_PICKAXE.get(),
                NtmItems.MESE_PICKAXE.get(),
                NtmItems.VOLCANIC_PICKAXE.get(),
                NtmItems.CHLOROPHYTE_PICKAXE.get()
        );

        this.tag(AXES).add(
                NtmItems.STEEL_AXE.get(),
                NtmItems.TITANIUM_AXE.get(),
                NtmItems.DESH_AXE.get(),
                NtmItems.COBALT_AXE.get(),
                NtmItems.COBALT_DECORATED_AXE.get(),
                NtmItems.CMB_AXE.get(),
                NtmItems.BISMUTH_AXE.get(),
                NtmItems.STARMETAL_AXE.get(),
                NtmItems.SCHRABIDIUM_AXE.get(),
                NtmItems.MESE_AXE.get(),
                NtmItems.VOLCANIC_AXE.get(),
                NtmItems.CHLOROPHYTE_AXE.get()
        );

        this.tag(SHOVELS).add(
                NtmItems.STEEL_SHOVEL.get(),
                NtmItems.TITANIUM_SHOVEL.get(),
                NtmItems.DESH_SHOVEL.get(),
                NtmItems.COBALT_SHOVEL.get(),
                NtmItems.COBALT_DECORATED_SHOVEL.get(),
                NtmItems.CMB_SHOVEL.get(),
                NtmItems.STARMETAL_SHOVEL.get(),
                NtmItems.SCHRABIDIUM_SHOVEL.get()
        );

        this.tag(HOES).add(
                NtmItems.STEEL_HOE.get(),
                NtmItems.TITANIUM_HOE.get(),
                NtmItems.DESH_HOE.get(),
                NtmItems.COBALT_HOE.get(),
                NtmItems.COBALT_DECORATED_HOE.get(),
                NtmItems.CMB_HOE.get(),
                NtmItems.STARMETAL_HOE.get(),
                NtmItems.SCHRABIDIUM_HOE.get()
        );

        this.tag(ENCHANTABLE_MINING).addTag(PICKAXES).addTag(AXES).addTag(SHOVELS).addTag(HOES);
        this.tag(ENCHANTABLE_MINING_LOOT).addTag(PICKAXES).addTag(AXES).addTag(SHOVELS).addTag(HOES);

        this.tag(ENCHANTABLE_DIGGER).add(
                NtmItems.STEEL_PICKAXE.get(),
                NtmItems.STEEL_AXE.get(),
                NtmItems.STEEL_SHOVEL.get(),
                NtmItems.STEEL_HOE.get(),
                NtmItems.TITANIUM_PICKAXE.get(),
                NtmItems.TITANIUM_AXE.get(),
                NtmItems.TITANIUM_SHOVEL.get(),
                NtmItems.TITANIUM_HOE.get(),
                NtmItems.DESH_PICKAXE.get(),
                NtmItems.DESH_AXE.get(),
                NtmItems.DESH_SHOVEL.get(),
                NtmItems.DESH_HOE.get(),
                NtmItems.COBALT_PICKAXE.get(),
                NtmItems.COBALT_AXE.get(),
                NtmItems.COBALT_SHOVEL.get(),
                NtmItems.COBALT_HOE.get(),
                NtmItems.COBALT_DECORATED_PICKAXE.get(),
                NtmItems.COBALT_DECORATED_AXE.get(),
                NtmItems.COBALT_DECORATED_SHOVEL.get(),
                NtmItems.COBALT_DECORATED_HOE.get(),
                NtmItems.CMB_PICKAXE.get(),
                NtmItems.CMB_AXE.get(),
                NtmItems.CMB_SHOVEL.get(),
                NtmItems.CMB_HOE.get(),
                NtmItems.BISMUTH_PICKAXE.get(),
                NtmItems.BISMUTH_AXE.get(),
                NtmItems.STARMETAL_PICKAXE.get(),
                NtmItems.STARMETAL_AXE.get(),
                NtmItems.STARMETAL_SHOVEL.get(),
                NtmItems.STARMETAL_HOE.get(),
                NtmItems.SCHRABIDIUM_PICKAXE.get(),
                NtmItems.SCHRABIDIUM_AXE.get(),
                NtmItems.SCHRABIDIUM_SHOVEL.get(),
                NtmItems.SCHRABIDIUM_HOE.get(),
                NtmItems.MESE_PICKAXE.get(),
                NtmItems.MESE_AXE.get(),
                NtmItems.VOLCANIC_PICKAXE.get(),
                NtmItems.VOLCANIC_AXE.get(),
                NtmItems.CHLOROPHYTE_PICKAXE.get(),
                NtmItems.CHLOROPHYTE_AXE.get()
        );

        this.tag(ENCHANTABLE_PICKAXE).add(
                NtmItems.STEEL_PICKAXE.get(),
                NtmItems.TITANIUM_PICKAXE.get(),
                NtmItems.DESH_PICKAXE.get(),
                NtmItems.COBALT_PICKAXE.get(),
                NtmItems.COBALT_DECORATED_PICKAXE.get(),
                NtmItems.CMB_PICKAXE.get(),
                NtmItems.BISMUTH_PICKAXE.get(),
                NtmItems.STARMETAL_PICKAXE.get(),
                NtmItems.SCHRABIDIUM_PICKAXE.get(),
                NtmItems.MESE_PICKAXE.get(),
                NtmItems.VOLCANIC_PICKAXE.get(),
                NtmItems.CHLOROPHYTE_PICKAXE.get()
        );

        this.tag(ENCHANTABLE_AXE).add(
                NtmItems.STEEL_AXE.get(),
                NtmItems.TITANIUM_AXE.get(),
                NtmItems.DESH_AXE.get(),
                NtmItems.COBALT_AXE.get(),
                NtmItems.COBALT_DECORATED_AXE.get(),
                NtmItems.CMB_AXE.get(),
                NtmItems.BISMUTH_AXE.get(),
                NtmItems.STARMETAL_AXE.get(),
                NtmItems.SCHRABIDIUM_AXE.get(),
                NtmItems.MESE_AXE.get(),
                NtmItems.VOLCANIC_AXE.get(),
                NtmItems.CHLOROPHYTE_AXE.get()
        );

        this.tag(ENCHANTABLE_SHOVEL).add(
                NtmItems.STEEL_SHOVEL.get(),
                NtmItems.TITANIUM_SHOVEL.get(),
                NtmItems.DESH_SHOVEL.get(),
                NtmItems.COBALT_SHOVEL.get(),
                NtmItems.COBALT_DECORATED_SHOVEL.get(),
                NtmItems.CMB_SHOVEL.get(),
                NtmItems.STARMETAL_SHOVEL.get(),
                NtmItems.SCHRABIDIUM_SHOVEL.get()
        );

        this.tag(ENCHANTABLE_HOE).add(
                NtmItems.STEEL_HOE.get(),
                NtmItems.TITANIUM_HOE.get(),
                NtmItems.DESH_HOE.get(),
                NtmItems.COBALT_HOE.get(),
                NtmItems.COBALT_DECORATED_HOE.get(),
                NtmItems.CMB_HOE.get(),
                NtmItems.STARMETAL_HOE.get(),
                NtmItems.SCHRABIDIUM_HOE.get()
        );
    }
}
