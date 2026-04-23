package com.hbm.datagen;

import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.hbm.inventory.ModTags.Items.*;

public class NtmItemTagProvider extends ItemTagsProvider {

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
    }
}
