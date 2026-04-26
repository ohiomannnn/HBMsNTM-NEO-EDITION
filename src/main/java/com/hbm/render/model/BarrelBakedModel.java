package com.hbm.render.model;

import com.hbm.render.loader.HFRWavefrontObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class BarrelBakedModel extends AbstractWavefrontBakedModel {

    private final TextureAtlasSprite baseSprite;
    private List<BakedQuad> quads;

    public BarrelBakedModel(HFRWavefrontObject model, TextureAtlasSprite baseSprite) {
        super(model, ItemTransforms.NO_TRANSFORMS);
        this.baseSprite = baseSprite;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        if(direction != null) return Collections.emptyList();

        if(quads == null) {
            quads = bakeSimpleQuads(null, 0F, 0F, 0F, true, BlockTranslate.CENTER_NO_Y_OFFSET, baseSprite);
        }

        return quads;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return baseSprite;
    }
}
