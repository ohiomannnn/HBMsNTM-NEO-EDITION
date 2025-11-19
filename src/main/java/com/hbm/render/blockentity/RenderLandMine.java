package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.LandMineBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

public class RenderLandMine implements BlockEntityRenderer<LandMineBlockEntity> {

    private final BlockRenderDispatcher renderDispatcher;

    public RenderLandMine(BlockEntityRendererProvider.Context context) {
        this.renderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(LandMineBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0.0, 0.5);

        BlockState state = be.getBlockState();
        BakedModel model = renderDispatcher.getBlockModel(state);

        if (state.getBlock() == ModBlocks.MINE_AP.get()) {
            poseStack.scale(0.375F, 0.375F, 0.375F);
            poseStack.translate(0, -0.0625F * 3.5F, 0);
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

        if (state.getBlock() == ModBlocks.MINE_HE.get()) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180F));
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

        if (state.getBlock() == ModBlocks.MINE_SHRAP.get()) {
            poseStack.scale(0.375F, 0.375F, 0.375F);
            poseStack.translate(0, -0.0625F * 3.5F, 0);
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

        if (state.getBlock() == ModBlocks.MINE_FAT.get()) {
            poseStack.scale(0.25F, 0.25F, 0.25F);
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

        if (state.getBlock() == ModBlocks.MINE_NAVAL.get()) {
            poseStack.scale(1F, 1F, 1F);
            poseStack.translate(0, 0.5F, 0);
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
    public boolean shouldRenderOffScreen(LandMineBlockEntity blockEntity) {
        return true;
    }
}
