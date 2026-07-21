package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineWoodBurnerBlockEntity;
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

public class RenderWoodBurner extends BlockEntityRendererNT<MachineWoodBurnerBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineWoodBurnerBlockEntity> create(Context context) {
        return new RenderWoodBurner();
    }

    @Override
    public void render(MachineWoodBurnerBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = facing.getClockWise();

        RenderContext.translate(
                0.5F + facing.getOpposite().getStepX() * 0.5F + side.getStepX() * 0.5F,
                0.0F,
                0.5F + facing.getOpposite().getStepZ() * 0.5F + side.getStepZ() * 0.5F
        );
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            default -> { }
        }

        renderModel();
    }

    private static void renderModel() {
        RenderSystem.setShaderTexture(0, ResourceManager.WOOD_BURNER_TEX);
        ResourceManager.wood_burner.renderAll();
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_WOOD_BURNER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0.0F, -0.75F, 0.0F);
                RenderContext.scale(3.5F, 3.5F, 3.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                renderModel();
            }
        };
    }
}
