package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineBlastFurnaceBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RenderBlastFurnace extends BlockEntityRendererNT<MachineBlastFurnaceBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineBlastFurnaceBlockEntity> create(Context context) {
        return new RenderBlastFurnace();
    }

    @Override
    public void render(MachineBlastFurnaceBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);

        RenderContext.translate(0.5F, 0.0F, 0.5F);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            default -> { }
        }

        renderModel();
    }

    private static void renderModel() {
        bindModelTexture();
        RenderSystem.enableCull();
        ResourceManager.blast_furnace.renderAll();
    }

    private static void bindModelTexture() {
        RenderSystem.setShaderTexture(0, ResourceManager.BLAST_FURNACE_TEX);
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_BLAST_FURNACE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0.0F, -2.75F, 0.0F);
                RenderContext.scale(2.35F, 2.35F, 2.35F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                renderModel();
            }
        };
    }
}
