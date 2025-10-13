package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleFoam extends TextureSheetParticle {

    private int age;
    private int maxAge;
    private float baseScale = 1.0F;
    private float maxScale = 1.5F;

    private final List<TrailPoint> trail = new ArrayList<>();
    private int trailLength = 15;
    private float initialVelocity;
    private float buoyancy = 0.05F;
    private float jitter = 0.15F;
    private float drag = 0.96F;
    private int explosionPhase; // 0=burst, 1=peak, 2=settle
    private final Random urandom = new Random();
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

        this.maxAge = 60 + this.random.nextInt(60);
        this.gravity = 0.005F + this.random.nextFloat() * 0.015F;

        this.initialVelocity = 2.0F + this.random.nextFloat() * 3.0F;
        this.yd = this.initialVelocity;

        double angle = this.random.nextDouble() * Math.PI * 2;
        double strength = this.random.nextDouble() * 0.5;
        this.xd = Math.cos(angle) * strength;
        this.zd = Math.sin(angle) * strength;

        this.explosionPhase = 0;
        this.quadSize = 0.3F + this.random.nextFloat() * 0.7F;
        this.alpha = 1.0F;
    }

    public void setBaseMaxAge(int a) { this.maxAge = a; }
    public void setBaseScale(float f) { this.baseScale = f; }
    public void setMaxScale(float f) { this.maxScale = f; }
    public void setTrailLength(int len) { this.trailLength = len; }
    public void setBuoyancy(float f) { this.buoyancy = f; }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        trail.add(0, new TrailPoint(x, y, z));
        while (trail.size() > trailLength) trail.remove(trail.size() - 1);

        ++age;
        if (age >= maxAge) {
            this.remove();
            return;
        }

        float phaseRatio = (float) age / (float) maxAge;
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

        xd *= drag;
        yd *= drag;
        zd *= drag;

        this.move(xd, yd, zd);

        if (this.onGround) this.remove();
    }

    @Override
    public void render(VertexConsumer vc, Camera camera, float pt) {
        Vec3 cam = camera.getPosition();
        double px = this.x - cam.x;
        double py = this.y - cam.y;
        double pz = this.z - cam.z;

        renderFoam(vc, camera, pt, px, py, pz, quadSize, alpha);

        for (int i = 1; i < trail.size(); i++) {
            TrailPoint p = trail.get(i);
            float tScale = quadSize * (1.0F - (float)i / trailLength);
            float tAlpha = alpha * (1.0F - (float)i / trailLength) * 0.7F;
            renderFoam(vc, camera, pt,
                    p.x - cam.x,
                    p.y - cam.y,
                    p.z - cam.z,
                    tScale, tAlpha);
        }
    }


    private void renderFoam(VertexConsumer vc, Camera camera, float pt, double x, double y, double z, float scale, float alpha) {
        urandom.setSeed((long)(x * 1000) + (long)(y * 10000) + (long)(z * 100000));
        int bubbles = explosionPhase == 0 ? 8 : (explosionPhase == 1 ? 6 : 4);

        Quaternionf q = camera.rotation();
        Matrix4f mat = new Matrix4f().rotation(q);

        float minU = getU0();
        float maxU = getU1();
        float minV = getV0();
        float maxV = getV1();

        for (int i = 0; i < bubbles; i++) {
            float w = 0.9F + urandom.nextFloat() * 0.1F;
            float bubbleScale = scale * (urandom.nextFloat() * 0.5F + 0.75F);
            float offset = explosionPhase == 0 ? 0.4F : (explosionPhase == 1 ? 0.6F : 0.9F);

            float ox = (float) (urandom.nextGaussian() * offset);
            float oy = (float) (urandom.nextGaussian() * offset * 0.7F);
            float oz = (float) (urandom.nextGaussian() * offset);

            Vector3f[] corners = new Vector3f[] {
                    new Vector3f(-1, -1, 0),
                    new Vector3f(-1,  1, 0),
                    new Vector3f( 1,  1, 0),
                    new Vector3f( 1, -1, 0)
            };

            for (Vector3f c : corners) {
                c.mul(bubbleScale);
                c.mulPosition(mat);
                c.add((float)x + ox, (float)y + oy, (float)z + oz);
            }

            vc.addVertex(corners[0]).setUv(maxU, maxV).setColor(w, w, w, alpha).setLight(0xF000F0).setNormal(0, 1, 0);
            vc.addVertex(corners[1]).setUv(maxU, minV).setColor(w, w, w, alpha).setLight(0xF000F0).setNormal(0, 1, 0);
            vc.addVertex(corners[2]).setUv(minU, minV).setColor(w, w, w, alpha).setLight(0xF000F0).setNormal(0, 1, 0);
            vc.addVertex(corners[3]).setUv(minU, maxV).setColor(w, w, w, alpha).setLight(0xF000F0).setNormal(0, 1, 0);
        }
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
