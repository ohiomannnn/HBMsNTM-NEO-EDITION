package com.hbm.inventory.menus;

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

public class ContainerMenuBase extends AbstractContainerMenu {

    public Container be;

    protected ContainerMenuBase(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
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

            if(index <= be.getContainerSize() - 1) {
                if (!this.moveItemStackTo(stack, be.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(stack, 0, be.getContainerSize(), false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    /** Standard player inventory with default hotbar offset */
    public void playerInv(Inventory invPlayer, int playerInvX, int playerInvY) {
        playerInv(invPlayer, playerInvX, playerInvY, playerInvY + 58);
    }

    /** Used to quickly set up the player inventory */
    public void playerInv(Inventory invPlayer, int playerInvX, int playerInvY, int playerHotbarY) {
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                this.addSlot(new SlotNonRetarded(invPlayer, x + y * 9 + 9, playerInvX + x * 18, playerInvY + y * 18));
            }
        }

        for(int x = 0; x < 9; x++) {
            this.addSlot(new SlotNonRetarded(invPlayer, x, playerInvX + x * 18, playerHotbarY));
        }
    }

    // I'm gonna make a farken helper function for this shit, why was it done
    // the old way for 9 whole ass years?
    // - Mellow, 1884
    /**
     * Used to add several conventional inventory slots at a time
     *
     * @param inv the inventory to add the slots to
     * @param from the slot index to start from
     */
    public void addSlots(Container inv, int from, int x, int y, int rows, int cols) {
        addSlots(inv, from, x, y, rows, cols, 18);
    }

    public void addSlots(Container inv, int from, int x, int y, int rows, int cols, int slotSize) {
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                this.addSlot(new SlotNonRetarded(inv, col + row * cols + from, x + col * slotSize, y + row * slotSize));
            }
        }
    }

    public void addOutputSlots(Player player, Container inv, int from, int x, int y, int rows, int cols) {
        addOutputSlots(player, inv, from, x, y, rows, cols, 18);
    }

    public void addOutputSlots(Player player, Container inv, int from, int x, int y, int rows, int cols, int slotSize) {
        for(int row = 0; row < rows; row++) for(int col = 0; col < cols; col++) {
            this.addSlot(new SlotCraftingOutput(player, inv, col + row * cols + from, x + col * slotSize, y + row * slotSize));
        }
    }

    public void addTakeOnlySlots(Container inv, int from, int x, int y, int rows, int cols) {
        addTakeOnlySlots(inv, from, x, y, rows, cols, 18);
    }

    public void addTakeOnlySlots(Container inv, int from, int x, int y, int rows, int cols, int slotSize) {
        for(int row = 0; row < rows; row++) for(int col = 0; col < cols; col++) {
            this.addSlot(new SlotTakeOnly(inv, col + row * cols + from, x + col * slotSize, y + row * slotSize));
        }
    }
}
