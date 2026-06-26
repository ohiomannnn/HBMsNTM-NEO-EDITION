package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineSatLinkerBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MachineSatLinkerMenu extends MenuBase<MachineSatLinkerBlockEntity> {

    public MachineSatLinkerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineSatLinkerBlockEntity) inventory.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public MachineSatLinkerMenu(int id, Inventory inventory, MachineSatLinkerBlockEntity be) {
        super(NtmMenuTypes.SAT_LINKER.get(), id, be);

        this.addSlot(new SlotNonRetarded(be, 0, 44, 35));
        this.addSlot(new SlotNonRetarded(be, 1, 80, 35));
        this.addSlot(new SlotNonRetarded(be, 2, 116, 35));

        this.playerInv(inventory, 8, 84);
    }
}