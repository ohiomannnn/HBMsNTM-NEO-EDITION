package com.hbm.handler.radiation;

import com.hbm.config.ServerConfig;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class ChunkRadiationManager {

    public static ChunkRadiationHandler proxy = new ChunkRadiationHandlerSimple();
    private int eggTimer = 0;

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
        if (ServerConfig.ENABLE_CHUNK_RADS.getAsBoolean()) {
            proxy.receiveWorldLoad(event);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        if (ServerConfig.ENABLE_CHUNK_RADS.getAsBoolean()) {
            proxy.receiveWorldUnload(event);
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event) {
        if (ServerConfig.ENABLE_CHUNK_RADS.getAsBoolean()) {
            proxy.receiveChunkLoad(event);
        }
    }

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event) {
        if (ServerConfig.ENABLE_CHUNK_RADS.getAsBoolean()) {
            proxy.receiveChunkSave(event);
        }
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        if (ServerConfig.ENABLE_CHUNK_RADS.getAsBoolean()) {
            proxy.receiveChunkUnload(event);
        }
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        if (!ServerConfig.ENABLE_CHUNK_RADS.getAsBoolean()) return;

        eggTimer++;

        if (eggTimer >= 20) {
            proxy.updateSystem();
            eggTimer = 0;
        }

        if (ServerConfig.WORLD_RAD_EFFECTS.getAsBoolean()) {
            proxy.handleWorldDestruction();
        }

        proxy.receiveWorldTick(event);
    }
}
