package com.hbm.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

/**
 * Masks operation such as isItemValidForSlot and getAccessibleSlotsFromSide found in ISidedInveotry
 * Intended to be used to return a different result depending on the port, assuming the port detects IConditionalInvAccess
 *
 * @author hbm
 */
public interface IConditionalInvAccess {

    boolean isItemValidForSlot(BlockPos pos, int slot, ItemStack stack);
    default boolean canInsertItem(BlockPos pos, int slot, ItemStack stack, Direction direction) { return isItemValidForSlot(pos, slot, stack); }
    boolean canExtractItem(BlockPos pos, int slot, ItemStack stack, Direction direction);
    int[] getAccessibleSlotsFromSide(BlockPos pos, Direction direction);
}