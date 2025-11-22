package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class DeadLeafParticle extends TextureSheetParticle {

    public DeadLeafParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setSpriteFromAge(ModParticles.DEAD_LEAVES_SPRITES);

        this.rCol = this.gCol = this.bCol = 1F - level.random.nextFloat() * 0.2F;
        this.quadSize = 0.1F;
        this.lifetime = 200 + level.random.nextInt(50);
        this.gravity = 0.2F;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.onGround) {
            this.xd += this.random.nextGaussian() * 0.002D;
            this.zd += this.random.nextGaussian() * 0.002D;

            if (this.yd < -0.025D) {
                this.yd = -0.025D;
            }
        }
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

        boolean flipU = this.hashCode() % 2 == 0;
        boolean flipV = this.hashCode() % 4 < 2;

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        float minU = flipU ? u1 : u0;
        float maxU = flipU ? u0 : u1;
        float minV = flipV ? v1 : v0;
        float maxV = flipV ? v0 : v1;

        renderQuad(consumer, camera, pX, pY, pZ, this.getLightColor(partialTicks), minU, maxU, minV, maxV);
    }

    private void renderQuad(VertexConsumer consumer, Camera camera,
                            float pX, float pY, float pZ, int brightness,
                            float u0, float u1, float v0, float v1) {

        Vector3f l = new Vector3f(camera.getLeftVector()).mul(this.quadSize);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(this.quadSize);

        consumer.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z)
                .setUv(u1, v1)
                .setColor(this.rCol, this.bCol, this.gCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(brightness);
        consumer.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z)
                .setUv(u1, v0)
                .setColor(this.rCol, this.bCol, this.gCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(brightness);
        consumer.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z)
                .setUv(u0, v0)
                .setColor(this.rCol, this.bCol, this.gCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(brightness);
        consumer.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z)
                .setUv(u0, v1)
                .setColor(this.rCol, this.bCol, this.gCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(brightness);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Provider(SpriteSet sprites) {
            ModParticles.DEAD_LEAVES_SPRITES = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DeadLeafParticle(level, x, y, z);
        }
    }
}
