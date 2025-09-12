package com.hbm.blocks.bomb;

import com.hbm.particle.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TestBomb extends Block {
    public TestBomb(Properties properties) {
        super(properties);
    }
    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (movedByPiston) {
            if (!level.isClientSide) {

                ServerLevel serverLevel = (ServerLevel) level;
                serverLevel.sendParticles(ModParticles.MUKE_FLASH.get(), pos.getX(), pos.getY(), pos.getZ(), 1, 0.0,0.0, 0.0, 0.0);
                serverLevel.sendParticles(ModParticles.MUKE_WAVE.get(), pos.getX(), pos.getY(), pos.getZ(), 1, 0.0,0.0, 0.0, 0.0);
            }
        }
    }
}
