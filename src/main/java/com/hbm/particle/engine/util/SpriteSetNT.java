package com.hbm.particle.engine.util;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

public class SpriteSetNT implements SpriteSet {

    private final List<TextureAtlasSprite> sprites = new ArrayList<>();

    public SpriteSetNT(TextureAtlas atlas, ResourceLocation[] spriteLocations) {
        for (int i = 0; i < spriteLocations.length; i++) {
            sprites.add(i, atlas.getSprite(spriteLocations[i]));
        }
    }

    public SpriteSetNT(TextureAtlas atlas, ResourceLocation spriteLocation) {
        sprites.add(atlas.getSprite(spriteLocation));
    }

    @Override
    public TextureAtlasSprite get(int particleAge, int particleMaxAge) {
        return this.sprites.get(particleAge * (this.sprites.size() - 1) / particleMaxAge);
    }

    @Override
    public TextureAtlasSprite get(RandomSource random) {
        return this.sprites.get(random.nextInt(this.sprites.size()));
    }
}
