package com.hbm.blockentity.machine.oil;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IOverpressurable;
import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineRefineryMenu;
import com.hbm.inventory.recipes.RefineryRecipes;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.Tuple;
import com.hbm.util.fauxpointtwelve.DirPos;
import com.hbm.lib.Library;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MachineRefineryBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IPersistentNBT, IOverpressurable {

    public static final int MAX_SULFUR = 100;
    public static final long MAX_POWER = 1_000L;

    public long power;
    public int sulfur;
    public int itemOutputTimer;
    public boolean isOn;
    public final FluidTank[] tanks;
    public boolean hasExploded;
    public boolean onFire;

    private AudioWrapper audio;
    private int audioTime;

    public MachineRefineryBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_REFINERY.get(), pos, state, 13);

        RefineryRecipes.registerDefaults();

        this.tanks = new FluidTank[] {
                new FluidTank(Fluids.OIL_CRUDE_HOT, 64_000),
                new FluidTank(Fluids.OIL_HEAVY, 24_000),
                new FluidTank(Fluids.NAPHTHA, 24_000),
                new FluidTank(Fluids.OIL_LIGHT, 24_000),
                new FluidTank(Fluids.PETROLEUM_GAS, 24_000)
        };
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_refinery");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            this.checkTilt(LoadedBaseBlockEntity.TiltType.CONFIG, false);

            this.isOn = false;

            if(!this.hasExploded) {
                if(this.level.getGameTime() % 20 == 0) {
                    for(DirPos pos : this.getConPos()) {
                        this.trySubscribe(this.level, pos);
                        this.trySubscribe(this.tanks[0].getTankType(), this.level, pos);
                    }
                }

                this.power = Library.chargeTEFromItems(this.slots, 0, this.power, MAX_POWER);
                this.tanks[0].setType(12, this.slots);
                this.tanks[0].loadTank(this.level, 1, 2, this.slots);

                boolean refined = this.refine();

                this.tanks[1].unloadTank(this.level, 3, 4, this.slots);
                this.tanks[2].unloadTank(this.level, 5, 6, this.slots);
                this.tanks[3].unloadTank(this.level, 7, 8, this.slots);
                this.tanks[4].unloadTank(this.level, 9, 10, this.slots);

                for(DirPos pos : this.getConPos()) {
                    for(int i = 1; i < this.tanks.length; i++) {
                        if(this.tanks[i].getFill() > 0) {
                            this.tryProvide(this.tanks[i], this.level, pos);
                        }
                    }
                }

                if(refined) {
                    this.setChanged();
                }
            } else if(this.onFire) {
                boolean hasFuel = false;
                for(FluidTank tank : this.tanks) {
                    if(tank.getFill() > 0) {
                        tank.setFill(Math.max(tank.getFill() - 10, 0));
                        hasFuel = true;
                    }
                }

                if(hasFuel) {
                    this.setChanged();
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
                    this.audio.updateVolume(getVolume(1F));
                    this.audio.keepAlive();
                }
            } else if(this.audio != null) {
                this.audio.stopSound();
                this.audio = null;
            }
        }
    }

    private boolean refine() {
        Tuple.Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack> refinery = RefineryRecipes.getRefinery(this.tanks[0].getTankType());

        if(refinery == null) {
            boolean changed = false;
            for(int i = 1; i < this.tanks.length; i++) {
                if(this.tanks[i].getTankType() != Fluids.NONE) {
                    this.tanks[i].setTankType(Fluids.NONE);
                    changed = true;
                }
            }
            return changed;
        }

        FluidStack[] outputs = new FluidStack[] { refinery.getV(), refinery.getW(), refinery.getX(), refinery.getY() };

        for(int i = 0; i < outputs.length; i++) {
            this.tanks[i + 1].setTankType(outputs[i].type);
        }

        if(this.power < 5 || this.tanks[0].getFill() < 100) {
            return false;
        }

        for(int i = 0; i < outputs.length; i++) {
            if(this.tanks[i + 1].getFill() + outputs[i].fill > this.tanks[i + 1].getMaxFill()) {
                return false;
            }
        }

        this.isOn = true;
        this.tanks[0].setFill(this.tanks[0].getFill() - 100);
        for(int i = 0; i < outputs.length; i++) {
            this.tanks[i + 1].setFill(this.tanks[i + 1].getFill() + outputs[i].fill);
        }

        this.sulfur++;
        this.itemOutputTimer++;

        if(this.sulfur >= MAX_SULFUR) {
            this.sulfur -= MAX_SULFUR;

            ItemStack out = refinery.getZ();
            if(!out.isEmpty()) {
                ItemStack slot = this.getItem(11);
                if(slot.isEmpty()) {
                    this.setItem(11, out.copy());
                } else if(ItemStack.isSameItemSameComponents(slot, out) && slot.getCount() + out.getCount() <= slot.getMaxStackSize()) {
                    ItemStack stack = slot.copy();
                    stack.grow(out.getCount());
                    this.setItem(11, stack);
                }
            }
        }

        this.power -= 5;
        return true;
    }

    public DirPos[] getConPos() {
        BlockPos pos = this.getBlockPos();
        return new DirPos[] {
                new DirPos(pos.getX() + 2, pos.getY(), pos.getZ() + 1, Direction.EAST),
                new DirPos(pos.getX() + 2, pos.getY(), pos.getZ() - 1, Direction.EAST),
                new DirPos(pos.getX() - 2, pos.getY(), pos.getZ() + 1, Direction.WEST),
                new DirPos(pos.getX() - 2, pos.getY(), pos.getZ() - 1, Direction.WEST),
                new DirPos(pos.getX() + 1, pos.getY(), pos.getZ() + 2, Direction.SOUTH),
                new DirPos(pos.getX() - 1, pos.getY(), pos.getZ() + 2, Direction.SOUTH),
                new DirPos(pos.getX() + 1, pos.getY(), pos.getZ() - 2, Direction.NORTH),
                new DirPos(pos.getX() - 1, pos.getY(), pos.getZ() - 2, Direction.NORTH)
        };
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) {
            return stack.getItem() instanceof api.hbm.energymk2.IBatteryItem;
        }

        if(slot == 1) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[0].getTankType()).isEmpty();
        }

        if(slot == 3) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[1].getTankType()).isEmpty();
        }

        if(slot == 5) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[2].getTankType()).isEmpty();
        }

        if(slot == 7) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[3].getTankType()).isEmpty();
        }

        if(slot == 9) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[4].getTankType()).isEmpty();
        }

        if(slot == 12) {
            return stack.getItem() instanceof IItemFluidIdentifier;
        }

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 2 || index == 4 || index == 6 || index == 8 || index == 10 || index == 11;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.itemOutputTimer = tag.getInt("itemOutputTimer");
        this.sulfur = tag.getInt("sulfur");
        this.hasExploded = tag.getBoolean("hasExploded");
        this.onFire = tag.getBoolean("onFire");
        this.isOn = tag.getBoolean("isOn");
        this.tanks[0].readFromNBT(tag, "input");
        this.tanks[1].readFromNBT(tag, "heavy");
        this.tanks[2].readFromNBT(tag, "naphtha");
        this.tanks[3].readFromNBT(tag, "light");
        this.tanks[4].readFromNBT(tag, "petroleum");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putInt("itemOutputTimer", this.itemOutputTimer);
        tag.putInt("sulfur", this.sulfur);
        tag.putBoolean("hasExploded", this.hasExploded);
        tag.putBoolean("onFire", this.onFire);
        tag.putBoolean("isOn", this.isOn);
        this.tanks[0].writeToNBT(tag, "input");
        this.tanks[1].writeToNBT(tag, "heavy");
        this.tanks[2].writeToNBT(tag, "naphtha");
        this.tanks[3].writeToNBT(tag, "light");
        this.tanks[4].writeToNBT(tag, "petroleum");
    }

    @Override
    public void writeNBT(CompoundTag savedTag) {
        CompoundTag tag = new CompoundTag();
        tag.putLong("power", this.power);
        tag.putInt("itemOutputTimer", this.itemOutputTimer);
        tag.putInt("sulfur", this.sulfur);
        tag.putBoolean("hasExploded", this.hasExploded);
        tag.putBoolean("onFire", this.onFire);
        tag.putBoolean("isOn", this.isOn);
        this.tanks[0].writeToNBT(tag, "input");
        this.tanks[1].writeToNBT(tag, "heavy");
        this.tanks[2].writeToNBT(tag, "naphtha");
        this.tanks[3].writeToNBT(tag, "light");
        this.tanks[4].writeToNBT(tag, "petroleum");
        savedTag.put(NBT_PERSISTENT_KEY, tag);
    }

    @Override
    public void readNBT(CompoundTag savedTag) {
        CompoundTag tag = savedTag.getCompound(NBT_PERSISTENT_KEY);
        this.power = tag.getLong("power");
        this.itemOutputTimer = tag.getInt("itemOutputTimer");
        this.sulfur = tag.getInt("sulfur");
        this.hasExploded = tag.getBoolean("hasExploded");
        this.onFire = tag.getBoolean("onFire");
        this.isOn = tag.getBoolean("isOn");
        this.tanks[0].readFromNBT(tag, "input");
        this.tanks[1].readFromNBT(tag, "heavy");
        this.tanks[2].readFromNBT(tag, "naphtha");
        this.tanks[3].readFromNBT(tag, "light");
        this.tanks[4].readFromNBT(tag, "petroleum");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeInt(this.itemOutputTimer);
        buf.writeInt(this.sulfur);
        buf.writeBoolean(this.hasExploded);
        buf.writeBoolean(this.onFire);
        buf.writeBoolean(this.isOn);
        for(FluidTank tank : this.tanks) {
            tank.serialize(buf);
        }
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.itemOutputTimer = buf.readInt();
        this.sulfur = buf.readInt();
        this.hasExploded = buf.readBoolean();
        this.onFire = buf.readBoolean();
        this.isOn = buf.readBoolean();
        for(FluidTank tank : this.tanks) {
            tank.deserialize(buf);
        }
    }

    @Override
    public long getPower() {
        return Math.max(Math.min(this.power, MAX_POWER), 0);
    }

    @Override
    public void setPower(long i) {
        this.power = i;
    }

    @Override
    public long getMaxPower() {
        return MAX_POWER;
    }

    @Override
    public long transferPower(long power) {
        if(power + this.getPower() <= this.getMaxPower()) {
            this.setPower(power + this.getPower());
            return 0;
        }

        long capacity = this.getMaxPower() - this.getPower();
        long overshoot = power - capacity;
        this.setPower(this.getMaxPower());
        return overshoot;
    }

    @Override
    public boolean canConnect(Direction dir) {
        return dir != null && dir.getAxis().isHorizontal();
    }

    @Override
    public boolean canConnect(FluidType type, Direction dir) {
        return dir != null && dir.getAxis().isHorizontal();
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { this.tanks[1], this.tanks[2], this.tanks[3], this.tanks[4] };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { this.tanks[0] };
    }

    @Override
    public FluidTank[] getAllTanks() {
        return this.tanks;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineRefineryMenu(id, inventory, this);
    }

    @Override
    public void explode(Level level, BlockPos pos) {
        if(this.hasExploded) return;
        this.hasExploded = true;
        this.onFire = true;
        this.setChanged();
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

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.ELECTRIC_MOTOR_LOOP.get(), SoundSource.BLOCKS, this, 0.25F, 15F, 1.0F, 20);
    }
}
