package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.heater.HeaterHeatexBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class HeaterHeatexMenu extends MenuBase<HeaterHeatexBlockEntity> {

    public HeaterHeatexMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (HeaterHeatexBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public HeaterHeatexMenu(int id, Inventory inventory, HeaterHeatexBlockEntity be) {
        super(NtmMenuTypes.HEATER_HEATEX.get(), id, be);

        this.addSlot(new SlotNonRetarded(be, 0, 80, 72));
        this.playerInv(inventory, 8, 122);
    }
}
