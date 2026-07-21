package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.MachineShredderBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.items.NtmItems;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineShredderMenu extends MenuBase<MachineShredderBlockEntity> {

    public MachineShredderMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineShredderBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level, extraData.readBlockPos()));
    }

    public MachineShredderMenu(int id, Inventory inventory, MachineShredderBlockEntity be) {
        super(NtmMenuTypes.MACHINE_SHREDDER.get(), id, be);

        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                this.addSlot(new SlotNonRetarded(be, col + row * 3, 44 + col * 18, 18 + row * 18));
            }
        }

        for(int row = 0; row < 6; row++) {
            for(int col = 0; col < 3; col++) {
                this.addSlot(new SlotTakeOnly(be, 9 + col + row * 3, 116 + col * 18, 18 + row * 18));
            }
        }

        this.addSlot(new SlotNonRetarded(be, 27, 44, 108));
        this.addSlot(new SlotNonRetarded(be, 28, 80, 108));
        this.addSlot(new SlotNonRetarded(be, 29, 8, 108));

        this.playerInv(inventory, 8, 151, 209);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index < 30) {
                if(!this.moveItemStackTo(stack, 30, this.slots.size(), true)) return ItemStack.EMPTY;
            } else {
                if(stack.getItem() instanceof IBatteryItem) {
                    if(!this.moveItemStackTo(stack, 29, 30, false)) return ItemStack.EMPTY;
                } else if(isBlade(stack)) {
                    if(!this.moveItemStackTo(stack, 27, 29, false)) return ItemStack.EMPTY;
                } else if(!this.moveItemStackTo(stack, 0, 9, false)) {
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

    private static boolean isBlade(ItemStack stack) {
        return stack.getItem() == NtmItems.BLADES_STEEL.get()
                || stack.getItem() == NtmItems.BLADES_TITANIUM.get()
                || stack.getItem() == NtmItems.BLADES_DESH.get();
    }
}
