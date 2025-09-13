package com.hbm.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class ParticleDeadLeaf extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected ParticleDeadLeaf(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);
        this.sprites = sprites;
        float color = 1F - level.random.nextFloat() * 0.2F;
        this.rCol = this.gCol = this.bCol = color;
        this.quadSize = 0.1F;
        this.lifetime = 200 + level.random.nextInt(50);
        this.gravity = 0.2F;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.onGround) {
            this.xd += this.random.nextGaussian() * 0.002D;
            this.zd += this.random.nextGaussian() * 0.002D;

            if (this.yd < -0.025D) {
                this.yd = -0.025D;
            }
        }

        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleDeadLeaf(level, x, y, z, sprites);
        }
    }
}
