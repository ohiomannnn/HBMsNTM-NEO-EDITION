package com.hbm.render.entity.effect;

import com.hbm.HBMsNTM;
import com.hbm.entity.effect.FalloutRain;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Random;

public class RenderFallout extends EntityRenderer<FalloutRain> {

    private static final ResourceLocation FALLOUT_TEXTURE = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/entity/fallout.png");

    private final Random random = new Random();
    private float[] rainXCoords;
    private float[] rainYCoords;
    private long lastTime = System.nanoTime();

    public RenderFallout(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(FalloutRain entity) {
        return FALLOUT_TEXTURE;
    }

    @Override
    public void render(FalloutRain entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Minecraft mc = Minecraft.getInstance();
        LivingEntity camera = mc.player;
        ClientLevel world = mc.level;
        if (camera == null || world == null) return;
        long time = System.nanoTime();
        float dt = (time - lastTime) / 50_000_000f;
        float interp = Math.min(dt, 1.0f);
        lastTime = time;

        PoseStack globalPose = new PoseStack();
        renderRainSnow(interp, globalPose, bufferSource, world, camera);
    }

    @Override
    public boolean shouldRender(FalloutRain livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    public void renderRainSnow(float interp, PoseStack pose, MultiBufferSource buffers, ClientLevel world, LivingEntity camera) {

        int timer = camera.tickCount;
        float intensity = 1.0F;
        if (intensity <= 0.0F) return;

        if (this.rainXCoords == null) {
            this.rainXCoords = new float[1024];
            this.rainYCoords = new float[1024];
            for (int i = 0; i < 32; ++i) {
                for (int j = 0; j < 32; ++j) {
                    float f2 = j - 16;
                    float f3 = i - 16;
                    float f4 = Mth.sqrt(f2 * f2 + f3 * f3);
                    this.rainXCoords[i << 5 | j] = -f3 / f4;
                    this.rainYCoords[i << 5 | j] = f2 / f4;
                }
            }
        }

        var graphics = Minecraft.getInstance().options.graphicsMode().get();
        boolean fancy = graphics == GraphicsStatus.FANCY || graphics == GraphicsStatus.FABULOUS;
        int renderLayerCount = fancy ? 10 : 5;

        double camX = camera.xOld + (camera.getX() - camera.xOld) * interp;
        double camY = camera.yOld + (camera.getY() - camera.yOld) * interp;
        double camZ = camera.zOld + (camera.getZ() - camera.zOld) * interp;

        int playerX = Mth.floor(camera.getX());
        int playerY = Mth.floor(camera.getY());
        int playerZ = Mth.floor(camera.getZ());

        pose.pushPose();
        pose.translate(-camX, -camY, -camZ);

        VertexConsumer consumer = buffers.getBuffer(RenderType.entityTranslucent(FALLOUT_TEXTURE));
        int overlay = OverlayTexture.NO_OVERLAY;
        int maxLight = 15728880;

        for (int layerZ = playerZ - renderLayerCount; layerZ <= playerZ + renderLayerCount; ++layerZ) {
            for (int layerX = playerX - renderLayerCount; layerX <= playerX + renderLayerCount; ++layerX) {

                int rainCoord = (layerZ - playerZ + 16) * 32 + layerX - playerX + 16;
                float rainCoordX = this.rainXCoords[rainCoord] * 0.5F;
                float rainCoordY = this.rainYCoords[rainCoord] * 0.5F;

                int rainHeight = world.getHeight(Heightmap.Types.MOTION_BLOCKING, layerX, layerZ);
                int minHeight = playerY - renderLayerCount;
                int maxHeight = playerY + renderLayerCount;
                if (minHeight < rainHeight) minHeight = rainHeight;
                if (maxHeight < rainHeight) maxHeight = rainHeight;
                if (minHeight == maxHeight) continue;

                this.random.setSeed(layerX * layerX * 3121L + layerX * 45238971L ^ layerZ * layerZ * 418711L + layerZ * 13761L);

                float swayLoop = ((timer & 511) + interp) / 512.0F;
                float fallVariation = 0.4F + this.random.nextFloat() * 0.2F;
                float swayVariation = this.random.nextFloat();

                double distX = layerX + 0.5D - camera.getX();
                double distZ = layerZ + 0.5D - camera.getZ();
                float intensityMod = Mth.sqrt((float) (distX * distX + distZ * distZ)) / renderLayerCount;

                float alpha = ((1.0F - intensityMod * intensityMod) * 0.3F + 0.5F) * intensity;

                float u0 = 0.0F + fallVariation;
                float u1 = 1.0F + fallVariation;
                float vMin = minHeight / 4.0F + swayLoop + swayVariation;
                float vMax = maxHeight / 4.0F + swayLoop + swayVariation;

                int argb = FastColor.ARGB32.color((int) (alpha * 255), 255, 255, 255);

                float vx0 = (float) (layerX - rainCoordX + 0.5D);
                float vz0 = (float) (layerZ - rainCoordY + 0.5D);
                float vx1 = (float) (layerX + rainCoordX + 0.5D);
                float vz1 = (float) (layerZ + rainCoordY + 0.5D);

                consumer.addVertex(pose.last().pose(), vx0, minHeight, vz0)
                        .setColor(argb).setUv(u0, vMin).setOverlay(overlay).setLight(maxLight).setNormal(0, 1, 0);
                consumer.addVertex(pose.last().pose(), vx1, minHeight, vz1)
                        .setColor(argb).setUv(u1, vMin).setOverlay(overlay).setLight(maxLight).setNormal(0, 1, 0);
                consumer.addVertex(pose.last().pose(), vx1, maxHeight, vz1)
                        .setColor(argb).setUv(u1, vMax).setOverlay(overlay).setLight(maxLight).setNormal(0, 1, 0);
                consumer.addVertex(pose.last().pose(), vx0, maxHeight, vz0)
                        .setColor(argb).setUv(u0, vMax).setOverlay(overlay).setLight(maxLight).setNormal(0, 1, 0);
            }
        }

        pose.popPose();
    }
}
