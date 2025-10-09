package com.hbm.datagen;

import com.hbm.HBMsNTM;
import com.hbm.blocks.ModBlocks;
import com.hbm.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HBMsNTM.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // im probably dumb but i dont know any tags like this
        tag(ModTags.Blocks.ACTUALLY_STONE)
                .add(Blocks.COAL_ORE)
                .add(Blocks.IRON_ORE)
                .add(Blocks.COPPER_ORE)
                .add(Blocks.LAPIS_ORE)
                .add(Blocks.DIAMOND_ORE)

                .add(Blocks.DEEPSLATE_COAL_ORE)
                .add(Blocks.DEEPSLATE_IRON_ORE)
                .add(Blocks.DEEPSLATE_COPPER_ORE)
                .add(Blocks.DEEPSLATE_LAPIS_ORE)
                .add(Blocks.DEEPSLATE_DIAMOND_ORE)

                .add(Blocks.GRANITE)
                .add(Blocks.ANDESITE)
                .add(Blocks.DIORITE)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.TUFF)
                .add(Blocks.COBBLESTONE)
                .add(Blocks.SANDSTONE)

                .add(ModBlocks.BRICK_CONCRETE.get())
                .add(ModBlocks.BRICK_CONCRETE_CRACKED.get())
                .add(ModBlocks.BRICK_CONCRETE_BROKEN.get())
                .add(ModBlocks.BRICK_CONCRETE_MOSSY.get())
                .add(ModBlocks.BRICK_CONCRETE_MARKED.get())
                .add(ModBlocks.BRICK_LIGHT.get())

                .add(Blocks.STONE);

        tag(ModTags.Blocks.GROUND)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.MUD)
                .add(Blocks.MUDDY_MANGROVE_ROOTS)
                .add(Blocks.MANGROVE_ROOTS)
                .add(Blocks.GRAVEL)
                .add(Blocks.DIRT_PATH)
                .add(Blocks.MYCELIUM)
                .add(Blocks.SAND)
                .add(Blocks.DIRT);

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
