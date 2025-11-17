package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeFatManBlockEntity;
import com.hbm.blocks.bomb.NukeManBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class RenderNukeFatMan implements BlockEntityRenderer<NukeFatManBlockEntity> {

    private final BlockRenderDispatcher renderDispatcher;

    public RenderNukeFatMan(BlockEntityRendererProvider.Context context) {
        this.renderDispatcher = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void render(NukeFatManBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(NukeManBlock.FACING);
        float rot = switch (facing) {
            case EAST -> 90f;
            case NORTH -> 180f;
            case WEST -> 270f;
            default -> 0f;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        BlockState state = be.getBlockState();
        BakedModel model = renderDispatcher.getBlockModel(state);

        renderDispatcher.getModelRenderer().renderModel(
                poseStack.last(),
                buffer.getBuffer(RenderType.cutout()),
                state,
                model,
                1.0f, 1.0f, 1.0f,
                packedLight,
                packedOverlay
        );

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(NukeFatManBlockEntity blockEntity) {
        return true;
    }
}
