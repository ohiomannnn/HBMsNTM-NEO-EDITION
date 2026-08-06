package com.hbm.blockentity.network;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.interfaces.IControlReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RadioTorchBaseBlockEntity extends LoadedBaseBlockEntity implements ITickable, IControlReceiver {

    /** channel we're broadcasting on/listening to */
    public String channel = "";
    /** previous redstone state for input/output, needed for state change detection */
    public int lastState = 0;
    /** last update tick, needed for receivers listening for changes */
    public long lastUpdate;
    /** switches state change mode to tick-based polling */
    public boolean polling = false;
    /** switches redstone passthrough to custom signal mapping */
    public boolean customMap = false;
    /** custom mapping */
    public String[] mapping = new String[16];

    public RadioTorchBaseBlockEntity(BlockEntityType<? extends RadioTorchBaseBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {

            this.networkPackNT(50);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.polling = tag.getBoolean("p");
        this.customMap = tag.getBoolean("m");
        this.lastState = tag.getInt("l");
        this.lastUpdate = tag.getLong("u");
        this.channel = tag.getString("c");
        for(int i = 0; i < 16; i++) this.mapping[i] = tag.getString("m" + i);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putBoolean("p", polling);
        tag.putBoolean("m", customMap);
        tag.putInt("l", lastState);
        tag.putLong("u", lastUpdate);
        if(channel != null) tag.putString("c", channel);
        for(int i = 0; i < 16; i++) if(mapping[i] != null) tag.putString("m" + i, mapping[i]);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {

        buf.writeBoolean(this.polling);
        buf.writeBoolean(this.customMap);
        buf.writeUtf(this.channel);
        for(int i = 0; i < 16; i++) if(mapping[i] != null) buf.writeUtf(this.mapping[i]);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {

        this.polling = buf.readBoolean();
        this.customMap = buf.readBoolean();
        this.channel = buf.readUtf();
        for(int i = 0; i < 16; i++) this.mapping[i] = buf.readUtf();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.putByte("l", (byte) this.lastState);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        int last = this.lastState;
        this.lastState = tag.getByte("l");
        if(this.lastState != last && level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }


    @Override
    public boolean hasPermission(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void receiveControl(CompoundTag data) {
        if(data.contains("p")) this.polling = data.getBoolean("p");
        if(data.contains("m")) this.customMap = data.getBoolean("m");
        if(data.contains("c")) this.channel = data.getString("c");
        for(int i = 0; i < 16; i++) if(data.contains("m" + i)) this.mapping[i] = data.getString("m" + i);

        this.setChanged();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
