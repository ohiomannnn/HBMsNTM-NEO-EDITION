package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SparkParticle extends TextureSheetParticle {

    private final List<double[]> steps = new ArrayList<>();
    private final int thresh;

    public SparkParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.thresh = 4 + random.nextInt(3);
        this.steps.add(new double[] { xd, yd, zd });
        this.lifetime = 20 + random.nextInt(10);
        this.gravity = 0.5F;
    }

    public SparkParticle(ClientLevel level, double x, double y, double z) {
        this(level, x, y, z, 0, 0, 0);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        this.steps.add(new double[] { xd, yd, zd });

        while (steps.size() > thresh) {
            steps.remove(0);
        }

        this.yd -= 0.04D * (double) this.gravity;
        double lastY = this.yd;
        this.move(xd, yd, zd);

        if (this.onGround) {
            this.onGround = false;
            yd = -lastY * 0.8D;
        }
    }

    @Override
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {

        if (steps.size() < 2) return;

        Vec3 cameraPosition = camera.getPosition();
        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

        BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

        VertexConsumer consumer = buffer.getBuffer(RenderType.debugLineStrip(3F));

        double[] prev = new double[] { pX, pY, pZ };

        for (int i = 1; i < steps.size(); i++) {

            double[] curr = new double[] { prev[0] + steps.get(i)[0], prev[1] + steps.get(i)[1], prev[2] + steps.get(i)[2] };

            consumer.addVertex((float) prev[0], (float) prev[1], (float) prev[2]).setColor(1F, 1F, 1F, 1F);

            prev = curr;
        }

        buffer.endBatch();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ModParticleRenderTypes.NONE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new SparkParticle(level, x, y, z);
        }
    }
}
