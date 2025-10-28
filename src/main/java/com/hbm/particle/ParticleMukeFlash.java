package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
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
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        float dX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float dY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
        float dZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

        this.alpha = Mth.clamp(1 - ((this.age + partialTicks) / (float)this.lifetime), 0.0F, 1.0F);
        this.alpha = this.alpha * 0.5F;
        float scale = (this.age + partialTicks) * 3F + 1F;

        this.rCol = 1.0F; this.gCol = 0.9F; this.bCol = 0.75F;

        Vector3f up = new Vector3f(camera.getUpVector());
        Vector3f left = new Vector3f(camera.getLeftVector());

        Random rand = new Random();

        for (int i = 0; i < 24; i++) {
            rand.setSeed(i * 31 + 1);

            float pX = (float) (dX + rand.nextDouble() * 15 - 7.5);
            float pY = (float) (dY + rand.nextDouble() * 7.5 - 3.75);
            float pZ = (float) (dZ + rand.nextDouble() * 15 - 7.5);

            addBillboardQuad(consumer, pX, pY, pZ, left, up, scale);
        }
    }

    private void addBillboardQuad(VertexConsumer consumer, float cx, float cy, float cz, Vector3f left, Vector3f up, float scale) {

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        Vector3f l = new Vector3f(left).mul(scale);
        Vector3f u = new Vector3f(up).mul(scale);

        consumer.addVertex(cx - l.x - u.x, cy - l.y - u.y, cz - l.z - u.z)
                .setUv(u1, v1)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(cx - l.x + u.x, cy - l.y + u.y, cz - l.z + u.z)
                .setUv(u1, v0)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(cx + l.x + u.x, cy + l.y + u.y, cz + l.z + u.z)
                .setUv(u0, v0)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(cx + l.x - u.x, cy + l.y - u.y, cz + l.z - u.z)
                .setUv(u0, v1)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
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
