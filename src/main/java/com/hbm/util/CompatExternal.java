package com.hbm.util;

import com.hbm.blocks.BlockDummyable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

/**
 * EXTERNAL COMPATIBILITY CLASS - DO NOT CHANGE METHOD NAMES/PARAMS ONCE CREATED
 * Is there a smarter way to do this? Most likely. Is there an easier one? Probably not.
 * @author hbm
 */
public class CompatExternal {

    /**
     * Gets the tile entity at that pos. If the tile entity is an mk1 or mk2 dummy, it will return the core instead.
     * This method will be updated in the event that other multiblock systems or dummies are added to retrain the intended functionality.
     * @return the core tile entity if the given position holds a dummy, the tile entity at that position if it doesn't or null if there is no tile entity
     */
    @Nullable
    public static BlockEntity getCoreFromPos(Level level, BlockPos pos) {

        Block block = level.getBlockState(pos).getBlock();

        // if the block at that pos is a Dummyable, use the mk2's system to find the core
        if (block instanceof BlockDummyable dummyable) {
            BlockPos core = dummyable.findCore(level, pos);

            if (core != null) {
                return level.getBlockEntity(core);
            }
        }

        BlockEntity be = level.getBlockEntity(pos);

        // otherwise, return the tile at that position whihc could be null
        return be;
    }
}
