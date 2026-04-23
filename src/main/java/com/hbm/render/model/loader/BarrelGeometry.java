package com.hbm.render.model.loader;

import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.model.BarrelBakedModel;
import com.hbm.render.model.PipeNeoBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

public class BarrelGeometry implements IUnbakedGeometry<BarrelGeometry> {

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state, ItemOverrides overrides) {

        TextureAtlasSprite textureSprite = null;

        if(context.hasMaterial("texture")) textureSprite = spriteGetter.apply(context.getMaterial("texture"));

        HFRWavefrontObject obj = new HFRWavefrontObject("models/obj/block/barrel.obj");

        return new BarrelBakedModel(obj, textureSprite);
    }
}
