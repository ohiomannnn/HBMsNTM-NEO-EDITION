package com.hbm.util.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleCreatorClient implements IParticleCreator {

    @Override
    @OnlyIn(Dist.CLIENT)
    public <T extends ParticleOptions> void addParticle(Level level, T options, double x, double y, double z, float xd, float yd, float zd, double radius) {
        Minecraft mc = Minecraft.getInstance();

        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();
        double d0 = x - camPos.x;
        double d1 = y - camPos.y;
        double d2 = z - camPos.z;
        if(d0 * d0 + d1 * d1 + d2 * d2 < radius * radius) {
            mc.particleEngine.createParticle(options, x, y, z, xd, yd, zd);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <T extends ParticleOptions> void addParticle(Level level, T options, double x, double y, double z, float xd, float yd, float zd) {
        Minecraft.getInstance().particleEngine.createParticle(options, x, y, z, xd, yd, zd);
    }
}
