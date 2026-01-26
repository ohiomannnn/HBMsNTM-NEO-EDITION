package com.hbm.particle;

import com.hbm.render.CustomRenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
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
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y) + this.quadSize;
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        Vector3f l = new Vector3f(camera.getLeftVector()).mul(this.quadSize);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(this.quadSize);

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer consumer = bufferSource.getBuffer(CustomRenderTypes.entityAdditive(TextureAtlas.LOCATION_PARTICLES));

        consumer.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z)
                .setColor(this.rCol, this.bCol, this.gCol, this.alpha)
                .setUv(u1, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z)
                .setColor(this.rCol, this.bCol, this.gCol, this.alpha)
                .setUv(u1, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z)
                .setColor(this.rCol, this.bCol, this.gCol, this.alpha)
                .setUv(u0, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z)
                .setColor(this.rCol, this.bCol, this.gCol, this.alpha)
                .setUv(u0, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);

        bufferSource.endBatch();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ModParticleRenderTypes.NONE;
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
