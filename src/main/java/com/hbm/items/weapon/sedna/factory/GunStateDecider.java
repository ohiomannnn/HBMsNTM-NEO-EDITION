package com.hbm.items.weapon.sedna.factory;

import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.items.weapon.sedna.GunBaseNTItem.GunState;
import com.hbm.items.weapon.sedna.GunBaseNTItem.LambdaContext;
import com.hbm.items.weapon.sedna.GunConfig;
import com.hbm.items.weapon.sedna.Receiver;
import com.hbm.items.weapon.sedna.mags.IMagazine;
import com.hbm.render.anim.AnimationEnums.GunAnimation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

public class GunStateDecider {

    /**
     * The meat and bones of the gun system's state machine.
     * This standard decider can handle guns with an automatic primary receiver, as well as one receiver's reloading state.
     * It supports draw delays as well as semi and auto fire with a standard left click refire check.
     * Only handles single receiver weapons!
     */
    public static BiConsumer<ItemStack, LambdaContext> LAMBDA_STANDARD_DECIDER = (stack, ctx) -> {
        int index = ctx.configIndex;
        GunState lastState = GunBaseNTItem.getState(stack, index);
        deciderStandardFinishDraw(stack, lastState, index);
        deciderStandardClearJam(stack, lastState, index);
        deciderStandardReload(stack, ctx, lastState, 0, index);
        deciderAutoRefire(stack, ctx, lastState, 0, index, () -> { return GunBaseNTItem.getPrimary(stack, index) && GunBaseNTItem.getMode(stack, ctx.configIndex) == 0; });
    };

    /** Transitions the gun from DRAWING to IDLE */
    public static void deciderStandardFinishDraw(ItemStack stack, GunState lastState, int index) {

        //transition to idle
        if(lastState == GunState.DRAWING) {
            GunBaseNTItem.setState(stack, index, GunState.IDLE);
            GunBaseNTItem.setTimer(stack, index, 0);
        }
    }

    /** Transitions the gun from DRAWING to IDLE */
    public static void deciderStandardClearJam(ItemStack stack, GunState lastState, int index) {

        //transition to idle
        if(lastState == GunState.JAMMED) {
            GunBaseNTItem.setState(stack, index, GunState.IDLE);
            GunBaseNTItem.setTimer(stack, index, 0);
        }
    }

    /** Triggers a reload action on the first receiver. If the mag is not full and reloading is still possible, set to RELOADING, otherwise IDLE */
    public static void deciderStandardReload(ItemStack stack, LambdaContext ctx, GunState lastState, int recIndex, int gunIndex) {

        if(lastState == GunState.RELOADING) {

            LivingEntity entity = ctx.entity;
            Player player = ctx.getPlayer();
            GunConfig cfg = ctx.config;
            Receiver rec = cfg.getReceivers(stack)[recIndex];
            IMagazine mag = rec.getMagazine(stack);

            mag.reloadAction(stack, ctx.container);
            boolean cancel = GunBaseNTItem.getReloadCancel(stack);

            //if after reloading the gun can still reload, assume a tube mag and resume reloading
            if(!cancel && mag.canReload(stack, ctx.container)) {
                GunBaseNTItem.setState(stack, gunIndex, GunState.RELOADING);
                GunBaseNTItem.setTimer(stack, gunIndex, rec.getReloadCycleDuration(stack));
                GunBaseNTItem.playAnimation(player, stack, GunAnimation.RELOAD_CYCLE, gunIndex);
                //if no more reloading can be done, go idle
            } else {

                if(getStandardJamChance(stack, cfg, gunIndex) > entity.random.nextFloat()) {
                    GunBaseNTItem.setState(stack, gunIndex, GunState.JAMMED);
                    GunBaseNTItem.setTimer(stack, gunIndex, rec.getJamDuration(stack));
                    GunBaseNTItem.playAnimation(player, stack, GunAnimation.JAMMED, gunIndex);
                } else {
                    GunBaseNTItem.setState(stack, gunIndex, GunState.DRAWING);
                    int duration = rec.getReloadEndDuration(stack) + (mag.getAmountBeforeReload(stack) <= 0 ? rec.getReloadCockOnEmptyPost(stack) : 0);
                    GunBaseNTItem.setTimer(stack, gunIndex, duration);
                    GunBaseNTItem.playAnimation(player, stack, GunAnimation.RELOAD_END, gunIndex);
                }

                GunBaseNTItem.setReloadCancel(stack, false);
            }

            mag.setAmountAfterReload(stack, mag.getAmount(stack, ctx.container));
        }
    }

