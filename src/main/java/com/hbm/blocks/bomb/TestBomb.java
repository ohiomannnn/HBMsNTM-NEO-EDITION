package com.hbm.blocks.bomb;

import com.hbm.packets.toclient.AuxParticlePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public class TestBomb extends Block {
    public TestBomb(Properties properties) {
        super(properties);
    }
    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (movedByPiston) {
            if (!level.isClientSide) {
                ServerLevel serverLevel = (ServerLevel) level;
                CompoundTag nbt  = new CompoundTag();
                nbt.putString("type", "muke");

                PacketDistributor.sendToPlayersNear(
                        serverLevel,
                        null,
                        pos.getX(), pos.getY(), pos.getZ(),
                        1000,
                        new AuxParticlePacket(nbt, pos.getX(), pos.getY(), pos.getZ())
                );
            }
        }
    }
}
