package com.hbm.util;

import com.hbm.HBMsNTMClient;
import com.hbm.network.toclient.AuxParticle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class ParticleUtil {
    public static void spawnGasFlame(Level level, double x, double y, double z, double mX, double mY, double mZ) {

        CompoundTag tag = new CompoundTag();
        tag.putString("type", "gasfire");
        tag.putDouble("mX", mX);
        tag.putDouble("mY", mY);
        tag.putDouble("mZ", mZ);

        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, 250, new AuxParticle(tag, x, y, z));
        } else if (level.isClientSide) {
            tag.putDouble("posX", x);
            tag.putDouble("posY", y);
            tag.putDouble("posZ", z);
            HBMsNTMClient.effectNT(tag);
        }
    }
}
