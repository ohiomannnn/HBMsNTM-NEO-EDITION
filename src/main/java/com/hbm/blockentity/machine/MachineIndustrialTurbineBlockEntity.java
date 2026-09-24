package com.hbm.blockentity.machine;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Coolable;
import com.hbm.inventory.fluid.trait.FT_Coolable.CoolingType;
import com.hbm.main.NuclearTechMod;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MachineIndustrialTurbineBlockEntity extends TurbineBaseBlockEntity {

    //Configurable values todo
    public static int inputTankSize = 750_000;
    public static int outputTankSize = 3_000_000;
    public static double efficiency = 1.0;

    public float rotor;
    public float lastRotor;

    public double spin = 0;
    public static double FLYWHEEL_MAX_ENERGY = 0.5e8; //aka flywheel mass
    public long maxPower = 0;
    public long lastPowerTarget = 0;
    public long flywheel_energy = 0;

    private AudioWrapper audio;
    private final float audioDesync;

    public MachineIndustrialTurbineBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_INDUSTRIAL_TURBINE.get(), pos, state);

        tanks = new FluidTank[2];
        tanks[0] = new FluidTank(Fluids.STEAM, inputTankSize);
        tanks[1] = new FluidTank(Fluids.SPENTSTEAM, outputTankSize);

        RandomSource rand = RandomSource.create();
        audioDesync = rand.nextFloat() * 0.05F;
    }

    @Override
    public void generatePower(long power, int steamConsumed) {
        FT_Coolable trait = tanks[0].getTankType().getTrait(FT_Coolable.class);
        double eff = trait.getEfficiency(CoolingType.TURBINE) * getEfficiency();
        int maxOps = (int) Math.ceil((tanks[0].getMaxFill() * consumptionPercent()) / trait.amountReq);
        this.maxPower = (long) (maxOps * trait.heatEnergy * eff);

        this.flywheel_energy += power;
    }

    @Override
    public void onServerTick() {
        this.spin = (double) flywheel_energy / FLYWHEEL_MAX_ENERGY; //because dense steams have way lower energy output, turbines running them take a lot longer to spool up
        this.lastPowerTarget = Math.min((long) (Math.max(this.spin, 0.05) * maxPower), this.flywheel_energy);
        this.flywheel_energy -= this.lastPowerTarget;
        this.powerBuffer = this.lastPowerTarget;
    }

    @Override
    public void onClientTick() {
        this.lastRotor = this.rotor;
        float speed = this.spin >= 0.5 ? 30 : (float) (Math.pow(this.spin * 2, 0.5) * 30);
        this.rotor += speed;

        if(this.rotor >= 360) {
            this.lastRotor -= 360;
            this.rotor -= 360;
        }

        if(this.spin > 0 && NuclearTechMod.proxy.me().distanceToSqr(Vec3.atCenterOf(this.getBlockPos())) <= 35) {

            float spinNum = (float) Math.min(1F, spin * 2);
            float volume = this.getVolume(0.25F + spinNum * 0.75F);
            float pitch = 0.5F + spinNum * 0.5F + this.audioDesync;

            if(audio == null) {
                audio = AudioWrapper.getLoopedSound(NtmSoundEvents.TURBINE_LEVI_LOOP.get(), SoundSource.BLOCKS, this, volume, 20F, pitch, 20);
                audio.startSound();
            }

            audio.keepAlive();
            audio.updatePitch(pitch);
            audio.updateVolume(volume);

        } else {
            if(audio != null) {
                audio.stopSound();
                audio = null;
            }
        }
    }

    @Override
    public boolean canConnect(Direction dir) {
        Direction myDir = this.getBlockState().getValue(DummyableBlock.FACING);
        return dir == myDir.getOpposite();
    }

    @Override
    public boolean canConnect(FluidType type, Direction dir) {
        if(!type.hasTrait(FT_Coolable.class) && type != Fluids.SPENTSTEAM) return false;
        Direction myDir = this.getBlockState().getValue(DummyableBlock.FACING);
        return dir == myDir.getOpposite();
    }

    @Override public double consumptionPercent() { return 0.2D; }
    @Override public double getEfficiency() { return efficiency; }
    @Override public boolean doesResizeCompressor() { return true; }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();

        if(audio != null) {
            audio.stopSound();
            audio = null;
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if(audio != null) {
            audio.stopSound();
            audio = null;
        }
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeDouble(this.spin);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.spin = buf.readDouble();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        lastPowerTarget = tag.getLong("lastPowerTarget");
        flywheel_energy = tag.getLong("flywheel_energy");
        maxPower = tag.getLong("maxPower");
        spin = tag.getDouble("spin");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("lastPowerTarget", lastPowerTarget);
        tag.putLong("flywheel_energy", flywheel_energy);
        tag.putLong("maxPower", maxPower);
        tag.putDouble("spin", spin);
    }

    @Override
    public DirPos[] getConPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise(Axis.Y);
        return new DirPos[] {
                new DirPos(this.getBlockPos().offset(dir.getStepX() * 3 + rot.getStepX() * 2, 0, dir.getStepZ() * 3 + rot.getStepZ() * 2), dir),
                new DirPos(this.getBlockPos().offset(dir.getStepX() * 3 - rot.getStepX() * 2, 0, dir.getStepZ() * 3 - rot.getStepZ() * 2), dir.getOpposite()),
                new DirPos(this.getBlockPos().offset(-dir.getStepX() * 1 + rot.getStepX() * 2, 0, -dir.getStepZ() * 1 + rot.getStepZ() * 2), dir),
                new DirPos(this.getBlockPos().offset(-dir.getStepX() * 1 - rot.getStepX() * 2, 0, -dir.getStepZ() * 1 - rot.getStepZ() * 2), dir.getOpposite()),
                new DirPos(this.getBlockPos().offset(dir.getStepX() * 3, 3, dir.getStepZ() * 3), Direction.UP),
                new DirPos(this.getBlockPos().offset(-dir.getStepX() * 1, 3, -dir.getStepZ() * 1), Direction.UP)
        };
    }

    @Override
    public DirPos[] getPowerPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        return new DirPos[] {
                new DirPos(this.getBlockPos().offset(-dir.getStepX() * 4, 1, -dir.getStepZ() * 4), dir.getOpposite())
        };
    }
}
