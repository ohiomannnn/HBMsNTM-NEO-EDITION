package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.CrashedBombBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.bomb.CrashedBombBlock.DudType;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RenderCrashedBomb extends BlockEntityRendererNT<CrashedBombBlockEntity> implements IBEWLRProvider {

    public RenderCrashedBomb(Context context) { }

    @Override
    public BlockEntityRenderer<CrashedBombBlockEntity> create(Context context) {
        return new RenderCrashedBomb(context);
    }

    @Override
    public void render(CrashedBombBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0.0, 0.5);

        RandomSource rand = RandomSource.create(be.getBlockPos().asLong());

        float yaw = rand.nextFloat() * 360f;
        float pitch = rand.nextFloat() * 45f + 45f;
        float roll = rand.nextFloat() * 360f;
        float offset = rand.nextFloat() * 2f - 1f;

        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(roll));

        poseStack.translate(0.0, 0.0, -offset);

        DudType type = be.type;

        if (type == DudType.BALEFIRE) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DUD_BALEFIRE_TEX));
            ResourceManager.dud_balefire.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
        if (type == DudType.CONVENTIONAL) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DUD_CONVENTIONAL_TEX));
            ResourceManager.dud_conventional.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
        if (type == DudType.NUKE) {
            poseStack.translate(0, 0, 1.25);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DUD_NUKE_TEX));
            ResourceManager.dud_nuke.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }
        if (type == DudType.SALTED) {
            poseStack.translate(0, 0, 0.5);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DUD_SALTED_TEX));
            ResourceManager.dud_salted.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }

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
                ModBlocks.CRASHED_BOMB_BALEFIRE.asItem(),
                ModBlocks.CRASHED_BOMB_CONVENTIONAL.asItem(),
                ModBlocks.CRASHED_BOMB_NUKE.asItem(),
                ModBlocks.CRASHED_BOMB_SALTED.asItem()
        };
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0, 3, 0);
                poseStack.scale(2.125F, 2.125F, 2.125F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(90F));
            }

            @Override
            public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
                if (stack.is(ModBlocks.CRASHED_BOMB_BALEFIRE.asItem())) {
                    VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.DUD_BALEFIRE_TEX));
                    ResourceManager.dud_balefire.renderAll(poseStack, consumer, packedLight, packedOverlay);
                }
                if (stack.is(ModBlocks.CRASHED_BOMB_CONVENTIONAL.asItem())) {
                    poseStack.translate(0F, 0F, -0.5F);
                    VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DUD_CONVENTIONAL_TEX));
                    ResourceManager.dud_conventional.renderAll(poseStack, consumer, packedLight, packedOverlay);
                }
                if (stack.is(ModBlocks.CRASHED_BOMB_NUKE.asItem())) {
                    poseStack.translate(0F, 0F, 1.25F);
                    VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.DUD_NUKE_TEX));
                    ResourceManager.dud_nuke.renderAll(poseStack, consumer, packedLight, packedOverlay);
                }
                if (stack.is(ModBlocks.CRASHED_BOMB_SALTED.asItem())) {
                    poseStack.translate(0F, 0F, 0.5F);
                    VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.DUD_SALTED_TEX));
                    ResourceManager.dud_salted.renderAll(poseStack, consumer, packedLight, packedOverlay);
                }
            }
        };
    }
}
