package com.hbm.util.mixins;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.config.MainConfig;
import com.hbm.extprop.LivingProperties;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
@OnlyIn(Dist.CLIENT)
public class RenderNTMSkybox {

    @Unique private static final ResourceLocation DIGAMMA_STAR = HBMsNTM.withDefaultNamespaceNT("textures/misc/star_digamma.png");
    @Unique private static final ResourceLocation LODESTAR = HBMsNTM.withDefaultNamespaceNT("textures/misc/star_lode.png");
    @Unique private static final ResourceLocation BOBMAZON_SATELLITE = HBMsNTM.withDefaultNamespaceNT("textures/misc/bobmazon_sat.png");

    @Inject(method = "renderSky", at = @At("TAIL"))
    private void renderSky(Matrix4f frustumMatrix, Matrix4f projectionMatrix,
                           float partialTick, Camera camera,
                           boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {

        hbmsntm$renderBobmazonSat(frustumMatrix);
        hbmsntm$renderDigammaStar(frustumMatrix, partialTick);
        hbmsntm$renderLodeStar(frustumMatrix);
    }

    @Unique
    private void hbmsntm$renderDigammaStar(Matrix4f modelView, float partialTicks) {
        if (!MainConfig.CLIENT.SKY_RENDERER.get()) return;
        Minecraft mc = Minecraft.getInstance();

        Level level = mc.level;
        if (level == null) return;
        if (level.dimension() != Level.OVERWORLD) return;

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

        float brightness = (float) Math.sin(level.getTimeOfDay(partialTicks) * Math.PI);
        brightness *= brightness;
        RenderSystem.setShaderColor(brightness, brightness, brightness, 1.0F);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, DIGAMMA_STAR);

        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.mulPose(Axis.YN.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTicks) * 360.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(140.0F));
        poseStack.mulPose(Axis.ZN.rotationDegrees(40.0F));

        Matrix4f finalModelView = new Matrix4f(modelView);
        finalModelView.mul(poseStack.last().pose());
        poseStack.popPose();

        float digamma = LivingProperties.getDigamma(mc.player);
        float size = 1F * (1 + digamma * 0.25F);
        float dist = 100F- digamma * 2.5F;

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buf.addVertex(finalModelView, -size, dist, -size).setUv(0, 0);
        buf.addVertex(finalModelView, size, dist, -size).setUv(0, 1);
        buf.addVertex(finalModelView, size, dist, size).setUv(1, 1);
        buf.addVertex(finalModelView, -size, dist, size).setUv(1, 0);
        BufferUploader.drawWithShader(buf.buildOrThrow());

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }

    @Unique
    private void hbmsntm$renderBobmazonSat(Matrix4f modelView) {
        if (!MainConfig.CLIENT.SKY_RENDERER.get()) return;
        Minecraft mc = Minecraft.getInstance();

        Level level = mc.level;
        if (level == null) return;
        if (level.dimension() != Level.OVERWORLD) return;

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BOBMAZON_SATELLITE);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(-40.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees((System.currentTimeMillis() % (360 * 1000)) / 1000F));
        poseStack.mulPose(Axis.XP.rotationDegrees((System.currentTimeMillis() % (360 * 100)) / 100F));

        Matrix4f finalModelView = new Matrix4f(modelView);
        finalModelView.mul(poseStack.last().pose());
        poseStack.popPose();

        float size = 0.5F;
        float dist = 100F;

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buf.addVertex(finalModelView, -size, dist, -size).setUv(0, 0);
        buf.addVertex(finalModelView, size, dist, -size).setUv(0, 1);
        buf.addVertex(finalModelView, size, dist, size).setUv(1, 1);
        buf.addVertex(finalModelView, -size, dist, size).setUv(1, 0);
        BufferUploader.drawWithShader(buf.buildOrThrow());

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }

    @Unique
    private void hbmsntm$renderLodeStar(Matrix4f modelView) {
        if (!MainConfig.CLIENT.SKY_RENDERER.get()) return;
        Minecraft mc = Minecraft.getInstance();

        Level level = mc.level;
        if (level == null) return;
        if (level.dimension() != Level.OVERWORLD) return;
        if (!HBMsNTMClient.renderLodeStar) return;

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, LODESTAR);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.mulPose(Axis.XN.rotationDegrees(75.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(10.0F));

        Matrix4f finalModelView = new Matrix4f(modelView);
        finalModelView.mul(poseStack.last().pose());
        poseStack.popPose();

        float size = 0.5F + level.random.nextFloat() * 0.25F;
        float dist = 100F;

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buf.addVertex(finalModelView, -size, dist, -size).setUv(0, 0);
        buf.addVertex(finalModelView, size, dist, -size).setUv(0, 1);
        buf.addVertex(finalModelView, size, dist, size).setUv(1, 1);
        buf.addVertex(finalModelView, -size, dist, size).setUv(1, 0);
        BufferUploader.drawWithShader(buf.buildOrThrow());

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }
}
