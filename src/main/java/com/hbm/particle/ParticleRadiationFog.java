package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;
import org.joml.Vector3f;

import java.util.Random;

public class ParticleRadiationFog extends TextureSheetParticle {

    private final Random rand = new Random();
    private int maxAge;

    public ParticleRadiationFog(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        this.maxAge = 100 + this.rand.nextInt(40);
//        this.rCol = this.gCol = this.bCol = 0.0F;
        this.quadSize = 7.5F;
        this.setSpriteFromAge(sprites);
    }

    public ParticleRadiationFog(ClientLevel level, double x, double y, double z, float red, float green, float blue, float scale, SpriteSet sprites) {
        super(level, x, y, z);
        this.maxAge = 100 + this.rand.nextInt(40);
        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.quadSize = scale;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (maxAge < 400) {
            maxAge = 400;
        }

        this.age++;

        if (this.age >= maxAge) {
            this.remove();
        }

        this.xd *= 0.96;
        this.yd *= 0.96;
        this.zd *= 0.96;

        if (this.onGround) {
            this.xd *= 0.7;
            this.zd *= 0.7;
        }
    }

    @Override
    public void render(VertexConsumer vc, Camera camera, float partialTicks) {
        float px = (float)(xo + (this.x - xo) * partialTicks - camera.getPosition().x());
        float py = (float)(yo + (this.y - yo) * partialTicks - camera.getPosition().y());
        float pz = (float)(zo + (this.z - zo) * partialTicks - camera.getPosition().z());

        float alpha = (float)Math.sin((this.age + partialTicks) * Math.PI / 400F) * 0.125F;
        if (alpha <= 0) return;

        int color = FastColor.ARGB32.color((int)(alpha * 255), (int)(rCol * 255), (int)(gCol * 255), (int)(bCol * 255));

        float size = this.quadSize;
        int light = 0xF000F0;
        int overlay = 0;

        Vector3f left = camera.getLeftVector();
        Vector3f up = camera.getUpVector();

        float leftX = left.x() * size;
        float leftY = left.y() * size;
        float leftZ = left.z() * size;
        float upX = up.x() * size;
        float upY = up.y() * size;
        float upZ = up.z() * size;

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();

        rand.setSeed(50);
        for (int i = 0; i < 25; i++) {
            float dx = (float)((rand.nextGaussian() - 1D) * 2.5D);
            float dy = (float)((rand.nextGaussian() - 1D) * 0.15D);
            float dz = (float)((rand.nextGaussian() - 1D) * 2.5D);

            float cx = px + dx;
            float cy = py + dy;
            float cz = pz + dz;

            float x0 = cx - leftX - upX;
            float y0 = cy - leftY - upY;
            float z0 = cz - leftZ - upZ;

            float x1 = cx - leftX + upX;
            float y1 = cy - leftY + upY;
            float z1 = cz - leftZ + upZ;

            float x2 = cx + leftX + upX;
            float y2 = cy + leftY + upY;
            float z2 = cz + leftZ + upZ;

            float x3 = cx + leftX - upX;
            float y3 = cy + leftY - upY;
            float z3 = cz + leftZ - upZ;

            vc.addVertex(x0, y0, z0, color, u1, v1, overlay, light, 0, 1, 0);
            vc.addVertex(x1, y1, z1, color, u1, v0, overlay, light, 0, 1, 0);
            vc.addVertex(x2, y2, z2, color, u0, v0, overlay, light, 0, 1, 0);
            vc.addVertex(x3, y3, z3, color, u0, v1, overlay, light, 0, 1, 0);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleRadiationFog(level, x, y, z, spriteSet);
        }
    }
}
