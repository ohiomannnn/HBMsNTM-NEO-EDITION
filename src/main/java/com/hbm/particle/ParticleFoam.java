package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleFoam extends TextureSheetParticle {

    private float baseScale = 1.0F;
    private float maxScale = 1.5F;

    // Parameters for the trail effect
    private List<TrailPoint> trail = new ArrayList<>();
    private int trailLength = 15;
    private float initialVelocity;
    private float buoyancy = 0.05F;
    private float jitter = 0.15F;
    private float drag = 0.96F;
    private int explosionPhase; // 0=burst up, 1=peak, 2=settle

    private static class TrailPoint {
        double x, y, z;

        public TrailPoint(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public ParticleFoam(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z, 0, 0, 0);
        this.setSpriteFromAge(ModParticles.ROCKET_FLAME_SPRITES);

        lifetime = 60 + random.nextInt(60);
        gravity = 0.005F + random.nextFloat() * 0.015F;

        initialVelocity = 2.0F + random.nextFloat() * 3.0F;
        yd = initialVelocity;

        double angle = random.nextDouble() * Math.PI * 2;
        double strength = random.nextDouble() * 0.5;
        xd = Math.cos(angle) * strength;
        zd = Math.sin(angle) * strength;

        explosionPhase = 0; // Start in burst phase

        quadSize = 0.3F + random.nextFloat() * 0.7F;
    }

    public void setBaseScale(float f) { this.baseScale = f; }
    public void setMaxScale(float f) { this.maxScale = f; }
    public void setTrailLength(int length) { this.trailLength = length; }
    public void setBuoyancy(float buoyancy) { this.buoyancy = buoyancy; }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        trail.add(0, new TrailPoint(x, y, z));

        while (trail.size() > trailLength) {
            trail.remove(trail.size() - 1);
        }

        // Update age and phase
        ++this.age;

        if (this.age == this.lifetime) {
            this.remove();
        }

        float phaseRatio = (float) age / (float) lifetime;
        if (phaseRatio < 0.3F) {
            explosionPhase = 0;

            if (phaseRatio < 0.15F) {
                yd += buoyancy * 6.0F;
            } else {
                yd += buoyancy * (1.0F - (phaseRatio / 0.3F)) * 2.0F;
            }

            quadSize = baseScale + (maxScale - baseScale) * (phaseRatio / 0.3F);
        } else if (phaseRatio < 0.6F) {
            explosionPhase = 1;
            yd *= 0.98F;

            quadSize = maxScale;
        } else {
            explosionPhase = 2;
            yd -= gravity;

            quadSize = maxScale * (1.0F - ((phaseRatio - 0.6F) / 0.4F) * 0.7F);
        }

        alpha = 0.8F * (1.0F - phaseRatio * phaseRatio);

        xd += (random.nextFloat() - 0.5F) * jitter;
        zd += (random.nextFloat() - 0.5F) * jitter;

        // drag like ninja drags the low taper fade
        xd *= drag;
        yd *= drag;
        zd *= drag;

        this.move(this.xd, yd, this.zd);

        // Kill particle if it hits ground
        if (this.onGround) {
            this.remove();
        }
    }

    // ty kercig cuz id kms lol

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        renderFoamBubbles(consumer, camera, this.x, this.y, this.z, this.quadSize, this.alpha, partialTicks);

        for (int i = 1; i < trail.size(); i++) {
            TrailPoint point = trail.get(i);
            float trailScale = quadSize * (1.0F - (float)i / trailLength);
            float trailAlpha = alpha * (1.0F - (float)i / trailLength) * 0.7F;

            renderFoamBubbles(consumer, camera, point.x, point.y, point.z, trailScale, trailAlpha, partialTicks);
        }
    }

    private void renderFoamBubbles(VertexConsumer consumer, Camera camera, double x, double y, double z, float scale, float alpha, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        Random urandom = new Random(this.hashCode() + (long)(x * 100) + (long)(y * 10) + (long)z);

        int bubbleCount = explosionPhase == 0 ? 8 : (explosionPhase == 1 ? 6 : 4);

        for (int i = 0; i < bubbleCount; i++) {
            float whiteness = 0.9F + urandom.nextFloat() * 0.1F;

            this.rCol = this.bCol = this.gCol = whiteness;

            float bubbleScale = scale * (urandom.nextFloat() * 0.5F + 0.75F);
            float offset = explosionPhase == 0 ? 0.4F : (explosionPhase == 1 ? 0.6F : 0.9F);

            float pX = (float)((x - cameraPosition.x) + urandom.nextGaussian() * offset);
            float pY = (float)((y - cameraPosition.y) + urandom.nextGaussian() * offset * 0.7F);
            float pZ = (float)((z - cameraPosition.z) + urandom.nextGaussian() * offset);

            Vector3f up = new Vector3f(camera.getUpVector());
            Vector3f left = new Vector3f(camera.getLeftVector());

            renderQuad(consumer, pX, pY, pZ, up, left, bubbleScale, alpha, this.getLightColor(partialTicks));
        }
    }


    private void renderQuad(VertexConsumer consumer, float cx, float cy, float cz, Vector3f up, Vector3f left, float scale, float alpha, int brightness) {

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        Vector3f l = new Vector3f(left).mul(scale);
        Vector3f u = new Vector3f(up).mul(scale);

        consumer.addVertex(cx - l.x - u.x, cy - l.y - u.y, cz - l.z - u.z)
                .setUv(u1, v1)
                .setColor(this.rCol, this.bCol, this.gCol, alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(brightness);
        consumer.addVertex(cx - l.x + u.x, cy - l.y + u.y, cz - l.z + u.z)
                .setUv(u1, v0)
                .setColor(this.rCol, this.bCol, this.gCol, alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(brightness);
        consumer.addVertex(cx + l.x + u.x, cy + l.y + u.y, cz + l.z + u.z)
                .setUv(u0, v0)
                .setColor(this.rCol, this.bCol, this.gCol, alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(brightness);
        consumer.addVertex(cx + l.x - u.x, cy + l.y - u.y, cz + l.z - u.z)
                .setUv(u0, v1)
                .setColor(this.rCol, this.bCol, this.gCol, alpha)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(brightness);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public TextureSheetParticle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double dx, double dy, double dz) {
            return new ParticleFoam(world, x, y, z);
        }
    }
}
