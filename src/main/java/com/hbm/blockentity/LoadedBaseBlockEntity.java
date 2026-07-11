package com.hbm.blockentity;

import api.hbm.blockentity.ILoadedBE;
import com.hbm.network.toclient.BufPacket;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.SoundUtils;
import com.hbm.util.fauxpointtwelve.BlockPosNT;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.connection.ConnectionType;

import java.util.Arrays;

public class LoadedBaseBlockEntity extends BlockEntity implements ILoadedBE, IBufPacketReceiver {

    public boolean isLoaded = true;
    public boolean muffled = false;
    public boolean tilted = false;
    public int tiltBlocksChecked = 0;
    public int tiltBlocksValid = 0;

    public LoadedBaseBlockEntity(BlockEntityType<? extends LoadedBaseBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public void onChunkUnloaded() {
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
        this.tilted = tag.getBoolean("tilted");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putBoolean("muffled", muffled);
        tag.putBoolean("tilted", tilted);
    }

    public float getVolume(float baseVolume) {
        return muffled ? baseVolume * 0.1F : baseVolume;
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(muffled);
        buf.writeBoolean(tilted);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        this.muffled = buf.readBoolean();
        this.tilted = buf.readBoolean();
    }

    private byte[] lastPacketData;

    /** Sends a sync packet that uses ByteBuf for efficient information-cramming */
    public void networkPackNT(int range) {
        if(this.level == null || this.level.isClientSide) return;

        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), this.level.registryAccess(), ConnectionType.OTHER);
        this.serialize(buf);
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        buf.release();

        // Don't send unnecessary packets, except for maybe one every second or so.
        // If we stop sending duplicate packets entirely, this causes issues when
        // a client unloads and then loads back a chunk with an unchanged tile entity.
        // For that client, the tile entity will appear default until anything changes about it.
        // In my testing, this can be reliably reproduced with a full fluid barrel, for instance.
        // I think it might be fixable by doing something with getDescriptionPacket() and onDataPacket(),
        // but this sidesteps the problem for the mean time.
        if(Arrays.equals(data, lastPacketData) && level.getGameTime() % 20 != 0) return;

        this.lastPacketData = data;

        if(this.level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersNear(serverLevel, null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), range, new BufPacket(worldPosition, data));
        }
    }

    public enum TiltType {
        UNAVOIDABLE, CONFIG;
    }

    public void checkTilt(TiltType type, boolean extraHeavy) {
        if(this.level == null) return;
        boolean doesTilt = false;
        if(type == TiltType.UNAVOIDABLE) doesTilt = true;
//        if(cfg == TiltType.CONFIG && GeneralConfig.enableMachineGravity) doesTilt = true;
//        if(cfg == TiltType.CONFIG && GeneralConfig.enable528MachineGravity) doesTilt = true;

        if(!doesTilt) { this.tilted = false; return; }
        if(this.getFloorCount() <= 0) { this.tilted = false; return; }
        if((this.level.getGameTime() + BlockPosNT.getIdentity(this.getBlockPos())) % 20 != 0) return;

        if(this.tiltBlocksChecked >= this.getFloorCount()) {

            if(this.tiltBlocksValid >= this.tiltBlocksChecked * 0.95) {
                this.tilted = false;
            } else {
                if(!this.tilted) SoundUtils.playAtVec3(this.level, Vec3.atCenterOf(this.getBlockPos()), NtmSoundEvents.METAL_IMPACT.get(), SoundSource.BLOCKS);
                this.tilted = true;
            }

            this.setChanged();
            this.tiltBlocksChecked = 0;
            this.tiltBlocksValid = 0;
        }

        BlockPos pos = this.getFloorPosFromIndex(this.tiltBlocksChecked);
        if(pos == null) return;

        BlockState groundState = level.getBlockState(pos);
        Block ground = groundState.getBlock();
        this.tiltBlocksChecked++;

        // for extra heavy machines, the ground needs to:
        // * be a fully solid block (side UP is checked for custom behavior)
        // * be opaque
        // * NOT be sand, cloth or ground material
        // * have an explosion resistance of stone or greater
        if(extraHeavy) {
            if(!groundState.isFaceSturdy(level, pos, Direction.UP)) return;
            if(!groundState.isCollisionShapeFullBlock(level, pos)) return;
            // todo materials
            if(ground.getExplosionResistance() < Blocks.STONE.getExplosionResistance()) return;
            this.tiltBlocksValid++;
        // for standard machines, the ground needs to:
        // * be solid at the top
        // * NOT be sand
        } else {
            if(!groundState.isFaceSturdy(level, pos, Direction.UP)) return;
            // todo materials
            //if(ground == ModBlocks.dirt_dead || ground == ModBlocks.dirt_oily || ground == ModBlocks.stone_cracked) return;
            this.tiltBlocksValid++;
        }
    }

    public int getFloorCount() { return 0; }
    public BlockPos getFloorPosFromIndex(int index) { return null; }

    public BlockPos standardFloor3x3(int index) {
        return this.getBlockPos().offset(-1 + (index / 2) * 2, -1, -1 + (index % 2) * 2);
    }
    public BlockPos standardFloor5x5(int index) {
        return this.getBlockPos().offset(-2 + (index / 3) * 2, -1, -2 + (index % 3) * 2);
    }
    public BlockPos standardFloor7x7(int index) {
        return this.getBlockPos().offset(-3 + (index / 4) * 2, -1, -3 + (index % 4) * 2);
    }
}
