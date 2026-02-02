package com.hbm.render.item;

import com.hbm.blocks.ModBlocks;
import com.hbm.items.ModItems;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;

public class RenderLandmineItem extends ItemRenderBase {

    @Override
    public void renderInventory(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (stack.is(ModBlocks.MINE_AP.asItem()) || stack.is(ModBlocks.MINE_SHRAP.asItem())) {
            poseStack.scale(8F, 8F, 8F);
        }
        if (stack.is(ModBlocks.MINE_HE.asItem())) {
            poseStack.scale(6F, 6F, 6F);
        }
        if (stack.is(ModBlocks.MINE_NAVAL.asItem())) {
            poseStack.translate(0F, 2F, -1F);
            poseStack.scale(5F, 5F, 5F);
        }
        if (stack.is(ModBlocks.MINE_FAT.asItem())) {
            poseStack.translate(0F, -1F, 0F);
            poseStack.scale(7F, 7F, 7F);
        }
    }

    @Override
    public void renderNonInv(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean righthand) {
        if (stack.is(ModBlocks.MINE_HE.asItem())) {
            if (righthand) {
                poseStack.translate(0.25F, 0.6F, 0F);
                poseStack.mulPose(Axis.YP.rotationDegrees(45F));
                poseStack.mulPose(Axis.ZN.rotationDegrees(15F));
            } else {
                poseStack.translate(0.25F, 0.6F, 0F);
                poseStack.mulPose(Axis.YN.rotationDegrees(45F));
                poseStack.mulPose(Axis.ZN.rotationDegrees(15F));
            }
        }
    }

    @Override
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (stack.is(ModBlocks.MINE_AP.asItem())) {
            poseStack.scale(1.25F, 1.25F, 1.25F);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_AP_GRASS_TEX));
            ResourceManager.mine_ap.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
        if (stack.is(ModBlocks.MINE_HE.asItem())) {
            poseStack.scale(4F, 4F, 4F);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(ResourceManager.MINE_HE_TEX));
            ResourceManager.mine_he.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
        if (stack.is(ModBlocks.MINE_SHRAP.asItem())) {
            poseStack.scale(1.25F, 1.25F, 1.25F);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_SHRAPNEL_TEX));
            ResourceManager.mine_ap.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
        if (stack.is(ModBlocks.MINE_NAVAL.asItem())) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(ResourceManager.MINE_NAVAL_TEX));
            ResourceManager.mine_naval.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
        if (stack.is(ModBlocks.MINE_FAT.asItem())) {
            poseStack.translate(0.25F, 0F, 0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(90F));
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_FAT_TEX));
            ResourceManager.mine_fat.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
    }
}
