package com.hbm.render.effect;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.render.CustomRenderTypes;
import com.hbm.entity.effect.EntityNukeTorex;
import com.hbm.entity.effect.EntityNukeTorex.Cloudlet;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Random;

public class RenderTorex extends EntityRenderer<EntityNukeTorex> {

    private static final ResourceLocation CLOUDLET =
            ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/particle_base.png");
    private static final ResourceLocation FLASH =
            ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/flare.png");

    public RenderTorex(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityNukeTorex entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        long now = System.currentTimeMillis();
        if (entity.didPlaySound && !entity.didShake && now - HBMsNTMClient.shakeTimestamp > 1000) {
            HBMsNTMClient.shakeTimestamp = now;
            entity.didShake = true;

            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.hurtTime = 15;
                player.hurtDuration = 15;
            }
        }
        renderCloudlet(entity, partialTicks, buffer);
        if (entity.tickCount < 101) renderFlash(entity, partialTicks, buffer);
    }

    private void renderCloudlet(EntityNukeTorex entity, float partialTicks, MultiBufferSource buffer) {
        ArrayList<Cloudlet> sorted = new ArrayList<>(entity.cloudlets);

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(CLOUDLET));

        for (Cloudlet cloudlet : sorted) {
            Vec3 pos = cloudlet.getInterpPos(partialTicks);
            Vec3 col = cloudlet.getInterpColor(partialTicks);

            float r = (float)Math.max(col.x, 0.25);
            float g = (float)Math.max(col.y, 0.25);
            float b = (float)Math.max(col.z, 0.25);

            float brightness = (cloudlet.type == EntityNukeTorex.TorexType.CONDENSATION) ? 0.9f : 0.75f * cloudlet.colorMod;

            r = Math.min(r * brightness, 1.0f);
            g = Math.min(g * brightness, 1.0f);
            b = Math.min(b * brightness, 1.0f);

            renderQuad(consumer, pos.x, pos.y, pos.z, cloudlet.getScale(), cloudlet.getAlpha(), r, g, b);
        }
    }



    private void renderFlash(EntityNukeTorex cloud, float partialTicks, MultiBufferSource buffer) {
        VertexConsumer consumer = buffer.getBuffer(CustomRenderTypes.additive(FLASH));

        double age = Math.min(cloud.tickCount + partialTicks, 100);
        float alpha = (float) ((100D - age) / 100F);

        Random rand = new Random(cloud.getId());
        double baseX = cloud.getX();
        double baseY = cloud.getY();
        double baseZ = cloud.getZ();

        for (int i = 0; i < 3; i++) {
            double x = baseX + rand.nextGaussian() * 0.5F * cloud.rollerSize;
            double y = baseY + cloud.coreHeight + rand.nextGaussian() * 0.5F * cloud.rollerSize;
            double z = baseZ + rand.nextGaussian() * 0.5F * cloud.rollerSize;

            renderQuad(consumer, x, y, z, (float) (25 * cloud.rollerSize), alpha, 1f, 1f, 1f);
        }
    }

    private void renderQuad(VertexConsumer consumer, double x, double y, double z, float scale, float alpha, float r, float g, float b) {

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        double cx = x - camera.getPosition().x;
        double cy = y - camera.getPosition().y;
        double cz = z - camera.getPosition().z;

        PoseStack poseStack = new PoseStack();
        poseStack.translate(cx, cy, cz);
        poseStack.mulPose(camera.rotation());
        PoseStack.Pose pose = poseStack.last();

        float half = scale * 0.5F;

        int packedLight = 15728880;
        int overlay = OverlayTexture.NO_OVERLAY;

        consumer.addVertex(pose, -half, -half, 0)
                .setColor(r, g, b, alpha)
                .setUv(1, 1)
                .setOverlay(overlay)
                .setLight(packedLight)
                .setNormal(pose, 0, 1, 0);

        consumer.addVertex(pose, -half, half, 0)
                .setColor(r, g, b, alpha)
                .setUv(1, 0)
                .setOverlay(overlay)
                .setLight(packedLight)
                .setNormal(pose, 0, 1, 0);

        consumer.addVertex(pose, half, half, 0)
                .setColor(r, g, b, alpha)
                .setUv(0, 0)
                .setOverlay(overlay)
                .setLight(packedLight)
                .setNormal(pose, 0, 1, 0);

        consumer.addVertex(pose, half, -half, 0)
                .setColor(r, g, b, alpha)
                .setUv(0, 1)
                .setOverlay(overlay)
                .setLight(packedLight)
                .setNormal(pose, 0, 1, 0);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityNukeTorex entity) {
        return null;
    }
}
