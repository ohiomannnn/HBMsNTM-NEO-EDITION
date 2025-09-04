package com.hbm.explosion.vanillalike.standard;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import com.hbm.explosion.vanillalike.interfaces.IExplosionSFX;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ExplosionEffectStandard implements IExplosionSFX {
    @Override
    public void doEffect(ExplosionVNT explosion, Level level, double x, double y, double z, float size) {
        if (level.isClientSide) return;

        Vec3 centerVec = new Vec3(x, y, z);
        BlockPos centerPos = BlockPos.containing(centerVec);

        AABB area = new AABB(x - size, y - size, z - size,
                x + size, y + size, z + size);

        BlockPos.betweenClosedStream(area).forEach(blockPos -> {
            double distance = Math.sqrt(blockPos.distSqr(centerPos));
            if (distance <= size) {
                BlockState blockState = level.getBlockState(blockPos);
                if (!blockState.isAir()) {
                    level.removeBlock(blockPos, false);
                }
            }
        });

        List<Entity> entities = level.getEntitiesOfClass(Entity.class, area);
        for (Entity entity : entities) {
            double distance = entity.position().distanceTo(centerVec);
            if (distance <= size) {
                float damage = (1.0F - (float) (distance / size)) * 20.0F;

                DamageSource damageSource = level.damageSources().source(DamageTypes.GENERIC);
                entity.hurt(damageSource, damage);
            }
        }

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.EXPLOSION_EMITTER,
                    x, y, z,
                    1, 0.0, 0.0, 0.0, 0.0
            );
        }

        level.playSound(
                null,
                x,y,z,
                SoundEvents.GENERIC_EXPLODE,
                SoundSource.AMBIENT,
                4.0F,
                (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F
        );
    }
}