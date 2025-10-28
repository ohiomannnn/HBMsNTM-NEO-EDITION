package com.hbm.lib.effects;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.MainConfig;
import com.hbm.lib.ModDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TaintEffect extends MobEffect {

    public TaintEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {

            // if (!(entity instanceof EntityCreeperTainted) && !(entity instanceof EntityTaintCrab)) {
            if (entity.level().random.nextInt(40) == 0) {
                DamageSource src = new DamageSource(
                        entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageSource.TAINT)
                );
                entity.hurt(src, (float) (amplifier + 1));
            }
            // }

            if (MainConfig.SERVER.TAINT_TRAILS.get()) {
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
        return true;
    }
}