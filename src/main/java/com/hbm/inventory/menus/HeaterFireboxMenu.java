package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.heater.HeaterFireboxBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HeaterFireboxMenu extends MenuBase<HeaterFireboxBlockEntity> {

    public HeaterFireboxMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (HeaterFireboxBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public HeaterFireboxMenu(int id, Inventory inventory, HeaterFireboxBlockEntity be) {
        super(NtmMenuTypes.HEATER_FIREBOX.get(), id, be);

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
