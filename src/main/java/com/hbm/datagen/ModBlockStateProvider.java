package com.hbm.datagen;

import com.hbm.HBMsNTM;
import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.LayeringBlock;
import com.hbm.blocks.generic.SellafieldSlakedBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HBMsNTM.MODID, exFileHelper);
    }
    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.GAS_COAL.get(), cubeAll(ModBlocks.GAS_COAL.get()));
        simpleBlockWithItem(ModBlocks.BRICK_CONCRETE.get(), cubeAll(ModBlocks.BRICK_CONCRETE.get()));
        simpleBlockWithItem(ModBlocks.BRICK_CONCRETE_MOSSY.get(), cubeAll(ModBlocks.BRICK_CONCRETE_MOSSY.get()));
        simpleBlockWithItem(ModBlocks.BRICK_CONCRETE_CRACKED.get(), cubeAll(ModBlocks.BRICK_CONCRETE_CRACKED.get()));
        simpleBlockWithItem(ModBlocks.BRICK_CONCRETE_BROKEN.get(), cubeAll(ModBlocks.BRICK_CONCRETE_BROKEN.get()));
        simpleBlockWithItem(ModBlocks.BRICK_CONCRETE_MARKED.get(),
                models().cubeColumn("brick_concrete_marked",
                        modLoc("block/brick_concrete_marked"),
                        modLoc("block/brick_concrete")
        ));
        simpleBlockWithItem(ModBlocks.DET_NUKE.get(),
                models().cubeColumn("det_nuke",
                        modLoc("block/det_nuke_side"),
                        modLoc("block/det_nuke_top")
                ));
        simpleBlockWithItem(ModBlocks.DET_CHARGE.get(), cubeAll(ModBlocks.DET_CHARGE.get()));

        simpleBlockWithItem(ModBlocks.DET_MINER.get(),
                models().cubeColumn("det_miner",
                        modLoc("block/det_miner_side"),
                        modLoc("block/det_miner_top")
                ));

        simpleBlockWithItem(ModBlocks.MACHINE_SATLINKER.get(),
                models().cubeColumn("machine_satlinker",
                        modLoc("block/machine_satlinker_side"),
                        modLoc("block/machine_satlinker_top")
                ));

        simpleBlockWithItem(ModBlocks.BRICK_LIGHT.get(), cubeAll(ModBlocks.BRICK_LIGHT.get()));
        simpleBlockWithItem(ModBlocks.BRICK_OBSIDIAN.get(), cubeAll(ModBlocks.BRICK_OBSIDIAN.get()));
        simpleBlockWithItem(ModBlocks.GRAVEL_OBSIDIAN.get(), cubeAll(ModBlocks.GRAVEL_OBSIDIAN.get()));

        sellafieldSlaked(
                ModBlocks.SELLAFIELD_SLAKED.get(),
                "sellafield_slaked",
                SellafieldSlakedBlock.VARIANT,
                SellafieldSlakedBlock.COLOR_LEVEL
        );

        sellafieldSlaked(
                ModBlocks.SELLAFIELD_BEDROCK.get(),
                "sellafield_bedrock",
                SellafieldSlakedBlock.VARIANT,
                SellafieldSlakedBlock.COLOR_LEVEL
        );

        sellafieldOre(ModBlocks.ORE_SELLAFIELD_DIAMOND.get(),
                "sellafield_ore_diamond",
                "block/ore_diamond_overlay",
                SellafieldSlakedBlock.VARIANT,
                SellafieldSlakedBlock.COLOR_LEVEL);

        sellafieldOre(ModBlocks.ORE_SELLAFIELD_EMERALD.get(),
                "sellafield_ore_emerald",
                "block/ore_emerald_overlay",
                SellafieldSlakedBlock.VARIANT,
                SellafieldSlakedBlock.COLOR_LEVEL);

        logBlock((RotatedPillarBlock) ModBlocks.WASTE_LOG.get());

        simpleBlockWithItem(ModBlocks.WASTE_EARTH.get(),
                models().cubeBottomTop(
                        ModBlocks.WASTE_EARTH.getId().getPath(),
                        modLoc("block/waste_earth_side"),
                        modLoc("block/waste_earth_bottom"),
                        modLoc("block/waste_earth_top")
                )
        );

        simpleBlockWithItem(ModBlocks.DECONTAMINATOR.get(),
                models().cubeBottomTop(
                        ModBlocks.DECONTAMINATOR.getId().getPath(),
                        modLoc("block/decontaminator_side"),
                        modLoc("block/decontaminator_side"),
                        modLoc("block/decontaminator_top")
                )
        );

        simpleBlockWithItem(ModBlocks.PWR_CONTROLLER.get(),
                models().cube(
                        ModBlocks.PWR_CONTROLLER.getId().getPath(),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_controller"),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_casing_blank"),
                        modLoc("block/pwr_casing_blank")
                )
        );

        simpleBlockWithItem(ModBlocks.WASTE_LEAVES.get(),
                models().cubeColumn("waste_leaves", modLoc("block/waste_leaves"), modLoc("block/waste_leaves"))
                        .renderType("cutout_mipped")
        );
        simpleBlockWithItem(ModBlocks.WASTE_PLANKS.get(), cubeAll(ModBlocks.WASTE_PLANKS.get()));
        simpleBlockWithItem(ModBlocks.WASTE_TRINITITE.get(), cubeAll(ModBlocks.WASTE_TRINITITE.get()));
        simpleBlockWithItem(ModBlocks.WASTE_TRINITITE_RED.get(), cubeAll(ModBlocks.WASTE_TRINITITE_RED.get()));
        simpleBlockWithItem(ModBlocks.WASTE_MYCELIUM.get(),
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

        simpleBlockWithItem(ModBlocks.BLOCK_SCRAP.get(), cubeAll(ModBlocks.BLOCK_SCRAP.get()));

        simpleBlockWithItem(ModBlocks.ORE_OIL.get(), cubeAll(ModBlocks.ORE_OIL.get()));
        simpleBlockWithItem(ModBlocks.ORE_URANIUM.get(), cubeAll(ModBlocks.ORE_URANIUM.get()));
        simpleBlockWithItem(ModBlocks.ORE_URANIUM_SCORCHED.get(), cubeAll(ModBlocks.ORE_URANIUM_SCORCHED.get()));
        simpleBlockWithItem(ModBlocks.ORE_SCHRABIDIUM.get(), cubeAll(ModBlocks.ORE_SCHRABIDIUM.get()));
        simpleBlockWithItem(ModBlocks.ORE_NETHER_URANIUM.get(), cubeAll(ModBlocks.ORE_NETHER_URANIUM.get()));
        simpleBlockWithItem(ModBlocks.ORE_NETHER_URANIUM_SCORCHED.get(), cubeAll(ModBlocks.ORE_NETHER_URANIUM_SCORCHED.get()));
        simpleBlockWithItem(ModBlocks.ORE_NETHER_SCHRABIDIUM.get(), cubeAll(ModBlocks.ORE_NETHER_SCHRABIDIUM.get()));
        simpleBlockWithItem(ModBlocks.ORE_TIKITE.get(), cubeAll(ModBlocks.ORE_TIKITE.get()));
        simpleBlockWithItem(ModBlocks.ORE_GNEISS_URANIUM.get(), cubeAll(ModBlocks.ORE_GNEISS_URANIUM.get()));
        simpleBlockWithItem(ModBlocks.ORE_GNEISS_URANIUM_SCORCHED.get(), cubeAll(ModBlocks.ORE_GNEISS_URANIUM_SCORCHED.get()));
        simpleBlockWithItem(ModBlocks.ORE_NETHER_PLUTONIUM.get(), cubeAll(ModBlocks.ORE_NETHER_PLUTONIUM.get()));
        simpleBlockWithItem(ModBlocks.ORE_GNEISS_SCHRABIDIUM.get(), cubeAll(ModBlocks.ORE_GNEISS_SCHRABIDIUM.get()));

        slabBlock(ModBlocks.BRICK_CONCRETE_SLAB.get(), blockTexture(ModBlocks.BRICK_CONCRETE.get()), blockTexture(ModBlocks.BRICK_CONCRETE.get()));
        slabBlock(ModBlocks.BRICK_CONCRETE_MOSSY_SLAB.get(), blockTexture(ModBlocks.BRICK_CONCRETE_MOSSY.get()), blockTexture(ModBlocks.BRICK_CONCRETE_MOSSY.get()));
        slabBlock(ModBlocks.BRICK_CONCRETE_CRACKED_SLAB.get(), blockTexture(ModBlocks.BRICK_CONCRETE_CRACKED.get()), blockTexture(ModBlocks.BRICK_CONCRETE_CRACKED.get()));
        slabBlock(ModBlocks.BRICK_CONCRETE_BROKEN_SLAB.get(), blockTexture(ModBlocks.BRICK_CONCRETE_BROKEN.get()), blockTexture(ModBlocks.BRICK_CONCRETE_BROKEN.get()));

        stairsBlock(ModBlocks.BRICK_CONCRETE_STAIRS.get(), blockTexture(ModBlocks.BRICK_CONCRETE.get()));
        stairsBlock(ModBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get(), blockTexture(ModBlocks.BRICK_CONCRETE_MOSSY.get()));
        stairsBlock(ModBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get(), blockTexture(ModBlocks.BRICK_CONCRETE_CRACKED.get()));
        stairsBlock(ModBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get(), blockTexture(ModBlocks.BRICK_CONCRETE_BROKEN.get()));

        blockItem(ModBlocks.BRICK_CONCRETE_STAIRS);
        blockItem(ModBlocks.BRICK_CONCRETE_MOSSY_STAIRS);
        blockItem(ModBlocks.BRICK_CONCRETE_CRACKED_STAIRS);
        blockItem(ModBlocks.BRICK_CONCRETE_BROKEN_STAIRS);

        blockItem(ModBlocks.BRICK_CONCRETE_SLAB);
        blockItem(ModBlocks.BRICK_CONCRETE_MOSSY_SLAB);
        blockItem(ModBlocks.BRICK_CONCRETE_CRACKED_SLAB);
        blockItem(ModBlocks.BRICK_CONCRETE_BROKEN_SLAB);

        simpleBlockWithItem(ModBlocks.TAINT.get(), cubeAll(ModBlocks.TAINT.get()));

        simpleBlockWithItem(ModBlocks.BALEFIRE.get(),
                models().withExistingParent("balefire", mcLoc("block/cross"))
                        .renderType("minecraft:cutout_mipped")
                        .texture("cross", modLoc("block/balefire"))
        );
        simpleBlockWithItem(ModBlocks.FIRE_DIGAMMA.get(),
                models().withExistingParent("fire_digamma", mcLoc("block/cross"))
                        .renderType("minecraft:cutout_mipped")
                        .texture("cross", modLoc("block/fire_digamma"))
        );

        generateLayeringBlock(ModBlocks.LEAVES_LAYER.get());

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

        getVariantBuilder(ModBlocks.FALLOUT.get())
                .partialState().setModels(new ConfiguredModel(falloutModel));

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

    private void sellafieldSlaked(Block block, String modelBaseName, IntegerProperty variantProperty, Property<?>... ignored) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    int variant = state.getValue(variantProperty);
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

                    return ConfiguredModel.builder()
                            .modelFile(tintedModel)
                            .build();
                }, ignored);

        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/sellafield_slaked"));
    }

    private void sellafieldOre(Block block, String baseName, String overlayTexture, IntegerProperty variantProperty, Property<?>... ignored) {

        getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    int variant = state.getValue(variantProperty);
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

                    return ConfiguredModel.builder()
                            .modelFile(oreModel)
                            .build();
                }, ignored);

        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/" + baseName));
    }

    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("hbmsntm:block/" + deferredBlock.getId().getPath()));
    }
}
