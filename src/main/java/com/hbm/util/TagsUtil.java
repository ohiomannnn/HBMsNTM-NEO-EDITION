package com.hbm.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class TagsUtil {
    public static boolean isValid(ItemStack stack) {
        return stack != null && !stack.isEmpty();
    }

    public static boolean hasTag(ItemStack stack) {
        return isValid(stack) && stack.has(DataComponents.CUSTOM_DATA);
    }

    public static CompoundTag getTag(ItemStack stack) {
        if (!hasTag(stack)) return new CompoundTag();
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null ? customData.copyTag() : new CompoundTag();
    }

    public static void setTag(ItemStack stack, CompoundTag newTag) {
        if (!isValid(stack)) return;
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(newTag));
    }

    public static CompoundTag getOrCreateTag(ItemStack stack) {
        CompoundTag tag = getTag(stack);
        if (tag.isEmpty()) {
            tag = new CompoundTag();
            setTag(stack, tag);
        }
        return tag;
    }

    public static CompoundTag getTagElement(ItemStack stack, String key) {
        CompoundTag root = getTag(stack);
        return root.contains(key) ? root.getCompound(key) : null;
    }
}
