package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidReceiverMK2;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.lib.Library;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.SoundUtils;
import com.hbm.util.fauxpointtwelve.DirPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class SoyuzLauncherBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidReceiverMK2 {

    public long power;
    public static final long maxPower = 1000000;
    public FluidTank[] tanks;
    //0: sat, 1: cargo
    public byte mode;
    public boolean starting;
    public int countdown;
    public static final int maxCount = 600;
    public byte rocketType = -1;

    private AudioWrapper audio;

    public SoyuzLauncherBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.SOYUZ_LAUNCHER.get(), pos, state, 27);

        tanks = new FluidTank[2];
        tanks[0] = new FluidTank(Fluids.KEROSENE, 128_000);
        tanks[1] = new FluidTank(Fluids.OXYGEN, 128_000);
    }

    @Override protected Component getDefaultName() { return Component.translatable("container.soyuz_launcher"); }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {

            if(level.getGameTime() % 20 == 0) {
                for(DirPos pos : this.getConPos()) {
                    this.trySubscribe(level, pos);
                    this.trySubscribe(tanks[0].getTankType(), level, pos);
                    this.trySubscribe(tanks[1].getTankType(), level, pos);
                }
            }

            tanks[0].loadTank(level, 4, 5, slots);
            tanks[1].loadTank(level, 6, 7, slots);

            power = Library.chargeTEFromItems(slots, 8, power, maxPower);

//            if(!starting || !canLaunch()) {
//                countdown = maxCount;
//                starting = false;
//            } else if(countdown > 0) {
//                countdown--;
//
//                if(countdown % 100 == 0 && countdown > 0) SoundUtils.playAtBlockPos(level, this.getBlockPos(), NtmSoundEvents.ALARM_HATCH.get(), SoundSource.RECORDS, 100F, 1.1F);
//
//            } else {
//                liftOff();
//            }

            this.networkPackNT(250);
        }
    }

    protected List<DirPos> conPos;

    protected List<DirPos> getConPos() {

        if(conPos != null) return conPos;

        this.conPos = new ArrayList<>();

        for(Direction dir : new Direction[] {Library.POS_X, Library.POS_Z, Library.NEG_X, Library.NEG_Z}) {
            Direction rot = dir.getClockWise(Axis.Y);

            for(int i = -6; i <= 6; i++) {
                conPos.add(new DirPos(this.getBlockPos().relative(dir, 7).relative(rot, i), dir));
                conPos.add(new DirPos(this.getBlockPos().relative(dir, 7).relative(rot, i).offset(0, -1, 0), dir));
            }
        }

        return conPos;
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.SOYUZ_READY.get(), SoundSource.BLOCKS, this, 2.0F, 100F, 1.0F, 20);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();

        if(audio != null) { audio.stopSound(); audio = null; }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if(audio != null) { audio.stopSound(); audio = null; }
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(power);
        buf.writeByte(mode);
        buf.writeBoolean(starting);
        //buf.writeByte(this.getRocketType());
        tanks[0].serialize(buf);
        tanks[1].serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        power = buf.readLong();
        mode = buf.readByte();
        starting = buf.readBoolean();
        rocketType = buf.readByte();
        tanks[0].deserialize(buf);
        tanks[1].deserialize(buf);
    }

    public void startCountdown() {

//        if(canLaunch())
//            starting = true;
    }


    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return null;
    }

    @Override
    public FluidTank[] getAllTanks() {
        return tanks;
    }

    @Override
    public long getPower() {
        return 0;
    }

    @Override
    public void setPower(long power) {

    }

    @Override
    public long getMaxPower() {
        return 0;
    }

    @Override
    public long transferFluid(FluidType type, int pressure, long amount) {
        return 0;
    }

    @Override
    public long getDemand(FluidType type, int pressure) {
        return 0;
    }
}
