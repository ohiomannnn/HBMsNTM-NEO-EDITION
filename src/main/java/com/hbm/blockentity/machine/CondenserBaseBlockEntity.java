package com.hbm.blockentity.machine;

import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.inventory.fluid.tank.FluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CondenserBaseBlockEntity extends LoadedBaseBlockEntity implements ITickable, IFluidStandardTransceiverMK2, IFluidCopiable {

    public int age = 0;
    public FluidTank[] tanks;

    public int waterTimer = 0;
    protected int throughput;

    // todo make Configurable values
    public static int inputTankSize = 100;
    public static int outputTankSize = 100;

    public CondenserBaseBlockEntity(BlockEntityType<? extends CondenserBaseBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {

            if(this.age++ >= 2) this.age = 0;

            if(this.waterTimer > 0) this.waterTimer--;

            int convert = Math.min(tanks[0].getFill(), tanks[1].getMaxFill() - tanks[1].getFill());
            this.throughput = convert;

            if(this.extraCondition(convert)) {
                tanks[0].setFill(tanks[0].getFill() - convert);

                if(convert > 0) this.waterTimer = 20;

                tanks[1].setFill(tanks[1].getFill() + convert);
                this.postConvert(convert);
            }

            for(Direction dir : Direction.values()) this.trySubscribe(tanks[0].getTankType(), this.level, this.getBlockPos(), dir);
            for(Direction dir : Direction.values()) this.tryProvide(tanks[1].getTankType(), this.level, this.getBlockPos(), dir);

            this.networkPackNT(150);
        }
    }

    public void packExtra(CompoundTag tag) { }
    public boolean extraCondition(int convert) { return true; }
    public void postConvert(int convert) { }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        this.tanks[0].serialize(buf);
        this.tanks[1].serialize(buf);
        buf.writeByte(this.waterTimer);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        this.tanks[0].deserialize(buf);
        this.tanks[1].deserialize(buf);
        this.waterTimer = buf.readByte();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        tanks[0].readFromNBT(tag, "water");
        tanks[1].readFromNBT(tag, "steam");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tanks[0].writeToNBT(tag, "water");
        tanks[1].writeToNBT(tag, "steam");
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { tanks[1] };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { tanks[0] };
    }

    @Override
    public FluidTank[] getAllTanks() {
        return tanks;
    }

    @Override
    public FluidTank getTankToPaste() {
        return null;
    }
}
