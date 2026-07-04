package com.hbm.particle;

import com.hbm.main.ResourceManager;
import com.hbm.particle.engine.ParticleEngineNT;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.render.NtmRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

public class CloudTomParticle extends ParticleNT {

    public CloudTomParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);

        this.lifetime = 500;
    }

    @Override
    public void tick() {

        this.level.setSkyFlashTime(5);

        if(this.age++ >= this.lifetime) this.dead = true;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {

        Vec3 camPos = camera.getPosition();
        PoseStack poseStack = new PoseStack();
        poseStack.translate(this.x - camPos.x, this.y - camPos.y, this.z - camPos.z);
        Matrix4f matrix = poseStack.last().pose();

        float scale = this.age + partialTicks;

        int segments = 16;
        float height = 20F;
        float depth = 20F;
        float angleStep = (float) (Math.PI * 2.0 / segments);

        float vOffset = -(this.age + partialTicks) * 0.05F;

        for(int i = 0; i < segments; i++) {
            float angle1 = i * angleStep;
            float angle2 = (i + 1) * angleStep;

            float x1 = (float) Math.cos(angle1) * scale;
            float z1 = (float) Math.sin(angle1) * scale;

            float x2 = (float) Math.cos(angle2) * scale;
            float z2 = (float) Math.sin(angle2) * scale;

            for(int j = 0; j < 5; j++) {
                float mod = 1.0F - j * 0.025F;
                float h = height + j * 10F;

                float off = 1.0F / (j + 1);

                float px1 = x1 * mod;
                float pz1 = z1 * mod;
                float px2 = x2 * mod;
                float pz2 = z2 * mod;
                float u0 = 0.0F;
                float u1 = 1.0F;
                float v0 = 0.0F + off + vOffset;
                float v1 = 1.0F + off + vOffset;

                consumer.addVertex(matrix, px1, h, pz1)
                        .setColor(1.0F, 1.0F, 1.0F, 0.0F)
                        .setUv(u0, v1)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(LightTexture.FULL_BRIGHT)
                        .setNormal(0.0F, 1.0F, 0.0F);
                consumer.addVertex(matrix, px1, -depth, pz1)
                        .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                        .setUv(u0, v0)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(LightTexture.FULL_BRIGHT)
                        .setNormal(0.0F, 1.0F, 0.0F);

                consumer.addVertex(matrix, px2, -depth, pz2)
                        .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                        .setUv(u1, v0)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(LightTexture.FULL_BRIGHT)
                        .setNormal(0.0F, 1.0F, 0.0F);
                consumer.addVertex(matrix, px2, h, pz2)
                        .setColor(1.0F, 1.0F, 1.0F, 0.0F)
                        .setUv(u1, v1)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(LightTexture.FULL_BRIGHT)
                        .setNormal(0.0F, 1.0F, 0.0F);
            }
        }
    }

    @Override
    public RenderType getRenderType() {
        return NtmRenderTypes.SMOTH_NO_DEPTH.apply(ResourceManager.TOM_BLAST_TEX);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            ParticleEngineNT.INSTANCE.add(new CloudTomParticle(level, x, y, z));
            return null;
        }
    }
}
