package com.hbm.particle;

import com.hbm.HBMsNTM;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;

public class ParticleRadiationFog extends TextureSheetParticle {

    private int age;
    private int maxAge;

    public ParticleRadiationFog(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        this.maxAge = 100 + this.random.nextInt(40);
        this.quadSize = 7.5F;

        this.rCol = this.gCol = this.bCol = 0;
        this.setSpriteFromAge(sprites);
    }

    public ParticleRadiationFog(ClientLevel level, double x, double y, double z, float red, float green, float blue, float scale, SpriteSet sprites) {
        super(level, x, y, z);
        this.maxAge = 100 + this.random.nextInt(40);

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
        if (this.age >= this.maxAge) {
            this.remove();
        }

        this.xd *= 0.9599999785423279D;
        this.yd *= 0.9599999785423279D;
        this.zd *= 0.9599999785423279D;

        if (this.onGround) {
            this.xd *= 0.699999988079071D;
            this.zd *= 0.699999988079071D;
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        Quaternionf q = new Quaternionf(camera.rotation());
        Vec3 camPos = camera.getPosition();

        this.rCol = 0.85F;
        this.gCol = 0.9F;
        this.bCol = 0.5F;
        float t = (float) this.age / 400F;
        this.alpha = Math.max(0F, (float)Math.sin(t * Math.PI)) * 0.5F;

        HBMsNTM.LOGGER.info("alpha = {}", this.alpha);

        Random rand = new Random(50);

        for (int i = 0; i < 25; i++) {

            double dX = (rand.nextGaussian() - 1D) * 2.5D;
            double dY = (rand.nextGaussian() - 1D) * 0.15D;
            double dZ = (rand.nextGaussian() - 1D) * 2.5D;
            double size = rand.nextDouble() * this.quadSize;

            float px = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x + dX);
            float py = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y + dY);
            float pz = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z + dZ);

            renderQuadWithSize(buffer, q, px, py, pz, (float)size, 240);
        }
    }

    private void renderQuadWithSize(VertexConsumer buffer, Quaternionf q, float x, float y, float z, float size, float partialTicks) {
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int light = this.getLightColor(partialTicks);

        renderVertex(buffer, q, x, y, z, 1.0F, -1.0F, size, u1, v1, light);
        renderVertex(buffer, q, x, y, z, 1.0F,  1.0F, size, u1, v0, light);
        renderVertex(buffer, q, x, y, z,-1.0F,  1.0F, size, u0, v0, light);
        renderVertex(buffer, q, x, y, z,-1.0F, -1.0F, size, u0, v1, light);
    }

    private void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = (new Vector3f(xOffset, yOffset, 0.0F)).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(packedLight);
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
