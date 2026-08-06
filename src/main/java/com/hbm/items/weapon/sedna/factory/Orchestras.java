package com.hbm.items.weapon.sedna.factory;

import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.items.weapon.sedna.GunBaseNTItem.LambdaContext;
import com.hbm.items.weapon.sedna.Receiver;
import com.hbm.items.weapon.sedna.mags.IMagazine;
import com.hbm.network.toclient.MuzzleFlashPacket;
import com.hbm.particle.SpentCasing;
import com.hbm.particle.helper.CasingCreator;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.render.anim.AnimationEnums.GunAnimation;
import com.hbm.util.SoundUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.BiConsumer;

/** Orchestras are server-side components that run along client-side animations.
 * The orchestra only knows what animation is or was playing and how long it started, but not if it is still active.
 * Orchestras are useful for things like playing server-side sound, spawning casings or sending particle packets.*/
public class Orchestras {

    public static BiConsumer<ItemStack, LambdaContext> DEBUG_ORCHESTRA = (stack, ctx) -> {
        LivingEntity entity = ctx.entity;
        Level level = entity.level;
        if(!(level instanceof ServerLevel serverLevel)) return;
        GunAnimation type = GunBaseNTItem.getLastAnim(stack, ctx.configIndex);
        int timer = GunBaseNTItem.getAnimTimer(stack, ctx.configIndex);

        if(type == GunAnimation.RELOAD) {
            if(timer == 3) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_COCK.get(), entity.getSoundSource());
            if(timer == 10) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_MAG_SMALL_REMOVE.get(), entity.getSoundSource());
            if(timer == 34) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_MAG_SMALL_INSERT.get(), entity.getSoundSource());
            if(timer == 40) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_CLOSE.get(), entity.getSoundSource());

           if(timer == 16) {
               Receiver rec = ctx.config.getReceivers(stack)[0];
               IMagazine mag = rec.getMagazine(stack);
               SpentCasing casing = mag.getCasing(stack, ctx.container);
               if(casing != null) for(int i = 0; i < mag.getCapacity(stack); i++) CasingCreator.composeEffect(entity.level, entity, 0.25, -0.125, -0.125, -0.05, 0, 0, 0.01, casing.getName());
           }
        }
        if(type == GunAnimation.CYCLE) {
            if(timer == 0) PacketDistributor.sendToPlayersNear(serverLevel, null, entity.getX(), entity.getY(), entity.getZ(), 100, new MuzzleFlashPacket(entity.getId()));
            if(timer == 11) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_COCK.get(), entity.getSoundSource());
        }
        if(type == GunAnimation.CYCLE_DRY) {
            if(timer == 2) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_DRY_FIRE.get(), entity.getSoundSource());
            if(timer == 11) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_COCK.get(), entity.getSoundSource());
        }
        if(type == GunAnimation.INSPECT) {
            if(timer == 3) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_COCK.get(), entity.getSoundSource());
            if(timer == 16) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_CLOSE.get(), entity.getSoundSource());
        }
    };

    public static BiConsumer<ItemStack, LambdaContext> ORCHESTRA_MARESLEG = (stack, ctx) -> {
        LivingEntity entity = ctx.entity;
        Level level = entity.level;
        if(!(level instanceof ServerLevel serverLevel)) return;
        GunAnimation type = GunBaseNTItem.getLastAnim(stack, ctx.configIndex);
        int timer = GunBaseNTItem.getAnimTimer(stack, ctx.configIndex);
        boolean aiming = GunBaseNTItem.getIsAiming(stack);

        if(type == GunAnimation.RELOAD) {
            if(timer == 8) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_COCK.get(), entity.getSoundSource(), 1F, 0.8F);
            if(timer == 16) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_SHOTGUN_LOAD.get(), entity.getSoundSource(), 1F, 1F);
        }
        if(type == GunAnimation.RELOAD_CYCLE) {
            if(timer == 0) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_SHOTGUN_LOAD.get(), entity.getSoundSource(), 1F, 1F);
        }
        if(type == GunAnimation.RELOAD_END) {
            if(timer == 2) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_COCK.get(), entity.getSoundSource(), 1F, 0.7F);
        }
        if(type == GunAnimation.JAMMED) {
            if(timer == 2) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_COCK.get(), entity.getSoundSource(), 1F, 0.7F);
            if(timer == 17) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_LEVER_COCK.get(), entity.getSoundSource(), 1F, 0.8F);
            if(timer == 29) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_LEVER_COCK.get(), entity.getSoundSource(), 1F, 0.8F);
        }
        if(type == GunAnimation.CYCLE) {
            if(timer == 0) PacketDistributor.sendToPlayersNear(serverLevel, null, entity.getX(), entity.getY(), entity.getZ(), 100, new MuzzleFlashPacket(entity.getId()));
            if(timer == 14) {
                SpentCasing casing = ctx.config.getReceivers(stack)[0].getMagazine(stack).getCasing(stack, ctx.container);
                if(casing != null) CasingCreator.composeEffect(entity.level, entity, 0.3125, -0.125, aiming ? -0.125 : -0.375D, 0, 0.18, -0.12, 0.01, -10F + (float)entity.random.nextGaussian() * 5F, (float)entity.random.nextGaussian() * 2.5F, casing.getName(), true, 60, 0.5D, 20);
            }
            if(timer == 8) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_LEVER_COCK.get(), entity.getSoundSource(), 1F, 0.8F);
        }
        if(type == GunAnimation.CYCLE_DRY) {
            if(timer == 2) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_DRY_FIRE.get(), entity.getSoundSource(), 1F, 1F);
            if(timer == 8) SoundUtils.playAtVec3(level, entity.position(), NtmSoundEvents.GUN_LEVER_COCK.get(), entity.getSoundSource(), 1F, 0.8F);
        }
    };
}
