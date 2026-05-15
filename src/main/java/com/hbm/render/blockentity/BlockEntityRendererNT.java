package com.hbm.render.blockentity;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BlockEntityRendererNT<T extends BlockEntity> implements BlockEntityRendererProvider<T>, BlockEntityRenderer<T> {

    @Override
    public int getViewDistance() {
        return 256;
    }

    public void bindTexture(ResourceLocation texture) { RenderSystem.setShaderTexture(0, texture); }
}
