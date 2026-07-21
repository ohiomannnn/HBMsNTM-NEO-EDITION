package com.hbm.blockentity.machine.storage;

import api.hbm.fluidmk2.FluidNode;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.machine.BarrelBlock;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.BarrelMenu;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.uninos.UniNodespace;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BarrelBlockEntity extends MachineBaseBlockEntity implements IFluidStandardTransceiverMK2, IPersistentNBT, IFluidCopiable, IControlReceiver {

    private static final int INV_SIZE = 6;
    private static final int MODES = 4;

    protected FluidNode node;
    public final FluidTank tank;
    public short mode = 0;
    private byte lastRedstone = 0;
    private FluidType lastType = Fluids.NONE;
    private boolean shouldDrop = true;
    private final int capacity;

    public BarrelBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.BARREL.get(), pos, state, INV_SIZE);

        this.capacity = state.getBlock() instanceof BarrelBlock barrel ? barrel.getCapacity() : 12_000;
        this.tank = new FluidTank(Fluids.NONE, this.capacity);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.barrel");
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        this.checkTilt(com.hbm.blockentity.LoadedBaseBlockEntity.TiltType.CONFIG, false);

        if(this.tank.setType(0, 1, this.slots)) {
            this.setChanged();
        }
        this.tank.loadTank(this.level, 2, 3, this.slots);
        this.tank.unloadTank(this.level, 4, 5, this.slots);

        byte comp = this.getComparatorPower();
        if(comp != this.lastRedstone) {
            this.setChanged();
            for(DirPos pos : this.getConPos()) {
                this.level.updateNeighbourForOutputSignal(pos.makeCompat(), this.getBlockState().getBlock());
            }
        }
        this.lastRedstone = comp;

        if(this.mode == 1) {
            if(this.node == null || this.node.expired || this.tank.getTankType() != this.lastType) {
                if(this.node != null) {
                    this.destroyNode();
                }

                FluidNode existing = (FluidNode) UniNodespace.getNode(this.level, this.getBlockPos(), this.tank.getTankType().getNetworkProvider());
                if(existing == null || existing.expired) {
                    this.node = this.createNode(this.tank.getTankType());
                    UniNodespace.createNode(this.level, this.node);
                } else {
                    this.node = existing;
                }
                this.lastType = this.tank.getTankType();
            }

            if(this.node != null && this.node.hasValidNet()) {
                this.node.net.addProvider(this);
                this.node.net.addReceiver(this);
            }
        } else {
            this.destroyNode();

            if(!this.tilted) {
                for(DirPos pos : this.getConPos()) {
                    if(this.mode == 2) {
                        this.tryProvide(this.tank, this.level, pos);
                    } else {
                        this.trySubscribe(this.tank.getTankType(), this.level, pos);
                    }
                }
            }
        }

        if(this.tank.getFill() > 0) {
            this.checkFluidInteraction();
        }

        this.networkPackNT(25);
    }

    private void destroyNode() {
        if(this.level == null) return;
        if(this.node != null) {
            UniNodespace.destroyNode(this.level, this.node);
            this.node = null;
            this.lastType = Fluids.NONE;
        }
    }

    protected FluidNode createNode(FluidType type) {
        DirPos[] conPos = this.getConPos();

        java.util.HashSet<BlockPos> posSet = new java.util.HashSet<>();
        posSet.add(this.getBlockPos());
        for(DirPos pos : conPos) {
            Direction dir = pos.getDir();
            posSet.add(new BlockPos(pos.getX() - dir.getStepX(), pos.getY() - dir.getStepY(), pos.getZ() - dir.getStepZ()));
        }

        return new FluidNode(type.getNetworkProvider(), posSet.toArray(new BlockPos[0])).setConnections(conPos);
    }

    private void checkFluidInteraction() {
        if(this.level == null || this.level.isClientSide) return;

        Block block = this.getBlockState().getBlock();

        if(this.tank.getTankType().isAntimatter()) {
            this.shouldDrop = false;
            this.level.destroyBlock(this.getBlockPos(), false);
            this.level.explode(null, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, 5F, net.minecraft.world.level.Level.ExplosionInteraction.TNT);
            return;
        }

        if(block == NtmBlocks.BARREL_PLASTIC.get() && (this.tank.getTankType().isHot() || this.tank.getTankType().isCorrosive())) {
            this.shouldDrop = false;
            this.level.destroyBlock(this.getBlockPos(), false);
            this.level.playSound(null, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    public DirPos[] getConPos() {
        return new DirPos[] {
                new DirPos(this.getBlockPos().getX() + 1, this.getBlockPos().getY(), this.getBlockPos().getZ(), Direction.EAST),
                new DirPos(this.getBlockPos().getX() - 1, this.getBlockPos().getY(), this.getBlockPos().getZ(), Direction.WEST),
                new DirPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 1, this.getBlockPos().getZ(), Direction.UP),
                new DirPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ(), Direction.DOWN),
                new DirPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + 1, Direction.SOUTH),
                new DirPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - 1, Direction.NORTH)
        };
    }

    public byte getComparatorPower() {
        if(this.tank.getFill() <= 0) return 0;
        double frac = (double) this.tank.getFill() / (double) this.tank.getMaxFill() * 15D;
        return (byte) Math.clamp((int) Math.ceil(frac), 0, 15);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return switch(slot) {
            case 0, 1 -> stack.getItem() instanceof IItemFluidIdentifier;
            case 2 -> !FluidContainerRegistry.getFullContainer(stack, this.tank.getTankType()).isEmpty();
            case 4 -> FluidContainerRegistry.getFluidContent(stack, this.tank.getTankType()) > 0;
            default -> true;
        };
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return switch(slot) {
            case 1, 3, 5 -> true;
            default -> !this.canPlaceItem(slot, stack);
        };
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return switch(direction.get3DDataValue()) {
            case 0 -> new int[] {3, 5};
            case 1 -> new int[] {2};
            default -> new int[] {4};
        };
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.distanceToSqr(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5) <= 256.0D;
    }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("Mode")) {
            this.mode = (short) ((this.mode + 1) % MODES);
            this.setChanged();
        }
    }

    @Override
    public long getDemand(FluidType type, int pressure) {
        if(this.tilted || this.mode == 2 || this.mode == 3) return 0;
        if(this.tank.getPressure() != pressure) return 0;
        return type == this.tank.getTankType() ? this.tank.getMaxFill() - this.tank.getFill() : 0;
    }

    @Override
    public long transferFluid(FluidType type, int pressure, long fluid) {
        long toTransfer = Math.min(this.getDemand(type, pressure), fluid);
        this.tank.setFill(this.tank.getFill() + (int) toTransfer);
        this.setChanged();
        return fluid - toTransfer;
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return (this.mode == 0 || this.mode == 1) ? new FluidTank[] { this.tank } : FluidTank.EMPTY_ARRAY;
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return (this.mode == 1 || this.mode == 2) ? new FluidTank[] { this.tank } : FluidTank.EMPTY_ARRAY;
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] { this.tank };
    }

    @Override
    public int[] getFluidIDToCopy() {
        return new int[] { this.tank.getTankType().getID() };
    }

    @Override
    public FluidTank getTankToPaste() {
        return this.tank;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BarrelMenu(id, inventory, this);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.mode = tag.getShort("mode");
        this.tank.readFromNBT(tag, "tank");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putShort("mode", this.mode);
        this.tank.writeToNBT(tag, "tank");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeShort(this.mode);
        this.tank.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.mode = buf.readShort();
        this.tank.deserialize(buf);
    }

    @Override
    public void writeNBT(CompoundTag savedTag) {
        if(this.tank.getFill() <= 0) return;
        CompoundTag tag = new CompoundTag();
        tag.putShort("mode", this.mode);
        this.tank.writeToNBT(tag, "tank");
        savedTag.put(NBT_PERSISTENT_KEY, tag);
    }

    @Override
    public void readNBT(CompoundTag savedTag) {
        CompoundTag tag = savedTag.getCompound(NBT_PERSISTENT_KEY);
        this.mode = tag.getShort("mode");
        this.tank.readFromNBT(tag, "tank");
    }

    public boolean shouldDrop() {
        return this.shouldDrop;
    }

    public void preventDrop() {
        this.shouldDrop = false;
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        this.destroyNode();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.destroyNode();
    }

    @Override
    public int getFloorCount() {
        return 1;
    }

    @Override
    public BlockPos getFloorPosFromIndex(int index) {
        return index == 0 ? this.getBlockPos().below() : null;
    }
}
