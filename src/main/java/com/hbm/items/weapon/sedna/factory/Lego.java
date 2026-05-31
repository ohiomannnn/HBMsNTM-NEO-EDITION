package com.hbm.items.weapon.sedna.factory;

import com.hbm.interfaces.NotableComments;
import com.hbm.items.weapon.sedna.BulletConfig;
import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.items.weapon.sedna.GunBaseNTItem.GunState;
import com.hbm.items.weapon.sedna.GunBaseNTItem.LambdaContext;
import com.hbm.items.weapon.sedna.GunConfig;
import com.hbm.items.weapon.sedna.Receiver;
import com.hbm.render.anim.BusAnimation;
import com.hbm.render.anim.AnimationEnums.GunAnimation;
import com.hbm.render.anim.BusAnimationSequence;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * "LEGO" - i.e. standardized building blocks which can be used to set up gun configs easily.
 *
 * small update, 24/11/03: this turned into fucking spaghetti. fuuuuuuuck.
 *
 * @author hbm
 */
@NotableComments
public class Lego {

    public static final RandomSource ANIM_RAND = RandomSource.create();

    /** Returns true if the mag has ammo in it. Used by keybind functions on whether to fire, and deciders on whether to trigger a refire. */
    public static BiFunction<ItemStack, LambdaContext, Boolean> LAMBDA_STANDARD_CAN_FIRE = (stack, ctx) -> { return ctx.config.getReceivers(stack)[0].getMagazine(stack).getAmount(stack, ctx.container) > 0; };
    public static BiFunction<ItemStack, LambdaContext, Boolean> LAMBDA_SECOND_CAN_FIRE = (stack, ctx) -> { return ctx.config.getReceivers(stack)[1].getMagazine(stack).getAmount(stack, ctx.container) > 0; };

    /** If IDLE and ammo is loaded, fire and set to JUST_FIRED. */
    public static BiConsumer<ItemStack, LambdaContext> LAMBDA_STANDARD_CLICK_PRIMARY = (stack, ctx) -> { clickReceiver(stack, ctx, 0); };

    public static void clickReceiver(ItemStack stack, LambdaContext ctx, int receiver) {

        LivingEntity entity = ctx.entity;
        Player player = ctx.getPlayer();
        Receiver rec = ctx.config.getReceivers(stack)[receiver];
        int index = ctx.configIndex;
        GunState state = GunBaseNTItem.getState(stack, index);

        if(state == GunState.IDLE) {

            if(rec.getCanFire(stack).apply(stack, ctx)) {
                rec.getOnFire(stack).accept(stack, ctx);

                //if(rec.getFireSound(stack) != null)
                //    entity.worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, rec.getFireSound(stack), rec.getFireVolume(stack), rec.getFirePitch(stack));

                int remaining = rec.getRoundsPerCycle(stack) - 1;
                for(int i = 0; i < remaining; i++) if(rec.getCanFire(stack).apply(stack, ctx)) rec.getOnFire(stack).accept(stack, ctx);

                GunBaseNTItem.setState(stack, index, GunState.COOLDOWN);
                GunBaseNTItem.setTimer(stack, index, rec.getDelayAfterFire(stack));
            } else {

                if(rec.getDoesDryFire(stack)) {
                    GunBaseNTItem.playAnimation(player, stack, GunAnimation.CYCLE_DRY, index);
                    GunBaseNTItem.setState(stack, index, rec.getRefireAfterDry(stack) ? GunState.COOLDOWN : GunState.DRAWING);
                    GunBaseNTItem.setTimer(stack, index, rec.getDelayAfterDryFire(stack));
                }
            }
        }

        if(state == GunState.RELOADING) {
            GunBaseNTItem.setReloadCancel(stack, true);
        }
    }

    public static float getStandardWearSpread(ItemStack stack, GunConfig config, int index) {
        float percent = GunBaseNTItem.getWear(stack, index) / config.getDurability(stack);
        if(percent < 0.5F) return 0F;
        return (percent - 0.5F) * 2F;
    }

    /** Returns the standard multiplier for damage based on wear */
    public static float getStandardWearDamage(ItemStack stack, GunConfig config, int index) {
        float percent = GunBaseNTItem.getWear(stack, index) / config.getDurability(stack);
        if(percent < 0.75F) return 1F;
        return 1F - (percent - 0.75F) * 2F;
    }

    /** Returns the full calculated damage based on guncfg and wear */
    public static float calcDamage(LambdaContext ctx, ItemStack stack, Receiver primary, boolean calcWear, int index) {
        return primary.getBaseDamage(stack) * (calcWear ? getStandardWearDamage(stack, ctx.config, index) : 1);
    }

