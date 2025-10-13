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
        super(level, x, y, z, 0, 0, 0);
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
        Quaternionf quaternionf = camera.rotation();
        Vec3 cameraPosition = camera.getPosition();
        Random urandom = new Random(this.hashCode());

        for (int i = 0; i < 10; i++) {
            float add = urandom.nextFloat() * 0.3F;
            float dark = 1 - Math.min(((float) (age) / (maxAge * 0.25F)), 1);

            this.rCol = Mth.clamp(1 * dark + add, 0.0F, 1.0F);
            this.gCol = Mth.clamp(0.6F * dark + add, 0.0F, 1.0F);
            this.bCol = Mth.clamp(0 + add, 0.0F, 1.0F);

            this.alpha = (float) Math.pow(1 - Math.min(((float) (age) / (float) (maxAge)), 1), 0.5);

            float spread = (float) Math.pow(((float) (age) / (float) maxAge) * 4F, 1.5) + 1F;
            spread *= this.quadSize;

            float scale = (urandom.nextFloat() * 0.5F + 0.1F + ((float) (age) / (float) maxAge) * 2F) * quadSize;

            float pX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x() + (urandom.nextGaussian() - 1D) * 0.2F * spread);
            float pY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y() + (urandom.nextGaussian() - 1D) * 0.2F * spread);
            float pZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z() + (urandom.nextGaussian() - 1D) * 0.2F * spread);

            float u0 = sprite.getU0();
            float u1 = sprite.getU1();
            float v0 = sprite.getV0();
            float v1 = sprite.getV1();

            renderVertex(consumer, quaternionf, pX, pY, pZ, 1.0F, -1.0F, scale, u1, v1, 240, this.rCol, this.gCol, this.bCol, alpha * 0.75F);
            renderVertex(consumer, quaternionf, pX, pY, pZ, 1.0F,  1.0F, scale, u1, v0, 240, this.rCol, this.gCol, this.bCol, alpha * 0.75F);
            renderVertex(consumer, quaternionf, pX, pY, pZ, -1.0F, 1.0F, scale, u0, v0, 240, this.rCol, this.gCol, this.bCol, alpha * 0.75F);
            renderVertex(consumer, quaternionf, pX, pY, pZ, -1.0F, -1.0F, scale, u0, v1, 240, this.rCol, this.gCol, this.bCol, alpha * 0.75F);
        }
    }

    private void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight, float r, float g, float b, float alpha) {
        Vector3f vec = new Vector3f(xOffset, yOffset, 0.0F).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.addVertex(vec.x(), vec.y(), vec.z()).setUv(u, v).setColor(r, g, b, alpha).setLight(packedLight);
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
