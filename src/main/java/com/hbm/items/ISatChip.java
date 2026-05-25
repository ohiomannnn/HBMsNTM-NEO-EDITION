package com.hbm.items;

import com.hbm.util.TagsUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface ISatChip {
    static int getFreqS(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ISatChip satChip) {
            return satChip.getFreq(stack);
        }

        return 0;
    }

    static void setFreqS(ItemStack stack, int freq) {
        if (!stack.isEmpty() && stack.getItem() instanceof ISatChip satChip) {
            satChip.setFreq(stack, freq);
        }
    }

    default int getFreq(ItemStack stack) {
        if (!TagsUtil.hasCData(stack)) {
            return 0;
        }
        CompoundTag tag = TagsUtil.getCData(stack);
        return tag.getInt("freq");
    }

    default void setFreq(ItemStack stack, int freq) {
        CompoundTag tag = TagsUtil.getCData(stack);
        tag.putInt("freq", freq);
        TagsUtil.putCData(stack, tag);
    }
}
