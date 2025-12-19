package com.hbm.blockentity.bomb;

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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class NukeFatManBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler SLOTS = new ItemStackHandler(6) {
        @Override protected void onContentsChanged(int slot) { setChanged(); }

        @Override public int getSlotLimit(int slot) { return 1; }
    };

    public ItemStackHandler getItems() {
        return SLOTS;
    }

    public NukeFatManBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.NUKE_FATMAN.get(), pos, blockState);
    }

    public boolean exp1() {
        return SLOTS.getStackInSlot(1).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get();
    }

    public boolean exp2() {
        return SLOTS.getStackInSlot(2).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get();
    }

    public boolean exp3() {
        return SLOTS.getStackInSlot(3).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get();
    }

    public boolean exp4() {
        return SLOTS.getStackInSlot(4).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get();
    }

    public boolean isReady() {
        if (this.exp1() && this.exp2() && this.exp3() && this.exp4()) {
            return SLOTS.getStackInSlot(0).getItem() == ModItems.MAN_IGNITER.get() && SLOTS.getStackInSlot(5).getItem() == ModItems.MAN_CORE.get();
        }

        return false;
    }


    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        tag.put("Slots", SLOTS.serializeNBT(provider));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        SLOTS.deserializeNBT(provider, tag.getCompound("Items"));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.nuke_fatman");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new NukeFatManMenu(id, inventory, this);
    }
}
