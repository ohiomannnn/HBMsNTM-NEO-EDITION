package com.hbm.inventory.menus;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.machine.NTMAnvilBlock;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.inventory.recipes.anvil.AnvilRecipes;
import com.hbm.inventory.recipes.anvil.AnvilSmithingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AnvilMenu extends MenuBase<SimpleContainer> {

    private static final int INPUT_LEFT = 0;
    private static final int INPUT_RIGHT = 1;
    private static final int OUTPUT = 2;

    private final BlockPos pos;
    private final int tier;
    private boolean suppressUpdates;

    public AnvilMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, extraData.readBlockPos(), extraData.readVarInt());
    }

    public AnvilMenu(int id, Inventory inventory, BlockPos pos, int tier) {
        super(NtmMenuTypes.ANVIL.get(), id, new SimpleContainer(3));

        this.pos = pos;
        this.tier = tier;

        this.addSlot(new InputSlot(this.be, INPUT_LEFT, 17, 27));
        this.addSlot(new InputSlot(this.be, INPUT_RIGHT, 53, 27));
        this.addSlot(new OutputSlot(inventory.player, this.be, OUTPUT, 89, 27));

        this.playerInv(inventory, 8, 140);
        this.updateResult();
    }

    public int getTier() {
        return this.tier;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if(player.level().isClientSide) return;

        for(int i = INPUT_LEFT; i <= INPUT_RIGHT; i++) {
            ItemStack stack = this.be.removeItemNoUpdate(i);
            if(!stack.isEmpty()) {
                player.drop(stack, false);
            }
        }

        this.be.setItem(OUTPUT, ItemStack.EMPTY);
    }

    @Override
    public boolean stillValid(Player player) {
        if(player.level() == null) return false;
        if(!player.level().getBlockState(this.pos).is(NtmBlocks.ANVIL.get())) return false;
        return player.distanceToSqr(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();

            if(index <= OUTPUT) {
                if(!this.moveItemStackTo(stack, OUTPUT + 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if(!this.moveItemStackTo(stack, INPUT_LEFT, OUTPUT, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if(stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return result;
    }

    private void updateResult() {
        ItemStack left = this.be.getItem(INPUT_LEFT);
        ItemStack right = this.be.getItem(INPUT_RIGHT);

        if(left.isEmpty() || right.isEmpty()) {
            this.be.setItem(OUTPUT, ItemStack.EMPTY);
            return;
        }

        for(AnvilSmithingRecipe recipe : AnvilRecipes.getSmithing()) {
            if(recipe.matches(left, right) && recipe.getTier() <= this.tier) {
                this.be.setItem(OUTPUT, recipe.getOutput(left, right));
                return;
            }
        }

        this.be.setItem(OUTPUT, ItemStack.EMPTY);
    }

    private void consumeInputs() {
        ItemStack left = this.be.getItem(INPUT_LEFT);
        ItemStack right = this.be.getItem(INPUT_RIGHT);

        if(left.isEmpty() || right.isEmpty()) return;

        this.suppressUpdates = true;
        try {
            for(AnvilSmithingRecipe recipe : AnvilRecipes.getSmithing()) {
                int mirrored = recipe.matchesInt(left, right);
                if(mirrored == -1 || recipe.getTier() > this.tier) {
                    continue;
                }

                this.be.removeItem(INPUT_LEFT, recipe.amountConsumed(INPUT_LEFT, mirrored == 1));
                this.be.removeItem(INPUT_RIGHT, recipe.amountConsumed(INPUT_RIGHT, mirrored == 1));
                return;
            }
        } finally {
            this.suppressUpdates = false;
        }
    }

    private final class InputSlot extends SlotNonRetarded {

        public InputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            if(!AnvilMenu.this.suppressUpdates) {
                AnvilMenu.this.updateResult();
            }
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            super.onTake(player, stack);
            if(!AnvilMenu.this.suppressUpdates) {
                AnvilMenu.this.updateResult();
            }
        }
    }

    private final class OutputSlot extends SlotTakeOnly {

        private final Player player;

        public OutputSlot(Player player, Container container, int slot, int x, int y) {
            super(container, slot, x, y);
            this.player = player;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            super.onTake(player, stack);
            AnvilMenu.this.consumeInputs();
            AnvilMenu.this.updateResult();
            stack.onCraftedBy(this.player.level(), this.player, stack.getCount());
        }
    }
}
