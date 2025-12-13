package com.hbm.blockentity;

import net.minecraft.client.gui.Gui;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IGUIProvider {
    Container provideContainer(int ID, Player player, Level level, BlockPos pos);
    Object provideGUI(int ID, Player player, Level level, BlockPos pos);
}
