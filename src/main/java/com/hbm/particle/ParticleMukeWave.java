package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

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
        this.alpha = 1.0F - t;
        this.quadSize = (1.0F - (float) Math.exp(-(this.age) * 0.125F)) * waveScale;
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {

        float cx = (float) (Mth.lerp(partialTicks, this.xo, this.x) - camera.getPosition().x);
        float cy = (float) (Mth.lerp(partialTicks, this.yo, this.y) - camera.getPosition().y);
        float cz = (float) (Mth.lerp(partialTicks, this.zo, this.z) - camera.getPosition().z);

        float half = this.quadSize;
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();

        int lightU = 0xF0;
        int lightV = 0xF0;

        buffer.addVertex(cx - half, cy, cz - half)
                .setUv(u1, v1)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setUv2(lightU, lightV)
                .setNormal(0, 1, 0);

        buffer.addVertex(cx - half, cy, cz + half)
                .setUv(u1, v0)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setUv2(lightU, lightV)
                .setNormal(0, 1, 0);

        buffer.addVertex(cx + half, cy, cz + half)
                .setUv(u0, v0)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setUv2(lightU, lightV)
                .setNormal(0, 1, 0);

        buffer.addVertex(cx + half, cy, cz - half)
                .setUv(u0, v1)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setUv2(lightU, lightV)
                .setNormal(0, 1, 0);
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
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new ParticleMukeWave(level, x, y, z, 45F, 25, sprites);
        }
    }
}
