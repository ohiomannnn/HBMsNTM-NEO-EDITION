package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.GeigerBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RenderGeigerBlock extends BlockEntityRendererNT<GeigerBlockEntity> implements IBEWLRProvider {

    public RenderGeigerBlock(Context context) { }

    @Override
    public BlockEntityRenderer<GeigerBlockEntity> create(Context context) {
        return new RenderGeigerBlock(context);
    }

    @Override
    public void render(GeigerBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rot = switch (facing) {
            case EAST -> 90f;
            case NORTH -> 180f;
            case WEST -> 270f;
            default -> 0f;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.GEIGER_TEX));
        ResourceManager.geiger.renderAll(poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.GEIGER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.scale(10F, 10F, 10F);
            }

            @Override
            public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0.2F, 0F, 0F);
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
                VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.GEIGER_TEX));
                ResourceManager.geiger.renderAll(poseStack, consumer, packedLight, packedOverlay);
            }
        };
    }
}
