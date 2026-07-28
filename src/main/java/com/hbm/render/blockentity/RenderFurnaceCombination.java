package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineFurnaceCombinationBlockEntity;
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

public class RenderFurnaceCombination extends BlockEntityRendererNT<MachineFurnaceCombinationBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<MachineFurnaceCombinationBlockEntity> create(Context context) { return new RenderFurnaceCombination(); }

    @Override
    public void render(MachineFurnaceCombinationBlockEntity be, MultiBufferSource buffer, float partialTicks) {

        RenderContext.translate(0.5F, 0.0F, 0.5F);

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
        }

        bindTexture(ResourceManager.FURNACE_COMBINATION_TEX);
        ResourceManager.furnace_combination.renderAll();
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.FURNACE_COMBINATION.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -1.5F, 0F);
                RenderContext.scale(3.25F, 3.25F, 3.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.FURNACE_COMBINATION_TEX);
                ResourceManager.furnace_combination.renderAll();
            }
        };
    }
}
