package com.hbm.inventory;

import com.hbm.interfaces.NotableComments;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;


/**
 * mojang fixed ts in newer versions
 * soo i had no reason to port this :(
 */
@NotableComments
public class SlotNonRetarded extends Slot {

    public SlotNonRetarded(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    /**
     * Dear mojang: Why wasn't that the standard to begin with? What do Containers have canPlaceItem for when by default nothing uses it?
     */
    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.container.canPlaceItem(this.getSlotIndex(), stack);
    }

    /**
     * Because if slots have higher stack sizes than the maximum allowed by the tile, the display just stops working.
     * Why was that necessary? Sure it's not intended but falsifying information isn't very cool.
     */
    @Override
    public int getMaxStackSize() {
        return Math.max(
                this.container.getMaxStackSize(),
                this.hasItem() ? this.getItem().getCount() : 1
        );
    }
}
