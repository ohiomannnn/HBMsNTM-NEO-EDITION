package com.hbm.util.particle;

import com.hbm.main.NuclearTechMod;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;

public class ParticleUtil {

    public static <T extends ParticleOptions> void addParticle(Level level, T type, double x, double y, double z, double radius) {
        addParticle(level, type, x, y, z, 0F, 0F, 0F, radius);
    }
    public static <T extends ParticleOptions> void addParticle(Level level, T type, double x, double y, double z, float xd, float yd, float zd, double radius) {
        NuclearTechMod.proxy.getParticleCreator().addParticle(level, type, x, y, z, xd, yd, zd, radius);
    }

    public static <T extends ParticleOptions> void addParticle(Level level, T type, double x, double y, double z) {
        addParticle(level, type, x, y, z, 0F, 0F, 0F);
    }
    public static <T extends ParticleOptions> void addParticle(Level level, T type, double x, double y, double z, float xd, float yd, float zd) {
        NuclearTechMod.proxy.getParticleCreator().addParticle(level, type, x, y, z, xd, yd, zd);
    }
}
