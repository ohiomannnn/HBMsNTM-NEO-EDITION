package com.hbm.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class ParticleCoolingTower extends TextureSheetParticle {

    private float baseScale = 1.0F;
    private float maxScale = 1.0F;
    private float lift = 0.3F;
    private float strafe = 0.075F;
    private boolean windDir = true;
    private float alphaMod = 0.25F;

    public ParticleCoolingTower(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        this.rCol = this.gCol = this.bCol = 0.9F + level.random.nextFloat() * 0.05F;
        this.hasPhysics = false;
        this.setSpriteFromAge(sprites);
    }

    public void setBaseScale(float f) { this.baseScale = f; }
    public void setMaxScale(float f) { this.maxScale = f; }
    public void setLift(float f) { this.lift = f; }
    public void setLife(int i) { this.lifetime = i; }
    public void setStrafe(float f) { this.strafe = f; }
    public void noWind() { this.windDir = false; }
    public void alphaMod(float mod) { this.alphaMod = mod; }

    @Override
    public void tick() {

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        float ageScale = (float) this.age / (float) this.lifetime;

        this.alpha = alphaMod - ageScale * alphaMod;
        this.quadSize = baseScale + (float)Math.pow((maxScale * ageScale - baseScale), 2);

        if (lift > 0 && this.yd < this.lift) {
            this.yd += 0.01F;
        }
        if (lift < 0 && this.yd > this.lift) {
            this.yd -= 0.01F;
        }

        this.xd += this.random.nextGaussian() * strafe * ageScale;
        this.zd += this.random.nextGaussian() * strafe * ageScale;

        if (windDir) {
            this.xd += 0.02 * ageScale;
            this.zd -= 0.01 * ageScale;
        }

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        this.move(this.xd, this.yd, this.zd);

        this.xd *= 0.925;
        this.yd *= 0.925;
        this.zd *= 0.925;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new ParticleCoolingTower(level, x, y, z, sprites);
        }
    }
}
