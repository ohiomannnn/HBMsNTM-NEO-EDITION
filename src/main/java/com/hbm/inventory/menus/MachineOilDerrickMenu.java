package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.oil.MachineOilDerrickBlockEntity;
import com.hbm.inventory.FluidContainerRegistry;
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

public class MachineOilDerrickMenu extends MenuBase<MachineOilDerrickBlockEntity> {

    public MachineOilDerrickMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineOilDerrickBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineOilDerrickMenu(int id, Inventory inventory, MachineOilDerrickBlockEntity be) {
        super(NtmMenuTypes.MACHINE_OIL_DERRICK.get(), id, be);

        this.addSlot(new SlotNonRetarded(be, 0, 8, 53));
        this.addSlot(new SlotNonRetarded(be, 1, 80, 17));
        this.addSlot(new SlotTakeOnly(be, 2, 80, 53));
        this.addSlot(new SlotNonRetarded(be, 3, 125, 17));
        this.addSlot(new SlotTakeOnly(be, 4, 125, 53));
        this.addSlot(new SlotNonRetarded(be, 5, 152, 17));
        this.addSlot(new SlotNonRetarded(be, 6, 152, 35));
        this.addSlot(new SlotNonRetarded(be, 7, 152, 53));

        this.playerInv(inventory, 8, 84);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index <= 7) {
                if(!this.moveItemStackTo(stack, 8, this.slots.size(), true)) return ItemStack.EMPTY;
            } else {
                if(stack.getItem() instanceof IBatteryItem) {
                    if(!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[0].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 1, 2, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[1].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 3, 4, false)) return ItemStack.EMPTY;
                } else if(stack.getItem() instanceof MachineUpgradeItem) {
                    if(!this.moveItemStackTo(stack, 5, 8, false)) return ItemStack.EMPTY;
                } else {
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
