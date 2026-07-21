package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.boiler.MachineIndustrialBoilerBlockEntity;
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

public class RenderIndustrialBoiler extends BlockEntityRendererNT<MachineIndustrialBoilerBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineIndustrialBoilerBlockEntity> create(Context context) {
        return new RenderIndustrialBoiler();
    }

    @Override
    public void render(MachineIndustrialBoilerBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);

        RenderContext.translate(0.5F, 0.0F, 0.5F);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            default -> { }
        }

        bindTexture(ResourceManager.INDUSTRIAL_BOILER_TEX);
        if(be.tanks[1].getFill() > be.tanks[1].getMaxFill() * 0.9F) {
            double sine = Math.sin(System.currentTimeMillis() / 50D % (Math.PI * 2));
            sine *= 0.01D;
            RenderContext.scale((float)(1 - sine), (float)(1 + sine), (float)(1 - sine));
        }

        ResourceManager.industrial_boiler.renderAll();
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_INDUSTRIAL_BOILER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0.0F, -2.55F, 0.0F);
                RenderContext.scale(3.05F, 3.05F, 3.05F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.INDUSTRIAL_BOILER_TEX);
                ResourceManager.industrial_boiler.renderAll();
            }
        };
    }
}
