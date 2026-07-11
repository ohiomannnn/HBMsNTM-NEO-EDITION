package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineRadarBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderRadar extends BlockEntityRendererNT<MachineRadarBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<MachineRadarBlockEntity> create(Context context) { return new RenderRadar(); }

    @Override
    public void render(MachineRadarBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(180F));

        RenderSystem.disableCull();
        bindTexture(ResourceManager.RADAR_BASE_TEX);
        ResourceManager.radar.renderPart("Base");

        RenderContext.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, be.prevRotation, be.rotation)));
        RenderContext.translate(-0.125F, 0F, 0F);

        bindTexture(ResourceManager.RADAR_DISH_TEX);
        ResourceManager.radar.renderPart("Dish");
        RenderSystem.enableCull();
    }

    private AABB bb = null;

    @Override
    public AABB getRenderBoundingBox(MachineRadarBlockEntity be) {

        if(bb == null) {
            int x = be.getBlockPos().getX();
            int y = be.getBlockPos().getY();
            int z = be.getBlockPos().getZ();

            bb = new AABB(x - 1, y, z - 1, x + 2, y + 3, z + 2);
        }

        return bb;
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_RADAR.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -4F, 0F);
                RenderContext.scale(5F, 5F, 5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderSystem.disableCull();
                bindTexture(ResourceManager.RADAR_BASE_TEX); ResourceManager.radar.renderPart("Base");
                RenderContext.translate(-0.125F, 0F, 0F);
                bindTexture(ResourceManager.RADAR_DISH_TEX); ResourceManager.radar.renderPart("Dish");
                RenderSystem.enableCull();
            }
        };
    }
}
