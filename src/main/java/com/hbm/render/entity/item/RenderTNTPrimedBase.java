package com.hbm.render.entity.item;

import com.hbm.entity.item.TNTPrimedBase;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class RenderTNTPrimedBase extends EntityRenderer<TNTPrimedBase> {

    private final BlockRenderDispatcher blockRenderer;

    public RenderTNTPrimedBase(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void render(TNTPrimedBase tnt, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        BlockState blockstate = tnt.getBlock().defaultBlockState();
        int fuse = tnt.fuse;
        float f = (float) fuse - partialTicks + 1.0F;

        poseStack.pushPose();
        poseStack.translate(0.0F, 0.5F, 0.0F);
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-90.0F));

        // Scale animation before explosion
        if (f < 10.0F) {
            float scale = 1.0F - f / 10.0F;
            scale = Mth.clamp(scale, 0.0F, 1.0F);
            scale = scale * scale * scale * scale;
            float finalScale = 1.0F + scale * 0.3F;
            poseStack.scale(finalScale, finalScale, finalScale);
        }

        poseStack.translate(-0.5F, -0.5F, -0.5F);

        // Normal render
        blockRenderer.renderSingleBlock(blockstate, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);

        // Flash effect (like blinking TNT)
        if (tnt.fuse / 5 % 2 == 0) {
            float alpha = (1.0F - f / 100.0F) * 0.8F;
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

            blockRenderer.renderSingleBlock(blockstate, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        }

        poseStack.popPose();
        super.render(tnt, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(TNTPrimedBase entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
