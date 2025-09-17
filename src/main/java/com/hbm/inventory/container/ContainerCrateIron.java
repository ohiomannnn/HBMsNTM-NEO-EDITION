package com.hbm.inventory.container;

import com.hbm.inventory.ModMenus;
import com.hbm.inventory.SlotNonRetarded;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

public class ContainerCrateIron extends ContainerCrateBase {

    public ContainerCrateIron(int windowId, Inventory playerInv, Container crateInv) {
        super(ModMenus.IRON_CRATE.get(), windowId, playerInv, crateInv);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new SlotNonRetarded(crateInv, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        this.addPlayerInventory(playerInv, 8, 104, 162);
    }

//    public ContainerCrateIron(int windowId, Inventory playerInv, FriendlyByteBuf data) {
//        this(windowId, playerInv, getCrateInventory(playerInv, data));
//    }
//
//    private static Container getCrateInventory(Inventory playerInv, FriendlyByteBuf data) {
//        final BlockEntity entity = playerInv.player.level().getBlockEntity(data.readBlockPos());
//        if (entity instanceof Container) {
//            return (Container) entity;
//        }
//        throw new IllegalStateException("Invalid tile entity at position!");
//    }
}