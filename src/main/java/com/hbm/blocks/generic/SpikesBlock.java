package com.hbm.blocks.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SpikesBlock extends Block {

    public SpikesBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(entity.getDeltaMovement().y < -0.1) {
            entity.hurt(entity.damageSources().cactus(), 100F);
        }
    }
}
