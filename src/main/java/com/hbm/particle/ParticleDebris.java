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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;

public class ParticleDebris extends TextureSheetParticle {

    private final BlockRenderDispatcher blockRenderer;
    public WorldInAJar world;

    public ParticleDebris(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
        this.world = new WorldInAJar(4, 4, 4);
        this.lifetime = 100;
        this.gravity = 0.05F;
    }

    public void setDeltaMovement(Vec3 deltaMovement) {
        this.xd = deltaMovement.x;
        this.yd = deltaMovement.y;
        this.zd = deltaMovement.z;
    }

    @Override
    public void tick() {
        super.tick();
        this.yd -= gravity;
        this.move(this.xd, this.yd, this.zd);

        if(this.age > 5) this.hasPhysics = false;

        ParticleRocketFlame fx = new ParticleRocketFlame(
                this.level,
                this.x, this.y, this.z,
                ModParticles.ROCKET_FLAME_SPRITES
        );
        fx.resetPrevPos();
        fx.setMaxAge(50);
        Minecraft.getInstance().particleEngine.add(fx);

        if (this.onGround) {
            this.remove();
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        PoseStack pose = new PoseStack();

        double camX = camera.getPosition().x;
        double camY = camera.getPosition().y;
        double camZ = camera.getPosition().z;

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
                            240,
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
            return new ParticleDebris(level, x, y, z);

        }
    }
}
