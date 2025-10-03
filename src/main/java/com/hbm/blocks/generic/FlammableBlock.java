package com.hbm.blocks.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FlammableBlock extends Block {

    public int encouragement;
    public int flammability;

    public FlammableBlock(Properties properties, int en, int flam) {
        super(properties);
        this.encouragement = en;
        this.flammability = flam;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return flammability;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return encouragement;
    }

    public boolean shouldIgnite(Level level, BlockPos pos) {
        if (flammability == 0) return false;

        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            if (level.getBlockState(neighborPos).is(Blocks.FIRE)) {
                return true;
            }
        }

        return false;
    }
}
