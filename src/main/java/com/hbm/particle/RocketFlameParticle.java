package com.hbm.particle;

import com.hbm.main.NuclearTechMod;
import com.hbm.particle.engine.ParticleEngineNT;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.particle.vanilla.NbtParticleOptions;
import com.hbm.particle.vanilla.ParticleProviderBase;
import com.hbm.render.NtmRenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class RocketFlameParticle extends ParticleNT {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/particle/base_particle.png");

    public RocketFlameParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.lifetime = 300 + this.random.nextInt(50);
        this.quadSize = 1.0F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        this.xd *= 0.91D;
        this.yd *= 0.91D;
        this.zd *= 0.91D;

        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        RandomSource urandom = RandomSource.create(this.hashCode());

        for (int i = 0; i < 10; i++) {
            float add = urandom.nextFloat() * 0.3F;
            float dark = 1 - Math.min(((float) (age) / (lifetime * 0.25F)), 1);

            this.rCol = Mth.clamp(1 * dark + add, 0.0F, 1.0F);
            this.gCol = Mth.clamp(0.6F * dark + add, 0.0F, 1.0F);
            this.bCol = Mth.clamp(0 + add, 0.0F, 1.0F);

            this.alpha = (float) (Math.pow(1 - Math.min(((float) (age) / (float) (lifetime)), 1), 0.5)) * 0.75F;

            float spread = (float) Math.pow(((float) (age) / (float) lifetime) * 4F, 1.5) + 1F;
            spread *= this.quadSize;

            float scale = (urandom.nextFloat() * 0.5F + 0.1F + ((float) (age) / (float) lifetime) * 2F) * quadSize;

            float pX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x() + (urandom.nextGaussian() - 1D) * 0.2F * spread);
            float pY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y() + (urandom.nextGaussian() - 1D) * 0.2F * spread);
            float pZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z() + (urandom.nextGaussian() - 1D) * 0.2F * spread);

            Vector3f l = new Vector3f(camera.getLeftVector()).mul(scale);
            Vector3f u = new Vector3f(camera.getUpVector()).mul(scale);

            consumer.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z)
                    .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                    .setUv(1, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z)
                    .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                    .setUv(1, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z)
                    .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240)
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z)
                    .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
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

    public static class ExhaustSoyuzProvider extends ParticleProviderBase<NbtParticleOptions> {

        @Override
        public void createParticle(NbtParticleOptions options, double x, double y, double z, double xd, double yd, double zd, ClientLevel level, LocalPlayer player, int particleSetting) {
            CompoundTag tag = options.tag;
            RandomSource random = level.random;

            int count = Math.max(1, tag.getInt("count"));
            double width = tag.getDouble("width");

            for(int i = 0; i < count; i++) {
                RocketFlameParticle particle = new RocketFlameParticle(level, x + random.nextGaussian() * width, y, z + random.nextGaussian() * width);
                particle.yd = -0.75 + random.nextDouble() * 0.5;
                ParticleEngineNT.INSTANCE.add(particle);
            }
        }
    }

    public static class ExhaustMeteorProvider extends ParticleProviderBase<NbtParticleOptions> {

        @Override
        public void createParticle(NbtParticleOptions options, double x, double y, double z, double xd, double yd, double zd, ClientLevel level, LocalPlayer player, int particleSetting) {
            CompoundTag tag = options.tag;
            RandomSource random = level.random;

            int count = Math.max(1, tag.getInt("count"));
            double width = tag.getDouble("width");

            for(int i = 0; i < count; i++) {
                RocketFlameParticle particle = new RocketFlameParticle(level, x + random.nextGaussian() * width, y + random.nextGaussian() * width, z + random.nextGaussian() * width);
                ParticleEngineNT.INSTANCE.add(particle);
            }
        }
    }
}
