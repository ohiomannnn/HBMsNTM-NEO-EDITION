package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.CrashedBombBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.bomb.CrashedBombBlock.DudType;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
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

        RenderContext.setup(poseStack, packedLight, packedOverlay);

        RenderContext.translate(0.5, 0.0, 0.5);

        RandomSource rand = RandomSource.create(be.getBlockPos().asLong());

        float yaw = rand.nextFloat() * 360f;
        float pitch = rand.nextFloat() * 45f + 45f;
        float roll = rand.nextFloat() * 360f;
        float offset = rand.nextFloat() * 2f - 1f;

        RenderContext.mulPose(Axis.YP.rotationDegrees(yaw));
        RenderContext.mulPose(Axis.XP.rotationDegrees(pitch));
        RenderContext.mulPose(Axis.ZP.rotationDegrees(roll));

        RenderContext.translate(0.0, 0.0, -offset);

        DudType type = be.type;

        if (type == DudType.BALEFIRE) {
            RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_BALEFIRE_TEX));
            ResourceManager.dud_balefire.renderAll();
        }
        if (type == DudType.CONVENTIONAL) {
            RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_CONVENTIONAL_TEX));
            ResourceManager.dud_conventional.renderAll();
        }
        if (type == DudType.NUKE) {
            RenderContext.translate(0, 0, 1.25);
            RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_NUKE_TEX));
            ResourceManager.dud_nuke.renderAll();
        }
        if (type == DudType.SALTED) {
            RenderContext.translate(0, 0, 0.5);
            RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_SALTED_TEX));
            ResourceManager.dud_salted.renderAll();
        }

        RenderContext.end();
    }

    @Override public boolean shouldRenderOffScreen(CrashedBombBlockEntity be) { return true; }

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
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0, 3, 0);
                RenderContext.scale(2.125F, 2.125F, 2.125F);
                RenderContext.mulPose(Axis.ZP.rotationDegrees(90F));
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                if (stack.is(ModBlocks.CRASHED_BOMB_BALEFIRE.asItem())) {
                    RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_BALEFIRE_TEX));
                    ResourceManager.dud_balefire.renderAll();
                }
                if (stack.is(ModBlocks.CRASHED_BOMB_CONVENTIONAL.asItem())) {
                    RenderContext.translate(0F, 0F, -0.5F);
                    RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_CONVENTIONAL_TEX));
                    ResourceManager.dud_conventional.renderAll();
                }
                if (stack.is(ModBlocks.CRASHED_BOMB_NUKE.asItem())) {
                    RenderContext.translate(0F, 0F, 1.25F);
                    RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_NUKE_TEX));
                    ResourceManager.dud_nuke.renderAll();
                }
                if (stack.is(ModBlocks.CRASHED_BOMB_SALTED.asItem())) {
                    RenderContext.translate(0F, 0F, 0.5F);
                    RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DUD_SALTED_TEX));
                    ResourceManager.dud_salted.renderAll();
                }
            }
        };
    }
}
