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
import org.joml.Vector3f;

import java.util.Random;

public class ParticleMukeFlash extends TextureSheetParticle {

    private final boolean bf;
    private final Random rand = new Random();

    public ParticleMukeFlash(ClientLevel level, double x, double y, double z, boolean bf) {
        super(level, x, y, z);
        this.bf = bf;
        this.lifetime = 20;
        this.setSpriteFromAge(ModParticles.MUKE_FLASH_SPRITES);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age == 15) {
            ParticleEngine engine = Minecraft.getInstance().particleEngine;
            // Stem
            for (double d = 0.0D; d <= 1.8D; d += 0.1) {
                engine.add(getCloud(level, x, y, z, rand.nextGaussian() * 0.05, d + rand.nextGaussian() * 0.02, rand.nextGaussian() * 0.05));
            }

            // Ground
            for (int i = 0; i < 100; i++) {
                engine.add(getCloud(level, x, y + 0.5, z, rand.nextGaussian() * 0.5, rand.nextInt(5) == 0 ? 0.02 : 0, rand.nextGaussian() * 0.5));
            }

            // Mush
            for (int i = 0; i < 75; i++) {
                double dx = rand.nextGaussian() * 0.5;
                double dz = rand.nextGaussian() * 0.5;

                if (dx * dx + dz * dz > 1.5) {
                    dx *= 0.5;
                    dz *= 0.5;
                }

                double dy = 1.8 + (rand.nextDouble() * 3 - 1.5) * (0.75 - (dx * dx + dz * dz)) * 0.5;

                engine.add(getCloud(level, x, y, z, dx, dy + rand.nextGaussian() * 0.02, dz));
            }
        }
    }

    private ParticleMukeCloud getCloud(ClientLevel level, double x, double y, double z, double mx, double my, double mz) {

        if (this.bf) {
            return new ParticleMukeCloud(level, x, y, z, mx, my, mz, true);
        } else {
            return new ParticleMukeCloud(level, x, y, z, mx, my, mz, false);
        }
    }

    @Override
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {

        Vec3 cameraPosition = camera.getPosition();

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer consumer = bufferSource.getBuffer(CustomRenderTypes.entityAdditive(ModParticles.FLARE));

        float dX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float dY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
        float dZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

        this.alpha = Math.clamp(1 - ((this.age + partialTicks) / (float)this.lifetime), 0.0F, 1.0F);
        this.quadSize = (this.age + partialTicks) * 3F + 1F;

        float u0 = 0, v0 = 0;
        float u1 = 1, v1 = 1;

        Vector3f l = new Vector3f(camera.getLeftVector()).mul(this.quadSize);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(this.quadSize);

        for (int i = 0; i < 24; i++) {

            rand.setSeed(i * 31 + 1);

            float pX = (float) (dX + rand.nextDouble() * 15 - 7.5);
            float pY = (float) (dY + rand.nextDouble() * 7.5 - 3.75);
            float pZ = (float) (dZ + rand.nextDouble() * 15 - 7.5);

            consumer.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z)
                    .setColor(1.0F, 0.9F, 0.75F, alpha * 0.5F)
                    .setUv(u1, v1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setNormal(0.0F, 1.0F, 0.0F)
                    .setLight(240);
            consumer.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z)
                    .setColor(1.0F, 0.9F, 0.75F, alpha * 0.5F)
                    .setUv(u1, v0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setNormal(0.0F, 1.0F, 0.0F)
                    .setLight(240);
            consumer.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z)
                    .setColor(1.0F, 0.9F, 0.75F, alpha * 0.5F)
                    .setUv(u0, v0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setNormal(0.0F, 1.0F, 0.0F)
                    .setLight(240);
            consumer.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z)
                    .setColor(1.0F, 0.9F, 0.75F, alpha * 0.5F)
                    .setUv(u0, v1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setNormal(0.0F, 1.0F, 0.0F)
                    .setLight(240);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomRenderType.NONE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Provider(SpriteSet sprites) {
            ModParticles.MUKE_FLASH_SPRITES = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            return new ParticleMukeFlash(level, x, y, z, false);
        }
    }
}
