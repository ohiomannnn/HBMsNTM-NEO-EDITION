package com.hbm.blockentity.machine.oil;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.SoundUtils;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public abstract class OilDrillBaseBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IPersistentNBT, IUpgradeInfoProvider, IFluidCopiable {

    public int indicator = 0;

    public long power;

    public FluidTank[] tanks;

    public UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public OilDrillBaseBlockEntity(BlockEntityType<? extends OilDrillBaseBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 8);

        tanks = new FluidTank[2];
        tanks[0] = new FluidTank(Fluids.OIL_CRUDE, 64_000);
        tanks[1] = new FluidTank(Fluids.GAS, 64_000);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.power = tag.getLong("power");
        for(int i = 0; i < this.tanks.length; i++) this.tanks[i].readFromNBT(tag, "t" + i);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putLong("power", power);
        for(int i = 0; i < this.tanks.length; i++)
            this.tanks[i].writeToNBT(tag, "t" + i);
    }

    @Override
    public void writeNBT(CompoundTag tag) {
        CompoundTag persistentTag = new CompoundTag();

        boolean empty = power == 0;
        for(FluidTank tank : tanks) if(tank.getFill() > 0) empty = false;

        if(!empty) {
            persistentTag.putLong("power", power);
            for(int i = 0; i < this.tanks.length; i++) {
                this.tanks[i].writeToNBT(persistentTag, "t" + i);
            }
            tag.put(NBT_PERSISTENT_KEY, persistentTag);
        }
    }

    @Override
    public void readNBT(CompoundTag tag) {
        CompoundTag persistentTag = tag.getCompound(NBT_PERSISTENT_KEY);
        this.power = persistentTag.getLong("power");
        for(int i = 0; i < this.tanks.length; i++) {
            this.tanks[i].readFromNBT(persistentTag, "t" + i);
        }
    }

    public int speedLevel;
    public int energyLevel;
    public int overLevel;

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {

            this.updateConnections();

            this.tanks[0].unloadTank(this.level, 1, 2, slots);
            this.tanks[1].unloadTank(this.level, 3, 4, slots);

            upgradeManager.checkSlots(slots, 5, 7);
            this.speedLevel = upgradeManager.getLevel(UpgradeType.SPEED);
            this.energyLevel = upgradeManager.getLevel(UpgradeType.POWER);
            this.overLevel = upgradeManager.getLevel(UpgradeType.OVERDRIVE) + 1;
            int abLevel = upgradeManager.getLevel(UpgradeType.AFTERBURN);

            int toBurn = Math.min(tanks[1].getFill(), abLevel * 10);

            if(toBurn > 0) {
                tanks[1].setFill(tanks[1].getFill() - toBurn);
                this.power += toBurn * 5;

                if(this.power > this.getMaxPower()) this.power = this.getMaxPower();
            }

            this.power = Library.chargeTEFromItems(slots, 0, power, this.getMaxPower());

            for(DirPos pos : this.getConPos()) {
                if(tanks[0].getFill() > 0) this.tryProvide(tanks[0], this.level, pos);
                if(tanks[1].getFill() > 0) this.tryProvide(tanks[1], this.level, pos);
            }

            if(this.power >= this.getPowerReqEff() && this.tanks[0].getFill() < this.tanks[0].getMaxFill() && this.tanks[1].getFill() < this.tanks[1].getMaxFill()) {

                this.power -= this.getPowerReqEff();
                if(this.level.getGameTime() % this.getDelayEff() == 0) {
                    this.indicator = 0;

                    for(int y = this.getBlockPos().getY() - 1; y >= this.getDrillDepth(); y--) {

                        if(!this.level.getBlockState(new BlockPos(this.getBlockPos().getX(), y, this.getBlockPos().getZ())).is(NtmBlocks.OIL_PIPE.get())) {

                            if(this.trySuck(y)) {
                                break;
                            } else {
                                this.tryDrill(y);
                                break;
                            }
                        }

                        if(y == this.getDrillDepth()) this.indicator = 1;
                    }
                }

            } else {
                this.indicator = 2;
            }

            this.networkPackNT(25);
        }
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);

        buf.writeLong(this.power);
        buf.writeInt(this.indicator);
        for(FluidTank tank : tanks) tank.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);

        this.power = buf.readLong();
        this.indicator = buf.readInt();
        for(FluidTank tank : tanks) tank.deserialize(buf);
    }

    public boolean canPump() {
        return true;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);

        if(this.level == null) return;
        if(index >= 5 && index <= 7 && stack.getItem() instanceof MachineUpgradeItem) {
            SoundUtils.playAtVec3(this.level, this.getBlockPos().getCenter().add(0.0, 1.0, 0.0), NtmSoundEvents.UPGRADE_PLUG.get(), SoundSource.BLOCKS);
        }
    }

    public int getPowerReqEff() {
        int req = this.getPowerReq();
        return (req + (req / 4 * this.speedLevel) - (req / 4 * this.energyLevel)) * this.overLevel;
    }

    public int getDelayEff() {
        int delay = getDelay();
        return Math.max((delay - (delay / 4 * this.speedLevel) + (delay / 10 * this.energyLevel)) / this.overLevel, 1);
    }

    public abstract int getPowerReq();
    public abstract int getDelay();

    public void tryDrill(int y) {
        if(this.level == null) return;

        BlockPos pos = new BlockPos(this.getBlockPos().getX(), y, this.getBlockPos().getZ());
        BlockState state = level.getBlockState(pos);

        if(state.getBlock().getExplosionResistance() < 1000F) {
            this.onDrill(y);
            this.level.setBlock(pos, NtmBlocks.OIL_PIPE.get().defaultBlockState(), 3);
        } else {
            this.indicator = 2;
        }
    }

    public void onDrill(int y) { }

    public int getDrillDepth() {
        return 6;
    }

    public boolean trySuck(int y) {
        if(this.level == null) return false;

        BlockState state = level.getBlockState(new BlockPos(this.getBlockPos().getX(), y, this.getBlockPos().getZ()));

        if(!canSuckBlock(state)) return false;
        if(!this.canPump()) return true;

        trace.clear();

        return this.suckRecursive(new BlockPos(this.getBlockPos().getX(), y, this.getBlockPos().getZ()), 0);
    }

    public boolean canSuckBlock(BlockState state) {
        return state.is(NtmBlocks.ORE_OIL.get()) || state.is(NtmBlocks.ORE_OIL_EMPTY.get());
    }

    protected HashSet<BlockPos> trace = new HashSet<>();

    public boolean suckRecursive(BlockPos pos, int layer) {
        if(this.level == null) return false;

        if(this.trace.contains(pos)) return false;

        this.trace.add(pos);

        if(layer > 64) return false;

        BlockState state = this.level.getBlockState(pos);

        if(state.is(NtmBlocks.ORE_OIL.get()) || state.is(NtmBlocks.ORE_BEDROCK_OIL.get())) {
            this.doSuck(pos);
            return true;
        }

        if(state.is(NtmBlocks.ORE_OIL_EMPTY.get())) {
            Collection<Direction> dirs = Direction.allShuffled(this.level.random);

            for(Direction dir : dirs) {
                if(this.suckRecursive(pos.relative(dir), layer + 1)) return true;
            }
        }

        return false;
    }

    public void doSuck(BlockPos pos) {
        if(this.level == null) return;

        if(this.level.getBlockState(pos).is(NtmBlocks.ORE_OIL.get())) {
            this.onSuck(pos);
        }
    }

    public abstract void onSuck(BlockPos pos);

    @Override public void setPower(long power) { this.power = power; }
    @Override public long getPower() { return this.power; }

    @Override public FluidTank[] getSendingTanks() { return tanks; }
    @Override public FluidTank[] getReceivingTanks() { return new FluidTank[0]; }
    @Override public FluidTank[] getAllTanks() { return tanks; }

    public abstract DirPos[] getConPos();

    protected void updateConnections() {
        if(this.level == null) return;

        for(DirPos pos : this.getConPos()) {
            this.trySubscribe(level, pos);
        }
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int lvl, TooltipFlag flag) {
        return type == UpgradeType.SPEED || type == UpgradeType.POWER || type == UpgradeType.OVERDRIVE || type == UpgradeType.AFTERBURN;
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new HashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        upgrades.put(UpgradeType.AFTERBURN, 3);
        upgrades.put(UpgradeType.OVERDRIVE, 3);
        return upgrades;
    }

    @Override
    public FluidTank getTankToPaste() {
        return null;
    }
}
