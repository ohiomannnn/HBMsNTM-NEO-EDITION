package com.hbm.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

import java.awt.*;

public class ParticleExplosionSmall extends TextureSheetParticle {

    private final SpriteSet sprites;
    private final float hue;

    protected ParticleExplosionSmall(ClientLevel level, double x, double y, double z,
                                     double dx, double dy, double dz,
                                     float scale, float speedMult,
                                     SpriteSet sprites) {
        super(level, x, y, z, dx, dy, dz);
        this.sprites = sprites;

        this.lifetime = 25 + this.random.nextInt(10);
        this.quadSize = scale * (0.9F + this.random.nextFloat() * 0.2F);

        this.xd = this.random.nextGaussian() * speedMult;
        this.yd = 0;
        this.zd = this.random.nextGaussian() * speedMult;

        this.gravity = -this.random.nextFloat() * 0.01F;

        this.hue = 20F + this.random.nextFloat() * 20F;
        Color color = Color.getHSBColor(hue / 255F, 1F, 1F);
        this.rCol = color.getRed() / 255F;
        this.gCol = color.getGreen() / 255F;
        this.bCol = color.getBlue() / 255F;

        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();

        float ageScaled = (float) this.age / (float) this.lifetime;
        Color color = Color.getHSBColor(hue / 255F,
                Math.max(1F - ageScaled * 2F, 0),
                Mth.clamp(1.25F - ageScaled * 2F, hue * 0.01F - 0.1F, 1F));

        this.rCol = color.getRed() / 255F;
        this.gCol = color.getGreen() / 255F;
        this.bCol = color.getBlue() / 255F;

        this.alpha = (float) Math.pow(1 - Math.min(ageScaled, 1), 0.25);

        this.setSpriteFromAge(sprites);
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        super.render(buffer, camera, partialTicks);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new ParticleExplosionSmall(level, x, y, z, dx, dy, dz, 1.0F, 0.1F, sprites);
        }
    }
}