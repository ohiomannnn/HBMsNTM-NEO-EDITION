package com.hbm.blockentity.network;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.network.RadioTorchBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RadioTorchSenderBlockEntity extends RadioTorchBaseBlockEntity {

    public RadioTorchSenderBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.RADIO_TORCH_SENDER.get(), pos, state);
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            Direction dir = this.getBlockState().getValue(RadioTorchBaseBlock.FACING).getOpposite();
            BlockPos neighborPos = worldPosition.relative(dir);
            int input = level.getSignal(neighborPos, dir);

            BlockState neighborState = level.getBlockState(neighborPos);
            if(neighborState.hasAnalogOutputSignal()) {
                input = neighborState.getAnalogOutputSignal(level, neighborPos);
            }

            boolean shouldSend = this.polling;

            if(input != this.lastState) {
                this.setChanged();
                BlockState state = this.getBlockState();
                level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
                this.lastState = input;
                shouldSend = true;
            }

            if(shouldSend && !this.channel.isEmpty()) {
                String toSend = this.customMap ? this.mapping[input] : (input + "");
                if(toSend != null && !toSend.isEmpty()) RTTYSystem.broadcast(this.level, this.channel, toSend);
            }
        }

        super.updateEntity();
    }
}
