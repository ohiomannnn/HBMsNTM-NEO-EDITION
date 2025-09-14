package com.hbm.blockentity.machine;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.items.ModItems;
import com.hbm.items.tools.ItemKey;
import com.hbm.lib.ModSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class LockableBaseBlockEntity extends LoadedBaseBlockEntity {

    protected int lock;
    private boolean isLocked = false;
    protected double lockMod = 0.1D;

    public LockableBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void lock() {
        if(lock == 0) {
            HBMsNTM.LOGGER.error("A block has been set to locked state before setting pins, this should not happen and may cause errors! {}", this);
        }
        isLocked = true;
        setChanged();
    }

    public void setPins(int pins) { lock = pins; setChanged(); }
    public int getPins() { return lock; }
    public void setMod(double mod) { lockMod = mod; setChanged(); }
    public double getMod() { return lockMod; }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        lock = tag.getInt("lock");
        isLocked = tag.getBoolean("isLocked");
        lockMod  = tag.getDouble("lockMod");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("lock", lock);
        tag.putBoolean("isLocked", isLocked);
        tag.putDouble("lockMod", lockMod);
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);

        buf.writeInt(lock);
        buf.writeBoolean(isLocked);
        buf.writeDouble(lockMod);
    }

    public boolean canAccess(Player player) {
        if (!isLocked) return true;
        if (player == null) return false;

        ItemStack stack = player.getMainHandItem();

        if (!stack.isEmpty() && stack.getItem() instanceof ItemKey && ItemKey.getPins(stack) == this.lock) {
            level.playSound(null, getBlockPos(), ModSounds.LOCK_OPEN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        }

        if (!stack.isEmpty() && stack.is(ModItems.KEY_RED.get())) {
            level.playSound(null, getBlockPos(), ModSounds.LOCK_OPEN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        }

        return tryPick(player);
    }

    private boolean tryPick(Player player) {
        boolean canPick = false;
        ItemStack stack = player.getMainHandItem();
        double chanceOfSuccess = this.lockMod * 100;

        if (!stack.isEmpty() && stack.is(ModItems.PIN.get()) &&
                (player.getInventory().contains(new ItemStack(ModItems.SCREWDRIVER.get())) ||
                        player.getInventory().contains(new ItemStack(ModItems.SCREWDRIVER_DESH.get())))) {

            stack.shrink(1);
            canPick = true;
        }

        if (!stack.isEmpty() &&
                (stack.is(ModItems.SCREWDRIVER.get()) || stack.is(ModItems.SCREWDRIVER_DESH.get())) &&
                player.getInventory().contains(new ItemStack(ModItems.PIN.get()))) {

            removeOne(player, ModItems.PIN.get());
            canPick = true;
        }

        if (canPick) {
//            if (ArmorUtil.checkArmorPiece(player, ModItems.JACKT.get(), 2) ||
//                    ArmorUtil.checkArmorPiece(player, ModItems.JACKT2.get(), 2)) {
//                chanceOfSuccess *= 100D;
//            }

            double rand = player.level().random.nextDouble() * 100;

            if (chanceOfSuccess > rand) {
                level.playSound(null, getBlockPos(), ModSounds.PIN_UNLOCK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                return true;
            }

            level.playSound(null, getBlockPos(), ModSounds.PIN_BREAK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        return false;
    }

    private void removeOne(Player player, net.minecraft.world.item.Item item) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack invStack = player.getInventory().getItem(i);
            if (invStack.is(item)) {
                invStack.shrink(1);
                return;
            }
        }
    }
}
