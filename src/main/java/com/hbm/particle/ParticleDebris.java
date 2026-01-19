package com.hbm.particle;

import com.hbm.wiaj.WorldInAJar;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;

public class ParticleDebris extends TextureSheetParticle {

    private final BlockRenderDispatcher blockRenderer;
    private WorldInAJar world;
    private static final RandomSource rng = RandomSource.create();

    public double prevRotationPitch;
    public double prevRotationYaw;
    public double rotationPitch;
    public double rotationYaw;

    public ParticleDebris(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    public ParticleDebris(ClientLevel level, double x, double y, double z, double mx, double my, double mz) {
        super(level, x, y, z);
        double mult = 3;
        this.xd = mx * mult;
        this.yd = my * mult;
        this.zd = mz * mult;
        this.lifetime = 100;
        this.gravity = 0.15F;
        this.hasPhysics = false;
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    public ParticleDebris setWorldInAJar(WorldInAJar world) {
        this.world = world;
        return this;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age > 5) this.hasPhysics = true;

        rng.setSeed(this.hashCode());
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
        this.rotationPitch += rng.nextFloat() * 10;
        this.rotationYaw += rng.nextFloat() * 10;

        if (this.hashCode() % 3 == 0) {
            RocketFlameParticle particle = new RocketFlameParticle(this.level, this.x, this.y, this.z);
            particle.setScale(1F * Math.max(world.sizeY, 6) / 16F);
            particle.resetPrevPos();
            particle.setMaxAge(50);
            Minecraft.getInstance().particleEngine.add(particle);
        }

        this.yd -= gravity;

        this.move(this.xd, this.yd, this.zd);

        this.age++;
        if (this.onGround) this.remove();
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        if (this.world == null) return;
        Vec3 camPos = camera.getPosition();

        double pX = Mth.lerp(partialTicks, this.xo, this.x) - camPos.x;
        double pY = Mth.lerp(partialTicks, this.yo, this.y) - camPos.y;
        double pZ = Mth.lerp(partialTicks, this.zo, this.z) - camPos.z;

        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();

        poseStack.translate(pX, pY, pZ);

        float interpPitch = (float) Mth.lerp(partialTicks, prevRotationPitch, rotationPitch);
        float interpYaw = (float) Mth.lerp(partialTicks, prevRotationYaw, rotationYaw);
        poseStack.mulPose(Axis.YP.rotationDegrees(interpPitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(interpYaw));

        poseStack.translate(-world.sizeX / 2.0, -world.sizeY / 2.0, -world.sizeZ / 2.0);

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        int packedLight = this.getLightColor(partialTicks);

        for (int ix = 0; ix < world.sizeX; ix++) {
            for (int iy = 0; iy < world.sizeY; iy++) {
                for (int iz = 0; iz < world.sizeZ; iz++) {
                    BlockState state = world.getBlockState(new BlockPos(ix, iy, iz));

                    if (!state.isAir()) {
                        poseStack.pushPose();
                        poseStack.translate(ix, iy, iz);

                        try {
                            blockRenderer.renderSingleBlock(
                                    state,
                                    poseStack,
                                    bufferSource,
                                    packedLight,
                                    OverlayTexture.NO_OVERLAY,
                                    ModelData.EMPTY,
                                    null
                            );
                        } catch (Exception ignored) { }

                        poseStack.popPose();
                    }
                }
            }
        }

        bufferSource.endBatch();
        poseStack.popPose();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomRenderType.NONE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new ParticleDebris(level, x, y, z);
        }
    }
}
