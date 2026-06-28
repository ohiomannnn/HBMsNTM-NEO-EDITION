package com.hbm.blocks.bomb;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.bomb.LaunchPadLargeBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.interfaces.IBomb;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public class LaunchPadLargeBlock extends DummyableBlock implements IBomb {

    public LaunchPadLargeBlock(Properties properties) {
        super(properties);
        this.bounding.add(new AABB(-4.5D, 0D, -4.5D, 4.5D, 1D, -0.5D));
        this.bounding.add(new AABB(-4.5D, 0D, 0.5D, 4.5D, 1D, 4.5D));
        this.bounding.add(new AABB(-4.5D, 0.875D, -0.5D, 4.5D, 1D, 0.5D));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch (type) {
            case CORE -> new LaunchPadLargeBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().power().fluid();
            default -> new ProxyComboBlockEntity(pos, state).inventory();
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<LaunchPadLargeBlock> CODEC = simpleCodec(LaunchPadLargeBlock::new);
    @Override protected MapCodec<LaunchPadLargeBlock> codec() { return CODEC; }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        if(!level.isClientSide) {
            BlockPos corePos = this.findCore(level, pos);
            if(corePos != null) {
                BlockEntity core = level.getBlockEntity(corePos);
                if(core instanceof LaunchPadLargeBlockEntity launchPad) {
                    launchPad.updateRedstonePower(pos);
                }
            }
        }
    }

    @Override public int[] getDimensions() { return new int[] { 0, 0, 4, 4, 4, 4 }; }
    @Override public int getOffset() { return 4; }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        BlockPos offsetPos = pos.offset(dir.getStepX() * offset, 0, dir.getStepZ() * offset);

        this.makeExtra(level, offsetPos.offset(+4, 0, +2));
        this.makeExtra(level, offsetPos.offset(+4, 0, -2));
        this.makeExtra(level, offsetPos.offset(-4, 0, +2));
        this.makeExtra(level, offsetPos.offset(-4, 0, -2));
        this.makeExtra(level, offsetPos.offset(+2, 0, +4));
        this.makeExtra(level, offsetPos.offset(-2, 0, +4));
        this.makeExtra(level, offsetPos.offset(+2, 0, -4));
        this.makeExtra(level, offsetPos.offset(-2, 0, -4));
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos) {

        if(!level.isClientSide) {
            BlockPos corePos = this.findCore(level, pos);
            if(corePos != null) {
                BlockEntity core = level.getBlockEntity(corePos);
                if(core instanceof LaunchPadLargeBlockEntity launchPad) {
                    return launchPad.launchFromDesignator();
                }
            }
        }

        return BombReturnCode.UNDEFINED;
    }
}
