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
import net.minecraft.world.level.block.Blocks;
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
        cub3All(ModBlocks.BRICK_CONCRETE.get());
        cub3All(ModBlocks.BRICK_CONCRETE_MOSSY.get());
        cub3All(ModBlocks.BRICK_CONCRETE_CRACKED.get());
        cub3All(ModBlocks.BRICK_CONCRETE_BROKEN.get());

        simpleColumnBlockWithItem(ModBlocks.BRICK_CONCRETE_MARKED.get(), modLoc("block/brick_concrete_marked"), modLoc("block/brick_concrete"));

        this.particleOnlyBlock(ModBlocks.PLUSHIE_YOMI.get(), mcLoc("block/" + name(Blocks.WHITE_WOOL)));
        this.particleOnlyBlock(ModBlocks.PLUSHIE_NUMBERNINE.get(), mcLoc("block/" + name(Blocks.WHITE_WOOL)));
        this.particleOnlyBlock(ModBlocks.PLUSHIE_HUNDUN.get(), mcLoc("block/" + name(Blocks.WHITE_WOOL)));
        this.particleOnlyBlock(ModBlocks.PLUSHIE_DERG.get(), mcLoc("block/" + name(Blocks.WHITE_WOOL)));

        cubeTop(ModBlocks.MACHINE_SATLINKER.get());

        this.cubeAll(ModBlocks.DET_CHARGE.get());
        this.particleOnlyBlock(ModBlocks.DET_CORD.get(), mcLoc("block/" + name(ModBlocks.DET_CORD.get())));
        this.cubeTop(ModBlocks.DET_NUKE.get());
        this.cubeTop(ModBlocks.DET_MINER.get());

        this.particleOnlyBlock(ModBlocks.BARREL_RED.get(), modLoc("block/" + name(ModBlocks.BARREL_RED.get())));
        this.particleOnlyBlock(ModBlocks.BARREL_PINK.get(), modLoc("block/" + name(ModBlocks.BARREL_PINK.get())));

        this.cubeSideBottomTop(ModBlocks.DYNAMITE.get());
        this.cubeSideBottomTop(ModBlocks.TNT.get());
        this.cubeSideBottomTop(ModBlocks.SEMTEX.get());
        this.cubeSideBottomTop(ModBlocks.C4.get());
        this.cubeSideBottomTop(ModBlocks.FISSURE_BOMB.get());

        cub3All(ModBlocks.BRICK_LIGHT.get());
        cub3All(ModBlocks.BRICK_OBSIDIAN.get());
        cub3All(ModBlocks.GRAVEL_OBSIDIAN.get());

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

        sellafieldOre(ModBlocks.ORE_SELLAFIELD_DIAMOND.get(), "sellafield_ore_diamond", "block/ore_diamond_overlay", SellafieldSlakedBlock.VARIANT, SellafieldSlakedBlock.COLOR_LEVEL);
        sellafieldOre(ModBlocks.ORE_SELLAFIELD_EMERALD.get(), "sellafield_ore_emerald", "block/ore_emerald_overlay", SellafieldSlakedBlock.VARIANT, SellafieldSlakedBlock.COLOR_LEVEL);

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

        this.slabBlock(ModBlocks.BRICK_CONCRETE_SLAB.get(), blockTexture(ModBlocks.BRICK_CONCRETE.get()), blockTexture(ModBlocks.BRICK_CONCRETE.get()));
        this.slabBlock(ModBlocks.BRICK_CONCRETE_MOSSY_SLAB.get(), blockTexture(ModBlocks.BRICK_CONCRETE_MOSSY.get()), blockTexture(ModBlocks.BRICK_CONCRETE_MOSSY.get()));
        this.slabBlock(ModBlocks.BRICK_CONCRETE_CRACKED_SLAB.get(), blockTexture(ModBlocks.BRICK_CONCRETE_CRACKED.get()), blockTexture(ModBlocks.BRICK_CONCRETE_CRACKED.get()));
        this.slabBlock(ModBlocks.BRICK_CONCRETE_BROKEN_SLAB.get(), blockTexture(ModBlocks.BRICK_CONCRETE_BROKEN.get()), blockTexture(ModBlocks.BRICK_CONCRETE_BROKEN.get()));

        this.stairsBlock(ModBlocks.BRICK_CONCRETE_STAIRS.get(), blockTexture(ModBlocks.BRICK_CONCRETE.get()));
        this.stairsBlock(ModBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get(), blockTexture(ModBlocks.BRICK_CONCRETE_MOSSY.get()));
        this.stairsBlock(ModBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get(), blockTexture(ModBlocks.BRICK_CONCRETE_CRACKED.get()));
        this.stairsBlock(ModBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get(), blockTexture(ModBlocks.BRICK_CONCRETE_BROKEN.get()));

        this.blockItem(ModBlocks.BRICK_CONCRETE_STAIRS);
        this.blockItem(ModBlocks.BRICK_CONCRETE_MOSSY_STAIRS);
        this.blockItem(ModBlocks.BRICK_CONCRETE_CRACKED_STAIRS);
        this.blockItem(ModBlocks.BRICK_CONCRETE_BROKEN_STAIRS);

        this.blockItem(ModBlocks.BRICK_CONCRETE_SLAB);
        this.blockItem(ModBlocks.BRICK_CONCRETE_MOSSY_SLAB);
        this.blockItem(ModBlocks.BRICK_CONCRETE_CRACKED_SLAB);
        this.blockItem(ModBlocks.BRICK_CONCRETE_BROKEN_SLAB);

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

    public void simpleColumnBlockWithItem(Block block, ResourceLocation side, ResourceLocation end) {
        simpleBlockWithItem(block, models().cubeColumn(name(block), side, end));
    }

    public void cub3All(Block block) {
        this.simpleBlock(block, cubeAll(block));
        this.simpleBlockItem(block, cubeAll(block));
    }

    private void particleOnlyBlock(Block block, ResourceLocation particleTexture) {
        ModelFile model = models().getBuilder(name(block) + "_particle").texture("particle", particleTexture);
        this.simpleBlock(block, model);
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

    private String name(Block block) { return this.key(block).getPath(); }
    private ResourceLocation key(Block block) { return BuiltInRegistries.BLOCK.getKey(block); }

    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("hbmsntm:block/" + deferredBlock.getId().getPath()));
    }
}
