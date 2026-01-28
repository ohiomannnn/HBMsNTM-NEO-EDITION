package com.hbm.inventory.menus;

import com.hbm.blockentity.bomb.NukeTsarBombaBlockEntity;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class NukeTsarBombaMenu extends AbstractContainerMenu {

    public final NukeTsarBombaBlockEntity be;

    public NukeTsarBombaMenu(int id, Inventory playerInv, FriendlyByteBuf extraData) {
        this(id, playerInv, (NukeTsarBombaBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public NukeTsarBombaMenu(int id, Inventory inventory, NukeTsarBombaBlockEntity blockEntity) {
        super(ModMenuTypes.NUKE_TSAR_BOMBA.get(), id);

        this.be = blockEntity;

        this.addSlot(new SlotNonRetarded(be, 0, 48, 101));
        this.addSlot(new SlotNonRetarded(be, 1, 66, 101));
        this.addSlot(new SlotNonRetarded(be, 2, 84, 101));
        this.addSlot(new SlotNonRetarded(be, 3, 102, 101));
        this.addSlot(new SlotNonRetarded(be, 4, 55, 51));
        this.addSlot(new SlotNonRetarded(be, 5, 138, 101));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(inventory, 9 + x + y * 9, 48 + x * 18, 151 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory, x, 48 + x * 18, 209));
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