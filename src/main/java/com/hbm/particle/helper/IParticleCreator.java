package com.hbm.particle.helper;

import com.hbm.network.toclient.AuxParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public interface IParticleCreator {

    void makeParticle(ClientLevel level, Player player, RandomSource rand, double x, double y, double z, CompoundTag tag);

    static void sendPacket(ServerLevel level, double x, double y, double z, int range, CompoundTag data) {
        PacketDistributor.sendToPlayersNear(level, null, x, y, z, range, new AuxParticle(data, x, y, z));
    }

    static void sendPacket(Level level, double x, double y, double z, int range, CompoundTag tag) {
        if(level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, range, new AuxParticle(tag, x, y, z));
        }
    }

    static <T extends ParticleOptions> void addParticle(Level level, T type, double x, double y, double z, double radius) {
        addParticle(level, type, x, y, z, 0F, 0F, 0F, radius);
    }
    static <T extends ParticleOptions> void addParticle(Level level, T type, double x, double y, double z, float xd, float yd, float zd, double radius) {
        if(level instanceof ServerLevel serverLevel) {
            serverLevel.getServer().getPlayerList().broadcast(
                    null,
                    x,
                    y,
                    z,
                    radius,
                    serverLevel.dimension(),
                    new ClientboundLevelParticlesPacket(type, true, x, y, z, xd, yd, zd, 1F, 1)
            );
        }
    }

    static <T extends ParticleOptions> void addParticle(Level level, T type, double x, double y, double z) {
        addParticle(level, type, x, y, z, 0F, 0F, 0F);
    }
    static <T extends ParticleOptions> void addParticle(Level level, T type, double x, double y, double z, float xd, float yd, float zd) {
        if(level instanceof ServerLevel serverLevel) {
            serverLevel.getServer().getPlayerList().broadcastAll(
                    new ClientboundLevelParticlesPacket(type, true, x, y, z, xd, yd, zd, 1F, 1),
                    serverLevel.dimension()
            );
        }
    }
}
