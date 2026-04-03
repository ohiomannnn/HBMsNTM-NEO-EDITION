package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.LaunchPadBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.item.ItemRenderMissileGeneric;
import com.hbm.render.item.ItemRenderMissileGeneric.RocketModelData;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RenderLaunchPad extends BlockEntityRendererNT<LaunchPadBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<LaunchPadBlockEntity> create(Context context) { return new RenderLaunchPad(); }

    @Override
    public void render(LaunchPadBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 90F;
            case SOUTH -> 180F;
            case EAST -> 270F;
            case NORTH -> 0F;
        };

        RenderContext.setup(NtmRenderTypes.FVBO.apply(ResourceManager.MISSILE_PAD_TEX), poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5, 0.0, 0.5);
        RenderContext.mulPose(Axis.YP.rotationDegrees(rot));

        ResourceManager.missile_pad.renderAll();

        ItemStack toRender = be.toRender;

        if (!toRender.isEmpty()) {
            poseStack.translate(0F, 1F, 0F);
            RocketModelData render = ItemRenderMissileGeneric.renderers.get(new ComparableStack(toRender).makeSingular());
            if (render != null) render.render(true);
        }

        RenderContext.end();
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.LAUNCH_PAD.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0F, -1F, 0F);
                poseStack.scale(3F, 3F, 3F);
            }

            @Override
            public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                RenderContext.setup(NtmRenderTypes.FVBO.apply(ResourceManager.MISSILE_PAD_TEX), poseStack, packedLight, packedOverlay);
                ResourceManager.missile_pad.renderAll();
                RenderContext.end();
            }
        };
    }
}
