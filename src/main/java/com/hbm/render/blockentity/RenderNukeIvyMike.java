package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeIvyMikeBlockEntity;
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

public class RenderNukeIvyMike implements BlockEntityRenderer<NukeIvyMikeBlockEntity> {

    public RenderNukeIvyMike(BlockEntityRendererProvider.Context ignored) { }

    @Override
    public void render(NukeIvyMikeBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 0F;
            case SOUTH -> 90F;
            case EAST -> 180F;
            case NORTH -> 270F;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.NUKE_IVY_MIKE));
        ResourceManager.nuke_ivy_mike.renderAll(poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}
