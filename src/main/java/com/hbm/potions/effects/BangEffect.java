package com.hbm.potions.effects;

//import com.hbm.explosion.ExplosionLarge; // Предполагается, что этот класс уже портирован
//import com.hbm.item.ModItems; // Предполагается, что этот класс уже портирован
//import com.hbm.lib.ModDamageSource; // Предполагается, что этот класс уже портирован
import com.hbm.potions.ModPotions;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import static com.hbm.lib.ModDamageSource.BANG;

public class BangEffect extends MobEffect {

    public BangEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return false;

        DamageSource src = new DamageSource(
                entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(BANG)
        );
        entity.hurt(src, Float.MAX_VALUE);
        entity.kill();

        // entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.LASER_BANG.get(), entity.getSoundSource(), 100.0F, 1.0F);
//        ExplosionLarge.spawnParticles(entity.level(), entity.getX(), entity.getY(), entity.getZ(), 10);

//        if (entity instanceof Cow cow) {
//            int toDrop = cow.isBaby() ? 10 : 3;
//            cow.spawnAtLocation(new ItemStack(ModItems.CHEESE.get(), toDrop));
//        }

        entity.removeEffect(ModPotions.BANG);
        return false;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
