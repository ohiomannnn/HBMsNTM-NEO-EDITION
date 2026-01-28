package com.hbm.blockentity.bomb;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blockentity.NukeBaseBlockEntity;
import com.hbm.inventory.menus.NukeGadgetMenu;
import com.hbm.inventory.menus.NukeTsarBombaMenu;
import com.hbm.items.ModItems;
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
        super(ModBlockEntities.NUKE_TSAR_BOMBA.get(), pos, state, 6);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.nukeTsarBomba"); }

    @Override public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        Item item = stack.getItem();
        return item == ModItems.EARLY_EXPLOSIVE_LENSES.get() ||
                item == ModItems.FAT_MAN_CORE.get() ||
                item == ModItems.TSAR_BOMBA_CORE.get();
    }
    @Override
    public boolean isReady() {
        return slots.get(0).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(1).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(2).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(3).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(4).getItem() == ModItems.FAT_MAN_CORE.get() &&
                slots.get(5).getItem() == ModItems.TSAR_BOMBA_CORE.get();
    }

    @Override
    public boolean isFilled() {
        return slots.get(0).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(1).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(2).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(3).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(4).getItem() == ModItems.FAT_MAN_CORE.get();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        if (player.isSpectator()) return null;
        return new NukeTsarBombaMenu(id, inventory, this);
    }
}
