package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class ParticleCoolingTower extends TextureSheetParticle {

    private final float baseScale;
    private final float maxScale;
    private final float lift;
    private final float strafe;
    private final boolean windDir;
    private final float alphaMod;
    private final SpriteSet sprites;

    public ParticleCoolingTower(ClientLevel level, double x, double y, double z, float baseScale, float maxScale, float lift, float strafe, boolean windDir, float alphaMod, int lifetime, SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);
        this.baseScale = baseScale;
        this.maxScale = maxScale;
        this.lift = lift;
        this.strafe = strafe;
        this.windDir = windDir;
        this.alphaMod = alphaMod;
        this.lifetime = lifetime;
        this.sprites = sprites;

        float gray = 0.9F + level.random.nextFloat() * 0.05F;
        this.rCol = gray;
        this.gCol = gray;
        this.bCol = gray;

        this.quadSize = baseScale;
        this.setSpriteFromAge(sprites);
    }

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

        this.move(this.xd, this.yd, this.zd);

        this.xd *= 0.925;
        this.yd *= 0.925;
        this.zd *= 0.925;

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(sprites);
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        super.render(buffer, camera, partialTicks);
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
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new ParticleCoolingTower(level, x, y, z,
                    1.0F,    // baseScale
                    1.0F,    // maxScale
                    0.3F,    // lift
                    0.075F,  // strafe
                    true,    // windDir
                    0.25F,   // alphaMod
                    40,      // lifetime
                    sprites);
        }
    }
}
