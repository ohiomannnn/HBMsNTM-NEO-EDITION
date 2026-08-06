package com.hbm.blocks.network;

import com.hbm.blockentity.network.RadioTorchSenderBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RadioTorchSenderBlock extends RadioTorchRWBaseBlock {

    public RadioTorchSenderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RadioTorchSenderBlockEntity(pos, state);
    }
}
