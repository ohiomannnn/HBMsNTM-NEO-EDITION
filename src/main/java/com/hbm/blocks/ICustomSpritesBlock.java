package com.hbm.blocks;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface ICustomSpritesBlock extends IMetaBlock {

    TextureAtlasSprite getSprite(int index, int meta);
    TextureAtlasSprite getParticleSprite(int index, int meta);
}
