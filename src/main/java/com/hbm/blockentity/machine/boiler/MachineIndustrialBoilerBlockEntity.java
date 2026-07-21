package com.hbm.blockentity.machine.boiler;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class MachineIndustrialBoilerBlockEntity extends AbstractBoilerBlockEntity {

    public MachineIndustrialBoilerBlockEntity(BlockPos pos, BlockState state) {
        super((net.minecraft.world.level.block.entity.BlockEntityType<? extends AbstractBoilerBlockEntity>) NtmBlockEntityTypes.MACHINE_INDUSTRIAL_BOILER.get(), pos, state, 64_000, 6_400_000);
    }

    @Override
    protected int getInputCapacity() {
        return 64_000;
    }

    @Override
    protected int getOutputCapacity() {
        return 6_400_000;
    }

    @Override
    protected DirPos[] getConPos() {
        BlockPos pos = this.getBlockPos();
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = facing.getClockWise();

        return new DirPos[] {
                new DirPos(pos.getX() + facing.getStepX() * 2, pos.getY(), pos.getZ() + facing.getStepZ() * 2, facing),
                new DirPos(pos.getX() - facing.getStepX() * 2, pos.getY(), pos.getZ() - facing.getStepZ() * 2, facing.getOpposite()),
                new DirPos(pos.getX() + rot.getStepX() * 2, pos.getY(), pos.getZ() + rot.getStepZ() * 2, rot),
                new DirPos(pos.getX() - rot.getStepX() * 2, pos.getY(), pos.getZ() - rot.getStepZ() * 2, rot.getOpposite()),
                new DirPos(pos.getX(), pos.getY() + 5, pos.getZ(), Direction.UP)
        };
    }

    @Override
    protected int getRenderHeight() {
        return 5;
    }

    @Override
    protected boolean canExplode() {
        return false;
    }
}
