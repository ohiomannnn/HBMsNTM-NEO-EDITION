package com.hbm.blockentity.bomb;

import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blockentity.NukeBaseBlockEntity;
import com.hbm.inventory.menus.NukeFleijaMenu;
import com.hbm.items.NtmItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class NukeFleijaBlockEntity extends NukeBaseBlockEntity {

    public NukeFleijaBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.NUKE_FLEIJA.get(), pos, state, 11);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.nukeFleija"); }

    @Override public int getMaxStackSize() { return 1; }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        Item item = stack.getItem();
        return item == NtmItems.FLEIJA_IGNITER.get() ||
                item == NtmItems.FLEIJA_PROPELLANT.get() ||
                item == NtmItems.FLEIJA_CORE.get();
    }

    @Override
    public boolean isReady() {
        return slots.get(0).getItem() == NtmItems.FLEIJA_IGNITER.get() &&
                slots.get(1).getItem() == NtmItems.FLEIJA_IGNITER.get() &&
                slots.get(2).getItem() == NtmItems.FLEIJA_PROPELLANT.get() &&
                slots.get(3).getItem() == NtmItems.FLEIJA_PROPELLANT.get() &&
                slots.get(4).getItem() == NtmItems.FLEIJA_PROPELLANT.get() &&
                slots.get(5).getItem() == NtmItems.FLEIJA_CORE.get() &&
                slots.get(6).getItem() == NtmItems.FLEIJA_CORE.get() &&
                slots.get(7).getItem() == NtmItems.FLEIJA_CORE.get() &&
                slots.get(8).getItem() == NtmItems.FLEIJA_CORE.get() &&
                slots.get(9).getItem() == NtmItems.FLEIJA_CORE.get() &&
                slots.get(10).getItem() == NtmItems.FLEIJA_CORE.get();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new NukeFleijaMenu(id, inventory, this);
    }
}
