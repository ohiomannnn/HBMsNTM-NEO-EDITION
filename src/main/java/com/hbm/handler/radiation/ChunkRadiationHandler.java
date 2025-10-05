package com.hbm.handler.radiation;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

public abstract class ChunkRadiationHandler {
    public abstract void updateSystem();
    public abstract float getRadiation(Level level, int x, int y, int z);
    public abstract void setRadiation(Level level, int x, int y, int z, float rad);
    public abstract void incrementRad(Level level, int x, int y, int z, float rad);
    public abstract void decrementRad(Level level, int x, int y, int z, float rad);
    public abstract void clearSystem(Level level);

    public void receiveWorldLoad(LevelEvent.Load event) {}
    public void receiveWorldUnload(LevelEvent.Unload event) {}
    public void receiveWorldTick(ServerTickEvent event) {}

    public void receiveChunkLoad(ChunkDataEvent.Load event) {}
    public void receiveChunkSave(ChunkDataEvent.Save event) {}
    public void receiveChunkUnload(ChunkEvent.Unload event) {}

    public void handleWorldDestruction() {}
}