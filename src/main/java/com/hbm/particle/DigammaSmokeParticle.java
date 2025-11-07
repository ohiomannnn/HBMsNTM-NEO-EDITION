package com.hbm.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class DigammaSmokeParticle extends TextureSheetParticle {

    public DigammaSmokeParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setSpriteFromAge(ModParticles.BASE_PARTICLE_SPRITES);
        this.lifetime = 100 + random.nextInt(40);
        this.hasPhysics = false;

        this.quadSize = 5;

        this.rCol = 0.5F + random.nextFloat() * 0.2F;
        this.gCol = 0.0F;
        this.bCol = 0.0F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.alpha = 1 - ((float) age / (float) lifetime);

        ++this.age;

        if (this.age == this.lifetime) {
            this.remove();
        }

        this.xd *= 0.99D;
        this.yd *= 0.99D;
        this.zd *= 0.99D;

        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new DigammaSmokeParticle(level, x, y, z);
        }
    }
}
