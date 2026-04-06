package com.hbm.handler.jei.subtypes;

import com.hbm.items.component.NtmDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import javax.annotation.Nullable;

public class BatterySubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final BatterySubtypeInterpreter INSTANCE = new BatterySubtypeInterpreter();

    private BatterySubtypeInterpreter() { }

    @Override
    public @Nullable Object getSubtypeData(ItemStack stack, UidContext context) {
        Integer meta = stack.get(NtmDataComponents.META);
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);

        return new BatterySubtype(meta, customData);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack stack, UidContext context) {
        Integer meta = stack.get(NtmDataComponents.META);
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return meta + "_" + (customData != null ? customData.hashCode() : "empty");
    }

    private record BatterySubtype(Integer meta, CustomData customData) {}
}