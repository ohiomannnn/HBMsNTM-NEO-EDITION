package com.hbm.particle;

import com.hbm.HBMsNTM;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.render.CustomRenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class MukeWaveParticle extends ParticleNT {

    private float waveScale = 45F;

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/particle/shockwave.png");

    public MukeWaveParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);

        this.lifetime = 25;
    }

    public void setup(float scale, int maxAge) {
        this.waveScale = scale;
        this.lifetime = maxAge;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {

        Vec3 camPos = camera.getPosition();
        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - camPos.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - camPos.y);
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - camPos.z);

        this.alpha = Math.clamp(1 - ((this.age + partialTicks) / (float)this.lifetime), 0.0F, 1.0F);
        this.quadSize = (1 - (float)Math.pow(Math.E, (this.age + partialTicks) * -0.125)) * waveScale;

        FogRenderer.setupNoFog();

        consumer.addVertex(pX - 1 * this.quadSize, pY - 0.25F, pZ - 1 * this.quadSize)
                .setUv(1, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX - 1 * this.quadSize, pY - 0.25F, pZ + 1 * this.quadSize)
                .setUv(1, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX + 1 * this.quadSize, pY - 0.25F, pZ + 1 * this.quadSize)
                .setUv(0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX + 1 * this.quadSize, pY - 0.25F, pZ - 1 * this.quadSize)
                .setUv(0, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setColor(1.0F, 1.0F, 1.0F, alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
    }

    @Override
    public RenderType getRenderType() {
        return CustomRenderTypes.entityAdditive(TEXTURE);
    }
}
