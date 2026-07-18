package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.oil.MachineRefineryBlockEntity;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineRefineryMenu extends MenuBase<MachineRefineryBlockEntity> {

    public MachineRefineryMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineRefineryBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineRefineryMenu(int id, Inventory inventory, MachineRefineryBlockEntity be) {
        super(NtmMenuTypes.MACHINE_REFINERY.get(), id, be);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 186, 72));
        // Hot oil input / output
        this.addSlot(new SlotNonRetarded(be, 1, 8, 99));
        this.addSlot(new SlotTakeOnly(be, 2, 8, 119));
        // Heavy oil
        this.addSlot(new SlotNonRetarded(be, 3, 86, 99));
        this.addSlot(new SlotTakeOnly(be, 4, 86, 119));
        // Naphtha
        this.addSlot(new SlotNonRetarded(be, 5, 106, 99));
        this.addSlot(new SlotTakeOnly(be, 6, 106, 119));
        // Light oil
        this.addSlot(new SlotNonRetarded(be, 7, 126, 99));
        this.addSlot(new SlotTakeOnly(be, 8, 126, 119));
        // Petroleum gas
        this.addSlot(new SlotNonRetarded(be, 9, 146, 99));
        this.addSlot(new SlotTakeOnly(be, 10, 146, 119));
        // Sulfur output
        this.addSlot(new SlotTakeOnly(be, 11, 58, 119));
        // Fluid ID
        this.addSlot(new SlotNonRetarded(be, 12, 186, 106));

        this.playerInv(inventory, 8, 150, 208);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index <= 12) {
                if(!this.moveItemStackTo(stack, 13, this.slots.size(), true)) return ItemStack.EMPTY;
            } else {
                if(stack.getItem() instanceof IBatteryItem) {
                    if(!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
                } else if(stack.getItem() instanceof IItemFluidIdentifier) {
                    if(!this.moveItemStackTo(stack, 12, 13, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[0].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 1, 2, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[1].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 3, 4, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[2].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 5, 6, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[3].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 7, 8, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[4].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 9, 10, false)) return ItemStack.EMPTY;
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
