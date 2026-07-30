package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.SoyuzLauncherBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
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

        //Soyuz
        this.addSlot(new Slot(be, 0, 98, 80));
        //Designator
        this.addSlot(new Slot(be, 1, 80, 80));
        //Satellite
        this.addSlot(new Slot(be, 2, 98, 26));
        //Landing module
        this.addSlot(new Slot(be, 3, 80, 26));
        //Kerosene IN
        this.addSlot(new Slot(be, 4, 152, 98));
        //Kerosene OUT
        this.addSlot(new Slot(be, 5, 152, 116));
        //Oxyden IN
        this.addSlot(new Slot(be, 6, 170, 98));
        //Oxyden OUT
        this.addSlot(new Slot(be, 7, 170, 116));
        //Battery
        this.addSlot(new Slot(be, 8, 134, 98));

        for(int i = 0; i < 3; i++) for(int j = 0; j < 6; j++) this.addSlot(new Slot(be, j + i * 6 + 9, 44 - i * 18, 26 + j * 18));

        this.playerInv(inventory, 17, 162, 220);
    }
}
