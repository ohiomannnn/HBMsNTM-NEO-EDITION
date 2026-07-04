package com.hbm.render.entity.projectile;

import com.hbm.entity.projectile.Tom;
import com.hbm.main.ResourceManager;
import com.hbm.render.util.RenderContext;
import com.hbm.util.Clock;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4f;

public class RenderTom extends EntityRenderer<Tom> {

    public RenderTom(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Tom tom, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        RenderContext.setup(poseStack, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

        renderTom();

        RenderContext.end();
    }

    public static void renderTom() {
        RenderContext.pushPose();

        RenderSystem.disableCull();
        RenderContext.setLightning(false);
        RenderContext.scale(100F, 100F, 100F);

        RenderSystem.setShaderTexture(0, ResourceManager.TOM_MAIN_TEX);
        ResourceManager.tom_main.renderAll();

        RenderSystem.setTextureMatrix(new Matrix4f().translate(0F, (float)(((double) Clock.get_ms() % 50000.0) / 2500.0), 0F));

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);

        float rot = -System.currentTimeMillis() / 10 % 360;
        RenderContext.scale(0.8F, 5F, 0.8F);

        RandomSource random = RandomSource.create(0);

        for(int i = 0; i < 20; i++) {
            RenderSystem.setShaderTexture(0, ResourceManager.TOM_FLAME_TEX);

            int r = random.nextInt(90);

            RenderContext.mulPose(Axis.YP.rotationDegrees(rot + r));

            ResourceManager.tom_flame.renderAll();

            RenderContext.mulPose(Axis.YN.rotationDegrees(rot));

            RenderContext.scale(-1.015F, 0.9F, 1.015F);
        }

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

        RenderSystem.resetTextureMatrix();

        RenderContext.setLightning(true);
        RenderSystem.enableCull();

        RenderContext.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Tom tom) {
        return ResourceManager.EMPTY;
    }
}
