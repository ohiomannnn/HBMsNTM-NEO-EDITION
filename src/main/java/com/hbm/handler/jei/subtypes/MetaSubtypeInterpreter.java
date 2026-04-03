package com.hbm.handler.jei.subtypes;

import com.hbm.items.component.NtmDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class MetaSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final MetaSubtypeInterpreter INSTANCE = new MetaSubtypeInterpreter();

    private MetaSubtypeInterpreter() { }

    @Override
    public @Nullable Object getSubtypeData(ItemStack stack, UidContext context) {
        return stack.get(NtmDataComponents.META);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack stack, UidContext context) {
        return String.valueOf(NtmDataComponents.META);
    }
}
