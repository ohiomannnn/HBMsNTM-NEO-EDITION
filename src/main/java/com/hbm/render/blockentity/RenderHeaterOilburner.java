package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.heater.HeaterOilburnerBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RenderHeaterOilburner extends BlockEntityRendererNT<HeaterOilburnerBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<HeaterOilburnerBlockEntity> create(Context context) {
        return new RenderHeaterOilburner();
    }

    @Override
    public void render(HeaterOilburnerBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);

        RenderContext.translate(0.5F, 0.0F, 0.5F);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            default -> { }
        }

        bindTexture(ResourceManager.HEATER_OILBURNER_TEX);
        ResourceManager.heater_oilburner.renderAll();
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.HEATER_OILBURNER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0.0F, -1.0F, 0.0F);
                RenderContext.scale(2.75F, 2.75F, 2.75F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.HEATER_OILBURNER_TEX);
                ResourceManager.heater_oilburner.renderAll();
            }
        };
    }
}
