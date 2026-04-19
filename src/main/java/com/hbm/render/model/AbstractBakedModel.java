package com.hbm.render.model;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;

public abstract class AbstractBakedModel implements BakedModel {

    private final boolean ambientOcclusion;
    private final boolean gui3d;
    private final boolean builtInRenderer;
    private final boolean usesBlockLight;
    private final ItemTransforms transforms;
    private final ItemOverrides overrides;

    protected AbstractBakedModel(ItemTransforms transforms) {
        this(true, true, false, true, transforms, ItemOverrides.EMPTY);
    }

    protected AbstractBakedModel(boolean ambientOcclusion, boolean gui3d, boolean builtInRenderer, boolean usesBlockLight, ItemTransforms transforms) {
        this(ambientOcclusion, gui3d, builtInRenderer, usesBlockLight, transforms, ItemOverrides.EMPTY);
    }

    protected AbstractBakedModel(boolean ambientOcclusion, boolean gui3d, boolean builtInRenderer, boolean usesBlockLight, ItemTransforms transforms, ItemOverrides overrides) {
        this.ambientOcclusion = ambientOcclusion;
        this.gui3d = gui3d;
        this.builtInRenderer = builtInRenderer;
        this.usesBlockLight = usesBlockLight;
        this.transforms = transforms != null ? transforms : ItemTransforms.NO_TRANSFORMS;
        this.overrides = overrides != null ? overrides : ItemOverrides.EMPTY;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return ambientOcclusion;
    }

    @Override
    public boolean isGui3d() {
        return gui3d;
    }

    @Override
    public boolean usesBlockLight() {
        return usesBlockLight;
    }

    @Override
    public boolean isCustomRenderer() {
        return builtInRenderer;
    }

    @Override
    public ItemTransforms getTransforms() {
        return transforms;
    }

    @Override
    public ItemOverrides getOverrides() {
        return overrides;
    }
}
