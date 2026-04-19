package com.hbm.render.entity.effect;

import com.hbm.entity.effect.FalloutRain;
import com.hbm.main.NuclearTechMod;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RenderFallout extends EntityRenderer<FalloutRain> {

    private static final ResourceLocation FALLOUT_LOCATION = NuclearTechMod.withDefaultNamespace("textures/entity/fallout.png");

    private final float[] rainSizeX;
    private final float[] rainSizeZ;
    private long lastTime = System.nanoTime();

    public RenderFallout(EntityRendererProvider.Context context) {
        super(context);

        this.rainSizeX = new float[1024];
        this.rainSizeZ = new float[1024];

        for(int i = 0; i < 32; ++i) {
            for(int j = 0; j < 32; ++j) {
                float f = (float)(j - 16);
                float f1 = (float)(i - 16);
                float f2 = Mth.sqrt(f * f + f1 * f1);
                this.rainSizeX[i << 5 | j] = -f1 / f2;
                this.rainSizeZ[i << 5 | j] = f / f2;
            }
        }
    }

    @Override
    public void render(FalloutRain rain, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        Entity camera = Minecraft.getInstance().getCameraEntity();
        if (camera == null) return;

        Vec3 vec = new Vec3(camera.position.x - rain.position.x, camera.position.y - rain.position.y, camera.position.z - rain.position.z);

        if (vec.length() <= rain.getScale()) {
            long time = System.nanoTime();
            float t = (time - lastTime) / 50_000_000;
            if(t <= 1.0F)
                renderSnowAndRain(t);
            else
                renderSnowAndRain(1.0F);

            lastTime = time;
        }
    }

    @Override public ResourceLocation getTextureLocation(FalloutRain rain) { return FALLOUT_LOCATION; }

    private void renderSnowAndRain(float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        int timer = minecraft.player.tickCount;

        minecraft.gameRenderer.lightTexture().turnOnLightLayer();
        Level level = minecraft.level;

        Entity camera = Minecraft.getInstance().getCameraEntity();
        if (camera == null) return;

        int playerX = Mth.floor(camera.position.x);
        int playerY = Mth.floor(camera.position.y);
        int playerZ = Mth.floor(camera.position.z);
        double dX = Mth.lerp(partialTicks, camera.xo, camera.position.x);
        double dY = Mth.lerp(partialTicks, camera.yo, camera.position.y);
        double dZ = Mth.lerp(partialTicks, camera.zo, camera.position.z);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = null;
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderSystem.enableDepthTest();
        int playerHeight = Mth.floor(dY);
        byte renderLayerCount = 5;
        byte layer = -1;
        if(Minecraft.useFancyGraphics()) renderLayerCount = 10;

        RenderSystem.depthMask(true);
        RenderSystem.setShader(GameRenderer::getParticleShader);
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for(int layerZ = playerZ - renderLayerCount; layerZ <= playerZ + renderLayerCount; ++layerZ) {
            for(int layerX = playerX - renderLayerCount; layerX <= playerX + renderLayerCount; ++layerX) {

                int rainCoord = (layerZ - playerZ + 16) * 32 + layerX - playerX + 16;
                float rainCoordX = this.rainSizeX[rainCoord] * 0.5F;
                float rainCoordY = this.rainSizeZ[rainCoord] * 0.5F;

                int rainHeight = level.getHeight(Heightmap.Types.MOTION_BLOCKING, layerX, layerZ);
                int minHeight = playerY - renderLayerCount;
                int maxHeight = playerY + renderLayerCount;

                if (minHeight < rainHeight) minHeight = rainHeight;
                if (maxHeight < rainHeight) maxHeight = rainHeight;

                int layerY = rainHeight;
                if(rainHeight < playerHeight) layerY = playerHeight;

                if(minHeight != maxHeight) {
                    RandomSource random = RandomSource.create((layerX * layerX * 3121L + layerX * 45238971L ^ layerZ * layerZ * 418711L + layerZ * 13761L));

                    if(layer != 1) {
                        if(layer >= 0) {
                            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
                        }
                        layer = 1;
                        RenderSystem.setShaderTexture(0, FALLOUT_LOCATION);
                        bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                    }

                    float fallSpeed = 1.0F;
                    float swayLoop = ((timer & 511) + partialTicks) / 512.0F;
                    float fallVariation = 0.4F + random.nextFloat() * 0.2F;
                    float swayVariation = random.nextFloat();
                    double distX = layerX + 0.5F - camera.position.x;
                    double distZ = layerZ + 0.5F - camera.position.z;
                    float intensityMod = Mth.sqrt((float) (distX * distX + distZ * distZ)) / renderLayerCount;
                    float colorMod = 1.0F;
                    float alpha = Math.clamp(((1.0F - intensityMod * intensityMod * 0.3F) + 0.5F) * 1.0F, 0.0F, 1.0F);

                    //the light
                    mutablePos.set(layerX, layerY, layerZ);
                    int packedLight = LevelRenderer.getLightColor(level, mutablePos);

                    PoseStack poseStack = new PoseStack();
                    poseStack.pushPose();

                    poseStack.translate(-dX * 1.0F, -dY * 1.0F, -dZ * 1.0F);
                    Matrix4f matrix = poseStack.last().pose();

                    bufferbuilder.addVertex(matrix, layerX - rainCoordX + 0.5F, minHeight, layerZ - rainCoordY + 0.5F).setUv(0.0F * fallSpeed + fallVariation, minHeight * fallSpeed / 4.0F + swayLoop * fallSpeed + swayVariation).setColor(colorMod, colorMod, colorMod, alpha).setLight(packedLight);
                    bufferbuilder.addVertex(matrix, layerX + rainCoordX + 0.5F, minHeight, layerZ + rainCoordY + 0.5F).setUv(1.0F * fallSpeed + fallVariation, minHeight * fallSpeed / 4.0F + swayLoop * fallSpeed + swayVariation).setColor(colorMod, colorMod, colorMod, alpha).setLight(packedLight);
                    bufferbuilder.addVertex(matrix, layerX + rainCoordX + 0.5F, maxHeight, layerZ + rainCoordY + 0.5F).setUv(1.0F * fallSpeed + fallVariation, maxHeight * fallSpeed / 4.0F + swayLoop * fallSpeed + swayVariation).setColor(colorMod, colorMod, colorMod, alpha).setLight(packedLight);
                    bufferbuilder.addVertex(matrix, layerX - rainCoordX + 0.5F, maxHeight, layerZ - rainCoordY + 0.5F).setUv(0.0F * fallSpeed + fallVariation, maxHeight * fallSpeed / 4.0F + swayLoop * fallSpeed + swayVariation).setColor(colorMod, colorMod, colorMod, alpha).setLight(packedLight);

                    poseStack.popPose();
                }
            }
        }

        if (layer >= 0) {
            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        }

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        minecraft.gameRenderer.lightTexture().turnOffLightLayer();
    }

    @Override
    public boolean shouldRender(FalloutRain rain, Frustum camera, double camX, double camY, double camZ) { return true; }
}
