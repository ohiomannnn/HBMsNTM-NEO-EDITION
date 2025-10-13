package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ParticleRotating extends TextureSheetParticle {

    public ParticleRotating(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    public void renderParticleRotated(VertexConsumer consumer, Camera camera, float r, float g, float b, float alpha, double scale, float partialTicks) {
        Vec3 camPos = camera.getPosition();
        float px = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x());
        float py = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y());
        float pz = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z());

        Quaternionf quaternion = new Quaternionf(camera.rotation());

        float roll = this.oRoll + (this.roll - this.oRoll) * partialTicks;
        if (roll != 0.0F) {
            quaternion.rotateZ(roll * ((float)Math.PI / 180F));
        }

        float f = (float) scale;
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        renderVertex(consumer, quaternion, px, py, pz, 1.0F, -1.0F, f, u1, v1, 240, r, g, b, alpha);
        renderVertex(consumer, quaternion, px, py, pz, 1.0F,  1.0F, f, u1, v0, 240, r, g, b, alpha);
        renderVertex(consumer, quaternion, px, py, pz, -1.0F, 1.0F, f, u0, v0, 240, r, g, b, alpha);
        renderVertex(consumer, quaternion, px, py, pz, -1.0F, -1.0F, f, u0, v1, 240, r, g, b, alpha);
    }

    private void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight, float r, float g, float b, float alpha) {
        Vector3f vec = new Vector3f(xOffset, yOffset, 0.0F).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.addVertex(vec.x(), vec.y(), vec.z()).setUv(u, v).setColor(r, g, b, alpha).setLight(packedLight);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}