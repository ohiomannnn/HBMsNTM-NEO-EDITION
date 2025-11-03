package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;

public class ParticleRocketFlame extends TextureSheetParticle {

    private int age;
    private int maxAge;

    public ParticleRocketFlame(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        this.maxAge = 300 + this.random.nextInt(50);
        this.quadSize = 1.0F;
        this.setSpriteFromAge(sprites);
    }

    public ParticleRocketFlame setScale(float scale) {
        this.quadSize = scale;
        return this;
    }

    public ParticleRocketFlame setMaxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public ParticleRocketFlame setNoClip() {
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
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        Random urandom = new Random(this.hashCode());

        Vector3f up = new Vector3f(camera.getUpVector());
        Vector3f left = new Vector3f(camera.getLeftVector());

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

            renderQuad(consumer, pX, pY, pZ, up, left, scale, 240);
        }
    }

    private void renderQuad(VertexConsumer consumer, float cx, float cy, float cz, Vector3f up, Vector3f left, float scale, int brightness) {

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        Vector3f l = new Vector3f(left).mul(scale);
        Vector3f u = new Vector3f(up).mul(scale);

        consumer.addVertex(cx - l.x - u.x, cy - l.y - u.y, cz - l.z - u.z)
                .setUv(u1, v1)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(brightness);
        consumer.addVertex(cx - l.x + u.x, cy - l.y + u.y, cz - l.z + u.z)
                .setUv(u1, v0)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(brightness);
        consumer.addVertex(cx + l.x + u.x, cy + l.y + u.y, cz + l.z + u.z)
                .setUv(u0, v0)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(brightness);
        consumer.addVertex(cx + l.x - u.x, cy + l.y - u.y, cz + l.z - u.z)
                .setUv(u0, v1)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(brightness);
    }

    public ParticleRocketFlame resetPrevPos() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        return this;
    }

    public ParticleRocketFlame setMotion(double mx, double my, double mz) {
        this.xd = mx;
        this.yd = my;
        this.zd = mz;
        return this;
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
            return new ParticleRocketFlame(level, x, y, z, this.sprites);
        }
    }
}
