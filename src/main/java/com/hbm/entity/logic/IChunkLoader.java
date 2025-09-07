package com.hbm.entity.logic;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.server.level.TicketType;

public interface IChunkLoader {
    void init();
    void loadChunk(int x, int z);
    void clearChunkLoader();
}