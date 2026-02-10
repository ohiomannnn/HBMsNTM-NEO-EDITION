package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.CrashedBombBlockEntity;
import com.hbm.blockentity.bomb.CrashedBombBlockEntity.EnumDudType;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;

public class RenderCrashedBomb implements BlockEntityRenderer<CrashedBombBlockEntity> {

    public RenderCrashedBomb(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(CrashedBombBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0.0, 0.5);

        RandomSource rand = RandomSource.create(be.getBlockPos().asLong());

        float yaw = rand.nextFloat() * 360f;
        float pitch = rand.nextFloat() * 45f + 45f;
        float roll = rand.nextFloat() * 360f;
        float offset = rand.nextFloat() * 2f - 1f;

        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(roll));

        poseStack.translate(0.0, 0.0, -offset);

        EnumDudType type = be.getDudType();

        if (type == EnumDudType.BALEFIRE) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DUD_BALEFIRE_TEX));
            ResourceManager.dud_balefire.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
        if (type == EnumDudType.CONVENTIONAL) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DUD_CONVENTIONAL_TEX));
            ResourceManager.dud_conventional.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
        if (type == EnumDudType.NUKE) {
            poseStack.translate(0, 0, 1.25);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DUD_NUKE_TEX));
            ResourceManager.dud_nuke.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
        if (type == EnumDudType.SALTED) {
            poseStack.translate(0, 0, 0.5);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DUD_SALTED_TEX));
            ResourceManager.dud_salted.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
