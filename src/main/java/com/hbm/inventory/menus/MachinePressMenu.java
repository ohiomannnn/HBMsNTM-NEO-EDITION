package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachinePressBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.items.machine.StampItem;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachinePressMenu extends MenuBase<MachinePressBlockEntity> {

    public MachinePressMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachinePressBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level, extraData.readBlockPos()));
    }

    public MachinePressMenu(int id, Inventory inventory, MachinePressBlockEntity be) {
        super(NtmMenuTypes.PRESS.get(), id, be);

        // Coal
        this.addSlot(new Slot(be, 0, 26, 53));
        // Stamp
        this.addSlot(new Slot(be, 1, 80, 17));
        // Input
        this.addSlot(new Slot(be, 2, 80, 53));
        // Output
        this.addSlot(new SlotCraftingOutput(inventory.player, be, 3, 140, 35));
        // Extra Storage
        this.addSlots(be, 4, 8, 84, 1, 9);

        this.playerInv(inventory, 8, 132);
    }


    @Override
    public ItemStack quickMoveStack(Player player, int index) {

        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {

            ItemStack stack = slot.getItem();
            newStack = stack.copy();

            if(index <= 12) {
                if(!this.moveItemStackTo(stack, 14, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if(newStack.getBurnTime(null) > 0) {
                    if(!this.moveItemStackTo(stack, 0, 1, false)) {
                        if(!this.moveItemStackTo(stack, 4, 13, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                } else if(newStack.getItem() instanceof StampItem) {
                    if(!this.moveItemStackTo(stack, 1, 2, false)) {
                        if(!this.moveItemStackTo(stack, 4, 13, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                } else {
                    if(!this.moveItemStackTo(stack, 2, 3, false)) {
                        if(!this.moveItemStackTo(stack, 4, 13, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if(stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }
}
