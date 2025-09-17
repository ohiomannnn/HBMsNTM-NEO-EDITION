package com.hbm.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

import java.awt.Color;

public class ParticleGasFlame extends SmokeParticle {

    private final float colorMod;

    public ParticleGasFlame(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float scale, SpriteSet sprites) {
        super(level, x, y, z, vx, vy * 1.5, vz, scale, sprites);
        this.colorMod = 0.8F + RandomSource.create().nextFloat() * 0.2F;
        this.hasPhysics = false;
        this.lifetime = 30 + RandomSource.create().nextInt(13);
        this.setSize(scale, scale);
        updateColor();
    }

    @Override
    public void tick() {
        double prevY = this.yd;
        super.tick();
        updateColor();
        this.yd = prevY;

        this.xd *= 0.75;
        this.yd += 0.005;
        this.zd *= 0.75;
    }

    private void updateColor() {
        float time = (float) this.age / (float) this.lifetime;
        Color color = Color.getHSBColor(Math.max((60 - time * 100) / 360F, 0.0F), 1 - time * 0.25F, 1 - time * 0.5F);

        this.rCol = (color.getRed() / 255F) * colorMod;
        this.gCol = (color.getGreen() / 255F) * colorMod;
        this.bCol = (color.getBlue() / 255F) * colorMod;
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            return new ParticleGasFlame(level, x, y, z, vx, vy, vz, 1.0F, sprites);
        }
    }
}
