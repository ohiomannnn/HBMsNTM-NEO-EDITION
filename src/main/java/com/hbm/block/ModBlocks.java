package com.hbm.block;

import com.hbm.HBMsNTM;
import com.hbm.block.bomb.TestBomb;
import com.hbm.block.taint.TaintBlock;
import com.hbm.block.withInteraction.ConcreteBrickMarked;
import com.hbm.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
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

    public static final DeferredBlock<Block> TEST_BOMB = registerBlock("test_bomb",
            () -> new TestBomb(BlockBehaviour.Properties.of()
                    .sound(SoundType.METAL)));

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
    public static final DeferredBlock<Block> TAINT = registerBlock("taint",
            () -> new TaintBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.SLIME_BLOCK)));

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
