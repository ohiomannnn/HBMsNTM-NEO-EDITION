package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.MachineCentrifugeBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineCentrifugeMenu extends MenuBase<MachineCentrifugeBlockEntity> {

    public MachineCentrifugeMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineCentrifugeBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level, extraData.readBlockPos()));
    }

    public MachineCentrifugeMenu(int id, Inventory inventory, MachineCentrifugeBlockEntity be) {
        super(NtmMenuTypes.MACHINE_CENTRIFUGE.get(), id, be);

        this.addSlot(new SlotNonRetarded(be, 0, 36, 50));
        this.addSlot(new SlotNonRetarded(be, 1, 9, 50));

        this.addSlot(new SlotTakeOnly(be, 2, 63, 50));
        this.addSlot(new SlotTakeOnly(be, 3, 83, 50));
        this.addSlot(new SlotTakeOnly(be, 4, 103, 50));
        this.addSlot(new SlotTakeOnly(be, 5, 123, 50));

        this.addSlot(new SlotNonRetarded(be, 6, 149, 22));
        this.addSlot(new SlotNonRetarded(be, 7, 149, 40));

        this.playerInv(inventory, 8, 104, 162);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index < 8) {
                if(!this.moveItemStackTo(stack, 8, this.slots.size(), true)) return ItemStack.EMPTY;
            } else {
                if(stack.getItem() instanceof IBatteryItem) {
                    if(!this.moveItemStackTo(stack, 1, 2, false)) return ItemStack.EMPTY;
                } else if(stack.getItem() instanceof MachineUpgradeItem) {
                    if(!this.moveItemStackTo(stack, 6, 8, false)) return ItemStack.EMPTY;
                } else if(!this.moveItemStackTo(stack, 0, 1, false)) {
                    return ItemStack.EMPTY;
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
