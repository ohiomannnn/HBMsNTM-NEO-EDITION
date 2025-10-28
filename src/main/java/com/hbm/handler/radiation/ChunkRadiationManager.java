package com.hbm.handler.radiation;

import com.hbm.HBMsNTM;
import com.hbm.config.MainConfig;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = HBMsNTM.MODID)
public class ChunkRadiationManager {

    // for now its final
    // TODO: make another handlers
    public static final ChunkRadiationHandler proxy = new ChunkRadiationHandlerSimple();

    private static int eggTimer = 0;

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) return;
        if (!MainConfig.COMMON.ENABLE_CHUNK_RADS.get()) return;
        proxy.receiveWorldLoad(event);
    }

    @SubscribeEvent
    public static void onWorldUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) return;
        if (!MainConfig.COMMON.ENABLE_CHUNK_RADS.get()) return;
        proxy.receiveWorldUnload(event);
    }

    @SubscribeEvent
    public static void onChunkDataLoad(ChunkDataEvent.Load event) {
        if (event.getLevel() == null) return;
        if (event.getLevel().isClientSide()) return;
        if (!MainConfig.COMMON.ENABLE_CHUNK_RADS.get()) return;
        proxy.receiveChunkLoad(event);
    }

    @SubscribeEvent
    public static void onChunkSave(ChunkDataEvent.Save event) {
        if (event.getLevel() == null) return;
        if (event.getLevel().isClientSide()) return;
        if (!MainConfig.COMMON.ENABLE_CHUNK_RADS.get()) return;
        proxy.receiveChunkSave(event);
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        if (event.getLevel().isClientSide()) return;
        if (!MainConfig.COMMON.ENABLE_CHUNK_RADS.get()) return;
        proxy.receiveChunkUnload(event);
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {
        if (!MainConfig.COMMON.ENABLE_CHUNK_RADS.get()) return;

        eggTimer++;
        if (eggTimer >= 20) {
            proxy.updateSystem();
            eggTimer = 0;
        }

        if (MainConfig.COMMON.WORLD_RAD_EFFECTS.get()) {
            proxy.handleWorldDestruction();
        }

        proxy.receiveWorldTick(event);
    }
}
