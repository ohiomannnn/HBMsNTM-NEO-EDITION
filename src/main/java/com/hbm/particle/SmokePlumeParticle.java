package com.hbm.particle;

import com.hbm.main.NuclearTechMod;
import com.hbm.blocks.bomb.LaunchPadBlock;
import com.hbm.particle.engine.ParticleNT;
import com.hbm.render.CustomRenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class SmokePlumeParticle extends ParticleNT {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/particle/contrail.png");

    public SmokePlumeParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.lifetime = 80 + this.random.nextInt(20);
        this.quadSize = 0.25F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.noClip = this.level.getBlockState(BlockPos.containing(x, y, z)).getBlock() instanceof LaunchPadBlock;

        this.alpha = 1 - ((float) age / (float) lifetime);
        float prevScale = this.quadSize;
        this.quadSize = 0.25F + ((float) age / (float) lifetime) * 2;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        double bak = new Vec3(xd, yd, zd).length();

        this.move(this.xd, this.yd + (this.quadSize - prevScale), this.zd);

        if (this.verticalCollision) {
            yd = bak;
        }

        this.xd *= 0.91D;
        this.yd *= 0.91D;
        this.zd *= 0.91D;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        RandomSource urandom = RandomSource.create(this.hashCode());

        for (int i = 0; i < 6; i++) {

            this.rCol = this.gCol = this.bCol = urandom.nextFloat() * 0.75F + 0.1F;

            float scale = this.quadSize;

            float pX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x + urandom.nextGaussian() * 0.5 * scale);
            float pY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y + urandom.nextGaussian() * 0.5 * scale);
            float pZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z + urandom.nextGaussian() * 0.5 * scale);

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
        return CustomRenderTypes.SMOTH_NO_DEPTH.apply(TEXTURE);
    }
}
