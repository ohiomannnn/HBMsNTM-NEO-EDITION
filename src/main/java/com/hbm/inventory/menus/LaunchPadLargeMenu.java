package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.bomb.LaunchPadBaseBlockEntity;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.items.IDesignatorItem;
import com.hbm.items.NtmItems;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class LaunchPadLargeMenu extends AbstractContainerMenu {

    public final LaunchPadBaseBlockEntity be;

    public LaunchPadLargeMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (LaunchPadBaseBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public LaunchPadLargeMenu(int id, Inventory inventory, LaunchPadBaseBlockEntity blockEntity) {
        super(ModMenuTypes.LAUNCH_PAD_LARGE.get(), id);

        this.be = blockEntity;

        //Missile
        this.addSlot(new Slot(be, 0, 26, 36));
        //Designator
        this.addSlot(new Slot(be, 1, 26, 72));
        //Battery
        this.addSlot(new Slot(be, 2, 107, 90));
        //Fuel in
        this.addSlot(new Slot(be, 3, 125, 90));
        //Fuel out
        this.addSlot(new SlotTakeOnly(be, 4, 125, 108));
        //Oxidizer in
        this.addSlot(new Slot(be, 5, 143, 90));
        //Oxidizer out
        this.addSlot(new SlotTakeOnly(be, 6, 143, 108));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 154 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 212));
        }
    }


    @Override
    public boolean stillValid(Player player) {
        return be.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {

        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {

            ItemStack stack = slot.getItem();
            newStack = stack.copy();

            if (index <= 6) {
                if (!this.moveItemStackTo(stack, 7, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (newStack.getItem() instanceof IBatteryItem || newStack.is(NtmItems.BATTERY_CREATIVE.get())) {
                    if (!this.moveItemStackTo(stack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (be.isMissileValid(newStack)) {
                    if (!this.moveItemStackTo(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (newStack.is(NtmItems.FLUID_BARREL_INFINITE.get())) {
                    if (!this.moveItemStackTo(stack, 3, 4, false)) if (!this.moveItemStackTo(stack, 5, 6, false)) {
                            return ItemStack.EMPTY;
                    }
                } else if (FluidContainerRegistry.getFluidContent(newStack, be.tanks[0].getTankType()) > 0) {
                    if (!this.moveItemStackTo(stack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (FluidContainerRegistry.getFluidContent(newStack, be.tanks[1].getTankType()) > 0) {
                    if (!this.moveItemStackTo(stack, 5, 6, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (newStack.getItem() instanceof IDesignatorItem) {
                    if (!this.moveItemStackTo(stack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }
}
