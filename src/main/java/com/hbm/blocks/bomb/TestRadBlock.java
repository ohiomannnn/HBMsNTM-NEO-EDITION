package com.hbm.blocks.bomb;

import com.hbm.handler.radiation.ChunkRadiationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TestRadBlock extends Block {
    public TestRadBlock(Properties properties) {
        super(properties);
    }
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        ChunkRadiationManager.getProxy().setRadiation(level, pos.getX(), pos.getY(), pos.getZ(), 1000F);
    }
}
