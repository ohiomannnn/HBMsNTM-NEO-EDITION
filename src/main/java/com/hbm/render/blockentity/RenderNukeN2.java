package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeN2BlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
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

public class RenderNukeN2 extends BlockEntityRendererNT<NukeN2BlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<NukeN2BlockEntity> create(Context context) { return new RenderNukeN2(); }

    @Override
    public void render(NukeN2BlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 90F;
            case SOUTH -> 180F;
            case EAST -> 270F;
            case NORTH -> 0F;
        };

        RenderContext.setup(NtmRenderTypes.FVBO.apply(ResourceManager.NUKE_N2_TEX), poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5, 0.0, 0.5);
        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
        RenderContext.mulPose(Axis.YP.rotationDegrees(rot));

        ResourceManager.nuke_n2.renderAll();

        RenderContext.end();
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.NUKE_N2.asItem();
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
                RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.NUKE_N2_TEX));
                ResourceManager.nuke_n2.renderAll();
            }
        };
    }
}
