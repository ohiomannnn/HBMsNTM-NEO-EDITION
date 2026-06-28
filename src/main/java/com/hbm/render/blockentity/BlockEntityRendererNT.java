package com.hbm.render.blockentity;

import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BlockEntityRendererNT<T extends BlockEntity> implements BlockEntityRendererProvider<T>, BlockEntityRenderer<T> {

    @Override
    public void render(T be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        RenderContext.setup(poseStack, this.getPacketLight(packedLight, be), packedOverlay);
        this.render(be, buffer, partialTicks);
        RenderContext.end();
    }

    public void render(T be, MultiBufferSource buffer, float partialTicks) { }
    public int getPacketLight(int packedLight, T be) { return packedLight; }

    @Override public int getViewDistance() { return 256; }

    public void bindTexture(ResourceLocation texture) { RenderSystem.setShaderTexture(0, texture); }
}
