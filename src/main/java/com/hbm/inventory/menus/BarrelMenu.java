package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.storage.BarrelBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BarrelMenu extends MenuBase<BarrelBlockEntity> {

    public BarrelMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (BarrelBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public BarrelMenu(int id, Inventory inventory, BarrelBlockEntity be) {
        super(NtmMenuTypes.BARREL.get(), id, be);

        this.addSlot(new Slot(be, 0, 8, 17));
        this.addSlot(new Slot(be, 1, 8, 53));
        this.addSlot(new Slot(be, 2, 35, 17));
        this.addSlot(new Slot(be, 3, 35, 53));
        this.addSlot(new Slot(be, 4, 125, 17));
        this.addSlot(new Slot(be, 5, 125, 53));

        this.playerInv(inventory, 8, 84);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.be.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            newStack = stack.copy();

            if(index <= 5) {
                if(!this.moveItemStackTo(stack, this.be.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if(!this.moveItemStackTo(stack, 0, this.be.getContainerSize(), false)) {
                    return ItemStack.EMPTY;
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