    public static float calcSpread(LambdaContext ctx, ItemStack stack, Receiver primary, BulletConfig config, boolean calcWear, int index, boolean aim) {
        // the gun's innate spread, SMGs will have poor accuracy no matter what
        float spreadInnate = primary.getInnateSpread(stack);
        // the ammo's spread (for example for buckshot) multiplied with the gun's ammo modifier (choke or sawed off barrel)
        float spreadAmmo = config.spread * primary.getAmmoSpread(stack);
        // hipfire penalty, i.e. extra spread when not aiming
        float spreadHipfire = aim ? 0F : primary.getHipfireSpread(stack);
        // extra spread caused by weapon durability, [0;0.125] by default
        float spreadWear = !calcWear ? 0F : (getStandardWearSpread(stack, ctx.config, index) * primary.getDurabilitySpread(stack));

        return spreadInnate + spreadAmmo + spreadHipfire + spreadWear;
    }

    /** anims for the DEBUG revolver, mostly a copy of the li'lpip but with some fixes regarding the cylinder movement */
    public static BiFunction<ItemStack, GunAnimation, BusAnimation> LAMBDA_DEBUG_ANIMS = (stack, type) -> switch (type) {
        case CYCLE -> new BusAnimation()
                .addBus("RECOIL", new BusAnimationSequence().addPos(0, 0, 0, 50).addPos(0, 0, -3, 50).addPos(0, 0, 0, 250))
                .addBus("HAMMER", new BusAnimationSequence().addPos(0, 0, 1, 50).addPos(0, 0, 1, 400).addPos(0, 0, 0, 200))
                .addBus("DRUM", new BusAnimationSequence().addPos(0, 0, 0, 450).addPos(0, 0, 1, 200));
        case CYCLE_DRY -> new BusAnimation()
                .addBus("HAMMER", new BusAnimationSequence().addPos(0, 0, 1, 50).addPos(0, 0, 1, 300 + 100).addPos(0, 0, 0, 200))
                .addBus("DRUM", new BusAnimationSequence().addPos(0, 0, 0, 450).addPos(0, 0, 1, 200));
        case EQUIP -> new BusAnimation().addBus("ROTATE", new BusAnimationSequence().addPos(-360, 0, 0, 350));
        case RELOAD -> new BusAnimation()
                .addBus("RELAOD_TILT", new BusAnimationSequence().addPos(-15, 0, 0, 100).addPos(65, 0, 0, 100).addPos(45, 0, 0, 50).addPos(0, 0, 0, 200).addPos(0, 0, 0, 1450).addPos(-80, 0, 0, 100).addPos(-80, 0, 0, 100).addPos(0, 0, 0, 200))
                .addBus("RELOAD_CYLINDER", new BusAnimationSequence().addPos(0, 0, 0, 200).addPos(90, 0, 0, 100).addPos(90, 0, 0, 1700).addPos(0, 0, 0, 70))
                .addBus("RELOAD_LIFT", new BusAnimationSequence().addPos(0, 0, 0, 350).addPos(-45, 0, 0, 250).addPos(-45, 0, 0, 350).addPos(-15, 0, 0, 200).addPos(-15, 0, 0, 1050).addPos(0, 0, 0, 100))
                .addBus("RELOAD_JOLT", new BusAnimationSequence().addPos(0, 0, 0, 600).addPos(2, 0, 0, 50).addPos(0, 0, 0, 100))
                .addBus("RELOAD_BULLETS", new BusAnimationSequence().addPos(0, 0, 0, 650).addPos(10, 0, 0, 300).addPos(10, 0, 0, 200).addPos(0, 0, 0, 700))
                .addBus("RELOAD_BULLETS_CON", new BusAnimationSequence().addPos(1, 0, 0, 0).addPos(1, 0, 0, 950).addPos(0, 0, 0, 1));
        case INSPECT, JAMMED -> new BusAnimation()
                .addBus("RELAOD_TILT", new BusAnimationSequence().addPos(-15, 0, 0, 100).addPos(65, 0, 0, 100).addPos(45, 0, 0, 50).addPos(0, 0, 0, 200).addPos(0, 0, 0, 200).addPos(-80, 0, 0, 100).addPos(-80, 0, 0, 100).addPos(0, 0, 0, 200))
                .addBus("RELOAD_CYLINDER", new BusAnimationSequence().addPos(0, 0, 0, 200).addPos(90, 0, 0, 100).addPos(90, 0, 0, 450).addPos(0, 0, 0, 70));
        default -> null;
    };

    /*
     * Be honest. Do you genuinely think posting a random screenshot of your game with absolutely ZERO context of what modpack, what
     * Shaders if any or literally any context at all would come to a magic solution?
     * For all we know you accidentally rubbed Vaseline all over your monitor and jizzed in the hdmi socket of your pc
     *
     * ~ u/Wolfyy47_, 2024
     */
}
