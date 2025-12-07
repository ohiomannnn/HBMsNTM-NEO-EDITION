package com.hbm.particle;

import com.hbm.HBMsNTM;
import com.hbm.render.CustomRenderTypes;
import com.hbm.util.old.TessColorUtil;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Random;

public class ParticleRadiationFog extends TextureSheetParticle {

    private static final ResourceLocation RAD = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/particle/rad_fog.png");

    public ParticleRadiationFog(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.lifetime = 100 + this.random.nextInt(40);
        this.quadSize = 7.5F;

        this.rCol = this.gCol = this.bCol = 0;
        this.setSpriteFromAge(ModParticles.RAD_FOG_SPRITES);
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
        Vec3 cameraPosition = camera.getPosition();

        this.alpha = (float) Math.sin(age * Math.PI / (400F)) * 0.25F;

        int color = TessColorUtil.getColorRGBA_F( 0.85F, 0.9F, 0.5F, this.alpha);

        Random rand = new Random(50);

        for (int i = 0; i < 25; i++) {

            float dX = (float) ((rand.nextGaussian() - 1F) * 2.5F);
            float dY = (float) ((rand.nextGaussian() - 1F) * 0.15F);
            float dZ = (float) ((rand.nextGaussian() - 1F) * 2.5F);
            float size = rand.nextFloat() * quadSize;

            float pX = (float) ((float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x) + dX + rand.nextGaussian() * 0.5);
            float pY = (float) ((float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y) + dY + rand.nextGaussian() * 0.5);
            float pZ = (float) ((float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z) + dZ + rand.nextGaussian() * 0.5);

            renderQuad(consumer, camera, pX, pY, pZ, color, size);
        }
    }

    private void renderQuad(VertexConsumer consumer, Camera camera, float pX, float pY, float pZ, int color, float size) {

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        Vector3f l = new Vector3f(camera.getLeftVector()).mul(size);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(size);

        consumer.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z)
                .setColor(color)
                .setUv(u1, v1)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z)
                .setColor(color)
                .setUv(u1, v0)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z)
                .setColor(color)
                .setUv(u0, v0)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z)
                .setColor(color)
                .setUv(u0, v1)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomRenderType.FOG;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Provider(SpriteSet spriteSet) {
            ModParticles.RAD_FOG_SPRITES = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleRadiationFog(level, x, y, z);
        }
    }
}
