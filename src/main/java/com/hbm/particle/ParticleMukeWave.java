package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ParticleMukeWave extends TextureSheetParticle {

    private float waveScale = 45F;

    public ParticleMukeWave(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setSpriteFromAge(ModParticles.MUKE_WAVE_SPRITES);
        this.lifetime = 25;
    }

    public void setup(float scale, int maxAge) {
        this.waveScale = scale;
        this.lifetime = maxAge;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 camPos = camera.getPosition();

        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - camPos.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - camPos.y);
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - camPos.z);

        this.alpha = Math.clamp(1 - ((this.age + partialTicks) / (float)this.lifetime), 0.0F, 1.0F);
        this.quadSize = (1 - (float)Math.pow(Math.E, (this.age + partialTicks) * -0.125)) * waveScale;

        renderQuad(consumer, pX, pY, pZ, this.quadSize);
    }

    private void renderQuad(VertexConsumer consumer, float pX, float pY, float pZ, float scale) {

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        consumer.addVertex(pX - 1 * scale, pY - 0.25F, pZ - 1 * scale)
                .setUv(u1, v1)
                .setColor(1.0F, 1.0F, 1.0F, this.alpha)
                .setLight(240);
        consumer.addVertex(pX - 1 * scale, pY - 0.25F, pZ + 1 * scale)
                .setUv(u1, v0)
                .setColor(1.0F, 1.0F, 1.0F, this.alpha)
                .setLight(240);
        consumer.addVertex(pX + 1 * scale, pY - 0.25F, pZ + 1 * scale)
                .setUv(u0, v0)
                .setColor(1.0F, 1.0F, 1.0F, this.alpha)
                .setLight(240);
        consumer.addVertex(pX + 1 * scale, pY - 0.25F, pZ - 1 * scale)
                .setUv(u0, v1)
                .setColor(1.0F, 1.0F, 1.0F, this.alpha)
                .setLight(240);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomRenderType.PARTICLE_SHEET_ADDITIVE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Provider(SpriteSet sprites) {
            ModParticles.MUKE_WAVE_SPRITES = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new ParticleMukeWave(level, x, y, z);
        }
    }
}
