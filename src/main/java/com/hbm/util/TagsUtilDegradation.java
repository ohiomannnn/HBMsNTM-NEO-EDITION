package com.hbm.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class TagsUtilDegradation {
    public static boolean containsAnyTag(ItemStack stack) {
        return stack.has(DataComponents.CUSTOM_DATA);
    }

    /**
     * After you put some values in the tag, DON'T FORGET ABOUT PUT IT BACK TO THE STACK WITH {@link TagsUtilDegradation#putTag}
     */
    public static CompoundTag getTag(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null ? customData.copyTag() : new CompoundTag();
    }

    public static void putTag(ItemStack stack, CompoundTag newTag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(newTag));
    }
}
