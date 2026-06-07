package com.hbm.render.model;

import com.hbm.render.loader.HFRWavefrontObject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class BarbedWireBakedModel extends AbstractWavefrontBakedModel {

    private static final ModelProperty<Boolean> IN_LEVEL = new ModelProperty<>();

    private final TextureAtlasSprite baseSprite;
    private List<BakedQuad> quads;

    public BarbedWireBakedModel(HFRWavefrontObject model, TextureAtlasSprite baseSprite) {
        super(model, ItemTransforms.NO_TRANSFORMS);
        this.baseSprite = baseSprite;
    }

    @Override public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) { return Collections.emptyList(); }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random, ModelData data, @Nullable RenderType type) {
        if(direction != null) return Collections.emptyList();

        if(quads == null) {
            quads = bakeSimpleQuads(null, 0F, 0F, 0F, data.has(IN_LEVEL) ? BlockTranslate.CENTER_NO_Y_OFFSET : BlockTranslate.NONE, baseSprite);
        }

        return quads;
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData data) {
        return ModelData.builder().with(IN_LEVEL, true).build();
    }

    private static final ChunkRenderTypeSet RENDER_TYPE = ChunkRenderTypeSet.of(RenderType.cutout());
    @Override public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) { return RENDER_TYPE; }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return baseSprite;
    }
}
