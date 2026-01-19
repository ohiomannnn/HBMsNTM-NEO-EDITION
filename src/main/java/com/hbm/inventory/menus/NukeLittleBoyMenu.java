package com.hbm.inventory.menus;

import com.hbm.blockentity.bomb.NukeBoyBlockEntity;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class NukeLittleBoyMenu extends AbstractContainerMenu {

    public final NukeBoyBlockEntity be;

    public NukeLittleBoyMenu(int id, Inventory playerInv, FriendlyByteBuf extraData) {
        this(id, playerInv, (NukeBoyBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public NukeLittleBoyMenu(int id, Inventory inventory, NukeBoyBlockEntity blockEntity) {
        super(ModMenuTypes.NUKE_LITTLE_BOY.get(), id);

        this.be = blockEntity;

        this.addSlot(new SlotNonRetarded(be, 0, 26, 36));
        this.addSlot(new SlotNonRetarded(be, 1, 44, 36));
        this.addSlot(new SlotNonRetarded(be, 2, 62, 36));
        this.addSlot(new SlotNonRetarded(be, 3, 80, 36));
        this.addSlot(new SlotNonRetarded(be, 4, 98, 36));

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