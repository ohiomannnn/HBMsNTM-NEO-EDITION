package com.hbm.particle;

import com.hbm.main.NuclearTechMod;
import com.hbm.particle.engine.ParticleEngineNT;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.render.NtmRenderTypes;
import com.hbm.util.old.TessColorUtil;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class ContrailParticle extends ParticleNT {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/particle/contrail.png");

    public ContrailParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);

        this.lifetime = 100 + this.random.nextInt(40);
        this.rCol = this.gCol = this.bCol = 0F;
        this.quadSize = 1F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.alpha = 1 - ((float) age / (float) lifetime);

        if(this.age++ >= this.lifetime) this.remove();
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 camPos = camera.getPosition();

        RandomSource urandom = RandomSource.create(this.hashCode());

        for(int i = 0; i < 6; i++) {

            float mod = urandom.nextFloat() * 0.2F + 0.2F;
            float scale = (this.alpha + 0.5F) * this.quadSize;

            float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - camPos.x + urandom.nextGaussian() * 0.5 * scale);
            float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - camPos.y + urandom.nextGaussian() * 0.5 * scale);
            float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - camPos.z + urandom.nextGaussian() * 0.5 * scale);

            int color = TessColorUtil.getColorRGBA_F(this.rCol + mod, this.gCol + mod, this.bCol + mod, this.alpha + mod);

            Vector3f l = new Vector3f(camera.getLeftVector()).mul(scale);
            Vector3f u = new Vector3f(camera.getUpVector()).mul(scale);

            consumer.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z)
                    .setColor(color)
                    .setUv(1, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z)
                    .setColor(color)
                    .setUv(1, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z)
                    .setColor(color)
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z)
                    .setColor(color)
                    .setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240)
                    .setNormal(0.0F, 1.0F, 0.0F);
        }
    }

    @Override
    public RenderType getRenderType() {
        return NtmRenderTypes.SMOTH_NO_DEPTH.apply(TEXTURE);
    }

    public static class ABMContrailProvider implements ParticleProvider<SimpleParticleType> {

        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            ParticleEngineNT.INSTANCE.add(new ContrailParticle(level, x, y, z));
            return null;
        }
    }
}
