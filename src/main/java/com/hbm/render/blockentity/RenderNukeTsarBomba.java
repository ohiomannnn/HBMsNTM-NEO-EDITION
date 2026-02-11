package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeTsarBombaBlockEntity;
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

public class RenderNukeTsarBomba extends BlockEntityRendererNT<NukeTsarBombaBlockEntity> implements IBEWLRProvider {

    public RenderNukeTsarBomba(Context ignored) { }

    @Override
    public BlockEntityRenderer<NukeTsarBombaBlockEntity> create(Context context) {
        return new RenderNukeTsarBomba(context);
    }

    @Override
    public void render(NukeTsarBombaBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 90F;
            case SOUTH -> 180F;
            case EAST -> 270F;
            case NORTH -> 0F;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(ResourceManager.NUKE_TSAR_TEX));
        ResourceManager.nuke_tsar.renderAll(poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }
    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.NUKE_TSAR_BOMBA.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.scale(2.25F, 2.25F, 2.25F);
            }

            @Override
            public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(1.5F, 0F, 0F);
                VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.NUKE_TSAR_TEX));
                ResourceManager.nuke_tsar.renderAll(poseStack, consumer, packedLight, packedOverlay);
            }
        };
    }
}
