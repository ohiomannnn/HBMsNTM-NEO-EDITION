package com.hbm.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class ParticleAura extends TextureSheetParticle {

    public ParticleAura(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.lifetime = 40;
        this.quadSize = 0.2F;
        this.setSpriteFromAge(sprites);
    }


    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            return new ParticleAura(level, x, y, z, vx, vy, vz, 0F, 0.75F, 1F, this.sprite);
        }
    }
}