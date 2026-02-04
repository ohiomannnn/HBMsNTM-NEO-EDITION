package com.hbm.particle.helper;

import com.hbm.particle.NukeTorexParticle;
import com.hbm.particle.engine.ParticleEngineNT;
import com.hbm.util.BobMathUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class NukeTorexCreator implements IParticleCreator {

    public static void statFacStandard(Level level, double x, double y, double z, float scale) {
        statFac(level, x, y, z, scale, 0);
    }

    public static void statFacBale(Level level, double x, double y, double z, float scale) {
        statFac(level, x, y, z, scale, 1);
    }

    private static void statFac(Level level, double x, double y, double z, float scale, int type) {

        CompoundTag tag = new CompoundTag();
        tag.putString("type", "nukeTorex");
        tag.putFloat("scale", scale);
        if (type == 1 || type == 0) tag.putFloat("cloudType", type);
        if (level instanceof ServerLevel serverLevel) {
            IParticleCreator.sendPacket(serverLevel, x, y, z, 2000, tag);
        }
    }

    @Override
    public void makeParticle(ClientLevel level, Player player, RandomSource rand, double x, double y, double z, CompoundTag tag) {
        int type = tag.getInt("cloudType");
        float scale = tag.getFloat("scale");

        NukeTorexParticle particle = new NukeTorexParticle(level, x, y, z);
        particle.setScale((float) Math.clamp(BobMathUtil.squirt(scale * 0.01) * 1.5F, 0.5F, 5F));
        particle.setType(type);
        ParticleEngineNT.INSTANCE.add(particle);
    }
}
