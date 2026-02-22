package com.hbm.blockentity.bomb;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardReceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.IRadarCommandReceiver;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.entity.missile.MissileBaseNT;
import com.hbm.entity.missile.MissileTier1.*;
import com.hbm.interfaces.IBomb.BombReturnCode;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.items.IDesignatorItem;
import com.hbm.items.ModItems;
import com.hbm.items.weapon.MissileItem;
import com.hbm.items.weapon.MissileItem.MissileFuel;
import com.hbm.lib.Library;
import com.hbm.lib.ModSounds;
import com.hbm.util.fauxpointtwelve.DirPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class LaunchPadBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardReceiverMK2, IRadarCommandReceiver, IFluidCopiable {

    /** Automatic instantiation of generic missiles, i.e. everything that both extends EntityMissileBaseNT and needs a designator */
    public static final HashMap<ComparableStack, Class<? extends MissileBaseNT>> missiles = new HashMap<>();

    public static void registerLaunchables() {
        //Tier 1
        missiles.put(new ComparableStack(ModItems.MISSILE_GENERIC.get()), MissileGeneric.class);
        missiles.put(new ComparableStack(ModItems.MISSILE_DECOY.get()), MissileDecoy.class);
    }

    public ItemStack toRender = ItemStack.EMPTY;

    public long power;
    public final long maxPower = 100_000;

    public int prevRedstonePower;
    public int redstonePower;
    public Set<BlockPos> activatedBlocks = new HashSet<>(4);

    public int state = 0;
    public static final int STATE_MISSING = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_READY = 2;

    public FluidTank[] tanks;

    public LaunchPadBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState, 7);
        this.tanks = new FluidTank[2];
        this.tanks[0] = new FluidTank(Fluids.NONE, 24_000);
        this.tanks[1] = new FluidTank(Fluids.NONE, 24_000);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.launchPad"); }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return super.canTakeItemThroughFace(index, stack, direction);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { 0 };
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return index == 0 && this.isMissileValid(stack);
    }

    public abstract DirPos[] getConPos();

    @Override
    public void updateEntity() {
        if (level == null) return;

        if (!level.isClientSide) {

            if (level.getGameTime() % 20 == 0) {
                for (DirPos pos : getConPos()) {
                    this.trySubscribe(level, pos);
                    if (tanks[0].getTankType() != Fluids.NONE) this.trySubscribe(tanks[0].getTankType(), level, pos);
                    if (tanks[1].getTankType() != Fluids.NONE) this.trySubscribe(tanks[1].getTankType(), level, pos);
                }
            }

            if (this.redstonePower > 0 && this.prevRedstonePower <= 0) {
                this.launchFromDesignator();
            }

            this.power = Library.chargeTEFromItems(slots, 2, power, maxPower);
            tanks[0].loadTank(level, 3, 4, slots);
            tanks[1].loadTank(level, 5, 6, slots);

            if (this.isMissileValid()) {
                if (slots.get(0).getItem() instanceof MissileItem missileItem) {
                    this.setFuel(missileItem);
                }
            }

            this.networkPackNT(250);
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);

        buf.writeLong(this.power);
        buf.writeInt(this.state);
        tanks[0].serialize(buf);
        tanks[1].serialize(buf);

        buf.writeInt(Item.getId(slots.get(0).getItem()));
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);

        this.power = buf.readLong();
        this.state = buf.readInt();
        tanks[0].deserialize(buf);
        tanks[1].deserialize(buf);

        this.toRender = new ItemStack(Item.byId(buf.readInt()), 1);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        power = tag.getLong("Power");
        tanks[0].readFromNBT(tag, "T0");
        tanks[1].readFromNBT(tag, "T1");

        this.redstonePower = tag.getInt("RedstonePower");
        this.prevRedstonePower = tag.getInt("PrevRedstonePower");
        CompoundTag activatedBlocksTag = tag.getCompound("ActivatedBlocks");
        this.activatedBlocks.clear();
        for (int i = 0; i < activatedBlocksTag.getAllKeys().size() / 3; i++) {
            this.activatedBlocks.add(new BlockPos(
                    activatedBlocksTag.getInt("x" + i),
                    activatedBlocksTag.getInt("y" + i),
                    activatedBlocksTag.getInt("z" + i)
            ));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putLong("Power", power);
        tanks[0].writeToNBT(tag, "T0");
        tanks[1].writeToNBT(tag, "T1");

        tag.putInt("RedstonePower", redstonePower);
        tag.putInt("PrevRedstonePower", prevRedstonePower);

        CompoundTag activatedBlocksTag = new CompoundTag();
        int i = 0;
        for (BlockPos p : this.activatedBlocks) {
            activatedBlocksTag.putInt("x" + i, p.getX());
            activatedBlocksTag.putInt("y" + i, p.getY());
            activatedBlocksTag.putInt("z" + i, p.getZ());
            i++;
        }
        tag.put("ActivatedBlocks", activatedBlocksTag);
    }

    public void updateRedstonePower(BlockPos pos) {
        if (level == null) return;
        boolean powered = level.hasNeighborSignal(pos);
        boolean contained = activatedBlocks.contains(pos);
        if (!contained && powered) {
            activatedBlocks.add(pos);
            if (redstonePower == -1) {
                redstonePower = 0;
            }
            redstonePower++;
        } else if (contained && !powered) {
            activatedBlocks.remove(pos);
            redstonePower--;
            if (redstonePower == 0) {
                redstonePower = -1;
            }
        }
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }
    @Override public FluidTank[] getAllTanks() { return this.tanks; }
    @Override public FluidTank[] getReceivingTanks() { return this.tanks; }

    @Override public boolean canConnect(Direction dir) {
        return dir != Direction.UP && dir != Direction.DOWN;
    }

    public void setFuel(MissileItem missile) {
        switch (missile.fuel) {
            case ETHANOL_PEROXIDE -> {
                tanks[0].setTankType(Fluids.ETHANOL);
                tanks[1].setTankType(Fluids.PEROXIDE);
            }
            case KEROSENE_PEROXIDE -> {
                tanks[0].setTankType(Fluids.KEROSENE);
                tanks[1].setTankType(Fluids.PEROXIDE);
            }
            case KEROSENE_LOXY -> {
                tanks[0].setTankType(Fluids.KEROSENE);
                tanks[1].setTankType(Fluids.OXYGEN);
            }
            case JETFUEL_LOXY -> {
                tanks[0].setTankType(Fluids.KEROSENE_REFORM);
                tanks[1].setTankType(Fluids.OXYGEN);
            }
        }
    }

    /** Requires the missile slot to be non-null and he item to be compatible */
    public boolean isMissileValid() {
        return isMissileValid(slots.get(0));
    }

    public boolean isMissileValid(ItemStack stack) {
        return stack.getItem() instanceof MissileItem missileItem && missileItem.launchable;
    }

    public boolean hasFuel() {
        if (this.power < 75_000) return false;

        if (slots.get(0).getItem() instanceof MissileItem missileItem) {
            if (this.tanks[0].getFill() < missileItem.fuelCap) return false;
            if (this.tanks[1].getFill() < missileItem.fuelCap) return false;

            return true;
        }

        return false;
    }

    public Entity instantiateMissile(int targetX, int targetZ) {

        if (slots.get(0).isEmpty()) return null;

        Class<? extends MissileBaseNT> clazz = missiles.get(new ComparableStack(slots.get(0)).makeSingular());

        if(clazz != null) {
            try {
                MissileBaseNT missile = clazz.getConstructor(Level.class, double.class, double.class, double.class, int.class, int.class).newInstance(level, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + getLaunchOffset() /* Position arguments need to be -floats- (doubles, whatever), jackass */, this.getBlockPos().getZ() + 0.5, targetX, targetZ);
                // if(GeneralConfig.enableExtendedLogging) MainRegistry.logger.log(Level.INFO, "[MISSILE] Tried to launch missile at " + xCoord + " / " + yCoord + " / " + zCoord + " to " + xCoord + " / " + zCoord + "!");
                missile.getEntityData().set(MissileBaseNT.ROT, this.getBlockState().getValue(DummyableBlock.FACING));
                return missile;
            } catch(Exception ignored) { }
        }

        return null;
    }

    public void finalizeLaunch(Entity missile) {
        if (this.level == null) return;
        level.addFreshEntity(missile);
        level.playSound(null, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY(), this.getBlockPos().getZ() + 0.5, ModSounds.MISSILE_TAKE_OFF.get(), SoundSource.BLOCKS, 2.0F, 1.0F);

        this.power -= 75_000;

        if (slots.get(0).getItem() instanceof MissileItem missileItem) {
            tanks[0].setFill(tanks[0].getFill() - missileItem.fuelCap);
            tanks[1].setFill(tanks[1].getFill() - missileItem.fuelCap);
        }

        this.removeItem(0, 1);
    }

    public BombReturnCode launchFromDesignator() {
        if(!canLaunch()) return BombReturnCode.ERROR_MISSING_COMPONENT;

        boolean needsDesignator = needsDesignator(slots.get(0).getItem());

        int x = this.getBlockPos().getX();
        int z = this.getBlockPos().getZ();

        int targetX = x;
        int targetZ = z;

        if (slots.get(1).getItem() instanceof IDesignatorItem designatorItem) {
            if (needsDesignator) {
                if(!designatorItem.isReady(level, slots.get(1), this.getBlockPos())) return BombReturnCode.ERROR_MISSING_COMPONENT;

                Vec3 coords = designatorItem.getCoords(level, slots.get(1), this.getBlockPos());
                targetX = (int) Math.floor(coords.x);
                targetZ = (int) Math.floor(coords.z);
            }

        } else {
            if (needsDesignator) return BombReturnCode.ERROR_MISSING_COMPONENT;
        }

        return this.launchToCoordinate(targetX, targetZ);
    }

    public BombReturnCode launchToEntity(Entity entity) {
        if (!canLaunch()) return BombReturnCode.ERROR_MISSING_COMPONENT;

        Entity e = instantiateMissile((int) Math.floor(entity.position.x), (int) Math.floor(entity.position.z));
        if (e != null) {
            finalizeLaunch(e);
            return BombReturnCode.LAUNCHED;
        }
        return BombReturnCode.ERROR_MISSING_COMPONENT;
    }

    public BombReturnCode launchToCoordinate(int targetX, int targetZ) {
        if (!canLaunch()) return BombReturnCode.ERROR_MISSING_COMPONENT;

        Entity e = instantiateMissile(targetX, targetZ);
        if (e != null) {
            finalizeLaunch(e);
            return BombReturnCode.LAUNCHED;
        }
        return BombReturnCode.ERROR_MISSING_COMPONENT;
    }

    @Override
    public boolean sendCommandPosition(BlockPos pos) {
        return this.launchToCoordinate(pos.getX(), pos.getZ()) == BombReturnCode.LAUNCHED;
    }

    @Override
    public boolean sendCommandEntity(Entity target) {
        return this.launchToEntity(target) == BombReturnCode.LAUNCHED;
    }

    public boolean needsDesignator(Item item) {
        return true;
    }

    /** Full launch condition, checks if the item is launchable, fuel and power are present and any additional checks based on launch pad type */
    public boolean canLaunch() {
        return this.isMissileValid() && this.hasFuel() && this.isReadyForLaunch();
    }

    public int getFuelState() {
        return getGaugeState(0);
    }

    public int getOxidizerState() {
        return getGaugeState(1);
    }

    public int getGaugeState(int tank) {
        if(slots.get(0).isEmpty()) return 0;

        if(slots.get(0).getItem() instanceof MissileItem missileItem) {
            MissileFuel fuel = missileItem.fuel;

            if (fuel == MissileFuel.SOLID) return 0;
            return tanks[tank].getFill() >= missileItem.fuelCap ? 1 : -1;
        }

        return 0;
    }

    /** Any extra conditions for launching in addition to the missile being valid and fueled */
    public abstract boolean isReadyForLaunch();
    public abstract double getLaunchOffset();

    @Override
    public int[] getFluidIDToCopy() {
        return new int[] { tanks[0].getTankType().getID(), tanks[1].getTankType().getID() };
    }

    @Override
    public FluidTank getTankToPaste() {
        return null;
    }
}
