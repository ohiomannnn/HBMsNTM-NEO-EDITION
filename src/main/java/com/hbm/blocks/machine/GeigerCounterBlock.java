package com.hbm.blocks.machine;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blockentity.machine.GeigerBlockEntity;
import com.hbm.blocks.bomb.CrashedBombBlock;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.lib.ModSounds;
import com.hbm.util.ContaminationUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class GeigerCounterBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING;

    public GeigerCounterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public static final MapCodec<GeigerCounterBlock> CODEC = simpleCodec(GeigerCounterBlock::new);
    @Override protected MapCodec<GeigerCounterBlock> codec() { return CODEC; }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction dir = blockState.getValue(FACING);

        return switch (dir) {
            case WEST -> Block.box(2, 0, 1.5, 16, 9, 16);
            case EAST -> Block.box(0, 0, 0, 14, 9, 14.5);
            case SOUTH -> Block.box(1.5, 0, 0, 16, 9, 14);
            default -> Block.box(0, 0, 2, 14.5, 9, 16);
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(blockState, level, pos, context);
    }

    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {

        float rad = ChunkRadiationManager.proxy.getRadiation(level, pos);

        // 0 at exactly 0 rads/sec
        // +1 per 5 rads/sec
        // 15 at 75+ rads/sec
        return Math.min((int)Math.ceil(rad / 5f), 15);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : BaseEntityBlock.createTickerHelper(type, ModBlockEntities.GEIGER_COUNTER.get(), GeigerBlockEntity::serverTick);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GeigerBlockEntity(ModBlockEntities.GEIGER_COUNTER.get(), blockPos, blockState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            level.playSound(null, pos, ModSounds.TECH_BOOP.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
            ContaminationUtil.printGeigerDataFromCoords(player, pos);
        }
        return InteractionResult.SUCCESS;
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
    }
}
