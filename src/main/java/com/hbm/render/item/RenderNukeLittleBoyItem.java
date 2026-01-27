package com.hbm.render.item;

import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class RenderNukeLittleBoyItem extends ItemRenderBase {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.scale(5F, 5F, 5F);
    }

    @Override
    public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(-1F, 0F, 0F);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.NUKE_LITTLE_BOY_TEX));
        ResourceManager.nuke_little_boy.renderAll(poseStack, consumer, packedLight, packedOverlay);
    }
}
