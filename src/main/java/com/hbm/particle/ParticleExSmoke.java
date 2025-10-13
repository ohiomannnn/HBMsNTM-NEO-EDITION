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

public class ParticleExSmoke extends TextureSheetParticle {

    private int age;
    public int maxAge;

    public ParticleExSmoke(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        maxAge = 100 + random.nextInt(40);
        this.setSpriteFromAge(ModParticles.ROCKET_FLAME_SPRITES);
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

        this.xd *= 0.7599999785423279D;
        this.yd *= 0.7599999785423279D;
        this.zd *= 0.7599999785423279D;

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
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Quaternionf quaternionf = new Quaternionf(camera.rotation());
        Vec3 camPos = camera.getPosition();

        Random urandom = new Random(this.hashCode());

        for (int i = 0; i < 6; i++) {
            this.rCol = this.gCol = this.bCol = urandom.nextFloat() * 0.25F + 0.25F;

            float pX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x() + (urandom.nextGaussian() - 1D) * 0.75F);
            float pY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y() + (urandom.nextGaussian() - 1D) * 0.75F);
            float pZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z() + (urandom.nextGaussian() - 1D) * 0.75F);

            float size = urandom.nextFloat() + 0.5F;
            float U0 = this.getU0();
            float U1 = this.getU1();
            float V0 = this.getV0();
            float V1 = this.getV1();
            int color = this.getLightColor(partialTicks);
            this.renderVertex(consumer, quaternionf, pX, pY, pZ, 1.0F, -1.0F, size, U1, V1, color);
            this.renderVertex(consumer, quaternionf, pX, pY, pZ, 1.0F, 1.0F, size, U1, V0, color);
            this.renderVertex(consumer, quaternionf, pX, pY, pZ, -1.0F, 1.0F, size, U0, V0, color);
            this.renderVertex(consumer, quaternionf, pX, pY, pZ, -1.0F, -1.0F, size, U0, V1, color);
        }
    }

    private void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = (new Vector3f(xOffset, yOffset, 0.0F)).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(packedLight);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            return new ParticleExSmoke(level, x, y, z);
        }
    }
}
