package com.hbm.blockentity.bomb;

import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blockentity.NukeBaseBlockEntity;
import com.hbm.inventory.menus.NukeTsarBombaMenu;
import com.hbm.items.NtmItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class NukeTsarBombaBlockEntity extends NukeBaseBlockEntity {

    public NukeTsarBombaBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.NUKE_TSAR_BOMBA.get(), pos, state, 6);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.nukeTsarBomba"); }

    @Override public int getMaxStackSize() { return 1; }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        Item item = stack.getItem();
        return item == NtmItems.EXPLOSIVE_LENSES.get() ||
                item == NtmItems.FAT_MAN_CORE.get() ||
                item == NtmItems.TSAR_BOMBA_CORE.get();
    }
    @Override
    public boolean isReady() {
        return slots.get(0).getItem() == NtmItems.EXPLOSIVE_LENSES.get() &&
                slots.get(1).getItem() == NtmItems.EXPLOSIVE_LENSES.get() &&
                slots.get(2).getItem() == NtmItems.EXPLOSIVE_LENSES.get() &&
                slots.get(3).getItem() == NtmItems.EXPLOSIVE_LENSES.get() &&
                slots.get(4).getItem() == NtmItems.FAT_MAN_CORE.get() &&
                slots.get(5).getItem() == NtmItems.TSAR_BOMBA_CORE.get();
    }

    @Override
    public boolean isFilled() {
        return slots.get(0).getItem() == NtmItems.EXPLOSIVE_LENSES.get() &&
                slots.get(1).getItem() == NtmItems.EXPLOSIVE_LENSES.get() &&
                slots.get(2).getItem() == NtmItems.EXPLOSIVE_LENSES.get() &&
                slots.get(3).getItem() == NtmItems.EXPLOSIVE_LENSES.get() &&
                slots.get(4).getItem() == NtmItems.FAT_MAN_CORE.get();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new NukeTsarBombaMenu(id, inventory, this);
    }
}
