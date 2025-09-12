package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.Random;

public class ParticleMukeFlash extends TextureSheetParticle {

    private final boolean bf;
    private final Random rand = new Random();

    public ParticleMukeFlash(ClientLevel level, double x, double y, double z, boolean bf, SpriteSet sprites) {
        super(level, x, y, z);
        this.bf = bf;
        this.lifetime = 20;
        this.setSize(99.0F, 99.0F);
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
        // Stem
        for (double d = 0.0D; d <= 1.8D; d += 0.1) {
            level.addParticle(ModParticles.MUKE_CLOUD.get(), x, y, z, rand.nextGaussian() * 0.05, d + rand.nextGaussian() * 0.02, rand.nextGaussian() * 0.05);
        }

        // Ground
        for (int i = 0; i < 100; i++) {
            level.addParticle(ModParticles.MUKE_CLOUD.get(), x, y + 0.5, z, rand.nextGaussian() * 0.5, rand.nextInt(5) == 0 ? 0.02 : 0, rand.nextGaussian() * 0.5);
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

            level.addParticle(ModParticles.MUKE_CLOUD.get(), x, y, z, dx, dy + rand.nextGaussian() * 0.02, dz);
        }
    }
    private void spawnCloudsBF() {
        // Stem
        for (double d = 0.0D; d <= 1.8D; d += 0.1) {
            level.addParticle(ModParticles.MUKE_CLOUD_BF.get(), x, y, z, rand.nextGaussian() * 0.05, d + rand.nextGaussian() * 0.02, rand.nextGaussian() * 0.05);
        }

        // Ground
        for (int i = 0; i < 100; i++) {
            level.addParticle(ModParticles.MUKE_CLOUD_BF.get(), x, y + 0.5, z, rand.nextGaussian() * 0.5, rand.nextInt(5) == 0 ? 0.02 : 0, rand.nextGaussian() * 0.5);
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

            level.addParticle(ModParticles.MUKE_CLOUD_BF.get(), x, y, z, dx, dy + rand.nextGaussian() * 0.02, dz);
        }
    }


    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        super.render(buffer, camera, partialTicks);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomPartSheet.PARTICLE_SHEET_ADDITIVE;
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
