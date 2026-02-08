package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeN2BlockEntity;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RenderNukeN2 implements BlockEntityRenderer<NukeN2BlockEntity> {

    public RenderNukeN2(BlockEntityRendererProvider.Context ignored) { }

    @Override
    public void render(NukeN2BlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 90F;
            case SOUTH -> 180F;
            case EAST -> 270F;
            case NORTH -> 0F;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(90F));
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(ResourceManager.NUKE_N2_TEX));
        ResourceManager.nuke_n2.renderAll(poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}
