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

public class ParticleHadron extends TextureSheetParticle {

    public ParticleHadron(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setSpriteFromAge(ModParticles.HADRON_SPITES);

        this.lifetime = 10;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

        this.alpha = Mth.clamp(1 - ((this.age + partialTicks) / (float)this.lifetime), 0.0F, 1.0F);
        this.quadSize = (this.age + partialTicks) * 0.15F;

        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        this.renderQuad(buffer, pX, pY, pZ, camera, this.quadSize, 240);
        buffer.endBatch();
    }

    private void renderQuad(MultiBufferSource.BufferSource buffer, float pX, float pY, float pZ, Camera camera, float scale, int brightness) {

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        Vector3f l = new Vector3f(camera.getLeftVector()).mul(scale);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(scale);

        VertexConsumer consumer = buffer.getBuffer(CustomRenderTypes.entityAdditive(TextureAtlas.LOCATION_PARTICLES));

        consumer.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setUv(u1, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setUv(u1, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setUv(u0, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setUv(u0, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ModParticleRenderTypes.NONE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Provider(SpriteSet sprites) {
            ModParticles.HADRON_SPITES = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleHadron(level, x, y, z);
        }
    }
}