    public static float getStandardJamChance(ItemStack stack, GunConfig config, int index) {
        float percent = GunBaseNTItem.getWear(stack, index) / config.getDurability(stack);
        if(percent < 0.66F) return 0F;
        return Math.min((percent - 0.66F) * 4F, 1F);
    }

    /** Triggers a re-fire of the primary if the fire delay has expired, the left mouse button is down and re-firing is enabled, otherwise switches to IDLE */
    public static void deciderAutoRefire(ItemStack stack, LambdaContext ctx, GunState lastState, int recIndex, int gunIndex, BooleanSupplier refireCondition) {

        if(lastState == GunState.COOLDOWN) {

            LivingEntity entity = ctx.entity;
            Player player = ctx.getPlayer();
            GunConfig cfg = ctx.config;
            Receiver rec = cfg.getReceivers(stack)[recIndex];

            //if the gun supports re-fire (i.e. if it's an auto)
            if(rec.getRefireOnHold(stack) && refireCondition.getAsBoolean()) {
                //if there's a bullet loaded, fire again
                if(rec.getCanFire(stack).apply(stack, ctx)) {
                    rec.getOnFire(stack).accept(stack, ctx);
                    GunBaseNTItem.setState(stack, gunIndex, GunState.COOLDOWN);
                    GunBaseNTItem.setTimer(stack, gunIndex, rec.getDelayAfterFire(stack));

                    //if(rec.getFireSound(stack) != null) entity.level.playSoundEffect(entity.posX, entity.posY, entity.posZ, rec.getFireSound(stack), rec.getFireVolume(stack), rec.getFirePitch(stack));

                    int remaining = rec.getRoundsPerCycle(stack) - 1;
                    for(int i = 0; i < remaining; i++) if(rec.getCanFire(stack).apply(stack, ctx)) rec.getOnFire(stack).accept(stack, ctx);
                    //if not, check if dry firing is allowed for refires
                } else if(rec.getDoesDryFireAfterAuto(stack)) {
                    //if refire after dry is allowed, switch to COOLDOWN which will trigger a refire, otherwise switch to DRAWING
                    GunBaseNTItem.setState(stack, gunIndex, rec.getRefireAfterDry(stack) ? GunState.COOLDOWN : GunState.DRAWING);
                    GunBaseNTItem.setTimer(stack, gunIndex, rec.getDelayAfterDryFire(stack));
                    GunBaseNTItem.playAnimation(player, stack, GunAnimation.CYCLE_DRY, gunIndex);
                    //if not, revert to idle
                } else {
                    GunBaseNTItem.setState(stack, gunIndex, GunState.IDLE);
                    GunBaseNTItem.setTimer(stack, gunIndex, 0);
                }
                //if not, go idle
            } else {

                //reload on empty, only for non-refiring guns
                if(rec.getReloadOnEmpty(stack) && rec.getMagazine(stack).getAmount(stack, ctx.container) <= 0) {
                    GunBaseNTItem.setIsAiming(stack, false);
                    IMagazine mag = rec.getMagazine(stack);

                    if(mag.canReload(stack, ctx.container)) {
                        int loaded = mag.getAmount(stack, ctx.container);
                        mag.setAmountBeforeReload(stack, loaded);
                        GunBaseNTItem.setState(stack, ctx.configIndex, GunState.RELOADING);
                        GunBaseNTItem.setTimer(stack, ctx.configIndex, rec.getReloadBeginDuration(stack) + (loaded <= 0 ? rec.getReloadCockOnEmptyPre(stack) : 0));
                        GunBaseNTItem.playAnimation(player, stack, GunAnimation.RELOAD, ctx.configIndex);
                    } else {
                        GunBaseNTItem.setState(stack, gunIndex, GunState.IDLE);
                        GunBaseNTItem.setTimer(stack, gunIndex, 0);
                    }

                } else {
                    GunBaseNTItem.setState(stack, gunIndex, GunState.IDLE);
                    GunBaseNTItem.setTimer(stack, gunIndex, 0);
                }
            }
        }
    }
}
