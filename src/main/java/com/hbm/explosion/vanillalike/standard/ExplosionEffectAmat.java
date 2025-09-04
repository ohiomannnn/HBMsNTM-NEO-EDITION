package com.hbm.explosion.vanillalike.standard;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import com.hbm.explosion.vanillalike.interfaces.IExplosionSFX;
import com.hbm.lib.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;


public class ExplosionEffectAmat implements IExplosionSFX {

    @Override
    public void doEffect(ExplosionVNT explosion, Level level, double x, double y, double z, float size) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (size < 15) {
            level.playSound(
                    null,
                    x, y, z,
                    SoundEvents.GENERIC_EXPLODE,
                    SoundSource.BLOCKS,
                    4.0F,
                    (1.4F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F
            );
        } else {
            level.playSound(
                    null,
                    x, y, z,
                    ModSounds.MUKE_EXPLOSION.get(),
                    SoundSource.BLOCKS,
                    15.0F,
                    1.0F
            );
        }

        serverLevel.sendParticles(
                ParticleTypes.EXPLOSION_EMITTER,
                x, y, z,
                2,
                1.0D, 1.0D, 1.0D,
                0.0D
        );

        serverLevel.sendParticles(ParticleTypes.FLASH, x, y, z, 1, 0, 0, 0, 0);
    }
}