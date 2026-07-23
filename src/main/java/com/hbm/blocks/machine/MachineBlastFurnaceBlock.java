package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineBlastFurnaceBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class MachineBlastFurnaceBlock extends DummyableBlock {

    public MachineBlastFurnaceBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<MachineBlastFurnaceBlock> CODEC = simpleCodec(MachineBlastFurnaceBlock::new);
    @Override public MapCodec<MachineBlastFurnaceBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineBlastFurnaceBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    @Override public int[] getDimensions() { return new int[] { 6, 0, 1, 1, 1, 1 }; }
    @Override public int getOffset() { return 1; }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        BlockPos center = pos.relative(dir.getOpposite());
        this.makeExtra(level, center.east());
        this.makeExtra(level, center.west());
        this.makeExtra(level, center.south());
        this.makeExtra(level, center.north());
        this.makeExtra(level, center.relative(dir, 1).above(3));
        this.makeExtra(level, center.relative(dir, 1).above(5));
        this.makeExtra(level, center.above(6));
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockState result = super.playerWillDestroy(level, pos, state, player);
        if(!level.isClientSide && !safeRem) {
            this.destroyBlastFurnace(level, pos);
        }

        return result;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!level.isClientSide && state.getBlock() == this && !state.is(newState.getBlock()) && !safeRem) {
            this.destroyBlastFurnace(level, pos);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    private void destroyBlastFurnace(Level level, BlockPos pos) {
        BlockPos center = this.findCore(level, pos);
        if(center == null) {
            center = pos;
        }

        if(level.getBlockState(center).getBlock() != this) {
            return;
        }

        safeRem = true;
        try {
            this.clearArea(level, center);
        } finally {
            safeRem = false;
        }
    }

    private void clearArea(Level level, BlockPos center) {
        for(int dx = -2; dx <= 2; dx++) {
            for(int dy = -1; dy <= 7; dy++) {
                for(int dz = -2; dz <= 2; dz++) {
                    BlockPos target = center.offset(dx, dy, dz);
                    if(level.getBlockState(target).getBlock() == this) {
                        level.removeBlock(target, false);
                    }
                }
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }
}
