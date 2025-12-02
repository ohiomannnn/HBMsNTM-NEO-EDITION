package com.hbm.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/**
 * Util for working with tags because mojang removed the "easy method"
 * @author ohiomannnn
 */
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

    public static void removeTag(ItemStack stack, String key) {
        CompoundTag tag = getTag(stack);
        if (tag.contains(key)) {
            tag.remove(key);
            setTag(stack, tag);
        }
    }

    public static void setString(ItemStack stack, String key, String value) {
        CompoundTag tag = getOrCreateTag(stack);
        tag.putString(key, value);
        setTag(stack, tag);
    }

    public static String getString(ItemStack stack, String key, String defaultValue) {
        CompoundTag tag = getTag(stack);
        return tag.contains(key) ? tag.getString(key) : defaultValue;
    }

    public static void setInt(ItemStack stack, String key, int value) {
        CompoundTag tag = getOrCreateTag(stack);
        tag.putInt(key, value);
        setTag(stack, tag);
    }

    public static int getInt(ItemStack stack, String key, int defaultValue) {
        CompoundTag tag = getTag(stack);
        return tag.contains(key) ? tag.getInt(key) : defaultValue;
    }

    public static void setIntArray(ItemStack stack, String key, int[] values) {
        CompoundTag tag = getOrCreateTag(stack);
        tag.putIntArray(key, values);
        setTag(stack, tag);
    }

    public static int[] getIntArray(ItemStack stack, String key) {
        CompoundTag tag = getTag(stack);
        return tag.contains(key) ? tag.getIntArray(key) : new int[0];
    }

    public static boolean contains(ItemStack stack, String key) {
        return getTag(stack).contains(key);
    }
}
