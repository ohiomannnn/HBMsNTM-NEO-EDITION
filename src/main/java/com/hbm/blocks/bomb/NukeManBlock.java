package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukeFatManBlockEntity;
import com.hbm.config.MainConfig;
import com.hbm.entity.effect.NukeTorex;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.interfaces.IBomb;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class NukeManBlock extends BaseEntityBlock implements IBomb {

    public static final DirectionProperty FACING;

    public NukeManBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(((this.stateDefinition.any()).setValue(FACING, Direction.NORTH)));
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

    public static final MapCodec<LandmineBlock> CODEC = simpleCodec(LandmineBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NukeFatManBlockEntity(blockPos, blockState);
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            igniteBomb(level, pos, MainConfig.COMMON.MAN_RADIUS.get());
        }

        return BombReturnCode.DETONATED;
    }

    public void igniteBomb(Level level, BlockPos pos, int strength) {
        if (!level.isClientSide) {
            level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0f, level.random.nextFloat() * 0.1F + 0.9F);

            NukeExplosionMK5.statFac(level, strength, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            NukeTorex.statFacStandard(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, strength);
        }
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
    }
}
