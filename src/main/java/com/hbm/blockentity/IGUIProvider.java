package com.hbm.blockentity;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public interface IGUIProvider {
    Screen provideScreenOnRightClick(Player player, BlockPos pos);
}
