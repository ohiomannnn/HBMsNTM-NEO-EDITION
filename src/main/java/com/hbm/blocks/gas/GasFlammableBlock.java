package com.hbm.blocks.gas;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;

public class GasFlammableBlock extends GasBaseBlock {

    public static HashSet<Block> fireSources = new HashSet<>();

    public GasFlammableBlock(Properties properties) {
        super(properties);

        if (fireSources.isEmpty()) {
            fireSources.add(Blocks.FIRE);
            fireSources.add(Blocks.LAVA);
            fireSources.add(Blocks.TORCH);
            fireSources.add(Blocks.JACK_O_LANTERN);
        }
    }

    @Override
    public Direction getFirstDirection(Level level, BlockPos pos) {
        if (level.random.nextInt(3) == 0) {
            return level.random.nextBoolean() ? Direction.DOWN : Direction.UP;
        }

        return this.randomHorizontal(level.getRandom());
    }

    @Override
    public Direction getSecondDirection(Level level, BlockPos pos) {
        return this.randomHorizontal(level.getRandom());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide) {

            for (Direction dir : Direction.values()) {
                BlockPos checkPos = pos.relative(dir);
                Block b = level.getBlockState(checkPos).getBlock();

                if (isFireSourceForGas(b)) {
                    combust(level, pos);
                    return;
                }
            }

            if (random.nextInt(20) == 0 && level.isEmptyBlock(pos.below())) {
                level.removeBlock(pos, false);
                return;
            }
        }

        super.tick(state, level, pos, random);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity.isOnFire()) {
            combust(level, pos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {

        for (Direction dir : Direction.values()) {
            BlockPos checkPos = pos.relative(dir);
            Block b = level.getBlockState(checkPos).getBlock();

            if (isFireSourceForGas(b)) {
                level.scheduleTick(pos, this, 2);
            }
        }
    }

    protected void combust(Level level,BlockPos pos) {
        level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
    }

    public boolean isFireSourceForGas(Block b) {
        return fireSources.contains(b);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getDelay(LevelAccessor levelAccessor) {
        return levelAccessor.getRandom().nextInt(5) + 16;
    }
}
