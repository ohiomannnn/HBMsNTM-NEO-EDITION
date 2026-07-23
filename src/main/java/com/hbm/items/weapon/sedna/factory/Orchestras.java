package com.hbm.items.weapon.sedna.factory;

import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.items.weapon.sedna.GunBaseNTItem.LambdaContext;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.render.anim.AnimationEnums.GunAnimation;
import com.hbm.util.SoundUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.BiConsumer;

/** Orchestras are server-side components that run along client-side animations.
 * The orchestra only knows what animation is or was playing and how long it started, but not if it is still active.
 * Orchestras are useful for things like playing server-side sound, spawning casings or sending particle packets.*/
public class Orchestras {

    public static BiConsumer<ItemStack, LambdaContext> DEBUG_ORCHESTRA = (stack, ctx) -> {
        LivingEntity entity = ctx.entity;
        Level level = entity.level;
        if(!level.isClientSide) return;
        GunAnimation type = GunBaseNTItem.getLastAnim(stack, ctx.configIndex);
        int timer = GunBaseNTItem.getAnimTimer(stack, ctx.configIndex);

        if(type == GunAnimation.RELOAD) {
            if(timer == 3) SoundUtils.playAtVec3C(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_COCK.get(), SoundSource.PLAYERS);
            if(timer == 10) SoundUtils.playAtVec3C(level, entity.position(), NtmSoundEvents.GUN_MAG_SMALL_REMOVE.get(), SoundSource.PLAYERS);
            if(timer == 34) SoundUtils.playAtVec3C(level, entity.position(), NtmSoundEvents.GUN_MAG_SMALL_INSERT.get(), SoundSource.PLAYERS);
            if(timer == 40) SoundUtils.playAtVec3C(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_CLOSE.get(), SoundSource.PLAYERS);

           //if(timer == 16) {
           //    Receiver rec = ctx.config.getReceivers(stack)[0];
           //    IMagazine mag = rec.getMagazine(stack);
           //    SpentCasing casing = mag.getCasing(stack, ctx.inventory);
           //    if(casing != null) for(int i = 0; i < mag.getCapacity(stack); i++) CasingCreator.composeEffect(entity.worldObj, entity, 0.25, -0.125, -0.125, -0.05, 0, 0, 0.01, casing.getName());
           //}
        }
        //if(type == GunAnimation.CYCLE) {
        //    if(timer == 0) PacketDispatcher.wrapper.sendToAllAround(new MuzzleFlashPacket(entity), new TargetPoint(entity.worldObj.provider.dimensionId, entity.posX, entity.posY, entity.posZ, 100));
        //    if(timer == 11) entity.worldObj.playSoundAtEntity(entity, NTMSounds.GUN_REVOLVER_COCK, 1F, 1F);
        //}
        if(type == GunAnimation.CYCLE_DRY) {
            if(timer == 2) SoundUtils.playAtVec3C(level, entity.position(), NtmSoundEvents.GUN_DRY_FIRE.get(), SoundSource.PLAYERS);
            if(timer == 11) SoundUtils.playAtVec3C(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_COCK.get(), SoundSource.PLAYERS);
        }
        if(type == GunAnimation.INSPECT) {
            if(timer == 3) SoundUtils.playAtVec3C(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_COCK.get(), SoundSource.PLAYERS);
            if(timer == 16) SoundUtils.playAtVec3C(level, entity.position(), NtmSoundEvents.GUN_REVOLVER_CLOSE.get(), SoundSource.PLAYERS);
        }
    };
}
