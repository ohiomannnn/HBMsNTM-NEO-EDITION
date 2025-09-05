package com.hbm.potions.effects;

import com.hbm.block.ModBlocks;
import com.hbm.config.ServerConfig;
import com.hbm.lib.ModDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.hbm.lib.ModDamageSource.TAINT;

public class TaintEffect extends MobEffect {

    public TaintEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {

            // if (!(entity instanceof EntityCreeperTainted) && !(entity instanceof EntityTaintCrab)) {
            DamageSource src = new DamageSource(
                    entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(TAINT)
            );
            entity.hurt(src, (float) (amplifier + 1));
            // }

            if (ServerConfig.ENABLE_TAINT_TRAIL.getAsBoolean()) {
                Level level = entity.level();
                BlockPos posBelow = entity.blockPosition().below();
                BlockState stateBelow = level.getBlockState(posBelow);

                if (posBelow.getY() > level.getMinBuildHeight() && stateBelow.canOcclude() && !stateBelow.isAir()) {
                    level.setBlock(posBelow, ModBlocks.TAINT.get().defaultBlockState(), 3);
                }
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 2 == 0;
    }
}