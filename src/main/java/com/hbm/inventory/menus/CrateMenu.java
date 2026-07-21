package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.storage.CrateBaseBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class CrateMenu extends MenuBase<CrateBaseBlockEntity> {

    public CrateMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (CrateBaseBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public CrateMenu(int id, Inventory inventory, CrateBaseBlockEntity crate) {
        super(NtmMenuTypes.CRATE.get(), id, crate);

        this.addSlots(crate, 0, crate.getSlotX(), crate.getSlotY(), crate.getRows(), crate.getColumns());
        this.playerInv(inventory, crate.getPlayerInvX(), crate.getPlayerInvY());
    }
}
