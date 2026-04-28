package com.hbm.blocks.bomb;

import com.hbm.blocks.states.NtmBlockStateProperties;
import com.hbm.entity.item.TNTPrimedBase;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.lib.Library;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class DetCordBlock extends DetonatableBlock implements IDetConnectible {

    public static final BooleanProperty NORTH = NtmBlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = NtmBlockStateProperties.SOUTH;
    public static final BooleanProperty EAST =  NtmBlockStateProperties.EAST;
    public static final BooleanProperty WEST =  NtmBlockStateProperties.WEST;
    public static final BooleanProperty UP =    NtmBlockStateProperties.UP;
    public static final BooleanProperty DOWN =  NtmBlockStateProperties.DOWN;

    public DetCordBlock(Properties properties) {
        super(properties, 0, 0, 0, false, false);
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

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        state = state
                .setValue(NORTH, IDetConnectible.isConnectible(level, pos.relative(Direction.NORTH), Direction.NORTH))
                .setValue(SOUTH, IDetConnectible.isConnectible(level, pos.relative(Direction.SOUTH), Direction.SOUTH))
                .setValue(EAST,  IDetConnectible.isConnectible(level, pos.relative(Direction.EAST), Direction.EAST))
                .setValue(WEST,  IDetConnectible.isConnectible(level, pos.relative(Direction.WEST), Direction.WEST))
                .setValue(UP,    IDetConnectible.isConnectible(level, pos.relative(Direction.UP), Direction.UP))
                .setValue(DOWN,  IDetConnectible.isConnectible(level, pos.relative(Direction.DOWN), Direction.DOWN));
        return state;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    public void explode(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            ExplosionVNT.createExplosion(level, null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1.5F, true);
        }
    }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, TNTPrimedBase entity) {
        this.explode(level, BlockPos.containing(x, y, z));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.hasNeighborSignal(pos)) {
            this.explode(level, pos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.hasNeighborSignal(pos)) {
            this.explode(level, pos);
        }
    }
}
