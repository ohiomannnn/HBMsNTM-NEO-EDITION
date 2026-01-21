package com.hbm.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class AmatFlashParticle extends TextureSheetParticle {

    public AmatFlashParticle(ClientLevel level, double x, double y, double z, float size) {
        super(level, x, y, z);
        this.lifetime = 10;
        this.quadSize = size;
    }

    @Override
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {
        PoseStack poseStack = new PoseStack();

        Vec3 cameraPosition = camera.getPosition();
        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

        poseStack.pushPose();
        poseStack.translate(pX, pY, pZ);
        poseStack.scale(0.2F * quadSize, 0.2F * quadSize, 0.2F * quadSize);

        float intensity = (this.age + partialTicks) / this.lifetime;
        float inverse = Math.clamp(1.0F - intensity, 0F, 1F);

        Tesselator tess = Tesselator.getInstance();

        RandomSource random = RandomSource.create(432L);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        RenderSystem.depthMask(false);
        RenderSystem.enableCull();

        float scale = 0.5F;

        for (int i = 0; i < 100; i++) {
            poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360F));
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360F));
            poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360F));
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360F));

            float vert1 = (random.nextFloat() * 20.0F + 5.0F + 1 * 10.0F) * (intensity * scale);
            float vert2 = (random.nextFloat() * 2.0F + 1.0F + 1 * 2.0F) * (intensity * scale);

            Matrix4f matrix = poseStack.last().pose();

            BufferBuilder buffer = tess.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            buffer.addVertex(matrix, 0, 0, 0)
                    .setColor(1.0F, 1.0F, 1.0F, inverse);
            buffer.addVertex(matrix, -0.866F * vert2, vert1, -0.5F * vert2)
                    .setColor(1.0F, 1.0F, 1.0F, 0F);
            buffer.addVertex(matrix, 0.866F * vert2, vert1, -0.5F * vert2)
                    .setColor(1.0F, 1.0F, 1.0F, 0F);
            buffer.addVertex(matrix, 0.0F, vert1, 1.0F * vert2)
                    .setColor(1.0F, 1.0F, 1.0F, 0F);
            buffer.addVertex(matrix, -0.866F * vert2, vert1, -0.5F * vert2)
                    .setColor(1.0F, 1.0F, 1.0F, 0F);

            BufferUploader.drawWithShader(buffer.buildOrThrow());
        }

        RenderSystem.disableBlend();
        RenderSystem.disableCull();
        RenderSystem.depthMask(true);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ModParticleRenderTypes.NONE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double dx, double dy, double dz) {
            return new AmatFlashParticle(world, x, y, z, 1.0F);
        }
    }
}
