package com.hbm.particle;

import com.hbm.HBMsNTM;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
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

    public ParticleRadiationFog(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        this.lifetime = 100 + this.random.nextInt(40);
        this.quadSize = 7.5F;

        this.rCol = this.gCol = this.bCol = 0;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (lifetime < 400) {
            lifetime = 400;
        }

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
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Quaternionf quaternionf = new Quaternionf(camera.rotation());
        Vec3 camPos = camera.getPosition();

        this.rCol = 0.85F;
        this.gCol = 0.9F;
        this.bCol = 0.5F;

        float interpAge = this.age + partialTicks;
        this.alpha = (float) Math.sin(interpAge * Math.PI / 400f) * 0.125f;

        Random urandom = new Random(50);

        float baseX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - camPos.x());
        float baseY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - camPos.y());
        float baseZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - camPos.z());

        for (int i = 0; i < 25; i++) {
            double dX = (urandom.nextGaussian() - 1.0) * 2.5;
            double dY = (urandom.nextGaussian() - 1.0) * 0.15;
            double dZ = (urandom.nextGaussian() - 1.0) * 2.5;
            double size = urandom.nextDouble() * this.quadSize;

            float pX = baseX + (float) dX + (float) (urandom.nextGaussian() * 0.5);
            float pY = baseY + (float) dY + (float) (urandom.nextGaussian() * 0.5);
            float pZ = baseZ + (float) dZ + (float) (urandom.nextGaussian() * 0.5);

            float U0 = this.getU0();
            float U1 = this.getU1();
            float V0 = this.getV0();
            float V1 = this.getV1();

            this.renderVertex(consumer, quaternionf, pX, pY, pZ,  1.0F, -1.0F, (float)size, U1, V1, 240);
            this.renderVertex(consumer, quaternionf, pX, pY, pZ,  1.0F,  1.0F, (float)size, U1, V0, 240);
            this.renderVertex(consumer, quaternionf, pX, pY, pZ, -1.0F,  1.0F, (float)size, U0, V0, 240);
            this.renderVertex(consumer, quaternionf, pX, pY, pZ, -1.0F, -1.0F, (float)size, U0, V1, 240);
        }
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
