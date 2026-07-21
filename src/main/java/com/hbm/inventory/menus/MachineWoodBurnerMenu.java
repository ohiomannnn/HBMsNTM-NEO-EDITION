package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineWoodBurnerBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineWoodBurnerMenu extends MenuBase<MachineWoodBurnerBlockEntity> {

    public MachineWoodBurnerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineWoodBurnerBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineWoodBurnerMenu(int id, Inventory inventory, MachineWoodBurnerBlockEntity be) {
        super(NtmMenuTypes.MACHINE_WOOD_BURNER.get(), id, be);

        this.addSlot(new SlotNonRetarded(be, MachineWoodBurnerBlockEntity.SLOT_FUEL, 26, 18));
        this.addSlot(new SlotTakeOnly(be, MachineWoodBurnerBlockEntity.SLOT_ASH, 26, 54));
        this.addSlot(new SlotNonRetarded(be, MachineWoodBurnerBlockEntity.SLOT_IDENTIFIER, 98, 54));
        this.addSlot(new SlotNonRetarded(be, MachineWoodBurnerBlockEntity.SLOT_FLUID_IN, 98, 18));
        this.addSlot(new SlotTakeOnly(be, MachineWoodBurnerBlockEntity.SLOT_FLUID_OUT, 98, 36));
        this.addSlot(new SlotNonRetarded(be, MachineWoodBurnerBlockEntity.SLOT_BATTERY, 143, 54));

        this.playerInv(inventory, 8, 104);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index < 6) {
                if(!this.moveItemStackTo(stack, 6, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if(this.be.canPlaceItem(MachineWoodBurnerBlockEntity.SLOT_FUEL, stack)) {
                if(!this.moveItemStackTo(stack, MachineWoodBurnerBlockEntity.SLOT_FUEL, MachineWoodBurnerBlockEntity.SLOT_FUEL + 1, false)) return ItemStack.EMPTY;
            } else if(this.be.canPlaceItem(MachineWoodBurnerBlockEntity.SLOT_IDENTIFIER, stack)) {
                if(!this.moveItemStackTo(stack, MachineWoodBurnerBlockEntity.SLOT_IDENTIFIER, MachineWoodBurnerBlockEntity.SLOT_IDENTIFIER + 1, false)) return ItemStack.EMPTY;
            } else if(this.be.canPlaceItem(MachineWoodBurnerBlockEntity.SLOT_FLUID_IN, stack)) {
                if(!this.moveItemStackTo(stack, MachineWoodBurnerBlockEntity.SLOT_FLUID_IN, MachineWoodBurnerBlockEntity.SLOT_FLUID_IN + 1, false)) return ItemStack.EMPTY;
            } else if(this.be.canPlaceItem(MachineWoodBurnerBlockEntity.SLOT_BATTERY, stack)) {
                if(!this.moveItemStackTo(stack, MachineWoodBurnerBlockEntity.SLOT_BATTERY, MachineWoodBurnerBlockEntity.SLOT_BATTERY + 1, false)) return ItemStack.EMPTY;
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
