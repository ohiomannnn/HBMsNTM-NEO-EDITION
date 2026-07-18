package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineSolderingStationBlockEntity;
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

public class RenderSolderingStation extends BlockEntityRendererNT<MachineSolderingStationBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineSolderingStationBlockEntity> create(Context context) {
        return new RenderSolderingStation();
    }

    @Override
    public void render(MachineSolderingStationBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);

        RenderContext.translate(0.5F, 0F, 0.5F);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            default -> { }
        }
        RenderContext.translate(-0.5F, 0F, 0.5F);

        bindTexture(ResourceManager.SOLDERING_STATION_TEX);
        ResourceManager.soldering_station.renderAll();

        if(!be.display.isEmpty()) {
            RenderContext.pushPose();
            RenderContext.translate(0F, 1.125F, 0F);
            RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            RenderContext.mulPose(Axis.XP.rotationDegrees(-90F));

            ItemStack stack = be.display.copy();
            stack.setCount(1);

            RenderContext.scale(0.76923075F, 0.76923075F, 0.76923075F);

            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            BakedModel model = renderer.getModel(stack, null, null, 0);
            renderer.render(stack, ItemDisplayContext.FIXED, false, RenderContext.poseStack(), buffer, this.getPacketLight(RenderContext.light(), be), RenderContext.overlay(), model);
            RenderContext.popPose();
        }
    }

    @Override
    public int getPacketLight(int packedLight, MachineSolderingStationBlockEntity be) {
        return packedLight;
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_SOLDERING_STATION.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -1F, 0F);
                RenderContext.scale(5F, 5F, 5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.SOLDERING_STATION_TEX);
                ResourceManager.soldering_station.renderAll();
            }
        };
    }
}
