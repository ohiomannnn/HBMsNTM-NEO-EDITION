package com.hbm.blockentity.bomb;

import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.ModBlockEntities;
import com.hbm.inventory.menus.NukeFatManMenu;
import com.hbm.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class NukeFatManBlockEntity extends MachineBaseBlockEntity implements MenuProvider {

    public NukeFatManBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NUKE_FATMAN.get(), pos, state, 6);
    }

    @Override public Component getName() { return Component.translatable("container.nukeFatman"); }

    @Override public void updateEntity() { }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return true;
    }

    public boolean exp1() {
        return slots.get(1).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get();
    }

    public boolean exp2() {
        return slots.get(2).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get();
    }

    public boolean exp3() {
        return slots.get(3).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get();
    }

    public boolean exp4() {
        return slots.get(4).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get();
    }

    public boolean isReady() {
        if (this.exp1() && this.exp2() && this.exp3() && this.exp4()) {
            return slots.get(0).getItem() == ModItems.MAN_IGNITER.get() &&
                    slots.get(5).getItem() == ModItems.MAN_CORE.get();
        }

        return false;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new NukeFatManMenu(id, inventory, this);
    }
}
