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
import com.hbm.render.model.loader.NtmGeometry.BakedModelType;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class NtmBlockStateProvider extends BlockStateProvider {

    public NtmBlockStateProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, NuclearTechMod.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {

        for(Block block : BuiltInRegistries.BLOCK) {
            if(BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(NuclearTechMod.MODID)) {
                if(block instanceof ICustomBlockModelRegister icbmr) {
                    ResourceLocation loc = Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block));
                    icbmr.registerModel(this, loc);
                }
            }
        }

        this.simpleCubeAllBlock(NtmBlocks.ORE_OIL);
        this.simpleCubeAllBlock(NtmBlocks.ORE_URANIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_URANIUM_SCORCHED);
        this.simpleCubeAllBlock(NtmBlocks.ORE_SCHRABIDIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_NETHER_URANIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_NETHER_URANIUM_SCORCHED);
        this.simpleCubeAllBlock(NtmBlocks.ORE_NETHER_SCHRABIDIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_TIKITE);
        this.simpleCubeAllBlock(NtmBlocks.ORE_GNEISS_URANIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_GNEISS_URANIUM_SCORCHED);
        this.simpleCubeAllBlock(NtmBlocks.ORE_NETHER_PLUTONIUM);
        this.simpleCubeAllBlock(NtmBlocks.ORE_GNEISS_SCHRABIDIUM);

        this.simpleCubeAllBlock(NtmBlocks.BLOCK_SCRAP);

        this.particleOnlyBlock(NtmBlocks.BOBBLEHEAD, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.PLUSHIE, modLoc("block/block_fiberglass_side"));

        this.simpleCubeAllBlock(NtmBlocks.GRAVEL_OBSIDIAN);
        this.simpleCubeAllBlock(NtmBlocks.GRAVEL_DIAMOND);

        this.simpleCubeAllBlock(NtmBlocks.ASPHALT);
        this.simpleCubeAllBlock(NtmBlocks.ASPHALT_LIGHT);

        this.simpleCubeAllBlock(NtmBlocks.BRICK_CONCRETE);
        this.simpleCubeAllBlock(NtmBlocks.BRICK_CONCRETE_MOSSY);
        this.simpleCubeAllBlock(NtmBlocks.BRICK_CONCRETE_CRACKED);
        this.simpleCubeAllBlock(NtmBlocks.BRICK_CONCRETE_BROKEN);
        this.simpleBlockWithItem(
                NtmBlocks.BRICK_CONCRETE_MARKED,
                this.models().cubeColumn(
                        name(NtmBlocks.BRICK_CONCRETE_MARKED),
                        blockTexture(NtmBlocks.BRICK_CONCRETE_MARKED),
                        blockTexture(NtmBlocks.BRICK_CONCRETE)
                )
        );
        this.simpleCubeAllBlock(NtmBlocks.BRICK_OBSIDIAN);
        this.simpleCubeAllBlock(NtmBlocks.BRICK_LIGHT);
        this.simpleCubeAllBlock(NtmBlocks.BRICK_ASBESTOS);
        this.simpleCubeAllBlock(NtmBlocks.BRICK_FIRE);

        this.slabBlock(NtmBlocks.BRICK_CONCRETE_SLAB.get(), blockTexture(NtmBlocks.BRICK_CONCRETE), blockTexture(NtmBlocks.BRICK_CONCRETE));
        this.slabBlock(NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_MOSSY), blockTexture(NtmBlocks.BRICK_CONCRETE_MOSSY));
        this.slabBlock(NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_CRACKED), blockTexture(NtmBlocks.BRICK_CONCRETE_CRACKED));
        this.slabBlock(NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_BROKEN), blockTexture(NtmBlocks.BRICK_CONCRETE_BROKEN));
        this.blockItem(NtmBlocks.BRICK_CONCRETE_SLAB);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB);

        this.stairsBlock(NtmBlocks.BRICK_CONCRETE_STAIRS.get(), blockTexture(NtmBlocks.BRICK_CONCRETE));
        this.stairsBlock(NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_MOSSY));
        this.stairsBlock(NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_CRACKED));
        this.stairsBlock(NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get(), blockTexture(NtmBlocks.BRICK_CONCRETE_BROKEN));
        this.blockItem(NtmBlocks.BRICK_CONCRETE_STAIRS);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS);
        this.blockItem(NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS);

        this.registerBarbedWire();
        this.registerSpikes();

        this.simpleCubeBottomTopBlock(NtmBlocks.WASTE_EARTH);
        this.simpleBlockWithItem(
                NtmBlocks.WASTE_MYCELIUM,
                this.models().cubeBottomTop(
                        name(NtmBlocks.WASTE_MYCELIUM),
                        blockTexture(NtmBlocks.WASTE_MYCELIUM, "_side"),
                        blockTexture(NtmBlocks.WASTE_EARTH, "_bottom"),
                        blockTexture(NtmBlocks.WASTE_MYCELIUM, "_top")
                )
        );
        this.simpleCubeAllBlock(NtmBlocks.WASTE_TRINITITE);
        this.simpleCubeAllBlock(NtmBlocks.WASTE_TRINITITE_RED);
        this.logBlock(NtmBlocks.WASTE_LOG.get());
        this.simpleBlockWithItem(
                NtmBlocks.WASTE_LEAVES,
                this.models().cubeAll(
                        name(NtmBlocks.WASTE_LEAVES),
                        blockTexture(NtmBlocks.WASTE_LEAVES)
                ).renderType("cutout_mipped")
        );
        this.simpleCubeAllBlock(NtmBlocks.WASTE_PLANKS);
        this.simpleCubeAllBlock(NtmBlocks.FROZEN_DIRT);
        this.simpleBlockWithItem(
                NtmBlocks.FROZEN_GRASS,
                this.models().cubeBottomTop(
                        name(NtmBlocks.FROZEN_GRASS),
                        blockTexture(NtmBlocks.FROZEN_GRASS, "_side"),
                        blockTexture(NtmBlocks.FROZEN_DIRT),
                        blockTexture(NtmBlocks.FROZEN_GRASS, "_top")
                )
        );
        this.logBlock(NtmBlocks.FROZEN_LOG.get());
        this.simpleCubeAllBlock(NtmBlocks.FROZEN_PLANKS);
        this.layeringBlock(NtmBlocks.LEAVES_LAYER.get());
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
        this.getVariantBuilder(NtmBlocks.FALLOUT.get()).partialState().setModels(new ConfiguredModel(falloutModel));
        this.sellafieldSlaked(NtmBlocks.SELLAFIELD_SLAKED.get(), "sellafield_slaked");
        this.sellafieldOre(NtmBlocks.ORE_SELLAFIELD_DIAMOND.get(), "sellafield_ore_diamond", "block/ore_diamond_overlay");
        this.sellafieldOre(NtmBlocks.ORE_SELLAFIELD_EMERALD.get(), "sellafield_ore_emerald", "block/ore_emerald_overlay");
        this.sellafieldSlaked(NtmBlocks.SELLAFIELD_BEDROCK.get(), "sellafield_bedrock");

        this.particleOnlyBlock(NtmBlocks.NUKE_GADGET, blockTexture(NtmBlocks.NUKE_GADGET));
        this.particleOnlyBlock(NtmBlocks.NUKE_LITTLE_BOY, blockTexture(NtmBlocks.NUKE_LITTLE_BOY));
        this.particleOnlyBlock(NtmBlocks.NUKE_FAT_MAN, blockTexture(NtmBlocks.NUKE_FAT_MAN));
        this.particleOnlyBlock(NtmBlocks.NUKE_IVY_MIKE, blockTexture(NtmBlocks.NUKE_IVY_MIKE));
        this.particleOnlyBlock(NtmBlocks.NUKE_TSAR_BOMBA, blockTexture(NtmBlocks.NUKE_TSAR_BOMBA));
        this.particleOnlyBlock(NtmBlocks.NUKE_PROTOTYPE, blockTexture(NtmBlocks.NUKE_PROTOTYPE));
        this.particleOnlyBlock(NtmBlocks.NUKE_FLEIJA, blockTexture(NtmBlocks.NUKE_FLEIJA));
        this.particleOnlyBlock(NtmBlocks.NUKE_N2, blockTexture(NtmBlocks.NUKE_N2));
        this.particleOnlyBlock(NtmBlocks.NUKE_FSTBMB, blockTexture(NtmBlocks.NUKE_FSTBMB));

        this.particleOnlyBlock(NtmBlocks.CRASHED_BOMB, modLoc("block/block_rust"), true);
        this.simpleCubeBottomTopBlock(NtmBlocks.DYNAMITE);
        this.simpleCubeBottomTopBlock(NtmBlocks.TNT);
        this.simpleCubeBottomTopBlock(NtmBlocks.SEMTEX);
        this.simpleCubeBottomTopBlock(NtmBlocks.C4);
        this.simpleCubeBottomTopBlock(NtmBlocks.FISSURE_BOMB);

        this.particleOnlyBlock(NtmBlocks.MINE_AP, blockTexture(NtmBlocks.MINE_AP));
        this.particleOnlyBlock(NtmBlocks.MINE_HE, blockTexture(NtmBlocks.MINE_HE));
        this.particleOnlyBlock(NtmBlocks.MINE_SHRAP, blockTexture(NtmBlocks.MINE_SHRAP));
        this.particleOnlyBlock(NtmBlocks.MINE_FAT, blockTexture(NtmBlocks.MINE_FAT));
        this.particleOnlyBlock(NtmBlocks.MINE_NAVAL, blockTexture(NtmBlocks.MINE_NAVAL));

        this.simpleCubeAllBlock(NtmBlocks.DET_CHARGE);
        this.registerDetCord();
        this.cubeTop(NtmBlocks.DET_NUKE);
        this.cubeTop(NtmBlocks.DET_MINER);
        this.barrelLoaderBlockItem(NtmBlocks.BARREL_RED.get(), blockTexture(NtmBlocks.BARREL_RED));
        this.barrelLoaderBlockItem(NtmBlocks.BARREL_PINK.get(), blockTexture(NtmBlocks.BARREL_PINK));
        this.barrelLoaderBlockItem(NtmBlocks.BARREL_LOX.get(), blockTexture(NtmBlocks.BARREL_LOX));
        this.barrelLoaderBlockItem(NtmBlocks.BARREL_TAINT.get(), blockTexture(NtmBlocks.BARREL_TAINT));

        this.particleOnlyBlock(NtmBlocks.GEIGER, blockTexture(NtmBlocks.GEIGER));

        this.particleOnlyBlock(NtmBlocks.MACHINE_PRESS, modLoc("block/block_steel"));

        this.registerCable();

        this.registerFluidDuct();

        this.particleOnlyBlock(NtmBlocks.MACHINE_BATTERY_SOCKET, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_BATTERY_REDD, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_ASSEMBLY_MACHINE, modLoc("block/block_steel"));
        this.particleOnlyBlock(NtmBlocks.MACHINE_FLUID_TANK, modLoc("block/block_steel"));

        this.cubeTop(NtmBlocks.MACHINE_SATLINKER);

        this.simpleBlockWithItem(
                NtmBlocks.DECONTAMINATOR,
                this.models().cubeBottomTop(
                        name(NtmBlocks.DECONTAMINATOR),
                        blockTexture(NtmBlocks.DECONTAMINATOR, "_side"),
                        blockTexture(NtmBlocks.DECONTAMINATOR, "_side"),
                        blockTexture(NtmBlocks.DECONTAMINATOR, "_top")
                )
        );

        this.simpleBlockWithItem(NtmBlocks.PWR_CONTROLLER.get(),
                this.models().cube(
                        NtmBlocks.PWR_CONTROLLER.getId().getPath(),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_controller"),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_casing_blank")
                )
        );

        this.simpleBlock(NtmBlocks.BALEFIRE.get(), this.models().withExistingParent("balefire", mcLoc("block/cross")).renderType("cutout_mipped").texture("cross", modLoc("block/balefire")));
        this.simpleBlock(NtmBlocks.FIRE_DIGAMMA.get(), this.models().withExistingParent("fire_digamma", mcLoc("block/cross")).renderType("cutout_mipped").texture("cross", modLoc("block/fire_digamma")));
        //VOLCANO_CORE uses custom register!
        //VOLCANO_RAD_CORE uses custom register!

        this.particleOnlyBlock(NtmBlocks.LAUNCH_PAD, blockTexture(NtmBlocks.LAUNCH_PAD));

        this.itemModels().basicItem(NtmBlocks.GAS_RADON.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_RADON_DENSE.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_RADON_TOMB.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_MELTDOWN.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_MONOXIDE.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_ASBESTOS.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_COAL.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_FLAMMABLE.asItem());
        this.itemModels().basicItem(NtmBlocks.GAS_EXPLOSIVE.asItem());

        this.simpleCubeAllBlock(NtmBlocks.TAINT);
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

            int subType = state.getValue(BarbedWireBlock.SUBTYPE);

            ModelFile model;

            switch(subType) {
                case 5 -> model = this.models().getBuilder(this.name(block) + "_ultradeath").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_ultradeath")).end();
                case 4 -> model = this.models().getBuilder(this.name(block) + "_wither").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_wither")).end();
                case 3 -> model = this.models().getBuilder(this.name(block) + "_acid").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_acid")).end();
                case 2 -> model = this.models().getBuilder(this.name(block) + "_poison").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_poison")).end();
                case 1 -> model = this.models().getBuilder(this.name(block) + "_fire").customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire_fire")).end();
                default -> model = this.models().getBuilder(this.name(block)).customLoader(BarbedWireBlockLoaderBuilder::new).texture("texture", modLoc("block/barbed_wire")).end();
            }

            return ConfiguredModel.builder().modelFile(model).build();
        });

        this.entityBlockItem(block, false);
    }

    private void registerSpikes() {
        Block block = NtmBlocks.SPIKES.get();

        this.simpleBlock(block, this.models().getBuilder(this.key(block).getPath()).customLoader(SpikesLoaderBuilder::new).texture("texture", modLoc("block/spikes")).end());
        this.entityBlockItem(block, false);
    }

    private void barrelLoaderBlockItem(Block block, ResourceLocation texture) {
        this.simpleBlock(block, this.models().getBuilder(this.key(block).getPath()).customLoader(BarrelBlockModelBuilder::new).texture("texture", texture).end());
        this.entityBlockItem(block, false);
    }

    private void layeringBlock(Block block) {
        ResourceLocation texture = modLoc("block/waste_leaves");

        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);

        for(int i = 1; i <= 8; i++) {
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

    public void simpleBlockWithItem(DeferredBlock<? extends Block> block, ModelFile model) {
        this.simpleBlockWithItem(block.get(), model);
    }

    /** Creates block with item, uses cube all model */
    public void simpleCubeAllBlock(DeferredBlock<? extends Block> block) { this.simpleBlockWithItem(block.get(), this.cubeAll(block.get())); }

    private void particleOnlyBlock(DeferredBlock<? extends Block> block, ResourceLocation particleTexture) { this.particleOnlyBlock(block, particleTexture, false); }
    private void particleOnlyBlock(DeferredBlock<? extends Block> block, ResourceLocation particleTexture, boolean frontLight) {
        this.simpleBlock(block.get(), this.models().getBuilder(name(block) + "_particle").texture("particle", particleTexture));
        this.entityBlockItem(block.get(), frontLight);
    }

    private void entityBlockItem(Block block, boolean frontLight) {
        this.itemModels().getBuilder(this.key(block).getPath()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).guiLight(frontLight ? BlockModel.GuiLight.FRONT : BlockModel.GuiLight.SIDE);
    }

    public void simpleCubeBottomTopBlock(DeferredBlock<? extends Block> block) {
        String blockName = name(block);
        this.simpleBlockWithItem(block, this.models().cubeBottomTop(blockName, modLoc("block/" + blockName + "_side"), modLoc("block/" + blockName + "_bottom"), modLoc("block/" + blockName + "_top")));
    }

    public void cubeTop(DeferredBlock<? extends Block> block) {
        String blockName = name(block);
        this.simpleBlockWithItem(block, this.models().cubeTop(blockName, modLoc("block/" + blockName + "_side"), modLoc("block/" + blockName + "_top")));
    }

    protected String name(Block block) { return this.key(block).getPath(); }
    protected String name(DeferredBlock<? extends Block> block) { return this.key(block).getPath(); }
    protected ResourceLocation key(Block block) { return BuiltInRegistries.BLOCK.getKey(block); }
    protected ResourceLocation key(DeferredBlock<? extends Block> block) { return BuiltInRegistries.BLOCK.getKey(block.get()); }
    protected ResourceLocation blockTexture(DeferredBlock<? extends Block> block) { ResourceLocation name = this.key(block); return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "block/" + name.getPath()); }
    protected ResourceLocation blockTexture(DeferredBlock<? extends Block> block, String toAppend) { ResourceLocation name = this.key(block); return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "block/" + name.getPath() + toAppend); }

    private void blockItem(DeferredBlock<? extends Block> block) { this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile("hbmsntm:block/" + block.getId().getPath())); }

    protected static class DuctBlockLoaderBuilder extends BlockModelBuilderBase {
        public DuctBlockLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }
        @Override public BakedModelType getType() { return BakedModelType.PIPE; }
    }
    protected static class BarrelBlockModelBuilder extends BlockModelBuilderBase {
        public BarrelBlockModelBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }
        @Override public BakedModelType getType() { return BakedModelType.BARREL; }
    }
    protected static class CableBlockLoaderBuilder extends BlockModelBuilderBase {
        public CableBlockLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }
        @Override public BakedModelType getType() { return BakedModelType.CABLE; }
    }
    protected static class DetCordBlockLoaderBuilder extends BlockModelBuilderBase {
        public DetCordBlockLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }
        @Override public BakedModelType getType() { return BakedModelType.DET_CORD; }
    }
    protected static class BarbedWireBlockLoaderBuilder extends BlockModelBuilderBase {
        public BarbedWireBlockLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }
        @Override public BakedModelType getType() { return BakedModelType.BARBED_WIRE; }
    }
    protected static class SpikesLoaderBuilder extends BlockModelBuilderBase {
        public SpikesLoaderBuilder(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(parent, helper);
        }
        @Override public BakedModelType getType() { return BakedModelType.SPIKES; }
    }

    public static abstract class BlockModelBuilderBase extends CustomLoaderBuilder<BlockModelBuilder> {

        private final Map<String, ResourceLocation> textures = new LinkedHashMap<>();

        protected BlockModelBuilderBase(BlockModelBuilder parent, ExistingFileHelper helper) {
            super(NuclearTechMod.withDefaultNamespace("ntm_geometry_loader"), parent, helper, false);
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
            json.addProperty("type", this.getType().name().toLowerCase(Locale.US));

            return json;
        }

        public abstract BakedModelType getType();
    }
}
