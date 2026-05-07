package com.hbm.inventory.menus;

import com.hbm.blockentity.bomb.NukePrototypeBlockEntity;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class NukePrototypeMenu extends AbstractContainerMenu {

    public final NukePrototypeBlockEntity be;

    public NukePrototypeMenu(int id, Inventory playerInv, FriendlyByteBuf extraData) {
        this(id, playerInv, (NukePrototypeBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public NukePrototypeMenu(int id, Inventory inventory, NukePrototypeBlockEntity blockEntity) {
        super(ModMenuTypes.NUKE_PROTOTYPE.get(), id);

        this.be = blockEntity;

        this.addSlot(new SlotNonRetarded(be, 0, 8, 35));
        this.addSlot(new SlotNonRetarded(be, 1, 26, 35));
        this.addSlot(new SlotNonRetarded(be, 2, 44, 26));
        this.addSlot(new SlotNonRetarded(be, 3, 44, 44));
        this.addSlot(new SlotNonRetarded(be, 4, 62, 26));
        this.addSlot(new SlotNonRetarded(be, 5, 62, 44));
        this.addSlot(new SlotNonRetarded(be, 6, 80, 26));
        this.addSlot(new SlotNonRetarded(be, 7, 80, 44));
        this.addSlot(new SlotNonRetarded(be, 8, 98, 26));
        this.addSlot(new SlotNonRetarded(be, 9, 98, 44));
        this.addSlot(new SlotNonRetarded(be, 10, 116, 26));
        this.addSlot(new SlotNonRetarded(be, 11, 116, 44));
        this.addSlot(new SlotNonRetarded(be, 12, 134, 35));
        this.addSlot(new SlotNonRetarded(be, 13, 152, 35));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(inventory, 9 + x + y * 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 142));
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