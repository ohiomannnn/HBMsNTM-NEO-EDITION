package com.hbm.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class ParticleMukeCloud extends TextureSheetParticle {

    private final SpriteSet sprites;
    private final float friction;

    public ParticleMukeCloud(ClientLevel level, double x, double y, double z, double mx, double my, double mz, SpriteSet sprites) {
        super(level, x, y, z);
        this.sprites = sprites;
        this.xd = mx;
        this.yd = my;
        this.zd = mz;

        if (yd > 0) {
            this.friction = 0.9F;
            if (yd > 0.1) {
                this.lifetime = 92 + random.nextInt(11) + (int)(yd * 20);
            } else {
                this.lifetime = 72 + random.nextInt(11);
            }
        } else if (yd == 0) {
            this.friction = 0.95F;
            this.lifetime = 52 + random.nextInt(11);
        } else {
            this.friction = 0.85F;
            this.lifetime = 122 + random.nextInt(31);
            this.age = 80;
        }

        this.setSpriteFromAge(sprites);
        this.quadSize = 3.0F;
    }

    @Override
    public void tick() {

        this.hasPhysics = this.age >= 2;

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) this.remove();

        this.yd -= 0.04D * this.gravity;
        this.move(this.xd, this.yd, this.zd);

        this.xd *= this.friction;
        this.yd *= this.friction;
        this.zd *= this.friction;

        if (this.onGround) {
            this.xd *= 0.7D;
            this.zd *= 0.7D;
        }

        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 0xF000F0;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleMukeCloud(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
