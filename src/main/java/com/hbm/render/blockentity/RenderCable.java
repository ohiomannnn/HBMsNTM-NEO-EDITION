package com.hbm.render.blockentity;

import com.hbm.blockentity.network.CableBlockEntityBaseNT;
import com.hbm.blocks.ModBlocks;
import com.hbm.lib.Library;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBaseStandard;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class RenderCable extends BlockEntityRendererNT<CableBlockEntityBaseNT> implements IBEWLRProvider {

    public RenderCable(Context context) { }

    @Override
    public BlockEntityRenderer<CableBlockEntityBaseNT> create(Context context) {
        return new RenderCable(context);
    }

    @Override
    public void render(CableBlockEntityBaseNT be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Level level = be.getLevel();
        if (level == null) return;

        BlockPos pos = be.getBlockPos();

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
    public int getViewDistance() {
        return 256;
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.CABLE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBaseStandard() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.scale(10F, 10F, 10F);
                poseStack.translate(0F, 0.05F, 0F);
            }

            @Override
            public void renderNonInv(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean righthand) {
                poseStack.translate(0F, 0.4F, 0F);
            }

            @Override
            public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
                poseStack.scale(1.25F, 1.25F, 1.25F);
                poseStack.translate(0F, 0.1F, 0F);
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
