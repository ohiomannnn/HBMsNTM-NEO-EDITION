package com.hbm.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;

public interface IGUIProvider {
    AbstractContainerMenu provideContainer(int ID, Player player, Level level, BlockPos pos);
    Object provideGUI(int ID, Player player, Level level, int x, int y, int z);
}
