package com.hbm.render.model;

import com.hbm.blocks.bomb.DetCordBlock;
import com.hbm.render.loader.HFRWavefrontObject;
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

public class DetCordBakedModel extends AbstractWavefrontBakedModel {

    private final TextureAtlasSprite sprite;
    private final boolean forBlock;
    private final List<BakedQuad>[] cache = new List[64];
    private List<BakedQuad> itemQuads;

    public DetCordBakedModel(HFRWavefrontObject model, TextureAtlasSprite baseSprite, boolean forBlock) {
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
                pX = state.getValue(DetCordBlock.EAST);
                nX = state.getValue(DetCordBlock.WEST);
                pY = state.getValue(DetCordBlock.UP);
                nY = state.getValue(DetCordBlock.DOWN);
                pZ = state.getValue(DetCordBlock.SOUTH);
                nZ = state.getValue(DetCordBlock.NORTH);
            } catch(Exception ignored) {}
        }

        int mask = 0 + (pX ? 32 : 0) + (nX ? 16 : 0) + (pY ? 8 : 0) + (nY ? 4 : 0) + (pZ ? 2 : 0) + (nZ ? 1 : 0);
        List<BakedQuad> quads = cache[mask];
        if(quads != null) return quads;

        quads = buildWorldQuads(pX, nX, pY, nY, pZ, nZ, mask);
        return cache[mask] = quads;
    }

    private List<BakedQuad> buildWorldQuads(boolean pX, boolean nX, boolean pY, boolean nY, boolean pZ, boolean nZ, int mask) {
        List<String> parts = new ArrayList<>();

        switch(mask) {
            case 0b110000, 0b100000, 0b010000 -> {
                parts.add("CX");
            }
            case 0b001100, 0b001000, 0b000100 -> {
                parts.add("CY");
            }
            case 0b000011, 0b000010, 0b000001 -> {
                parts.add("CZ");
            }
            default -> {
                parts.add("Core");
                if(pX) parts.add("posX");
                if(nX) parts.add("negX");
                if(pY) parts.add("posY");
                if(nY) parts.add("negY");
                if(nZ) parts.add("posZ");
                if(pZ) parts.add("negZ");
            }
        }

        return bakeSimpleQuads(parts, 0.0F, 0.0F, 0.0F, false, BlockTranslate.CENTER, sprite);
    }

    private List<BakedQuad> buildItemQuads() {
        List<String> parts = List.of("CZ");
        return bakeSimpleQuads(parts, 0F, 0F, 0F, true, BlockTranslate.NONE, sprite);
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return sprite;
    }
}
