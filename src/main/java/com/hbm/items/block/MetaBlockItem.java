package com.hbm.items.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class MetaBlockItem extends BlockItem {

    public MetaBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
//
//    @Override
//    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
//        return context.getLevel().setBlock(context.getClickedPos(), state.setValue(EnumTypes.PIPE_TYPES, EnumUtil.grabEnumSafely(EnumTypes.PIPE_TYPES.getValueClass(), context.getItemInHand().getOrDefault(ModDataComponents.META, 0))), 11);
//    }
}
