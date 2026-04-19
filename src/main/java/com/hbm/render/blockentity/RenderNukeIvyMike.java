package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeIvyMikeBlockEntity;
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

public class RenderNukeIvyMike extends BlockEntityRendererNT<NukeIvyMikeBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<NukeIvyMikeBlockEntity> create(Context context) { return new RenderNukeIvyMike(); }

    @Override
    public void render(NukeIvyMikeBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 0F;
            case SOUTH -> 90F;
            case EAST -> 180F;
            case NORTH -> 270F;
        };

        RenderContext.setup(NtmRenderTypes.FVBO_NC.apply(ResourceManager.NUKE_IVY_MIKE), poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5, 0.0, 0.5);
        RenderContext.mulPose(Axis.YP.rotationDegrees(rot));

        ResourceManager.nuke_ivy_mike.renderAll();

        RenderContext.end();
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.NUKE_IVY_MIKE.asItem();
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
                RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(ResourceManager.NUKE_IVY_MIKE));
                ResourceManager.nuke_ivy_mike.renderAll();
            }
        };
    }
}
