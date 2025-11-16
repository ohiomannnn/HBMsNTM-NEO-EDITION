package com.hbm.render.entity.effect;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.entity.effect.NukeTorex;
import com.hbm.entity.effect.NukeTorex.Cloudlet;
import com.hbm.lib.ModSounds;
import com.hbm.render.CustomRenderTypes;
import com.hbm.util.old.TessColorUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class RenderTorex extends EntityRenderer<NukeTorex> {

    private static final ResourceLocation CLOUDLET = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/base_particle.png");
    private static final ResourceLocation FLASH = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/flare.png");

    public RenderTorex(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(NukeTorex entity, float yaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int packedLight) {
        cloudletWrapper(entity, partialTicks, buffer);
        if (entity.tickCount < 101) flashWrapper(entity, partialTicks, buffer);
        if (entity.tickCount < 10 && System.currentTimeMillis() - HBMsNTMClient.flashTimestamp > 1_000) HBMsNTMClient.flashTimestamp = System.currentTimeMillis();
        if (entity.didPlaySound && !entity.didShake && System.currentTimeMillis() - HBMsNTMClient.shakeTimestamp > 1_000) {
            HBMsNTMClient.shakeTimestamp = System.currentTimeMillis();
            entity.didShake = true;
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.hurtDuration = 15;
                player.hurtTime = 15;
            }
        }
    }

    private void cloudletWrapper(NukeTorex cloud, float partialTicks, MultiBufferSource buffer) {
        VertexConsumer consumer = buffer.getBuffer(CustomRenderTypes.entitySmoth(CLOUDLET));

        Vec3 camPosition = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        ArrayList<Cloudlet> cloudlets = new ArrayList<>(cloud.cloudlets);
        cloudlets.sort(cloudSorter);

        for (Cloudlet cloudlet : cloudlets) {
            Vec3 vec = cloudlet.getInterpPos(partialTicks);
            double x = vec.x - camPosition.x;
            double y = vec.y - camPosition.y;
            double z = vec.z - camPosition.z;
            renderCloudlet(consumer, (float) x, (float) y, (float) z, cloudlet, partialTicks);
        }
    }

    private void flashWrapper(NukeTorex cloud, float partialTicks, MultiBufferSource buffer) {
        VertexConsumer consumer = buffer.getBuffer(CustomRenderTypes.entityAdditive(FLASH));
        Vec3 camPosition = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        double baseX = cloud.getX() - camPosition.x;
        double baseY = cloud.getY() - camPosition.y;
        double baseZ = cloud.getZ() - camPosition.z;

        double age = Math.min(cloud.tickCount + partialTicks, 100);
        float alpha = (float) ((100D - age) / 100F);

        Random rand = new Random(cloud.getId());
        for (int i = 0; i < 3; i++) {
            double x = baseX + rand.nextGaussian() * 0.5F * cloud.rollerSize;
            double y = baseY + rand.nextGaussian() * 0.5F * cloud.rollerSize;
            double z = baseZ + rand.nextGaussian() * 0.5F * cloud.rollerSize;
            renderFlash(consumer, (float) x, (float) (y + cloud.coreHeight), (float) z, (float) (25 * cloud.rollerSize), alpha);
        }
    }


    private void renderCloudlet(VertexConsumer consumer, float posX, float posY, float posZ, Cloudlet cloud, float partialTicks) {

        float alpha = cloud.getAlpha();
        float scale = cloud.getScale();

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vector3f l = new Vector3f(camera.getLeftVector()).mul(scale);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(scale);

        float brightness = cloud.type == NukeTorex.TorexType.CONDENSATION ? 0.9F : 0.75F * cloud.colorMod;
        Vec3 interpColor = cloud.getInterpColor(partialTicks);

        int color = TessColorUtil.getColorRGBA_F((float)interpColor.x * brightness, (float)interpColor.y * brightness, (float)interpColor.z * brightness, alpha);
        int overlay = OverlayTexture.NO_OVERLAY;

        consumer.addVertex(posX - l.x - u.x, posY - l.y - u.y, posZ - l.z - u.z)
                .setColor(color)
                .setUv(1, 1)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(posX - l.x + u.x, posY - l.y + u.y, posZ - l.z + u.z)
                .setColor(color)
                .setUv(1, 0)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(posX + l.x + u.x, posY + l.y + u.y, posZ + l.z + u.z)
                .setColor(color)
                .setUv(0, 0)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(posX + l.x - u.x, posY + l.y - u.y, posZ + l.z - u.z)
                .setColor(color)
                .setUv(0, 1)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
    }

    private void renderFlash(VertexConsumer consumer, float posX, float posY, float posZ, float scale, float alpha) {

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vector3f l = new Vector3f(camera.getLeftVector()).mul(scale);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(scale);

        int color = TessColorUtil.getColorRGBA_F(1.0F, 1.0F, 1.0F, alpha);
        int overlay = OverlayTexture.NO_OVERLAY;

        consumer.addVertex(posX - l.x - u.x, posY - l.y - u.y, posZ - l.z - u.z)
                .setColor(color)
                .setUv(1, 1)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(posX - l.x + u.x, posY - l.y + u.y, posZ - l.z + u.z)
                .setColor(color)
                .setUv(1, 0)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(posX + l.x + u.x, posY + l.y + u.y, posZ + l.z + u.z)
                .setColor(color)
                .setUv(0, 0)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(posX + l.x - u.x, posY + l.y - u.y, posZ + l.z - u.z)
                .setColor(color)
                .setUv(0, 1)
                .setOverlay(overlay)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
    }

    @Override
    public ResourceLocation getTextureLocation(NukeTorex entity) {
        return CLOUDLET;
    }

    private final Comparator<Cloudlet> cloudSorter = (ca, cb) -> {
        Player player = Minecraft.getInstance().player;
        if (player == null) return 0;
        double dist1 = player.distanceToSqr(ca.posX, ca.posY, ca.posZ);
        double dist2 = player.distanceToSqr(cb.posX, cb.posY, cb.posZ);
        return Double.compare(dist2, dist1);
    };

    public static void handleSound(NukeTorex entity, int tickCount) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            double dist = player.distanceTo(entity);
            double radius = (tickCount * 1.5 + 1) * 1.5;
            if (dist < radius) {
                entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), ModSounds.NUCLEAR_EXPLOSION.get(), SoundSource.AMBIENT, 10_000F, 1F, false);
                entity.didPlaySound = true;
            }
        }
    }
}
