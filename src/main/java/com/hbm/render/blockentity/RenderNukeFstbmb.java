package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeBalefireBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.hbm.render.util.RenderMiscEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Matrix4f;

public class RenderNukeFstbmb extends BlockEntityRendererNT<NukeBalefireBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<NukeBalefireBlockEntity> create(Context context) { return new RenderNukeFstbmb(); }

    @Override
    public void render(NukeBalefireBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 180F;
            case SOUTH -> 270F;
            case EAST -> 0F;
            case NORTH -> 90F;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        RenderContext.setup(NtmRenderTypes.FVBO_NC.apply(ResourceManager.NUKE_FSTBMB_TEX), poseStack, packedLight, packedOverlay);
        ResourceManager.nuke_fstbmb.renderPart("Body");
        ResourceManager.nuke_fstbmb.renderPart("Balefire");

        if (be.hasEgg()) {
            RenderContext.setRenderType(NtmRenderTypes.FVBO_ADDITIVE_NL.apply(RenderMiscEffects.GLINT_BF));
            RenderMiscEffects.renderClassicGlint(partialTicks, ResourceManager.nuke_fstbmb, "Balefire", 0.0F, 0.8F, 0.15F, 5, 2F);
        }

        if (be.hasBattery()) {
            Font font = Minecraft.getInstance().font;
            float f3 = 0.04F;
            poseStack.translate(0.815F, 0.9275F, 0.5F);
            poseStack.scale(f3, -f3, f3);
            poseStack.mulPose(Axis.YP.rotationDegrees(90F));
            poseStack.translate(0F, 1F, 0F);
            Matrix4f matrix = poseStack.last().pose();
            font.drawInBatch(be.getMinutes() + ":" + be.getSeconds(), 0, 0, 0xff0000, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, packedLight);
        }

        RenderContext.end();
        poseStack.popPose();
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.NUKE_FSTBMB.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.scale(2.25F, 2.25F, 2.25F);
            }

            @Override
            public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(1F, 0F, 0F);
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
                RenderType type = NtmRenderTypes.FVBO_NC.apply(ResourceManager.NUKE_FSTBMB_TEX);

                RenderContext.setup(type, poseStack, packedLight, packedOverlay);
                ResourceManager.nuke_fstbmb.renderPart("Body");
                ResourceManager.nuke_fstbmb.renderPart("Balefire");
                RenderContext.end();
            }
        };
    }
}
