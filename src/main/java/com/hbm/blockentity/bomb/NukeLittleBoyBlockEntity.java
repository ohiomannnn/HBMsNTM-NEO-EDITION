package com.hbm.blockentity.bomb;

import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blockentity.NukeBaseBlockEntity;
import com.hbm.inventory.menus.NukeLittleBoyMenu;
import com.hbm.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class NukeLittleBoyBlockEntity extends NukeBaseBlockEntity {

    public NukeLittleBoyBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.NUKE_LITTLE_BOY.get(), pos, state, 5);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.nukeLittleBoy"); }
    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        Item item = stack.getItem();
        return item == ModItems.LITTLE_BOY_SHIELDING.get() ||
                item == ModItems.LITTLE_BOY_TARGET.get() ||
                item == ModItems.LITTLE_BOY_BULLET.get() ||
                item == ModItems.LITTLE_BOY_PROPELLANT.get() ||
                item == ModItems.LITTLE_BOY_IGNITER.get();
    }

    @Override
    public boolean isReady() {
        return slots.get(0).getItem() == ModItems.LITTLE_BOY_SHIELDING.get() &&
                slots.get(1).getItem() == ModItems.LITTLE_BOY_TARGET.get() &&
                slots.get(2).getItem() == ModItems.LITTLE_BOY_BULLET.get() &&
                slots.get(3).getItem() == ModItems.LITTLE_BOY_PROPELLANT.get() &&
                slots.get(4).getItem() == ModItems.LITTLE_BOY_IGNITER.get();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new NukeLittleBoyMenu(id, inventory, this);
    }
}
