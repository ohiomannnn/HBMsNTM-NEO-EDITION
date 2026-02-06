package com.hbm.blockentity;

import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class MachineBaseBlockEntity extends LoadedBaseBlockEntity implements WorldlyContainer, Nameable, MenuProvider {

    public NonNullList<ItemStack> slots;

    private Component customName;

    public MachineBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int size) {
        super(type, pos, blockState);
        slots = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    /** The "chunks is modified, pls don't forget to save me" effect of markDirty, minus the block updates */
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
    public ItemStack getItem(int index) {
        return slots.get(index);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack itemstack = this.slots.get(index);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.slots.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        this.slots.set(index, itemStack);
        itemStack.limitSize(this.getMaxStackSize(itemStack));
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player player) {
        if (level != null && level.getBlockEntity(this.getBlockPos()) != this) {
            return false;
        } else {
            return player.distanceToSqr(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5) <= 128;
        }
    }

    @Override public void startOpen(Player player) {}
    @Override public void stopOpen(Player player) {}

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = slots.get(slot);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (stack.getCount() <= amount) {
            slots.set(slot, ItemStack.EMPTY);
            return stack;
        }
        ItemStack split = stack.split(amount);
        if (stack.isEmpty()) {
            slots.set(slot, ItemStack.EMPTY);
        }
        return split;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(slot, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { };
    }

    public abstract void updateEntity();

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        ListTag list = tag.getList("Items", 10);

        for (int i = 0; i < list.size(); i++) {
            CompoundTag tagAt = list.getCompound(i);
            byte b = tagAt.getByte("Slot");
            if (b >= 0 && b < slots.size()) {
                slots.set(b, ItemStack.parse(registries, tagAt).orElse(ItemStack.EMPTY));
            }
        }

        if (tag.contains("Name", 8)) {
            this.customName = parseCustomNameSafe(tag.getString("Name"), registries);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        ListTag list = new ListTag();

        for (int i = 0; i < slots.size(); i++) {
            ItemStack stack = slots.get(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                list.add(stack.save(registries, itemTag));
            }
        }

        tag.put("Items", list);

        if (this.customName != null) {
            tag.putString("Name", Component.Serializer.toJson(this.customName, registries));
        }
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : slots) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clearContent() {
        slots.clear();
    }

    public void updateRedstoneConnection(DirPos pos) {

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        Direction dir = pos.getDir();
        BlockState s = level.getBlockState(pos.makeCompat());
        Block b = s.getBlock();

        b.onNeighborChange(s, level, pos.makeCompat(), this.getBlockPos());
        if (s.isSolidRender(level, pos.makeCompat())) {
            x += dir.getStepX();
            y += dir.getStepY();
            z += dir.getStepZ();
            BlockPos newPos = new BlockPos(x, y, z);
            BlockState s2 = level.getBlockState(pos.makeCompat());
            Block b2 = s2.getBlock();

            if (b2.getWeakChanges(s2, level, newPos)) {
                b2.onNeighborChange(s2, level, newPos, this.getBlockPos());
            }
        }
    }
}
