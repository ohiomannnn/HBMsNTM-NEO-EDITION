package com.hbm.blockentity.machine.heater;

import api.hbm.tile.IHeatSource;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.IPersistentNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public abstract class AbstractHeaterMachineBlockEntity extends MachineBaseBlockEntity implements IHeatSource, IPersistentNBT {

    public int heatEnergy;

    protected AbstractHeaterMachineBlockEntity(BlockEntityType<? extends MachineBaseBlockEntity> type, BlockPos pos, BlockState state, int slots) {
        super(type, pos, state, slots);
    }

    protected abstract int getMaxHeat();

    protected int getRenderHeight() {
        return 1;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return null;
    }

    @Override
    public int getHeatStored() {
        return this.heatEnergy;
    }

    @Override
    public void useUpHeat(int heat) {
        if(heat <= 0) return;
        this.heatEnergy = Math.max(this.heatEnergy - heat, 0);
        this.setChanged();
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        int[] slots = new int[this.getContainerSize()];
        for(int i = 0; i < slots.length; i++) slots[i] = i;
        return slots;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, net.minecraft.world.item.ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.heatEnergy = tag.getInt("heatEnergy");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("heatEnergy", this.heatEnergy);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.heatEnergy);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.heatEnergy = buf.readInt();
    }

    public AABB getRenderBoundingBox() {
        return new AABB(
                this.getBlockPos().getX() - 1,
                this.getBlockPos().getY(),
                this.getBlockPos().getZ() - 1,
                this.getBlockPos().getX() + 2,
                this.getBlockPos().getY() + this.getRenderHeight(),
                this.getBlockPos().getZ() + 2
        );
    }

    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

    @Override
    public void writeNBT(CompoundTag savedTag) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("heatEnergy", this.heatEnergy);
        savedTag.put(NBT_PERSISTENT_KEY, tag);
    }

    @Override
    public void readNBT(CompoundTag savedTag) {
        CompoundTag tag = savedTag.getCompound(NBT_PERSISTENT_KEY);
        this.heatEnergy = tag.getInt("heatEnergy");
    }

    protected void clearArea(int radiusX, int radiusY, int radiusZ) {
        if(this.level == null || this.level.isClientSide) return;

        BlockPos center = this.getBlockPos();
        for(int dx = -radiusX; dx <= radiusX; dx++) {
            for(int dy = -radiusY; dy <= radiusY; dy++) {
                for(int dz = -radiusZ; dz <= radiusZ; dz++) {
                    BlockPos pos = center.offset(dx, dy, dz);
                    if(this.level.getBlockState(pos).getBlock() == this.getBlockState().getBlock()) {
                        this.level.removeBlock(pos, false);
                    }
                }
            }
        }
    }
}
