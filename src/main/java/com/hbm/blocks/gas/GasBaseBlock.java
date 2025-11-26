package com.hbm.blocks.gas;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;

public abstract class GasBaseBlock extends Block {

    public GasBaseBlock(Properties properties) {
        super(properties);
    }

    @Override public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) { return Shapes.empty(); }
    @Override public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) { return Shapes.empty(); }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }

    @Override public boolean canBeReplaced(BlockState state, BlockPlaceContext context) { return true; }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 10);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean isMoving) {
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 10);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!tryMove(level, pos, getFirstDirection(level, pos))) {
            if (!tryMove(level, pos, getSecondDirection(level, pos))) {
                level.scheduleTick(pos, this, getDelay(level));
            }
        }
    }

    public abstract Direction getFirstDirection(Level level, BlockPos pos);

    public Direction getSecondDirection(Level level, BlockPos pos) {
        return getFirstDirection(level, pos);
    }

    public boolean tryMove(ServerLevel level, BlockPos pos, Direction dir) {
        BlockPos targetPos = pos.relative(dir);

        if (level.getBlockState(targetPos).isAir()) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            level.setBlock(targetPos, this.defaultBlockState(), 2);
            level.scheduleTick(targetPos, this, getDelay(level));
            return true;
        }

        return false;
    }

    public int getDelay(LevelAccessor levelAccessor) {
        return 2;
    }

    public Direction randomHorizontal(RandomSource random) {
        return Direction.Plane.HORIZONTAL.getRandomDirection(random);
    }
}