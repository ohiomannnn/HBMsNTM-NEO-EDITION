package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukeFatManBlockEntity;
import com.hbm.blockentity.machine.MachineSatLinkerBlockEntity;
import com.hbm.config.MainConfig;
import com.hbm.entity.effect.NukeTorex;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.interfaces.IBomb;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

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

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof NukeFatManBlockEntity blockEntity) {
                NonNullList<ItemStack> stacks = NonNullList.create();
                for (int i = 0; i < blockEntity.getItems().getSlots(); i++) {
                    stacks.add(blockEntity.getItems().getStackInSlot(i));
                }
                if (level instanceof ServerLevel) {
                    Containers.dropContents(level, pos, stacks);
                }
                super.onRemove(state, level, pos, newState, isMoving);
            } else {
                super.onRemove(state, level, pos, newState, isMoving);
            }
        }

    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof NukeFatManBlockEntity entity) {
            if (!level.isClientSide) {
                player.openMenu(new SimpleMenuProvider(entity, entity.getDisplayName()), pos);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    public static final MapCodec<NukeManBlock> CODEC = simpleCodec(NukeManBlock::new);
    @Override protected MapCodec<NukeManBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukeFatManBlockEntity(pos, state);
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            NukeFatManBlockEntity blockEntity = (NukeFatManBlockEntity) level.getBlockEntity(pos);
            if (blockEntity == null) return BombReturnCode.UNDEFINED;
            if (blockEntity.isReady()) {
                for (int i = 0; i < blockEntity.getItems().getSlots(); i++) {
                    blockEntity.getItems().insertItem(i, ItemStack.EMPTY, false);
                }
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                NukeExplosionMK5.statFac(level, MainConfig.COMMON.MAN_RADIUS.get(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                NukeTorex.statFacStandard(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, MainConfig.COMMON.MAN_RADIUS.get());
                return BombReturnCode.DETONATED;
            }
            return BombReturnCode.ERROR_MISSING_COMPONENT;
        }

        return BombReturnCode.UNDEFINED;
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
    }
}
