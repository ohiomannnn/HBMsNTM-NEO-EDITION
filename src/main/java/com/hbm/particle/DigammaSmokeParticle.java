package com.hbm.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class DigammaSmokeParticle extends TextureSheetParticle {

    public DigammaSmokeParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setSpriteFromAge(ModParticles.BASE_PARTICLE_SPRITES);
        this.lifetime = 100 + random.nextInt(40);
        this.hasPhysics = false;

        this.quadSize = 5;

        this.rCol = 0.5F + random.nextFloat() * 0.2F;
        this.gCol = 0.0F;
        this.bCol = 0.0F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        ++this.age;

        if (this.age == this.lifetime) {
            this.remove();
        }

        this.xd *= 0.99D;
        this.yd *= 0.99D;
        this.zd *= 0.99D;

        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ModParticles.BASE);

        this.alpha = Math.clamp(1 - ((this.age + partialTicks) / (float)this.lifetime), 0.0F, 1.0F);

        RenderSystem.setShaderColor(this.rCol, this.gCol, this.bCol, alpha);

        float u0 = 0, v0 = 0;
        float u1 = 1, v1 = 1;

        Vector3f l = new Vector3f(camera.getLeftVector()).mul(this.quadSize);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(this.quadSize);

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buf.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z).setUv(u1, v1);
        buf.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z).setUv(u1, v0);
        buf.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z).setUv(u0, v0);
        buf.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z).setUv(u0, v1);
        BufferUploader.drawWithShader(buf.buildOrThrow());

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new DigammaSmokeParticle(level, x, y, z);
        }
    }
}
