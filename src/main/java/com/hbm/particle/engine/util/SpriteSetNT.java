package com.hbm.particle.engine.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.function.Function;

public class SpriteSetNT implements SpriteSet {

    private final TextureAtlasSprite[] sprites;

    public SpriteSetNT(ResourceLocation[] spriteLocations) {
        Function<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_PARTICLES);

        sprites = new TextureAtlasSprite[spriteLocations.length];
        for (int i = 0; i < spriteLocations.length; i++) {
            sprites[i] = atlas.apply(spriteLocations[i]);
        }
    }

    @Override
    public TextureAtlasSprite get(int particleAge, int particleMaxAge) {
        return this.sprites[particleAge * (this.sprites.length - 1) / particleMaxAge];
    }

    @Override
    public TextureAtlasSprite get(RandomSource random) {
        return this.sprites[random.nextInt(this.sprites.length)];
    }
}
