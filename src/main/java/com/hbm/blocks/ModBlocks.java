package com.hbm.blocks;

import com.hbm.HBMsNTM;
import com.hbm.blocks.bomb.BlockTaint;
import com.hbm.blocks.special.ConcreteBrickMarked;
import com.hbm.items.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HBMsNTM.MODID);

    // FOR BLOCKS //
    public static final DeferredBlock<Block> BRICK_CONCRETE = registerBlock("brick_concrete",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .explosionResistance(160.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> BRICK_CONCRETE_MOSSY = registerBlock("brick_concrete_mossy",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .explosionResistance(160.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> BRICK_CONCRETE_CRACKED = registerBlock("brick_concrete_cracked",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .explosionResistance(60.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> BRICK_CONCRETE_BROKEN = registerBlock("brick_concrete_broken",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .explosionResistance(45.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> BRICK_CONCRETE_MARKED = registerBlock("brick_concrete_marked",
            () -> new ConcreteBrickMarked(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .explosionResistance(160.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> BRICK_LIGHT = registerBlock("brick_light",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(20.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> WASTE_EARTH = registerBlock("waste_earth",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.6F)
                    .sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> WASTE_LOG = registerBlock("waste_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(2.5F)
                    .sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> WASTE_LEAVES = registerBlock("waste_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.of()
                    .strength(0.1F)
                    .randomTicks()
                    .noOcclusion()
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)
                    .sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> WASTE_TRINITITE = registerBlock("waste_trinitite",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.5F)
                    .explosionResistance(2.5F)
                    .sound(SoundType.SAND)));
    public static final DeferredBlock<Block> WASTE_TRINITITE_RED = registerBlock("waste_trinitite_red",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.5F)
                    .explosionResistance(2.5F)
                    .sound(SoundType.SAND)));
    public static final DeferredBlock<Block> WASTE_MYCELIUM = registerBlock("waste_mycelium",
            () -> new Block(BlockBehaviour.Properties.of()
                    .lightLevel(value -> 1)
                    .strength(0.6F)
                    .sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> WASTE_PLANKS = registerBlock("waste_planks",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.5F)
                    .explosionResistance(2.5F)
                    .sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> BLOCK_SCRAP = registerBlock("block_scrap",
            () -> new FallingBlock(BlockBehaviour.Properties.of()
                    .strength(2.5F)
                    .explosionResistance(5.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.GRAVEL)) {
                    // ok... this is for falling blocks don't try to read lol
                    @Override
                    protected MapCodec<? extends FallingBlock> codec() {
                        return simpleCodec(props -> new FallingBlock(props) {
                            @Override
                            protected MapCodec<? extends FallingBlock> codec() {
                                return this.codec();
                            }
                        });
                    }
            });

    public static final DeferredBlock<Block> BRICK_OBSIDIAN = registerBlock("brick_obsidian",
            () -> new Block(BlockBehaviour.Properties.of()
                    //.setLightOpacity(15) idk what is it
                    .strength(15.0F)
                    .explosionResistance(120.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> GRAVEL_OBSIDIAN = registerBlock("gravel_obsidian",
            () -> new FallingBlock(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .explosionResistance(120.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)) {
                    @Override
                    protected MapCodec<? extends FallingBlock> codec() {
                        return simpleCodec(props -> new FallingBlock(props) {
                            @Override
                            protected MapCodec<? extends FallingBlock> codec() {
                                return this.codec();
                            }
                        });
                    }
            });

    public static final DeferredBlock<Block> ORE_OIL = registerBlock("ore_oil",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_URANIUM = registerBlock("ore_uranium",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_URANIUM_SCORCHED = registerBlock("ore_uranium_scorched",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_SCHRABIDIUM = registerBlock("ore_schrabidium",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .explosionResistance(600.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_NETHER_URANIUM = registerBlock("ore_nether_uranium",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.4F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_NETHER_URANIUM_SCORCHED = registerBlock("ore_nether_uranium_scorched",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.4F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_NETHER_PLUTONIUM = registerBlock("ore_nether_plutonium",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.4F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_NETHER_SCHRABIDIUM = registerBlock("ore_nether_schrabidium",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .explosionResistance(600.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_TIKITE = registerBlock("ore_tikite",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> ORE_GNEISS_URANIUM = registerBlock("ore_gneiss_uranium",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(1.5F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_GNEISS_URANIUM_SCORCHED = registerBlock("ore_gneiss_uranium_scorched",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(1.5F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_GNEISS_SCHRABIDIUM = registerBlock("ore_gneiss_schrabidium",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(1.5F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.STONE)));


    public static final DeferredBlock<Block> TAINT = registerBlock("taint",
            () -> new BlockTaint(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .explosionResistance(10.0F)
                    .randomTicks()));

    // FOR STAIRS //
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_STAIRS = registerBlock("brick_concrete_stairs",
            () -> new StairBlock(ModBlocks.BRICK_CONCRETE.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(15.0F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_MOSSY_STAIRS = registerBlock("brick_concrete_mossy_stairs",
            () -> new StairBlock(ModBlocks.BRICK_CONCRETE_MOSSY.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(15.0F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_CRACKED_STAIRS = registerBlock("brick_concrete_cracked_stairs",
            () -> new StairBlock(ModBlocks.BRICK_CONCRETE_CRACKED.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(15.0F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_BROKEN_STAIRS = registerBlock("brick_concrete_broken_stairs",
            () -> new StairBlock(ModBlocks.BRICK_CONCRETE_BROKEN.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(15.0F).requiresCorrectToolForDrops()));

    // FOR SLABS //
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_SLAB = registerBlock("brick_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(15.0F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_MOSSY_SLAB = registerBlock("brick_concrete_mossy_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(15.0F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_CRACKED_SLAB = registerBlock("brick_concrete_cracked_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(15.0F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_BROKEN_SLAB = registerBlock("brick_concrete_broken_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(15.0F).requiresCorrectToolForDrops()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> regBlock = BLOCKS.register(name, block);
        registerBlockItem(name, regBlock);
        return regBlock;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
