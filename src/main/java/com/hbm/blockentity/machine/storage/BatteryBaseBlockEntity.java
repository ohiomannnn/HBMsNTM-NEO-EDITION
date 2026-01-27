package com.hbm.blockentity.machine.storage;

import api.hbm.energymk2.*;
import api.hbm.energymk2.Nodespace.PowerNode;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.interfaces.ICopiable;
import com.hbm.uninos.UniNodespace;
import com.hbm.util.Compat;
import com.hbm.util.EnumUtil;
import com.hbm.util.fauxpointtwelve.DirPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BatteryBaseBlockEntity extends MachineBaseBlockEntity implements IEnergyConductorMK2, IEnergyProviderMK2, IEnergyReceiverMK2, IControlReceiver, ICopiable {

    public byte lastRedstone = 0;
    public long prevPowerState = 0;

    public static final int mode_input = 0;
    public static final int mode_buffer = 1;
    public static final int mode_output = 2;
    public static final int mode_none = 3;
    public short redLow = 0;
    public short redHigh = 2;
    public ConnectionPriority priority = ConnectionPriority.LOW;

    protected PowerNode node;

    public BatteryBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int size) {
        super(type, pos, blockState, size);
    }

    @Override
    public void updateEntity() {
        if (level != null && !level.isClientSide) {
            if (priority == null || priority.ordinal() == 0 || priority.ordinal() == 4) {
                priority = ConnectionPriority.LOW;
            }

            if (this.node == null || this.node.expired) {
                this.node = (PowerNode) UniNodespace.getNode(level, this.getBlockPos(), Nodespace.THE_POWER_PROVIDER);

                if (this.node == null || this.node.expired) {
                    this.node = this.createNode();
                    UniNodespace.createNode(level, this.node);
                }
            }

            if (this.node != null && this.node.hasValidNet()) {
                switch (this.getRelevantMode(false)) {
                    case mode_input -> {
                        this.node.net.removeProvider(this);
                        this.node.net.addReceiver(this);
                    }
                    case mode_output -> {
                        this.node.net.addProvider(this);
                        this.node.net.removeReceiver(this);
                    }
                    case mode_buffer -> {
                        this.node.net.addProvider(this);
                        this.node.net.addReceiver(this);
                    }
                    case mode_none -> {
                        this.node.net.removeProvider(this);
                        this.node.net.removeReceiver(this);
                    }
                }
            }

            byte comp = this.getComparatorPower();
            if (comp != this.lastRedstone) {
                for (BlockPos port : this.getPortPos()) {
                    BlockEntity be = Compat.getBlockEntityStandard(level, port);
                    if (be != null) be.setChanged();
                }
            }

            this.lastRedstone = comp;

            prevPowerState = this.getPower();

            this.networkPackNT(100);
        }
    }

    public byte getComparatorPower() {
        double frac = (double) this.getPower() / (double) Math.max(this.getMaxPower(), 1) * 15D;
        return (byte) (Mth.clamp((int) Math.round(frac), 0, 15)); //to combat eventual rounding errors with the FEnSU's stupid maxPower
    }

    @Override
    public PowerNode createNode() {
        return new PowerNode(this.getPortPos()).setConnections(this.getConPos());
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if (this.level != null && !this.level.isClientSide) {
            if (this.node != null) {
                UniNodespace.destroyNode(level, this.getBlockPos(), Nodespace.THE_POWER_PROVIDER);
            }
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.getItem() instanceof IBatteryItem;
    }

    @Override
    public void serialize(ByteBuf buf, RegistryAccess registryAccess) {
        super.serialize(buf, registryAccess);

        buf.writeShort(redLow);
        buf.writeShort(redHigh);
        buf.writeByte(priority.ordinal());
    }

    @Override
    public void deserialize(ByteBuf buf, RegistryAccess registryAccess) {
        super.deserialize(buf, registryAccess);

        redLow = buf.readShort();
        redHigh = buf.readShort();
        priority = EnumUtil.grabEnumSafely(ConnectionPriority.class, buf.readByte());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.redLow = tag.getShort("RedLow");
        this.redHigh = tag.getShort("RedHigh");
        this.lastRedstone = tag.getByte("LastRedstone");
        this.priority = EnumUtil.grabEnumSafely(ConnectionPriority.class, tag.getByte("Priority"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putShort("RedLow", redLow);
        tag.putShort("RedHigh", redHigh);
        tag.putByte("LastRedstone", lastRedstone);
        tag.putByte("Priority", (byte) this.priority.ordinal());
    }

    @Override public boolean allowDirectProvision() { return false; }
    @Override public ConnectionPriority getPriority() { return this.priority; }

    public abstract BlockPos[] getPortPos();
    public abstract DirPos[] getConPos();

    private short modeCache = 0;

    public short getRelevantMode(boolean useCache) {
        if (useCache) return this.modeCache;
        boolean powered = false;
        for (BlockPos pos : getPortPos()) if (level != null && level.hasNeighborSignal(pos)) { powered = true; break; }
        this.modeCache = powered ? this.redHigh : this.redLow;
        return this.modeCache;
    }

    @Override public boolean hasPermission(Player player) { return this.stillValid(player); }

    @Override
    public void receiveControl(CompoundTag tag) {
        if (tag.contains("low")) {
            this.redLow++;
            if (this.redLow > 3) this.redLow = 0;
        }
        if (tag.contains("high")) {
            this.redHigh++;
            if (this.redHigh > 3) this.redHigh = 0;
        }
        if (tag.contains("priority")) {
            int ordinal = this.priority.ordinal();
            ordinal++;
            if (ordinal > ConnectionPriority.HIGH.ordinal()) ordinal = ConnectionPriority.LOW.ordinal();
            this.priority = EnumUtil.grabEnumSafely(ConnectionPriority.class, ordinal);
        }
    }

    @Override
    public CompoundTag getSettings(Level level, BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putShort("redLow", redLow);
        tag.putShort("redHigh", redHigh);
        tag.putByte("priority", (byte) this.priority.ordinal());
        return null;
    }

    @Override
    public void pasteSettings(CompoundTag tag, int index, Level level, Player player, BlockPos pos) {
        if (tag.contains("redLow")) this.redLow = tag.getShort("redLow");
        if (tag.contains("redHigh")) this.redHigh = tag.getShort("redHigh");
        if (tag.contains("priority")) this.priority = EnumUtil.grabEnumSafely(ConnectionPriority.class, tag.getByte("priority"));
    }
}
