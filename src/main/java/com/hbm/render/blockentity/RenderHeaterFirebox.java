package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.heater.HeaterFireboxBlockEntity;
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

public class RenderHeaterFirebox extends BlockEntityRendererNT<HeaterFireboxBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<HeaterFireboxBlockEntity> create(Context context) {
        return new RenderHeaterFirebox();
    }

    @Override
    public void render(HeaterFireboxBlockEntity be, MultiBufferSource buffer, float partialTicks) {
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

        bindTexture(ResourceManager.HEATER_FIREBOX_TEX);
        ResourceManager.heater_firebox.renderPart("Main");

        RenderContext.pushPose();
        float door = be.prevDoorAngle + (be.doorAngle - be.prevDoorAngle) * partialTicks;
        RenderContext.translate(1.375F, 0.0F, 0.375F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(-door));
        RenderContext.translate(-1.375F, 0.0F, -0.375F);
        ResourceManager.heater_firebox.renderPart("Door");
        RenderContext.popPose();

        if(be.wasOn) {
            RenderContext.pushPose();
            ResourceManager.heater_firebox.renderPart("InnerBurning");
            RenderContext.popPose();
        } else {
            ResourceManager.heater_firebox.renderPart("InnerEmpty");
        }
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.HEATER_FIREBOX.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0.0F, -1.0F, 0.0F);
                RenderContext.scale(3.25F, 3.25F, 3.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.HEATER_FIREBOX_TEX);
                ResourceManager.heater_firebox.renderAll();
            }
        };
    }
}
