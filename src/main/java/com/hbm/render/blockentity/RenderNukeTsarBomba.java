package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeTsarBombaBlockEntity;
import com.hbm.blocks.bomb.NukeBaseBlock;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class RenderNukeTsarBomba implements BlockEntityRenderer<NukeTsarBombaBlockEntity> {

    public RenderNukeTsarBomba(BlockEntityRendererProvider.Context ignored) { }

    @Override
    public void render(NukeTsarBombaBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(NukeBaseBlock.FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 90F;
            case SOUTH -> 180F;
            case EAST -> 270F;
            case NORTH -> 0F;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.NUKE_TSAR_TEX));
        ResourceManager.nuke_tsar.renderAll(poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}
