package com.hbm.render.item;

import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class RenderNukeGadgetItem extends ItemRenderBase {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0F, -3F, 0F);
        poseStack.scale(5F, 5F, 5F);
    }

    @Override
    public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.mulPose(Axis.YN.rotationDegrees(90F));
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.NUKE_GADGET_TEX));
        ResourceManager.nuke_gadget.renderPart("Body", poseStack, consumer, packedLight, packedOverlay);

        GraphicsStatus graphics = Minecraft.getInstance().options.graphicsMode().get();

        if (graphics == GraphicsStatus.FANCY || graphics == GraphicsStatus.FABULOUS) {
            ResourceManager.nuke_gadget.renderPart("Wires", poseStack, consumer, packedLight, packedOverlay);
        }
    }
}
