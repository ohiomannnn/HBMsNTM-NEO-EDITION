package com.hbm.items.weapon.sedna.mags;

import com.hbm.particle.SpentCasing;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * The magazine simply provides the receiver it's attached to with ammo, the receiver does not care where it comes from.
 * Therefore it is the mag's responsibility to handle reloading, any type restrictions as well as belt-like action from "magless" guns.
 *
 * @author hbm
 */
public interface IMagazine<T> {

    /** What ammo is loaded currently */
    T getType(ItemStack stack, Container container);
    /** Sets the mag's ammo type */
    void setType(ItemStack stack, T type);
    /** How much ammo this mag can carry */
    int getCapacity(ItemStack stack);
    /** How much ammo is currently loaded */
    int getAmount(ItemStack stack, Container container);
    /** Sets the mag's ammo level */
    void setAmount(ItemStack stack, int amount);
    /** removes the specified amount from the magazine */
    void useUpAmmo(ItemStack stack, Container container, int amount);
    /** If a reload can even be initiated, i.e. the player even has bullets to load, inventory can be null */
    boolean canReload(ItemStack stack, Container container);
    /** On the begin of a reload, potentially change the mag type before the reload happens for animation purposes */
    void initNewType(ItemStack stack, Container container);
    /** The action done at the end of one reload cycle, either loading one shell or replacing the whole mag, inventory can be null */
    void reloadAction(ItemStack stack, Container container);
    /** The stack that should be displayed for the ammo HUD */
    ItemStack getIconForHUD(ItemStack stack, Player player);
    /** It explains itself */
    String reportAmmoStateForHUD(ItemStack stack, Player player);
    /** Casing config to use then ejecting */
    SpentCasing getCasing(ItemStack stack, Container container);
    /** When reloading, remember the amount before reload is initiated */
    void setAmountBeforeReload(ItemStack stack, int amount);
    /** Amount of rounds before reload has started. Do note that the NBT stack sync likely arrives
     * after the animation packets, so for RELOAD type anims, use the live ammo count instead! */
    int getAmountBeforeReload(ItemStack stack);
    /** Sets amount of ammo after each reload operation */
    void setAmountAfterReload(ItemStack stack, int amount);
    /** Cached amount of ammo after the most recent reload */
    int getAmountAfterReload(ItemStack stack);

//    static void handleAmmoBag(Container container, BulletConfig config, int shotsFired) {
//        if(config.casingItem != null && config.casingAmount > 0 && container instanceof Inventory inv) {
//            for(ItemStack stack : inv.items) {
//                if(stack != null && stack.getItem() == NtmItems.casing_bag && ItemCasingBag.pushCasing(stack, config.casingItem, 1F / config.casingAmount * 0.5F * shotsFired)) return;
//            }
//        }
//    }

    static boolean shouldUseUpTrenchie(Container container) {
        if(container instanceof Inventory inv) {
            boolean trenchie = false;//ArmorTrenchmaster.isTrenchMaster(invPlayer.player);
            boolean aos = false;//ArmorTrenchmaster.hasAoS(invPlayer.player);
            if(trenchie || aos) return inv.player.random.nextInt(3) < 2;
        }
        return true;
    }
}
