package com.hbm.inventory.menus;

import com.hbm.blockentity.bomb.NukeIvyMikeBlockEntity;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class NukeIvyMikeMenu extends AbstractContainerMenu {

    public final NukeIvyMikeBlockEntity be;

    public NukeIvyMikeMenu(int id, Inventory playerInv, FriendlyByteBuf extraData) {
        this(id, playerInv, (NukeIvyMikeBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public NukeIvyMikeMenu(int id, Inventory inventory, NukeIvyMikeBlockEntity blockEntity) {
        super(ModMenuTypes.NUKE_IVY_MIKE.get(), id);

        this.be = blockEntity;

        this.addSlot(new SlotNonRetarded(be, 0, 26, 83));
        this.addSlot(new SlotNonRetarded(be, 1, 26, 101));
        this.addSlot(new SlotNonRetarded(be, 2, 44, 83));
        this.addSlot(new SlotNonRetarded(be, 3, 44, 101));
        this.addSlot(new SlotNonRetarded(be, 4, 39, 35));
        this.addSlot(new SlotNonRetarded(be, 5, 98, 91));
        this.addSlot(new SlotNonRetarded(be, 6, 116, 91));
        this.addSlot(new SlotNonRetarded(be, 7, 134, 91));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 135 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 193));
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