package com.hbm.datagen;

import com.hbm.HBMsNTM;
import com.hbm.blocks.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HBMsNTM.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.WASTE_LOG.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.BRICK_CONCRETE.get())
                .add(ModBlocks.BRICK_CONCRETE_MOSSY.get())
                .add(ModBlocks.BRICK_CONCRETE_CRACKED.get())
                .add(ModBlocks.BRICK_CONCRETE_BROKEN.get())
                .add(ModBlocks.BRICK_CONCRETE_MARKED.get())

                .add(ModBlocks.BRICK_CONCRETE_STAIRS.get())
                .add(ModBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get())
                .add(ModBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get())
                .add(ModBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get())

                .add(ModBlocks.BRICK_CONCRETE_SLAB.get())
                .add(ModBlocks.BRICK_CONCRETE_MOSSY_SLAB.get())
                .add(ModBlocks.BRICK_CONCRETE_CRACKED_SLAB.get())
                .add(ModBlocks.BRICK_CONCRETE_BROKEN_SLAB.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.BRICK_CONCRETE.get())
                .add(ModBlocks.BRICK_CONCRETE_MOSSY.get())
                .add(ModBlocks.BRICK_CONCRETE_CRACKED.get())
                .add(ModBlocks.BRICK_CONCRETE_BROKEN.get())
                .add(ModBlocks.BRICK_CONCRETE_MARKED.get())

                .add(ModBlocks.BRICK_CONCRETE_STAIRS.get())
                .add(ModBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get())
                .add(ModBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get())
                .add(ModBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get())

                .add(ModBlocks.BRICK_CONCRETE_SLAB.get())
                .add(ModBlocks.BRICK_CONCRETE_MOSSY_SLAB.get())
                .add(ModBlocks.BRICK_CONCRETE_CRACKED_SLAB.get())
                .add(ModBlocks.BRICK_CONCRETE_BROKEN_SLAB.get());

    }
}
