package com.hbm.blocks.bomb;

import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.Tickable;
import com.hbm.blockentity.bomb.LaunchPadBlockEntity;
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

public class LaunchPadBlock extends DummyableBlock implements IBomb {

    public LaunchPadBlock(Properties properties) {
        super(properties);
        this.bounding.add(new AABB(-1.5D, 0D, -1.5D, -0.5D, 1D, -0.5D));
        this.bounding.add(new AABB(0.5D, 0D, -1.5D, 1.5D, 1D, -0.5D));
        this.bounding.add(new AABB(-1.5D, 0D, 0.5D, -0.5D, 1D, 1.5D));
        this.bounding.add(new AABB(0.5D, 0D, 0.5D, 1.5D, 1D, 1.5D));
        this.bounding.add(new AABB(-0.5D, 0.5D, -1.5D, 0.5D, 1D, 1.5D));
        this.bounding.add(new AABB(-1.5D, 0.5D, -0.5D, 1.5D, 1D, 0.5D));
    }

    public static final MapCodec<LaunchPadBlock> CODEC = simpleCodec(LaunchPadBlock::new);
    @Override protected MapCodec<LaunchPadBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch (type) {
            case CORE -> new LaunchPadBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().power().fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> {
            if (be instanceof Tickable tickable) tickable.updateEntity();
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player, 0);
    }

    @Override public int[] getDimensions() { return new int[] {0, 0, 1, 1, 1, 1}; }
    @Override public int getOffset() { return 1; }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos) {

        if (!level.isClientSide) {
            BlockPos corePos = this.findCore(level, pos);
            if (corePos != null) {
                BlockEntity core = level.getBlockEntity(corePos);
                if (core instanceof LaunchPadBlockEntity launchPad) {
                    return launchPad.launchFromDesignator();
                }
            }
        }

        return BombReturnCode.UNDEFINED;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if (!level.isClientSide) {
            BlockPos corePos = this.findCore(level, pos);
            if (corePos != null) {
                BlockEntity core = level.getBlockEntity(corePos);
                if (core instanceof LaunchPadBlockEntity launchPad) {
                    launchPad.updateRedstonePower(pos);
                }
            }
        }
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        x += dir.getStepX() * offset;
        z += dir.getStepZ() * offset;

        this.makeExtra(level, new BlockPos(x + 1, y, z + 1));
        this.makeExtra(level, new BlockPos(x + 1, y, z - 1));
        this.makeExtra(level, new BlockPos(x - 1, y, z + 1));
        this.makeExtra(level, new BlockPos(x - 1, y, z - 1));
    }
}
