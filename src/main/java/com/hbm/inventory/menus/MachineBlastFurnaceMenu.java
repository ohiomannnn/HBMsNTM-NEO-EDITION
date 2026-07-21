package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineBlastFurnaceBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineBlastFurnaceMenu extends MenuBase<MachineBlastFurnaceBlockEntity> {

    public MachineBlastFurnaceMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineBlastFurnaceBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineBlastFurnaceMenu(int id, Inventory inventory, MachineBlastFurnaceBlockEntity be) {
        super(NtmMenuTypes.MACHINE_BLAST_FURNACE.get(), id, be);

        this.addSlot(new SlotNonRetarded(be, MachineBlastFurnaceBlockEntity.SLOT_FUEL, 80, 81));
        this.addSlot(new SlotNonRetarded(be, MachineBlastFurnaceBlockEntity.SLOT_INPUT_1, 80, 27));
        this.addSlot(new SlotNonRetarded(be, MachineBlastFurnaceBlockEntity.SLOT_INPUT_2, 80, 45));
        this.addSlot(new SlotTakeOnly(be, MachineBlastFurnaceBlockEntity.SLOT_OUTPUT_1, 134, 72));
        this.addSlot(new SlotTakeOnly(be, MachineBlastFurnaceBlockEntity.SLOT_OUTPUT_2, 134, 90));

        this.playerInv(inventory, 8, 140);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index < 5) {
                if(!this.moveItemStackTo(stack, 5, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if(MachineBlastFurnaceBlockEntity.getBurnTime(stack) > 0) {
                if(!this.moveItemStackTo(stack, MachineBlastFurnaceBlockEntity.SLOT_FUEL, MachineBlastFurnaceBlockEntity.SLOT_FUEL + 1, false)) return ItemStack.EMPTY;
            } else if(this.be.canPlaceItem(MachineBlastFurnaceBlockEntity.SLOT_INPUT_1, stack)) {
                if(!this.moveItemStackTo(stack, MachineBlastFurnaceBlockEntity.SLOT_INPUT_1, MachineBlastFurnaceBlockEntity.SLOT_INPUT_2 + 1, false)) return ItemStack.EMPTY;
            } else {
                return ItemStack.EMPTY;
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
