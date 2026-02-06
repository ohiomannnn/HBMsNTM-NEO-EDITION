package com.hbm.render.item;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.PlushieBlock.PlushieType;
import com.hbm.render.blockentity.RenderPlushie;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class RenderPlushieItem extends ItemRenderBase {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0F, -6F, 0F);
        poseStack.scale(6F, 6F, 6F);
    }

    @Override
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0F, 0.25F, 0F);

        PlushieType type = getType(stack);

        if (type == null) return;

        switch (type) {
            case YOMI -> poseStack.scale(1.25F, 1.25F, 1.25F);
            case NUMBERNINE -> {
                poseStack.translate(0F, 0.25F, 0.25F);
                poseStack.scale(1.25F, 1.25F, 1.25F);
            }
            case HUNDUN -> {
                poseStack.translate(0.5F, 0.5F, 0);
                poseStack.scale(1.25F, 1.25F, 1.25F);
            }
            case DERG -> poseStack.scale(1.5F, 1.5F, 1.5F);
        }

        RenderPlushie.renderPlushie(poseStack, buffer, packedLight, packedOverlay, type, false);
    }

    @Nullable
    private static PlushieType getType(ItemStack stack) {
        if (stack.is(ModBlocks.PLUSHIE_YOMI.asItem())) return PlushieType.YOMI;
        if (stack.is(ModBlocks.PLUSHIE_NUMBERNINE.asItem())) return PlushieType.NUMBERNINE;
        if (stack.is(ModBlocks.PLUSHIE_HUNDUN.asItem())) return PlushieType.HUNDUN;
        if (stack.is(ModBlocks.PLUSHIE_DERG.asItem())) return PlushieType.DERG;
        return null;
    }
}
