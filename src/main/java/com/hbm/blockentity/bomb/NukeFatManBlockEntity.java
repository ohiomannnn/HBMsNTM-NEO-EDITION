package com.hbm.blockentity.bomb;

import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blockentity.NukeBaseBlockEntity;
import com.hbm.inventory.menus.NukeFatManMenu;
import com.hbm.items.NtmItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class NukeFatManBlockEntity extends NukeBaseBlockEntity {

    public NukeFatManBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.NUKE_FAT_MAN.get(), pos, state, 6);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.nukeFatMan"); }

    @Override public int getMaxStackSize() { return 1; }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        Item item = stack.getItem();
        return item == NtmItems.EARLY_EXPLOSIVE_LENSES.get() ||
                item == NtmItems.FAT_MAN_IGNITER.get() ||
                item == NtmItems.FAT_MAN_CORE.get();
    }

    @Override
    public boolean isReady() {
        return slots.get(0).getItem() == NtmItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(1).getItem() == NtmItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(2).getItem() == NtmItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(3).getItem() == NtmItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(4).getItem() == NtmItems.FAT_MAN_IGNITER.get() &&
                slots.get(5).getItem() == NtmItems.FAT_MAN_CORE.get();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new NukeFatManMenu(id, inventory, this);
    }
}
