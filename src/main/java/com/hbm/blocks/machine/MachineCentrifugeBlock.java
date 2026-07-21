package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineCentrifugeBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public class MachineCentrifugeBlock extends DummyableBlock {

    public static final MapCodec<MachineCentrifugeBlock> CODEC = simpleCodec(MachineCentrifugeBlock::new);

    public MachineCentrifugeBlock(Properties properties) {
        super(properties);
        this.bounding.add(new AABB(-0.5D, 0.0D, -0.5D, 0.5D, 1.0D, 0.5D));
        this.bounding.add(new AABB(-0.375D, 1.0D, -0.375D, 0.375D, 4.0D, 0.375D));
    }

    @Override
    public MapCodec<MachineCentrifugeBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineCentrifugeBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().power();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> {
            if(be instanceof ITickable tickable) tickable.updateEntity();
        };
    }

    @Override
    public int[] getDimensions() {
        return new int[] { 3, 0, 0, 0, 0, 0 };
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        this.makeExtra(level, pos.above(1));
        this.makeExtra(level, pos.above(2));
        this.makeExtra(level, pos.above(3));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }
}
