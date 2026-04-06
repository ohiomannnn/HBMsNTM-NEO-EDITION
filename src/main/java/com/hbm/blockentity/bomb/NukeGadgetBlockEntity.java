package com.hbm.blockentity.bomb;

import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blockentity.NukeBaseBlockEntity;
import com.hbm.inventory.menus.NukeGadgetMenu;
import com.hbm.items.NtmItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class NukeGadgetBlockEntity extends NukeBaseBlockEntity {

    public NukeGadgetBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.NUKE_GADGET.get(), pos, state, 6);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.nukeGadget"); }

    @Override public int getMaxStackSize() { return 1; }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        Item item = stack.getItem();
        return item == NtmItems.EARLY_EXPLOSIVE_LENSES.get() ||
                item == NtmItems.GADGET_WIREING.get() ||
                item == NtmItems.GADGET_CORE.get();
    }

    @Override
    public boolean isReady() {
        return slots.get(0).getItem() == NtmItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(1).getItem() == NtmItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(2).getItem() == NtmItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(3).getItem() == NtmItems.EARLY_EXPLOSIVE_LENSES.get() &&
                slots.get(4).getItem() == NtmItems.GADGET_WIREING.get() &&
                slots.get(5).getItem() == NtmItems.GADGET_CORE.get();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new NukeGadgetMenu(id, inventory, this);
    }
}
