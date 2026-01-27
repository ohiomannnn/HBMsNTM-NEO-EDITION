package com.hbm.blocks.bomb;

import com.hbm.blockentity.NukeBaseBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.MainConfig;
import com.hbm.entity.effect.NukeTorex;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.interfaces.IBomb;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

// now were thinking with abstraction
// is it was too hard, or bob was too lazy?
public abstract class NukeBaseBlock extends BaseEntityBlock implements IBomb {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private int size = 0;

    public NukeBaseBlock(Properties properties) {
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
        if (level.getBlockEntity(pos) instanceof Container container) {
            Containers.dropContents(level, pos, container);
            level.updateNeighbourForOutputSignal(pos, this);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!player.isShiftKeyDown()) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof MenuProvider be) {
                player.openMenu(new SimpleMenuProvider(be, be.getDisplayName()), pos);
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * Because neoforge config system sucks, we will register sizes on FMLCommonSetupEvent, NOT on item registration
     */
    public NukeBaseBlock setSize(int size) {
        this.size = size;
        return this;
    }

    public static void registerSizes() {
        ModBlocks.NUKE_GADGET.get()      .setSize(MainConfig.COMMON.GADGET_RADIUS.get());
        ModBlocks.NUKE_LITTLE_BOY.get()  .setSize(MainConfig.COMMON.BOY_RADIUS.get());
        ModBlocks.NUKE_FAT_MAN.get()     .setSize(MainConfig.COMMON.MAN_RADIUS.get());
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be == null) return BombReturnCode.UNDEFINED;
            if (be instanceof NukeBaseBlockEntity nuke) {
                if (nuke.isReady()) {
                    nuke.slots.clear();
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    NukeExplosionMK5.statFac(level, this.size, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    NukeTorex.statFacStandard(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, this.size);
                    return BombReturnCode.DETONATED;
                }
            }
            return BombReturnCode.ERROR_MISSING_COMPONENT;
        }

        return BombReturnCode.UNDEFINED;
    }
}
