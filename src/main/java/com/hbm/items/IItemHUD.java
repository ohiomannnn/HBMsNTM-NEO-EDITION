package com.hbm.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public interface IItemHUD {
    void renderHUD(RenderGuiEvent.Pre event, Player player, ItemStack stack);
}
