package com.hbm.particle;

import com.hbm.HBMsNTM;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class HazeParticle extends TextureSheetParticle {

    private static final ResourceLocation HAZE = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/haze.png");

    public HazeParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.lifetime = 600 + random.nextInt(100);

        this.quadSize = 10F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.age++;

        if (this.age >= this.lifetime) {
            this.remove();
        }

        this.xd *= 0.96D;
        this.yd *= 0.96D;
        this.zd *= 0.96D;

        if (this.onGround) {
            this.xd *= 0.7D;
            this.zd *= 0.7D;
        }

        int x = (int) (this.x + random.nextInt(15) - 7);
        int z = (int) (this.y + random.nextInt(15) - 7);
        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
        Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.LAVA, x + random.nextDouble(), y + 0.1, z + random.nextDouble(), 0.0, 0.0, 0.0);
    }

    @Override
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {
        PoseStack poseStack = new PoseStack();
        Vec3 cameraPosition = camera.getPosition();

        this.alpha = (float) Math.sin(age * Math.PI / (400F)) * 0.25F;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        RenderSystem.depthMask(true);
        RenderSystem.setShaderTexture(0, HAZE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha * 0.1F);

        RandomSource random = RandomSource.create(50);

        poseStack.pushPose();
        for (int i = 0; i < 25; i++) {
            double dX = random.nextGaussian() * 2.5D;
            double dY = random.nextGaussian() * 0.15D;
            double dZ = random.nextGaussian() * 2.5D;
            float size = (random.nextFloat() * 0.25F + 0.75F) * quadSize;

            poseStack.translate(dX, dY, dZ);

            float pX = (float) ((float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x) + random.nextGaussian() * 0.5);
            float pY = (float) ((float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y) + random.nextGaussian() * 0.5);
            float pZ = (float) ((float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z) + random.nextGaussian() * 0.5);

            Vector3f l = new Vector3f(camera.getLeftVector()).mul(size);
            Vector3f u = new Vector3f(camera.getUpVector()).mul(size);

            Matrix4f matrix = poseStack.last().pose();

            Tesselator tess = Tesselator.getInstance();
            BufferBuilder buf = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            buf.addVertex(matrix, pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z).setUv(1, 1);
            buf.addVertex(matrix, pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z).setUv(1, 0);
            buf.addVertex(matrix, pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z).setUv(0, 0);
            buf.addVertex(matrix, pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z).setUv(0, 1);
            BufferUploader.drawWithShader(buf.buildOrThrow());
        }
        poseStack.popPose();

        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.disableBlend();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomRenderType.NONE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Provider(SpriteSet sprites) {
            ModParticles.HAZE_SPRITES = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new HazeParticle(level, x, y, z);
        }
    }
}
