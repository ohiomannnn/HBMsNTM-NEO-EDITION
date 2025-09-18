package com.hbm.inventory.container;

import com.hbm.HBMsNTM;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.items.block.ItemBlockStorageCrate;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class ContainerCrateBase extends ContainerBase {

    public ContainerCrateBase(MenuType<?> menuType, int windowId, Inventory playerInv, Container crateInv) {
        super(menuType, windowId, playerInv, crateInv);
        if (!playerInv.player.level().isClientSide) {
            crateInv.startOpen(playerInv.player);
        }
    }

    @Override
    public void initializeContents(int stateId, List<ItemStack> stacks, ItemStack carried) {
        if (stacks.size() != this.slots.size()) {
            HBMsNTM.LOGGER.error("Mismatch: server sent {} slots, client has {}", stacks.size(), this.slots.size());
        }
        super.initializeContents(stateId, stacks, carried);
    }

    @Override
    public void addPlayerInventory(Inventory playerInv, int playerInvX, int playerInvY, int playerHotbarY) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new SlotNonRetarded(playerInv, j + i * 9 + 9, playerInvX + j * 18, playerInvY + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(
                    (playerInv.selected == i && this.tile instanceof ItemBlockStorageCrate.InventoryCrate)
                            ? new SlotPlayerCrateLocked(playerInv, i, playerInvX + i * 18, playerHotbarY)
                            : new SlotNonRetarded(playerInv, i, playerInvX + i * 18, playerHotbarY)
            );
        }
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        ItemStack held = player.getInventory().getItem(player.getInventory().selected);
        if (!held.isEmpty() &&
                held.getItem() instanceof ItemBlockStorageCrate &&
                !(this.tile instanceof BlockEntity)) {

            if (clickType == ClickType.SWAP && button == player.getInventory().selected) {
                return;
            }
            if (slotId == this.tile.getContainerSize() + 27 + player.getInventory().selected) {
                return;
            }
        }
        super.clicked(slotId, button, clickType, player);
    }


    @Override
    public void removed(Player player) {
        super.removed(player);
        tile.stopOpen(player);
    }

    public static class SlotPlayerCrateLocked extends Slot {
        public SlotPlayerCrateLocked(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}