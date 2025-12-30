package com.hbm.render.blockentity;

import com.hbm.blockentity.network.CableBlockEntityBaseNT;
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

public class RenderCable implements BlockEntityRenderer<CableBlockEntityBaseNT> {

    public RenderCable(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(CableBlockEntityBaseNT be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        BlockPos pos = be.getBlockPos();
        Level level = be.getLevel();
        if (level == null) return;

        boolean pX = Library.canConnect(level, pos.relative(Library.POS_X), Library.POS_X);
        boolean nX = Library.canConnect(level, pos.relative(Library.NEG_X), Library.NEG_X);
        boolean pY = Library.canConnect(level, pos.relative(Library.POS_Y), Library.POS_Y);
        boolean nY = Library.canConnect(level, pos.relative(Library.NEG_Y), Library.NEG_Y);
        boolean pZ = Library.canConnect(level, pos.relative(Library.POS_Z), Library.POS_Z);
        boolean nZ = Library.canConnect(level, pos.relative(Library.NEG_Z), Library.NEG_Z);

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.CABLE_NEO_TEX));

        if (pX && nX && !pY && !nY && !pZ && !nZ) {
            ResourceManager.cable_neo.renderPart("CX", poseStack, consumer, packedLight, packedOverlay);
        } else if (!pX && !nX && pY && nY && !pZ && !nZ) {
            ResourceManager.cable_neo.renderPart("CY", poseStack, consumer, packedLight, packedOverlay);
        } else if (!pX && !nX && !pY && !nY && pZ && nZ) {
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
}
