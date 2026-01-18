package com.hbm.inventory.menus;

import com.hbm.blockentity.bomb.NukeFatManBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.items.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class NukeFatManMenu extends AbstractContainerMenu {

    private final NukeFatManBlockEntity be;

    public NukeFatManMenu(int id, Inventory playerInv, FriendlyByteBuf extraData) {
        this(id, playerInv, (NukeFatManBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public NukeFatManMenu(int id, Inventory inventory, NukeFatManBlockEntity blockEntity) {
        super(ModMenuTypes.NUKE_FATMAN.get(), id);

        this.be = blockEntity;

        this.addSlot(new SlotNonRetarded(be, 0, 26, 35));
        this.addSlot(new SlotNonRetarded(be, 1, 8, 17));
        this.addSlot(new SlotNonRetarded(be, 2, 44, 17));
        this.addSlot(new SlotNonRetarded(be, 3, 8, 53));
        this.addSlot(new SlotNonRetarded(be, 4, 44, 53));
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

    public boolean exp1() {
        return this.be.exp1();
    }

    public boolean exp2() {
        return this.be.exp2();
    }

    public boolean exp3() {
        return this.be.exp3();
    }

    public boolean exp4() {
        return this.be.exp4();
    }

    public boolean isReady() {
        return this.be.isReady();
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