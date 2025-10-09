package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.CrashedBombBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class RenderCrashedBomb implements BlockEntityRenderer<CrashedBombBlockEntity> {

    private final BlockRenderDispatcher renderDispatcher;

    public RenderCrashedBomb(BlockEntityRendererProvider.Context ctx) {
        this.renderDispatcher = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void render(CrashedBombBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0.0, 0.5);

        RandomSource rand = RandomSource.create(be.getBlockPos().asLong());

        float yaw   = rand.nextFloat() * 360f;
        float pitch = rand.nextFloat() * 45f + 45f;
        float roll  = rand.nextFloat() * 360f;
        float offset = rand.nextFloat() * 2f - 1f;

        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(roll));

        BlockState state = be.getBlockState();
        BakedModel model = renderDispatcher.getBlockModel(state);

        poseStack.translate(0.0, 0.0, -offset);

        CrashedBombBlockEntity.EnumDudType type = be.getDudType();

        if (type == CrashedBombBlockEntity.EnumDudType.BALEFIRE) {
            renderDispatcher.getModelRenderer().renderModel(
                    poseStack.last(),
                    buffer.getBuffer(RenderType.cutout()),
                    state,
                    model,
                    1.0f, 1.0f, 1.0f,
                    packedLight,
                    packedOverlay
            );
        }
        if (type == CrashedBombBlockEntity.EnumDudType.CONVENTIONAL) {
            renderDispatcher.getModelRenderer().renderModel(
                    poseStack.last(),
                    buffer.getBuffer(RenderType.cutout()),
                    state,
                    model,
                    1.0f, 1.0f, 1.0f,
                    packedLight,
                    packedOverlay
            );
        }
        if (type == CrashedBombBlockEntity.EnumDudType.NUKE) {
            poseStack.translate(0, 0, 1.25);
            renderDispatcher.getModelRenderer().renderModel(
                    poseStack.last(),
                    buffer.getBuffer(RenderType.cutout()),
                    state,
                    model,
                    1.0f, 1.0f, 1.0f,
                    packedLight,
                    packedOverlay
            );
        }
        if (type == CrashedBombBlockEntity.EnumDudType.SALTED) {
            poseStack.translate(0, 0, 0.5);
            renderDispatcher.getModelRenderer().renderModel(
                    poseStack.last(),
                    buffer.getBuffer(RenderType.cutout()),
                    state,
                    model,
                    1.0f, 1.0f, 1.0f,
                    packedLight,
                    packedOverlay
            );
        }

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(CrashedBombBlockEntity blockEntity) {
        return true;
    }
}
