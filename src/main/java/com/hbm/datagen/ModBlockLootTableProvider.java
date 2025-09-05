package com.hbm.datagen;

import com.hbm.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.BRICK_CONCRETE.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_MOSSY.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_BROKEN.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_CRACKED.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_MARKED.get());

        dropSelf(ModBlocks.BRICK_CONCRETE_STAIRS.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get());

        dropSelf(ModBlocks.TEST_BOMB.get());
        dropSelf(ModBlocks.TAINT.get());

        add(ModBlocks.BRICK_CONCRETE_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.BRICK_CONCRETE_SLAB.get()));
        add(ModBlocks.BRICK_CONCRETE_MOSSY_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.BRICK_CONCRETE_SLAB.get()));
        add(ModBlocks.BRICK_CONCRETE_BROKEN_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.BRICK_CONCRETE_SLAB.get()));
        add(ModBlocks.BRICK_CONCRETE_CRACKED_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.BRICK_CONCRETE_SLAB.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
