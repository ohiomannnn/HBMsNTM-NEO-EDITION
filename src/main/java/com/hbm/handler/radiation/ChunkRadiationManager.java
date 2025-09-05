package com.hbm.handler.radiation;

import com.hbm.HBMsNTM;
import com.hbm.config.ServerConfig;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class ChunkRadiationManager {

    public static ChunkRadiationHandler proxy;

    public static void initProxy() {
        if (ServerConfig.ENABLE_PRISM_RAD.getAsBoolean()) {
            proxy = new ChunkRadiationHandlerPRISM();
            HBMsNTM.LOGGER.info("Using PRISM radiation system");
        } else {
            proxy = new ChunkRadiationHandlerSimple();
            HBMsNTM.LOGGER.info("Using simple radiation system");
        }
    }
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
