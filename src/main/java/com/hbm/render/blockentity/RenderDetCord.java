package com.hbm.render.blockentity;

import com.hbm.blockentity.EmptyBlockEntity;
import com.hbm.blocks.bomb.IDetConnectible;
import com.hbm.lib.Library;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class RenderDetCord implements BlockEntityRenderer<EmptyBlockEntity> {

    public RenderDetCord(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(EmptyBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        BlockPos pos = be.getBlockPos();
        Level level = be.getLevel();
        if (level == null) return;

        boolean pX = IDetConnectible.isConnectible(level, pos.relative(Library.POS_X), Library.POS_X);
        boolean nX = IDetConnectible.isConnectible(level, pos.relative(Library.NEG_X), Library.NEG_X);
        boolean pY = IDetConnectible.isConnectible(level, pos.relative(Library.POS_Y), Library.POS_Y);
        boolean nY = IDetConnectible.isConnectible(level, pos.relative(Library.NEG_Y), Library.NEG_Y);
        boolean pZ = IDetConnectible.isConnectible(level, pos.relative(Library.POS_Z), Library.POS_Z);
        boolean nZ = IDetConnectible.isConnectible(level, pos.relative(Library.NEG_Z), Library.NEG_Z);

        int mask = (pX ? 32 : 0) + (nX ? 16 : 0) + (pY ? 8 : 0) + (nY ? 4 : 0) + (pZ ? 2 : 0) + (nZ ? 1 : 0);

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.DET_CORD_TEX));

        if (mask == 0b110000 || mask == 0b100000 || mask == 0b010000) {
            ResourceManager.cable_neo.renderPart("CX", poseStack, consumer, packedLight, packedOverlay);
        } else if (mask == 0b001100 || mask == 0b001000 || mask == 0b000100) {
            ResourceManager.cable_neo.renderPart("CY", poseStack, consumer, packedLight, packedOverlay);
        } else if (mask == 0b000011 || mask == 0b000010 || mask == 0b000001) {
            ResourceManager.cable_neo.renderPart("CZ", poseStack, consumer, packedLight, packedOverlay);
        } else {
            ResourceManager.cable_neo.renderPart("Core", poseStack, consumer, packedLight, packedOverlay);
            if (pX) ResourceManager.cable_neo.renderPart("posX", poseStack, consumer, packedLight, packedOverlay);
            if (nX) ResourceManager.cable_neo.renderPart("negX", poseStack, consumer, packedLight, packedOverlay);
            if (pY) ResourceManager.cable_neo.renderPart("posY", poseStack, consumer, packedLight, packedOverlay);
            if (nY) ResourceManager.cable_neo.renderPart("negY", poseStack, consumer, packedLight, packedOverlay);
            if (nZ) ResourceManager.cable_neo.renderPart("posZ", poseStack, consumer, packedLight, packedOverlay);
            if (pZ) ResourceManager.cable_neo.renderPart("negZ", poseStack, consumer, packedLight, packedOverlay);
        }
        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
