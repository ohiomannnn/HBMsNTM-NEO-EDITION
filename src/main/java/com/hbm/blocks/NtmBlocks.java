package com.hbm.blocks;

import com.hbm.blocks.bomb.*;
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
import com.hbm.items.block.BlockItemBase;
import com.hbm.main.NuclearTechMod;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item.Properties;
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

    // Reinforced Blocks
    public static final DeferredBlock<Block> ASPHALT =       registerBlock("asphalt",       () -> new SpeedyBlock(1.5, BlockBehaviour.Properties.of().strength(15.0F, 120.0F)                                  .mapColor(MapColor.COLOR_BLACK)));
    public static final DeferredBlock<Block> ASPHALT_LIGHT = registerBlock("asphalt_light", () -> new SpeedyBlock(1.5, BlockBehaviour.Properties.of().strength(15.0F, 120.0F).lightLevel(state -> 15).mapColor(MapColor.SAND)));

    // Bricks
    public static final DeferredBlock<Block> BRICK_CONCRETE =         registerBlock("brick_concrete",         () -> new Block(        BlockBehaviour.Properties.of().strength(15.0F, 160.0F).requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final DeferredBlock<Block> BRICK_CONCRETE_MOSSY =   registerBlock("brick_concrete_mossy",   () -> new Block(        BlockBehaviour.Properties.of().strength(15.0F, 160.0F).requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final DeferredBlock<Block> BRICK_CONCRETE_CRACKED = registerBlock("brick_concrete_cracked", () -> new Block(        BlockBehaviour.Properties.of().strength(15.0F, 60.0F ).requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final DeferredBlock<Block> BRICK_CONCRETE_BROKEN =  registerBlock("brick_concrete_broken",  () -> new Block(        BlockBehaviour.Properties.of().strength(15.0F, 45.0F ).requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final DeferredBlock<Block> BRICK_CONCRETE_MARKED =  registerBlock("brick_concrete_marked",  () -> new WritingBlock( BlockBehaviour.Properties.of().strength(15.0F, 160.0F).requiresCorrectToolForDrops().mapColor(MapColor.STONE)));
    public static final DeferredBlock<Block> BRICK_OBSIDIAN =         registerBlock("brick_obsidian",         () -> new Block(        BlockBehaviour.Properties.of().strength(15.0F, 120.0F).requiresCorrectToolForDrops().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)));
    public static final DeferredBlock<Block> BRICK_LIGHT =            registerBlock("brick_light",            () -> new Block(        BlockBehaviour.Properties.of().strength(5.0F,  20.0F ).requiresCorrectToolForDrops().mapColor(MapColor.SAND)));

    // Other defensive stuff
    public static final DeferredBlock<Block> BARBED_WIRE = registerBlockNew("barbed_wire", () -> new BarbedWireBlock(BlockBehaviour.Properties.of().strength(5.0F, 10.0F).noCollission()));
    public static final DeferredBlock<Block> SPIKES = registerBlock("spikes", () -> new SpikesBlock(BlockBehaviour.Properties.of().strength(2.5F, 5.0F).noCollission()));

    public static final DeferredBlock<Block> GRAVEL_OBSIDIAN = registerBlock("gravel_obsidian", () -> new ColoredFallingBlock(new ColorRGBA(-8356741), BlockBehaviour.Properties.of().strength(15.0F).explosionResistance(120.0F).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> WASTE_EARTH =         registerBlock("waste_earth",         () -> new Block(               BlockBehaviour.Properties.of().strength(0.6F).sound(SoundType.GRASS).mapColor(MapColor.DIRT)));
    public static final DeferredBlock<Block> WASTE_MYCELIUM =      registerBlock("waste_mycelium",      () -> new WasteMyceliumBlock(  BlockBehaviour.Properties.of().strength(0.6F).lightLevel(value -> 10).sound(SoundType.GRASS).mapColor(MapColor.COLOR_LIGHT_GREEN)));
    public static final DeferredBlock<Block> WASTE_TRINITITE =     registerBlock("waste_trinitite",     () -> new WasteTrinititeBlock( BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.SAND).mapColor(MapColor.SAND).instrument(NoteBlockInstrument.SNARE)));
    public static final DeferredBlock<Block> WASTE_TRINITITE_RED = registerBlock("waste_trinitite_red", () -> new WasteTrinititeBlock( BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.SAND).mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.SNARE)));
    public static final DeferredBlock<RotatedPillarBlock> WASTE_LOG = registerBlock("waste_log",    () -> new RotatedPillarBlock( BlockBehaviour.Properties.of().strength(5.0F, 2.5F).sound(SoundType.WOOD).mapColor(MapColor.COLOR_BLACK)));
    public static final DeferredBlock<Block> WASTE_LEAVES =           registerBlock("waste_leaves", () -> new WasteLeavesBlock(   BlockBehaviour.Properties.of().strength(0.2F).randomTicks().mapColor(MapColor.COLOR_BROWN).sound(SoundType.GRASS).noOcclusion().isValidSpawn(Blocks::never).isSuffocating(NtmBlocks::never).isViewBlocking(NtmBlocks::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(NtmBlocks::never)));
    public static final DeferredBlock<Block> WASTE_PLANKS =           registerBlock("waste_planks", () -> new Block(              BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.WOOD).mapColor(MapColor.COLOR_BLACK)));
    public static final DeferredBlock<Block> FROZEN_DIRT =             registerBlock("frozen_dirt",   () -> new FrozenBlock(        BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.GLASS).mapColor(DyeColor.LIGHT_BLUE)));
    public static final DeferredBlock<Block> FROZEN_GRASS =            registerBlock("frozen_grass",  () -> new FrozenBlock(        BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.GLASS).mapColor(DyeColor.WHITE)));
    public static final DeferredBlock<RotatedPillarBlock> FROZEN_LOG = registerBlock("frozen_log",    () -> new RotatedPillarBlock( BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.GLASS).mapColor(DyeColor.LIGHT_BLUE)));
    public static final DeferredBlock<Block> FROZEN_PLANKS =           registerBlock("frozen_planks", () -> new Block(              BlockBehaviour.Properties.of().strength(0.5F, 2.5F).sound(SoundType.GLASS).mapColor(DyeColor.LIGHT_BLUE)));

    public static final DeferredBlock<Block> LEAVES_LAYER = registerBlock("leaves_layer",
            () -> new LayeringBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .randomTicks()
                    .mapColor(MapColor.COLOR_BROWN)
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isSuffocating(NtmBlocks::never)
                    .isViewBlocking(NtmBlocks::never)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor(NtmBlocks::never)));

    public static final DeferredBlock<Block> FALLOUT = registerBlock("fallout", () -> new FalloutBlock(BlockBehaviour.Properties.of().replaceable().strength(0.1F).sound(SoundType.GRAVEL).mapColor(MapColor.STONE)));
    public static final DeferredBlock<Block> SELLAFIELD_SLAKED = registerBlock("sellafield_slaked", () -> new SellafieldSlakedBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).isValidSpawn(Blocks::never).requiresCorrectToolForDrops().strength(3.0F, 10.0F).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_SELLAFIELD_DIAMOND = registerBlock("ore_sellafield_diamond", () -> new SellafieldSlakedBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).isValidSpawn(Blocks::never).requiresCorrectToolForDrops().strength(3.0F, 10.0F).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORE_SELLAFIELD_EMERALD = registerBlock("ore_sellafield_emerald", () -> new SellafieldSlakedBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).isValidSpawn(Blocks::never).requiresCorrectToolForDrops().strength(3.0F, 10.0F).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> SELLAFIELD_BEDROCK = registerBlock("sellafield_bedrock", () -> new SellafieldSlakedBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).isValidSpawn(Blocks::never).noLootTable().strength(-1.0F, 6000000.0F).sound(SoundType.STONE)));

    public static final DeferredBlock<Block> BLOCK_SCRAP = registerBlock("block_scrap",
            () -> new ColoredFallingBlock(new ColorRGBA(-8356741), BlockBehaviour.Properties.of()
                    .strength(2.5F)
                    .explosionResistance(5.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.GRAVEL)));
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

    public static final DeferredBlock<Block> RED_CABLE = registerBlock("red_cable", () -> new CableBlock(BlockBehaviour.Properties.of().strength(5.0F).explosionResistance(10.0F).noOcclusion().sound(SoundType.METAL)));
    public static final DeferredBlock<Block> FLUID_DUCT_NEO = registerBlockNew("fluid_duct_neo", () -> new FluidDuctStandardBlock(BlockBehaviour.Properties.of().strength(5.0F).explosionResistance(10.0F).noOcclusion().sound(ModSoundTypes.PIPE)));

    public static final DeferredBlock<Block> MACHINE_SATLINKER = registerBlock(
            "machine_satlinker",
            () -> new MachineSatLinkerBlock(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> MACHINE_PRESS = registerBlock(
            "machine_press",
            () -> new MachinePressBlock(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(10.0F)
                    .noOcclusion()
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> MACHINE_FLUID_TANK = registerBlock(
            "machine_fluid_tank",
            () -> new MachineFluidTankBlock(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(10.0F)
                    .noOcclusion()
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> MACHINE_ASSEMBLY_MACHINE = registerBlock(
            "machine_assembly_machine",
            () -> new MachineAssemblyMachineBlock(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(30.0F)
                    .noOcclusion()
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> MACHINE_BATTERY_SOCKET = registerBlock(
            "machine_battery_socket",
            () -> new MachineBatterySocketBlock(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(10.0F)
                    .noOcclusion()
                    .sound(SoundType.METAL)));
    public static final DeferredBlock<Block> MACHINE_BATTERY_REDD = registerBlock(
            "machine_battery_redd",
            () -> new MachineBatteryREDDBlock(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(10.0F)
                    .noOcclusion()
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> GAS_RADON =       registerBlock("gas_radon",       () -> new GasRadonBlock(      BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_RADON_DENSE = registerBlock("gas_radon_dense", () -> new GasRadonDenseBlock( BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_RADON_TOMB =  registerBlock("gas_radon_tomb",  () -> new GasRadonTombBlock(  BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_MELTDOWN =    registerBlock("gas_meltdown",    () -> new GasMeltdownBlock(   BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_MONOXIDE =    registerBlock("gas_monoxide",    () -> new GasMonoxideBlock(   BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_ASBESTOS =    registerBlock("gas_asbestos",    () -> new GasAsbestosBlock(   BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_COAL =        registerBlock("gas_coal",        () -> new GasCoalBlock(       BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_FLAMMABLE =   registerBlock("gas_flammable",   () -> new GasFlammableBlock(  BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));
    public static final DeferredBlock<Block> GAS_EXPLOSIVE =   registerBlock("gas_explosive",   () -> new GasExplosiveBlock(  BlockBehaviour.Properties.of().replaceable().noCollission().noOcclusion().noLootTable()));

    // E
    public static final DeferredBlock<Block> BALEFIRE =     BLOCKS.register("balefire",     () -> new BalefireBlock(     BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).replaceable().noCollission().noOcclusion().strength(0F).lightLevel(state -> 15).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> FIRE_DIGAMMA = BLOCKS.register("fire_digamma", () -> new DigammaFlameBlock( BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).replaceable().noCollission().noOcclusion().strength(0F, 150F).lightLevel(state -> 10).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> VOLCANO_CORE =     registerBlockNew("volcano_core",     () -> new VolcanoBlock(BlockBehaviour.Properties.of().strength(-1.0F, 10000.0F).mapColor(MapColor.NETHER)));
    public static final DeferredBlock<Block> VOLCANO_RAD_CORE = registerBlockNew("volcano_rad_core", () -> new VolcanoBlock(BlockBehaviour.Properties.of().strength(-1.0F, 10000.0F).mapColor(DyeColor.GREEN)));

    public static final DeferredBlock<LiquidBlock> VOLCANIC_LAVA = BLOCKS.register("volcanic_lava", () -> new VolcanicLiquidBlock(NtmFluids.VOLCANIC_LAVA.get(), BlockBehaviour.Properties.of().randomTicks().noCollission().replaceable().strength(500F).lightLevel(state -> 15).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY)));

    public static final DeferredBlock<Block> TAINT = registerBlock("taint", () -> new TaintBlock(BlockBehaviour.Properties.of().randomTicks().strength(15.0F, 10.0F).noLootTable().mapColor(DyeColor.GRAY)));

    public static final DeferredBlock<Block> BOBBLEHEAD = registerBlockNew("bobblehead", () -> new BobbleBlock(BlockBehaviour.Properties.of().noOcclusion().instabreak().sound(SoundType.WOOL).mapColor(DyeColor.WHITE)));
    public static final DeferredBlock<Block> PLUSHIE = registerBlockNew("plushie", () -> new PlushieBlock(BlockBehaviour.Properties.of().noOcclusion().instabreak().sound(SoundType.WOOL).mapColor(DyeColor.WHITE)));

    public static final DeferredBlock<Block> GEIGER = registerBlock(
            "geiger",
            () -> new GeigerCounterBlock(BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .explosionResistance(0.25F)
                    .sound(SoundType.METAL)));
    public static final DeferredBlock<Block> DECONTAMINATOR = registerBlock(
            "decontaminator",
            () -> new DecontaminatorBlock(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .explosionResistance(10.0F)
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> PWR_CONTROLLER = registerBlock(
            "pwr_controller",
            () -> new Block(BlockBehaviour.Properties.of().noLootTable()));

    //Nukes
    public static final DeferredBlock<Block> NUKE_GADGET =     registerBlock("nuke_gadget",     () -> new NukeGadgetBlock(    BlockBehaviour.Properties.of().strength(5.0F, 200.0F).requiresCorrectToolForDrops().noOcclusion().sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_LITTLE_BOY = registerBlock("nuke_little_boy", () -> new NukeLittleBoyBlock( BlockBehaviour.Properties.of().strength(5.0F, 200.0F).requiresCorrectToolForDrops().noOcclusion().sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_FAT_MAN =    registerBlock("nuke_fat_man",    () -> new NukeFatManBlock(    BlockBehaviour.Properties.of().strength(5.0F, 200.0F).requiresCorrectToolForDrops().noOcclusion().sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_IVY_MIKE =   registerBlock("nuke_ivy_mike",   () -> new NukeIvyMikeBlock(   BlockBehaviour.Properties.of().strength(5.0F, 200.0F).requiresCorrectToolForDrops().noOcclusion().sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_TSAR_BOMBA = registerBlock("nuke_tsar_bomba", () -> new NukeTsarBombaBlock( BlockBehaviour.Properties.of().strength(5.0F, 200.0F).requiresCorrectToolForDrops().noOcclusion().sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_PROTOTYPE =  registerBlock("nuke_prototype",  () -> new NukePrototypeBlock( BlockBehaviour.Properties.of().strength(5.0F, 200.0F).requiresCorrectToolForDrops().noOcclusion().sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_FLEIJA =     registerBlock("nuke_fleija",     () -> new NukeFleijaBlock(    BlockBehaviour.Properties.of().strength(5.0F, 200.0F).requiresCorrectToolForDrops().noOcclusion().sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_N2 =         registerBlock("nuke_n2",         () -> new NukeN2Block(        BlockBehaviour.Properties.of().strength(5.0F, 200.0F).requiresCorrectToolForDrops().noOcclusion().sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> NUKE_FSTBMB =     registerBlock("nuke_fstbmb",     () -> new NukeBalefireBlock(  BlockBehaviour.Properties.of().strength(5.0F, 200.0F).requiresCorrectToolForDrops().noOcclusion().sound(SoundType.METAL).mapColor(MapColor.METAL)));

    public static final DeferredBlock<Block> DET_CHARGE =  registerBlock("det_charge",  () -> new ExplosiveChargeBlock(BlockBehaviour.Properties.of().strength(0.1F).explosionResistance(0.0F).sound(SoundType.METAL)));
    public static final DeferredBlock<Block> DET_CORD =    registerBlock("det_cord",    () -> new DetCordBlock(BlockBehaviour.Properties.of().strength(0.1F).explosionResistance(0.0F).noOcclusion().isSuffocating(NtmBlocks::never).isViewBlocking(NtmBlocks::never).sound(SoundType.METAL)));
    public static final DeferredBlock<Block> DET_NUKE =    registerBlock("det_nuke",    () -> new ExplosiveChargeBlock(BlockBehaviour.Properties.of().strength(0.1F).explosionResistance(0.0F).sound(SoundType.METAL)));
    public static final DeferredBlock<Block> DET_MINER =   registerBlock("det_miner",   () -> new ExplosiveChargeBlock(BlockBehaviour.Properties.of().strength(0.1F).explosionResistance(0.0F).sound(SoundType.METAL)));
    public static final DeferredBlock<Block> BARREL_RED =   registerBlock("barrel_red",   () -> new RedBarrelBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.1F).explosionResistance(2.5F).sound(SoundType.METAL), true));
    public static final DeferredBlock<Block> BARREL_PINK =  registerBlock("barrel_pink",  () -> new RedBarrelBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.1F).explosionResistance(2.5F).sound(SoundType.METAL), true));
    public static final DeferredBlock<Block> BARREL_LOX =   registerBlock("barrel_lox",   () -> new RedBarrelBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.1F).explosionResistance(2.5F).sound(SoundType.METAL), false));
    public static final DeferredBlock<Block> BARREL_TAINT = registerBlock("barrel_taint", () -> new RedBarrelBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.1F).explosionResistance(2.5F).sound(SoundType.METAL), false));
    public static final DeferredBlock<Block> CRASHED_BOMB = registerBlockNew("crashed_bomb", () -> new CrashedBombBlock(BlockBehaviour.Properties.of().noLootTable().noOcclusion().strength(6000.0F).sound(SoundType.METAL).mapColor(MapColor.METAL)));
    public static final DeferredBlock<Block> MINE_AP =    registerBlock("mine_ap",    () -> new LandmineBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.0F, 0.0F).sound(SoundType.METAL).mapColor(MapColor.METAL), 1.5D, 1D));
    public static final DeferredBlock<Block> MINE_HE =    registerBlock("mine_he",    () -> new LandmineBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.0F, 0.0F).sound(SoundType.METAL).mapColor(MapColor.METAL), 2D, 5D));
    public static final DeferredBlock<Block> MINE_SHRAP = registerBlock("mine_shrap", () -> new LandmineBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.0F, 0.0F).sound(SoundType.METAL).mapColor(MapColor.METAL), 1.5D, 1D));
    public static final DeferredBlock<Block> MINE_FAT =   registerBlock("mine_fat",   () -> new LandmineBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.0F, 0.0F).sound(SoundType.METAL).mapColor(MapColor.METAL), 2.5D, 1D));
    public static final DeferredBlock<Block> MINE_NAVAL = registerBlock("mine_naval", () -> new LandmineBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.0F, 0.0F).sound(SoundType.METAL).mapColor(MapColor.METAL), 2.5D, 1D));
    public static final DeferredBlock<Block> DYNAMITE =     registerBlock("dynamite",     () -> new DynamiteBlock(    BlockBehaviour.Properties.of().instabreak().ignitedByLava().isRedstoneConductor(NtmBlocks::never).sound(SoundType.GRASS).mapColor(MapColor.FIRE)));
    public static final DeferredBlock<Block> TNT =          registerBlock("tnt",          () -> new TNTBlock(         BlockBehaviour.Properties.of().instabreak().ignitedByLava().isRedstoneConductor(NtmBlocks::never).sound(SoundType.GRASS).mapColor(MapColor.FIRE)));
    public static final DeferredBlock<Block> SEMTEX =       registerBlock("semtex",       () -> new SemtexBlock(      BlockBehaviour.Properties.of().instabreak().ignitedByLava().isRedstoneConductor(NtmBlocks::never).sound(SoundType.GRASS).mapColor(MapColor.FIRE)));
    public static final DeferredBlock<Block> C4 =           registerBlock("c4",           () -> new C4Block(          BlockBehaviour.Properties.of().instabreak().ignitedByLava().isRedstoneConductor(NtmBlocks::never).sound(SoundType.GRASS).mapColor(MapColor.FIRE)));
    public static final DeferredBlock<Block> FISSURE_BOMB = registerBlock("fissure_bomb", () -> new FissureBombBlock( BlockBehaviour.Properties.of().instabreak().ignitedByLava().isRedstoneConductor(NtmBlocks::never).sound(SoundType.GRASS).mapColor(MapColor.FIRE)));

    public static final DeferredBlock<Block> LAUNCH_PAD = registerBlock("launch_pad", () -> new LaunchPadBlock(BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 10.0F).mapColor(MapColor.METAL)));

    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_STAIRS = registerBlock(
            "brick_concrete_stairs",
            () -> new StairBlock(NtmBlocks.BRICK_CONCRETE.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_MOSSY_STAIRS = registerBlock(
            "brick_concrete_mossy_stairs",
            () -> new StairBlock(NtmBlocks.BRICK_CONCRETE_MOSSY.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_CRACKED_STAIRS = registerBlock(
            "brick_concrete_cracked_stairs",
            () -> new StairBlock(NtmBlocks.BRICK_CONCRETE_CRACKED.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(15.0F)
                    .requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_BROKEN_STAIRS = registerBlock(
            "brick_concrete_broken_stairs",
            () -> new StairBlock(NtmBlocks.BRICK_CONCRETE_BROKEN.get().defaultBlockState(), BlockBehaviour.Properties.of()
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

    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) { return true; }
    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) { return false; }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> defBlock = BLOCKS.register(name, block);
        NtmItems.ITEMS.register(name, () -> new BlockItem(defBlock.get(), new Properties()));
        return defBlock;
    }

    private static <T extends Block> DeferredBlock<T> registerBlockNew(String name, Supplier<T> block) {
        DeferredBlock<T> defBlock = BLOCKS.register(name, block);
        NtmItems.ITEMS.register(name, () -> new BlockItemBase(defBlock.get(), new Properties()));
        return defBlock;
    }

    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> block, Supplier<? extends BlockItem> blockItem) {
        DeferredBlock<T> defBlock = BLOCKS.register(name, block);
        NtmItems.ITEMS.register(name, blockItem);
        return defBlock;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
