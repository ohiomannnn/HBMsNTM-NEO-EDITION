package com.hbm.blockentity.machine.storage;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.machine.LockableBaseBlockEntity;
import com.hbm.inventory.menus.CrateMenu;
import com.hbm.registry.NtmSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class CrateBaseBlockEntity extends LockableBaseBlockEntity implements WorldlyContainer, Nameable, MenuProvider, IPersistentNBT {

    public NonNullList<ItemStack> slots;

    private Component customName;

    public boolean hasSpiders = false;

    private final int columns;
    private final int rows;
    private final int slotX;
    private final int slotY;
    private final int playerInvX;
    private final int playerInvY;
    private final int guiWidth;
    private final int guiHeight;
    private final int inventoryLabelX;
    private final int titleColor;
    private final int inventoryLabelColor;
    private final String texture;

    public CrateBaseBlockEntity(BlockEntityType<? extends CrateBaseBlockEntity> type, BlockPos pos, BlockState blockState, int size, String texture, int columns, int rows, int slotX, int slotY, int playerInvX, int playerInvY, int guiWidth, int guiHeight, int inventoryLabelX, int titleColor, int inventoryLabelColor) {
        super(type, pos, blockState);
        this.slots = NonNullList.withSize(size, ItemStack.EMPTY);
        this.texture = texture;
        this.columns = columns;
        this.rows = rows;
        this.slotX = slotX;
        this.slotY = slotY;
        this.playerInvX = playerInvX;
        this.playerInvY = playerInvY;
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
        this.inventoryLabelX = inventoryLabelX;
        this.titleColor = titleColor;
        this.inventoryLabelColor = inventoryLabelColor;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void onDataPacket(Connection con, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider registries) {
        CompoundTag tag = packet.getTag();
        this.loadAdditional(tag, registries);
    }

    @Override
    public int getContainerSize() {
        return this.slots.size();
    }

    @Override
    public ItemStack getItem(int index) {
        return slots.get(index);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack itemstack = this.slots.get(index);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.slots.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        this.slots.set(index, itemStack);
        itemStack.limitSize(this.getMaxStackSize(itemStack));
        this.setChanged();
    }

    @Override
    public Component getDisplayName() {
        return this.hasCustomName() ? this.customName : getName();
    }

    public abstract Component getName();

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.getString().isEmpty();
    }

    public void setCustomName(Component name) {
        this.customName = name;
        this.setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player player) {
        if (level != null && level.getBlockEntity(this.getBlockPos()) != this) {
            return false;
        } else {
            return player.distanceToSqr(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5) <= 64;
        }
    }

    @Override
    public void startOpen(Player player) {
        player.level().playSound(null, this.getBlockPos(), NtmSoundEvents.CRATE_OPEN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }
    @Override
    public void stopOpen(Player player) {
        player.level().playSound(null, this.getBlockPos(), NtmSoundEvents.CRATE_CLOSE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = slots.get(slot);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (stack.getCount() <= amount) {
            slots.set(slot, ItemStack.EMPTY);
            return stack;
        }
        ItemStack split = stack.split(amount);
        if (stack.isEmpty()) {
            slots.set(slot, ItemStack.EMPTY);
        }
        this.setChanged();
        return split;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(slot, itemStack) && !this.isLocked();
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return !this.isLocked();
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        int[] slots = new int[this.slots.size()];
        for (int i = 0; i < slots.length; i++) slots[i] = i;
        return slots;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        ListTag list = tag.getList("Items", 10);

        for (int i = 0; i < list.size(); i++) {
            CompoundTag tagAt = list.getCompound(i);
            byte b = tagAt.getByte("Slot");
            if (b >= 0 && b < slots.size()) {
                slots.set(b, ItemStack.parse(registries, tagAt).orElse(ItemStack.EMPTY));
            }
        }

        this.hasSpiders = tag.getBoolean("Spiders");

        if (tag.contains("Name", 8)) {
            this.customName = parseCustomNameSafe(tag.getString("Name"), registries);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        ListTag list = new ListTag();

        for (int i = 0; i < slots.size(); i++) {
            ItemStack stack = slots.get(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                list.add(stack.save(registries, itemTag));
            }
        }

        tag.put("Items", list);
        tag.putBoolean("Spiders", hasSpiders);

        if (this.customName != null) {
            tag.putString("Name", Component.Serializer.toJson(this.customName, registries));
        }
    }

    @Override
    public void writeNBT(CompoundTag savedTag) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, this.getLevel().registryAccess());
        savedTag.put(IPersistentNBT.NBT_PERSISTENT_KEY, tag);
    }

    @Override
    public void readNBT(CompoundTag savedTag) {
        if(savedTag.contains(IPersistentNBT.NBT_PERSISTENT_KEY)) {
            this.loadAdditional(savedTag.getCompound(IPersistentNBT.NBT_PERSISTENT_KEY), this.getLevel().registryAccess());
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new CrateMenu(id, inventory, this);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeBoolean(this.hasSpiders);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.hasSpiders = buf.readBoolean();
    }

    public int getColumns() { return columns; }
    public int getRows() { return rows; }
    public int getSlotX() { return slotX; }
    public int getSlotY() { return slotY; }
    public int getPlayerInvX() { return playerInvX; }
    public int getPlayerInvY() { return playerInvY; }
    public int getGuiWidth() { return guiWidth; }
    public int getGuiHeight() { return guiHeight; }
    public int getInventoryLabelX() { return inventoryLabelX; }
    public int getTitleColor() { return titleColor; }
    public int getInventoryLabelColor() { return inventoryLabelColor; }
    public String getTexture() { return texture; }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : slots) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clearContent() {
        for(int i = 0; i < slots.size(); i++) {
            slots.set(i, ItemStack.EMPTY);
        }
        this.setChanged();
    }

    // Spiders!!!
    public void fillWithSpiders() {
        this.hasSpiders = true;
    }

    private static final int numSpiders = 3; // leave that at 3 for now TODO: maybe a config option or smth

    /// For when opening from a TileEntity.
    public static void spawnSpiders(Player player, Level level, CrateBaseBlockEntity crate) {
        if (crate.hasSpiders) {
            RandomSource random = level.random;

            for (int i = 0; i < numSpiders; i++) {

                CaveSpider spider = EntityType.CAVE_SPIDER.create(level); // lord
                spider.moveTo(crate.getBlockPos().getX() + random.nextGaussian() * 2, crate.getBlockPos().getY() + 1, crate.getBlockPos().getZ() + random.nextGaussian() * 2, random.nextFloat(), 0);
                spider.setTarget(player);

                level.addFreshEntity(spider);
            }
            crate.hasSpiders = false;
            crate.setChanged();
        }
    }
}
