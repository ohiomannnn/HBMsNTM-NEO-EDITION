package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeLittleBoyBlockEntity;
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

public class RenderNukeLittleBoy extends BlockEntityRendererNT<NukeLittleBoyBlockEntity> implements IBEWLRProvider {

    public RenderNukeLittleBoy(Context ignored) { }

    @Override
    public BlockEntityRenderer<NukeLittleBoyBlockEntity> create(Context context) {
        return new RenderNukeLittleBoy(context);
    }

    @Override
    public void render(NukeLittleBoyBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

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
        poseStack.translate(-2.0F, 0.0F, 0.0F);

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.NUKE_LITTLE_BOY_TEX));
        ResourceManager.nuke_little_boy.renderAll(poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.NUKE_LITTLE_BOY.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.scale(5F, 5F, 5F);
            }

            @Override
            public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(-1F, 0F, 0F);
                VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.NUKE_LITTLE_BOY_TEX));
                ResourceManager.nuke_little_boy.renderAll(poseStack, consumer, packedLight, packedOverlay);
            }
        };
    }
}
