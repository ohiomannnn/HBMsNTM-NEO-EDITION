package com.hbm.items.weapon.sedna.mods;

import net.minecraft.world.item.ItemStack;

public interface IWeaponMod {

    /** Lower numbers get installed and therefore evaluated first. Important when multiplicative and additive bonuses are supposed to stack */
    int getModPriority();
    String[] getSlots();
    /** The meat and bones of the upgrade eval. Requires the base value, the held gun, the value's
     * identifier and the yet unmodified parent (i.e. if the value is part of the receiver, that receiver) */
    <T> T eval(T base, ItemStack gun, String key, Object parent);

    default void onInstall(ItemStack gun, ItemStack mod, int index) { }
    default void onUninstall(ItemStack gun, ItemStack mod, int index) { }
}
