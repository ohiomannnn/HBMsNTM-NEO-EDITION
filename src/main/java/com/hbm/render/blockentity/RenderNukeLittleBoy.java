package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.CrashedBombBlockEntity;
import com.hbm.blockentity.bomb.NukeLittleBoyBlockEntity;
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

public class RenderNukeLittleBoy extends BlockEntityRendererNT<NukeLittleBoyBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<NukeLittleBoyBlockEntity> create(Context context) { return new RenderNukeLittleBoy(); }

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

        RenderContext.setup(NtmRenderTypes.FVBO.apply(ResourceManager.NUKE_LITTLE_BOY_TEX), poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5, 0.0, 0.5);
        RenderContext.mulPose(Axis.YP.rotationDegrees(rot));
        RenderContext.translate(-2.0F, 0.0F, 0.0F);

        ResourceManager.nuke_little_boy.renderAll();

        RenderContext.end();
    }

    @Override public boolean shouldRenderOffScreen(NukeLittleBoyBlockEntity be) { return true; }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.NUKE_LITTLE_BOY.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(5F, 5F, 5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(-1F, 0F, 0F);
                RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.NUKE_LITTLE_BOY_TEX));
                ResourceManager.nuke_little_boy.renderAll();
            }
        };
    }
}
