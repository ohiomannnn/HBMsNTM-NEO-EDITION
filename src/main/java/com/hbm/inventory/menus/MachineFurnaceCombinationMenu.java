package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineFurnaceCombinationBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineFurnaceCombinationMenu extends MenuBase<MachineFurnaceCombinationBlockEntity> {

    public MachineFurnaceCombinationMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineFurnaceCombinationBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineFurnaceCombinationMenu(int id, Inventory inventory, MachineFurnaceCombinationBlockEntity be) {
        super(NtmMenuTypes.FURNACE_COMBINATION.get(), id, be);
        this.addSlot(new SlotNonRetarded(be, MachineFurnaceCombinationBlockEntity.SLOT_INPUT, 26, 36));
        this.addSlot(new SlotTakeOnly(be, MachineFurnaceCombinationBlockEntity.SLOT_OUTPUT, 89, 36));
        this.addSlot(new SlotNonRetarded(be, MachineFurnaceCombinationBlockEntity.SLOT_FLUID_IN, 136, 18));
        this.addSlot(new SlotTakeOnly(be, MachineFurnaceCombinationBlockEntity.SLOT_FLUID_OUT, 136, 54));
        this.playerInv(inventory, 8, 104);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if(!slot.hasItem()) return ret;

        ItemStack stack = slot.getItem();
        ret = stack.copy();

        if(index < 4) {
            if(!this.moveItemStackTo(stack, 4, this.slots.size(), true)) return ItemStack.EMPTY;
        } else if(this.be.canPlaceItem(MachineFurnaceCombinationBlockEntity.SLOT_INPUT, stack)) {
            if(!this.moveItemStackTo(stack, MachineFurnaceCombinationBlockEntity.SLOT_INPUT, MachineFurnaceCombinationBlockEntity.SLOT_INPUT + 1, false)) return ItemStack.EMPTY;
        } else {
            if(!this.moveItemStackTo(stack, MachineFurnaceCombinationBlockEntity.SLOT_FLUID_IN, MachineFurnaceCombinationBlockEntity.SLOT_FLUID_IN + 1, false)) {
                return ItemStack.EMPTY;
            }
        }

        if(stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        return ret;
    }
}
