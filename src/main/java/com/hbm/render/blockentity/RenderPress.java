package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachinePressBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderPress extends BlockEntityRendererNT<MachinePressBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<MachinePressBlockEntity> create(Context context) { return new RenderPress(); }

    @Override
    public void render(MachinePressBlockEntity be, MultiBufferSource buffer, float partialTicks) {

        RenderContext.translate(0.5F, 0F, 0.5F);

        RenderContext.pushPose(); {
            RenderContext.mulPose(Axis.YP.rotationDegrees(180F));

            ResourceManager.press_body_render.render();
        } RenderContext.popPose();

        RenderContext.pushPose(); {
            RenderContext.scale(0.99F, 1F, 0.99F);

            float lerp = (Mth.lerp(partialTicks, be.lastPress, be.renderPress) / (float) MachinePressBlockEntity.maxPress);
            RenderContext.translate(0F, Mth.clamp((1F - lerp), 0F, 1F) * 0.875F, 0);

            ResourceManager.press_head_render.render();
        } RenderContext.popPose();

        RenderContext.pushPose(); {

            ItemStack stack = be.syncStack;

            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            BakedModel model = renderer.getModel(stack, null, null, 0);

            RenderContext.translate(0F, 1F, 0F);
            RenderContext.mulPose(Axis.XP.rotationDegrees(-90F));
            RenderContext.scale(0.6F, 0.6F, 0.6F);

            renderer.render(stack, ItemDisplayContext.FIXED, false, RenderContext.poseStack(), buffer, RenderContext.light(), RenderContext.overlay(), model);

        } RenderContext.popPose();
    }

    @Override
    public int getPacketLight(int packedLight, MachinePressBlockEntity be) {
        if(be.getLevel() != null && be.getBlockState().getBlock() instanceof DummyableBlock dummy) {
            return LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(dummy.getDimensions()[0]));
        }
        return packedLight;
    }

    private AABB bb = null;

    @Override
    public AABB getRenderBoundingBox(MachinePressBlockEntity be) {

        if(bb == null) {
            int x = be.getBlockPos().getX();
            int y = be.getBlockPos().getY();
            int z = be.getBlockPos().getZ();

            bb = new AABB(x, y, z, x + 1, y + 3, z + 1);
        }

        return bb;
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_PRESS.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -4F, 0F);
                RenderContext.scale(4.5F, 4.5F, 4.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                ResourceManager.press_body_render.render();
                RenderContext.translate(0F, 0.5F, 0F);
                ResourceManager.press_head_render.render();
            }
        };
    }
}
