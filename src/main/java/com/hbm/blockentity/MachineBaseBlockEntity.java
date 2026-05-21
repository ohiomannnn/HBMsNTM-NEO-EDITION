package com.hbm.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class MachineBaseBlockEntity extends LoadedBaseBlockEntity implements WorldlyContainer, Nameable, MenuProvider, ITickable {

    public NonNullList<ItemStack> slots;

    @Nullable private Component customName;

    public MachineBaseBlockEntity(BlockEntityType<? extends MachineBaseBlockEntity> type, BlockPos pos, BlockState state, int size) {
        super(type, pos, state);
        this.slots = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    /** The "chunks is modified, pls don't forget to save me" effect of markDirty, minus the block updates */
    @Deprecated // copy of setChanged!!!
    public void markChanged() {
        if (level != null) {
            level.blockEntityChanged(this.worldPosition);
        }
    }

    public Component getName() {
        return this.customName != null ? this.customName : this.getDefaultName();
    }

    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    public Component getCustomName() {
        return this.customName;
    }

    protected abstract Component getDefaultName();

    @Override
    public int getContainerSize() {
        return this.slots.size();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.slots.get(slot);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.slots.set(index, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override public void startOpen(Player player) {}
    @Override public void stopOpen(Player player) {}

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.slots, slot, amount);
        if(!itemstack.isEmpty()) this.setChanged();
        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.slots, slot);
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : this.slots) {
            if(!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public void clearContent() {
        this.slots.clear();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { 0 };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        ContainerHelper.loadAllItems(tag, this.slots, registries);

        if(tag.contains("CustomName", 8)) this.customName = parseCustomNameSafe(tag.getString("CustomName"), registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        ContainerHelper.saveAllItems(tag, this.slots, registries);

        if(this.customName != null) tag.putString("CustomName", Serializer.toJson(this.customName, registries));
    }
}
