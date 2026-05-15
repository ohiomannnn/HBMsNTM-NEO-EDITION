package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeIvyMikeBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RenderNukeIvyMike extends BlockEntityRendererNT<NukeIvyMikeBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<NukeIvyMikeBlockEntity> create(Context context) { return new RenderNukeIvyMike(); }

    @Override
    public void render(NukeIvyMikeBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        RenderContext.setup(poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5F, 0, 0.5F);
        RenderSystem.disableCull();

        Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        switch(facing) {
            case WEST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case EAST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(180));
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
        }

        bindTexture(ResourceManager.NUKE_IVY_MIKE_TEX);
        ResourceManager.nuke_ivy_mike.renderAll();

        RenderSystem.enableCull();
        RenderContext.end();
    }

    @Override public boolean shouldRenderOffScreen(NukeIvyMikeBlockEntity be) { return true; }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.NUKE_IVY_MIKE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -5F, 0F);
                RenderContext.scale(2.25F, 2.25F, 2.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.NUKE_IVY_MIKE_TEX);
                ResourceManager.nuke_ivy_mike.renderAll();
            }
        };
    }
}
