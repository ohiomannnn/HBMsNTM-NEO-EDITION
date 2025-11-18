package com.hbm.blocks;

import com.hbm.HBMsNTM;
import com.hbm.blocks.bomb.*;
import com.hbm.blocks.gas.GasCoalBlock;
import com.hbm.blocks.gas.GasMonoxideBlock;
import com.hbm.blocks.generic.*;
//import com.hbm.blocks.generic.StorageCrateBlock;
import com.hbm.blocks.special.ConcreteBrickMBlock;
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
            () -> new ConcreteBrickMBlock(BlockBehaviour.Properties.of()
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
    public static final DeferredBlock<Block> BRICK_OBSIDIAN = registerBlock("brick_obsidian",
            () -> new Block(BlockBehaviour.Properties.of()
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
            () -> new WasteLeavesBlock(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .strength(0.1F)
                    .randomTicks()
                    .noOcclusion()
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)
                    .sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> LEAVES_LAYER = registerBlock("leaves_layer",
            () -> new LayeringBlock(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .strength(0.1F)
                    .randomTicks()
                    .noOcclusion()
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)
                    .sound(SoundType.GRASS)));

    public static final DeferredBlock<Block> FALLOUT = registerBlock("fallout",
            () -> new FalloutBlock(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .strength(0.1F)
                    .sound(SoundType.GRAVEL)));
    public static final DeferredBlock<Block> SELLAFIELD_SLAKED = registerBlock("sellafield_slaked",
            () -> new SellafieldSlakedBlock(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .strength(3.0F, 10.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_SELLAFIELD_DIAMOND = registerBlock("ore_sellafield_diamond",
            () -> new SellafieldSlakedBlock(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .strength(3.0F, 10.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_SELLAFIELD_EMERALD = registerBlock("ore_sellafield_emerald",
            () -> new SellafieldSlakedBlock(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .strength(3.0F, 10.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> SELLAFIELD_BEDROCK = registerBlock("sellafield_bedrock",
            () -> new SellafieldSlakedBlock(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .strength(-1.0F, 6000000.0F)
                    .sound(SoundType.STONE)));
    public static final DeferredBlock<Block> WASTE_PLANKS = registerBlock("waste_planks",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.5F)
                    .explosionResistance(2.5F)
                    .sound(SoundType.WOOD)));
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
                    .sound(SoundType.NETHER_ORE)));
    public static final DeferredBlock<Block> ORE_NETHER_URANIUM_SCORCHED = registerBlock("ore_nether_uranium_scorched",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.4F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.NETHER_ORE)));
    public static final DeferredBlock<Block> ORE_NETHER_PLUTONIUM = registerBlock("ore_nether_plutonium",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.4F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.NETHER_ORE)));
    public static final DeferredBlock<Block> ORE_NETHER_SCHRABIDIUM = registerBlock("ore_nether_schrabidium",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .explosionResistance(600.0F)
                    .sound(SoundType.NETHER_ORE)));
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

    public static final DeferredBlock<Block> GAS_COAL = registerBlock("gas_coal",
            () -> new GasCoalBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .noOcclusion()
                    .noLootTable()
            ));
    public static final DeferredBlock<Block> GAS_MONOXIDE = registerBlock("gas_monoxide",
            () -> new GasMonoxideBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .noOcclusion()
                    .noLootTable()
            ));

    public static final DeferredBlock<Block> TAINT = registerBlock("taint",
            () -> new TaintBlock(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .explosionResistance(10.0F)
                    .randomTicks()));

    public static final DeferredBlock<Block> NUKE_FATMAN = registerBlock("nuke_fatman",
            () -> new NukeManBlock(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(200.0F)
                    .noOcclusion()
                    .sound(SoundType.METAL)
            ));

    public static final DeferredBlock<Block> DET_CHARGE = registerBlock(
            "det_charge",
            () -> new ExplosiveChargeBlock(BlockBehaviour.Properties.of()
                    .strength(0.1F)
                    .explosionResistance(0.0F)
                    .noLootTable()
                    .sound(SoundType.METAL)));
    public static final DeferredBlock<Block> DET_NUKE = registerBlock(
            "det_nuke",
            () -> new ExplosiveChargeBlock(BlockBehaviour.Properties.of()
                    .strength(0.1F)
                    .explosionResistance(0.0F)
                    .noLootTable()
                    .sound(SoundType.METAL)));
    public static final DeferredBlock<Block> DET_MINER = registerBlock(
            "det_miner",
            () -> new DetMinerBlock(BlockBehaviour.Properties.of()
                    .strength(0.1F)
                    .explosionResistance(0.0F)
                    .noLootTable()
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> MINE_AP = registerBlock(
            "mine_ap",
            () -> new LandmineBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .noLootTable()
                    .strength(1.0F)
                    .sound(SoundType.METAL), 1.5D, 1D));

    public static final DeferredBlock<Block> MINE_HE = registerBlock(
            "mine_he",
            () -> new LandmineBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .noLootTable()
                    .strength(1.0F)
                    .sound(SoundType.METAL), 2D, 5D));

    public static final DeferredBlock<Block> MINE_SHRAP = registerBlock(
            "mine_shrap",
            () -> new LandmineBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .noLootTable()
                    .strength(1.0F)
                    .sound(SoundType.METAL), 1.5D, 1D));

    public static final DeferredBlock<Block> MINE_FAT = registerBlock(
            "mine_fat",
            () -> new LandmineBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .noLootTable()
                    .strength(1.0F)
                    .sound(SoundType.METAL), 2.5D, 1D));

    public static final DeferredBlock<Block> MINE_NAVAL = registerBlock(
            "mine_naval",
            () -> new LandmineBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .noLootTable()
                    .strength(1.0F)
                    .sound(SoundType.METAL), 2.5D, 1D));

    public static final DeferredBlock<Block> CRASHED_BOMB_BALEFIRE = registerBlock(
            "crashed_bomb_balefire",
            () -> new CrashedBombBlock(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .noOcclusion()
                    .strength(6000.0F)
                    .sound(SoundType.METAL)));
    public static final DeferredBlock<Block> CRASHED_BOMB_CONVENTIONAL = registerBlock(
            "crashed_bomb_conventional",
            () -> new CrashedBombBlock(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .noOcclusion()
                    .strength(6000.0F)
                    .sound(SoundType.METAL)));
    public static final DeferredBlock<Block> CRASHED_BOMB_NUKE = registerBlock(
            "crashed_bomb_nuke",
            () -> new CrashedBombBlock(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .noOcclusion()
                    .strength(6000.0F)
                    .sound(SoundType.METAL)));
    public static final DeferredBlock<Block> CRASHED_BOMB_SALTED = registerBlock(
            "crashed_bomb_salted",
            () -> new CrashedBombBlock(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .noOcclusion()
                    .strength(6000.0F)
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> RED_BARREL = registerBlock("red_barrel",
            () -> new RedBarrelBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .noLootTable()
                    .strength(0.1F)
                    .explosionResistance(2.5F)
                    .sound(SoundType.METAL),
                    true
            ));

    public static final DeferredBlock<Block> URANIUM_BLOCK = registerBlock("uranium_block",
            () -> new HazardBlock(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(50.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_STAIRS = registerBlock(
            "brick_concrete_stairs",
            () -> new StairBlock(ModBlocks.BRICK_CONCRETE.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_MOSSY_STAIRS = registerBlock(
            "brick_concrete_mossy_stairs",
            () -> new StairBlock(ModBlocks.BRICK_CONCRETE_MOSSY.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_CRACKED_STAIRS = registerBlock(
            "brick_concrete_cracked_stairs",
            () -> new StairBlock(ModBlocks.BRICK_CONCRETE_CRACKED.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_BROKEN_STAIRS = registerBlock(
            "brick_concrete_broken_stairs",
            () -> new StairBlock(ModBlocks.BRICK_CONCRETE_BROKEN.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_SLAB = registerBlock(
            "brick_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_MOSSY_SLAB = registerBlock(
            "brick_concrete_mossy_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_CRACKED_SLAB = registerBlock(
            "brick_concrete_cracked_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_BROKEN_SLAB = registerBlock(
            "brick_concrete_broken_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> VOLCANIC_LAVA_BLOCK = registerBlock("volcanic_lava_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> RAD_LAVA_BLOCK = registerBlock("rad_lava_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> ASH_DIGAMMA = registerBlock("ash_digamma",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.5F)
                    .explosionResistance(150.0F)
                    .sound(SoundType.SAND)));
    public static final DeferredBlock<Block> PRIBRIS_DIGAMMA = registerBlock("pribris_digamma",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(50.0F)
                    .explosionResistance(600.0F)
                    .sound(SoundType.STONE)
                    .noLootTable()));

    public static final DeferredBlock<Block> IRON_CRATE =
            BLOCKS.register("iron_crate",
                    () -> new StorageCrateBlock(BlockBehaviour.Properties.of()
                            .noLootTable()
                            .sound(SoundType.STONE), false));

    public static final DeferredBlock<Block> BALEFIRE = BLOCKS.register(
            "balefire",
            () -> new BalefireBlock(BlockBehaviour.Properties.of()
                    .replaceable()
                    .noCollission()
                    .lightLevel(state -> 15)
                    .noLootTable()));

    public static final DeferredBlock<Block> FIRE_DIGAMMA = registerBlock(
            "fire_digamma",
            () -> new DigammaFlameBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .noOcclusion()
                    .lightLevel(state -> 10)
                    .noLootTable()));

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
