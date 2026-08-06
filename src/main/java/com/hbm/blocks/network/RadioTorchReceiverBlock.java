package com.hbm.blocks.network;

import com.hbm.blockentity.network.RadioTorchReceiverBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RadioTorchReceiverBlock extends RadioTorchRWBaseBlock {

    public RadioTorchReceiverBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RadioTorchReceiverBlockEntity(pos, state);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockEntity tile = level.getBlockEntity(pos);
        if(tile instanceof RadioTorchReceiverBlockEntity receiver) {
            return receiver.lastState;
        }
        return 0;
    }
}
