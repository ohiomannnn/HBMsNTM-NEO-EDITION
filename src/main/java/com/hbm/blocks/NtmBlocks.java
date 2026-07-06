package com.hbm.blocks;

import com.hbm.blocks.bomb.*;
import com.hbm.blocks.fluids.RadLiquidBlock;
import com.hbm.blocks.fluids.VolcanicLiquidBlock;
import com.hbm.blocks.gas.*;
import com.hbm.blocks.generic.*;
import com.hbm.blocks.machine.*;
import com.hbm.blocks.network.CableBlock;
import com.hbm.blocks.network.FluidDuctStandardBlock;
import com.hbm.blocks.network.MachineBatteryREDDBlock;
import com.hbm.blocks.network.MachineBatterySocketBlock;
import com.hbm.fluids.NtmFluids;
import com.hbm.items.NtmItems;
import com.hbm.items.block.BlastInfoBlockItem;
import com.hbm.items.block.BlockItemBase;
import com.hbm.items.block.LoreBlockItem;
import com.hbm.main.NuclearTechMod;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NtmBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(NuclearTechMod.MODID);

    // Ores
    public static final DeferredBlock<Block> ORE_OIL = register("ore_oil", () -> new Block(BlockBehaviour.Properties.of().strength(5.0F).explosionResistance(10.0F).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_URANIUM = register("ore_uranium", () -> new Block(BlockBehaviour.Properties.of().strength(5.0F).explosionResistance(10.0F).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_URANIUM_SCORCHED = register("ore_uranium_scorched", () -> new Block(BlockBehaviour.Properties.of().strength(5.0F).explosionResistance(10.0F).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_SCHRABIDIUM = register("ore_schrabidium", () -> new Block(BlockBehaviour.Properties.of().strength(15.0F).explosionResistance(600.0F).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_NETHER_URANIUM = register("ore_nether_uranium", () -> new Block(BlockBehaviour.Properties.of().strength(0.4F).explosionResistance(10.0F).sound(SoundType.NETHER_ORE)));
    public static final DeferredBlock<Block> ORE_NETHER_URANIUM_SCORCHED = register("ore_nether_uranium_scorched", () -> new Block(BlockBehaviour.Properties.of().strength(0.4F).explosionResistance(10.0F).sound(SoundType.NETHER_ORE)));
    public static final DeferredBlock<Block> ORE_NETHER_PLUTONIUM = register("ore_nether_plutonium", () -> new Block(BlockBehaviour.Properties.of().strength(0.4F).explosionResistance(10.0F).sound(SoundType.NETHER_ORE)));
    public static final DeferredBlock<Block> ORE_NETHER_SCHRABIDIUM = register("ore_nether_schrabidium", () -> new Block(BlockBehaviour.Properties.of().strength(15.0F).explosionResistance(600.0F).sound(SoundType.NETHER_ORE)));
    public static final DeferredBlock<Block> ORE_TIKITE = register("ore_tikite", () -> new Block(BlockBehaviour.Properties.of().strength(5.0F).explosionResistance(10.0F).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_GNEISS_URANIUM = register("ore_gneiss_uranium", () -> new Block(BlockBehaviour.Properties.of().strength(1.5F).explosionResistance(10.0F).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_GNEISS_URANIUM_SCORCHED = register("ore_gneiss_uranium_scorched", () -> new Block(BlockBehaviour.Properties.of().strength(1.5F).explosionResistance(10.0F).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_GNEISS_SCHRABIDIUM = register("ore_gneiss_schrabidium", () -> new Block(BlockBehaviour.Properties.of().strength(1.5F).explosionResistance(10.0F).sound(SoundType.STONE)));

    // Basalt ores
    public static final DeferredBlock<Block> ORE_BASALT = registerNew("ore_basalt", () -> new OreBasaltBlock(BlockBehaviour.Properties.of().strength(5F, 10F).sound(SoundType.BASALT).mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)));

    // Stone Variants
    public static final DeferredBlock<RotatedPillarBlock> BASALT = register("basalt",          () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(5F, 10F).sound(SoundType.BASALT).mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)));
    public static final DeferredBlock<Block> BASALT_SMOOTH =       register("basalt_smooth",   () -> new Block(BlockBehaviour.Properties.of().strength(5F, 10F).sound(SoundType.BASALT).mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)));
    public static final DeferredBlock<Block> BASALT_BRICK =        register("basalt_brick",    () -> new Block(BlockBehaviour.Properties.of().strength(5F, 10F).sound(SoundType.BASALT).mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)));
    public static final DeferredBlock<Block> BASALT_POLISHED =     register("basalt_polished", () -> new Block(BlockBehaviour.Properties.of().strength(5F, 10F).sound(SoundType.BASALT).mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)));
    public static final DeferredBlock<Block> BASALT_TILES =        register("basalt_tiles",    () -> new Block(BlockBehaviour.Properties.of().strength(5F, 10F).sound(SoundType.BASALT).mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)));

    // Blocks
    public static final DeferredBlock<Block> BLOCK_SCRAP = register("block_scrap", () -> new ColoredFallingBlock(new ColorRGBA(-8356741), BlockBehaviour.Properties.of().strength(2.5F, 5.0F).sound(SoundType.GRAVEL)));

    // Deco Blocks
    public static final DeferredBlock<Block> BOBBLEHEAD = registerNew("bobblehead", () -> new BobbleBlock(BlockBehaviour.Properties.of().noOcclusion().instabreak().mapColor(DyeColor.WHITE)));
    public static final DeferredBlock<Block> PLUSHIE = registerNew("plushie", () -> new PlushieBlock(BlockBehaviour.Properties.of().noOcclusion().instabreak().sound(SoundType.WOOL).mapColor(DyeColor.WHITE)));

    // Gravel
    public static final DeferredBlock<Block> GRAVEL_OBSIDIAN = registerBlastInfoBlock("gravel_obsidian", () -> new ColoredFallingBlock(new ColorRGBA(-8356741), BlockBehaviour.Properties.of().strength(5.0F, 240.0F).sound(SoundType.GRAVEL).mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)));
    public static final DeferredBlock<Block> GRAVEL_DIAMOND = register("gravel_diamond", () -> new ColoredFallingBlock(new ColorRGBA(-8356741), BlockBehaviour.Properties.of().strength(0.6F).sound(SoundType.GRAVEL).mapColor(MapColor.STONE)), LoreBlockItem.class, new Item.Properties().rarity(Rarity.RARE));

    // Reinforced Blocks
    public static final DeferredBlock<Block> ASPHALT =       registerBlastInfoBlock("asphalt",       () -> new SpeedyBlock(1.5, BlockBehaviour.Properties.of().strength(15.0F, 120.0F)                                  .mapColor(MapColor.COLOR_BLACK)));
    public static final DeferredBlock<Block> ASPHALT_LIGHT = registerBlastInfoBlock("asphalt_light", () -> new SpeedyBlock(1.5, BlockBehaviour.Properties.of().strength(15.0F, 120.0F).lightLevel(state -> 15).mapColor(MapColor.SAND)));

    // Bricks
    public static final DeferredBlock<Block> BRICK_CONCRETE =         registerBlastInfoBlock("brick_concrete",         () -> new NoSpawnBlock(BlockBehaviour.Properties.of().strength(15.0F, 160.0F).mapColor(MapColor.STONE)));
    public static final DeferredBlock<Block> BRICK_CONCRETE_MOSSY =   registerBlastInfoBlock("brick_concrete_mossy",   () -> new Block(BlockBehaviour.Properties.of().strength(15.0F, 160.0F).mapColor(MapColor.STONE)));
    public static final DeferredBlock<Block> BRICK_CONCRETE_CRACKED = registerBlastInfoBlock("brick_concrete_cracked", () -> new Block(BlockBehaviour.Properties.of().strength(15.0F, 60.0F).mapColor(MapColor.STONE)));
    public static final DeferredBlock<Block> BRICK_CONCRETE_BROKEN =  registerBlastInfoBlock("brick_concrete_broken",  () -> new Block(BlockBehaviour.Properties.of().strength(15.0F, 45.0F).mapColor(MapColor.STONE)));
    public static final DeferredBlock<Block> BRICK_CONCRETE_MARKED =  registerBlastInfoBlock("brick_concrete_marked",  () -> new WritingBlock(BlockBehaviour.Properties.of().strength(15.0F, 160.0F ).mapColor(MapColor.STONE)));
    public static final DeferredBlock<Block> BRICK_OBSIDIAN =         registerBlastInfoBlock("brick_obsidian",         () -> new Block(BlockBehaviour.Properties.of().strength(15.0F, 120.0F).mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)));
    public static final DeferredBlock<Block> BRICK_LIGHT =            registerBlastInfoBlock("brick_light",            () -> new Block(BlockBehaviour.Properties.of().strength(5.0F, 20.0F).mapColor(MapColor.SAND)));
    public static final DeferredBlock<Block> BRICK_ASBESTOS =         register(         "brick_asbestos",         () -> new OutgasBlock(true, true, BlockBehaviour.Properties.of().strength(5.0F, 1000.0F).mapColor(MapColor.SNOW)));
    public static final DeferredBlock<Block> BRICK_FIRE =             registerBlastInfoBlock("brick_fire",             () -> new Block(BlockBehaviour.Properties.of().strength(5.0F, 35.0F).mapColor(MapColor.FIRE)));

    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_SLAB =         register("brick_concrete_slab",         () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BRICK_CONCRETE.get())));
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_MOSSY_SLAB =   register("brick_concrete_mossy_slab",   () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BRICK_CONCRETE.get())));
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_CRACKED_SLAB = register("brick_concrete_cracked_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BRICK_CONCRETE.get())));
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_BROKEN_SLAB =  register("brick_concrete_broken_slab",  () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BRICK_CONCRETE.get())));

    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_STAIRS =         register("brick_concrete_stairs",         () -> new StairBlock(BRICK_CONCRETE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BRICK_CONCRETE.get())));
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_MOSSY_STAIRS =   register("brick_concrete_mossy_stairs",   () -> new StairBlock(BRICK_CONCRETE_MOSSY.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BRICK_CONCRETE.get())));
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_CRACKED_STAIRS = register("brick_concrete_cracked_stairs", () -> new StairBlock(BRICK_CONCRETE_CRACKED.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BRICK_CONCRETE.get())));
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_BROKEN_STAIRS =  register("brick_concrete_broken_stairs",  () -> new StairBlock(BRICK_CONCRETE_BROKEN.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BRICK_CONCRETE.get())));

    // Other defensive stuff
    public static final DeferredBlock<Block> BARBED_WIRE = registerNew("barbed_wire", () -> new BarbedWireBlock(BlockBehaviour.Properties.of().strength(5.0F, 10.0F).noCollission()));
    public static final DeferredBlock<Block> SPIKES = register("spikes", () -> new SpikesBlock(BlockBehaviour.Properties.of().strength(2.5F, 5.0F).noCollission()));

    // Waste
    public static final DeferredBlock<Block> WASTE_EARTH =         register("waste_earth",         () -> new Block(               BlockBehaviour.Properties.of().strength(0.6F).sound(SoundType.GRASS).mapColor(MapColor.DIRT)));
    public static final DeferredBlock<Block> WASTE_MYCELIUM =      register("waste_mycelium",      () -> new WasteMyceliumBlock(  BlockBehaviour.Properties.of().strength(0.6F).lightLevel(value -> 10).sound(SoundType.GRASS).mapColor(MapColor.COLOR_LIGHT_GREEN)));
    public static final DeferredBlock<Block> WASTE_TRINITITE =     register("waste_trinitite",     () -> new WasteTrinititeBlock( BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.SAND).mapColor(MapColor.SAND).instrument(NoteBlockInstrument.SNARE)));
    public static final DeferredBlock<Block> WASTE_TRINITITE_RED = register("waste_trinitite_red", () -> new WasteTrinititeBlock( BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.SAND).mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.SNARE)));
    public static final DeferredBlock<RotatedPillarBlock> WASTE_LOG = register("waste_log",    () -> new RotatedPillarBlock( BlockBehaviour.Properties.of().strength(5.0F, 2.5F).sound(SoundType.WOOD).mapColor(MapColor.COLOR_BLACK)));
    public static final DeferredBlock<Block> WASTE_LEAVES =           register("waste_leaves", () -> new WasteLeavesBlock(   BlockBehaviour.Properties.of().strength(0.2F).randomTicks().mapColor(MapColor.COLOR_BROWN).sound(SoundType.GRASS).noOcclusion().isValidSpawn(Blocks::never).isSuffocating(NtmBlocks::never).isViewBlocking(NtmBlocks::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(NtmBlocks::never)));
    public static final DeferredBlock<Block> WASTE_PLANKS =           register("waste_planks", () -> new Block(              BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.WOOD).mapColor(MapColor.COLOR_BLACK)));
    public static final DeferredBlock<Block> FROZEN_DIRT =             register("frozen_dirt",   () -> new FrozenBlock(        BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.GLASS).mapColor(DyeColor.LIGHT_BLUE)));
    public static final DeferredBlock<Block> FROZEN_GRASS =            register("frozen_grass",  () -> new FrozenBlock(        BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.GLASS).mapColor(DyeColor.WHITE)));
    public static final DeferredBlock<RotatedPillarBlock> FROZEN_LOG = register("frozen_log",    () -> new RotatedPillarBlock( BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.GLASS).mapColor(DyeColor.LIGHT_BLUE)));
    public static final DeferredBlock<Block> FROZEN_PLANKS =           register("frozen_planks", () -> new Block(              BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.GLASS).mapColor(DyeColor.LIGHT_BLUE)));
    public static final DeferredBlock<Block> LEAVES_LAYER = register("leaves_layer", () -> new LayeringBlock(BlockBehaviour.Properties.of().strength(0.2F).randomTicks().mapColor(MapColor.COLOR_BROWN).sound(SoundType.GRASS).noOcclusion().isValidSpawn(Blocks::never).isSuffocating(NtmBlocks::never).isViewBlocking(NtmBlocks::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(NtmBlocks::never)));
    public static final DeferredBlock<Block> FALLOUT = register("fallout", () -> new FalloutBlock(BlockBehaviour.Properties.of().replaceable().strength(0.1F).sound(SoundType.GRAVEL).mapColor(MapColor.STONE)));
    public static final DeferredBlock<Block> SELLAFIELD_SLAKED = register("sellafield_slaked", () -> new SellafieldSlakedBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).isValidSpawn(Blocks::never).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));
    public static final DeferredBlock<Block> ORE_SELLAFIELD_DIAMOND = register("ore_sellafield_diamond", () -> new SellafieldSlakedBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).isValidSpawn(Blocks::never).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));
    public static final DeferredBlock<Block> ORE_SELLAFIELD_EMERALD = register("ore_sellafield_emerald", () -> new SellafieldSlakedBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).isValidSpawn(Blocks::never).requiresCorrectToolForDrops().strength(3.0F, 10.0F)));
    public static final DeferredBlock<Block> SELLAFIELD_BEDROCK = register("sellafield_bedrock", () -> new SellafieldSlakedBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).isValidSpawn(Blocks::never).noLootTable().strength(-1.0F, 6000000.0F).instrument(NoteBlockInstrument.BASEDRUM)));

    // Nukes
    public static final DeferredBlock<Block> NUKE_GADGET =     register("nuke_gadget",     () -> new NukeGadgetBlock(    BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 200.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_LITTLE_BOY = register("nuke_little_boy", () -> new NukeLittleBoyBlock( BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 200.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_FAT_MAN =    register("nuke_fat_man",    () -> new NukeFatManBlock(    BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 200.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_IVY_MIKE =   register("nuke_ivy_mike",   () -> new NukeIvyMikeBlock(   BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 200.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_TSAR_BOMBA = register("nuke_tsar_bomba", () -> new NukeTsarBombaBlock( BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 200.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_PROTOTYPE =  register("nuke_prototype",  () -> new NukePrototypeBlock( BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 200.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_FLEIJA =     register("nuke_fleija",     () -> new NukeFleijaBlock(    BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 200.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_SOLINIUM =   register("nuke_solinium",   () -> new NukeSoliniumBlock(  BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 200.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_N2 =         register("nuke_n2",         () -> new NukeN2Block(        BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 200.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_FSTBMB =     register("nuke_fstbmb",     () -> new NukeBalefireBlock(  BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 200.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));

    // Generic Bombs
    // todo add bomb_multi
    public static final DeferredBlock<Block> CRASHED_BOMB = registerNew("crashed_bomb", () -> new CrashedBombBlock(BlockBehaviour.Properties.of().noLootTable().noOcclusion().strength(6000.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> DYNAMITE = register("dynamite", () -> new DynamiteBlock( BlockBehaviour.Properties.of().instabreak().ignitedByLava().isRedstoneConductor(NtmBlocks::never).sound(SoundType.GRASS).mapColor(MapColor.FIRE)));
    public static final DeferredBlock<Block> TNT =      register("tnt",      () -> new TNTBlock(      BlockBehaviour.Properties.of().instabreak().ignitedByLava().isRedstoneConductor(NtmBlocks::never).sound(SoundType.GRASS).mapColor(MapColor.FIRE)));
    public static final DeferredBlock<Block> SEMTEX =   register("semtex",   () -> new SemtexBlock(   BlockBehaviour.Properties.of().instabreak().ignitedByLava().isRedstoneConductor(NtmBlocks::never).sound(SoundType.GRASS).mapColor(MapColor.FIRE)));
    public static final DeferredBlock<Block> C4 =       register("c4",       () -> new C4Block(       BlockBehaviour.Properties.of().instabreak().ignitedByLava().isRedstoneConductor(NtmBlocks::never).sound(SoundType.GRASS).mapColor(MapColor.FIRE)));
    public static final DeferredBlock<Block> FISSURE_BOMB = register("fissure_bomb", () -> new FissureBombBlock(BlockBehaviour.Properties.of().instabreak().ignitedByLava().isRedstoneConductor(NtmBlocks::never).sound(SoundType.GRASS).mapColor(MapColor.FIRE)));

    // Mines
    public static final DeferredBlock<Block> MINE_AP =    register("mine_ap",    () -> new LandmineBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.0F, 0.0F).sound(SoundType.METAL).mapColor(MapColor.METAL), 1.5D, 1D));
    public static final DeferredBlock<Block> MINE_HE =    register("mine_he",    () -> new LandmineBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.0F, 0.0F).sound(SoundType.METAL).mapColor(MapColor.METAL), 2D, 5D));
    public static final DeferredBlock<Block> MINE_SHRAP = register("mine_shrap", () -> new LandmineBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.0F, 0.0F).sound(SoundType.METAL).mapColor(MapColor.METAL), 1.5D, 1D));
    public static final DeferredBlock<Block> MINE_FAT =   register("mine_fat",   () -> new LandmineBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.0F, 0.0F).sound(SoundType.METAL).mapColor(MapColor.METAL), 2.5D, 1D));
    public static final DeferredBlock<Block> MINE_NAVAL = register("mine_naval", () -> new LandmineBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.0F, 0.0F).sound(SoundType.METAL).mapColor(MapColor.METAL), 2.5D, 1D));

    // Block Bombs
    public static final DeferredBlock<Block> DET_CHARGE =  register("det_charge",  () -> new ExplosiveChargeBlock(BlockBehaviour.Properties.of().strength(0.1F, 0.0F).sound(SoundType.METAL)));
    public static final DeferredBlock<Block> DET_CORD =    register("det_cord",    () -> new DetCordBlock(BlockBehaviour.Properties.of().strength(0.1F, 0.0F).noOcclusion().isSuffocating(NtmBlocks::never).isViewBlocking(NtmBlocks::never).sound(SoundType.METAL)));
    public static final DeferredBlock<Block> DET_NUKE =    register("det_nuke",    () -> new ExplosiveChargeBlock(BlockBehaviour.Properties.of().strength(0.1F, 0.0F).sound(SoundType.METAL)));
    public static final DeferredBlock<Block> DET_MINER =   register("det_miner",   () -> new ExplosiveChargeBlock(BlockBehaviour.Properties.of().strength(0.1F, 0.0F).sound(SoundType.METAL)));
    public static final DeferredBlock<Block> BARREL_RED =   register("barrel_red",   () -> new RedBarrelBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.1F, 2.5F).sound(SoundType.METAL), true));
    public static final DeferredBlock<Block> BARREL_PINK =  register("barrel_pink",  () -> new RedBarrelBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.1F, 2.5F).sound(SoundType.METAL), true));
    public static final DeferredBlock<Block> BARREL_LOX =   register("barrel_lox",   () -> new RedBarrelBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.1F, 2.5F).sound(SoundType.METAL), false));
    public static final DeferredBlock<Block> BARREL_TAINT = register("barrel_taint", () -> new RedBarrelBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.1F, 2.5F).sound(SoundType.METAL), false));

    // Geiger Counter
    public static final DeferredBlock<Block> GEIGER = register("geiger", () -> new GeigerCounterBlock(BlockBehaviour.Properties.of().strength(15.0F, 0.25F).sound(SoundType.METAL).mapColor(MapColor.COLOR_YELLOW)));

    // Machines
    public static final DeferredBlock<Block> MACHINE_PRESS = register("machine_press", () -> new MachinePressBlock(BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 10.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));

    public static final DeferredBlock<Block> RED_CABLE = register("red_cable", () -> new CableBlock(BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 10.0F).sound(SoundType.METAL).mapColor(MapColor.COLOR_BLACK)));

    public static final DeferredBlock<Block> FLUID_DUCT_NEO = registerNew("fluid_duct_neo", () -> new FluidDuctStandardBlock(BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 10.0F).sound(ModSoundTypes.PIPE).mapColor(MapColor.METAL)));

    public static final DeferredBlock<Block> MACHINE_BATTERY_SOCKET = register("machine_battery_socket", () -> new MachineBatterySocketBlock(BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 10.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> MACHINE_BATTERY_REDD = register("machine_battery_redd", () -> new MachineBatteryREDDBlock(BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 10.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> MACHINE_ASSEMBLY_MACHINE = register("machine_assembly_machine", () -> new MachineAssemblyMachineBlock(BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 30.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> MACHINE_FLUID_TANK = register("machine_fluid_tank", () -> new MachineFluidTankBlock(BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 20.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));

    public static final DeferredBlock<Block> MACHINE_SATLINKER = register("machine_satlinker", () -> new MachineSatLinkerBlock(BlockBehaviour.Properties.of().strength(5.0F, 10.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));

    // Absorbers
    public static final DeferredBlock<Block> DECONTAMINATOR = register("decontaminator", () -> new DecontaminatorBlock(BlockBehaviour.Properties.of().strength(5.0F, 10.0F).sound(SoundType.METAL).mapColor(MapColor.TERRACOTTA_GREEN)));

    // PWR
    public static final DeferredBlock<Block> PWR_CONTROLLER = register("pwr_controller", () -> new Block(BlockBehaviour.Properties.of()));
    
    // E
    public static final DeferredBlock<Block> BALEFIRE =     BLOCKS.register("balefire",     () -> new BalefireBlock(     BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).replaceable().noCollission().noOcclusion().strength(0F).lightLevel(state -> 15).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> FIRE_DIGAMMA = BLOCKS.register("fire_digamma", () -> new DigammaFlameBlock( BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).replaceable().noCollission().noOcclusion().strength(0F, 150F).lightLevel(state -> 10).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> VOLCANO_CORE =     registerNew("volcano_core",     () -> new VolcanoBlock(BlockBehaviour.Properties.of().strength(-1.0F, 10000.0F).mapColor(MapColor.NETHER)));
    public static final DeferredBlock<Block> VOLCANO_RAD_CORE = registerNew("volcano_rad_core", () -> new VolcanoBlock(BlockBehaviour.Properties.of().strength(-1.0F, 10000.0F).mapColor(DyeColor.GREEN)));

    // Missile Blocks
    public static final DeferredBlock<Block> LAUNCH_PAD = register("launch_pad", () -> new LaunchPadBlock(BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 10.0F).mapColor(MapColor.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> LAUNCH_PAD_LARGE = register("launch_pad_large", () -> new LaunchPadLargeBlock(BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 10.0F).mapColor(MapColor.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> SOYUZ_LAUNCHER = register("soyuz_launcher", () -> new SoyuzLauncherBlock(BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 10.0F).mapColor(MapColor.METAL).mapColor(MapColor.METAL)));

    // Fluids
    public static final DeferredBlock<LiquidBlock> VOLCANIC_LAVA = BLOCKS.register("volcanic_lava", () -> new VolcanicLiquidBlock(NtmFluids.VOLCANIC_LAVA.get(), BlockBehaviour.Properties.of().randomTicks().noCollission().replaceable().strength(500F).lightLevel(state -> 15).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY)));
    public static final DeferredBlock<LiquidBlock> RAD_LAVA = BLOCKS.register("rad_lava", () -> new RadLiquidBlock(NtmFluids.RAD_LAVA.get(), BlockBehaviour.Properties.of().randomTicks().noCollission().replaceable().strength(500F).lightLevel(state -> 15).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY)));

    // Other Technical Blocks
    public static final DeferredBlock<Block> GAS_RADON =       register("gas_radon",       () -> new GasRadonBlock(      BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_RADON_DENSE = register("gas_radon_dense", () -> new GasRadonDenseBlock( BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_RADON_TOMB =  register("gas_radon_tomb",  () -> new GasRadonTombBlock(  BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_MELTDOWN =    register("gas_meltdown",    () -> new GasMeltdownBlock(   BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_MONOXIDE =    register("gas_monoxide",    () -> new GasMonoxideBlock(   BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_ASBESTOS =    register("gas_asbestos",    () -> new GasAsbestosBlock(   BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_COAL =        register("gas_coal",        () -> new GasCoalBlock(       BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_FLAMMABLE =   register("gas_flammable",   () -> new GasFlammableBlock(  BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_EXPLOSIVE =   register("gas_explosive",   () -> new GasExplosiveBlock(  BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));

    // ???
    public static final DeferredBlock<Block> TAINT = register("taint", () -> new TaintBlock(BlockBehaviour.Properties.of().randomTicks().strength(15.0F, 10.0F).noLootTable().mapColor(DyeColor.GRAY)));

    public static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) { return true; }
    public static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) { return false; }
    public static boolean noSpawn(BlockState var1, BlockGetter var2, BlockPos var3, EntityType<?> var4) { return false; }

    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> block) {
        DeferredBlock<T> defBlock = BLOCKS.register(name, block);
        NtmItems.ITEMS.register(name, () -> new BlockItem(defBlock.get(), new Properties()));
        return defBlock;
    }

    private static <T extends Block> DeferredBlock<T> registerNew(String name, Supplier<T> block) {
        DeferredBlock<T> defBlock = BLOCKS.register(name, block);
        NtmItems.ITEMS.register(name, () -> new BlockItemBase(defBlock.get(), new Properties()));
        return defBlock;
    }

    private static <T extends Block> DeferredBlock<T> registerBlastInfoBlock(String name, Supplier<T> block) {
        DeferredBlock<T> defBlock = BLOCKS.register(name, block);
        NtmItems.ITEMS.register(name, () -> new BlastInfoBlockItem(defBlock.get(), new Properties()));
        return defBlock;
    }

    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> block, Class<? extends BlockItem> clazz, Item.Properties properties) {
        DeferredBlock<T> defBlock = BLOCKS.register(name, block);
        NtmItems.ITEMS.register(name, () -> {
            try {
                return clazz.getConstructor(Block.class, Properties.class).newInstance(defBlock.get(), properties);
            } catch(Exception ignored) {}
            return null;
        });
        return defBlock;
    }

    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> block, Class<? extends BlockItem> clazz) { return register(name, block, clazz, new Item.Properties()); }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
