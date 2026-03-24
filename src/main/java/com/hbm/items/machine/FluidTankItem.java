package com.hbm.items.machine;

import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.fluid.Fluids;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FluidTankItem extends Item {

    public FluidTankItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(this.getDescriptionId(), Fluids.fromID(MetaHelper.getMeta(stack)).getName());
    }
}
