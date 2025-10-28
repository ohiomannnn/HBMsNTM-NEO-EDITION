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
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.Random;

public class ParticleDebris extends TextureSheetParticle {

    private final BlockRenderDispatcher blockRenderer;
    private WorldInAJar world;
    private static final Random rng = new Random();

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
        this.world = new WorldInAJar(4, 4, 4);
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
        this.oRoll = this.roll;
        this.roll += rng.nextFloat() * 10;

        if (this.hashCode() % 3 == 0) {
            ParticleRocketFlame fx = new ParticleRocketFlame(
                    this.level,
                    this.x, this.y, this.z,
                    ModParticles.ROCKET_FLAME_SPRITES
            );
            fx.setScale(1F * Math.max(world.sizeY, 6) / 16F);
            fx.resetPrevPos();
            fx.setMaxAge(50);
            Minecraft.getInstance().particleEngine.add(fx);
        }

        this.yd -= gravity;

        this.move(this.xd, this.yd, this.zd);

        this.age++;
        if (this.onGround) this.remove();
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        PoseStack pose = new PoseStack();

        double camX = camera.getPosition().x;
        double camY = camera.getPosition().y;
        double camZ = camera.getPosition().z;

        BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        int packedLight = LightTexture.pack(blockLight, skyLight);

        double interpX = Mth.lerp(partialTicks, this.xo, this.x);
        double interpY = Mth.lerp(partialTicks, this.yo, this.y);
        double interpZ = Mth.lerp(partialTicks, this.zo, this.z);

        pose.pushPose();
        pose.translate(interpX - camX, interpY - camY, interpZ - camZ);

        pose.translate(world.sizeX / -2.0D, world.sizeY / -2.0D, world.sizeZ / -2.0D);

        float angle = (this.oRoll + (this.roll - this.oRoll) * partialTicks);
        pose.translate(world.sizeX / 2.0D, world.sizeY / 2.0D, world.sizeZ / 2.0D);
        pose.mulPose(Axis.YP.rotationDegrees(angle));
        pose.mulPose(Axis.XP.rotationDegrees(angle * 0.5f));
        pose.translate(-world.sizeX / 2.0D, -world.sizeY / 2.0D, -world.sizeZ / 2.0D);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        for (int ix = 0; ix < world.sizeX; ix++) {
            for (int iy = 0; iy < world.sizeY; iy++) {
                for (int iz = 0; iz < world.sizeZ; iz++) {
                    BlockState state = world.getBlock(ix, iy, iz);
                    if (!state.isAir()) {
                        pose.pushPose();
                        pose.translate(ix, iy, iz);
                        blockRenderer.getModelRenderer().tesselateBlock(
                                level,
                                blockRenderer.getBlockModel(state),
                                state,
                                pos.offset(ix, iy, iz),
                                pose,
                                bufferSource.getBuffer(RenderType.cutout()),
                                false,
                                level.random,
                                42L,
                                packedLight,
                                ModelData.EMPTY,
                                RenderType.cutout()
                        );

                        pose.popPose();
                    }
                }
            }
        }

        pose.popPose();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new ParticleDebris(level, x, y, z, 0, 0, 0);
        }
    }
}
