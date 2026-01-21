package com.hbm.particle;

import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AshesParticle extends RotatingParticle {

    public AshesParticle(ClientLevel level, double x, double y, double z, float scale) {
        super(level, x, y, z);
        this.setSpriteFromAge(ModParticles.BASE_PARTICLE_SPRITES);

        this.lifetime = 1200 + random.nextInt(20);
        this.quadSize = scale * 0.9F + random.nextFloat() * 0.2F;

        this.gravity = 0.01F;

        this.rCol = this.gCol = this.bCol = this.random.nextFloat() * 0.1F + 0.1F;
    }

    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square((double)100.0F);

    @Override
    public void move(double x, double y, double z) {
        double d0 = x;
        double d1 = y;
        double d2 = z;

        if (this.hasPhysics && (x != 0.0 || y != 0.0 || z != 0.0) && x * x + y * y + z * z < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
            Vec3 vec3 = Entity.collideBoundingBox(null, new Vec3(x, y, z), this.getBoundingBox(), this.level, List.of());
            x = vec3.x;
            y = vec3.y;
            z = vec3.z;
        }

        if (x != 0.0 || y != 0.0 || z != 0.0) {
            this.setBoundingBox(this.getBoundingBox().move(x, y, z));
            this.setLocationFromBoundingbox();
        }

        this.onGround = d1 != y && d1 < 0.0;

        if (this.onGround) {
            this.xd = 0.0;
            this.yd = 0.0;
            this.zd = 0.0;
        } else {
            if (d0 != x) this.xd = 0.0;
            if (d2 != z) this.zd = 0.0;
        }
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.age++;

        if (this.age >= this.lifetime) {
            this.remove();
        }

        this.yd -= gravity;
        this.oRoll = this.roll;

        if (!this.onGround) this.roll += (float) (2 * ((this.hashCode() % 2) - 0.5));

        this.xd *= 0.95D;
        this.yd *= 0.99D;
        this.zd *= 0.95D;

        boolean wasOnGround = this.onGround;
        this.move(this.xd, this.yd, this.zd);
        if (!wasOnGround && this.onGround) this.roll = random.nextFloat() * 360F;

        if (this.hashCode() % 5 == 0 && this.onGround && random.nextInt(15) == 0) {
            level.addParticle(ParticleTypes.SMOKE, x, y + 0.125, z, 0, 0.05, 0);
        }
    }

    @Override
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();
        float timeLeft = this.lifetime - (this.age + partialTicks);

        if (timeLeft < 40) {
            this.alpha = timeLeft / 40F;
        } else {
            this.alpha = 1F;
        }

        if (this.onGround) {
            BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(TextureAtlas.LOCATION_PARTICLES));

            float pX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x);
            float pY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y);
            float pZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z);

            Vec3NT vec = new Vec3NT(quadSize, 0, quadSize).rotateAroundYDeg(this.roll);

            float u0 = sprite.getU0();
            float u1 = sprite.getU1();
            float v0 = sprite.getV0();
            float v1 = sprite.getV1();

            int light = this.getLightColor(partialTicks);

            consumer.addVertex((float) (pX + vec.xCoord), pY + 0.15F, (float) (pZ + vec.zCoord))
                    .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                    .setUv(u1, v1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(0.0F, 1.0F, 0.0F);
            vec.rotateAroundYDeg(90);
            consumer.addVertex((float) (pX + vec.xCoord), pY + 0.15F, (float) (pZ + vec.zCoord))
                    .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                    .setUv(u1, v0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(0.0F, 1.0F, 0.0F);
            vec.rotateAroundYDeg(90);
            consumer.addVertex((float) (pX + vec.xCoord), pY + 0.15F, (float) (pZ + vec.zCoord))
                    .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                    .setUv(u0, v0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(0.0F, 1.0F, 0.0F);
            vec.rotateAroundYDeg(90);
            consumer.addVertex((float) (pX + vec.xCoord), pY + 0.15F, (float) (pZ + vec.zCoord))
                    .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                    .setUv(u0, v1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(0.0F, 1.0F, 0.0F);

            buffer.endBatch();
        } else {
            BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            this.renderParticleRotated(buffer, RenderType.entityTranslucent(TextureAtlas.LOCATION_PARTICLES), camera, this.rCol, this.gCol, this.bCol, this.alpha, this.quadSize, partialTicks, this.getLightColor(partialTicks));
            buffer.endBatch();
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public TextureSheetParticle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double dx, double dy, double dz) {
            return new AshesParticle(world, x, y, z, 0.125F);
        }
    }
}
