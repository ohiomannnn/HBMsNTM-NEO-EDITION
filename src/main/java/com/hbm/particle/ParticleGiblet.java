package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class ParticleGiblet extends TextureSheetParticle {

    public ParticleGiblet(ClientLevel level, double x, double y, double z, double mX, double mY, double mZ) {
        super(level, x, y, z);
        this.xd = mX;
        this.yd = mY;
        this.zd = mZ;
        this.lifetime = 140 + random.nextInt(20);
        this.gravity = 2F;

        this.setSpriteFromAge(ModParticles.GIBLET_SPRITES);
    }

    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square((double)100.0F);

    @Override
    public void move(double x, double y, double z) {
        double d0 = x;
        double d1 = y;
        double d2 = z;

        if (this.hasPhysics && (x != 0.0 || y != 0.0 || z != 0.0) && x * x + y * y + z * z < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
            Vec3 vec3 = Entity.collideBoundingBox(null, new Vec3(x, y, z), this.getBoundingBox(), this.level, List.of());
            x = vec3.x;
            y = vec3.y;
            z = vec3.z;
        }

        if (x != 0.0 || y != 0.0 || z != 0.0) {
            this.setBoundingBox(this.getBoundingBox().move(x, y, z));
            this.setLocationFromBoundingbox();
        }

        this.onGround = d1 != y && d1 < 0.0;

        if (this.onGround) {
            this.xd = 0.0;
            this.yd = 0.0;
            this.zd = 0.0;
        } else {
            if (d0 != x) this.xd = 0.0;
            if (d2 != z) this.zd = 0.0;
        }
    }

    @Override
    public void tick() {
        super.tick();

        this.oRoll = this.roll;

        if (!this.onGround) {
            ParticleDust particleDust = new ParticleDust(level, x, y, z, 0, 0, 0, Blocks.REDSTONE_BLOCK.defaultBlockState());
            particleDust.setLifetime(20 + random.nextInt(20));
            particleDust.setOriginalSize();
            Minecraft.getInstance().particleEngine.add(particleDust);
        }
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        float dX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float dY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
        float dZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

        Vector3f up = new Vector3f(camera.getUpVector());
        Vector3f left = new Vector3f(camera.getLeftVector());

        renderQuad(consumer, dX, dY, dZ, up, left, this.quadSize, this.getLightColor(partialTicks));
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

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Provider(SpriteSet sprites) {
            ModParticles.GIBLET_SPRITES = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            return new ParticleGiblet(level, x, y, z, 0, 0, 0);
        }
    }
}
