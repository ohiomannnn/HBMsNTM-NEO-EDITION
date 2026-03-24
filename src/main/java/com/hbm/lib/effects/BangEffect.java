package com.hbm.lib.effects;

import com.hbm.lib.ModEffect;
import com.hbm.registry.NtmDamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BangEffect extends MobEffect {

    public BangEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return false;

        entity.hurt(entity.damageSources().source(NtmDamageTypes.BANG), Float.MAX_VALUE);
        entity.kill();

        // entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.LASER_BANG.get(), entity.getSoundSource(), 100.0F, 1.0F);
//        ExplosionLarge.spawnParticles(entity.level(), entity.getX(), entity.getY(), entity.getZ(), 10);

//        if (entity instanceof Cow cow) {
//            int toDrop = cow.isBaby() ? 10 : 3;
//            cow.spawnAtLocation(new ItemStack(ModItems.CHEESE.get(), toDrop));
//        }

        entity.removeEffect(ModEffect.BANG);
        return false;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
