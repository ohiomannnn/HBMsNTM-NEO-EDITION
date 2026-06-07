package com.hbm.blockentity;

import com.hbm.blocks.IMultiBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityNT extends BlockEntity {

    public BlockEntityNT(BlockEntityType<? extends BlockEntityNT> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public boolean isValidBlockState(BlockState state) {
        return super.isValidBlockState(state) || !(state.getBlock() instanceof IMultiBlock);
    }

    public int getMeta() {
        return ((IMultiBlock) this.getBlockState().getBlock()).getMeta(this.getBlockState());
    }
}
