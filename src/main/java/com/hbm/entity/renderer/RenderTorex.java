package com.hbm.entity.renderer;

import com.hbm.HBMsNTM;
import com.hbm.entity.logic.CustomRenderTypes;
import com.hbm.entity.logic.EntityNukeTorex;
import com.hbm.entity.logic.EntityNukeTorex.Cloudlet;
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
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class RenderTorex extends EntityRenderer<EntityNukeTorex> {

    private static final ResourceLocation CLOUDLET_TEX =
            ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/cloudlet.png");
    private static final ResourceLocation FLASH_TEX =
            ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/flare.png");

    public RenderTorex(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityNukeTorex entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        renderCloudlets(entity, partialTicks, buffer);
        if (entity.tickCount < 101) renderFlash(entity, partialTicks, buffer);
    }

    private void renderCloudlets(EntityNukeTorex cloud, float partialTicks, MultiBufferSource buffer) {
        ArrayList<Cloudlet> sorted = new ArrayList<>(cloud.cloudlets);
        Player player = Minecraft.getInstance().player;
        sorted.sort(Comparator.comparingDouble(c -> -player.distanceToSqr(c.posX, c.posY, c.posZ)));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(CLOUDLET_TEX));

        for (Cloudlet cloudlet : sorted) {
            Vec3 pos = cloudlet.getInterpPos(partialTicks);
            Vec3 col = cloudlet.getInterpColor(partialTicks);

            float r = (float) col.x;
            float g = (float) col.y;
            float b = (float) col.z;

            float brightness = (cloudlet.type == EntityNukeTorex.TorexType.CONDENSATION)
                    ? 0.9f
                    : 0.75f * cloudlet.colorMod;

            r *= brightness;
            g *= brightness;
            b *= brightness;

            r = Math.min(r, 1.0f);
            g = Math.min(g, 1.0f);
            b = Math.min(b, 1.0f);

            renderBillboardQuad(consumer, pos.x, pos.y, pos.z,
                    cloudlet.getScale(), cloudlet.getAlpha(), r, g, b);
        }
    }

    private void renderFlash(EntityNukeTorex cloud, float partialTicks, MultiBufferSource buffer) {
        VertexConsumer consumer = buffer.getBuffer(CustomRenderTypes.additive(FLASH_TEX));

        double age = Math.min(cloud.tickCount + partialTicks, 100);
        float alpha = (float) ((100D - age) / 100F);

        Random rand = new Random(cloud.getId());
        double baseX = cloud.getX();
        double baseY = cloud.getY();
        double baseZ = cloud.getZ();

        for (int i = 0; i < 10; i++) {
            double x = baseX + rand.nextGaussian() * 0.5F * cloud.rollerSize;
            double y = baseY + cloud.coreHeight + rand.nextGaussian() * 0.5F * cloud.rollerSize;
            double z = baseZ + rand.nextGaussian() * 0.5F * cloud.rollerSize;

            renderBillboardQuad(consumer, x, y, z,
                    (float) (25 * cloud.rollerSize), alpha, 1f, 1f, 1f);
        }
    }

    private void renderBillboardQuad(VertexConsumer consumer, double x, double y, double z, float scale, float alpha, float r, float g, float b) {

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        double cx = x - camera.getPosition().x;
        double cy = y - camera.getPosition().y;
        double cz = z - camera.getPosition().z;

        PoseStack poseStack = new PoseStack();
        poseStack.translate(cx, cy, cz);
        poseStack.mulPose(camera.rotation());
        PoseStack.Pose pose = poseStack.last();

        float half = scale * 0.5F;

        int packedLight = 0xF000F0;
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
