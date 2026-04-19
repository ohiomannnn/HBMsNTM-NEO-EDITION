package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeFatManBlockEntity;
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

public class RenderNukeFatMan extends BlockEntityRendererNT<NukeFatManBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<NukeFatManBlockEntity> create(Context context) { return new RenderNukeFatMan(); }

    @Override
    public void render(NukeFatManBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case EAST -> 90f;
            case NORTH -> 180f;
            case WEST -> 270f;
            default -> 0f;
        };

        RenderContext.setup(NtmRenderTypes.FVBO.apply(ResourceManager.NUKE_FAT_MAN_TEX), poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5, 0.0, 0.5);
        RenderContext.mulPose(Axis.YP.rotationDegrees(rot));

        RenderContext.enableCull(false);
        ResourceManager.nuke_fat_man.renderAll();
        RenderContext.enableCull(true);

        RenderContext.end();
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.NUKE_FAT_MAN.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {

            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0, -2, 0);
                RenderContext.scale(5F, 5F, 5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
                RenderContext.translate(-0.75F, 0F, 0F);

                RenderContext.enableCull(false);

                RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.NUKE_FAT_MAN_TEX));
                ResourceManager.nuke_fat_man.renderAll();

                RenderContext.enableCull(true);
            }
        };
    }
}
