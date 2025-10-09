package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;
import org.joml.Vector3f;

import java.util.Random;

public class ParticleMukeFlash extends TextureSheetParticle {

    private final boolean bf;
    private final Random rand = new Random();

    public ParticleMukeFlash(ClientLevel level, double x, double y, double z, boolean bf, SpriteSet sprites) {
        super(level, x, y, z);
        this.bf = bf;
        this.lifetime = 20;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        if (this.age == 15 && !bf) {
            spawnClouds();
        } else if (this.age == 15) {
            spawnCloudsBF();
        }
    }

    private void spawnClouds() {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        // Stem
        for (double d = 0.0D; d <= 1.8D; d += 0.1) {
            ParticleMukeCloud fx = new ParticleMukeCloud(level, x, y, z, rand.nextGaussian() * 0.05, d + rand.nextGaussian() * 0.02, rand.nextGaussian() * 0.05,  ModParticles.MUKE_CLOUD_SPRITES);
            engine.add(fx);
        }

        // Ground
        for (int i = 0; i < 100; i++) {
            ParticleMukeCloud fx = new ParticleMukeCloud(level, x, y + 0.5, z, rand.nextGaussian() * 0.5, rand.nextInt(5) == 0 ? 0.02 : 0, rand.nextGaussian() * 0.5,  ModParticles.MUKE_CLOUD_SPRITES);
            engine.add(fx);
        }

        // Mush
        for (int i = 0; i < 75; i++) {
            double dx = rand.nextGaussian() * 0.5;
            double dz = rand.nextGaussian() * 0.5;

            if (dx * dx + dz * dz > 1.5) {
                dx *= 0.5;
                dz *= 0.5;
            }

            double dy = 1.8 + (rand.nextDouble() * 3 - 1.5) * (0.75 - (dx * dx + dz * dz)) * 0.5;

            ParticleMukeCloud fx = new ParticleMukeCloud(level, x, y, z, dx, dy + rand.nextGaussian() * 0.02, dz,  ModParticles.MUKE_CLOUD_SPRITES);
            engine.add(fx);
        }
    }
    private void spawnCloudsBF() {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        // Stem
        for (double d = 0.0D; d <= 1.8D; d += 0.1) {
            ParticleMukeCloud fx = new ParticleMukeCloud(level, x, y, z, rand.nextGaussian() * 0.05, d + rand.nextGaussian() * 0.02, rand.nextGaussian() * 0.05,  ModParticles.MUKE_CLOUD_BF_SPRITES);
            engine.add(fx);
        }

        // Ground
        for (int i = 0; i < 100; i++) {
            ParticleMukeCloud fx = new ParticleMukeCloud(level, x, y + 0.5, z, rand.nextGaussian() * 0.5, rand.nextInt(5) == 0 ? 0.02 : 0, rand.nextGaussian() * 0.5,  ModParticles.MUKE_CLOUD_BF_SPRITES);
            engine.add(fx);
        }

        // Mush
        for (int i = 0; i < 75; i++) {
            double dx = rand.nextGaussian() * 0.5;
            double dz = rand.nextGaussian() * 0.5;

            if (dx * dx + dz * dz > 1.5) {
                dx *= 0.5;
                dz *= 0.5;
            }

            double dy = 1.8 + (rand.nextDouble() * 3 - 1.5) * (0.75 - (dx * dx + dz * dz)) * 0.5;

            ParticleMukeCloud fx = new ParticleMukeCloud(level, x, y, z, dx, dy + rand.nextGaussian() * 0.02, dz,  ModParticles.MUKE_CLOUD_BF_SPRITES);
            engine.add(fx);
        }
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        float px = (float)(xo + (this.x - xo) * partialTicks - camera.getPosition().x());
        float py = (float)(yo + (this.y - yo) * partialTicks - camera.getPosition().y());
        float pz = (float)(zo + (this.z - zo) * partialTicks - camera.getPosition().z());

        float scale = (this.age + partialTicks) * 3.0F + 1.0F;
        float alpha = 1.0F - ((this.age + partialTicks) / (float)this.lifetime);
        if (alpha < 0) alpha = 0;
        if (alpha > 1) alpha = 1;

        int color = FastColor.ARGB32.color((int)(alpha * 255), 255, 230, 191);

        float u0 = this.getU0();
        float v0 = this.getV0();
        float u1 = this.getU1();
        float v1 = this.getV1();
        int light = 0xF000F0;
        int overlay = 0;

        Vector3f left = camera.getLeftVector();
        Vector3f up = camera.getUpVector();

        float leftX = left.x() * scale;
        float leftY = left.y() * scale;
        float leftZ = left.z() * scale;
        float upX = up.x() * scale;
        float upY = up.y() * scale;
        float upZ = up.z() * scale;

        for (int i = 0; i < 24; i++) {

            float x0 = px - leftX - upX;
            float y0 = py - leftY - upY;
            float z0 = pz - leftZ - upZ;

            float x1 = px - leftX + upX;
            float y1 = py - leftY + upY;
            float z1 = pz - leftZ + upZ;

            float x2 = px + leftX + upX;
            float y2 = py + leftY + upY;
            float z2 = pz + leftZ + upZ;

            float x3 = px + leftX - upX;
            float y3 = py + leftY - upY;
            float z3 = pz + leftZ - upZ;

            vertexConsumer.addVertex(x0, y0, z0, color, u1, v1, overlay, light, 0, 1, 0);
            vertexConsumer.addVertex(x1, y1, z1, color, u1, v0, overlay, light, 0, 1, 0);
            vertexConsumer.addVertex(x2, y2, z2, color, u0, v0, overlay, light, 0, 1, 0);
            vertexConsumer.addVertex(x3, y3, z3, color, u0, v1, overlay, light, 0, 1, 0);
        }
    }
    @Override
    public ParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_ADDITIVE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            return new ParticleMukeFlash(level, x, y, z, false, sprites);
        }
    }
}
