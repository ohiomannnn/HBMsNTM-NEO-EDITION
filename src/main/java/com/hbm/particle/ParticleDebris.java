package com.hbm.particle;

import com.hbm.wiaj.WorldInAJar;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.Random;

public class ParticleDebris extends TextureSheetParticle {

    private final BlockRenderDispatcher blockRenderer;
    private WorldInAJar world;
    private static Random rng = new Random();

    public ParticleDebris(ClientLevel level, double x, double y, double z, double mx, double my, double mz) {
        super(level, x, y, z);
        double mult = 3;
        this.xd = mx * mult;
        this.yd = my * mult;
        this.zd = mz * mult;
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
        this.world = new WorldInAJar(4, 4, 4);
        this.lifetime = 100;
        this.gravity = 0.15F;
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
        this.roll += rng.nextFloat() * 10 * (Mth.DEG_TO_RAD);

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
        int skyLight   = level.getBrightness(LightLayer.SKY, pos);
        int packedLight = LightTexture.pack(blockLight, skyLight);

        pose.pushPose();
        pose.translate(this.x - camX, this.y - camY, this.z - camZ);

        float angle = (this.age + partialTicks) * 10f;
        pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(angle));
        pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(angle * 0.5f));

        for (int x = 0; x < world.sizeX; x++) {
            for (int y = 0; y < world.sizeY; y++) {
                for (int z = 0; z < world.sizeZ; z++) {
                    BlockState state = world.getBlock(x, y, z);
                    if (state.isAir()) continue;

                    pose.pushPose();
                    pose.translate(
                            x - world.sizeX / 2.0 + 1.5,
                            y - world.sizeY / 2.0 + 1.25,
                            z - world.sizeZ / 2.0 + 1.5
                    );

                    blockRenderer.renderSingleBlock(
                            state,
                            pose,
                            Minecraft.getInstance().renderBuffers().bufferSource(),
                            packedLight,
                            OverlayTexture.NO_OVERLAY,
                            ModelData.EMPTY,
                            RenderType.TRANSLUCENT
                    );

                    pose.popPose();
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
