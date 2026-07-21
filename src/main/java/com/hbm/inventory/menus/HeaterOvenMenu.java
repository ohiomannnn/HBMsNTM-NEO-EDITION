package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.heater.HeaterOvenBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class HeaterOvenMenu extends MenuBase<HeaterOvenBlockEntity> {

    public HeaterOvenMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (HeaterOvenBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public HeaterOvenMenu(int id, Inventory inventory, HeaterOvenBlockEntity be) {
        super(NtmMenuTypes.HEATER_OVEN.get(), id, be);

        this.be.startOpen(inventory.player);

        this.addSlot(new SlotNonRetarded(be, 0, 44, 27));
        this.addSlot(new SlotNonRetarded(be, 1, 62, 27));

        this.playerInv(inventory, 8, 86);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.be.stopOpen(player);
    }
}
