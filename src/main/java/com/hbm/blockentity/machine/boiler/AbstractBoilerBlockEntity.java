package com.hbm.blockentity.machine.boiler;

import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import api.hbm.fluidmk2.IFluidConnectorMK2;
import api.hbm.tile.IHeatSource;
import com.hbm.blockentity.IOverpressurable;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Heatable;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractBoilerBlockEntity extends LoadedBaseBlockEntity implements ITickable, IFluidStandardTransceiverMK2, IFluidConnectorMK2, IOverpressurable, IHeatSource, IPersistentNBT {

    protected static final int MAX_HEAT = 12_800_000;
    protected static final double DIFFUSION = 0.1D;

    public final FluidTank[] tanks;
    public int heat;
    public boolean isOn;
    public boolean hasExploded;

    private AudioWrapper audio;
    private int audioTime;

    protected AbstractBoilerBlockEntity(BlockEntityType<? extends AbstractBoilerBlockEntity> type, BlockPos pos, BlockState state, int inputCapacity, int outputCapacity) {
        super(type, pos, state);

        this.tanks = new FluidTank[] {
                new FluidTank(Fluids.WATER, inputCapacity),
                new FluidTank(Fluids.STEAM, outputCapacity)
        };
    }

    protected abstract int getInputCapacity();
    protected abstract int getOutputCapacity();
    protected abstract DirPos[] getConPos();
    protected abstract int getRenderHeight();
    protected abstract boolean canExplode();

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            if(!this.hasExploded) {
                this.setupTanks();
                this.updateConnections();
                this.tryPullHeat();

                this.isOn = false;
                this.tryConvert();

                if(this.tanks[1].getFill() > 0) {
                    for(DirPos pos : this.getConPos()) {
                        this.tryProvide(this.tanks[1], this.level, pos);
                    }
                }
            }

            this.networkPackNT(25);
        } else {
            if(this.isOn) {
                this.audioTime = 20;
            }

            if(this.audioTime > 0) {
                this.audioTime--;

                if(this.audio == null) {
                    this.audio = this.createAudioLoop();
                    if(this.audio != null) {
                        this.audio.startSound();
                    }
                } else if(!this.audio.isPlaying()) {
                    this.audio = this.rebootAudio(this.audio);
                }

                if(this.audio != null) {
                    this.audio.updateVolume(this.getVolume(0.125F));
                    this.audio.keepAlive();
                }
            } else if(this.audio != null) {
                this.audio.stopSound();
                this.audio = null;
            }
        }
    }

    protected void setupTanks() {
        if(this.tanks[0].getTankType().hasTrait(FT_Heatable.class)) {
            FT_Heatable trait = this.tanks[0].getTankType().getTrait(FT_Heatable.class);
            if(trait.getEfficiency(FT_Heatable.HeatingType.BOILER) > 0) {
                FT_Heatable.HeatingStep entry = trait.getFirstStep();
                this.tanks[1].setTankType(entry.typeProduced);
                this.tanks[1].changeTankSize(this.getInputCapacity() * entry.amountProduced / entry.amountReq);
                return;
            }
        }

        this.tanks[0].setTankType(Fluids.NONE);
        this.tanks[1].setTankType(Fluids.NONE);
    }

    protected void tryConvert() {
        if(!this.tanks[0].getTankType().hasTrait(FT_Heatable.class)) return;

        FT_Heatable trait = this.tanks[0].getTankType().getTrait(FT_Heatable.class);
        if(trait.getEfficiency(FT_Heatable.HeatingType.BOILER) <= 0) return;

        FT_Heatable.HeatingStep entry = trait.getFirstStep();
        int heatReq = (int)Math.max(entry.heatReq / trait.getEfficiency(FT_Heatable.HeatingType.BOILER), 1);
        int inputOps = this.tanks[0].getFill() / entry.amountReq;
        int outputOps = (this.tanks[1].getMaxFill() - this.tanks[1].getFill()) / entry.amountProduced;
        int heatOps = this.heat / heatReq;

        int ops = Math.min(inputOps, Math.min(outputOps, heatOps));

        if(ops > 0) {
            this.tanks[0].setFill(this.tanks[0].getFill() - entry.amountReq * ops);
            this.tanks[1].setFill(this.tanks[1].getFill() + entry.amountProduced * ops);
            this.heat -= heatReq * ops;
            this.isOn = true;

            if(this.level != null && this.level.random.nextInt(400) == 0) {
                this.level.playSound(null, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 2, this.getBlockPos().getZ() + 0.5, NtmSoundEvents.BOILER_GROAN.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
            }
        } else if(outputOps == 0 && this.canExplode()) {
            this.explode(this.level, this.getBlockPos());
        }
    }

    @Override
    public void explode(Level level, BlockPos pos) {
        if(this.hasExploded) return;
        this.hasExploded = true;
        this.isOn = false;
        this.heat = 0;
        this.setChanged();
    }

    @Override
    public int getHeatStored() {
        return this.heat;
    }

    @Override
    public void useUpHeat(int heat) {
        if(heat <= 0) return;
        this.heat = Math.max(this.heat - heat, 0);
        this.setChanged();
    }

    protected void tryPullHeat() {
        if(this.level == null) return;
        if(this.heat >= MAX_HEAT) return;

        BlockPos blockBelow = this.getBlockPos().below();
        BlockEntity con = this.level.getBlockEntity(blockBelow);

        if(con instanceof IHeatSource source) {
            int diff = source.getHeatStored() - this.heat;

            if(diff > 0) {
                diff = (int)Math.ceil(diff * DIFFUSION);
                diff = Math.min(diff, MAX_HEAT - this.heat);
                source.useUpHeat(diff);
                this.heat += diff;
                if(this.heat > MAX_HEAT) {
                    this.heat = MAX_HEAT;
                }
                return;
            }
        }

        this.heat = Math.max(this.heat - Math.max(this.heat / 1000, 1), 0);
    }

    protected void updateConnections() {
        for(DirPos pos : this.getConPos()) {
            this.trySubscribe(this.tanks[0].getTankType(), this.level, pos);
        }
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { this.tanks[1] };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { this.tanks[0] };
    }

    @Override
    public FluidTank[] getAllTanks() {
        return this.tanks;
    }

    public boolean canConnect(Direction dir) {
        return dir != null && dir.getAxis().isHorizontal();
    }

    @Override
    public boolean canConnect(FluidType type, Direction dir) {
        return dir != null && dir.getAxis().isHorizontal();
    }

    @Override
    public void writeNBT(CompoundTag savedTag) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("heat", this.heat);
        tag.putBoolean("isOn", this.isOn);
        tag.putBoolean("hasExploded", this.hasExploded);
        this.tanks[0].writeToNBT(tag, "input");
        this.tanks[1].writeToNBT(tag, "output");
        savedTag.put(NBT_PERSISTENT_KEY, tag);
    }

    @Override
    public void readNBT(CompoundTag savedTag) {
        CompoundTag tag = savedTag.getCompound(NBT_PERSISTENT_KEY);
        this.heat = tag.getInt("heat");
        this.isOn = tag.getBoolean("isOn");
        this.hasExploded = tag.getBoolean("hasExploded");
        this.tanks[0].readFromNBT(tag, "input");
        this.tanks[1].readFromNBT(tag, "output");
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.heat = tag.getInt("heat");
        this.isOn = tag.getBoolean("isOn");
        this.hasExploded = tag.getBoolean("hasExploded");
        this.tanks[0].readFromNBT(tag, "input");
        this.tanks[1].readFromNBT(tag, "output");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("heat", this.heat);
        tag.putBoolean("isOn", this.isOn);
        tag.putBoolean("hasExploded", this.hasExploded);
        this.tanks[0].writeToNBT(tag, "input");
        this.tanks[1].writeToNBT(tag, "output");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.heat);
        buf.writeBoolean(this.isOn);
        buf.writeBoolean(this.hasExploded);
        this.tanks[0].serialize(buf);
        this.tanks[1].serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.heat = buf.readInt();
        this.isOn = buf.readBoolean();
        this.hasExploded = buf.readBoolean();
        this.tanks[0].deserialize(buf);
        this.tanks[1].deserialize(buf);
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.BOILER.get(), SoundSource.BLOCKS, this, 0.125F, 10F, 1.0F, 20);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if(this.audio != null) {
            this.audio.stopSound();
            this.audio = null;
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(this.audio != null) {
            this.audio.stopSound();
            this.audio = null;
        }
    }

    public AABB getRenderBoundingBox() {
        return new AABB(
                this.getBlockPos().getX() - 1,
                this.getBlockPos().getY(),
                this.getBlockPos().getZ() - 1,
                this.getBlockPos().getX() + 2,
                this.getBlockPos().getY() + this.getRenderHeight(),
                this.getBlockPos().getZ() + 2
        );
    }

    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }
}
