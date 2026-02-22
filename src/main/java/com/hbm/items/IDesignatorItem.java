package com.hbm.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface IDesignatorItem {
    /**
     * Whether the target is valid
     * @param level for things like restricting dimensions or getting entities
     * @param stack to check NBT and metadata
     * @param pos position of the launch pad
     */
    boolean isReady(Level level, ItemStack stack, BlockPos pos);

    /**
     * The target position if the designator is ready
     * @return the target
     */
    Vec3 getCoords(Level level, ItemStack stack, BlockPos pos);
}
