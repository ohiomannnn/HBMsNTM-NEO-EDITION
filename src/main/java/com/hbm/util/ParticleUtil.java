package com.hbm.util;

import com.hbm.packet.toclient.AuxParticlePacketNT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class ParticleUtil {

    public static void spawnGasFlame(Level level, double x, double y, double z, double mX, double mY, double mZ) {
        CompoundTag data = new CompoundTag();
        data.putString("type", "gasfire");
        data.putDouble("mX", mX);
        data.putDouble("mY", mY);
        data.putDouble("mZ", mZ);
        if (level.isClientSide) {
            data.putDouble("posX", x);
            data.putDouble("posY", y);
            data.putDouble("posZ", z);
            MainRegistry.proxy.effectNT(data);
        } else if (level instanceof ServerLevel serverLevel) {
            AuxParticlePacketNT packet = new AuxParticlePacketNT("gasfire", x, y, z, mX, mY, mZ, -1);
            PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, 150, packet);
        }
    }

    public static void spawnDroneLine(Level level, double x, double y, double z,
                                      double x0, double y0, double z0, int color) {
        CompoundTag data = new CompoundTag();
        data.putString("type", "debugdrone");
        data.putDouble("mX", x0);
        data.putDouble("mY", y0);
        data.putDouble("mZ", z0);
        data.putInt("color", color);
        if (level.isClientSide) {
            data.putDouble("posX", x);
            data.putDouble("posY", y);
            data.putDouble("posZ", z);
            MainRegistry.proxy.effectNT(data);
        } else if (level instanceof ServerLevel serverLevel) {
            AuxParticlePacketNT packet = new AuxParticlePacketNT("debugdrone", x, y, z, x0, y0, z0, color);
            PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, 150, packet);
        }
    }
}
