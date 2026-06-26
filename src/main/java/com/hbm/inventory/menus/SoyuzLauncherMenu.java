package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.SoyuzLauncherBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class SoyuzLauncherMenu extends MenuBase<SoyuzLauncherBlockEntity> {

    public SoyuzLauncherMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (SoyuzLauncherBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level, extraData.readBlockPos()));
    }

    public SoyuzLauncherMenu(int id, Inventory inventory, SoyuzLauncherBlockEntity be) {
        super(NtmMenuTypes.SOYUZ_LAUNCHER.get(), id, be);

        // Soyuz and Designator
        this.addSlots(be, 0, 62, 18, 2, 1);
        // Satellite and Landing module
        this.addSlots(be, 2, 116, 18, 2, 1);
        //Kerosene IN
        this.addSlot(new Slot(be, 4, 8, 90));
        //Kerosene OUT
        this.addSlot(new SlotTakeOnly(be, 5, 8, 108));
        //Peroxide IN
        this.addSlot(new Slot(be, 6, 26, 90));
        //Peroxide OUT
        this.addSlot(new SlotTakeOnly(be, 7, 26, 108));
        // Battery
        this.addSlot(new SlotNonRetarded(be, 8, 44, 108));

        this.addSlots(be, 9, 62, 72, 3, 6);

        this.playerInv(inventory, 8, 140, 198); // 84 + 56, 142 + 56
    }
}
