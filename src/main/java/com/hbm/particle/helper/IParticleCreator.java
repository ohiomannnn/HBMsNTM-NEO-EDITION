package com.hbm.particle.helper;

import com.hbm.network.toclient.AuxParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public interface IParticleCreator {

    void makeParticle(ClientLevel level, Player player, RandomSource rand, double x, double y, double z, CompoundTag tag);

    static void sendPacket(ServerLevel level, double x, double y, double z, int range, CompoundTag data) {
        PacketDistributor.sendToPlayersNear(level, null, x, y, z, range, new AuxParticle(data, x, y, z));
    }
}
