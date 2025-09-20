package com.hbm.handler.radiation;

import com.hbm.HBMsNTM;
import com.hbm.config.ServerConfig;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class ChunkRadiationManager {

    private static ChunkRadiationHandler proxy;

    public static ChunkRadiationHandler getProxy() {
        return proxy;
    }

    private int eggTimer = 0;

    public static void initProxy() {
        if (ServerConfig.ENABLE_PRISM_RAD.getAsBoolean()) {
            proxy = new ChunkRadiationHandlerPRISM();
            HBMsNTM.LOGGER.info("Using PRISM radiation system");
        } else {
            proxy = new ChunkRadiationHandlerSimple();
            HBMsNTM.LOGGER.info("Using simple radiation system");
        }
    }

    private void ensureProxy() {
        if (proxy == null) {
            initProxy();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
        if (!ServerConfig.ENABLE_CHUNK_RADS.getAsBoolean()) return;
        if (event.getLevel().isClientSide()) return;
        ensureProxy();
        proxy.receiveWorldLoad(event);
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        if (!ServerConfig.ENABLE_CHUNK_RADS.getAsBoolean()) return;
        if (event.getLevel().isClientSide()) return;
        ensureProxy();
        proxy.receiveWorldUnload(event);
    }

    @SubscribeEvent
    public void onChunkDataLoad(ChunkDataEvent.Load event) {
        if (!ServerConfig.ENABLE_CHUNK_RADS.getAsBoolean()) return;
        if (event.getLevel() == null) return;
        if (event.getLevel().isClientSide()) return;
        ensureProxy();
        proxy.receiveChunkLoad(event);
    }

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event) {
        if (!ServerConfig.ENABLE_CHUNK_RADS.getAsBoolean()) return;
        if (event.getLevel() == null) return;
        if (event.getLevel().isClientSide()) return;
        ensureProxy();
        proxy.receiveChunkSave(event);
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        if (!ServerConfig.ENABLE_CHUNK_RADS.getAsBoolean()) return;
        if (event.getLevel().isClientSide()) return;
        ensureProxy();
        proxy.receiveChunkUnload(event);
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Pre event) {
        if (!ServerConfig.ENABLE_CHUNK_RADS.getAsBoolean()) return;
        ensureProxy();

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
