package com.hbm.render.model.loader;

import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.model.*;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

public class NtmGeometry implements IUnbakedGeometry<NtmGeometry> {

    public enum BakedModelType {
        BARBED_WIRE,
        SPIKES,
        BARREL,
        CABLE,
        DET_CORD,
        PIPE
    }

    private final BakedModelType type;

    public NtmGeometry(BakedModelType type) {
        this.type = type;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state, ItemOverrides overrides) {

        TextureAtlasSprite textureSprite = null;
        TextureAtlasSprite overlaySprite = null;

        if(context.hasMaterial("texture")) textureSprite = spriteGetter.apply(context.getMaterial("texture"));
        if(context.hasMaterial("overlay")) overlaySprite = spriteGetter.apply(context.getMaterial("overlay"));

        return switch(this.type) {
            case BARBED_WIRE -> new BarbedWireBakedModel(new HFRWavefrontObject("models/obj/block/barbed_wire.obj"), textureSprite);
            case SPIKES -> new BarbedWireBakedModel(new HFRWavefrontObject("models/obj/block/spikes.obj"), textureSprite);
            case BARREL -> new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/block/barrel.obj"), textureSprite);
            case CABLE -> new CableBakedModel(new HFRWavefrontObject("models/obj/block/cable_neo.obj"), textureSprite);
            case DET_CORD -> new DetCordBakedModel(new HFRWavefrontObject("models/obj/block/cable_neo.obj"), textureSprite);
            case PIPE -> new PipeNeoBakedModel(new HFRWavefrontObject("models/obj/block/pipe_neo.obj"), textureSprite, overlaySprite);
        };
    }
}
