package com.hbm.items.weapon.sedna.mags;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/** Uses individual bullets which are loaded all at once */
public class MagazineFullReload extends MagazineSingleTypeBase {

    public MagazineFullReload(int index, int capacity) {
        super(index, capacity);
    }

    /** Reloads all rounds at once. If the mag is empty, the mag's type will change to the first valid ammo type */
    @Override
    public void reloadAction(ItemStack stack, Container container) {
        standardReload(stack, container, this.capacity);
    }
}
