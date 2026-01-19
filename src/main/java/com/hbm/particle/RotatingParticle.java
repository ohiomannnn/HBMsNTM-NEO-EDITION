package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public abstract class RotatingParticle extends TextureSheetParticle {

    public RotatingParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    public void renderParticleRotated(BufferSource buffer, RenderType renderType, Camera camera, float r, float g, float b, float alpha, float scale, float partialTicks, int brightness) {
        Vec3 camPos = camera.getPosition();
        float pX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x);
        float pY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y);
        float pZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z);
        float rotation = Mth.lerp(partialTicks, this.oRoll, this.roll);

        Vector3f l = new Vector3f(camera.getLeftVector()).mul(scale);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(scale);

        double x1 = 0 - l.x - u.x;
        double y1 = 0 - l.y - u.y;
        double z1 = 0 - l.z - u.z;
        double x2 = 0 - l.x + u.x;
        double y2 = 0 + l.y + u.y;
        double z2 = 0 - l.z + u.z;
        double x3 = 0 + l.x + u.x;
        double y3 = 0 + l.y + u.y;
        double z3 = 0 + l.z + u.z;
        double x4 = 0 + l.x - u.x;
        double y4 = 0 - l.y - u.y;
        double z4 = 0 + l.z - u.z;

        double nX = ((y2 - y1) * (z3 - z1)) - ((z2 - z1) * (y3 - y1));
        double nY = ((z2 - z1) * (x3 - x1)) - ((x2 - x1) * (z3 - z1));
        double nZ = ((x2 - x1) * (y3 - y1)) - ((y2 - y1) * (x3 - x1));

        Vec3 vec = new Vec3(nX, nY, nZ).normalize();
        nX = vec.x;
        nY = vec.y;
        nZ = vec.z;

        double cosTh = Math.cos(rotation * Math.PI / 180D);
        double sinTh = Math.sin(rotation * Math.PI / 180D);

        float x01 = (float) (x1 * cosTh + (nY * z1 - nZ * y1) * sinTh);
        float y01 = (float) (y1 * cosTh + (nZ * x1 - nX * z1) * sinTh);
        float z01 = (float) (z1 * cosTh + (nX * y1 - nY * x1) * sinTh);
        float x02 = (float) (x2 * cosTh + (nY * z2 - nZ * y2) * sinTh);
        float y02 = (float) (y2 * cosTh + (nZ * x2 - nX * z2) * sinTh);
        float z02 = (float) (z2 * cosTh + (nX * y2 - nY * x2) * sinTh);
        float x03 = (float) (x3 * cosTh + (nY * z3 - nZ * y3) * sinTh);
        float y03 = (float) (y3 * cosTh + (nZ * x3 - nX * z3) * sinTh);
        float z03 = (float) (z3 * cosTh + (nX * y3 - nY * x3) * sinTh);
        float x04 = (float) (x4 * cosTh + (nY * z4 - nZ * y4) * sinTh);
        float y04 = (float) (y4 * cosTh + (nZ * x4 - nX * z4) * sinTh);
        float z04 = (float) (z4 * cosTh + (nX * y4 - nY * x4) * sinTh);

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        VertexConsumer consumer = buffer.getBuffer(renderType);

        consumer.addVertex(pX + x01, pY + y01, pZ + z01)
                .setColor(r, g, b, alpha)
                .setUv(u1, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(pX + x02, pY + y02, pZ + z02)
                .setColor(r, g, b, alpha)
                .setUv(u1, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(pX + x03, pY + y03, pZ + z03)
                .setColor(r, g, b, alpha)
                .setUv(u0, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(pX + x04, pY + y04, pZ + z04)
                .setColor(r, g, b, alpha)
                .setUv(u0, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(brightness)
                .setNormal(0.0F, 1.0F, 0.0F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomRenderType.NONE;
    }
}