package com.hbm.particle;

import com.hbm.HBMsNTM;
import com.hbm.particle.engine.ParticleEngineNT;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.render.CustomRenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class MukeFlashParticle extends ParticleNT {

    private final boolean bf;

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/particle/flare.png");

    public MukeFlashParticle(ClientLevel level, double x, double y, double z, boolean bf) {
        super(level, x, y, z);
        this.bf = bf;
        this.lifetime = 20;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age == 15) {
            // Stem
            for (double d = 0.0D; d <= 1.8D; d += 0.1) {
                ParticleEngineNT.INSTANCE.add(getCloud(level, x, y, z, random.nextGaussian() * 0.05, d + random.nextGaussian() * 0.02, random.nextGaussian() * 0.05));
            }

            // Ground
            for (int i = 0; i < 100; i++) {
                ParticleEngineNT.INSTANCE.add(getCloud(level, x, y + 0.5, z, random.nextGaussian() * 0.5, random.nextInt(5) == 0 ? 0.02 : 0, random.nextGaussian() * 0.5));
            }

            // Mush
            for (int i = 0; i < 75; i++) {
                double dx = random.nextGaussian() * 0.5;
                double dz = random.nextGaussian() * 0.5;

                if (dx * dx + dz * dz > 1.5) {
                    dx *= 0.5;
                    dz *= 0.5;
                }

                double dy = 1.8 + (random.nextDouble() * 3 - 1.5) * (0.75 - (dx * dx + dz * dz)) * 0.5;

                ParticleEngineNT.INSTANCE.add(getCloud(level, x, y, z, dx, dy + random.nextGaussian() * 0.02, dz));
            }
        }
    }

    private MukeCloudParticle getCloud(ClientLevel level, double x, double y, double z, double mx, double my, double mz) {

        if (this.bf) {
            return new MukeCloudBFParticle(level, x, y, z, mx, my, mz);
        } else {
            return new MukeCloudParticle(level, x, y, z, mx, my, mz);
        }
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {

        FogRenderer.setupNoFog();

        RandomSource rand = RandomSource.create();

        Vec3 cameraPosition = camera.getPosition();
        float dX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float dY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
        float dZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

        this.alpha = Math.clamp(1 - ((this.age + partialTicks) / (float)this.lifetime), 0.0F, 1.0F);
        this.quadSize = (this.age + partialTicks) * 3F + 1F;

        Vector3f l = new Vector3f(camera.getLeftVector()).mul(this.quadSize);
        Vector3f u = new Vector3f(camera.getUpVector()).mul(this.quadSize);

        for (int i = 0; i < 24; i++) {

            rand.setSeed(i * 31 + 1);

            float pX = (float) (dX + rand.nextDouble() * 15 - 7.5);
            float pY = (float) (dY + rand.nextDouble() * 7.5 - 3.75);
            float pZ = (float) (dZ + rand.nextDouble() * 15 - 7.5);

            consumer.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z)
                    .setColor(1.0F, 0.9F, 0.75F, alpha * 0.5F)
                    .setUv(1, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setNormal(0.0F, 1.0F, 0.0F)
                    .setLight(240);
            consumer.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z)
                    .setColor(1.0F, 0.9F, 0.75F, alpha * 0.5F)
                    .setUv(1, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setNormal(0.0F, 1.0F, 0.0F)
                    .setLight(240);
            consumer.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z)
                    .setColor(1.0F, 0.9F, 0.75F, alpha * 0.5F)
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setNormal(0.0F, 1.0F, 0.0F)
                    .setLight(240);
            consumer.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z)
                    .setColor(1.0F, 0.9F, 0.75F, alpha * 0.5F)
                    .setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setNormal(0.0F, 1.0F, 0.0F)
                    .setLight(240);
        }
    }

    @Override
    public RenderType getRenderType() {
        return CustomRenderTypes.entityAdditive(TEXTURE);
    }
}
