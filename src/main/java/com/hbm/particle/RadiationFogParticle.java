package com.hbm.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Random;

public class RadiationFogParticle extends TextureSheetParticle {

    public RadiationFogParticle(ClientLevel level, double x, double y, double z) {
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

        this.alpha = (float) Math.sin(age * Math.PI / (400F)) * 0.125F;

        Random rand = new Random(50);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        RenderSystem.depthMask(false);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
        RenderSystem.setShaderColor(0.85F, 0.9F, 0.5F, this.alpha);

        for (int i = 0; i < 25; i++) {

            float dX = (float) ((rand.nextGaussian() - 1F) * 2.5F);
            float dY = (float) ((rand.nextGaussian() - 1F) * 0.15F);
            float dZ = (float) ((rand.nextGaussian() - 1F) * 2.5F);

            float pX = (float) ((float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x) + dX + rand.nextGaussian() * 0.5);
            float pY = (float) ((float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y) + dY + rand.nextGaussian() * 0.5);
            float pZ = (float) ((float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z) + dZ + rand.nextGaussian() * 0.5);
            float size = rand.nextFloat() * quadSize;

            Vector3f l = new Vector3f(camera.getLeftVector()).mul(size);
            Vector3f u = new Vector3f(camera.getUpVector()).mul(size);

            float u0 = sprite.getU0();
            float u1 = sprite.getU1();
            float v0 = sprite.getV0();
            float v1 = sprite.getV1();

            Tesselator tess = Tesselator.getInstance();
            BufferBuilder buf = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            buf.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z).setUv(u1, v1);
            buf.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z).setUv(u1, v0);
            buf.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z).setUv(u0, v0);
            buf.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z).setUv(u0, v1);
            BufferUploader.drawWithShader(buf.buildOrThrow());
        }

        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ModParticleRenderTypes.NONE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Provider(SpriteSet spriteSet) {
            ModParticles.RAD_FOG_SPRITES = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new RadiationFogParticle(level, x, y, z);
        }
    }
}
