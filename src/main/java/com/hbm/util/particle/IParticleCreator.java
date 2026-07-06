package com.hbm.util.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;

public interface IParticleCreator {

    <T extends ParticleOptions> void addParticle(Level level, T options, double x, double y, double z, float xd, float yd, float zd, double radius);
    <T extends ParticleOptions> void addParticle(Level level, T options, double x, double y, double z, float xd, float yd, float zd);
}
