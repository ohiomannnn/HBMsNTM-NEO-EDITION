package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.awt.*;

public class ParticleExplosionSmall extends ParticleRotating {

    private final float hue;

    public ParticleExplosionSmall(ClientLevel level, double x, double y, double z, float scale, float speedMult, SpriteSet sprites) {
        super(level, x, y, z);
        this.setSpriteFromAge(sprites);
        this.lifetime = 25 + this.random.nextInt(10);
        this.quadSize = scale * 0.9F + this.random.nextFloat() * 0.2F;

        this.xd = this.random.nextGaussian() * speedMult;
        this.zd = this.random.nextGaussian() * speedMult;

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

        this.age++;

        if (this.age >= this.lifetime) {
            this.remove();
        }

        this.xd -= gravity;
        this.oRoll = this.roll;

        float ageScaled = (float) this.age / (float) this.lifetime;
        this.roll += (float) ((1 - ageScaled) * 5 * ((this.hashCode() % 2) - 0.5));

        this.xd *= 0.65D;
        this.zd *= 0.65D;

        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {

        Vector3f up = new Vector3f(camera.getUpVector());
        Vector3f left = new Vector3f(camera.getLeftVector());

        double ageScaled = (double) (this.age + partialTicks) / (double) this.lifetime;

        Color color = Color.getHSBColor(hue / 255F, Math.max(1F - (float) ageScaled * 2F, 0), Mth.clamp(1.25F - (float) ageScaled * 2F, hue * 0.01F - 0.1F, 1F));
        this.rCol = color.getRed() / 255F;
        this.gCol = color.getGreen() / 255F;
        this.bCol = color.getBlue() / 255F;

        this.alpha = (float) Math.pow(1 - Math.min(ageScaled, 1), 0.25);

        float scale = (float) ((0.25 + 1 - Math.pow(1 - ageScaled, 4) + (this.age + partialTicks) * 0.02) * this.quadSize);
        renderParticleRotated(consumer, camera, up, left, this.rCol, this.gCol, this.bCol, this.alpha * 0.5F, scale, partialTicks, 240);
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