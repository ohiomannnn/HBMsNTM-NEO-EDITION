package com.hbm.particle;

import com.hbm.render.CustomRenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {
        Vec3 camPos = camera.getPosition();

        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - camPos.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - camPos.y);
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - camPos.z);

        this.alpha = Math.clamp(1 - ((this.age + partialTicks) / (float)this.lifetime), 0.0F, 1.0F);
        this.quadSize = (1 - (float)Math.pow(Math.E, (this.age + partialTicks) * -0.125)) * waveScale;

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer consumer = bufferSource.getBuffer(CustomRenderTypes.entityAdditive(ModParticles.SHOCKWAVE));

        float u0 = 0, v0 = 0;
        float u1 = 1, v1 = 1;

        consumer.addVertex(pX - 1 * this.quadSize, pY - 0.25F, pZ - 1 * this.quadSize)
                .setUv(u1, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX - 1 * this.quadSize, pY - 0.25F, pZ + 1 * this.quadSize)
                .setUv(u1, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX + 1 * this.quadSize, pY - 0.25F, pZ + 1 * this.quadSize)
                .setUv(u0, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX + 1 * this.quadSize, pY - 0.25F, pZ - 1 * this.quadSize)
                .setUv(u0, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomRenderType.NONE;
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
