package com.hbm.blockentity;

import com.hbm.util.CompatExternal;
import com.hbm.util.TagsUtilDegradation;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;

public interface IPersistentNBT {

    String NBT_PERSISTENT_KEY = "persistent";

    void writeNBT(CompoundTag nbt);
    void readNBT(CompoundTag nbt);

    default ArrayList<ItemStack> getDrops(Block block) {
        ArrayList<ItemStack> list = new ArrayList<>();
        ItemStack stack = new ItemStack(block);
        CompoundTag tag = new CompoundTag();
        writeNBT(tag);
        if (!tag.isEmpty()) {
            TagsUtilDegradation.putTag(stack, tag);
        }
        list.add(stack);
        return list;
    }

    static ArrayList<ItemStack> getDrops(Level level, BlockPos pos, Block b) {

        BlockEntity be = CompatExternal.getCoreFromPos(level, pos);

        if (be instanceof IPersistentNBT persistentNBT) {
            return persistentNBT.getDrops(b);
        }

        return new ArrayList<>();
    }

    static void restoreData(Level level, BlockPos pos, ItemStack stack) {
        try {
            if (!TagsUtilDegradation.containsAnyTag(stack)) return;
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof IPersistentNBT persistentNBT)) return;
            persistentNBT.readNBT(TagsUtilDegradation.getTag(stack));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
