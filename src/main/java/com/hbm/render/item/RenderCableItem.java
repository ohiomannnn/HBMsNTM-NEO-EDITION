package com.hbm.render.item;

import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class RenderCableItem extends ItemRenderBaseStandard {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.scale(10F, 10F, 10F);
    }

    @Override
    public void renderNonInv(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean righthand) {
        poseStack.translate(0F, 0.4F, 0F);
    }

    @Override
    public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.mulPose(Axis.YP.rotationDegrees(180F));
        poseStack.scale(1.25F, 1.25F, 1.25F);
        poseStack.translate(0F, 0.1F, 0F);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.CABLE_NEO_TEX));
        ResourceManager.cable_neo.renderPart("Core", poseStack, consumer, packedLight, packedOverlay);
        ResourceManager.cable_neo.renderPart("posX", poseStack, consumer, packedLight, packedOverlay);
        ResourceManager.cable_neo.renderPart("negX", poseStack, consumer, packedLight, packedOverlay);
        ResourceManager.cable_neo.renderPart("posZ", poseStack, consumer, packedLight, packedOverlay);
        ResourceManager.cable_neo.renderPart("negZ", poseStack, consumer, packedLight, packedOverlay);
    }
}
