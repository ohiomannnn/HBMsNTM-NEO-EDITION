package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

import java.awt.*;

public class ParticleExplosionSmall extends RotatingParticle {

    private final float hue;
    private float gravity;

    public ParticleExplosionSmall(ClientLevel level, double x, double y, double z, float scale, float speedMult, SpriteSet sprites) {
        super(level, x, y, z, sprites);

        this.lifetime = 25 + this.random.nextInt(10);

        this.quadSize = scale * 0.9F + this.random.nextFloat() * 0.2F;

        this.xd = this.random.nextGaussian() * speedMult;
        this.zd = this.random.nextGaussian() * speedMult;
        this.yd = 0.0;

        this.gravity = this.random.nextFloat() * -0.01F;

        this.hue = 20F + this.random.nextFloat() * 20F;
        Color base = Color.getHSBColor(hue / 255F, 1F, 1F);
        this.rCol = base.getRed() / 255F;
        this.gCol = base.getGreen() / 255F;
        this.bCol = base.getBlue() / 255F;

        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (++this.age >= this.lifetime) {
            this.remove();
            return;
        }

        this.yd -= this.gravity;
        float ageScaled = (float) this.age / (float) this.lifetime;
        this.oRoll = this.roll;
        float parity = ((this.hashCode() % 2) == 0) ? -0.5F : 0.5F;
        float deltaDeg = (1 - ageScaled) * 5F * parity;
        this.roll += (float) Math.toRadians(deltaDeg);

        this.xd *= 0.65D;
        this.zd *= 0.65D;

        this.move(this.xd, this.yd, this.zd);

        this.sprite = sprites.get(this.age, this.lifetime);
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        double ageScaled = (double) (this.age + partialTicks) / (double) this.lifetime;

        Color color = Color.getHSBColor(
                hue / 255F,
                Math.max(1F - (float) ageScaled * 2F, 0F),
                Mth.clamp(1.25F - (float) ageScaled * 2F, hue * 0.01F - 0.1F, 1F)
        );

        this.rCol = color.getRed() / 255F;
        this.gCol = color.getGreen() / 255F;
        this.bCol = color.getBlue() / 255F;

        this.alpha = 0.5F * (float) Math.pow(1 - Math.min(ageScaled, 1.0), 0.25);

        super.render(buffer, camera, partialTicks);
    }

    @Override
    public float getQuadSize(float partialTicks) {
        double ageScaled = (double) (this.age + partialTicks) / (double) this.lifetime;
        double scale = (0.25 + 1 - Math.pow(1 - ageScaled, 4) + (this.age + partialTicks) * 0.02) * this.quadSize;
        return (float) scale;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new ParticleExplosionSmall(level, x, y, z, 1.0F, 0.1F, sprites);
        }
    }
}