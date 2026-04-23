package com.hbm.blocks.network;

import com.hbm.blockentity.Tickable;
import com.hbm.blockentity.network.CableBaseBlockEntity;
import com.hbm.lib.Library;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CableBlock extends Block implements EntityBlock {

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST =  BlockStateProperties.EAST;
    public static final BooleanProperty WEST =  BlockStateProperties.WEST;
    public static final BooleanProperty UP =    BlockStateProperties.UP;
    public static final BooleanProperty DOWN =  BlockStateProperties.DOWN;

    public CableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, Boolean.FALSE)
                .setValue(SOUTH, Boolean.FALSE)
                .setValue(EAST,  Boolean.FALSE)
                .setValue(WEST,  Boolean.FALSE)
                .setValue(UP,    Boolean.FALSE)
                .setValue(DOWN,  Boolean.FALSE)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    public static final MapCodec<CableBlock> CODEC = simpleCodec(CableBlock::new);
    @Override protected MapCodec<CableBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CableBaseBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> { if (be instanceof Tickable tickable) tickable.updateEntity(); };
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        state = state
                .setValue(NORTH, Library.canConnect(level, pos.relative(Direction.NORTH), Direction.NORTH))
                .setValue(SOUTH, Library.canConnect(level, pos.relative(Direction.SOUTH), Direction.SOUTH))
                .setValue(EAST,  Library.canConnect(level, pos.relative(Direction.EAST), Direction.EAST))
                .setValue(WEST,  Library.canConnect(level, pos.relative(Direction.WEST), Direction.WEST))
                .setValue(UP,    Library.canConnect(level, pos.relative(Direction.UP), Direction.UP))
                .setValue(DOWN,  Library.canConnect(level, pos.relative(Direction.DOWN), Direction.DOWN));
        return state;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {

        boolean posX = state.getValue(EAST);
        boolean negX = state.getValue(WEST);
        boolean posY = state.getValue(UP);
        boolean negY = state.getValue(DOWN);
        boolean posZ = state.getValue(SOUTH);
        boolean negZ = state.getValue(NORTH);

        return this.getBlockBounds(posX, negX, posY, negY, posZ, negZ);
    }

    private VoxelShape getBlockBounds(boolean posX, boolean negX, boolean posY, boolean negY, boolean posZ, boolean negZ) {

        double pixel = 0.0625D;
        double min = pixel * 5.5D;
        double max = pixel * 10.5D;

        double minX = negX ? 0D : min;
        double maxX = posX ? 1D : max;
        double minY = negY ? 0D : min;
        double maxY = posY ? 1D : max;
        double minZ = negZ ? 0D : min;
        double maxZ = posZ ? 1D : max;

        return Shapes.box(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
