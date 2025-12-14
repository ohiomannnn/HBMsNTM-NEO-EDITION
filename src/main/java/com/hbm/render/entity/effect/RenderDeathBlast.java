package com.hbm.render.entity.effect;

import com.hbm.entity.logic.DeathBlast;
import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RenderDeathBlast extends EntityRenderer<DeathBlast> {

    public RenderDeathBlast(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(DeathBlast blast, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Matrix4f matrix = poseStack.last().pose();
        Tesselator tess = Tesselator.getInstance();

        Vec3NT vector = new Vec3NT(0.5, 0, 0);

        for (int i = 0; i < 8; i++) {
            BufferBuilder buffer = tess.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            buffer.addVertex(matrix, (float) vector.xCoord, 250F, (float) vector.zCoord).setColor(1.0F, 0, 0, 1.0F);
            buffer.addVertex(matrix, (float) vector.xCoord, 0F, (float) vector.zCoord).setColor(1.0F, 0, 0, 1.0F);
            vector.rotateAroundYRad(45);
            buffer.addVertex(matrix, (float) vector.xCoord, 0F, (float) vector.zCoord).setColor(1.0F, 0, 0, 1.0F);
            buffer.addVertex(matrix, (float) vector.xCoord, 250F, (float) vector.zCoord).setColor(1.0F, 0, 0, 1.0F);
            BufferUploader.drawWithShader(buffer.buildOrThrow());
        }

        for (int i = 0; i < 8; i++) {
            BufferBuilder buffer = tess.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            buffer.addVertex(matrix, (float) vector.xCoord / 2, 250F, (float) vector.zCoord / 2).setColor(1.0F, 0, 1.0F, 1.0F);
            buffer.addVertex(matrix, (float) vector.xCoord / 2, 0F, (float) vector.zCoord / 2).setColor(1.0F, 0, 1.0F, 1.0F);
            vector.rotateAroundYRad(45);
            buffer.addVertex(matrix, (float) vector.xCoord / 2, 0F, (float) vector.zCoord / 2).setColor(1.0F, 0, 1.0F, 1.0F);
            buffer.addVertex(matrix, (float) vector.xCoord / 2, 250F, (float) vector.zCoord / 2).setColor(1.0F, 0, 1.0F, 1.0F);
            BufferUploader.drawWithShader(buffer.buildOrThrow());
        }

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);

        renderSphereWrapper(blast, poseStack, partialTicks);
    }

    private void renderSphereWrapper(DeathBlast blast, PoseStack poseStack, float partialTicks) {
        poseStack.pushPose();

        double maxAge = DeathBlast.MAX_AGE;
        double tickProgress = (blast.tickCount + partialTicks) / maxAge;
        double scale = 10.0 - 10.0 * tickProgress;
        float alpha = (float) tickProgress;

        if (scale < 0) scale = 0;

        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        poseStack.scale((float)scale, (float)scale, (float)scale);

        renderSphere(poseStack.last().pose(), 1.0f, 1.0f, 0.0f, 1.0f, alpha);

        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

        poseStack.scale(1.25f, 1.25f, 1.25f);

        for (int i = 0; i < 8; i++) {
            renderSphere(poseStack.last().pose(), 1.0f, 1.0f, 0.0f, 0.0f, alpha * 0.125f);
            poseStack.scale(1.05f, 1.05f, 1.05f);
        }

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        poseStack.popPose();
    }

    private static final int STACKS = 16;
    private static final int SLICES = 16;

    private void renderSphere(Matrix4f matrix4f, float radius, float r, float g, float b, float a) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        int colorR = (int)(r * 255);
        int colorG = (int)(g * 255);
        int colorB = (int)(b * 255);
        int colorA = (int)(a * 255);

        for (int i = 0; i < STACKS; i++) {
            double phi1 = Math.PI * i / STACKS;
            double phi2 = Math.PI * (i + 1) / STACKS;

            for (int j = 0; j < SLICES; j++) {
                double theta1 = 2.0 * Math.PI * j / SLICES;
                double theta2 = 2.0 * Math.PI * (j + 1) / SLICES;

                float x1 = (float)(radius * Math.sin(phi1) * Math.cos(theta1));
                float y1 = (float)(radius * Math.cos(phi1));
                float z1 = (float)(radius * Math.sin(phi1) * Math.sin(theta1));

                float x2 = (float)(radius * Math.sin(phi2) * Math.cos(theta1));
                float y2 = (float)(radius * Math.cos(phi2));
                float z2 = (float)(radius * Math.sin(phi2) * Math.sin(theta1));

                float x3 = (float)(radius * Math.sin(phi2) * Math.cos(theta2));
                float y3 = (float)(radius * Math.cos(phi2));
                float z3 = (float)(radius * Math.sin(phi2) * Math.sin(theta2));

                float x4 = (float)(radius * Math.sin(phi1) * Math.cos(theta2));
                float y4 = (float)(radius * Math.cos(phi1));
                float z4 = (float)(radius * Math.sin(phi1) * Math.sin(theta2));

                buffer.addVertex(matrix4f, x1, y1, z1).setColor(colorR, colorG, colorB, colorA);
                buffer.addVertex(matrix4f, x2, y2, z2).setColor(colorR, colorG, colorB, colorA);
                buffer.addVertex(matrix4f, x3, y3, z3).setColor(colorR, colorG, colorB, colorA);

                buffer.addVertex(matrix4f, x1, y1, z1).setColor(colorR, colorG, colorB, colorA);
                buffer.addVertex(matrix4f, x3, y3, z3).setColor(colorR, colorG, colorB, colorA);
                buffer.addVertex(matrix4f, x4, y4, z4).setColor(colorR, colorG, colorB, colorA);
            }
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    @Override
    public ResourceLocation getTextureLocation(DeathBlast deathBlast) {
        return null;
    }

    @Override
    public boolean shouldRender(DeathBlast livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }
}
