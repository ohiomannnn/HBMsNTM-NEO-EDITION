package com.hbm.blockentity;

import api.hbm.energymk2.IEnergyReceiverMK2;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ProxyComboBlockEntity extends ProxyBaseBlockEntity implements IEnergyReceiverMK2 {

    private BlockEntity be;
    private boolean power;

    public ProxyComboBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    /** Returns the actual tile entity that represents the core. Only for internal use, and EnergyControl. */
    public BlockEntity getBlockEntity() {
        if (be == null || be.isRemoved() || (be instanceof LoadedBaseBlockEntity && !((LoadedBaseBlockEntity) be).isLoaded)) {
            be = this.getBE();
        }
        return be;
    }

    /** Returns the core tile entity, or a delegate object. */
    protected Object getCoreObject() {
        return getBlockEntity();
    }

    @Override
    public void setPower(long i) {
        if (!power) return;

        if (getCoreObject() instanceof IEnergyReceiverMK2 rec) {
            rec.setPower(i);
        }
    }

    @Override
    public long getPower() {
        if (!power) return 0;

        if (getCoreObject() instanceof IEnergyReceiverMK2 rec) {
            return rec.getPower();
        }

        return 0;
    }

    @Override
    public long getMaxPower() {
        if (!power) return 0;

        if (getCoreObject() instanceof IEnergyReceiverMK2 rec) {
            return rec.getMaxPower();
        }

        return 0;
    }
}
