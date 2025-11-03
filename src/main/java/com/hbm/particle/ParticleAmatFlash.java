package com.hbm.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.Random;

public class ParticleAmatFlash extends TextureSheetParticle {

    public ParticleAmatFlash(ClientLevel level, double x, double y, double z, float scale) {
        super(level, x, y, z);
        this.lifetime = 10;
        this.quadSize = scale;
    }

    private static float[] rotX(float x, float y, float z, float deg) {
        double r = Math.toRadians(deg);
        double c = Math.cos(r), s = Math.sin(r);
        return new float[]{ x, (float)(y * c - z * s), (float)(y * s + z * c) };
    }

    private static float[] rotY(float x, float y, float z, float deg) {
        double r = Math.toRadians(deg);
        double c = Math.cos(r), s = Math.sin(r);
        return new float[]{ (float)(x * c + z * s), y, (float)(-x * s + z * c) };
    }

    private static float[] rotZ(float x, float y, float z, float deg) {
        double r = Math.toRadians(deg);
        double c = Math.cos(r), s = Math.sin(r);
        return new float[]{ (float)(x * c - y * s), (float)(x * s + y * c), z };
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        PoseStack poseStack = new PoseStack();

        double camX = camera.getPosition().x;
        double camY = camera.getPosition().y;
        double camZ = camera.getPosition().z;

        float px = (float) (this.xo + (this.x - this.xo) * partialTicks - camX);
        float py = (float) (this.yo + (this.y - this.yo) * partialTicks - camY);
        float pz = (float) (this.zo + (this.z - this.zo) * partialTicks - camZ);

        poseStack.pushPose();
        poseStack.translate(px, py, pz);
        float globalScale = 0.2F * this.quadSize;
        poseStack.scale(globalScale, globalScale, globalScale);

        float intensity = (this.age + partialTicks) / (float) this.lifetime;
        float inverse = 1.0F - intensity;

        PoseStack.Pose pose = poseStack.last();

        Random random = new Random(432L);
        float localScale = 0.5F;

        float startR = 0.4F;
        float startG = 0.5F;
        float startB = 1.0F;
        float endR = 1.0F;
        float endG = 1.0F;
        float endB = 1.0F;

        float r = startR + (endR - startR) * intensity;
        float g = startG + (endG - startG) * intensity;
        float b = startB + (endB - startB) * intensity;
        int centerAlpha = Math.max(0, Math.min(255, (int)(inverse * 255.0F)));

        for (int i = 0; i < 100; i++) {
            float vert1 = (random.nextFloat() * 20.0F + 5.0F + 1 * 10.0F) * (intensity * localScale);
            float vert2 = (random.nextFloat() * 2.0F + 1.0F + 1 * 2.0F) * (intensity * localScale);

            float x1 = -0.866f * vert2, y1 = vert1, z1 = -0.5f * vert2;
            float x2 = 0.866f * vert2,  y2 = vert1, z2 = -0.5f * vert2;
            float x3 = 0.0f,            y3 = vert1, z3 = 1.0f * vert2;

            float a1 = random.nextFloat() * 360.0F;
            float a2 = random.nextFloat() * 360.0F;
            float a3 = random.nextFloat() * 360.0F;
            float a4 = random.nextFloat() * 360.0F;
            float a5 = random.nextFloat() * 360.0F;

            float[] p1 = rotY(rotX(x1, y1, z1, a1)[0], rotX(x1, y1, z1, a1)[1], rotX(x1, y1, z1, a1)[2], a2);
            p1 = rotZ(p1[0], p1[1], p1[2], a3);
            p1 = rotX(p1[0], p1[1], p1[2], a4);
            p1 = rotY(p1[0], p1[1], p1[2], a5);

            float[] p2 = rotY(rotX(x2, y2, z2, a1)[0], rotX(x2, y2, z2, a1)[1], rotX(x2, y2, z2, a1)[2], a2);
            p2 = rotZ(p2[0], p2[1], p2[2], a3);
            p2 = rotX(p2[0], p2[1], p2[2], a4);
            p2 = rotY(p2[0], p2[1], p2[2], a5);

            float[] p3 = rotY(rotX(x3, y3, z3, a1)[0], rotX(x3, y3, z3, a1)[1], rotX(x3, y3, z3, a1)[2], a2);
            p3 = rotZ(p3[0], p3[1], p3[2], a3);
            p3 = rotX(p3[0], p3[1], p3[2], a4);
            p3 = rotY(p3[0], p3[1], p3[2], a5);

            consumer.addVertex(pose.pose(), 0.0F, 0.0F, 0.0F)
                    .setColor(r, g, b, centerAlpha / 255.0F)
                    .setNormal(pose, 0.0F, 1.0F, 0.0F);

            consumer.addVertex(pose.pose(), p1[0], p1[1], p1[2])
                    .setColor(r, g, b, 0.0F)
                    .setNormal(pose, 0.0F, 1.0F, 0.0F);

            consumer.addVertex(pose.pose(), p2[0], p2[1], p2[2])
                    .setColor(r, g, b, 0.0F)
                    .setNormal(pose, 0.0F, 1.0F, 0.0F);

            consumer.addVertex(pose.pose(), 0.0F, 0.0F, 0.0F)
                    .setColor(r, g, b, centerAlpha / 255.0F)
                    .setNormal(pose, 0.0F, 1.0F, 0.0F);

            consumer.addVertex(pose.pose(), p2[0], p2[1], p2[2])
                    .setColor(r, g, b, 0.0F)
                    .setNormal(pose, 0.0F, 1.0F, 0.0F);

            consumer.addVertex(pose.pose(), p3[0], p3[1], p3[2])
                    .setColor(r, g, b, 0.0F)
                    .setNormal(pose, 0.0F, 1.0F, 0.0F);

            consumer.addVertex(pose.pose(), 0.0F, 0.0F, 0.0F)
                    .setColor(r, g, b, centerAlpha / 255.0F)
                    .setNormal(pose, 0.0F, 1.0F, 0.0F);

            consumer.addVertex(pose.pose(), p3[0], p3[1], p3[2])
                    .setColor(r, g, b, 0.0F)
                    .setNormal(pose, 0.0F, 1.0F, 0.0F);

            consumer.addVertex(pose.pose(), p1[0], p1[1], p1[2])
                    .setColor(r, g, b, 0.0F)
                    .setNormal(pose, 0.0F, 1.0F, 0.0F);
        }

        poseStack.popPose();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return IParticleRenderType.AMAT_FLASH;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double dx, double dy, double dz) {
            return new ParticleAmatFlash(world, x, y, z, 1.0F);
        }
    }
}
