package com.hbm.particle;

import com.hbm.HBMsNTM;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.render.CustomRenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ParticleExSmoke extends ParticleNT {

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/particle/base_particle.png");

    public ParticleExSmoke(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.lifetime = 100 + random.nextInt(40);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.alpha = 1 - ((float) age / (float) lifetime);

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        this.xd *= 0.76D;
        this.yd *= 0.76D;
        this.zd *= 0.76D;

        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 camPos = camera.getPosition();

        RandomSource urandom = RandomSource.create(this.hashCode());

        for (int i = 0; i < 6; i++) {
            this.rCol = this.gCol = this.bCol = urandom.nextFloat() * 0.25F + 0.25F;

            float pX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x() + (urandom.nextGaussian() - 1D) * 0.75F);
            float pY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y() + (urandom.nextGaussian() - 1D) * 0.75F);
            float pZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z() + (urandom.nextGaussian() - 1D) * 0.75F);

            this.quadSize = urandom.nextFloat() + 0.5F;

            Vector3f l = new Vector3f(camera.getLeftVector()).mul(this.quadSize);
            Vector3f u = new Vector3f(camera.getUpVector()).mul(this.quadSize);

            consumer.addVertex(pX - l.x - u.x, pY - l.y - u.y, pZ - l.z - u.z)
                    .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                    .setUv(1, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(this.getLightColor())
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(pX - l.x + u.x, pY - l.y + u.y, pZ - l.z + u.z)
                    .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                    .setUv(1, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(this.getLightColor())
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(pX + l.x + u.x, pY + l.y + u.y, pZ + l.z + u.z)
                    .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(this.getLightColor())
                    .setNormal(0.0F, 1.0F, 0.0F);
            consumer.addVertex(pX + l.x - u.x, pY + l.y - u.y, pZ + l.z - u.z)
                    .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                    .setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(this.getLightColor())
                    .setNormal(0.0F, 1.0F, 0.0F);
        }
    }

    @Override
    public RenderType getRenderType() {
        return CustomRenderTypes.SMOTH_NO_DEPTH.apply(TEXTURE);
    }
}
