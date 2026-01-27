package com.hbm.render.item;

import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class RenderGeigerItem extends ItemRenderBase {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.scale(10F, 10F, 10F);
    }

    @Override
    public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0.2F, 0F, 0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90F));
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.GEIGER_TEX));
        ResourceManager.geiger.renderAll(poseStack, consumer, packedLight, packedOverlay);
    }
}
