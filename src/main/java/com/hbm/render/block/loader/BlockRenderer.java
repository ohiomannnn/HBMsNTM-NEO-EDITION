package com.hbm.render.block.loader;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public interface BlockRenderer {

    void render(BlockPos pos, BlockState state, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, int packedLight, int packedOverlay);

    BlockEntityWithoutLevelRenderer getItemRenderer();

    default int getViewDistance() {
        return 64;
    }

    default boolean shouldRender(BlockPos pos, Vec3 cameraPos) {
        return Vec3.atCenterOf(pos).closerThan(cameraPos, this.getViewDistance());
    }
}
