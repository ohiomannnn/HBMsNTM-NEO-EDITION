package com.hbm.blockentity.network;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blockentity.network.RTTYSystem.RTTYChannel;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.EntityProcessorCrossSmooth;
import com.hbm.explosion.vanillant.standard.ExplosionEffectWeapon;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RadioTorchReceiverBlockEntity extends RadioTorchBaseBlockEntity {

    public RadioTorchReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.RADIO_TORCH_RECEIVER.get(), pos, state);
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {

            if(!this.channel.isEmpty()) {

                RTTYChannel chan = RTTYSystem.listen(level, this.channel);

                if(chan != null && (this.polling || (chan.timeStamp > this.lastUpdate - 1 && chan.timeStamp != -1))) { // if we're either polling or a new message has come in
                    String msg = "" + chan.signal;
                    this.lastUpdate = this.level.getGameTime();
                    int nextState = 0; //if no remap apply, default to 0

                    if("selfdestruct".equals(msg)) {
                        BlockPos pos = this.getBlockPos();
                        this.level.removeBlock(pos, false);
                        ExplosionVNT vnt = new ExplosionVNT(this.level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5)
                                .setEntityProcessor(new EntityProcessorCrossSmooth(1, 50).setupPiercing(5F, 0.5F))
                                .setSFX(new ExplosionEffectWeapon(10, 2.5F, 1F));
                        vnt.explode();
                        return;
                    }

                    if(this.customMap) {
                        for(int i = 15; i >= 0; i--) { // highest to lowest, if duplicates exist for some reason
                            if(msg.equals(this.mapping[i])) {
                                nextState = i;
                                break;
                            }
                        }
                    } else {
                        int sig = 0;
                        try { sig = Integer.parseInt(msg); } catch(Exception ignored) { };
                        nextState = Mth.clamp(sig, 0, 15);
                    }

                    if(chan.timeStamp < this.lastUpdate - 2 && this.polling) {
                        nextState = 0;
                    }

                    if(this.lastState != nextState) {
                        this.lastState = nextState;
                        BlockState state = this.getBlockState();
                        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
                        level.updateNeighborsAt(worldPosition, state.getBlock());
                        this.setChanged();
                    }
                }
            }
        }

        super.updateEntity();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if(this.level != null) this.lastUpdate = this.level.getGameTime();
    }
}
