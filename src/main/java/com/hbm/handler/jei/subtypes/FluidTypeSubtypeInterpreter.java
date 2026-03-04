package com.hbm.handler.jei.subtypes;

import com.hbm.items.datacomps.FluidTypeComponent;
import com.hbm.items.datacomps.ModDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import javax.annotation.Nullable;

public class FluidTypeSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final FluidTypeSubtypeInterpreter INSTANCE = new FluidTypeSubtypeInterpreter();

    private FluidTypeSubtypeInterpreter() { }

    @Override
    public @Nullable Object getSubtypeData(ItemStack stack, UidContext context) {
        return stack.get(ModDataComponents.FLUID_TYPE);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack stack, UidContext context) {
        FluidTypeComponent amplifier = stack.get(ModDataComponents.FLUID_TYPE.get());
        if (amplifier == null) {
            return "";
        }
        return amplifier.toString();
    }
}
