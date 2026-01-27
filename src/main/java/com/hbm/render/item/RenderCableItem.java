package com.hbm.render.item;

import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderCableItem extends BlockEntityWithoutLevelRenderer {

    public RenderCableItem() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180F));
        poseStack.scale(1.25F, 1.25F, 1.25F);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.CABLE_NEO_TEX));
        ResourceManager.cable_neo.renderPart("Core", poseStack, consumer, packedLight, packedOverlay);
        ResourceManager.cable_neo.renderPart("posX", poseStack, consumer, packedLight, packedOverlay);
        ResourceManager.cable_neo.renderPart("negX", poseStack, consumer, packedLight, packedOverlay);
        ResourceManager.cable_neo.renderPart("posZ", poseStack, consumer, packedLight, packedOverlay);
        ResourceManager.cable_neo.renderPart("negZ", poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}
