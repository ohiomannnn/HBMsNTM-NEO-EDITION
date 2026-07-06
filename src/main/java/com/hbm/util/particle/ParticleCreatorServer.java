package com.hbm.util.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class ParticleCreatorServer implements IParticleCreator {

    @Override
    public <T extends ParticleOptions> void addParticle(Level level, T options, double x, double y, double z, float xd, float yd, float zd, double radius) {
        if(level instanceof ServerLevel serverLevel) {
            serverLevel.getServer().getPlayerList().broadcast(
                    null,
                    x,
                    y,
                    z,
                    radius,
                    serverLevel.dimension(),
                    new ClientboundLevelParticlesPacket(options, true, x, y, z, xd, yd, zd, 1F, 1)
            );
        }
    }

    @Override
    public <T extends ParticleOptions> void addParticle(Level level, T options, double x, double y, double z, float xd, float yd, float zd) {
        if(level instanceof ServerLevel serverLevel) {
            serverLevel.getServer().getPlayerList().broadcastAll(
                    new ClientboundLevelParticlesPacket(options, true, x, y, z, xd, yd, zd, 1F, 1),
                    serverLevel.dimension()
            );
        }
    }
}
