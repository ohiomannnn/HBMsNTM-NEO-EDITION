package com.hbm.render.block;

import com.hbm.lib.Library;
import com.hbm.main.ResourceManager;
import com.hbm.render.block.loader.BlockRenderer;
import com.hbm.render.item.ItemRenderBaseStandard;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RenderCableBlock implements BlockRenderer {

    @Override
    public void render(BlockPos pos, BlockState state, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, int packedLight, int packedOverlay) {

        Level level = Minecraft.getInstance().level;
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

    @Override
    public BlockEntityWithoutLevelRenderer getItemRenderer() {
        return new ItemRenderBaseStandard() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0F, 0.15F, 0F);
            }

            @Override
            public void renderNonInv(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean righthand) {
                poseStack.translate(0F, 0.5F, 0F);
            }

            @Override
            public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
                poseStack.scale(1.25F, 1.25F, 1.25F);
                VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.CABLE_NEO_TEX));
                ResourceManager.cable_neo.renderPart("Core", poseStack, consumer, packedLight, packedOverlay);
                ResourceManager.cable_neo.renderPart("posX", poseStack, consumer, packedLight, packedOverlay);
                ResourceManager.cable_neo.renderPart("negX", poseStack, consumer, packedLight, packedOverlay);
                ResourceManager.cable_neo.renderPart("posZ", poseStack, consumer, packedLight, packedOverlay);
                ResourceManager.cable_neo.renderPart("negZ", poseStack, consumer, packedLight, packedOverlay);
            }
        };
    }
}
