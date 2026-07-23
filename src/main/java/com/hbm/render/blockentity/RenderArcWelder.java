package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineArcWelderBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderArcWelder extends BlockEntityRendererNT<MachineArcWelderBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineArcWelderBlockEntity> create(Context context) {
        return new RenderArcWelder();
    }

    @Override
    public void render(MachineArcWelderBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        RenderContext.translate(0.5F, 0.0F, 0.5F);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            default -> { }
        }
        RenderContext.translate(-0.5F, 0.0F, 0.0F);

        bindTexture(ResourceManager.ARC_WELDER_TEX);
        ResourceManager.arc_welder.renderAll();

        if(!be.display.isEmpty()) {
            RenderContext.pushPose();
            RenderContext.translate(0.0F, 1.125F, 0.0F);
            RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            RenderContext.mulPose(Axis.XP.rotationDegrees(-90F));
            RenderContext.scale(0.7692307F, 0.7692307F, 0.7692307F);
            ItemStack stack = be.display.copy();
            stack.setCount(1);
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            BakedModel model = renderer.getModel(stack, null, null, 0);
            renderer.render(stack, ItemDisplayContext.FIXED, false, RenderContext.poseStack(), buffer, this.getPacketLight(RenderContext.light(), be), RenderContext.overlay(), model);
            RenderContext.popPose();
        }
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_ARC_WELDER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0.0F, -0.85F, 0.0F);
                RenderContext.scale(4.0F, 4.0F, 4.0F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.ARC_WELDER_TEX);
                ResourceManager.arc_welder.renderAll();
            }
        };
    }
}
