package com.hbm.explosion.vanillant.standard;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.interfaces.IExplosionSFX;
import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.AuxParticle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;


public class ExplosionEffectAmat implements IExplosionSFX {

    @Override
    public void doEffect(ExplosionVNT explosion, Level level, double x, double y, double z, float size) {

        if (size < 15) {
            level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.4F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);
        } else {
            level.playSound(null, x, y, z, ModSounds.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 15.0F, 1.0F);
        }

        if (level instanceof ServerLevel serverLevel) {
            CompoundTag tag = new CompoundTag();
            tag.putString("type", "amat");
            tag.putFloat("scale", size);
            PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, 250, new AuxParticle(tag, x, y, z));
        }
    }
}