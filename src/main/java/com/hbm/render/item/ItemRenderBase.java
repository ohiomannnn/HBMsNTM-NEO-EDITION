package com.hbm.render.item;

import com.hbm.render.util.RenderContext;
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
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        RenderContext.setup(poseStack, packedLight, packedOverlay);

        if (displayContext != ItemDisplayContext.GUI) RenderContext.translate(0.5F, 0F, 0.5F);
        switch (displayContext) {
            case FIRST_PERSON_RIGHT_HAND -> {
                RenderContext.translate(0.2F, 0.44F, 0F);
                RenderContext.scale(0.2F, 0.2F, 0.2F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
                RenderContext.mulPose(Axis.YP.rotationDegrees(3F));
                RenderContext.mulPose(Axis.XP.rotationDegrees(25F));
                renderNonInv(stack, buffer, true);
            }
            case FIRST_PERSON_LEFT_HAND -> {
                RenderContext.translate(-0.2F, 0.44F, 0F);
                RenderContext.scale(0.2F, 0.2F, 0.2F);
                RenderContext.mulPose(Axis.YN.rotationDegrees(3F));
                RenderContext.mulPose(Axis.XN.rotationDegrees(25F));
                renderNonInv(stack, buffer, false);
            }
            case THIRD_PERSON_RIGHT_HAND, HEAD -> {
                RenderContext.translate(-0.05F, 0.5F, 0F);
                RenderContext.scale(0.145F, 0.145F, 0.145F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
                renderNonInv(stack, buffer, true);
            }
            case THIRD_PERSON_LEFT_HAND -> {
                RenderContext.translate(0.05F, 0.5F, 0F);
                RenderContext.scale(0.145F, 0.145F, 0.145F);
                renderNonInv(stack, buffer, false);
            }
            case GROUND -> {
                RenderContext.translate(0F, 0.3F, 0F);
                RenderContext.scale(0.125F, 0.125F, 0.125F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                //renderGround(stack, buffer);
            }
            case FIXED -> {
                RenderContext.translate(0F, 0.3F, 0F);
                RenderContext.scale(0.25F, 0.25F, 0.25F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                renderNonInv(stack, buffer, false);
            }
            case GUI -> {
                RenderContext.mulPose(Axis.XP.rotationDegrees(30F));
                RenderContext.mulPose(Axis.YP.rotationDegrees(225F));  // 45 + 180
                RenderContext.scale(0.0620F, 0.0620F, 0.0620F);
                RenderContext.translate(0F, 11.6F, -11.6F);
                renderInventory(stack, buffer);
            }
            default -> { }
        }

        renderCommon(stack, buffer);

        RenderContext.end();
    }

    public void renderNonInv(ItemStack stack, MultiBufferSource buffer, boolean rightHand) { }
    public void renderInventory(ItemStack stack, MultiBufferSource buffer) { }
    public void renderCommon(ItemStack stack, MultiBufferSource buffer) { }

    // nothing overriding it anyway
    //public void renderGround(ItemStack stack, MultiBufferSource buffer) { }
}
