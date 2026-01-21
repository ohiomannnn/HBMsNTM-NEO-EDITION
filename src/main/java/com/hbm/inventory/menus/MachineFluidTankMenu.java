package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.storage.MachineFluidTankBlockEntity;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineFluidTankMenu extends AbstractContainerMenu {

    public final MachineFluidTankBlockEntity be;

    public MachineFluidTankMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineFluidTankBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineFluidTankMenu(int id, Inventory inventory, MachineFluidTankBlockEntity blockEntity) {
        super(ModMenuTypes.FLUID_TANK.get(), id);

        this.be = blockEntity;

        this.addSlot(new Slot(be, 0, 8, 17));
        this.addSlot(new Slot(be, 1, 8, 53));
        this.addSlot(new Slot(be, 2, 53 - 18, 17));
        this.addSlot(new Slot(be, 3, 53 - 18, 53));
        this.addSlot(new Slot(be, 4, 125, 17));
        this.addSlot(new Slot(be, 5, 125, 53));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return be.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {

        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {

            ItemStack stack = slot.getItem();
            newStack = stack.copy();

            if (index < 1) {
                if (!this.moveItemStackTo(stack, be.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(stack, 0, be.getContainerSize(), false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }
}
