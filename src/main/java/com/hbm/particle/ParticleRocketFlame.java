package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

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
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        Random urandom = new Random(this.hashCode());
        Quaternionf q = camera.rotation();
        Vec3 camPos = camera.getPosition();

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

            double baseX = Mth.lerp(partialTicks, this.xo, this.x);
            double baseY = Mth.lerp(partialTicks, this.yo, this.y);
            double baseZ = Mth.lerp(partialTicks, this.zo, this.z);

            float pX = (float)(baseX - camPos.x + (urandom.nextGaussian() - 1D) * 0.2F * spread);
            float pY = (float)(baseY - camPos.y + (urandom.nextGaussian() - 1D) * 0.5F * spread);
            float pZ = (float)(baseZ - camPos.z + (urandom.nextGaussian() - 1D) * 0.2F * spread);

            float oldSize = this.quadSize;
            this.quadSize = scale;

            this.renderRotatedQuad(buffer, q, pX, pY, pZ, partialTicks);

            this.quadSize = oldSize;
        }
    }

    public ParticleRocketFlame resetPrevPos() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        return this;
    }

    public ParticleRocketFlame setGravity(float gravity) {
        this.gravity = gravity;
        return this;
    }

    public ParticleRocketFlame maxAge(int gravity) {
        this.lifetime = gravity;
        return this;
    }

    public ParticleRocketFlame setPhysics(boolean physics) {
        this.hasPhysics = physics;
        return this;
    }

    public ParticleRocketFlame setSize(float size) {
        this.quadSize = size;
        return this;
    }

    public ParticleRocketFlame setMotion(double mx, double my, double mz) {
        this.xd = mx;
        this.yd = my;
        this.zd = mz;
        return this;
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
