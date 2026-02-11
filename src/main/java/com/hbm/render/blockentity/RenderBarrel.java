package com.hbm.render.blockentity;

import com.hbm.blockentity.EmptyBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBaseStandard;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class RenderBarrel extends BlockEntityRendererNT<EmptyBlockEntity> implements IBEWLRProvider {

    public RenderBarrel(Context context) { }

    @Override
    public BlockEntityRenderer<EmptyBlockEntity> create(Context context) {
        return new RenderBarrel(context);
    }

    @Override
    public void render(EmptyBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0.0, 0.5);

        Block block = be.getBlockState().getBlock();

        VertexConsumer consumer;
        if (block == ModBlocks.BARREL_PINK.get()) consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.BARREL_PINK_TEX));
        else consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.BARREL_RED_TEX));
        ResourceManager.barrel.renderAll(poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public Item getItemForRenderer() {
        return null;
    }

    @Override
    public Item[] getItemsForRenderer() {
        return new Item[] {
                ModBlocks.BARREL_RED.asItem(),
                ModBlocks.BARREL_PINK.asItem()
        };
    }


    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBaseStandard() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.scale(10F, 10F, 10F);
                poseStack.translate(0F, -0.3F, 0F);
            }

            @Override
            public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                VertexConsumer consumer;
                if (stack.is(ModBlocks.BARREL_PINK.asItem())) consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.BARREL_PINK_TEX));
                else consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.BARREL_RED_TEX));
                ResourceManager.barrel.renderAll(poseStack, consumer, packedLight, packedOverlay);
            }
        };
    }
}
