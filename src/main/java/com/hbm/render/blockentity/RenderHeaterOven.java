package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.heater.HeaterOvenBlockEntity;
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

public class RenderHeaterOven extends BlockEntityRendererNT<HeaterOvenBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<HeaterOvenBlockEntity> create(Context context) {
        return new RenderHeaterOven();
    }

    @Override
    public void render(HeaterOvenBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);

        RenderContext.translate(0.5F, 0.0F, 0.5F);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            default -> { }
        }
        RenderContext.mulPose(Axis.YP.rotationDegrees(-90F));

        bindTexture(ResourceManager.HEATER_OVEN_TEX);
        ResourceManager.heater_oven.renderPart("Main");

        RenderContext.pushPose();
        float door = be.prevDoorAngle + (be.doorAngle - be.prevDoorAngle) * partialTicks;
        RenderContext.translate(0.0F, 0.0F, door * 0.75F / 135F);
        ResourceManager.heater_oven.renderPart("Door");
        RenderContext.popPose();

        if(be.wasOn) {
            ResourceManager.heater_oven.renderPart("InnerBurning");
        } else {
            ResourceManager.heater_oven.renderPart("Inner");
        }
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.HEATER_OVEN.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0.0F, -1.0F, 0.0F);
                RenderContext.scale(1.9F, 1.9F, 1.9F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
                RenderContext.scale(1.9F, 1.9F, 1.9F);
                bindTexture(ResourceManager.HEATER_OVEN_TEX);
                ResourceManager.heater_oven.renderAll();
            }
        };
    }
}
