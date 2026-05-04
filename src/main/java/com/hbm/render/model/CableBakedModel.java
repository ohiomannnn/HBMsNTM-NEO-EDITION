package com.hbm.render.model;

import com.hbm.blocks.network.CableBlock;
import com.hbm.main.NuclearTechMod;
import com.hbm.render.loader.HFRWavefrontObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CableBakedModel extends AbstractWavefrontBakedModel {

    private final TextureAtlasSprite sprite;
    private final boolean forBlock;
    private final List<BakedQuad>[] cache = new List[64];
    private List<BakedQuad> itemQuads;

    public CableBakedModel(HFRWavefrontObject model, TextureAtlasSprite baseSprite, boolean forBlock) {
        super(model, ItemTransforms.NO_TRANSFORMS);
        this.sprite = baseSprite;
        this.forBlock = forBlock;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        if(direction != null) return Collections.emptyList();

        if(!forBlock) {
            if (itemQuads == null) {
                itemQuads = buildItemQuads();
            }
            return itemQuads;
        }

        boolean pX = false, nX = false, pY = false, nY = false, pZ = false, nZ = false;
        if(state != null) {
            try {
                pX = state.getValue(CableBlock.EAST);
                nX = state.getValue(CableBlock.WEST);
                pY = state.getValue(CableBlock.UP);
                nY = state.getValue(CableBlock.DOWN);
                pZ = state.getValue(CableBlock.SOUTH);
                nZ = state.getValue(CableBlock.NORTH);
            } catch(Exception ignored) {}
        }

        int mask = 0 + (pX ? 32 : 0) + (nX ? 16 : 0) + (pY ? 8 : 0) + (nY ? 4 : 0) + (pZ ? 2 : 0) + (nZ ? 1 : 0);
        List<BakedQuad> quads = cache[mask];
        if (quads != null) return quads;

        quads = buildWorldQuads(pX, nX, pY, nY, pZ, nZ);
        return cache[mask] = quads;
    }

    private List<BakedQuad> buildWorldQuads(boolean pX, boolean nX, boolean pY, boolean nY, boolean pZ, boolean nZ) {
        List<String> parts = new ArrayList<>();

        if(pX && nX && !pY && !nY && !pZ && !nZ)
            parts.add("CX");
        else if(!pX && !nX && pY && nY && !pZ && !nZ)
            parts.add("CY");
        else if(!pX && !nX && !pY && !nY && pZ && nZ)
            parts.add("CZ");
        else {
            parts.add("Core");
            if(pX) parts.add("posX");
            if(nX) parts.add("negX");
            if(pY) parts.add("posY");
            if(nY) parts.add("negY");
            if(nZ) parts.add("posZ");
            if(pZ) parts.add("negZ");
        }

        return bakeSimpleQuads(parts, 0.0F, 0.0F, 0.0F, BlockTranslate.CENTER, sprite);
    }

    private List<BakedQuad> buildItemQuads() {
        List<String> parts = List.of("Core", "posX", "negX", "posZ", "negZ");
        return bakeSimpleQuads(parts, 0F, 0F, 0F, BlockTranslate.NONE, sprite);
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return sprite;
    }
}
