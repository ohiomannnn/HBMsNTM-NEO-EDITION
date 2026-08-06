package com.hbm.explosion.vanillant.standard;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.interfaces.IExplosionSFX;
import com.hbm.particle.NtmParticleTypes;
import com.hbm.particle.vanilla.NbtParticleOptions;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.particle.ParticleUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public class ExplosionEffectAmat implements IExplosionSFX {

    @Override
    public void doEffect(ExplosionVNT explosion, Level level, double x, double y, double z, float size) {

        if(size < 15) {
            level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.4F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);
        } else {
            level.playSound(null, x, y, z, NtmSoundEvents.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 15.0F, 1.0F);
        }

        CompoundTag tag = new CompoundTag();
        tag.putFloat("scale", size);
        ParticleUtil.addParticle(level, new NbtParticleOptions(NtmParticleTypes.AMAT.get(), tag), x, y, z, 250);
    }
}