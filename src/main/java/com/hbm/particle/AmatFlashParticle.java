package com.hbm.particle;

import com.hbm.particle.engine.ParticleEngineNT;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.particle.vanilla.NbtParticleOptions;
import com.hbm.particle.vanilla.ParticleProviderBase;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class AmatFlashParticle extends ParticleNT {

    private static final RenderType AMAT = RenderType.create(
            "amat_render_type", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.TRIANGLE_FAN, 1024,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_TEXT_BACKGROUND_SHADER)
                    .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
                    .setTextureState(RenderType.NO_TEXTURE)
                    .setLightmapState(RenderType.LIGHTMAP)
                    .setOverlayState(RenderType.OVERLAY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                    .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
                    .createCompositeState(false)
    );

    public AmatFlashParticle(ClientLevel level, double x, double y, double z, float size) {
        super(level, x, y, z);

        this.lifetime = 10;
        this.quadSize = size;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        PoseStack poseStack = new PoseStack();

        Vec3 cameraPosition = camera.getPosition();
        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

        poseStack.pushPose();
        poseStack.translate(pX, pY, pZ);
        poseStack.scale(0.2F * quadSize, 0.2F * quadSize, 0.2F * quadSize);

        float intensity = (this.age + partialTicks) / this.lifetime;
        float inverse = Math.clamp(1.0F - intensity, 0F, 0.3F);

        RandomSource random = RandomSource.create(432L);

        float scale = 0.5F;

        for(int i = 0; i < 100; i++) {
            poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360F));
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360F));
            poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360F));
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360F));

            float vert1 = (random.nextFloat() * 20.0F + 5.0F + 1 * 10.0F) * (intensity * scale);
            float vert2 = (random.nextFloat() * 2.0F + 1.0F + 1 * 2.0F) * (intensity * scale);

            int packedLight = this.getLightColor();
            Matrix4f matrix = poseStack.last().pose();

            consumer.addVertex(matrix, 0, 0, 0).setColor(1.0F, 1.0F, 1.0F, inverse).setLight(packedLight);
            consumer.addVertex(matrix, -0.866F * vert2, vert1, -0.5F * vert2).setColor(1.0F, 1.0F, 1.0F, 0F).setLight(packedLight);
            consumer.addVertex(matrix, 0.866F * vert2, vert1, -0.5F * vert2).setColor(1.0F, 1.0F, 1.0F, 0F).setLight(packedLight);
            consumer.addVertex(matrix, 0.0F, vert1, 1.0F * vert2).setColor(1.0F, 1.0F, 1.0F, 0F).setLight(packedLight);
            consumer.addVertex(matrix, -0.866F * vert2, vert1, -0.5F * vert2).setColor(1.0F, 1.0F, 1.0F, 0F).setLight(packedLight);
        }

        poseStack.popPose();
    }

    @Override public RenderType getRenderType() { return AMAT; }

    public static class Provider extends ParticleProviderBase<NbtParticleOptions> {

        @Override
        public void createParticle(NbtParticleOptions options, double x, double y, double z, double xd, double yd, double zd, ClientLevel level, RandomSource random, ParticleStatus particleStatus) {
            CompoundTag tag = options.tag;

            float scale = tag.getFloat("scale");

            ParticleEngineNT.INSTANCE.add(new AmatFlashParticle(level, x, y, z, scale));
        }
    }
}
