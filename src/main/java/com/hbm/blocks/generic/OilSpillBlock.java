package com.hbm.blocks.generic;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class OilSpillBlock extends LayeringBlock {

    public OilSpillBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if(context.getLevel().getBlockState(context.getClickedPos()).is(this)) {
            return null;
        }

        return super.getStateForPlacement(context);
    }
}
