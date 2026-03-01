package com.hbm.particle.helper;

import com.hbm.particle.CloudParticle;
import com.hbm.particle.engine.ParticleEngineNT;
import com.hbm.util.EnumUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class CloudCreator implements IParticleCreator {

    public static void composeEffect(Level level, double x, double y, double z, CloudType type) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "cloud");
        tag.putByte("cType", (byte) type.ordinal());
        if (level instanceof ServerLevel serverLevel) {
            IParticleCreator.sendPacket(serverLevel, x, y, z, 1000, tag);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void makeParticle(ClientLevel level, Player player, RandomSource rand, double x, double y, double z, CompoundTag tag) {
        CloudParticle particle = new CloudParticle(level, x, y, z);
        particle.type = EnumUtil.grabEnumSafely(CloudType.class, tag.getByte("cType"));
        ParticleEngineNT.INSTANCE.add(particle);
    }

    public enum CloudType {
        FLEIJA,
        SOLINIUM,
        RAINBOW
    }
}
