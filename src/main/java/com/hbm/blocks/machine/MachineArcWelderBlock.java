package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineArcWelderBlockEntity;
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
import net.minecraft.world.phys.BlockHitResult;

public class MachineArcWelderBlock extends DummyableBlock {

    public static final MapCodec<MachineArcWelderBlock> CODEC = simpleCodec(MachineArcWelderBlock::new);

    public MachineArcWelderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<MachineArcWelderBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineArcWelderBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().power().fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    @Override
    public int[] getDimensions() {
        return new int[] { 1, 0, 1, 0, 1, 1 };
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        Direction side = dir.getClockWise();
        BlockPos core = pos.relative(dir, offset);
        this.makeExtra(level, core.relative(side));
        this.makeExtra(level, core.relative(dir.getOpposite()));
        this.makeExtra(level, core.relative(dir.getOpposite()).relative(side));
        this.makeExtra(level, core.above());
        this.makeExtra(level, core.relative(side).above());
        this.makeExtra(level, core.relative(dir.getOpposite()).above());
        this.makeExtra(level, core.relative(dir.getOpposite()).relative(side).above());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }
}
