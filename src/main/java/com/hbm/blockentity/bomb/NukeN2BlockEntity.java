package com.hbm.blockentity.bomb;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blockentity.NukeBaseBlockEntity;
import com.hbm.inventory.menus.NukeN2Menu;
import com.hbm.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class NukeN2BlockEntity extends NukeBaseBlockEntity {

    public NukeN2BlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NUKE_N2.get(), pos, state, 12);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.nukeN2"); }
    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        Item item = stack.getItem();
        return item == ModItems.N2_CHARGE.get();
    }

    @Override
    public boolean isReady() {
        return slots.get(0).getItem() == ModItems.N2_CHARGE.get() &&
                slots.get(1).getItem() == ModItems.N2_CHARGE.get() &&
                slots.get(2).getItem() == ModItems.N2_CHARGE.get() &&
                slots.get(3).getItem() == ModItems.N2_CHARGE.get() &&
                slots.get(4).getItem() == ModItems.N2_CHARGE.get() &&
                slots.get(5).getItem() == ModItems.N2_CHARGE.get() &&
                slots.get(6).getItem() == ModItems.N2_CHARGE.get() &&
                slots.get(7).getItem() == ModItems.N2_CHARGE.get() &&
                slots.get(8).getItem() == ModItems.N2_CHARGE.get() &&
                slots.get(9).getItem() == ModItems.N2_CHARGE.get() &&
                slots.get(10).getItem() == ModItems.N2_CHARGE.get() &&
                slots.get(11).getItem() == ModItems.N2_CHARGE.get();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new NukeN2Menu(id, inventory, this);
    }
}
