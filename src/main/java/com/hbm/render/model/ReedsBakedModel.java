package com.hbm.render.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReedsBakedModel extends AbstractBakedModel {

    public static final ModelProperty<Integer> DEPTH = new ModelProperty<>();

    protected final TextureAtlasSprite[] sprites;

    private final BakedModel crossModel;

    private List<BakedQuad>[] quads = new List[0];

    private int lastDepth;

    protected ReedsBakedModel(TextureAtlasSprite[] sprites, BakedModel crossModel) {
        super(ItemTransforms.NO_TRANSFORMS);

        this.sprites = sprites;
        this.crossModel = crossModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random, ModelData data, @Nullable RenderType renderType) {
        if(direction != null) return Collections.emptyList();
        if(state == null) return Collections.emptyList();

        int depth = 0;
        if(data.has(DEPTH)) depth = data.get(DEPTH);

        if(lastDepth != depth) {
            int newSize = quads.length + depth;
            quads = Arrays.copyOf(quads, newSize);

            for(int i = 0; i < depth; i++) {

            }

            lastDepth = depth;
        }

        return quads[depth];
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {

        int depth = 0;

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for(int i = y - 1; i > 0 ; i--) {
            FluidState fluidState = level.getFluidState(new BlockPos(x, i, z));
            depth = y - i;
            if(!fluidState.is(FluidTags.WATER)) break;
        }

        return ModelData.builder().with(DEPTH, depth).build();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return sprites[0];
    }
}
