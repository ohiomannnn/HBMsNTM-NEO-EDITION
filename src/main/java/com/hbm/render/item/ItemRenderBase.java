package com.hbm.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

// copied and modified from CE edition for sake of my insanity
public class ItemRenderBase extends BlockEntityWithoutLevelRenderer {

    public ItemRenderBase() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack itemStackIn, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        if (displayContext != ItemDisplayContext.GUI) poseStack.translate(0.5F, 0F, 0.5F);
        switch (displayContext) {
            case FIRST_PERSON_RIGHT_HAND -> {
                poseStack.translate(0.2F, 0.43F, 0F);
                poseStack.scale(0.2F, 0.2F, 0.2F);
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
                poseStack.mulPose(Axis.XP.rotationDegrees(25F));
                renderNonInv(itemStackIn, poseStack, buffer, packedLight, packedOverlay, true);
                renderFirstPersonRightHand(poseStack, buffer, packedLight, packedOverlay);
            }
            case FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(-0.2F, 0.43F, 0F);
                poseStack.scale(0.2F, 0.2F, 0.2F);
                poseStack.mulPose(Axis.XN.rotationDegrees(25F));
                renderNonInv(itemStackIn, poseStack, buffer, packedLight, packedOverlay, false);
            }
            case THIRD_PERSON_RIGHT_HAND, HEAD -> {
                poseStack.translate(0F, 0.5F, 0F);
                poseStack.scale(0.1875F, 0.1875F, 0.1875F);
                poseStack.mulPose(Axis.YP.rotationDegrees(345F));
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
                renderNonInv(itemStackIn, poseStack, buffer, packedLight, packedOverlay, true);
            }
            case THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0F, 0.5F, 0F);
                poseStack.scale(0.1875F, 0.1875F, 0.1875F);
                poseStack.mulPose(Axis.YP.rotationDegrees(15F));
                renderNonInv(itemStackIn, poseStack, buffer, packedLight, packedOverlay, false);
            }
            case GROUND -> {
                poseStack.translate(0F, 0.3F, 0F);
                poseStack.scale(0.125F, 0.125F, 0.125F);
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
                renderGround(poseStack, buffer, packedLight, packedOverlay);
            }
            case FIXED -> {
                poseStack.translate(0F, 0.3F, 0F);
                poseStack.scale(0.25F, 0.25F, 0.25F);
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
                renderNonInv(itemStackIn, poseStack, buffer, packedLight, packedOverlay, false);
            }
            case GUI -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(30F));
                poseStack.mulPose(Axis.YP.rotationDegrees(225F));  // 45 + 180
                poseStack.scale(0.0620F, 0.0620F, 0.0620F);
                poseStack.translate(0F, 11.6F, -11.6F);
                renderInventory(itemStackIn, poseStack, buffer, packedLight, packedOverlay);
            }
            case NONE -> {}
        }

        renderCommon(itemStackIn, poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    public void renderNonInv(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean righthand) { renderNonInv(poseStack, buffer, packedLight, packedOverlay); }
    public void renderInventory(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) { renderInventory(poseStack, buffer, packedLight, packedOverlay); }
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) { renderCommon(poseStack, buffer, packedLight, packedOverlay); }
    public void renderNonInv(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) { }
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) { }
    public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) { }
    public void renderGround(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) { }
    public void renderFirstPersonRightHand(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) { }
}
