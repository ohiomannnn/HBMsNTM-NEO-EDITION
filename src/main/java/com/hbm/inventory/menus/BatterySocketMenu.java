package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.storage.BatterySocketBlockEntity;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BatterySocketMenu extends AbstractContainerMenu {

    public BatterySocketBlockEntity be;

    public BatterySocketMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (BatterySocketBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public BatterySocketMenu(int id, Inventory inventory, BatterySocketBlockEntity blockEntity) {
        super(ModMenuTypes.BATTERY_SOCKET.get(), id);

        this.be = blockEntity;

        this.addSlot(new SlotNonRetarded(be, 0, 35, 35));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 99 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 157));
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
