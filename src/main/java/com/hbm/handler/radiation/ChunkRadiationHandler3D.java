package com.hbm.handler.radiation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

public class ChunkRadiationHandler3D extends ChunkRadiationHandler {

    private final Set<LevelChunk> loadedChunks = new HashSet<>();

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        if (!event.getLevel().isClientSide() && event.getChunk() instanceof LevelChunk chunk) {
            loadedChunks.add(chunk);
        }
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        if (!event.getLevel().isClientSide() && event.getChunk() instanceof LevelChunk chunk) {
            loadedChunks.remove(chunk);
        }
    }

    private final HashMap<Level, ThreeDimRadiationPerWorld> perWorld = new HashMap<>();

    @Override
    public float getRadiation(Level world, int x, int y, int z) {
        ThreeDimRadiationPerWorld radWorld = perWorld.get(world);
        if (radWorld != null) {
            ChunkPos coords = new ChunkPos(x >> 4, z >> 4);
            int yReg = Math.max(0, Math.min(y >> 4, 15));
            Float[] vals = radWorld.radiation.get(coords);
            if (vals == null) return 0F;
            return vals[yReg] == null ? 0F : vals[yReg];
        }
        return 0F;
    }

    @Override
    public void setRadiation(Level world, int x, int y, int z, float rad) {
        ThreeDimRadiationPerWorld radWorld = perWorld.get(world);
        if (radWorld != null) {
            ChunkPos coords = new ChunkPos(x >> 4, z >> 4);
            int yReg = Math.max(0, Math.min(y >> 4, 15));
            Float[] vals = radWorld.radiation.computeIfAbsent(coords, k -> new Float[16]);
            vals[yReg] = rad;
            LevelChunk chunk = world.getChunk(coords.x, coords.z);
            chunk.setUnsaved(true);
        }
    }

    @Override
    public void incrementRad(Level world, int x, int y, int z, float rad) {
        setRadiation(world, x, y, z, getRadiation(world, x, y, z) + rad);
    }

    @Override
    public void decrementRad(Level world, int x, int y, int z, float rad) {
        setRadiation(world, x, y, z, Math.max(getRadiation(world, x, y, z) - rad, 0));
    }

    @Override
    public void updateSystem() {
        for (Map.Entry<Level, ThreeDimRadiationPerWorld> entry : perWorld.entrySet()) {
            Level world = entry.getKey();
            ThreeDimRadiationPerWorld radWorld = entry.getValue();

            HashMap<ChunkPos, Float[]> newRadiation = new HashMap<>();

            for (LevelChunk chunk : loadedChunks) {
                ChunkPos pos = chunk.getPos();

                Float[] vals = radWorld.radiation.get(pos);
                if (vals == null) vals = new Float[16];

                for (int y = 0; y < 16; y++) {
                    float baseRad = vals[y] == null ? 0F : vals[y];

                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            for (int dz = -1; dz <= 1; dz++) {
                                int type = Math.abs(dx) + Math.abs(dy) + Math.abs(dz);
                                if (type == 3) continue;

                                float percent = (type == 0) ? 0.6F : (type == 1) ? 0.075F : 0.025F;
                                int newY = clamp(y + dy, 0, 15);
                                ChunkPos newChunkPos = new ChunkPos(pos.x + dx, pos.z + dz);

                                if (!world.getChunkSource().hasChunk(newChunkPos.x, newChunkPos.z)) continue;

                                Float[] newVals = newRadiation.computeIfAbsent(newChunkPos, k -> new Float[16]);
                                if (newVals[newY] == null) newVals[newY] = 0F;

                                float newRad = newVals[newY] + baseRad * percent;
                                newVals[newY] = Math.max(0F, newRad * 0.999F - 0.05F);
                            }
                        }
                    }
                }
            }

            radWorld.radiation.clear();
            radWorld.radiation.putAll(newRadiation);
        }
    }

    private static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    @Override
    public void clearSystem(Level world) {
        ThreeDimRadiationPerWorld radWorld = perWorld.get(world);
        if (radWorld != null) radWorld.radiation.clear();
    }

    @Override
    public void receiveWorldLoad(LevelEvent.Load event) {
        if (!event.getLevel().isClientSide())
            perWorld.put((Level) event.getLevel(), new ThreeDimRadiationPerWorld());
    }

    @Override
    public void receiveWorldUnload(LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide())
            perWorld.remove((Level) event.getLevel());
    }

    private static final String NBT_KEY_CHUNK_RADIATION = "hfr_3d_radiation_";

    @Override
    public void receiveChunkLoad(ChunkDataEvent.Load event) {
        if (!event.getLevel().isClientSide()) {
            ThreeDimRadiationPerWorld radWorld = perWorld.remove((Level) event.getLevel());
            if (radWorld != null) {
                Float[] vals = new Float[16];
                CompoundTag data = event.getData();
                for (int i = 0; i < 16; i++) vals[i] = data.getFloat(NBT_KEY_CHUNK_RADIATION + i);
                radWorld.radiation.put(event.getChunk().getPos(), vals);
            }
        }
    }

    @Override
    public void receiveChunkSave(ChunkDataEvent.Save event) {
        if (!event.getLevel().isClientSide()) {
            ThreeDimRadiationPerWorld radWorld = perWorld.remove((Level) event.getLevel());
            if (radWorld != null) {
                Float[] vals = radWorld.radiation.get(event.getChunk().getPos());
                if (vals != null) {
                    CompoundTag data = event.getData();
                    for (int i = 0; i < 16; i++) {
                        float rad = vals[i] == null ? 0F : vals[i];
                        data.putFloat(NBT_KEY_CHUNK_RADIATION + i, rad);
                    }
                }
            }
        }
    }

    @Override
    public void receiveChunkUnload(ChunkEvent.Unload event) {
        if (!event.getLevel().isClientSide()) {
            ThreeDimRadiationPerWorld radWorld = perWorld.remove((Level) event.getLevel());
            if (radWorld != null) radWorld.radiation.remove(event.getChunk().getPos());
        }
    }

    public static class ThreeDimRadiationPerWorld {
        public HashMap<ChunkPos, Float[]> radiation = new HashMap<>();
    }
}
