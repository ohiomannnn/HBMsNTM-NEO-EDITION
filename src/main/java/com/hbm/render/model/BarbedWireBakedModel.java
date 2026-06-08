package com.hbm.render.model;

import com.hbm.blocks.generic.BarbedWireBlock;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.util.BobMathUtil;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class BarbedWireBakedModel extends LevelAwareWavefrontBakedModel {

    private final TextureAtlasSprite baseSprite;
    private final List<BakedQuad>[] cache = new List[4];
    private List<BakedQuad> itemQuads;

    public BarbedWireBakedModel(HFRWavefrontObject model, TextureAtlasSprite baseSprite) {
        super(model, ItemTransforms.NO_TRANSFORMS);
        this.baseSprite = baseSprite;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random, ModelData data, @Nullable RenderType type) {
        if(direction != null) return Collections.emptyList();

        if(!data.has(IN_LEVEL)) {
            if(itemQuads == null) {
                itemQuads = bakeSimpleQuads(null, null, baseSprite);
            }
            return itemQuads;
        }

        Direction dir = Direction.NORTH;
        if(state != null) {
            try {
                dir = state.getValue(BarbedWireBlock.HORIZONTAL_FACING);
            } catch(Exception ignored) {}
        }

        int mask = 0 + (dir.get3DDataValue() - 2);
        List<BakedQuad> quads = cache[mask];
        if(quads != null) return quads;

        quads = buildWorldQuads(dir);
        return cache[mask] = quads;
    }

    private List<BakedQuad> buildWorldQuads(Direction dir) {

        Matrix4f matrix = new Matrix4f();
        matrix.translate(0.5F, 0.0F, 0.5F);

        switch(dir) {
            case NORTH -> matrix.rotateY(0F);
            case SOUTH -> matrix.rotateY(BobMathUtil.toRadians(180));
            case WEST  -> matrix.rotateY(BobMathUtil.toRadians(90));
            case EAST  -> matrix.rotateY(BobMathUtil.toRadians(270));
        }

        return bakeSimpleQuads(null, matrix, baseSprite);
    }

    private static final ChunkRenderTypeSet RENDER_TYPE = ChunkRenderTypeSet.of(RenderType.cutout());
    @Override public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) { return RENDER_TYPE; }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return baseSprite;
    }
}
