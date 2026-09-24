package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineIndustrialTurbineBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RenderIndustrialTurbine extends BlockEntityRendererNT<MachineIndustrialTurbineBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<MachineIndustrialTurbineBlockEntity> create(Context context) { return new RenderIndustrialTurbine(); }

    @Override
    public void render(MachineIndustrialTurbineBlockEntity be, MultiBufferSource buffer, float partialTicks) {

        RenderContext.translate(0.5F, 0F, 0.5F);

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
        }

        bindTexture(ResourceManager.INDUSTRIAL_TURBINE_TEX);
        ResourceManager.industrial_turbine.renderPart("Turbine");

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.5F, 0F);
        RenderContext.mulPose(Axis.ZP.rotationDegrees(135 - (be.tanks[0].getTankType().getID() - Fluids.STEAM.getID()) * 90F));
        RenderContext.translate(0F, -1.5F, 0F);
        ResourceManager.industrial_turbine.renderPart("Gauge");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.5F, 0F);
        RenderContext.mulPose(Axis.ZN.rotationDegrees(Mth.lerp(partialTicks, be.lastRotor, be.rotor)));
        RenderContext.translate(0F, -1.5F, 0F);
        ResourceManager.industrial_turbine.renderPart("Flywheel");
        RenderContext.popPose();
    }

    @Override
    public int getPacketLight(int packedLight, MachineIndustrialTurbineBlockEntity be) {
        if(be.getLevel() != null && be.getBlockState().getBlock() instanceof DummyableBlock dummy) {
            return LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(dummy.getDimensions()[0]));
        }
        return packedLight;
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_INDUSTRIAL_TURBINE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(1F, 0F, 0F);
                RenderContext.scale(3F, 3F, 3F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.scale(0.75F, 0.75F, 0.75F);
                RenderContext.translate(0.5F, 0F, 0F);
                bindTexture(ResourceManager.INDUSTRIAL_TURBINE_TEX);
                ResourceManager.industrial_turbine.renderPart("Turbine");

                RenderContext.translate(0F, 1.5F, 0F);
                RenderContext.mulPose(Axis.ZP.rotationDegrees(135));
                RenderContext.translate(0F, -1.5F, 0F);
                ResourceManager.industrial_turbine.renderPart("Gauge");

                double rot = (System.currentTimeMillis() / 5) % 336.0;
                RenderContext.translate(0F, 1.5F, 0F);
                RenderContext.mulPose(Axis.ZN.rotationDegrees((float) rot));
                RenderContext.translate(0F, -1.5F, 0F);
                ResourceManager.industrial_turbine.renderPart("Flywheel");
            }
        };
    }
}
