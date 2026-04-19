package com.hbm.render.model;

import com.hbm.render.loader.HFRWavefrontObject;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;

public abstract class AbstractWavefrontBakedModel extends AbstractBakedModel {

    protected final HFRWavefrontObject model;

    protected AbstractWavefrontBakedModel(HFRWavefrontObject model) {
        super(false, true, false, true, BakedModelTransforms.pipeItem(), ItemOverrides.EMPTY);
        this.model = model;
    }
}
