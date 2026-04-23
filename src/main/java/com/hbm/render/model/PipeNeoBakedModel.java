package com.hbm.render.model;

import com.hbm.blocks.network.FluidDuctConnectingBlock;
import com.hbm.render.loader.HFRWavefrontObject;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PipeNeoBakedModel extends AbstractWavefrontBakedModel {

    private final TextureAtlasSprite baseSprite;
    private final TextureAtlasSprite overlaySprite;
    private final boolean forBlock;
    private final List<BakedQuad>[] cache = new List[64];
    private List<BakedQuad> itemQuads;

    public PipeNeoBakedModel(HFRWavefrontObject model, TextureAtlasSprite baseSprite, TextureAtlasSprite overlaySprite, boolean forBlock) {
        super(model, BakedModelTransforms.PIPE_ITEM);
        this.baseSprite = baseSprite;
        this.overlaySprite = overlaySprite;
        this.forBlock = forBlock;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        if(direction != null) return Collections.emptyList();

        if(!forBlock) {
            if(itemQuads == null) {
                itemQuads = buildItemQuads();
            }
            return itemQuads;
        }

        boolean pX = false, nX = false, pY = false, nY = false, pZ = false, nZ = false;
        if(state != null) {
            try {
                pX = state.getValue(FluidDuctConnectingBlock.EAST);
                nX = state.getValue(FluidDuctConnectingBlock.WEST);
                pY = state.getValue(FluidDuctConnectingBlock.UP);
                nY = state.getValue(FluidDuctConnectingBlock.DOWN);
                pZ = state.getValue(FluidDuctConnectingBlock.SOUTH);
                nZ = state.getValue(FluidDuctConnectingBlock.NORTH);
            } catch (Exception ignored) {}
        }
        int mask = (pX ? 32 : 0) | (nX ? 16 : 0) | (pY ? 8 : 0) | (nY ? 4 : 0) | (pZ ? 2 : 0) | (nZ ? 1 : 0);
        List<BakedQuad> quads = cache[mask];
        if(quads != null) return quads;
        quads = buildWorldQuads(pX, nX, pY, nY, pZ, nZ, mask);
        return cache[mask] = quads;
    }

    private List<BakedQuad> buildWorldQuads(boolean pX, boolean nX, boolean pY, boolean nY, boolean pZ, boolean nZ, int mask) {
        List<String> parts = new ArrayList<>();

        switch (mask) {
            case 0 -> {
                parts.add("pX");
                parts.add("nX");
                parts.add("pY");
                parts.add("nY");
                parts.add("pZ");
                parts.add("nZ");
            }
            case 32, 16 -> {
                parts.add("pX");
                parts.add("nX");
            }
            case 8, 4 -> {
                parts.add("pY");
                parts.add("nY");
            }
            case 2, 1 -> {
                parts.add("pZ");
                parts.add("nZ");
            }
            default -> {
                if (pX) parts.add("pX");
                if (nX) parts.add("nX");
                if (pY) parts.add("pY");
                if (nY) parts.add("nY");
                if (pZ) parts.add("nZ"); // mirrors original (pZ -> nZ)
                if (nZ) parts.add("pZ"); // mirrors original (nZ -> pZ)

                if (!pX && !pY && !pZ) parts.add("ppn");
                if (!pX && !pY && !nZ) parts.add("ppp");
                if (!nX && !pY && !pZ) parts.add("npn");
                if (!nX && !pY && !nZ) parts.add("npp");
                if (!pX && !nY && !pZ) parts.add("pnn");
                if (!pX && !nY && !nZ) parts.add("pnp");
                if (!nX && !nY && !pZ) parts.add("nnn");
                if (!nX && !nY && !nZ) parts.add("nnp");
            }
        }

        return bakeWithOverlay(parts, true);
    }


    private List<BakedQuad> buildItemQuads() {
        List<String> parts = List.of("pX", "nX", "pZ", "nZ");
        return bakeWithOverlay(parts, false);
    }

    private List<BakedQuad> bakeWithOverlay(List<String> parts, boolean centerToBlock) {
        List<FaceGeometry> geometry = buildGeometry(parts, 0.0F, 0.0F, 0.0F, true, centerToBlock ? BlockTranslate.CENTER : BlockTranslate.NONE);
        List<BakedQuad> quads = new ArrayList<>(geometry.size() * 2);
        for(FaceGeometry geo : geometry) {
            quads.add(geo.buildQuad(baseSprite, -1));
            quads.add(geo.buildQuad(overlaySprite, 1));
        }
        return quads;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return baseSprite;
    }
}
