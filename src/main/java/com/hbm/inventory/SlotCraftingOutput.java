package com.hbm.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotCraftingOutput extends Slot {

    private final Player player;
    private int craftBuffer;

    public SlotCraftingOutput(Player player, Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    public void checkAchievements(Player player, ItemStack stack) {
        super.onTake(player, stack);
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.craftBuffer += Math.min(amount, this.getItem().getCount());
        }
        return super.remove(amount);
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(player, stack);
    }

    protected void checkTakeAchievements(ItemStack stack, int amount) {
        this.craftBuffer += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player.level(), this.player, this.craftBuffer);
        checkAchievements(this.player, stack);
        this.craftBuffer = 0;
    }
}
