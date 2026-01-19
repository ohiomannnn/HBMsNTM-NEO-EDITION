package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class RocketFlameParticle extends TextureSheetParticle {

    private int age;
    private int maxAge;

    public RocketFlameParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.maxAge = 300 + this.random.nextInt(50);
        this.quadSize = 1.0F;
        this.setSpriteFromAge(ModParticles.BASE_PARTICLE_SPRITES);
    }

    public RocketFlameParticle setScale(float scale) {
        this.quadSize = scale;
        return this;
    }

    public RocketFlameParticle setMaxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public RocketFlameParticle setNoClip() {
        this.hasPhysics = false;
        return this;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.age++;
        if (this.age >= this.maxAge) {
            this.remove();
        }

        this.xd *= 0.91D;
        this.yd *= 0.91D;
        this.zd *= 0.91D;

        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        RandomSource urandom = RandomSource.create(this.hashCode());

        for (int i = 0; i < 10; i++) {
            float add = urandom.nextFloat() * 0.3F;
            float dark = 1 - Math.min(((float) (age) / (maxAge * 0.25F)), 1);

            this.rCol = Mth.clamp(1 * dark + add, 0.0F, 1.0F);
            this.gCol = Mth.clamp(0.6F * dark + add, 0.0F, 1.0F);
            this.bCol = Mth.clamp(0 + add, 0.0F, 1.0F);

            this.alpha = (float) (Math.pow(1 - Math.min(((float) (age) / (float) (maxAge)), 1), 0.5)) * 0.75F;

            float spread = (float) Math.pow(((float) (age) / (float) maxAge) * 4F, 1.5) + 1F;
            spread *= this.quadSize;

            float scale = (urandom.nextFloat() * 0.5F + 0.1F + ((float) (age) / (float) maxAge) * 2F) * quadSize;

            float pX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x() + (urandom.nextGaussian() - 1D) * 0.2F * spread);
            float pY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y() + (urandom.nextGaussian() - 1D) * 0.2F * spread);
            float pZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z() + (urandom.nextGaussian() - 1D) * 0.2F * spread);

            BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            this.renderQuad(buffer, pX, pY, pZ, camera, scale, 240);
            buffer.endBatch();
        }
    }

    private void renderQuad(BufferSource buffer, float pX, float pY, float pZ, Camera camera, float scale, int brightness) {

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        Vector3f l = new Vector3f(camera.getLeftVector()).mul(scale);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(scale);

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(TextureAtlas.LOCATION_PARTICLES));

        consumer.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setUv(u1, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setUv(u1, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setUv(u0, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setUv(u0, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
    }

    public RocketFlameParticle resetPrevPos() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        return this;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomRenderType.NONE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new RocketFlameParticle(level, x, y, z);
        }
    }
}
