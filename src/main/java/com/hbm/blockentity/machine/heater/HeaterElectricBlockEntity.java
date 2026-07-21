package com.hbm.blockentity.machine.heater;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.tile.IHeatSource;
import com.hbm.blockentity.IBufPacketReceiver;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.interfaces.ICopiable;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.util.fauxpointtwelve.DirPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class HeaterElectricBlockEntity extends LoadedBaseBlockEntity implements IHeatSource, IEnergyReceiverMK2, com.hbm.blockentity.ITickable, IBufPacketReceiver, ICopiable, IControlReceiver {

    public long power;
    public int heatEnergy;
    public boolean isOn;
    public int setting = 1;

    private AABB bb;

    public HeaterElectricBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.HEATER_ELECTRIC.get(), pos, state);
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            if(this.level.getGameTime() % 20 == 0) {
                Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
                this.trySubscribe(this.level, this.getBlockPos().relative(facing, 3), facing);
            }

            this.heatEnergy = (int) (this.heatEnergy * 0.999D);
            this.tryPullHeat();

            this.isOn = false;
            if(this.setting > 0 && this.power >= this.getConsumption()) {
                this.power -= this.getConsumption();
                this.heatEnergy += this.getHeatGen();
                this.isOn = true;
            }

            this.networkPackNT(25);
        }
    }

    protected void tryPullHeat() {
        if(this.level == null) return;
        var con = this.level.getBlockEntity(this.getBlockPos().below());
        if(con instanceof IHeatSource source) {
            this.heatEnergy += (int) (source.getHeatStored() * 0.85D);
            source.useUpHeat(source.getHeatStored());
        }
    }

    public void toggleSettingUp() {
        this.setting++;
        if(this.setting > 10) this.setting = 0;
        this.setChanged();
    }

    public void toggleSettingDown() {
        this.setting--;
        if(this.setting < 0) this.setting = 10;
        this.setChanged();
    }

    public long getConsumption() {
        return (long) (Math.pow(this.setting, 1.4D) * 200D);
    }

    public int getHeatGen() {
        return this.setting * 100;
    }

    @Override
    public long getPower() {
        return this.power;
    }

    @Override
    public long getMaxPower() {
        return this.getConsumption() * 20L;
    }

    @Override
    public void setPower(long power) {
        this.power = power;
    }

    @Override
    public int getHeatStored() {
        return this.heatEnergy;
    }

    @Override
    public void useUpHeat(int heat) {
        this.heatEnergy = Math.max(0, this.heatEnergy - heat);
        this.setChanged();
    }

    @Override
    public boolean canConnect(Direction dir) {
        return dir != null && dir.getAxis().isHorizontal();
    }

    @Override
    public long transferPower(long power) {
        if(power + this.power <= this.getMaxPower()) {
            this.power += power;
            this.setChanged();
            return 0;
        }

        long capacity = this.getMaxPower() - this.power;
        long overshoot = power - capacity;
        this.power = this.getMaxPower();
        this.setChanged();
        return overshoot;
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.distanceToSqr(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5) <= 256.0D;
    }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("setting")) {
            this.setting = Math.max(0, Math.min(tag.getInt("setting"), 10));
        }
        this.setChanged();
    }

    @Override
    public void receiveControl(Player player, CompoundTag tag) {
        this.receiveControl(tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.heatEnergy = tag.getInt("heatEnergy");
        this.isOn = tag.getBoolean("isOn");
        this.setting = tag.getInt("setting");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putInt("heatEnergy", this.heatEnergy);
        tag.putBoolean("isOn", this.isOn);
        tag.putInt("setting", this.setting);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeInt(this.heatEnergy);
        buf.writeBoolean(this.isOn);
        buf.writeByte(this.setting);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.heatEnergy = buf.readInt();
        this.isOn = buf.readBoolean();
        this.setting = buf.readByte();
    }

    @Override
    public CompoundTag getSettings(net.minecraft.world.level.Level level, BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("setting", this.setting);
        return tag;
    }

    @Override
    public void pasteSettings(CompoundTag tag, int index, net.minecraft.world.level.Level level, Player player, BlockPos pos) {
        if(tag.contains("setting")) {
            this.setting = tag.getInt("setting");
        }
    }

    public AABB getRenderBoundingBox() {
        if(this.bb == null) {
            this.bb = new AABB(this.getBlockPos().getX() - 1, this.getBlockPos().getY(), this.getBlockPos().getZ() - 1, this.getBlockPos().getX() + 2, this.getBlockPos().getY() + 2, this.getBlockPos().getZ() + 2);
        }
        return this.bb;
    }

    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }
}
