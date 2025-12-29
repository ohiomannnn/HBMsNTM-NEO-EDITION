package com.hbm.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

public interface ICustomBlockHighlight {
    @OnlyIn(Dist.CLIENT) boolean shouldDrawHighlight(Level level, BlockPos pos);
    @OnlyIn(Dist.CLIENT) void drawHighlight(RenderHighlightEvent.Block event, Level level, BlockPos pos);
}
