package com.hbm.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public class RenderDiFurnaceExtensionItem extends ItemRenderBaseStandard {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.scale(10F, 10F, 10F);
        poseStack.translate(0F, -0.2F, 0F);
    }

    @Override
    public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        //VertexConsumer topConsumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.DIFURNACE_EXTENSION_TOP_TEX));
        //ResourceManager.difurnace_extension.renderPart("Top", poseStack, topConsumer, packedLight, packedOverlay);
        //VertexConsumer sideConsumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.DIFURNACE_EXTENSION_TEX));
        //ResourceManager.difurnace_extension.renderPart("Side", poseStack, sideConsumer, packedLight, packedOverlay);
        //VertexConsumer bottomConsumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.DIFURNACE_EXTENSION_BOTTOM_TEX));
        //ResourceManager.difurnace_extension.renderPart("Bottom", poseStack, bottomConsumer, packedLight, packedOverlay);
    }
}
