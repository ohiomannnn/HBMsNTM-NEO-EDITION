package com.hbm.inventory;

import com.hbm.items.component.NtmDataComponents;
import net.minecraft.world.item.ItemStack;

public class MetaHelper {

    /** Gets meta from the stack */
    public static int getMeta(ItemStack stack) { return stack.getOrDefault(NtmDataComponents.META.get(), 0); }
    /** Sets meta to the stack */
    public static void setMeta(ItemStack stack, int meta) { stack.set(NtmDataComponents.META.get(), meta); }

    /** Creates stack and sets meta */
    public static ItemStack metaStack(ItemStack stack, int meta) { stack.set(NtmDataComponents.META.get(), meta); return stack; }
}
