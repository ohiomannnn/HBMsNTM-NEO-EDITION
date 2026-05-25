package com.hbm.items.weapon.sedna.hud;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public interface IHUDComponent {

    int getComponentHeight(Player player, ItemStack stack);
    void renderHUDComponent(RenderGuiEvent.Pre event, Player player, ItemStack stack, int bottomOffset, int gunIndex);
}
