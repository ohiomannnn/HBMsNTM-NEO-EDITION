package com.hbm.datagen;

import com.hbm.HBMsNTM;
import com.hbm.items.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.hbm.inventory.ModTags.Items.*;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper helper) {
        super(output, provider, blockTags, HBMsNTM.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        /*
         * TANKS
         */
        this.tag(UNIVERSAL_TANK).add(ModItems.FLUID_TANK_FULL.get());
        this.tag(HAZARD_TANK).add(ModItems.FLUID_TANK_LEAD_FULL.get());
        this.tag(UNIVERSAL_BARREL).add(ModItems.FLUID_BARREL_FULL.get());
    }
}
