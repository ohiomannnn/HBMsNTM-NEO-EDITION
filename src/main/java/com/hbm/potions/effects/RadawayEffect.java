package com.hbm.potions.effects;

import com.hbm.extprop.HbmLivingProps;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class RadawayEffect extends MobEffect {
    public RadawayEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        HbmLivingProps.incrementRadiation(entity, -(amplifier + 1));
        return true;
    }
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
