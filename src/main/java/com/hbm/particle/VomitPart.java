package com.hbm.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class VomitPart extends TextureSheetParticle {

    public VomitPart(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, SpriteSet spriteSet) {
        super(level, x, y, z, vx, vy, vz);
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.lifetime = 150 + level.random.nextInt(50);
        this.gravity = 0.4F;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        super.tick();
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
            return new VomitPart(level, x, y, z, vx, vy, vz, 0.72F, 0.12F, 0F, this.sprite);
        }
    }
}