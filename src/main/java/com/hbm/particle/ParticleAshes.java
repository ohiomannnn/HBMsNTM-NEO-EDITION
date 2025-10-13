package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ParticleAshes extends ParticleRotating {
    public ParticleAshes(ClientLevel level, double x, double y, double z, float scale) {
        super(level, x, y, z);
        this.setSpriteFromAge(ModParticles.ROCKET_FLAME_SPRITES);

        this.lifetime = 1200 + random.nextInt(20);
        this.quadSize = scale * 0.9F + random.nextFloat() * 0.2F;

        this.gravity = 0.01F;

        this.rCol = this.gCol = this.bCol = this.random.nextFloat() * 0.1F + 0.1F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.age++;

        if (this.age >= this.lifetime) {
            this.remove();
        }

        this.yd -= gravity;
        this.oRoll = this.roll;

        if(!this.onGround) this.roll += (float) (2 * ((this.hashCode() % 2) - 0.5));

        this.xd *= 0.95D;
        this.yd *= 0.99D;
        this.zd *= 0.95D;

        boolean wasOnGround = this.onGround;
        this.move(this.xd, this.yd, this.zd);
        if (!wasOnGround && this.onGround) this.roll = random.nextFloat() * 360F;

        if (this.hashCode() % 5 == 0 && this.onGround && random.nextInt(15) == 0) {
            level.addParticle(ParticleTypes.SMOKE, x, y + 0.125, z, 0, 0.05, 0);
        }
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 camPos = camera.getPosition();
        float timeLeft = this.lifetime - (this.age + partialTicks);

        if (timeLeft < 40) {
            this.alpha = timeLeft / 40F;
        } else {
            this.alpha = 1F;
        }

        if (this.onGround) {
            float pX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x());
            float pY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y());
            float pZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z());

            float f = this.getQuadSize(partialTicks);
            float u0 = this.getU0();
            float u1 = this.getU1();
            float v0 = this.getV0();
            float v1 = this.getV1();
            float y = pY + 0.01F;
            float angle = (float) Math.toRadians(this.roll);
            float sin = Mth.sin(angle);
            float cos = Mth.cos(angle);
            float half = f / 2.0F;

            float x0 = -half * cos - -half * sin;
            float z0 = -half * sin + -half * cos;
            float x1 = -half * cos - half * sin;
            float z1 = -half * sin + half * cos;
            float x2 = half * cos - half * sin;
            float z2 = half * sin + half * cos;
            float x3 = half * cos - -half * sin;
            float z3 = half * sin + -half * cos;

            int light = this.getLightColor(partialTicks);
            consumer.addVertex(pX + x0, y, pZ + z0).setUv(u1, v1).setColor(rCol, gCol, bCol, alpha).setLight(light);
            consumer.addVertex(pX + x1, y, pZ + z1).setUv(u1, v0).setColor(rCol, gCol, bCol, alpha).setLight(light);
            consumer.addVertex(pX + x2, y, pZ + z2).setUv(u0, v0).setColor(rCol, gCol, bCol, alpha).setLight(light);
            consumer.addVertex(pX + x3, y, pZ + z3).setUv(u0, v1).setColor(rCol, gCol, bCol, alpha).setLight(light);

        } else {
            renderParticleRotated(consumer, camera, this.rCol, this.gCol, this.bCol, this.alpha, this.quadSize, partialTicks);
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public TextureSheetParticle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double dx, double dy, double dz) {
            return new ParticleAshes(world, x, y, z, 0.5F);
        }
    }
}
