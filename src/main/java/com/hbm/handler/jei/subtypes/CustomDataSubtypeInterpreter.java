package com.hbm.handler.jei.subtypes;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import javax.annotation.Nullable;

public class CustomDataSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final CustomDataSubtypeInterpreter INSTANCE = new CustomDataSubtypeInterpreter();

    private CustomDataSubtypeInterpreter() { }

    @Override
    public @Nullable Object getSubtypeData(ItemStack stack, UidContext context) {
        return stack.get(DataComponents.CUSTOM_DATA);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack stack, UidContext context) {
        CustomData amplifier = stack.get(DataComponents.CUSTOM_DATA);
        if (amplifier == null) {
            return "";
        }
        return amplifier.toString();
    }
}
