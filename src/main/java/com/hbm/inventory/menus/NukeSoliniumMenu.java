package com.hbm.inventory.menus;

import com.hbm.blockentity.bomb.NukeSoliniumBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class NukeSoliniumMenu extends MenuBase<NukeSoliniumBlockEntity> {

    public NukeSoliniumMenu(int id, Inventory playerInv, FriendlyByteBuf extraData) {
        this(id, playerInv, (NukeSoliniumBlockEntity) playerInv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public NukeSoliniumMenu(int id, Inventory inventory, NukeSoliniumBlockEntity be) {
        super(NtmMenuTypes.NUKE_SOLINIUM.get(), id, be);

        this.addSlot(new SlotNonRetarded(be, 0, 26, 18));
        this.addSlot(new SlotNonRetarded(be, 1, 53, 18));
        this.addSlot(new SlotNonRetarded(be, 2, 107, 18));
        this.addSlot(new SlotNonRetarded(be, 3, 134, 18));
        this.addSlot(new SlotNonRetarded(be, 4, 80, 36));
        this.addSlot(new SlotNonRetarded(be, 5, 26, 54));
        this.addSlot(new SlotNonRetarded(be, 6, 53, 54));
        this.addSlot(new SlotNonRetarded(be, 7, 107, 54));
        this.addSlot(new SlotNonRetarded(be, 8, 134, 54));

        this.playerInv(inventory, 8, 138, 198);
    }
}