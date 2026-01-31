package com.hbm.render.item;

import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;

public class RenderBarrelItem extends ItemRenderBaseStandard {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.scale(10F, 10F, 10F);
        poseStack.translate(0F, -0.3F, 0F);
    }

    @Override
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        VertexConsumer consumer;
        if (stack.is(ModBlocks.BARREL_PINK.asItem())) consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.BARREL_PINK_TEX));
        else consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.BARREL_RED_TEX));
        ResourceManager.barrel.renderAll(poseStack, consumer, packedLight, packedOverlay);
    }
}
