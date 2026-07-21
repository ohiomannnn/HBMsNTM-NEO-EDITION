package com.hbm.blockentity.machine.boiler;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class MachineHeatBoilerBlockEntity extends AbstractBoilerBlockEntity {

    public MachineHeatBoilerBlockEntity(BlockPos pos, BlockState state) {
        super((net.minecraft.world.level.block.entity.BlockEntityType<? extends AbstractBoilerBlockEntity>) NtmBlockEntityTypes.HEAT_BOILER.get(), pos, state, 16_000, 1_600_000);
    }

    @Override
    protected int getInputCapacity() {
        return 16_000;
    }

    @Override
    protected int getOutputCapacity() {
        return 1_600_000;
    }

    @Override
    protected DirPos[] getConPos() {
        BlockPos pos = this.getBlockPos();
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        return new DirPos[] {
                new DirPos(pos.getX() + facing.getStepX() * 2, pos.getY(), pos.getZ() + facing.getStepZ() * 2, facing),
                new DirPos(pos.getX() - facing.getStepX() * 2, pos.getY(), pos.getZ() - facing.getStepZ() * 2, facing.getOpposite()),
                new DirPos(pos.getX(), pos.getY() + 4, pos.getZ(), Direction.UP)
        };
    }

    @Override
    protected int getRenderHeight() {
        return 4;
    }

    @Override
    protected boolean canExplode() {
        return true;
    }
}
