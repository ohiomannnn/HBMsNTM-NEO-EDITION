package com.hbm.inventory.container;

import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerBase extends AbstractContainerMenu {

    protected final Container tile;

    public ContainerBase(MenuType<?> menuType, int windowId, Inventory inv, Container tileInv) {
        super(menuType, windowId);
        this.tile = tileInv;
    }

    @Override
    public boolean stillValid(Player player) {
        return tile.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack original = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            original = slotStack.copy();

            if (index < tile.getContainerSize()) {
                if (!this.moveItemStackTo(slotStack, tile.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, tile.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            slot.onTake(player, slotStack);
        }

        return original;
    }

    public void addPlayerInventory(Inventory playerInv, int playerInvX, int playerInvY) {
        addPlayerInventory(playerInv, playerInvX, playerInvY, playerInvY + 58);
    }

    public void addPlayerInventory(Inventory playerInv, int playerInvX, int playerInvY, int playerHotbarY) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new SlotNonRetarded(playerInv, j + i * 9 + 9, playerInvX + j * 18, playerInvY + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotNonRetarded(playerInv, i, playerInvX + i * 18, playerHotbarY));
        }
    }

    public void addSlots(Container inv, int from, int x, int y, int rows, int cols) {
        addSlots(inv, from, x, y, rows, cols, 18);
    }

    public void addSlots(Container inv, int from, int x, int y, int rows, int cols, int slotSize) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                this.addSlot(new SlotNonRetarded(inv, col + row * cols + from, x + col * slotSize, y + row * slotSize));
            }
        }
    }

    public void addOutputSlots(Player player, Container inv, int from, int x, int y, int rows, int cols) {
        addOutputSlots(player, inv, from, x, y, rows, cols, 18);
    }

    public void addOutputSlots(Player player, Container inv, int from, int x, int y, int rows, int cols, int slotSize) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                this.addSlot(new SlotCraftingOutput(player, inv, col + row * cols + from, x + col * slotSize, y + row * slotSize));
            }
        }
    }

    public void addTakeOnlySlots(Container inv, int from, int x, int y, int rows, int cols) {
        addTakeOnlySlots(inv, from, x, y, rows, cols, 18);
    }

    public void addTakeOnlySlots(Container inv, int from, int x, int y, int rows, int cols, int slotSize) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                this.addSlot(new SlotTakeOnly(inv, col + row * cols + from, x + col * slotSize, y + row * slotSize));
            }
        }
    }
}
