package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeFatManBlockEntity;
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

public class RenderNukeFatMan implements BlockEntityRenderer<NukeFatManBlockEntity> {

    public RenderNukeFatMan(BlockEntityRendererProvider.Context ignored) { }

    @Override
    public void render(NukeFatManBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case EAST -> 90f;
            case NORTH -> 180f;
            case WEST -> 270f;
            default -> 0f;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(ResourceManager.NUKE_FAT_MAN_TEX));
        ResourceManager.nuke_fat_man.renderAll(poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
