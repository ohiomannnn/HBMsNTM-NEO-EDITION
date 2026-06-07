package com.hbm.datagen;

import com.google.gson.JsonObject;
import com.hbm.blocks.ICustomBlockModelRegister;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.generic.BarbedWireBlock;
import com.hbm.blocks.generic.LayeringBlock;
import com.hbm.blocks.generic.SellafieldSlakedBlock;
import com.hbm.blocks.network.FluidDuctConnectingBlock;
import com.hbm.blocks.states.NtmBlockStateProperties;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class NtmBlockStateProvider extends BlockStateProvider {

    public NtmBlockStateProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, NuclearTechMod.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {

        this.simpleBlockWithItem(NtmBlocks.ASPHALT, this.cubeAll(NtmBlocks.ASPHALT.get()));
        this.simpleBlockWithItem(NtmBlocks.ASPHALT_LIGHT, this.cubeAll(NtmBlocks.ASPHALT_LIGHT.get()));

        cub3All(NtmBlocks.BRICK_CONCRETE.get());
        cub3All(NtmBlocks.BRICK_CONCRETE_MOSSY.get());
        cub3All(NtmBlocks.BRICK_CONCRETE_CRACKED.get());
        cub3All(NtmBlocks.BRICK_CONCRETE_BROKEN.get());

        simpleColumnBlockWithItem(NtmBlocks.BRICK_CONCRETE_MARKED.get(), modLoc("block/brick_concrete_marked"), modLoc("block/brick_concrete"));

        this.particleOnlyBlock(NtmBlocks.PLUSHIE.get(), blockTexture(Blocks.WHITE_WOOL));

        this.particleOnlyBlock(NtmBlocks.LAUNCH_PAD.get(), blockTexture(NtmBlocks.LAUNCH_PAD.get()));

        cubeTop(NtmBlocks.MACHINE_SATLINKER.get());

        this.cubeAll(NtmBlocks.DET_CHARGE.get());
        this.cubeTop(NtmBlocks.DET_NUKE.get());
        this.cubeTop(NtmBlocks.DET_MINER.get());

        this.particleOnlyBlock(NtmBlocks.MACHINE_ASSEMBLY_MACHINE.get(), modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_BATTERY_REDD.get(), modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_BATTERY_SOCKET.get(), modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_FLUID_TANK.get(), modLoc("block/block_steel"));

        this.particleOnlyBlock(NtmBlocks.NUKE_GADGET.get(), blockTexture(NtmBlocks.NUKE_GADGET.get()));
        this.particleOnlyBlock(NtmBlocks.NUKE_LITTLE_BOY.get(), blockTexture(NtmBlocks.NUKE_LITTLE_BOY.get()));
        this.particleOnlyBlock(NtmBlocks.NUKE_FAT_MAN.get(), blockTexture(NtmBlocks.NUKE_FAT_MAN.get()));
        this.particleOnlyBlock(NtmBlocks.NUKE_IVY_MIKE.get(), blockTexture(NtmBlocks.NUKE_IVY_MIKE.get()));
        this.particleOnlyBlock(NtmBlocks.NUKE_TSAR_BOMBA.get(), blockTexture(NtmBlocks.NUKE_TSAR_BOMBA.get()));
        this.particleOnlyBlock(NtmBlocks.NUKE_PROTOTYPE.get(), blockTexture(NtmBlocks.NUKE_PROTOTYPE.get()));
        this.particleOnlyBlock(NtmBlocks.NUKE_FLEIJA.get(), blockTexture(NtmBlocks.NUKE_FLEIJA.get()));
        this.particleOnlyBlock(NtmBlocks.NUKE_N2.get(), blockTexture(NtmBlocks.NUKE_N2.get()));
        this.particleOnlyBlock(NtmBlocks.NUKE_FSTBMB.get(), blockTexture(NtmBlocks.NUKE_FSTBMB.get()));

        this.barrelLoaderBlockItem(NtmBlocks.BARREL_RED.get(), blockTexture(NtmBlocks.BARREL_RED.get()));
        this.barrelLoaderBlockItem(NtmBlocks.BARREL_PINK.get(), blockTexture(NtmBlocks.BARREL_PINK.get()));
        this.barrelLoaderBlockItem(NtmBlocks.BARREL_LOX.get(), blockTexture(NtmBlocks.BARREL_LOX.get()));
        this.barrelLoaderBlockItem(NtmBlocks.BARREL_TAINT.get(), blockTexture(NtmBlocks.BARREL_TAINT.get()));

        this.particleOnlyBlock(NtmBlocks.CRASHED_BOMB.get(), modLoc("block/block_rust"), true);

        this.cubeSideBottomTop(NtmBlocks.DYNAMITE.get());
        this.cubeSideBottomTop(NtmBlocks.TNT.get());
        this.cubeSideBottomTop(NtmBlocks.SEMTEX.get());
        this.cubeSideBottomTop(NtmBlocks.C4.get());
        this.cubeSideBottomTop(NtmBlocks.FISSURE_BOMB.get());

        for(Block block : BuiltInRegistries.BLOCK) {
            if(BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(NuclearTechMod.MODID)) {
                if(block instanceof ICustomBlockModelRegister icbmr) {
                    ResourceLocation loc = Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block));

                    icbmr.registerModel(this, loc);
                }
            }
        }

        cub3All(NtmBlocks.BRICK_LIGHT.get());
        cub3All(NtmBlocks.BRICK_OBSIDIAN.get());
        cub3All(NtmBlocks.GRAVEL_OBSIDIAN.get());

        sellafieldSlaked(
                NtmBlocks.SELLAFIELD_SLAKED.get(),
                "sellafield_slaked"
        );

        sellafieldSlaked(
                NtmBlocks.SELLAFIELD_BEDROCK.get(),
                "sellafield_bedrock"
        );

        sellafieldOre(NtmBlocks.ORE_SELLAFIELD_DIAMOND.get(), "sellafield_ore_diamond", "block/ore_diamond_overlay");
        sellafieldOre(NtmBlocks.ORE_SELLAFIELD_EMERALD.get(), "sellafield_ore_emerald", "block/ore_emerald_overlay");

        this.logBlock(NtmBlocks.WASTE_LOG.get());
        this.logBlock(NtmBlocks.FROZEN_LOG.get());
        this.cub3All(NtmBlocks.FROZEN_DIRT.get());
        this.cub3All(NtmBlocks.FROZEN_PLANKS.get());

        simpleBlockWithItem(NtmBlocks.FROZEN_GRASS.get(),
                models().cubeBottomTop(
                        NtmBlocks.FROZEN_GRASS.getId().getPath(),
                        modLoc("block/frozen_grass_side"),
                        modLoc("block/frozen_dirt"),
                        modLoc("block/frozen_grass_top")
                )
        );

        simpleBlockWithItem(NtmBlocks.WASTE_EARTH.get(),
                models().cubeBottomTop(
                        NtmBlocks.WASTE_EARTH.getId().getPath(),
                        modLoc("block/waste_earth_side"),
                        modLoc("block/waste_earth_bottom"),
                        modLoc("block/waste_earth_top")
                )
        );

        simpleBlockWithItem(NtmBlocks.DECONTAMINATOR.get(),
                models().cubeBottomTop(
                        NtmBlocks.DECONTAMINATOR.getId().getPath(),
                        modLoc("block/decontaminator_side"),
                        modLoc("block/decontaminator_side"),
                        modLoc("block/decontaminator_top")
                )
        );

        simpleBlockWithItem(NtmBlocks.PWR_CONTROLLER.get(),
                models().cube(
                        NtmBlocks.PWR_CONTROLLER.getId().getPath(),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_controller"),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_casing_blank")
                )
        );

        simpleBlockWithItem(NtmBlocks.WASTE_LEAVES.get(),
                models().cubeColumn("waste_leaves", modLoc("block/waste_leaves"), modLoc("block/waste_leaves"))
                        .renderType("cutout_mipped")
        );
        simpleBlockWithItem(NtmBlocks.WASTE_PLANKS.get(), cubeAll(NtmBlocks.WASTE_PLANKS.get()));
        simpleBlockWithItem(NtmBlocks.WASTE_TRINITITE.get(), cubeAll(NtmBlocks.WASTE_TRINITITE.get()));
        simpleBlockWithItem(NtmBlocks.WASTE_TRINITITE_RED.get(), cubeAll(NtmBlocks.WASTE_TRINITITE_RED.get()));
        simpleBlockWithItem(NtmBlocks.WASTE_MYCELIUM.get(),
                models().withExistingParent("waste_mycelium", mcLoc("block/block"))
                        .texture("particle", modLoc("block/waste_earth_bottom"))
                        .texture("bottom", modLoc("block/waste_earth_bottom"))
                        .texture("top", modLoc("block/waste_mycelium_top"))
                        .texture("side", modLoc("block/waste_mycelium_side"))
                        .element()
                        .from(0, 0, 0)
                        .to(16, 16, 16)
                        .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom").cullface(Direction.DOWN).end()
                        .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top").cullface(Direction.UP).tintindex(0).end()
                        .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#side").cullface(Direction.NORTH).end()
                        .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#side").cullface(Direction.SOUTH).end()
                        .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#side").cullface(Direction.WEST).end()
                        .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#side").cullface(Direction.EAST).end()
                        .end()
        );

        simpleBlockWithItem(NtmBlocks.BLOCK_SCRAP.get(), cubeAll(NtmBlocks.BLOCK_SCRAP.get()));

        simpleBlockWithItem(NtmBlocks.ORE_OIL.get(), cubeAll(NtmBlocks.ORE_OIL.get()));
        simpleBlockWithItem(NtmBlocks.ORE_URANIUM.get(), cubeAll(NtmBlocks.ORE_URANIUM.get()));
        simpleBlockWithItem(NtmBlocks.ORE_URANIUM_SCORCHED.get(), cubeAll(NtmBlocks.ORE_URANIUM_SCORCHED.get()));
        simpleBlockWithItem(NtmBlocks.ORE_SCHRABIDIUM.get(), cubeAll(NtmBlocks.ORE_SCHRABIDIUM.get()));
        simpleBlockWithItem(NtmBlocks.ORE_NETHER_URANIUM.get(), cubeAll(NtmBlocks.ORE_NETHER_URANIUM.get()));
        simpleBlockWithItem(NtmBlocks.ORE_NETHER_URANIUM_SCORCHED.get(), cubeAll(NtmBlocks.ORE_NETHER_URANIUM_SCORCHED.get()));
        simpleBlockWithItem(NtmBlocks.ORE_NETHER_SCHRABIDIUM.get(), cubeAll(NtmBlocks.ORE_NETHER_SCHRABIDIUM.get()));
        simpleBlockWithItem(NtmBlocks.ORE_TIKITE.get(), cubeAll(NtmBlocks.ORE_TIKITE.get()));
        simpleBlockWithItem(NtmBlocks.ORE_GNEISS_URANIUM.get(), cubeAll(NtmBlocks.ORE_GNEISS_URANIUM.get()));
        simpleBlockWithItem(NtmBlocks.ORE_GNEISS_URANIUM_SCORCHED.get(), cubeAll(NtmBlocks.ORE_GNEISS_URANIUM_SCORCHED.get()));
        simpleBlockWithItem(NtmBlocks.ORE_NETHER_PLUTONIUM.get(), cubeAll(NtmBlocks.ORE_NETHER_PLUTONIUM.get()));
        simpleBlockWithItem(NtmBlocks.ORE_GNEISS_SCHRABIDIUM.get(), cubeAll(NtmBlocks.ORE_GNEISS_SCHRABIDIUM.get()));

        this.slabBlock(NtmBlocks.BRICK_CONCRETE_SLAB.get(), blockTexture(NtmBlocks.BRICK_CONCRETE.get()), blockTexture(NtmBlocks.BRICK_CONCRETE.get()));
        this.slabBlock(NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_MOSSY.get()), blockTexture(NtmBlocks.BRICK_CONCRETE_MOSSY.get()));
        this.slabBlock(NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_CRACKED.get()), blockTexture(NtmBlocks.BRICK_CONCRETE_CRACKED.get()));
        this.slabBlock(NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_BROKEN.get()), blockTexture(NtmBlocks.BRICK_CONCRETE_BROKEN.get()));

        this.stairsBlock(NtmBlocks.BRICK_CONCRETE_STAIRS.get(), blockTexture(NtmBlocks.BRICK_CONCRETE.get()));
        this.stairsBlock(NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_MOSSY.get()));
        this.stairsBlock(NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_CRACKED.get()));
        this.stairsBlock(NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_BROKEN.get()));

        this.blockItem(NtmBlocks.BRICK_CONCRETE_STAIRS);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS);

        this.blockItem(NtmBlocks.BRICK_CONCRETE_SLAB);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB);

        simpleBlockWithItem(NtmBlocks.TAINT.get(), cubeAll(NtmBlocks.TAINT.get()));

        simpleBlockWithItem(NtmBlocks.BALEFIRE.get(),
                models().withExistingParent("balefire", mcLoc("block/cross"))
                        .renderType("minecraft:cutout_mipped")
                        .texture("cross", modLoc("block/balefire"))
        );
        simpleBlockWithItem(NtmBlocks.FIRE_DIGAMMA.get(),
                models().withExistingParent("fire_digamma", mcLoc("block/cross"))
                        .renderType("minecraft:cutout_mipped")
                        .texture("cross", modLoc("block/fire_digamma"))
        );

        generateLayeringBlock(NtmBlocks.LEAVES_LAYER.get());

        ResourceLocation texture = modLoc("block/ash");

        ModelFile falloutModel = models()
                .getBuilder("fallout")
                .parent(new ModelFile.UncheckedModelFile("block/block"))
                .texture("all", texture)
                .texture("particle", texture)
                .element()
                .from(0, 0, 0)
                .to(16, 2, 16)
                .face(Direction.UP).texture("#all").end()
                .face(Direction.DOWN).texture("#all").end()
                .face(Direction.NORTH).texture("#all").end()
                .face(Direction.SOUTH).texture("#all").end()
                .face(Direction.WEST).texture("#all").end()
                .face(Direction.EAST).texture("#all").end()
                .end();

        getVariantBuilder(NtmBlocks.FALLOUT.get()).partialState().setModels(new ConfiguredModel(falloutModel));

        this.registerCable();
        this.registerDetCord();
        this.registerFluidDuct();
        this.registerBarbedWire();
    }

    private void registerCable() {
        Block block = NtmBlocks.RED_CABLE.get();

        this.simpleBlock(block, this.models().getBuilder(this.key(block).getPath()).customLoader(CableBlockLoaderBuilder::new).texture("texture", modLoc("block/cable_neo")).end());
        this.entityBlockItem(block, false);
    }

    private void registerDetCord() {
        Block block = NtmBlocks.DET_CORD.get();

        this.simpleBlock(block, this.models().getBuilder(this.key(block).getPath()).customLoader(DetCordBlockLoaderBuilder::new).texture("texture", modLoc("block/det_cord")).end());
        this.entityBlockItem(block, false);
    }

    private void registerFluidDuct() {
        Block block = NtmBlocks.FLUID_DUCT_NEO.get();

        this.getVariantBuilder(block).forAllStatesExcept(state -> {

            int meta = state.getValue(NtmBlockStateProperties.META);
            
            ModelFile model;

            switch(meta) {
                case 2 -> model = this.models().getBuilder("hbmsntm:block/fluid_duct_silver").customLoader(DuctBlockLoaderBuilder::new).texture("texture", modLoc("block/pipe_silver")).texture("overlay", modLoc("block/pipe_silver_overlay")).end();
                case 1 -> model = this.models().getBuilder("hbmsntm:block/fluid_duct_colored").customLoader(DuctBlockLoaderBuilder::new).texture("texture", modLoc("block/pipe_colored")).texture("overlay", modLoc("block/pipe_colored_overlay")).end();
                default -> model = this.models().getBuilder("hbmsntm:block/fluid_duct_neo").customLoader(DuctBlockLoaderBuilder::new).texture("texture", modLoc("block/pipe_neo")).texture("overlay", modLoc("block/pipe_neo_overlay")).end();
            }

            return ConfiguredModel.builder().modelFile(model).build();
        }, FluidDuctConnectingBlock.NORTH, FluidDuctConnectingBlock.SOUTH, FluidDuctConnectingBlock.EAST, FluidDuctConnectingBlock.WEST, FluidDuctConnectingBlock.UP, FluidDuctConnectingBlock.DOWN);

        this.entityBlockItem(block, false);
    }

    private void registerBarbedWire() {
        Block block = NtmBlocks.BARBED_WIRE.get();

        this.getVariantBuilder(block).forAllStates(state -> {

            int subType = state.getValue(BarbedWireBlock.TYPE);

            ModelFile model;

            switch(subType) {
                case 5 -> model = this.models().getBuilder(this.key(block).getPath() + "_ultradeath").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_ultradeath")).end();
                case 4 -> model = this.models().getBuilder(this.key(block).getPath() + "_wither").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_wither")).end();
                case 3 -> model = this.models().getBuilder(this.key(block).getPath() + "_acid").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_acid")).end();
                case 2 -> model = this.models().getBuilder(this.key(block).getPath() + "_poison").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_poison")).end();
                case 1 -> model = this.models().getBuilder(this.key(block).getPath() + "_fire").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_fire")).end();
                default -> model = this.models().getBuilder(this.key(block).getPath()).customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire")).end();
            }

            return ConfiguredModel.builder().modelFile(model).build();
        });

        this.entityBlockItem(block, false);
    }

    private void barrelLoaderBlockItem(Block block, ResourceLocation texture) {
        this.simpleBlock(block, this.models().getBuilder(this.key(block).getPath()).customLoader(BarrelBlockModelBuilder::new).texture("texture", texture).end());
        this.entityBlockItem(block, false);
    }

    private void generateLayeringBlock(Block block) {
        ResourceLocation texture = modLoc("block/waste_leaves");

        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);

        for (int i = 1; i <= 8; i++) {
            float height = i * 2f / 16f;

            ModelFile model = models()
                    .withExistingParent("layering_" + i, mcLoc("block/block"))
                    .texture("all", texture)
                    .texture("particle", texture)
                    .renderType("cutout_mipped")
                    .element()
                    .from(0, 0, 0)
                    .to(16, height * 16, 16)
                    .face(Direction.UP).texture("#all").end()
                    .face(Direction.DOWN).texture("#all").end()
                    .face(Direction.NORTH).texture("#all").end()
                    .face(Direction.SOUTH).texture("#all").end()
                    .face(Direction.WEST).texture("#all").end()
                    .face(Direction.EAST).texture("#all").end()
                    .end();

            builder.part()
                    .modelFile(model)
                    .addModel()
                    .condition(LayeringBlock.LAYERS, i)
                    .end();
        }
    }

    private void sellafieldSlaked(Block block, String modelBaseName) {
        getVariantBuilder(block).forAllStatesExcept(state -> {
            int variant = state.getValue(SellafieldSlakedBlock.VARIANT);
            String modelName = modelBaseName + (variant == 0 ? "" : "_" + variant);
            String texName = "sellafield_slaked" + (variant == 0 ? "" : "_" + variant);

            ModelFile tintedModel = models().withExistingParent(modelName, mcLoc("block/cube"))
                    .texture("particle", modLoc("block/" + texName))
                    .texture("down", modLoc("block/" + texName))
                    .texture("up", modLoc("block/" + texName))
                    .texture("north", modLoc("block/" + texName))
                    .texture("south", modLoc("block/" + texName))
                    .texture("west", modLoc("block/" + texName))
                    .texture("east", modLoc("block/" + texName))
                    .element()
                    .from(0, 0, 0).to(16, 16, 16)
                    .allFaces((dir, face) -> face.texture("#" + dir.getName()).tintindex(0))
                    .end();

            return ConfiguredModel.builder().modelFile(tintedModel).build();
            }, SellafieldSlakedBlock.COLOR_LEVEL);

        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/sellafield_slaked"));
    }

    private void sellafieldOre(Block block, String baseName, String overlayTexture) {

        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            int variant = state.getValue(SellafieldSlakedBlock.VARIANT);
            String modelName = baseName + (variant == 0 ? "" : "_" + variant);
            String baseTex = "sellafield_slaked" + (variant == 0 ? "" : "_" + variant);

            ModelFile oreModel = models().withExistingParent(modelName, mcLoc("block/cube"))
                    .renderType("cutout")
                    .texture("base", modLoc("block/" + baseTex))
                    .texture("overlay", modLoc(overlayTexture))
                    .texture("particle", modLoc(overlayTexture))
                    .element()
                    .from(0, 0, 0).to(16, 16, 16)
                    .allFaces((dir, face) -> face.texture("#base").tintindex(0))
                    .end()
                    .element()
                    .from(0, 0, 0).to(16, 16, 16)
                    .allFaces((dir, face) -> face.texture("#overlay"))
                    .end();
            return ConfiguredModel.builder().modelFile(oreModel).build();

            }, SellafieldSlakedBlock.COLOR_LEVEL);

        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/" + baseName));
    }

    public void simpleColumnBlockWithItem(Block block, ResourceLocation side, ResourceLocation end) {
        simpleBlockWithItem(block, models().cubeColumn(name(block), side, end));
    }

    public void cub3All(Block block) {
        this.simpleBlock(block, cubeAll(block));
        this.simpleBlockItem(block, cubeAll(block));
    }

    public void simpleBlockWithItem(DeferredBlock<? extends Block> block, ModelFile model) {
        this.simpleBlockWithItem(block.get(), model);
    }

    private void particleOnlyBlock(Block block, ResourceLocation particleTexture) {
        this.particleOnlyBlock(block, particleTexture, false);
    }

    private void particleOnlyBlock(Block block, ResourceLocation particleTexture, boolean frontLight) {
        this.simpleBlock(block, this.models().getBuilder(name(block) + "_particle").texture("particle", particleTexture));
        this.entityBlockItem(block, frontLight);
    }

    public void cubeSideBottomTop(Block block) {
        String blockName = name(block);
        this.simpleBlock(block, this.models().cubeBottomTop(blockName, modLoc("block/" + blockName + "_side"), modLoc("block/" + blockName + "_bottom"), modLoc("block/" + blockName + "_top")));
        this.simpleBlockItem(block, this.models().cubeBottomTop(blockName, modLoc("block/" + blockName + "_side"), modLoc("block/" + blockName + "_bottom"), modLoc("block/" + blockName + "_top")));
    }

    public void cubeTop(Block block) {
        String blockName = name(block);
        this.simpleBlock(block, this.models().cubeTop(blockName, modLoc("block/" + blockName + "_side"), modLoc("block/" + blockName + "_top")));
        this.simpleBlockItem(block, this.models().cubeTop(blockName, modLoc("block/" + blockName + "_side"), modLoc("block/" + blockName + "_top")));
    }

    private void entityBlockItem(Block block, boolean frontLight) {
        this.itemModels().getBuilder(this.key(block).getPath()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).guiLight(frontLight ? BlockModel.GuiLight.FRONT : BlockModel.GuiLight.SIDE);
    }

    private String name(Block block) { return this.key(block).getPath(); }
    private ResourceLocation key(Block block) { return BuiltInRegistries.BLOCK.getKey(block); }

    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("hbmsntm:block/" + deferredBlock.getId().getPath()));
    }

    protected static class DuctBlockLoaderBuilder extends BlockModelBuilderBase {
        public DuctBlockLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(NuclearTechMod.withDefaultNamespace("pipe_geometry_loader"), parent, helper);
        }
    }
    protected static class BarrelBlockModelBuilder extends BlockModelBuilderBase {
        public BarrelBlockModelBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(NuclearTechMod.withDefaultNamespace("barrel_geometry_loader"), parent, helper);
        }
    }
    protected static class CableBlockLoaderBuilder extends BlockModelBuilderBase {
        public CableBlockLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(NuclearTechMod.withDefaultNamespace("cable_geometry_loader"), parent, helper);
        }
    }
    protected static class DetCordBlockLoaderBuilder extends BlockModelBuilderBase {
        public DetCordBlockLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(NuclearTechMod.withDefaultNamespace("det_cord_geometry_loader"), parent, helper);
        }
    }
    protected static class BarbedWireBlockLoaderBuilder extends BlockModelBuilderBase {
        public BarbedWireBlockLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(NuclearTechMod.withDefaultNamespace("barbed_wire_geometry_loader"), parent, helper);
        }
    }

    public static abstract class BlockModelBuilderBase extends CustomLoaderBuilder<BlockModelBuilder> {

        private final Map<String, ResourceLocation> textures = new LinkedHashMap<>();

        protected BlockModelBuilderBase(ResourceLocation loaderId, BlockModelBuilder parent, ExistingFileHelper helper) {
            super(loaderId, parent, helper, false);
        }

        public BlockModelBuilderBase texture(String key, ResourceLocation location) {
            this.textures.put(key, location);
            return this;
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            super.toJson(json);

            JsonObject texturesObject = new JsonObject();
            for(Entry<String, ResourceLocation> entry : this.textures.entrySet()) {
                texturesObject.addProperty(entry.getKey(), entry.getValue().toString());
            }
            json.add("textures", texturesObject);

            return json;
        }
    }

}
