package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.heater.HeaterOilburnerBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HeaterOilburnerMenu extends MenuBase<HeaterOilburnerBlockEntity> {

    public HeaterOilburnerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (HeaterOilburnerBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public HeaterOilburnerMenu(int id, Inventory inventory, HeaterOilburnerBlockEntity be) {
        super(NtmMenuTypes.HEATER_OILBURNER.get(), id, be);

        this.addSlot(new SlotNonRetarded(be, 0, 26, 17));
        this.addSlot(new SlotNonRetarded(be, 1, 26, 53));
        this.addSlot(new SlotNonRetarded(be, 2, 44, 71));

        this.playerInv(inventory, 8, 121);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return super.quickMoveStack(player, index);
    }
}
