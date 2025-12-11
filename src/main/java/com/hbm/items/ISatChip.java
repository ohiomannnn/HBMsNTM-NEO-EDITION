package com.hbm.items;

import com.hbm.util.TagsUtilDegradation;
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
        if (!TagsUtilDegradation.containsAnyTag(stack)) {
            return 0;
        }
        CompoundTag tag = TagsUtilDegradation.getTag(stack);
        return tag.getInt("freq");
    }

    default void setFreq(ItemStack stack, int freq) {
        CompoundTag tag = TagsUtilDegradation.getTag(stack);
        tag.putInt("freq", freq);
        TagsUtilDegradation.putTag(stack, tag);
    }
}
