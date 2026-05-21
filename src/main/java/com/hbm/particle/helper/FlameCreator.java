package com.hbm.particle.helper;

import com.hbm.main.NuclearTechModClient;
import com.hbm.particle.FlamethrowerParticle;
import com.hbm.particle.engine.ParticleEngineNT;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FlameCreator implements IParticleCreator {

    public static final int META_FIRE = 0;
    public static final int META_BALEFIRE = 1;
    public static final int META_DIGAMMA = 2;
    public static final int META_OXY = 3;
    public static final int META_BLACK = 4;

    public static void composeEffect(Level level, double x, double y, double z, int meta) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "flamethrower");
        tag.putInt("meta", meta);
        IParticleCreator.sendPacket(level, x, y, z, 50, tag);
    }

    public static void composeEffectClient(double x, double y, double z, int meta) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "flamethrower");
        tag.putInt("meta", meta);
        tag.putDouble("posX", x);
        tag.putDouble("posY", y);
        tag.putDouble("posZ", z);
        NuclearTechModClient.effectNT(tag);
    }

    @Override
    public void makeParticle(ClientLevel level, Player player, RandomSource rand, double x, double y, double z, CompoundTag tag) {
        FlamethrowerParticle particle = new FlamethrowerParticle(level, x, y, z, tag.getInt("meta"));

        ParticleEngineNT.INSTANCE.add(particle);
    }
}
