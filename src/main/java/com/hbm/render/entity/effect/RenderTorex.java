package com.hbm.render.entity.effect;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.entity.effect.EntityNukeTorex;
import com.hbm.entity.effect.EntityNukeTorex.Cloudlet;
import com.hbm.render.CustomRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class RenderTorex extends EntityRenderer<EntityNukeTorex> {

    private static final ResourceLocation CLOUDLET = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/particle_base.png");
    private static final ResourceLocation FLASH = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/flare.png");

    public RenderTorex(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityNukeTorex entity, float yaw, float partialTicks, PoseStack pose, MultiBufferSource buffers, int packedLight) {
        pose.pushPose();
        pose.translate(0, 0, 0);

        cloudletWrapper(entity, partialTicks, pose, buffers);
        if (entity.tickCount < 101) {
            flashWrapper(entity, partialTicks, pose, buffers);
        }

        if (entity.tickCount < 10 && System.currentTimeMillis() - HBMsNTMClient.flashTimestamp > 1000) {
            HBMsNTMClient.flashTimestamp = System.currentTimeMillis();
        }

        if (entity.didPlaySound && !entity.didShake && System.currentTimeMillis() - HBMsNTMClient.shakeTimestamp > 1000) {
            HBMsNTMClient.shakeTimestamp = System.currentTimeMillis();
            entity.didShake = true;
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.hurtDuration = 15;
                player.hurtTime = 15;
            }
        }

        pose.popPose();
        super.render(entity, yaw, partialTicks, pose, buffers, packedLight);
    }

    private final Comparator<Cloudlet> cloudSorter = (a, b) -> {
        Player player = Minecraft.getInstance().player;
        if (player == null) return 0;
        double d1 = player.distanceToSqr(a.posX, a.posY, a.posZ);
        double d2 = player.distanceToSqr(b.posX, b.posY, b.posZ);
        return Double.compare(d2, d1);
    };

    private void cloudletWrapper(EntityNukeTorex cloud, float partialTicks, PoseStack pose, MultiBufferSource buffers) {
        VertexConsumer consumer = buffers.getBuffer(RenderType.entityTranslucent(CLOUDLET));

        ArrayList<Cloudlet> cloudlets = new ArrayList<>(cloud.cloudlets);
        cloudlets.sort(cloudSorter);

        for (Cloudlet c : cloudlets) {
            Vec3 vec = c.getInterpPos(partialTicks);
            double x = vec.x - cloud.getX();
            double y = vec.y - cloud.getY();
            double z = vec.z - cloud.getZ();
            renderCloudlet(consumer, pose, x, y, z, c, partialTicks);
        }
    }

    private void flashWrapper(EntityNukeTorex cloud, float partialTicks, PoseStack pose, MultiBufferSource buffers) {
        VertexConsumer consumer = buffers.getBuffer(CustomRenderTypes.additive(FLASH));

        double age = Math.min(cloud.tickCount + partialTicks, 100);
        float alpha = (float) ((100D - age) / 100F);

        Random rand = new Random(cloud.getId());
        for (int i = 0; i < 3; i++) {
            float x = (float) (rand.nextGaussian() * 0.5F * cloud.rollerSize);
            float y = (float) (rand.nextGaussian() * 0.5F * cloud.rollerSize);
            float z = (float) (rand.nextGaussian() * 0.5F * cloud.rollerSize);
            renderFlash(consumer, pose, x, y + cloud.coreHeight, z, (float) (25 * cloud.rollerSize), alpha);
        }
    }

    private void renderCloudlet(VertexConsumer consumer, PoseStack pose, double px, double py, double pz, Cloudlet cloud, float partialTicks) {
        float alpha = cloud.getAlpha();
        float scale = cloud.getScale();

        float brightness = cloud.type == EntityNukeTorex.TorexType.CONDENSATION ? 0.9F : 0.75F * cloud.colorMod;
        Vec3 color = cloud.getInterpColor(partialTicks);

        float r = (float)Math.max(color.x, 0.25);
        float g = (float)Math.max(color.y, 0.25);
        float b = (float)Math.max(color.z, 0.25);

        r = Math.min(r * brightness, 1.0f);
        g = Math.min(g * brightness, 1.0f);
        b = Math.min(b * brightness, 1.0f);

        drawBillboardQuad(consumer, pose, px, py, pz, scale, r, g, b, alpha);
    }

    private void renderFlash(VertexConsumer consumer, PoseStack pose, double px, double py, double pz, float scale, float alpha) {
        drawBillboardQuad(consumer, pose, px, py, pz, scale, 1F, 1F, 1F, alpha);
    }

    private void drawBillboardQuad(VertexConsumer consumer, PoseStack pose, double px, double py, double pz, float scale, float r, float g, float b, float a) {
        PoseStack.Pose last = pose.last();
        Matrix4f matrix = last.pose();

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Quaternionf camRot = camera.rotation();
        Matrix3f camMat = new Matrix3f();
        camRot.get(camMat);

        Vector3f right = new Vector3f(1, 0, 0).mul(camMat).mul(scale);
        Vector3f up    = new Vector3f(0, 1, 0).mul(camMat).mul(scale);

        Vector3f normal = last.normal().transform(new Vector3f(0, 1, 0));

        int color = FastColor.ARGB32.color((int)(a * 255), (int)(r * 255), (int)(g * 255), (int)(b * 255));
        int light = 15728880;
        int overlay = OverlayTexture.NO_OVERLAY;

        float u0 = 0, v0 = 0;
        float u1 = 1, v1 = 1;

        consumer.addVertex(matrix, (float)(px - right.x() - up.x()), (float)(py - right.y() - up.y()), (float)(pz - right.z() - up.z()))
                .setColor(color).setUv(u1, v1).setOverlay(overlay).setLight(light)
                .setNormal(normal.x(), normal.y(), normal.z());

        consumer.addVertex(matrix, (float)(px - right.x() + up.x()), (float)(py - right.y() + up.y()), (float)(pz - right.z() + up.z()))
                .setColor(color).setUv(u1, v0).setOverlay(overlay).setLight(light)
                .setNormal(normal.x(), normal.y(), normal.z());

        consumer.addVertex(matrix, (float)(px + right.x() + up.x()), (float)(py + right.y() + up.y()), (float)(pz + right.z() + up.z()))
                .setColor(color).setUv(u0, v0).setOverlay(overlay).setLight(light)
                .setNormal(normal.x(), normal.y(), normal.z());

        consumer.addVertex(matrix, (float)(px + right.x() - up.x()), (float)(py + right.y() - up.y()), (float)(pz + right.z() - up.z()))
                .setColor(color).setUv(u0, v1).setOverlay(overlay).setLight(light)
                .setNormal(normal.x(), normal.y(), normal.z());
    }

    @Override
    public ResourceLocation getTextureLocation(EntityNukeTorex entity) {
        return null;
    }
}
