package com.hbm.blockentity.bomb;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardReceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.IRadarCommandReceiver;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.config.NtmConfig;
import com.hbm.entity.NtmEntityTypes;
import com.hbm.entity.missile.MissileAntiBallistic;
import com.hbm.entity.missile.MissileBase;
import com.hbm.interfaces.IBomb.BombReturnCode;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.LaunchPadLargeMenu;
import com.hbm.items.IDesignatorItem;
import com.hbm.items.NtmItems;
import com.hbm.items.weapon.MissileItem;
import com.hbm.items.weapon.MissileItem.MissileFuel;
import com.hbm.lib.Library;
import com.hbm.main.NuclearTechMod;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.SoundUtils;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class LaunchPadBaseBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardReceiverMK2, IRadarCommandReceiver, IFluidCopiable {

    /** Automatic instantiation of generic missiles, i.e. everything that both extends EntityMissileBaseNT and needs a designator */
    public static final HashMap<ComparableStack, EntityType<? extends MissileBase>> missiles = new HashMap<>();

    public static void registerLaunchables() {

        //Tier 0
        missiles.put(new ComparableStack(NtmItems.MISSILE_MICRO.get()), NtmEntityTypes.MISSILE_MICRO.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_SCHRABIDIUM.get()), NtmEntityTypes.MISSILE_SCHRABIDIUM.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_BHOLE.get()), NtmEntityTypes.MISSILE_BHOLE.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_TAINT.get()), NtmEntityTypes.MISSILE_TAINT.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_EMP.get()), NtmEntityTypes.MISSILE_EMP.get());

        // Tier 1
        missiles.put(new ComparableStack(NtmItems.MISSILE_GENERIC.get()), NtmEntityTypes.MISSILE_GENERIC.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_DECOY.get()), NtmEntityTypes.MISSILE_DECOY.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_INCENDIARY.get()), NtmEntityTypes.MISSILE_INCENDIARY.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_CLUSTER.get()), NtmEntityTypes.MISSILE_CLUSTER.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_BUSTER.get()), NtmEntityTypes.MISSILE_BUSTER.get());

        // Stealth missile
        missiles.put(new ComparableStack(NtmItems.MISSILE_STEALTH.get()), NtmEntityTypes.MISSILE_STEALTH.get());

        // Tier 2
        missiles.put(new ComparableStack(NtmItems.MISSILE_STRONG.get()), NtmEntityTypes.MISSILE_STRONG.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_INCENDIARY_STRONG.get()), NtmEntityTypes.MISSILE_INCENDIARY_STRONG.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_CLUSTER_STRONG.get()), NtmEntityTypes.MISSILE_CLUSTER_STRONG.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_BUSTER_STRONG.get()), NtmEntityTypes.MISSILE_BUSTER_STRONG.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_EMP_STRONG.get()), NtmEntityTypes.MISSILE_EMP_STRONG.get());

        // Tier 3
        missiles.put(new ComparableStack(NtmItems.MISSILE_BURST.get()), NtmEntityTypes.MISSILE_BURST.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_INFERNO.get()), NtmEntityTypes.MISSILE_INFERNO.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_RAIN.get()), NtmEntityTypes.MISSILE_RAIN.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_DRILL.get()), NtmEntityTypes.MISSILE_DRILL.get());

        // Shuttle missile
        missiles.put(new ComparableStack(NtmItems.MISSILE_SHUTTLE.get()), NtmEntityTypes.MISSILE_SHUTTLE.get());

        // Tier 4
        missiles.put(new ComparableStack(NtmItems.MISSILE_NUCLEAR.get()), NtmEntityTypes.MISSILE_NUCLEAR.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_NUCLEAR_CLUSTER.get()), NtmEntityTypes.MISSILE_NUCLEAR_CLUSTER.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_VOLCANO.get()), NtmEntityTypes.MISSILE_VOLCANO.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_DOOMSDAY.get()), NtmEntityTypes.MISSILE_DOOMSDAY.get());
        missiles.put(new ComparableStack(NtmItems.MISSILE_DOOMSDAY_RUSTED.get()), NtmEntityTypes.MISSILE_DOOMSDAY_RUSTED.get());
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

    public LaunchPadBaseBlockEntity(BlockEntityType<? extends LaunchPadBaseBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 7);
        this.tanks = new FluidTank[2];
        this.tanks[0] = new FluidTank(Fluids.NONE, 24_000);
        this.tanks[1] = new FluidTank(Fluids.NONE, 24_000);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.launch_pad"); }

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

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {

            if(level.getGameTime() % 20 == 0) {
                for(DirPos pos : this.getConPos()) {
                    this.trySubscribe(level, pos);
                    if(tanks[0].getTankType() != Fluids.NONE) this.trySubscribe(tanks[0].getTankType(), level, pos);
                    if(tanks[1].getTankType() != Fluids.NONE) this.trySubscribe(tanks[1].getTankType(), level, pos);
                }
            }

            if(this.redstonePower > 0 && this.prevRedstonePower <= 0) {
                this.launchFromDesignator();
            }

            this.power = Library.chargeTEFromItems(slots, 2, power, maxPower);
            tanks[0].loadTank(level, 3, 4, slots);
            tanks[1].loadTank(level, 5, 6, slots);

            if(this.isMissileValid()) {
                if(slots.get(0).getItem() instanceof MissileItem missileItem) {
                    this.setFuel(missileItem);
                }
            }

            this.networkPackNT(250);
        }
    }

    public abstract DirPos[] getConPos();

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);

        buf.writeLong(this.power);
        buf.writeInt(this.state);
        tanks[0].serialize(buf);
        tanks[1].serialize(buf);

        buf.writeInt(Item.getId(slots.get(0).getItem()));
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
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
        for(int i = 0; i < activatedBlocksTag.getAllKeys().size() / 3; i++) {
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
        for(BlockPos p : this.activatedBlocks) {
            activatedBlocksTag.putInt("x" + i, p.getX());
            activatedBlocksTag.putInt("y" + i, p.getY());
            activatedBlocksTag.putInt("z" + i, p.getZ());
            i++;
        }
        tag.put("ActivatedBlocks", activatedBlocksTag);
    }

    public void updateRedstonePower(BlockPos pos) {
        if(level == null) return;
        boolean powered = level.hasNeighborSignal(pos);
        boolean contained = activatedBlocks.contains(pos);
        if(!contained && powered) {
            activatedBlocks.add(pos);
            if(redstonePower == -1) redstonePower = 0;
            redstonePower++;
        } else if(contained && !powered) {
            activatedBlocks.remove(pos);
            redstonePower--;
            if(redstonePower == 0) redstonePower = -1;
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new LaunchPadLargeMenu(id, inventory, this);
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }
    @Override public FluidTank[] getAllTanks() { return this.tanks; }
    @Override public FluidTank[] getReceivingTanks() { return this.tanks; }

    @Override public boolean canConnect(Direction dir) { return dir != Direction.UP && dir != Direction.DOWN; }

    public void setFuel(MissileItem missile) {
        switch(missile.fuel) {
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
        if(this.power < 75_000) return false;

        if(slots.get(0).getItem() instanceof MissileItem missileItem) {
            if(this.tanks[0].getFill() < missileItem.fuelCap) return false;
            if(this.tanks[1].getFill() < missileItem.fuelCap) return false;

            return true;
        }

        return false;
    }

    @Nullable
    public Entity instantiateMissile(int targetX, int targetZ) {
        if(this.level == null) return null;

        if(slots.get(0).isEmpty()) return null;

        EntityType<? extends MissileBase> entityType = missiles.get(new ComparableStack(slots.get(0)).makeSingular());

        if(entityType != null) {
            MissileBase missile = entityType.create(level);
            if(missile == null) return null;
            missile.setPosAndTarget(Vec3.atBottomCenterOf(this.getBlockPos()).add(0.0, this.getLaunchOffset(), 0.0), targetX, targetZ);
            if(NtmConfig.COMMON.ENABLE_EXTENDED_LOGGING.get()) NuclearTechMod.LOGGER.info("[MISSILE] Tried to launch missile at {} / {} / {} to {} / {}!", this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), targetX, targetZ);
            missile.getEntityData().set(MissileBase.ROT, this.getBlockState().getValue(DummyableBlock.FACING));
            return missile;
        }

        if(slots.get(0).is(NtmItems.MISSILE_ANTI_BALLISTIC.get())) {
            MissileAntiBallistic missile = new MissileAntiBallistic(NtmEntityTypes.MISSILE_ANTI_BALLISTIC.get(), this.level);
            missile.setPos(Vec3.atBottomCenterOf(this.getBlockPos()).add(0.0, this.getLaunchOffset(), 0.0));
            return missile;
        }

        return null;
    }

    public void finalizeLaunch(Entity missile) {
        if(this.level == null) return;

        level.addFreshEntity(missile);
        SoundUtils.playAtVec3(this.level, this.getBlockPos().getCenter(), NtmSoundEvents.MISSILE_TAKEOFF.get(), SoundSource.BLOCKS);

        this.power -= 75_000;

        if(slots.get(0).getItem() instanceof MissileItem missileItem) {
            tanks[0].setFill(tanks[0].getFill() - missileItem.fuelCap);
            tanks[1].setFill(tanks[1].getFill() - missileItem.fuelCap);
        }

        this.removeItem(0, 1);
    }

    public BombReturnCode launchFromDesignator() {
        if(!this.canLaunch()) return BombReturnCode.ERROR_MISSING_COMPONENT;

        boolean needsDesignator = needsDesignator(slots.get(0).getItem());

        int x = this.getBlockPos().getX();
        int z = this.getBlockPos().getZ();

        int targetX = x;
        int targetZ = z;

        if(slots.get(1).getItem() instanceof IDesignatorItem idi) {
            if(needsDesignator) {

                if(!idi.isReady(level, slots.get(1), this.getBlockPos())) return BombReturnCode.ERROR_MISSING_COMPONENT;

                Vec3 coords = idi.getCoords(level, slots.get(1), this.getBlockPos());
                targetX = Mth.floor(coords.x);
                targetZ = Mth.floor(coords.z);
            }

        } else {
            if(needsDesignator) return BombReturnCode.ERROR_MISSING_COMPONENT;
        }

        return this.launchToCoordinate(targetX, targetZ);
    }

    public BombReturnCode launchToEntity(Entity entity) {
        if(!this.canLaunch()) return BombReturnCode.ERROR_MISSING_COMPONENT;

        Entity e = this.instantiateMissile((int) Math.floor(entity.position.x), (int) Math.floor(entity.position.z));
        if(e != null) {
            finalizeLaunch(e);
            return BombReturnCode.LAUNCHED;
        }
        return BombReturnCode.ERROR_MISSING_COMPONENT;
    }

    public BombReturnCode launchToCoordinate(int targetX, int targetZ) {
        if(!this.canLaunch()) return BombReturnCode.ERROR_MISSING_COMPONENT;

        Entity e = instantiateMissile(targetX, targetZ);
        if(e != null) {
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
        return item != NtmItems.MISSILE_ANTI_BALLISTIC.get();
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

        if(slots.get(0).getItem() instanceof MissileItem missile) {
            MissileFuel fuel = missile.fuel;

            if(fuel == MissileFuel.SOLID) return 0;
            return tanks[tank].getFill() >= missile.fuelCap ? 1 : -1;
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
