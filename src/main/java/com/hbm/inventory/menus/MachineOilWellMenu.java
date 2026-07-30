package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.oil.OilDrillBaseBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineOilWellMenu<T extends OilDrillBaseBlockEntity> extends MenuBase<T> {

    @SuppressWarnings("unchecked")
    public MachineOilWellMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (T) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineOilWellMenu(int id, Inventory inventory, T be) {
        super(NtmMenuTypes.MACHINE_OIL_WELL.get(), id, be);

        // Battery
        this.addSlot(new Slot(be, 0, 8, 58));
        // Canister Input
        this.addSlot(new Slot(be, 1, 94, 22));
        // Canister Output
        this.addSlot(new SlotTakeOnly(be, 2, 94, 58));
        // Gas Input
        this.addSlot(new Slot(be, 3, 130, 22));
        // Gas Output
        this.addSlot(new SlotTakeOnly(be, 4, 130, 58));
        // Upgrades
        this.addSlot(new Slot(be, 5, 156, 36));
        this.addSlot(new Slot(be, 6, 156, 54));

        this.playerInv(inventory, 12, 108, 166);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index <= 7) {
                if(!this.moveItemStackTo(stack, 8, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if(stack.getItem() instanceof MachineUpgradeItem) {
                    if(!this.moveItemStackTo(stack, 5, 8, true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if(!this.moveItemStackTo(stack, 0, 2, false)) {
                        if(!this.moveItemStackTo(stack, 3, 4, false)) {
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

        return ret;
    }
}
