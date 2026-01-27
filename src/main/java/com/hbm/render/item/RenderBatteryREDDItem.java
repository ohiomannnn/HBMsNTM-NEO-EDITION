package com.hbm.render.item;

import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class RenderBatteryREDDItem extends ItemRenderBase {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0, -3, 0);
        poseStack.scale(2.5F, 2.5F, 2.5F);
    }

    @Override
    public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.mulPose(Axis.YN.rotationDegrees(-90F));
        poseStack.scale(0.5F, 0.5F, 0.5F);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.BATTERY_REDD_TEX));
        ResourceManager.battery_redd.renderPart("Base", poseStack, consumer, packedLight, packedOverlay);
        ResourceManager.battery_redd.renderPart("Wheel", poseStack, consumer, packedLight, packedOverlay);
        ResourceManager.battery_redd.renderPart("Lights", poseStack, consumer, 240, packedOverlay);
    }
}
