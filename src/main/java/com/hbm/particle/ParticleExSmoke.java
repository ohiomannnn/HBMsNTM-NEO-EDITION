package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ParticleExSmoke extends TextureSheetParticle {

    private int age;
    public int maxAge;

    public ParticleExSmoke(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        maxAge = 100 + random.nextInt(40);
        this.setSpriteFromAge(ModParticles.BASE_PARTICLE_SPRITES);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.alpha = 1 - ((float) age / (float) maxAge);

        this.age++;

        if (this.age == this.maxAge) {
            this.remove();
        }

        this.xd *= 0.76D;
        this.yd *= 0.76D;
        this.zd *= 0.76D;

        this.move(this.xd, this.yd, this.zd);
    }

    public ParticleExSmoke setMotionX(double motionX) {
        this.xd = motionX;
        return this;
    }
    public ParticleExSmoke setMotionY(double motionY) {
        this.yd = motionY;
        return this;
    }

    public ParticleExSmoke setMotionZ(double motionZ) {
        this.zd = motionZ;
        return this;
    }

    // what
    public double getMotionY() {
        return this.yd;
    }

    @Override
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {
        Vec3 camPos = camera.getPosition();

        RandomSource urandom = RandomSource.create(this.hashCode());

        for (int i = 0; i < 6; i++) {
            this.rCol = this.gCol = this.bCol = urandom.nextFloat() * 0.25F + 0.25F;

            float pX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x() + (urandom.nextGaussian() - 1D) * 0.75F);
            float pY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y() + (urandom.nextGaussian() - 1D) * 0.75F);
            float pZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z() + (urandom.nextGaussian() - 1D) * 0.75F);

            this.quadSize = urandom.nextFloat() + 0.5F;

            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            this.renderQuad(buffer, pX, pY, pZ, camera, this.quadSize, this.getLightColor(partialTicks));
            buffer.endBatch();
        }
    }

    private void renderQuad(MultiBufferSource.BufferSource buffer, float pX, float pY, float pZ, Camera camera, float scale, int brightness) {

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

    @Override
    public ParticleRenderType getRenderType() {
        return CustomRenderType.NONE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            return new ParticleExSmoke(level, x, y, z);
        }
    }
}
