package com.hbm.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public interface IGUIProvider {

    Object provideScreen(Player player, BlockPos pos);
}
