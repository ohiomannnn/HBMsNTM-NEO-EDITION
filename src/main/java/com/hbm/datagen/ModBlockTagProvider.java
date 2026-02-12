package com.hbm.datagen;

import com.hbm.HBMsNTM;
import com.hbm.blocks.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

// common bullshit
import static net.neoforged.neoforge.common.Tags.Blocks.*;
import static com.hbm.inventory.ModTags.Blocks.*;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HBMsNTM.MODID, existingFileHelper);
    }

    @Override
    @SuppressWarnings("unchecked") // no
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(PLANTS)
                .addTags(
                        BlockTags.FLOWERS,
                        BlockTags.SAPLINGS,
                        BlockTags.CROPS,
                        PUMPKINS
                )
                .add(
                        Blocks.SHORT_GRASS,
                        Blocks.FERN,
                        Blocks.DEAD_BUSH,
                        Blocks.VINE,
                        Blocks.TALL_GRASS,
                        Blocks.LARGE_FERN
                );

        // im probably dumb but i dont know any tags like this
        tag(ACTUALLY_STONE)
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

        tag(GROUND)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.MUD)
                .add(Blocks.MUDDY_MANGROVE_ROOTS)
                .add(Blocks.MANGROVE_ROOTS)
                .add(Blocks.GRAVEL)
                .add(Blocks.DIRT_PATH)
                .add(Blocks.MYCELIUM)
                .add(Blocks.SAND)
                .add(Blocks.DIRT);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
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
                .add(ModBlocks.BRICK_CONCRETE_BROKEN_SLAB.get())

                .add(ModBlocks.SELLAFIELD_SLAKED.get())
                .add(ModBlocks.ORE_SELLAFIELD_EMERALD.get())
                .add(ModBlocks.ORE_SELLAFIELD_DIAMOND.get())

                .add(ModBlocks.MACHINE_FLUID_TANK.get())

                .add(ModBlocks.MACHINE_BATTERY_REDD.get())
                .add(ModBlocks.MACHINE_BATTERY_SOCKET.get())

                .add(ModBlocks.MACHINE_SATLINKER.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(
                        ModBlocks.WASTE_LOG.get(),
                        ModBlocks.WASTE_PLANKS.get(),
                        ModBlocks.FROZEN_LOG.get(),
                        ModBlocks.FROZEN_PLANKS.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(
                        ModBlocks.WASTE_EARTH.get(),
                        ModBlocks.WASTE_MYCELIUM.get(),
                        ModBlocks.WASTE_TRINITITE.get(),
                        ModBlocks.WASTE_TRINITITE_RED.get(),
                        ModBlocks.FROZEN_DIRT.get(),
                        ModBlocks.FROZEN_GRASS.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_HOE)
                .add(
                        ModBlocks.WASTE_LEAVES.get(),
                        ModBlocks.LEAVES_LAYER.get()
                );
    }
}
