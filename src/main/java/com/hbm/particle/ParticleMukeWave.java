package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
public class ParticleMukeWave extends TextureSheetParticle {

    private final SpriteSet sprites;
    private final float waveScale;


    protected ParticleMukeWave(ClientLevel level, double x, double y, double z,
                               float waveScale, int maxAge, SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);
        this.sprites = sprites;
        this.waveScale = waveScale;
        this.lifetime = maxAge;
        this.alpha = 1.0F;
        this.quadSize = 1.0F;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        float t = (float) this.age / (float) this.lifetime;

        // Альфа уходит к нулю
        this.alpha = 1.0F - t;

        // Масштаб увеличивается как у волны
        this.quadSize = (1.0F - (float)Math.exp(-(this.age) * 0.125F)) * waveScale;

        this.setSpriteFromAge(this.sprites);
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
            return new ParticleMukeWave(level, x, y, z,
                    45F, 25,
                    sprites);
        }
    }
}
