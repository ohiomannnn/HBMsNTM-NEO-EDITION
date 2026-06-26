package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeSoliniumBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RenderNukeSolinium extends BlockEntityRendererNT<NukeSoliniumBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<NukeSoliniumBlockEntity> create(Context context) { return new RenderNukeSolinium(); }

    @Override
    public void render(NukeSoliniumBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        RenderContext.setup(poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);

        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

        Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        switch(facing) {
            case WEST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
        }

        bindTexture(ResourceManager.NUKE_SOLINIUM_TEX);
        ResourceManager.nuke_solinium.renderAll();

        RenderContext.end();
    }

    @Override public boolean shouldRenderOffScreen(NukeSoliniumBlockEntity be) { return true; }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.NUKE_SOLINIUM.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -0.125F, 0F);
                RenderContext.scale(5F, 5F, 5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.translate(0F, -0.125F, 0F);

                bindTexture(ResourceManager.NUKE_SOLINIUM_TEX);
                ResourceManager.nuke_solinium.renderAll();
            }
        };
    }
}
