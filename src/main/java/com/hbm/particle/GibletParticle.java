package com.hbm.particle;

import com.hbm.NuclearTechMod;
import com.hbm.particle.engine.ParticleNT;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class GibletParticle extends ParticleNT {

    private static final ResourceLocation TEXTURE_MEAT = NuclearTechMod.withDefaultNamespace("textures/particle/meat.png");
    private static final ResourceLocation TEXTURE_SLIME = NuclearTechMod.withDefaultNamespace("textures/particle/slime.png");
    private static final ResourceLocation TEXTURE_METAL = NuclearTechMod.withDefaultNamespace("textures/particle/metal.png");

    private float momentumRoll;
    private int gibType;

    public GibletParticle(ClientLevel level, double x, double y, double z, double mX, double mY, double mZ, int gibType) {
        super(level, x, y, z);
        this.xd = mX;
        this.yd = mY;
        this.zd = mZ;
        this.lifetime = 140 + random.nextInt(20);
        this.gravity = 2F;
        this.gibType = gibType;

        if (gibType == 2) this.gravity *= 2;

        this.momentumRoll = (float) random.nextGaussian() * 15F;
    }

    @Override
    public void tick() {
        super.tick();

        this.oRoll = this.roll;

        if (!this.onGround) {
            this.roll += this.momentumRoll;

            if (gibType == 2) return;

            ParticleDust particleDust = new ParticleDust(level, x, y, z, 0, 0, 0, gibType == 1 ? Blocks.MELON.defaultBlockState() : Blocks.REDSTONE_BLOCK.defaultBlockState());
            particleDust.setLifetime(20 + random.nextInt(20));
            particleDust.setOriginalSize();
            Minecraft.getInstance().particleEngine.add(particleDust);
        }
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        float pX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
        float pY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
        float pZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

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

    @Override
    public RenderType getRenderType() {
        return switch (gibType) {
            case 1 -> RenderType.entityTranslucent(TEXTURE_SLIME);
            case 2 -> RenderType.entityTranslucent(TEXTURE_METAL);
            default -> RenderType.entityTranslucent(TEXTURE_MEAT);
        };
    }
}
