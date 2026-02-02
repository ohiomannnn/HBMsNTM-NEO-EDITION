package com.hbm.render.item;

import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;

public class RenderDetCordItem extends ItemRenderBaseStandard {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.scale(10F, 10F, 10F);
        poseStack.translate(0F, 0.05F, 0F);
    }

    @Override
    public void renderNonInv(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean righthand) {
        poseStack.translate(0F, 0.4F, 0F);
    }

    @Override
    public void renderFirstPerson(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean righthand) {
        poseStack.mulPose(Axis.YP.rotationDegrees(90F));
    }

    @Override
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.mulPose(Axis.YP.rotationDegrees(90F));
        poseStack.scale(1.25F, 1.25F, 1.25F);
        poseStack.translate(0F, 0.1F, 0F);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.DET_CORD_TEX));
        ResourceManager.cable_neo.renderPart("CZ", poseStack, consumer, packedLight, packedOverlay);
    }
}
