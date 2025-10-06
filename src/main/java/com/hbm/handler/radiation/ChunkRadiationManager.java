package com.hbm.handler.radiation;

import com.hbm.config.ModConfigs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class ChunkRadiationManager {

    // for now its final TODO: make another handlers
    public static final ChunkRadiationHandler proxy = new ChunkRadiationHandlerSimple();

    private static int eggTimer = 0;

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) return;
        if (!ModConfigs.COMMON.ENABLE_CHUNK_RADS.get()) return;
        proxy.receiveWorldLoad(event);
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) return;
        if (!ModConfigs.COMMON.ENABLE_CHUNK_RADS.get()) return;
        proxy.receiveWorldUnload(event);
    }

    @SubscribeEvent
    public void onChunkDataLoad(ChunkDataEvent.Load event) {
        if (event.getLevel() == null) return;
        if (event.getLevel().isClientSide()) return;
        if (!ModConfigs.COMMON.ENABLE_CHUNK_RADS.get()) return;
        proxy.receiveChunkLoad(event);
    }

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event) {
        if (event.getLevel() == null) return;
        if (event.getLevel().isClientSide()) return;
        if (!ModConfigs.COMMON.ENABLE_CHUNK_RADS.get()) return;
        proxy.receiveChunkSave(event);
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        if (event.getLevel().isClientSide()) return;
        if (!ModConfigs.COMMON.ENABLE_CHUNK_RADS.get()) return;
        proxy.receiveChunkUnload(event);
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Pre event) {
        if (!ModConfigs.COMMON.ENABLE_CHUNK_RADS.get()) return;

        eggTimer++;
        if (eggTimer >= 20) {
            proxy.updateSystem();
            eggTimer = 0;
        }

        if (ModConfigs.COMMON.WORLD_RAD_EFFECTS.get()) {
            proxy.handleWorldDestruction();
        }

        proxy.receiveWorldTick(event);
    }
}
