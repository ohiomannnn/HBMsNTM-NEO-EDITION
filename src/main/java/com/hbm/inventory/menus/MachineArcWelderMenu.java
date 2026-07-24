package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.MachineArcWelderBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.recipes.ArcWelderRecipes;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineArcWelderMenu extends MenuBase<MachineArcWelderBlockEntity> {

    public MachineArcWelderMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineArcWelderBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineArcWelderMenu(int id, Inventory inventory, MachineArcWelderBlockEntity be) {
        super(NtmMenuTypes.MACHINE_ARC_WELDER.get(), id, be);

        this.addSlot(new SlotNonRetarded(be, 0, 17, 36));
        this.addSlot(new SlotNonRetarded(be, 1, 35, 36));
        this.addSlot(new SlotNonRetarded(be, 2, 53, 36));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, 3, 107, 36));
        this.addSlot(new SlotNonRetarded(be, 4, 152, 72));
        this.addSlot(new SlotNonRetarded(be, 5, 17, 63));
        this.addSlot(new SlotNonRetarded(be, 6, 89, 63));
        this.addSlot(new SlotNonRetarded(be, 7, 107, 63));

        this.playerInv(inventory, 8, 122);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index <= 7) {
                if(!this.moveItemStackTo(stack, 8, this.slots.size(), true)) return ItemStack.EMPTY;
            } else {
                if(stack.getItem() instanceof IBatteryItem) {
                    if(!this.moveItemStackTo(stack, 4, 5, false)) return ItemStack.EMPTY;
                } else if(stack.getItem() instanceof IItemFluidIdentifier) {
                    if(!this.moveItemStackTo(stack, 5, 6, false)) return ItemStack.EMPTY;
                } else if(stack.getItem() instanceof MachineUpgradeItem) {
                    if(!this.moveItemStackTo(stack, 6, 8, false)) return ItemStack.EMPTY;
                } else {
                    for(com.hbm.inventory.RecipesCommon.AStack ingredient : ArcWelderRecipes.ingredients) {
                        if(ingredient.matchesRecipe(stack, false) && this.moveItemStackTo(stack, 0, 3, false)) {
                            break;
                        }
                    }
                    if(!stack.isEmpty()) return ItemStack.EMPTY;
                }
            }

            if(stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return ret;
    }
}
