package com.hbm.blockentity.bomb;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blockentity.NukeBaseBlockEntity;
import com.hbm.inventory.menus.NukeSoliniumMenu;
import com.hbm.items.NtmItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class NukeSoliniumBlockEntity extends NukeBaseBlockEntity {

    public NukeSoliniumBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.NUKE_SOLINUIM.get(), pos, state, 9);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.nuke_solinium"); }

    @Override public int getMaxStackSize() { return 1; }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        Item item = stack.getItem();
        return item == NtmItems.SOLINIUM_IGNITER.get() ||
                item == NtmItems.SOLINIUM_PROPELLANT.get() ||
                item == NtmItems.SOLINIUM_CORE.get();
    }

    @Override
    public boolean isReady() {
        return slots.get(0).is(NtmItems.SOLINIUM_IGNITER.get()) &&
                slots.get(1).is(NtmItems.SOLINIUM_PROPELLANT.get()) &&
                slots.get(2).is(NtmItems.SOLINIUM_PROPELLANT.get()) &&
                slots.get(3).is(NtmItems.SOLINIUM_IGNITER.get()) &&
                slots.get(4).is(NtmItems.SOLINIUM_CORE.get()) &&
                slots.get(5).is(NtmItems.SOLINIUM_IGNITER.get()) &&
                slots.get(6).is(NtmItems.SOLINIUM_PROPELLANT.get()) &&
                slots.get(7).is(NtmItems.SOLINIUM_PROPELLANT.get()) &&
                slots.get(8).is(NtmItems.SOLINIUM_IGNITER.get());
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new NukeSoliniumMenu(id, inventory, this);
    }
}
