package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.oil.MachineRefineryBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RenderOilRefinery extends BlockEntityRendererNT<MachineRefineryBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineRefineryBlockEntity> create(Context context) {
        return new RenderOilRefinery();
    }

    @Override
    public void render(MachineRefineryBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);

        RenderContext.translate(0.5F, 0F, 0.5F);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            default -> { }
        }

        bindTexture(ResourceManager.REFINERY_TEX);
        ResourceManager.refinery.renderAll();
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_REFINERY.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -4F, 0F);
                RenderContext.scale(1.5F, 1.5F, 1.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.REFINERY_TEX);
                ResourceManager.refinery.renderAll();
            }
        };
    }
}
