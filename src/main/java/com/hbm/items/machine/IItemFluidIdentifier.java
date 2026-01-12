package com.hbm.items.machine;

import com.hbm.inventory.fluid.FluidType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IItemFluidIdentifier {
    /**
     * World might be null if the ID is used inside of a GUI
     * Position only has to be accurate when used in-world
     */
    FluidType getType(Level level, BlockPos pos, ItemStack stack);
}
