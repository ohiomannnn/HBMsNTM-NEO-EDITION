package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineSatLinkerBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class MachineSatLinkerMenu extends AbstractContainerMenu {

    private final MachineSatLinkerBlockEntity be;

    public MachineSatLinkerMenu(int id, Inventory playerInv, FriendlyByteBuf extraData) {
        this(id, playerInv, (MachineSatLinkerBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public MachineSatLinkerMenu(int id, Inventory playerInv, MachineSatLinkerBlockEntity be) {
        super(ModMenuTypes.SAT_LINKER.get(), id);

        this.be = be;

        ItemStackHandler handler = be.getItems();

        this.addSlot(new SlotItemHandler(handler, 0, 44, 35));
        this.addSlot(new SlotItemHandler(handler, 1, 80, 35));
        this.addSlot(new SlotItemHandler(handler, 2, 116, 35));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInv, x, 8 + x * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), be.getBlockPos()), player, ModBlocks.MACHINE_SATLINKER.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {

        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {

            ItemStack stack = slot.getItem();
            newStack = stack.copy();

            if (index < 3) {
                if (!moveItemStackTo(stack, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!moveItemStackTo(stack, 0, 1, false)) {
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