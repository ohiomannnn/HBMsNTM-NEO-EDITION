package com.hbm.items.weapon.sedna.factory;

import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.particle.helper.AshesCreator;
import com.hbm.particle.helper.SkeletonCreator;
import com.hbm.util.DamageSourceNT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class ConfettiUtil {

    public static void decideConfetti(LivingEntity entity, DamageSourceNT source) {
        if (entity.isAlive()) return;
        if (source.isExplosion()) gib(entity);
        if (source.isFireDamage()) cremate(entity);
    }

    public static void pulverize(LivingEntity entity) {
        Level level = entity.level();

        int amount = Mth.clamp((int) (entity.getBbWidth() * entity.getBbHeight() * entity.getBbWidth() * 25), 5, 50);
        AshesCreator.composeEffect(level, entity, amount, 0.125F);
        SkeletonCreator.composeEffect(level, entity, 1F);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.FIRE_DISINTEGRATION, SoundSource.AMBIENT, 2.0F, 0.9F + entity.getRandom().nextFloat() * 0.2F);
    }

    public static void cremate(LivingEntity entity) {
        Level level = entity.level();

        int amount = Mth.clamp((int) (entity.getBbWidth() * entity.getBbHeight() * entity.getBbWidth() * 25), 5, 50);
        AshesCreator.composeEffect(level, entity, amount, 0.125F);
        SkeletonCreator.composeEffect(level, entity, 0.25F);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.FIRE_DISINTEGRATION, SoundSource.AMBIENT, 2.0F, 0.9F + entity.getRandom().nextFloat() * 0.2F);
    }

    public static void gib(LivingEntity entity) {
        Level level = entity.level();

        if (entity instanceof Slime) return;

        SkeletonCreator.composeEffectGib(level, entity, 0.25F);

        if (entity instanceof AbstractSkeleton) return;

        CompoundTag tag = new CompoundTag();
        tag.putString("type", "giblets");
        tag.putInt("ent", entity.getId());
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersNear(serverLevel, null, entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(), 150, new AuxParticle(tag, entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ()));
            serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.BLOCKS, 2.0F, 0.95F + entity.getRandom().nextFloat() * 0.2F);
        }
    }
}
