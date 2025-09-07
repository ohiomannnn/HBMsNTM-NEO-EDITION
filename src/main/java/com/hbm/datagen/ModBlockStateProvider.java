package com.hbm.datagen;

import com.hbm.HBMsNTM;
import com.hbm.blocks.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HBMsNTM.MODID, exFileHelper);
    }
    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ModBlocks.BRICK_CONCRETE.get(), cubeAll(ModBlocks.BRICK_CONCRETE.get()));
        simpleBlockWithItem(ModBlocks.BRICK_CONCRETE_MOSSY.get(), cubeAll(ModBlocks.BRICK_CONCRETE_MOSSY.get()));
        simpleBlockWithItem(ModBlocks.BRICK_CONCRETE_CRACKED.get(), cubeAll(ModBlocks.BRICK_CONCRETE_CRACKED.get()));
        simpleBlockWithItem(ModBlocks.BRICK_CONCRETE_BROKEN.get(), cubeAll(ModBlocks.BRICK_CONCRETE_BROKEN.get()));
        simpleBlockWithItem(ModBlocks.BRICK_CONCRETE_MARKED.get(),
                models().cubeColumn("brick_concrete_marked",
                        modLoc("block/brick_concrete_marked"),
                        modLoc("block/brick_concrete")
        ));

        logBlock((RotatedPillarBlock) ModBlocks.WASTE_LOG.get());

        simpleBlockWithItem(ModBlocks.WASTE_EARTH.get(),
                models().withExistingParent("waste_earth", mcLoc("block/block"))
                        .texture("particle", modLoc("block/waste_earth_bottom"))
                        .texture("bottom", modLoc("block/waste_earth_bottom"))
                        .texture("top", modLoc("block/waste_earth_top"))
                        .texture("side", modLoc("block/waste_earth_side"))
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
        simpleBlockWithItem(
                ModBlocks.WASTE_LEAVES.get(),
                models().cubeColumn("waste_leaves", modLoc("block/waste_leaves"), modLoc("block/waste_leaves"))
                        .renderType("cutout_mipped")
        );

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

    }
    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("hbmsntm:block/" + deferredBlock.getId().getPath()));
    }
}
