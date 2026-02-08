package com.hbm.inventory.menus;

import com.hbm.blockentity.bomb.NukeN2BlockEntity;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class NukeN2Menu extends AbstractContainerMenu {

    public final NukeN2BlockEntity be;

    public NukeN2Menu(int id, Inventory playerInv, FriendlyByteBuf extraData) {
        this(id, playerInv, (NukeN2BlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public NukeN2Menu(int id, Inventory inventory, NukeN2BlockEntity blockEntity) {
        super(ModMenuTypes.NUKE_N2.get(), id);

        this.be = blockEntity;

        this.addSlot(new SlotNonRetarded(be, 0, 98, 36));
        this.addSlot(new SlotNonRetarded(be, 1, 116, 36));
        this.addSlot(new SlotNonRetarded(be, 2, 134, 36));
        this.addSlot(new SlotNonRetarded(be, 3, 98, 54));
        this.addSlot(new SlotNonRetarded(be, 4, 116, 54));
        this.addSlot(new SlotNonRetarded(be, 5, 134, 54));
        this.addSlot(new SlotNonRetarded(be, 6, 98, 72));
        this.addSlot(new SlotNonRetarded(be, 7, 116, 72));
        this.addSlot(new SlotNonRetarded(be, 8, 134, 72));
        this.addSlot(new SlotNonRetarded(be, 9, 98, 90));
        this.addSlot(new SlotNonRetarded(be, 10, 116, 90));
        this.addSlot(new SlotNonRetarded(be, 11, 134, 90));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18 + 56));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 142 + 56));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return be.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}