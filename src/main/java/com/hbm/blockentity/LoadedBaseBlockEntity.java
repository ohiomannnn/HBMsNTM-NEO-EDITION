package com.hbm.blockentity;

import api.hbm.tile.ILoadedTile;
import com.hbm.network.toclient.BufNT;
import com.hbm.sound.AudioWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public class LoadedBaseBlockEntity extends BlockEntity implements ILoadedTile, IBufPacketReceiver {

    public boolean isLoaded = true;
    public boolean muffled = false;

    public LoadedBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        this.isLoaded = false;
    }

    public AudioWrapper createAudioLoop() { return null; }

    public AudioWrapper rebootAudio(AudioWrapper wrapper) {
        wrapper.stopSound();
        AudioWrapper audio = createAudioLoop();
        audio.startSound();
        return audio;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.muffled = tag.getBoolean("muffled");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("muffled", muffled);
    }

    public float getVolume(float baseVolume) {
        return muffled ? baseVolume * 0.1F : baseVolume;
    }

    private ByteBuf lastPackedBuf;

    @Override
    public void serialize(ByteBuf buf) {
        buf.writeBoolean(muffled);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        this.muffled = buf.readBoolean();
    }

    public void networkPackNT(int range) {
        if (level.isClientSide) return;

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        this.serialize(buf);

        BufNT packet = new BufNT(worldPosition, buf);

        if (buf.equals(lastPackedBuf) && level.getGameTime() % 20 != 0) return;
        lastPackedBuf = new FriendlyByteBuf(buf.copy());

        PacketDistributor.sendToPlayersNear(
                (ServerLevel) level,
                null,
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5,
                range,
                packet
        );
    }
}
