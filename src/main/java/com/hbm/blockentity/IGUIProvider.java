package com.hbm.blockentity;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IGUIProvider {
    Container provideContainer(int ID, Player player, Level level, int x, int y, int z);
    Object provideGUI(int ID, Player player, Level level, int x, int y, int z);
}
