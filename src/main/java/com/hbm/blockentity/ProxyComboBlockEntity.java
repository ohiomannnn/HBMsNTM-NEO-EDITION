package com.hbm.blockentity;

import api.hbm.energymk2.IEnergyConductorMK2;
import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.redstoneoverradio.IRORInfo;
import api.hbm.redstoneoverradio.IRORInteractive;
import api.hbm.redstoneoverradio.IRORValueProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ProxyComboBlockEntity extends ProxyBaseBlockEntity implements IEnergyReceiverMK2, IEnergyConductorMK2, IRORValueProvider, IRORInteractive {

    private BlockEntity be;
    private boolean inventory;
    private boolean power;
    private boolean conductor;
    private boolean fluid;
    private boolean heat;
    public boolean moltenMetal;

    public ProxyComboBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PROXY_COMBO.get(), pos, blockState);
    }

    public ProxyComboBlockEntity inventory() {
        this.inventory = true;
        return this;
    }

    public ProxyComboBlockEntity power() {
        this.power = true;
        return this;
    }
    public ProxyComboBlockEntity conductor() {
        this.conductor = true;
        return this;
    }
    public ProxyComboBlockEntity moltenMetal() {
        this.moltenMetal = true;
        return this;
    }
    public ProxyComboBlockEntity fluid() {
        this.fluid = true;
        return this;
    }

    public ProxyComboBlockEntity heatSource() {
        this.heat = true;
        return this;
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
        return this.getBlockEntity();
    }

    @Override
    public void setPower(long i) {
        if (!this.power) return;

        if (getCoreObject() instanceof IEnergyReceiverMK2 rec) {
            rec.setPower(i);
        }
    }

    @Override
    public long getPower() {
        if (!this.power) return 0;

        if (this.getCoreObject() instanceof IEnergyReceiverMK2 rec) {
            return rec.getPower();
        }

        return 0;
    }

    @Override
    public long getMaxPower() {
        if (!this.power) return 0;

        if (this.getCoreObject() instanceof IEnergyReceiverMK2 rec) {
            return rec.getMaxPower();
        }

        return 0;
    }

    @Override
    public long transferPower(long power) {
        if (!this.power) return power;

        if (this.getCoreObject() instanceof IEnergyReceiverMK2 rec) {
            return rec.transferPower(power);
        }

        return power;
    }

    @Override
    public boolean canConnect(Direction dir) {

        if (this.power && this.getCoreObject() instanceof IEnergyReceiverMK2 rec) {
            return rec.canConnect(dir);
        }

        if (this.conductor && this.getCoreObject() instanceof IEnergyConductorMK2 con) {
            return con.canConnect(dir);
        }

        return true;
    }

    @Override
    public String[] getFunctionInfo() {
        if (this.getCoreObject() instanceof IRORInfo info) return info.getFunctionInfo();
        return new String[0];
    }

    @Override
    public String provideRORValue(String name) {
        if (this.getCoreObject() instanceof IRORValueProvider provider) return provider.provideRORValue(name);
        return null;
    }

    @Override
    public String runRORFunction(String name, String[] params) {
        if (this.getCoreObject() instanceof IRORInteractive interactive) return interactive.runRORFunction(name, params);
        return null;
    }
}
