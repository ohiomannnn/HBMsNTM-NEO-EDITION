package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeBoyBlockEntity;
import com.hbm.blockentity.bomb.NukeFatManBlockEntity;
import com.hbm.blocks.bomb.NukeBoyBlock;
import com.hbm.blocks.bomb.NukeManBlock;
import com.hbm.main.ResourceManager;
import com.hbm.render.loader.IModelCustom;
import com.hbm.render.loader.bakedLoader.HFRObjGeometry.HFRObjBakedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class RenderNukeLittleBoy implements BlockEntityRenderer<NukeBoyBlockEntity> {

    public RenderNukeLittleBoy(BlockEntityRendererProvider.Context ignored) { }

    @Override
    public void render(NukeBoyBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(NukeBoyBlock.FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case NORTH -> 0F;
            case EAST -> 270F;
            case SOUTH -> 180F;
            case WEST -> 90F;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));
        poseStack.translate(-2.0F, 0.0F, 0.0F);

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.NUKE_LITTLE_BOY_TEX));
        ResourceManager.nuke_little_boy.renderAll(poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}
