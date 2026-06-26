package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineAssemblyMachineBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MachineAssemblyMachineMenu extends MenuBase<MachineAssemblyMachineBlockEntity> {

    public MachineAssemblyMachineMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineAssemblyMachineBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level, extraData.readBlockPos()));
    }

    public MachineAssemblyMachineMenu(int id, Inventory inventory, MachineAssemblyMachineBlockEntity be) {
        super(NtmMenuTypes.ASSEMBLY_MACHINE.get(), id, be);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 152, 81));
        // Schematic
        this.addSlot(new SlotNonRetarded(be, 1, 35, 126));
        // Upgrades
        this.addSlots(be, 2, 152, 108, 2, 1);
        // Input
        this.addSlots(be, 4, 8, 18, 4, 3);
        // Output
        this.addSlot(new SlotCraftingOutput(inventory.player, be, 16, 98, 45));

        this.playerInv(inventory, 8, 174);
    }
}
