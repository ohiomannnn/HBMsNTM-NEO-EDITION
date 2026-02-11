package com.hbm.blockentity.machine.storage;

import com.hbm.HBMsNTMClient;
import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.menus.BatteryREDDMenu;
import com.hbm.lib.Library;
import com.hbm.lib.ModSounds;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.fauxpointtwelve.DirPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.math.BigInteger;

public class BatteryREDDBlockEntity extends BatteryBaseBlockEntity implements IPersistentNBT {

    public float prevRotation = 0F;
    public float rotation = 0F;

    public BigInteger[] log = new BigInteger[20];
    public BigInteger delta = BigInteger.valueOf(0);

    public BigInteger power = BigInteger.valueOf(0);

    private AudioWrapper audio;

    public BatteryREDDBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.BATTERY_REDD.get(), pos, blockState, 2);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.batteryREDD"); }

    @Override
    public void updateEntity() {
        BigInteger prevPower = new BigInteger(power.toByteArray());
        super.updateEntity();

        if (!level.isClientSide) {

            long toAdd = Library.chargeTEFromItems(slots, 0, 0, this.getMaxPower());
            if (toAdd > 0) this.power = this.power.add(BigInteger.valueOf(toAdd));

            long toRemove = this.getPower() - Library.chargeItemsFromTE(slots, 1, this.getPower(), this.getMaxPower());
            if (toRemove > 0)this.power = this.power.subtract(BigInteger.valueOf(toRemove));

            // same implementation as for batteries, however retooled to use bigints because fuck
            BigInteger avg = this.power.add(prevPower).divide(BigInteger.valueOf(2));
            this.delta = avg.subtract(this.log[0] == null ? BigInteger.ZERO : this.log[0]);

            for (int i = 1; i < this.log.length; i++) {
                this.log[i - 1] = this.log[i];
            }

            this.log[19] = avg;

        } else {
            this.prevRotation = this.rotation;
            this.rotation += this.getSpeed();

            if (rotation >= 360) {
                rotation -= 360;
                prevRotation -= 360;
            }

            float pitch = 0.5F + this.getSpeed() / 15F * 1.5F;

            if (this.prevRotation != this.rotation && HBMsNTMClient.me().distanceToSqr(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 5.5, this.getBlockPos().getZ() + 0.5) < 30 * 30) {
                if (this.audio == null || !this.audio.isPlaying()) {
                    this.audio = HBMsNTMClient.getLoopedSound(ModSounds.FENSU_HUM.get(), SoundSource.AMBIENT, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), this.getVolume(1.5F), 25F, pitch, 5);
                    this.audio.startSound();
                }

                this.audio.updateVolume(this.getVolume(1.5F));
                this.audio.updatePitch(pitch);
                this.audio.keepAlive();

            } else {
                if (this.audio != null) {
                    this.audio.stopSound();
                    this.audio = null;
                }
            }
        }
    }

    public static void tick(Level ignored, BlockPos ignored1, BlockState ignored2, BatteryREDDBlockEntity be) { be.updateEntity(); }

    public float getSpeed() {
        return (float) Math.min(Math.pow(Math.log(this.power.doubleValue() * 0.05 + 1) * 0.05F, 5), 15F);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if (audio != null) {
            audio.stopSound();
            audio = null;
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if (audio != null) {
            audio.stopSound();
            audio = null;
        }
    }

    @Override
    public void serialize(ByteBuf buf, RegistryAccess registryAccess) {
        super.serialize(buf, registryAccess);

        byte[] array0 = this.power.toByteArray();
        buf.writeInt(array0.length);
        for (byte b : array0) buf.writeByte(b);

        byte[] array1 = this.delta.toByteArray();
        buf.writeInt(array1.length);
        for (byte b : array1) buf.writeByte(b);
    }

    @Override
    public void deserialize(ByteBuf buf, RegistryAccess registryAccess) {
        super.deserialize(buf, registryAccess);

        byte[] array0 = new byte[buf.readInt()];
        for (int i = 0 ; i < array0.length; i++) array0[i] = buf.readByte();
        this.power = new BigInteger(array0);

        byte[] array1 = new byte[buf.readInt()];
        for (int i = 0 ; i < array1.length; i++) array1[i] = buf.readByte();
        this.delta = new BigInteger(array1);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.power = new BigInteger(tag.getByteArray("Power"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putByteArray("Power", this.power.toByteArray());
    }

    @Override
    public BlockPos[] getPortPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise();

        BlockPos pos = this.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int dirX = dir.getStepX();
        int dirZ = dir.getStepZ();
        int rotX = rot.getStepX();
        int rotZ = rot.getStepZ();

        return new BlockPos[] {
                new BlockPos(x + dirX * 2 + rotX * 2, y, z + dirZ * 2 + rotZ * 2),
                new BlockPos(x + dirX * 2 - rotX * 2, y, z + dirZ * 2 - rotZ * 2),
                new BlockPos(x - dirX * 2 + rotX * 2, y, z - dirZ * 2 + rotZ * 2),
                new BlockPos(x - dirX * 2 - rotX * 2, y, z - dirZ * 2 - rotZ * 2),
                new BlockPos(x + rotZ * 4, y, z + rotZ * 4),
                new BlockPos(x - rotZ * 4, y, z - rotZ * 4),
        };
    }

    @Override
    public DirPos[] getConPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise();

        BlockPos pos = this.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int dirX = dir.getStepX();
        int dirZ = dir.getStepZ();
        int rotX = rot.getStepX();
        int rotZ = rot.getStepZ();

        return new DirPos[] {
                new DirPos(x + dirX * 3 + rotX * 2, y, z + dirZ * 3 + rotZ * 2, dir),
                new DirPos(x + dirX * 3 - rotX * 2, y, z + dirZ * 3 - rotZ * 2, dir),
                new DirPos(x - dirX * 3 + rotX * 2, y, z - dirZ * 3 + rotZ * 2, dir.getOpposite()),
                new DirPos(x - dirX * 3 - rotX * 2, y, z - dirZ * 3 - rotZ * 2, dir.getOpposite()),
                new DirPos(x + rotX * 5, y, z + rotZ * 5, rot),
                new DirPos(x - rotX * 5, y, z - rotZ * 5, rot.getOpposite()),
        };
    }

    @Override
    public void usePower(long power) {
        this.power = this.power.subtract(BigInteger.valueOf(power));
    }

    @Override
    public long transferPower(long power) {
        this.power = this.power.add(BigInteger.valueOf(power));
        return 0L;
    }

    @Override public long getPower() { return this.power.min(BigInteger.valueOf(getMaxPower() / 2)).longValue(); } // for provision
    @Override public void setPower(long power) { } // not needed since we use transferPower and usePower directly
    @Override public long getMaxPower() { return Long.MAX_VALUE / 100L; } // for connection speed

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BatteryREDDMenu(id, inventory, this);
    }

    @Override
    public void writeNBT(CompoundTag savedTag) {
        CompoundTag tag = new CompoundTag();
        tag.putByteArray("Power", this.power.toByteArray());
        tag.putBoolean("Muffled", muffled);
        savedTag.put(NBT_PERSISTENT_KEY, tag);
    }

    @Override
    public void readNBT(CompoundTag savedTag) {
        CompoundTag tag = savedTag.getCompound(NBT_PERSISTENT_KEY);
        this.power = new BigInteger(tag.getByteArray("Power"));
        this.muffled = tag.getBoolean("Muffled");
    }
}
