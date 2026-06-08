package com.hbm.render.model;

import com.hbm.render.loader.HFRWavefrontObject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class SimpleWavefrontBakedModel extends LevelAwareWavefrontBakedModel {

    private final TextureAtlasSprite baseSprite;
    private List<BakedQuad> quads;

    public SimpleWavefrontBakedModel(HFRWavefrontObject model, TextureAtlasSprite baseSprite) {
        super(model, ItemTransforms.NO_TRANSFORMS);
        this.baseSprite = baseSprite;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random, ModelData data, @Nullable RenderType type) {
        if(direction != null) return Collections.emptyList();

        if(quads == null) {

            Matrix4f matrix = new Matrix4f();
            if(data.has(IN_LEVEL)) matrix.translate(0.5F, 0.0F, 0.5F);

            quads = bakeSimpleQuads(null, matrix, baseSprite);
        }

        return quads;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return baseSprite;
    }
}
