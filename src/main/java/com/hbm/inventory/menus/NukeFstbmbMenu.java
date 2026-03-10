package com.hbm.inventory.menus;

import com.hbm.blockentity.bomb.NukeBalefireBlockEntity;
import com.hbm.blockentity.bomb.NukeFleijaBlockEntity;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class NukeFstbmbMenu extends AbstractContainerMenu {

    public final NukeBalefireBlockEntity be;

    public NukeFstbmbMenu(int id, Inventory playerInv, FriendlyByteBuf extraData) {
        this(id, playerInv, (NukeBalefireBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public NukeFstbmbMenu(int id, Inventory inventory, NukeBalefireBlockEntity blockEntity) {
        super(ModMenuTypes.NUKE_FSTBMB.get(), id);

        this.be = blockEntity;

        this.addSlot(new SlotNonRetarded(be, 0, 17, 36));
        this.addSlot(new SlotNonRetarded(be, 1, 53, 36));

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