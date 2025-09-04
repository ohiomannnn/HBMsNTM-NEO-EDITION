package com.hbm.datagen;

import com.hbm.HBMsNTM;
import com.hbm.block.ModBlocks;
import net.minecraft.data.PackOutput;
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
        ModelFile columnModel = models().cubeColumn(
                "brick_concrete_marked",
                modLoc("block/brick_concrete_marked"),
                modLoc("block/brick_concrete")
        );
        simpleBlockWithItem(ModBlocks.BRICK_CONCRETE_MARKED.get(), columnModel);

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

        simpleBlockWithItem(ModBlocks.TEST_BOMB.get(), cubeAll(ModBlocks.TEST_BOMB.get()));

    }
    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("hbmsntm:block/" + deferredBlock.getId().getPath()));
    }
}
