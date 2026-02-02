package com.hbm.inventory.menus;

import com.hbm.blockentity.bomb.NukeFatManBlockEntity;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class NukeFatManMenu extends AbstractContainerMenu {

    public final NukeFatManBlockEntity be;

    public NukeFatManMenu(int id, Inventory playerInv, FriendlyByteBuf extraData) {
        this(id, playerInv, (NukeFatManBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public NukeFatManMenu(int id, Inventory inventory, NukeFatManBlockEntity blockEntity) {
        super(ModMenuTypes.NUKE_FAT_MAN.get(), id);

        this.be = blockEntity;

        this.addSlot(new SlotNonRetarded(be, 0, 8, 17));
        this.addSlot(new SlotNonRetarded(be, 1, 44, 17));
        this.addSlot(new SlotNonRetarded(be, 2, 8, 53));
        this.addSlot(new SlotNonRetarded(be, 3, 44, 53));
        this.addSlot(new SlotNonRetarded(be, 4, 26, 35));
        this.addSlot(new SlotNonRetarded(be, 5, 98, 35));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
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