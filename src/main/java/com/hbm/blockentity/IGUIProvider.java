package com.hbm.blockentity;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IGUIProvider {
    @OnlyIn(Dist.CLIENT) Screen provideScreenOnRightClick(Player player, BlockPos pos);
}
