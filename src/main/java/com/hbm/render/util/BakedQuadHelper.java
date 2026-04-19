package com.hbm.render.util;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class BakedQuadHelper {

    protected int[] vertexData;
    protected Direction direction;
    protected boolean shade;
    protected boolean hasAmbientOcclusion;

    public BakedQuadHelper(int[] vertexData, Direction direction, boolean shade, boolean hasAmbientOcclusion) {
        this.vertexData = vertexData;
        this.direction = direction;
        this.shade = shade;
        this.hasAmbientOcclusion = hasAmbientOcclusion;
    }

    public BakedQuad buildQuads(TextureAtlasSprite sprite, int tintIndex) {
        return new BakedQuad(vertexData, tintIndex, direction, sprite, shade, hasAmbientOcclusion);
    }
}
