package com.hbm.blockentity.machine.storage;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.machine.LockableBaseBlockEntity;
import com.hbm.inventory.container.ContainerCrateIron;
import com.hbm.lib.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.core.Direction;

public abstract class CrateBaseBlockEntity extends LockableBaseBlockEntity implements WorldlyContainer, MenuProvider {

    protected NonNullList<ItemStack> items;
    private final int[] allSlots;
    public String customName;
    public boolean hasSpiders = false;

    public CrateBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int size) {
        super(type, pos, state);
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
        this.allSlots = new int[size];
        for (int i = 0; i < size; i++) {
            this.allSlots[i] = i;
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new ContainerCrateIron(windowId, playerInv, this);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    public ItemStack getItem(int index) {
        if (index < 0 || index >= items.size()) return ItemStack.EMPTY;
        return items.get(index);
    }
    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(items, slot, amount);
        if (!result.isEmpty()) setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack result = items.get(slot);
        if (result.isEmpty()) return ItemStack.EMPTY;
        items.set(slot, ItemStack.EMPTY);
        return result;
    }

    public void setItem(int slot, ItemStack stack) {
        if (slot < 0 || slot >= items.size()) {
            HBMsNTM.LOGGER.warn("setItem: invalid slot {} for {} (size {})", slot, this, items.size());
            return;
        }
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }
    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) return false;
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64;
    }

    public void setCustomName(String name) {
        this.customName = name;
        setChanged();
    }

    @Override
    public void startOpen(Player player) {
        if (level != null) {
            level.playSound(null, worldPosition, ModSounds.CRATE_OPEN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (level != null) {
            level.playSound(null, worldPosition, ModSounds.CRATE_CLOSE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putBoolean("Spiders", hasSpiders);
        if (customName != null) tag.putString("CustomName", customName);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
        hasSpiders = tag.getBoolean("Spiders");
        if (tag.contains("CustomName")) customName = tag.getString("CustomName");
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return this.allSlots;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return canPlaceItem(slot, stack) && !isLocked();
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return !isLocked();
    }

    public void fillWithSpiders() {
        this.hasSpiders = true;
    }

    private static final int NUM_SPIDERS = 3;

    public static void spawnSpiders(Player player, Level level, CrateBaseBlockEntity crate) {
        if (crate.hasSpiders && level instanceof ServerLevel serverLevel) {
            RandomSource random = serverLevel.getRandom();
            for (int i = 0; i < NUM_SPIDERS; i++) {
                CaveSpider spider = new CaveSpider(EntityType.CAVE_SPIDER, level);
                spider.moveTo(crate.getBlockPos().getX() + random.nextGaussian() * 2,
                        crate.getBlockPos().getY() + 1,
                        crate.getBlockPos().getZ() + random.nextGaussian() * 2,
                        random.nextFloat(), 0);
                spider.setTarget(player);
                serverLevel.addFreshEntity(spider);
            }
            crate.hasSpiders = false;
            crate.setChanged();
        }
    }

    public static void spawnSpiders(Player player, Level level, ItemStack crate) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        CustomData customData = crate.get(DataComponents.CUSTOM_DATA);
        if (customData != null && customData.getUnsafe().getBoolean("Spiders")) {
            RandomSource random = serverLevel.getRandom();
            for (int i = 0; i < NUM_SPIDERS; i++) {
                CaveSpider spider = new CaveSpider(EntityType.CAVE_SPIDER, level);
                spider.moveTo(player.getX() + random.nextGaussian() * 2,
                        player.getY() + 1,
                        player.getZ() + random.nextGaussian() * 2,
                        random.nextFloat(), 0);
                spider.setTarget(player);
                serverLevel.addFreshEntity(spider);
            }

            customData.getUnsafe().remove("Spiders");
            crate.set(DataComponents.CUSTOM_DATA, customData);
        }
    }
}
