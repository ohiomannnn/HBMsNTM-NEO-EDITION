package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class DebugParticle extends TextureSheetParticle {

    public DebugParticle(ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(level, x, y, z);
        this.lifetime = 10;
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;
        this.hasPhysics = false;
    }

    public DebugParticle(ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ, int color) {
        this(level, x, y, z, motionX, motionY, motionZ);

        this.rCol = ((color & 0xff0000) >> 16) / 255F;
        this.gCol = ((color & 0x00ff00) >> 8) / 255F;
        this.bCol = (color & 0x0000ff) / 255F;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        Vector3f l = new Vector3f(camera.getLeftVector()).mul(this.quadSize);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(this.quadSize);

        consumer.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setUv(u1, v1)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setUv(u1, v0)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setUv(u0, v0)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setUv(u0, v1)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class PowerProvider implements ParticleProvider<SimpleParticleType> {

        public PowerProvider(SpriteSet sprites) {
            ModParticles.POWER_SPRITES = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            DebugParticle particle = new DebugParticle(level, x, y, z, dx, dy, dz);
            particle.setSpriteFromAge(ModParticles.POWER_SPRITES);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FluidProvider implements ParticleProvider<SimpleParticleType> {

        public FluidProvider(SpriteSet sprites) {
            ModParticles.FLUID_SPRITES = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            DebugParticle particle = new DebugParticle(level, x, y, z, dx, dy, dz, 0x344feb);
            particle.setSpriteFromAge(ModParticles.FLUID_SPRITES);
            return particle;
        }
    }
}
