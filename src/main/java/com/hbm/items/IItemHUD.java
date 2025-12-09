package com.hbm.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public interface IItemHUD {
    @OnlyIn(Dist.CLIENT) void renderHUD(RenderGuiEvent.Pre event, Player player, ItemStack stack);
}
