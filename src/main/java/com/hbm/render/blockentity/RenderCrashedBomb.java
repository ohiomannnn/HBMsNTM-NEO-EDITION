package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.CrashedBombBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.bomb.CrashedBombBlock.DudType;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RenderCrashedBomb extends BlockEntityRendererNT<CrashedBombBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<CrashedBombBlockEntity> create(Context context) { return new RenderCrashedBomb(); }

    @Override
    public void render(CrashedBombBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        RenderStateManager.setupR(null, poseStack, packedLight, packedOverlay);

        RenderStateManager.translate(0.5, 0.0, 0.5);

        RandomSource rand = RandomSource.create(be.getBlockPos().asLong());

        float yaw = rand.nextFloat() * 360f;
        float pitch = rand.nextFloat() * 45f + 45f;
        float roll = rand.nextFloat() * 360f;
        float offset = rand.nextFloat() * 2f - 1f;

        RenderStateManager.mulPose(Axis.YP.rotationDegrees(yaw));
        RenderStateManager.mulPose(Axis.XP.rotationDegrees(pitch));
        RenderStateManager.mulPose(Axis.ZP.rotationDegrees(roll));

        RenderStateManager.translate(0.0, 0.0, -offset);

        DudType type = be.type;

        if (type == DudType.BALEFIRE) {
            RenderStateManager.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_BALEFIRE_TEX));
            ResourceManager.dud_balefire.renderAll();
        }
        if (type == DudType.CONVENTIONAL) {
            RenderStateManager.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_CONVENTIONAL_TEX));
            ResourceManager.dud_conventional.renderAll();
        }
        if (type == DudType.NUKE) {
            RenderStateManager.translate(0, 0, 1.25);
            RenderStateManager.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_NUKE_TEX));
            ResourceManager.dud_nuke.renderAll();
        }
        if (type == DudType.SALTED) {
            RenderStateManager.translate(0, 0, 0.5);
            RenderStateManager.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_SALTED_TEX));
            ResourceManager.dud_salted.renderAll();
        }

        RenderStateManager.end();
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
                RenderStateManager.setupR(null, poseStack, packedLight, packedOverlay);
                if (stack.is(ModBlocks.CRASHED_BOMB_BALEFIRE.asItem())) {
                    RenderStateManager.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_BALEFIRE_TEX));
                    ResourceManager.dud_balefire.renderAll();
                }
                if (stack.is(ModBlocks.CRASHED_BOMB_CONVENTIONAL.asItem())) {
                    poseStack.translate(0F, 0F, -0.5F);
                    RenderStateManager.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_CONVENTIONAL_TEX));
                    ResourceManager.dud_conventional.renderAll();
                }
                if (stack.is(ModBlocks.CRASHED_BOMB_NUKE.asItem())) {
                    poseStack.translate(0F, 0F, 1.25F);
                    RenderStateManager.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_NUKE_TEX));
                    ResourceManager.dud_nuke.renderAll();
                }
                if (stack.is(ModBlocks.CRASHED_BOMB_SALTED.asItem())) {
                    poseStack.translate(0F, 0F, 0.5F);
                    RenderStateManager.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_SALTED_TEX));
                    ResourceManager.dud_salted.renderAll();
                }
                RenderStateManager.end();
            }
        };
    }
}
