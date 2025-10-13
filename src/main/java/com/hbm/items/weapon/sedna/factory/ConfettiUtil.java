package com.hbm.items.weapon.sedna.factory;

import com.hbm.particle.helper.AshesCreator;
import com.hbm.util.DamageResistanceHandler.DamageClass;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Slime;

import java.util.Locale;

public class ConfettiUtil {
    public static void decideConfetti(LivingEntity entity, DamageSource source) {
        if (entity.isAlive()) return;
        if (source.type().equals(DamageClass.LASER.name().toLowerCase(Locale.US))) pulverize(entity);
        if (source.type().equals(DamageClass.ELECTRIC.name().toLowerCase(Locale.US))) pulverize(entity);
        if (source.is(DamageTypes.EXPLOSION)) gib(entity);
        if (source.is(DamageTypes.ON_FIRE)) cremate(entity);
    }

    public static void pulverize(LivingEntity entity) {
        int amount = Mth.clamp((int) (entity.getBbWidth() * entity.getBbHeight() * entity.getBbWidth() * 25), 5, 50);
        AshesCreator.composeEffect(entity.level(), entity, amount, 0.125F);
        //SkeletonCreator.composeEffect(entity.worldObj, entity, 1F);
        //entity.worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "hbm:weapon.fire.disintegration", 2.0F, 0.9F + entity.getRNG().nextFloat() * 0.2F);
    }

    public static void cremate(LivingEntity entity) {
        int amount = Mth.clamp((int) (entity.getBbWidth() * entity.getBbHeight() * entity.getBbWidth() * 25), 5, 50);
        AshesCreator.composeEffect(entity.level(), entity, amount, 0.125F);
        //SkeletonCreator.composeEffect(entity.worldObj, entity, 0.25F);
        //entity.worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "hbm:weapon.fire.disintegration", 2.0F, 0.9F + entity.getRNG().nextFloat() * 0.2F);
    }

    public static void gib(LivingEntity entity) {
        if (entity instanceof Skeleton) return;
        if (entity instanceof Slime) return;

//        SkeletonCreator.composeEffectGib(entity.worldObj, entity, 0.25F);
//
//        NBTTagCompound vdat = new NBTTagCompound();
//        vdat.setString("type", "giblets");
//        vdat.setInteger("ent", entity.getEntityId());
//        PacketThreading.createAllAroundThreadedPacket(new AuxParticlePacketNT(vdat, entity.posX, entity.posY + entity.height * 0.5, entity.posZ), new TargetPoint(entity.dimension, entity.posX, entity.posY + entity.height * 0.5, entity.posZ, 150));
//        entity.worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "mob.zombie.woodbreak", 2.0F, 0.95F + entity.getRNG().nextFloat() * 0.2F);
    }
}
