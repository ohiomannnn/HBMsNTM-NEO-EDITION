package com.hbm.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class RBMKMushParticle extends TextureSheetParticle {

    public RBMKMushParticle(ClientLevel level, double x, double y, double z, float size) {
        super(level, x, y, z);
        this.setSpriteFromAge(ModParticles.RBMK_MUSH_SPRITES);

        this.lifetime = 50;
        this.quadSize = size;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) this.remove();

        this.setSpriteFromAge(ModParticles.RBMK_MUSH_SPRITES);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y) + this.quadSize;
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

        Vector3f up = new Vector3f(camera.getUpVector());
        Vector3f left = new Vector3f(camera.getLeftVector());

        renderQuad(consumer, pX, pY, pZ, up, left, this.quadSize);

        RenderSystem.enableCull();
    }

    private void renderQuad(VertexConsumer consumer, float pX, float pY, float pZ, Vector3f up, Vector3f left, float scale) {

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        Vector3f l = new Vector3f(left).mul(scale);
        Vector3f u = new Vector3f(up).mul(scale);

        consumer.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z)
                .setUv(u1, v1)
                .setColor(this.rCol, this.bCol, this.gCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z)
                .setUv(u1, v0)
                .setColor(this.rCol, this.bCol, this.gCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z)
                .setUv(u0, v0)
                .setColor(this.rCol, this.bCol, this.gCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z)
                .setUv(u0, v1)
                .setColor(this.rCol, this.bCol, this.gCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomRenderType.PARTICLE_SHEET_ADDITIVE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Provider(SpriteSet sprites) {
            ModParticles.RBMK_MUSH_SPRITES = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new RBMKMushParticle(level, x, y, z, 10);
        }
    }
}
