package com.hbm.blockentity.machine;

import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.inventory.menus.MachineSatLinkerMenu;
import com.hbm.items.ISatChip;
import com.hbm.items.machine.SatChipItem;
import com.hbm.saveddata.SatelliteSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class MachineSatLinkerBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler items = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof SatChipItem;
        }
    };

    private Component customName;

    public MachineSatLinkerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.MACHINE_SATLINKER.get(), pos, state);
    }

    public void setCustomName(Component name) {
        this.customName = name;
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return customName != null ? customName : Component.translatable("container.satLinker");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Items", items.serializeNBT(provider));
        if (customName != null) tag.putString("Name", Component.Serializer.toJson(customName, provider));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        items.deserializeNBT(provider, tag.getCompound("Items"));

        if (tag.contains("Name")) {
            customName = Component.Serializer.fromJson(tag.getString("Name"), provider);
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new MachineSatLinkerMenu(id, playerInv, this);
    }

    public ItemStackHandler getItems() {
        return items;
    }

    public static void serverTick(Level level, BlockPos ignored, BlockState ignored1, MachineSatLinkerBlockEntity be) {

        ItemStack s0 = be.items.getStackInSlot(0);
        ItemStack s1 = be.items.getStackInSlot(1);
        ItemStack s2 = be.items.getStackInSlot(2);

        if (!s0.isEmpty() && !s1.isEmpty() && s0.getItem() instanceof ISatChip && s1.getItem() instanceof ISatChip) {
            ISatChip.setFreqS(s1, ISatChip.getFreqS(s0));
        }

        if (!s2.isEmpty() && s2.getItem() instanceof ISatChip) {

            if (level instanceof ServerLevel serverLevel) {
                SatelliteSavedData data = SatelliteSavedData.get(serverLevel);

                int id = level.getRandom().nextInt(100000);

                if (!data.isFreqTaken(id)) {
                    ISatChip.setFreqS(s2, id);
                }
            }
        }
    }
}
