package com.hbm.render.util;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.ArrayList;
import java.util.List;

public class QuadHelper {

    public static List<BakedQuad> retextureQuads(List<BakedQuad> toRetexture, TextureAtlasSprite sprite) {
        List<BakedQuad> quads = new ArrayList<>();

        for(BakedQuad quad : toRetexture) {
            quads.add(new BakedQuad(quad.getVertices(), quad.getTintIndex(), quad.getDirection(), sprite, quad.isShade(), quad.hasAmbientOcclusion()));
        }

        return quads;
    }

}
