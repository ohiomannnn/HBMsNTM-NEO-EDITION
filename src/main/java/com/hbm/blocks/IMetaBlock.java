package com.hbm.blocks;

import net.minecraft.world.level.block.state.BlockState;

public interface IMetaBlock {

    /** Gets current block meta, save meta with IntegerProperty */
    int getMeta(BlockState state);
    /** Max meta value, if you gonna use IntegerProperty, use its max value*/
    int getMaxMeta();
}
