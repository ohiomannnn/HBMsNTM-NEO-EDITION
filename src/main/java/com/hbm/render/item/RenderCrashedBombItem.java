package com.hbm.render.item;

import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;

public class RenderCrashedBombItem extends ItemRenderBase {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0, 3, 0);
        poseStack.scale(2.125F, 2.125F, 2.125F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(90F));
    }

    @Override
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.mulPose(Axis.YP.rotationDegrees(90F));
        if (stack.is(ModBlocks.CRASHED_BOMB_BALEFIRE.asItem())) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.DUD_BALEFIRE_TEX));
            ResourceManager.dud_balefire.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
        if (stack.is(ModBlocks.CRASHED_BOMB_CONVENTIONAL.asItem())) {
            poseStack.translate(0F, 0F, -0.5F);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DUD_CONVENTIONAL_TEX));
            ResourceManager.dud_conventional.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
        if (stack.is(ModBlocks.CRASHED_BOMB_NUKE.asItem())) {
            poseStack.translate(0F, 0F, 1.25F);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.DUD_NUKE_TEX));
            ResourceManager.dud_nuke.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
        if (stack.is(ModBlocks.CRASHED_BOMB_SALTED.asItem())) {
            poseStack.translate(0F, 0F, 0.5F);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.DUD_SALTED_TEX));
            ResourceManager.dud_salted.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
    }
}
