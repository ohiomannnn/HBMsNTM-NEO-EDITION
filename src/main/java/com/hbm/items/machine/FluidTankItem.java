package com.hbm.items.machine;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.datacomps.FluidTypeComponent;
import com.hbm.items.datacomps.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FluidTankItem extends Item {

    public FluidTankItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(this.getDescriptionId(), FluidTypeComponent.getFluidType(stack).getName());
    }

    public static ItemStack createStack(Item item, FluidType type) {
        ItemStack stack = new ItemStack(item);
        stack.set(ModDataComponents.FLUID_TYPE.get(), new FluidTypeComponent(type.getID()));
        return stack;
    }
}
