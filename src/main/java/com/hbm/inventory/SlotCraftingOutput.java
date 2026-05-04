package com.hbm.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotCraftingOutput extends Slot {

    private final Player player;
    private int craftBuffer;

    public SlotCraftingOutput(Player player, Container container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack remove(int amount) {
        if(this.hasItem()) {
            this.craftBuffer += Math.min(amount, this.getItem().getCount());
        }
        return super.remove(amount);
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(player, stack);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.craftBuffer += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        if (this.craftBuffer > 0) {
            stack.onCraftedBy(this.player.level(), this.player, this.craftBuffer);
        }
        this.craftBuffer = 0;
    }
}
