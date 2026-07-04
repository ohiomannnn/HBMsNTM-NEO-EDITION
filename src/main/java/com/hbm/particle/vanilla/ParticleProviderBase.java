package com.hbm.particle.vanilla;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;

import javax.annotation.Nullable;

public abstract class ParticleProviderBase<T extends ParticleOptions> implements ParticleProvider<T> {

    @Override
    public @Nullable Particle createParticle(T options, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
        Minecraft mc = Minecraft.getInstance();
        this.createParticle(options, x, y, z, xd, yd, zd, level, mc.player, mc.options.particles().get().getId());
        return null;
    }

    public abstract void createParticle(T options, double x, double y, double z, double xd, double yd, double zd, ClientLevel level, LocalPlayer player, int particleSetting);
}
