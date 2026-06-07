package com.hbm.datagen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.main.NuclearTechMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.hbm.inventory.ModTags.Blocks.*;
import static net.neoforged.neoforge.common.Tags.Blocks.PUMPKINS;

// common bullshit

public class NtmBlockTagProvider extends BlockTagsProvider {

    public NtmBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, NuclearTechMod.MODID, existingFileHelper);
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

                .add(NtmBlocks.BRICK_CONCRETE.get())
                .add(NtmBlocks.BRICK_CONCRETE_CRACKED.get())
                .add(NtmBlocks.BRICK_CONCRETE_BROKEN.get())
                .add(NtmBlocks.BRICK_CONCRETE_MOSSY.get())
                .add(NtmBlocks.BRICK_CONCRETE_MARKED.get())
                .add(NtmBlocks.BRICK_LIGHT.get())

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
                .add(
                        NtmBlocks.ASPHALT.get(),
                        NtmBlocks.ASPHALT_LIGHT.get(),

                        NtmBlocks.BRICK_CONCRETE.get(),
                        NtmBlocks.BRICK_CONCRETE_MOSSY.get(),
                        NtmBlocks.BRICK_CONCRETE_CRACKED.get(),
                        NtmBlocks.BRICK_CONCRETE_BROKEN.get(),
                        NtmBlocks.BRICK_CONCRETE_MARKED.get(),

                        NtmBlocks.BARBED_WIRE.get(),
                        NtmBlocks.SPIKES.get(),

                        NtmBlocks.BRICK_CONCRETE_STAIRS.get(),
                        NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get(),
                        NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get(),
                        NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get(),

                        NtmBlocks.BRICK_CONCRETE_SLAB.get(),
                        NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB.get(),
                        NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB.get(),
                        NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB.get(),

                        NtmBlocks.NUKE_GADGET.get(),
                        NtmBlocks.NUKE_LITTLE_BOY.get(),
                        NtmBlocks.NUKE_FAT_MAN.get(),
                        NtmBlocks.NUKE_IVY_MIKE.get(),
                        NtmBlocks.NUKE_TSAR_BOMBA.get(),
                        NtmBlocks.NUKE_PROTOTYPE.get(),
                        NtmBlocks.NUKE_FLEIJA.get(),
                        NtmBlocks.NUKE_N2.get(),
                        NtmBlocks.NUKE_FSTBMB.get(),

                        NtmBlocks.SELLAFIELD_SLAKED.get(),
                        NtmBlocks.ORE_SELLAFIELD_EMERALD.get(),
                        NtmBlocks.ORE_SELLAFIELD_DIAMOND.get(),

                        NtmBlocks.MACHINE_FLUID_TANK.get(),
                        NtmBlocks.MACHINE_BATTERY_REDD.get(),
                        NtmBlocks.MACHINE_BATTERY_SOCKET.get(),
                        NtmBlocks.LAUNCH_PAD.get(),
                        NtmBlocks.MACHINE_SATLINKER.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(
                        NtmBlocks.WASTE_LOG.get(),
                        NtmBlocks.WASTE_PLANKS.get(),
                        NtmBlocks.FROZEN_LOG.get(),
                        NtmBlocks.FROZEN_PLANKS.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(
                        NtmBlocks.WASTE_EARTH.get(),
                        NtmBlocks.WASTE_MYCELIUM.get(),
                        NtmBlocks.WASTE_TRINITITE.get(),
                        NtmBlocks.WASTE_TRINITITE_RED.get(),
                        NtmBlocks.FROZEN_DIRT.get(),
                        NtmBlocks.FROZEN_GRASS.get(),
                        NtmBlocks.FALLOUT.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_HOE)
                .add(
                        NtmBlocks.WASTE_LEAVES.get(),
                        NtmBlocks.LEAVES_LAYER.get()
                );

        // vanilla compat
        this.tag(BlockTags.STRIDER_WARM_BLOCKS).add(NtmBlocks.VOLCANIC_LAVA.get());
    }
}
