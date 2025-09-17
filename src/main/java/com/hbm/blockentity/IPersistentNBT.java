package com.hbm.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public interface IPersistentNBT {

    String NBT_PERSISTENT_KEY = "persistent";

    void writeNBT(CompoundTag nbt);
    void readNBT(CompoundTag nbt);

    default List<ItemStack> getDrops(Block block) {
        List<ItemStack> list = new ArrayList<>();
        ItemStack stack = new ItemStack(block);
        CompoundTag data = new CompoundTag();
        writeNBT(data);
        if (!data.isEmpty()) {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
        }
        list.add(stack);
        return list;
    }

    static List<ItemStack> getDrops(Level level, BlockPos pos, Block block) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof IPersistentNBT persistent) {
            return persistent.getDrops(block);
        }
        return new ArrayList<>();
    }

    static void restoreData(Level level, BlockPos pos, ItemStack stack) {
        if (!stack.has(DataComponents.CUSTOM_DATA)) return;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof IPersistentNBT persistent) {
            CompoundTag tag = stack.get(DataComponents.CUSTOM_DATA).copyTag();
            persistent.readNBT(tag);
        }
    }
}
