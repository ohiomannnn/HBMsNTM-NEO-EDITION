package com.hbm.inventory;

import com.hbm.items.component.NtmDataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class MetaHelper {

    // pro tip: if item is vanilla or is from other mod, use this
    public static final int WILDCARD_VALUE = Short.MAX_VALUE;

    /** Gets meta from the stack */
    public static int getMeta(ItemStack stack) { return stack.getOrDefault(NtmDataComponents.META.get(), 0); }
    /** Sets meta to the stack */
    public static void setMeta(ItemStack stack, int meta) { stack.set(NtmDataComponents.META.get(), meta); }

    public static ItemStack metaStack(ItemStack stack, int meta) { stack.set(NtmDataComponents.META.get(), meta); return stack; }

    /** Creates stack and sets meta */
    public static ItemStack newStack(ItemLike item, int meta) {
        return newStack(item, 1, meta);
    }

    public static ItemStack newStack(ItemLike item, int count, int meta) {
        ItemStack stack = new ItemStack(item, count);
        return metaStack(stack, meta);
    }

    public static ItemStack newStack(ItemLike item, Enum<?> theEnum) {
        return newStack(item, 1, theEnum.ordinal());
    }

    public static ItemStack newStack(ItemLike item, int count, Enum<?> theEnum) {
        ItemStack stack = new ItemStack(item, count);
        return metaStack(stack, theEnum.ordinal());
    }

    public static boolean compareStacks(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && getMeta(stack1) == getMeta(stack2);
    }
}
