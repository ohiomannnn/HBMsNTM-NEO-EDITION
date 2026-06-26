package com.hbm.blockentity.machine;

import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.menus.MachineSatLinkerMenu;
import com.hbm.items.ISatChip;
import com.hbm.saveddata.SatelliteSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class MachineSatLinkerBlockEntity extends MachineBaseBlockEntity {

    public MachineSatLinkerBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_SATLINKER.get(), pos, state, 3);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.sat_linker");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            // copy id
            ISatChip.setFreqS(this.slots.get(1), ISatChip.getFreqS(this.slots.get(0)));
            // generate random id
            if(this.level instanceof ServerLevel serverLevel) {
                SatelliteSavedData data = SatelliteSavedData.getData(serverLevel);
                int id = this.level.random.nextInt(100000);
                if(!data.isFreqTaken(id)) ISatChip.setFreqS(this.slots.get(2), id);
            }
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.getItem() instanceof ISatChip;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineSatLinkerMenu(id, inventory, this);
    }
}
