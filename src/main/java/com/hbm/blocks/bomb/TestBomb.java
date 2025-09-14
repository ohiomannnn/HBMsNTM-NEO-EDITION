package com.hbm.blocks.bomb;

import com.hbm.explosion.ExplosionNukeSmall;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TestBomb extends Block {
    public TestBomb(Properties properties) {
        super(properties);
    }
    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide) {
            ExplosionNukeSmall.explode(level, pos.getX(), pos.getY(), pos.getZ(), ExplosionNukeSmall.PARAMS_MEDIUM);
        }
    }
}
