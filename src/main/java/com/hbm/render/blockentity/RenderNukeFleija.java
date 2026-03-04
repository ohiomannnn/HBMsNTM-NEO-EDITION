package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeFleijaBlockEntity;
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

public class RenderNukeFleija extends BlockEntityRendererNT<NukeFleijaBlockEntity> implements IBEWLRProvider {

    public RenderNukeFleija(Context ignored) { }

    @Override
    public BlockEntityRenderer<NukeFleijaBlockEntity> create(Context context) {
        return new RenderNukeFleija(context);
    }

    @Override
    public void render(NukeFleijaBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 180F;
            case SOUTH -> 270F;
            case EAST -> 0F;
            case NORTH -> 90F;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(ResourceManager.NUKE_FLEIJA_TEX));
        ResourceManager.nuke_fleija.renderAll(poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.NUKE_FLEIJA.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.scale(6.8F, 6.8F, 6.8F);
            }

            @Override
            public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0.125F, 0F, 0F);
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
                VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.NUKE_FLEIJA_TEX));
                ResourceManager.nuke_fleija.renderAll(poseStack, consumer, packedLight, packedOverlay);
            }
        };
    }
}
