package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.MachineSolderingStationBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineSolderingStationMenu extends MenuBase<MachineSolderingStationBlockEntity> {

    public MachineSolderingStationMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineSolderingStationBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineSolderingStationMenu(int id, Inventory inventory, MachineSolderingStationBlockEntity be) {
        super(NtmMenuTypes.MACHINE_SOLDERING_STATION.get(), id, be);

        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 3; j++) {
                this.addSlot(new SlotNonRetarded(be, i * 3 + j, 17 + j * 18, 18 + i * 18));
            }
        }

        this.addSlot(new SlotCraftingOutput(inventory.player, be, 6, 107, 27));
        this.addSlot(new SlotNonRetarded(be, 7, 152, 72));
        this.addSlot(new SlotNonRetarded(be, 8, 17, 63));
        this.addSlot(new SlotNonRetarded(be, 9, 89, 63));
        this.addSlot(new SlotNonRetarded(be, 10, 107, 63));

        this.playerInv(inventory, 8, 122);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index <= 10) {
                if(!this.moveItemStackTo(stack, 11, this.slots.size(), true)) return ItemStack.EMPTY;
            } else {
                if(stack.getItem() instanceof IBatteryItem) {
                    if(!this.moveItemStackTo(stack, 7, 8, false)) return ItemStack.EMPTY;
                } else if(stack.getItem() instanceof IItemFluidIdentifier) {
                    if(!this.moveItemStackTo(stack, 8, 9, false)) return ItemStack.EMPTY;
                } else if(stack.getItem() instanceof MachineUpgradeItem) {
                    if(!this.moveItemStackTo(stack, 9, 11, false)) return ItemStack.EMPTY;
                } else {
                    for(com.hbm.inventory.RecipesCommon.AStack t : com.hbm.inventory.recipes.SolderingRecipes.toppings) {
                        if(t.matchesRecipe(stack, false) && this.moveItemStackTo(stack, 0, 3, false)) {
                            break;
                        }
                    }
                    if(!stack.isEmpty()) {
                        for(com.hbm.inventory.RecipesCommon.AStack t : com.hbm.inventory.recipes.SolderingRecipes.pcb) {
                            if(t.matchesRecipe(stack, false) && this.moveItemStackTo(stack, 3, 5, false)) {
                                break;
                            }
                        }
                    }
                    if(!stack.isEmpty()) {
                        for(com.hbm.inventory.RecipesCommon.AStack t : com.hbm.inventory.recipes.SolderingRecipes.solder) {
                            if(t.matchesRecipe(stack, false) && this.moveItemStackTo(stack, 5, 6, false)) {
                                break;
                            }
                        }
                    }

                    if(!stack.isEmpty()) return ItemStack.EMPTY;
                }
            }

            if(stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return ret;
    }
}
