package com.hbm.render.blockentity;

import com.hbm.blockentity.EmptyBlockEntity;
import com.hbm.blockentity.bomb.CrashedBombBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;

public class RenderBarrel implements BlockEntityRenderer<EmptyBlockEntity> {

    public RenderBarrel(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(EmptyBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0.0, 0.5);

        Block block = be.getBlockState().getBlock();

        VertexConsumer consumer;
        if (block == ModBlocks.BARREL_PINK.get()) consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.BARREL_PINK_TEX));
        else consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.BARREL_RED_TEX));
        ResourceManager.barrel.renderAll(poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
