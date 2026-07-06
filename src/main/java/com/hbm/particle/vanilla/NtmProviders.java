package com.hbm.particle.vanilla;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

import javax.annotation.Nullable;

public class NtmProviders {

    public static class VanillaVolcanoProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public VanillaVolcanoProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {

//            SmokeParticle particle = SmokeParticleInvoker.create(level, x, y, z, xd, yd, zd, 100F, this.sprites);
//            particle.setColor();

            return null;
        }
    }
}
