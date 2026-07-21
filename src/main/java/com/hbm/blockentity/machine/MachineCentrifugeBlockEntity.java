package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyReceiverMK2;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.menus.MachineCentrifugeMenu;
import com.hbm.inventory.recipes.CentrifugeRecipes;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.List;

public class MachineCentrifugeBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IUpgradeInfoProvider {

    public static final int MAX_POWER = 100_000;
    public static final int PROCESSING_SPEED = 200;
    public static final int BASE_CONSUMPTION = 200;

    private static final int SLOT_INPUT = 0;
    private static final int SLOT_BATTERY = 1;
    private static final int SLOT_OUTPUT_START = 2;
    private static final int SLOT_OUTPUT_END = 6;
    private static final int SLOT_UPGRADE_START = 6;
    private static final int SLOT_UPGRADE_END = 7;
    private static final int[] ACCESSIBLE_SLOTS = new int[] { 0, 2, 3, 4, 5 };

    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);
    public long power;
    public int progress;
    public boolean isProgressing;

    private AudioWrapper audio;
    private AABB renderBox;

    public MachineCentrifugeBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_CENTRIFUGE.get(), pos, state, 8);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_centrifuge");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(this.level.isClientSide) {
            this.updateAudio();
            return;
        }

        for(DirPos dirPos : this.getConPos()) {
            this.trySubscribe(this.level, dirPos);
        }

        this.power = Library.chargeTEFromItems(this.slots, SLOT_BATTERY, this.getPower(), this.getMaxPower());
        this.upgradeManager.checkSlots(this.slots, SLOT_UPGRADE_START, SLOT_UPGRADE_END);

        int speedLevel = this.upgradeManager.getLevel(UpgradeType.SPEED);
        int powerLevel = this.upgradeManager.getLevel(UpgradeType.POWER);
        int overdriveLevel = this.upgradeManager.getLevel(UpgradeType.OVERDRIVE);

        int speed = 1 + speedLevel;
        if(overdriveLevel > 0) {
            speed *= 1 + overdriveLevel * 5;
        }

        long consumption = BASE_CONSUMPTION + (long) speedLevel * BASE_CONSUMPTION + (long) overdriveLevel * BASE_CONSUMPTION * 50L;
        consumption /= 1L + powerLevel;
        consumption = Math.max(consumption, 1L);

        if(this.getPower() >= consumption && this.canProcess()) {
            this.power -= consumption;
            this.progress += speed;
            this.isProgressing = true;

            if(this.progress >= PROCESSING_SPEED) {
                this.progress = 0;
                this.processItem();
            }
        } else {
            this.progress = 0;
            this.isProgressing = false;
        }

        this.power = Math.max(0L, Math.min(this.power, this.getMaxPower()));
        this.networkPackNT(25);
    }

    private void updateAudio() {
        if(this.isProgressing) {
            if(this.audio == null) {
                this.audio = this.createAudioLoop();
                if(this.audio != null) {
                    this.audio.startSound();
                }
            } else if(!this.audio.isPlaying()) {
                this.audio = this.rebootAudio(this.audio);
            }

            if(this.audio != null) {
                this.audio.keepAlive();
            }
        } else if(this.audio != null) {
            this.audio.stopSound();
            this.audio = null;
        }
    }

    private boolean canProcess() {
        ItemStack[] outputs = CentrifugeRecipes.getOutput(this.slots.get(SLOT_INPUT));
        if(outputs.length == 0) return false;

        ItemStack[] simulated = new ItemStack[SLOT_OUTPUT_END - SLOT_OUTPUT_START];
        for(int i = 0; i < simulated.length; i++) {
            simulated[i] = this.slots.get(SLOT_OUTPUT_START + i).copy();
        }

        for(ItemStack output : outputs) {
            if(output.isEmpty()) continue;
            if(!insertIntoSimulation(simulated, output.copy())) return false;
        }

        return true;
    }

    private static boolean insertIntoSimulation(ItemStack[] simulated, ItemStack stack) {
        for(int i = 0; i < simulated.length && !stack.isEmpty(); i++) {
            ItemStack current = simulated[i];
            if(current.isEmpty()) continue;
            if(!ItemStack.isSameItemSameComponents(current, stack)) continue;

            int moved = Math.min(stack.getCount(), current.getMaxStackSize() - current.getCount());
            if(moved > 0) {
                current.grow(moved);
                stack.shrink(moved);
            }
        }

        for(int i = 0; i < simulated.length && !stack.isEmpty(); i++) {
            ItemStack current = simulated[i];
            if(!current.isEmpty()) continue;

            int moved = Math.min(stack.getCount(), stack.getMaxStackSize());
            ItemStack placed = stack.copy();
            placed.setCount(moved);
            simulated[i] = placed;
            stack.shrink(moved);
        }

        return stack.isEmpty();
    }

    private void processItem() {
        ItemStack[] outputs = CentrifugeRecipes.getOutput(this.slots.get(SLOT_INPUT));
        if(outputs.length == 0) return;

        for(ItemStack output : outputs) {
            if(output.isEmpty()) continue;
            this.insertOutput(output.copy());
        }

        ItemStack input = this.slots.get(SLOT_INPUT);
        input.shrink(1);
        if(input.isEmpty()) {
            this.slots.set(SLOT_INPUT, ItemStack.EMPTY);
        }

        this.setChanged();
    }

    private void insertOutput(ItemStack stack) {
        for(int slot = SLOT_OUTPUT_START; slot < SLOT_OUTPUT_END && !stack.isEmpty(); slot++) {
            ItemStack current = this.slots.get(slot);
            if(current.isEmpty()) continue;
            if(!ItemStack.isSameItemSameComponents(current, stack)) continue;

            int moved = Math.min(stack.getCount(), current.getMaxStackSize() - current.getCount());
            if(moved > 0) {
                current.grow(moved);
                stack.shrink(moved);
            }
        }

        for(int slot = SLOT_OUTPUT_START; slot < SLOT_OUTPUT_END && !stack.isEmpty(); slot++) {
            if(!this.slots.get(slot).isEmpty()) continue;

            int moved = Math.min(stack.getCount(), stack.getMaxStackSize());
            ItemStack placed = stack.copy();
            placed.setCount(moved);
            this.slots.set(slot, placed);
            stack.shrink(moved);
        }
    }

    public DirPos[] getConPos() {
        BlockPos pos = this.getBlockPos();
        return new DirPos[] {
                new DirPos(pos.getX() + 1, pos.getY(), pos.getZ(), Direction.EAST),
                new DirPos(pos.getX() - 1, pos.getY(), pos.getZ(), Direction.WEST),
                new DirPos(pos.getX(), pos.getY(), pos.getZ() + 1, Direction.SOUTH),
                new DirPos(pos.getX(), pos.getY(), pos.getZ() - 1, Direction.NORTH),
                new DirPos(pos.getX(), pos.getY() + 1, pos.getZ(), Direction.UP),
                new DirPos(pos.getX(), pos.getY() - 1, pos.getZ(), Direction.DOWN)
        };
    }

    public int getCentrifugeProgressScaled(int pixels) {
        return this.progress * pixels / PROCESSING_SPEED;
    }

    public int getPowerRemainingScaled(int pixels) {
        return (int) (this.getPower() * pixels / Math.max(this.getMaxPower(), 1L));
    }

    public boolean hasPower() {
        return this.getPower() > 0;
    }

    public boolean isProcessing() {
        return this.progress > 0;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == SLOT_INPUT) return CentrifugeRecipes.hasRecipe(stack);
        if(slot == SLOT_BATTERY) return stack.getItem() instanceof IBatteryItem;
        if(slot >= SLOT_UPGRADE_START && slot <= SLOT_UPGRADE_END) {
            return stack.getItem() instanceof MachineUpgradeItem item && this.getValidUpgrades().containsKey(item.type);
        }
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return ACCESSIBLE_SLOTS;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index >= SLOT_OUTPUT_START && index < SLOT_OUTPUT_END;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.progress = tag.getInt("progress");
        this.isProgressing = tag.getBoolean("isProgressing");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putInt("progress", this.progress);
        tag.putBoolean("isProgressing", this.isProgressing);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeInt(this.progress);
        buf.writeBoolean(this.isProgressing);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.progress = buf.readInt();
        this.isProgressing = buf.readBoolean();
    }

    @Override
    public long getPower() {
        return Math.max(0L, Math.min(this.power, this.getMaxPower()));
    }

    @Override
    public void setPower(long power) {
        this.power = power;
    }

    @Override
    public long getMaxPower() {
        return MAX_POWER;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineCentrifugeMenu(id, inventory, this);
    }

    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.CENTRIFUGE_OPERATE.get(), SoundSource.BLOCKS, this, 1.0F, 15F, 1.0F, 20);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if(this.audio != null) {
            this.audio.stopSound();
            this.audio = null;
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(this.audio != null) {
            this.audio.stopSound();
            this.audio = null;
        }
    }

    public AABB getRenderBoundingBox() {
        if(this.renderBox == null) {
            BlockPos pos = this.getBlockPos();
            this.renderBox = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 4, pos.getZ() + 1);
        }
        return this.renderBox;
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED || type == UpgradeType.POWER || type == UpgradeType.OVERDRIVE;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        if(type == UpgradeType.SPEED) {
            info.add("&a+" + level + " processing speed");
        }
        if(type == UpgradeType.POWER) {
            info.add("&a-" + (100 - 100 / (1 + level)) + "% power consumption");
        }
        if(type == UpgradeType.OVERDRIVE) {
            info.add("&a+" + (level * 500) + "% processing speed");
            info.add("&c+" + (level * 10_000) + " HE/t consumption");
        }
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new HashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        upgrades.put(UpgradeType.OVERDRIVE, 3);
        return upgrades;
    }
}
