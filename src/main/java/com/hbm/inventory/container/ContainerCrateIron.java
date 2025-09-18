package com.hbm.inventory.container;

import com.hbm.blockentity.machine.storage.CrateIronBlockEntity;
import com.hbm.inventory.ModMenus;
import com.hbm.inventory.SlotNonRetarded;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

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

    public ContainerCrateIron(int windowId, Inventory playerInv, FriendlyByteBuf data) {
        this(windowId, playerInv, getClientSideContainer(playerInv, data.readBlockPos()));
    }

    private static Container getClientSideContainer(Inventory playerInv, BlockPos pos) {
        BlockEntity be = playerInv.player.level().getBlockEntity(pos);
        if (be instanceof CrateIronBlockEntity crate) {
            return crate;
        }
        return new SimpleContainer(CrateIronBlockEntity.SIZE);
    }
}