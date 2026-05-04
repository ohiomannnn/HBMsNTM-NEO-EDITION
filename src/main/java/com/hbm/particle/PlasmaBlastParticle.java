package com.hbm.particle;

import com.hbm.main.NuclearTechMod;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.render.NtmRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class PlasmaBlastParticle extends ParticleNT {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/particle/shockwave.png");

    public PlasmaBlastParticle(ClientLevel level, double x, double y, double z, float r, float g, float b, float pitch, float yaw) {
        super(level, x, y, z);

        this.lifetime = 20;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.xRot = pitch;
        this.yRot = yaw;
    }

    public void setMaxAge(int maxAge) {
        this.lifetime = maxAge;
    }

    public void setScale(float scale) {
        this.quadSize = scale;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {

        Vec3 camPos = camera.getPosition();
        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - camPos.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - camPos.y);
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - camPos.z);

        PoseStack poseStack = new PoseStack();

        poseStack.pushPose();
        poseStack.translate(pX, pY, pZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(this.yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(this.xRot));

        this.alpha = Math.clamp(1 - ((this.age + partialTicks) / (float)this.lifetime), 0.0F, 1.0F);
        this.quadSize = (1 - (float)Math.pow(Math.E, (this.age + partialTicks) * -0.125)) * quadSize;

        FogRenderer.setupNoFog();

        Matrix4f matrix = poseStack.last().pose();

        consumer.addVertex(matrix, - 1 * quadSize, 0, - 1 * quadSize)
                .setUv(1, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(matrix, - 1 * quadSize, 0, + 1 * quadSize)
                .setUv(1, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(matrix, + 1 * quadSize, 0, + 1 * quadSize)
                .setUv(0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(matrix, + 1 * quadSize, 0, - 1 * quadSize)
                .setUv(0, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);

        poseStack.popPose();
    }

    @Override
    public RenderType getRenderType() {
        return NtmRenderTypes.entityAdditive(TEXTURE);
    }
}
