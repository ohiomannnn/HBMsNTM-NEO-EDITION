package com.hbm.inventory.menus;

import com.hbm.blockentity.bomb.NukeFleijaBlockEntity;
import com.hbm.blockentity.bomb.NukeN2BlockEntity;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class NukeFleijaMenu extends AbstractContainerMenu {

    public final NukeFleijaBlockEntity be;

    public NukeFleijaMenu(int id, Inventory playerInv, FriendlyByteBuf extraData) {
        this(id, playerInv, (NukeFleijaBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public NukeFleijaMenu(int id, Inventory inventory, NukeFleijaBlockEntity blockEntity) {
        super(ModMenuTypes.NUKE_FLEIJA.get(), id);

        this.be = blockEntity;

        this.addSlot(new SlotNonRetarded(be, 0, 8, 36));
        this.addSlot(new SlotNonRetarded(be, 1, 152, 36));
        this.addSlot(new SlotNonRetarded(be, 2, 44, 18));
        this.addSlot(new SlotNonRetarded(be, 3, 44, 36));
        this.addSlot(new SlotNonRetarded(be, 4, 44, 54));
        this.addSlot(new SlotNonRetarded(be, 5, 80, 18));
        this.addSlot(new SlotNonRetarded(be, 6, 98, 18));
        this.addSlot(new SlotNonRetarded(be, 7, 80, 36));
        this.addSlot(new SlotNonRetarded(be, 8, 98, 36));
        this.addSlot(new SlotNonRetarded(be, 9, 80, 54));
        this.addSlot(new SlotNonRetarded(be, 10, 98, 54));

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