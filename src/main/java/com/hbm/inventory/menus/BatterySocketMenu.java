package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.storage.BatterySocketBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class BatterySocketMenu extends AbstractContainerMenu {

    public BatterySocketBlockEntity socket;

    public BatterySocketMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, getBlockEntity(inventory.player.level(), extraData.readBlockPos()));
    }

    private static BatterySocketBlockEntity getBlockEntity(Level level, BlockPos pos) {
        Block b = level.getBlockState(pos).getBlock();
        if (b instanceof DummyableBlock dummy) {
            // pos might be null, i dont care
            BlockPos dummyCore = dummy.findCore(level, pos);
            // if minecraft going to crash its not my fault
            return (BatterySocketBlockEntity) level.getBlockEntity(dummyCore);
        }
        return null;
    }

    public BatterySocketMenu(int id, Inventory inventory, BatterySocketBlockEntity be) {
        super(ModMenuTypes.BATTERY_SOCKET.get(), id);

        this.socket = be;

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
        return socket.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {

        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {

            ItemStack stack = slot.getItem();
            newStack = stack.copy();

            if (index < 1) {
                if (!this.moveItemStackTo(stack, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(stack, 0, 1, false)) {
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
