package com.hbm.render.model;

import com.hbm.render.loader.HFRWavefrontObject;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.QuadTransformers;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

// little name
public abstract class LevelAwareWavefrontBakedModel extends AbstractWavefrontBakedModel {

    public static final ModelProperty<Boolean> IN_LEVEL = new ModelProperty<>();

    public LevelAwareWavefrontBakedModel(HFRWavefrontObject model, ItemTransforms transforms) {
        super(model, transforms);
    }

    @Override public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) { return Collections.emptyList(); }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData data) {
        return ModelData.builder().with(IN_LEVEL, true).build();
    }
}
