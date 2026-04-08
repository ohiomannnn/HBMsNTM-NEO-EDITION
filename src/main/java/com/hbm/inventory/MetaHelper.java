package com.hbm.inventory;

import com.hbm.items.component.NtmDataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class MetaHelper {

    public static final int WILDCARD_VALUE = Short.MAX_VALUE;

    /** Gets meta from the stack */
    public static int getMeta(ItemStack stack) { return stack.getOrDefault(NtmDataComponents.META.get(), 0); }
    /** Sets meta to the stack */
    public static void setMeta(ItemStack stack, int meta) { stack.set(NtmDataComponents.META.get(), meta); }

    /** Creates stack and sets meta */
    public static ItemStack metaStack(ItemStack stack, int meta) { stack.set(NtmDataComponents.META.get(), meta); return stack; }

    public static ItemStack newStack(ItemLike item, int meta) {
        ItemStack stack = new ItemStack(item, 1);
        return metaStack(stack, meta);
    }

    public static ItemStack newStack(ItemLike item, int count, int meta) {
        ItemStack stack = new ItemStack(item, count);
        return metaStack(stack, meta);
    }
}
