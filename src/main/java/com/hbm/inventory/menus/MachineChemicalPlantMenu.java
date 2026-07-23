package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineChemicalPlantBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class MachineChemicalPlantMenu extends MenuBase<MachineChemicalPlantBlockEntity> {

    public MachineChemicalPlantMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineChemicalPlantBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineChemicalPlantMenu(int id, Inventory inventory, MachineChemicalPlantBlockEntity be) {
        super(NtmMenuTypes.MACHINE_CHEMICAL_PLANT.get(), id, be);

        this.addSlot(new SlotNonRetarded(be, 0, 152, 81));
        this.addSlot(new SlotNonRetarded(be, 1, 35, 126));
        this.addSlots(be, 2, 152, 108, 2, 1);
        this.addSlots(be, 4, 8, 99, 1, 3);
        this.addOutputSlots(inventory.player, be, 7, 80, 99, 1, 3);
        this.addSlots(be, 10, 8, 54, 1, 3);
        this.addTakeOnlySlots(be, 13, 8, 72, 1, 3);
        this.addSlots(be, 16, 80, 54, 1, 3);
        this.addTakeOnlySlots(be, 19, 80, 72, 1, 3);
        this.playerInv(inventory, 8, 174);
    }

    @Override
    public ItemStack quickMoveStack(net.minecraft.world.entity.player.Player player, int index) {
        return super.quickMoveStack(player, index);
    }
}
