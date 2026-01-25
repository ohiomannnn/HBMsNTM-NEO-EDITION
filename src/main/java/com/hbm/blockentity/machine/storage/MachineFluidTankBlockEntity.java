package com.hbm.blockentity.machine.storage;

import api.hbm.energymk2.IEnergyReceiverMK2.ConnectionPriority;
import api.hbm.fluidmk2.FluidNode;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.*;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Corrosive;
import com.hbm.inventory.fluid.trait.FT_Flammable;
import com.hbm.inventory.fluid.trait.FluidTraitSimple.FT_Gaseous;
import com.hbm.inventory.fluid.trait.FluidTraitSimple.FT_Gaseous_ART;
import com.hbm.inventory.fluid.trait.FluidTraitSimple.FT_Liquid;
import com.hbm.inventory.menus.MachineFluidTankMenu;
import com.hbm.items.ModItems;
import com.hbm.lib.Library;
import com.hbm.uninos.UniNodespace;
import com.hbm.util.fauxpointtwelve.DirPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MachineFluidTankBlockEntity extends MachineBaseBlockEntity implements IFluidStandardTransceiverMK2, IPersistentNBT, IOverpressurable, MenuProvider, IRepairable, IFluidCopiable, IControlReceiver {

    protected FluidNode node;
    protected FluidType lastType;

    public FluidTank tank;
    public short mode = 0;
    public static final short modes = 4;
    public boolean hasExploded = false;
    public boolean onFire = false;
    public byte lastRedstone = 0;
    public Explosion lastExplosion = null;

    public int age = 0;

    public MachineFluidTankBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUID_TANK.get(), pos, state, 6);
        tank = new FluidTank(Fluids.NONE, 256000);
    }

    @Override public Component getName() { return Component.translatable("container.fluidTank"); }

    public byte getComparatorPower() {
        if (tank.getFill() == 0) return 0;
        double frac = (double) tank.getFill() / (double) tank.getMaxFill() * 15D;
        return (byte) (Math.clamp(frac + 1, 0, 15));
    }

    @Override
    public void updateEntity() {
        if (level != null && !level.isClientSide) {
            if (!hasExploded) {
                age++;

                if (age >= 20) {
                    age = 0;
                    this.markChanged();
                }

                // In buffer mode, acts like a pipe block, providing fluid to its own node
                // otherwise, it is a regular providing/receiving machine, blocking further propagation
                if (mode == 1) {
                    if (this.node == null || this.node.expired || tank.getTankType() != lastType) {

                        this.node = (FluidNode) UniNodespace.getNode(level, getBlockPos(), tank.getTankType().getNetworkProvider());

                        if (this.node == null || this.node.expired || tank.getTankType() != lastType) {
                            this.node = this.createNode(tank.getTankType());
                            UniNodespace.createNode(level, this.node);
                            lastType = tank.getTankType();
                        }
                    }

                    if (node != null && node.hasValidNet()) {
                        node.net.addProvider(this);
                        node.net.addReceiver(this);
                    }
                } else {
                    if (this.node != null) {
                        UniNodespace.destroyNode(level, getBlockPos(), tank.getTankType().getNetworkProvider());
                        this.node = null;
                    }

                    for (DirPos pos : getConPos()) {
                        FluidNode dirNode = (FluidNode) UniNodespace.getNode(level, pos, tank.getTankType().getNetworkProvider());

                        if (mode == 2) {
                            tryProvide(tank, level, pos, pos.getDir());
                        } else {
                            if (dirNode != null && dirNode.hasValidNet()) dirNode.net.removeProvider(this);
                        }

                        if (mode == 0) {
                            if (dirNode != null && dirNode.hasValidNet()) dirNode.net.addReceiver(this);
                        } else {
                            if (dirNode != null && dirNode.hasValidNet()) dirNode.net.removeReceiver(this);
                        }
                    }
                }

                tank.loadTank(level, 2, 3, slots);
                tank.setType(0, 1, slots);
                tank.setFill(tank.getFill() + 10);
            } else if (this.node != null) {
                UniNodespace.destroyNode(level, getBlockPos(), tank.getTankType().getNetworkProvider());
                this.node = null;
            }

            byte comp = this.getComparatorPower(); //comparator shit
            if (comp != this.lastRedstone) {
                this.setChanged();
                for (DirPos pos : getConPos()) this.updateRedstoneConnection(pos);
            }
            this.lastRedstone = comp;

            if (tank.getFill() > 0) {
                if (tank.getTankType().isAntimatter()) {
                    new ExplosionVNT(level, getBlockPos().getX() + 0.5, getBlockPos().getY() + 1.5, getBlockPos().getZ() + 0.5, 5F).makeAmat().setBlockAllocator(null).setBlockProcessor(null).explode();
                    this.explode();
                    this.tank.setFill(0);
                }

                if (tank.getTankType().hasTrait(FT_Corrosive.class) && tank.getTankType().getTrait(FT_Corrosive.class).isHighlyCorrosive()) {
                    this.explode();
                }

                if (this.hasExploded) {

                    int leaking;
                    if (tank.getTankType().isAntimatter()) {
                        leaking = tank.getFill();
                    } else if (tank.getTankType().hasTrait(FT_Gaseous.class) || tank.getTankType().hasTrait(FT_Gaseous_ART.class)) {
                        leaking = Math.min(tank.getFill(), tank.getMaxFill() / 100);
                    } else {
                        leaking = Math.min(tank.getFill(), tank.getMaxFill() / 10000);
                    }

                    updateLeak(leaking);
                }
            }

            tank.unloadTank(level, 4, 5, slots);

            this.networkPackNT(150);
        }
    }

    public static void tick(Level ignored, BlockPos ignored1, BlockState ignored2, MachineFluidTankBlockEntity be) { be.updateEntity(); }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if (this.level != null && !this.level.isClientSide) {
            if (this.node != null) {
                UniNodespace.destroyNode(this.level, this.getBlockPos(), this.tank.getTankType().getNetworkProvider());
            }
        }
    }

    protected FluidNode createNode(FluidType type) {
        DirPos[] conPos = getConPos();

        HashSet<BlockPos> posSet = new HashSet<>();
        posSet.add(new BlockPos(this.getBlockPos()));
        for (DirPos pos : conPos) {
            Direction dir = pos.getDir();
            posSet.add(new BlockPos(pos.getX() - dir.getStepX(), pos.getY() - dir.getStepY(), pos.getZ() - dir.getStepZ()));
        }

        return new FluidNode(type.getNetworkProvider(), posSet.toArray(new BlockPos[posSet.size()])).setConnections(conPos);
    }


    @Override
    public void serialize(ByteBuf buf, RegistryAccess registryAccess) {
        super.serialize(buf, registryAccess);
        buf.writeShort(mode);
        buf.writeBoolean(hasExploded);
        tank.serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf, RegistryAccess registryAccess) {
        super.deserialize(buf, registryAccess);
        mode = buf.readShort();
        hasExploded = buf.readBoolean();
        tank.deserialize(buf);
    }

    /** called when the tank breaks due to hazardous materials or external force, can be used to quickly void part of the tank or spawn a mushroom cloud */
    public void explode() {
        this.hasExploded = true;
        this.onFire = tank.getTankType().hasTrait(FT_Flammable.class);
        this.markChanged();
    }

    /** called every tick post explosion, used for leaking fluid and spawning particles */
    public void updateLeak(int amount) {
        if (!hasExploded) return;
        if (amount <= 0) return;

        this.tank.getTankType().onFluidRelease(this, tank, amount);
        this.tank.setFill(Math.max(0, this.tank.getFill() - amount));
    }

    @Override
    public void explode(Level level, BlockPos pos) {
        if (this.hasExploded) return;
        this.onFire = tank.getTankType().hasTrait(FT_Flammable.class);
        this.hasExploded = true;
        this.markChanged();
    }

    protected DirPos[] getConPos() {

        int x = this.getBlockPos().getX();
        int y = this.getBlockPos().getY();
        int z = this.getBlockPos().getZ();

        return new DirPos[] {
                new DirPos(x + 2, y, z - 1, Library.POS_X),
                new DirPos(x + 2, y, z + 1, Library.POS_X),
                new DirPos(x - 2, y, z - 1, Library.NEG_X),
                new DirPos(x - 2, y, z + 1, Library.NEG_X),
                new DirPos(x - 1, y, z + 2, Library.POS_Z),
                new DirPos(x + 1, y, z + 2, Library.POS_Z),
                new DirPos(x - 1, y, z - 2, Library.NEG_Z),
                new DirPos(x + 1, y, z - 2, Library.NEG_Z)
        };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        mode = tag.getShort("Mode");
        tank.readFromNBT(tag, "Tank");
        hasExploded = tag.getBoolean("Exploded");
        onFire = tag.getBoolean("OnFire");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putShort("Mode", mode);
        tank.writeToNBT(tag, "Tank");
        tag.putBoolean("Exploded", hasExploded);
        tag.putBoolean("OnFire", onFire);
    }

    @Override
    public long transferFluid(FluidType type, int pressure, long fluid) {
        long toTransfer = Math.min(getDemand(type, pressure), fluid);
        tank.setFill(tank.getFill() + (int) toTransfer);
        return fluid - toTransfer;
    }

    @Override
    public long getDemand(FluidType type, int pressure) {
        if (this.mode == 2 || this.mode == 3) return 0;
        if (tank.getPressure() != pressure) return 0;
        return type == tank.getTankType() ? tank.getMaxFill() - tank.getFill() : 0;
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] { tank };
    }

    @Override
    public void writeNBT(CompoundTag savedTag) {
        if (this.tank.getFill() == 0 && !this.hasExploded) return;
        CompoundTag tag = new CompoundTag();
        this.tank.writeToNBT(tag, "Tank");
        tag.putShort("Mode", mode);
        tag.putBoolean("HasExploded", hasExploded);
        tag.putBoolean("OnFire", onFire);
        savedTag.put(NBT_PERSISTENT_KEY, tag);
    }

    @Override
    public void readNBT(CompoundTag savedTag) {
        CompoundTag tag = savedTag.getCompound(NBT_PERSISTENT_KEY);
        this.tank.readFromNBT(tag, "Tank");
        this.mode = tag.getShort("Mode");
        this.hasExploded = tag.getBoolean("HasExploded");
        this.onFire = tag.getBoolean("OnFire");
    }

    @Override public boolean canConnect(FluidType fluid, Direction dir) { return true; }

    @Override
    public FluidTank[] getSendingTanks() {
        if (this.hasExploded) return FluidTank.EMPTY_ARRAY;
        return (mode == 1 || mode == 2) ? new FluidTank[] {tank} : FluidTank.EMPTY_ARRAY;
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        if (this.hasExploded) return FluidTank.EMPTY_ARRAY;
        return (mode == 0 || mode == 1) ? new FluidTank[] {tank} : FluidTank.EMPTY_ARRAY;
    }

    @Override
    public ConnectionPriority getFluidPriority() {
        return mode == 1 ? ConnectionPriority.LOW : ConnectionPriority.NORMAL;
    }

    @Override
    public int[] getFluidIDToCopy() {
        return new int[] {tank.getTankType().getID()};
    }

    @Override
    public FluidTank getTankToPaste() {
        return tank;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        if (player.isSpectator()) return null;
        return new MachineFluidTankMenu(id, inventory, this);
    }

    @Override
    public boolean isDamaged() {
        return this.hasExploded;
    }

    List<AStack> repair = new ArrayList<>();
    @Override
    public List<AStack> getRepairMaterials() {
        if (!repair.isEmpty()) return repair;
        repair.add(new ComparableStack(ModItems.NOTHING.get(), 6));
        return repair;
    }

    @Override
    public void repair() {
        this.hasExploded = false;
        this.markChanged();
    }

    @Override
    public void tryExtinguish(Level level, BlockPos pos, EnumExtinguishType type) {
        if (!this.hasExploded || !this.onFire) return;

        if (type == EnumExtinguishType.WATER) {
            if (tank.getTankType().hasTrait(FT_Liquid.class)) { // extinguishing oil with water is a terrible idea!
                level.explode(null, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 1.5, this.getBlockPos().getZ() + 0.5, 5F, Level.ExplosionInteraction.TNT);
            } else {
                this.onFire = false;
                this.markChanged();
                return;
            }
        }

        if (type == EnumExtinguishType.FOAM || type == EnumExtinguishType.CO2) {
            this.onFire = false;
            this.markChanged();
        }
    }

    @Override public boolean hasPermission(Player player) { return this.stillValid(player); }

    @Override
    public void receiveControl(CompoundTag tag) {
        if (tag.contains("Mode")) {
            mode = (short) ((mode + 1) % modes);
            this.markChanged();
        }
    }
}
