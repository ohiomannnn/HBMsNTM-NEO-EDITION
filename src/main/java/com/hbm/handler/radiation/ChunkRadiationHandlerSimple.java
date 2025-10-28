package com.hbm.handler.radiation;

import java.util.HashMap;
import java.util.Map.Entry;

import com.hbm.HBMsNTMClient;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.MainConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

/**
 * Most basic implementation of a chunk radiation system: Each chunk has a radiation value which spreads out to its neighbors.
 * @author hbm
 */
public class ChunkRadiationHandlerSimple extends ChunkRadiationHandler {

    private final HashMap<Level, SimpleRadiationPerWorld> perWorld = new HashMap<>();
    private static final float maxRad = 100_000F;

    @Override
    public float getRadiation(Level level, int x, int y, int z) {
        SimpleRadiationPerWorld radWorld = perWorld.get(level);

        if (radWorld != null) {
            ChunkPos coords = new ChunkPos(x >> 4, z >> 4);
            Float rad = radWorld.radiation.get(coords);
            return rad == null ? 0F : Mth.clamp(rad, 0, maxRad);
        }

        return 0;
    }

    @Override
    public void setRadiation(Level level, int x, int y, int z, float rad) {
        SimpleRadiationPerWorld radWorld = perWorld.get(level);

        if (radWorld != null) {
            if (level.isLoaded(new BlockPos(x, y, z))) {
                ChunkPos coords = new ChunkPos(x >> 4, z >> 4);
                radWorld.radiation.put(coords, Mth.clamp(rad, 0, maxRad));
                level.getChunk(coords.x, coords.z).setUnsaved(true);
            }
        }
    }

    @Override
    public void incrementRad(Level level, int x, int y, int z, float rad) {
        setRadiation(level, x, y, z, getRadiation(level, x, y, z) + rad);
    }

    @Override
    public void decrementRad(Level level, int x, int y, int z, float rad) {
        setRadiation(level, x, y, z, Math.max(getRadiation(level, x, y, z) - rad, 0));
    }

    @Override
    public void updateSystem() {

        for (Entry<Level, SimpleRadiationPerWorld> entry : perWorld.entrySet()) {

            HashMap<ChunkPos, Float> radiation = entry.getValue().radiation;
            HashMap<ChunkPos, Float> buff = new HashMap<>(radiation);
            radiation.clear();
            Level level = entry.getKey();

            for (Entry<ChunkPos, Float> chunk : buff.entrySet()) {

                if (chunk.getValue() == 0) continue;

                ChunkPos coord = chunk.getKey();

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {

                        int type = Math.abs(i) + Math.abs(j);
                        float percent = type == 0 ? 0.6F : type == 1 ? 0.075F : 0.025F;
                        ChunkPos newCoord = new ChunkPos(coord.x + i, coord.z + j);

                        if (buff.containsKey(newCoord)) {
                            Float val = radiation.get(newCoord);
                            float rad = val == null ? 0 : val;
                            float newRad = rad + chunk.getValue() * percent;
                            newRad = Mth.clamp(0F, newRad * 0.99F - 0.05F, maxRad);
                            radiation.put(newCoord, newRad);
                        } else {
                            radiation.put(newCoord, chunk.getValue() * percent);
                        }

                        float rad = radiation.get(newCoord);
                        if (rad > MainConfig.COMMON.FOG_RAD.get() && level != null && level.random.nextInt(MainConfig.COMMON.FOG_RAD_CH.get()) == 0 && level.hasChunk(coord.x, coord.z)) {

                            int x = newCoord.getMinBlockX() + level.random.nextInt(16);
                            int z = newCoord.getMinBlockZ() + level.random.nextInt(16);
                            int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) + level.random.nextInt(5);

                            CompoundTag data = new CompoundTag();
                            data.putString("type", "radFog");
                            data.putDouble("posX", x);
                            data.putDouble("posY", y);
                            data.putDouble("posZ", z);
                            HBMsNTMClient.effectNT(data);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void clearSystem(Level level) {
        SimpleRadiationPerWorld radWorld = perWorld.get(level);

        if (radWorld != null) {
            radWorld.radiation.clear();
        }
    }

    @Override
    public void receiveWorldLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            perWorld.put(serverLevel, new SimpleRadiationPerWorld());
        }
    }

    @Override
    public void receiveWorldUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            perWorld.remove(serverLevel);
        }
    }

    private static final String NBT_KEY_CHUNK_RADIATION = "hfr_simple_radiation";

    @Override
    public void receiveChunkLoad(ChunkDataEvent.Load event) {
        Level level = (Level) event.getLevel();
        if (level.isClientSide) return;
        SimpleRadiationPerWorld radWorld = perWorld.get(level);

        if (radWorld != null) {
            radWorld.radiation.put(event.getChunk().getPos(), event.getData().getFloat(NBT_KEY_CHUNK_RADIATION));
        }
    }

    @Override
    public void receiveChunkSave(ChunkDataEvent.Save event) {
        Level level = (Level) event.getLevel();
        if (level.isClientSide) return;
        SimpleRadiationPerWorld radWorld = perWorld.get(level);

        if (radWorld != null) {
            Float val = radWorld.radiation.get(event.getChunk().getPos());
            float rad = val == null ? 0F : val;
            event.getData().putFloat(NBT_KEY_CHUNK_RADIATION, rad);
        }
    }

    @Override
    public void receiveChunkUnload(ChunkEvent.Unload event) {
        Level level = (Level) event.getLevel();
        if (level.isClientSide) return;
        SimpleRadiationPerWorld radWorld = perWorld.get(level);

        if (radWorld != null) {
            radWorld.radiation.remove(event.getChunk().getPos());
        }
    }

    public static class SimpleRadiationPerWorld {
        public HashMap<ChunkPos, Float> radiation = new HashMap<>();
    }

    @Override
    public void handleWorldDestruction() {

        int count = 10;
        int threshold = 10;
        int chunks = 5;

        for (Entry<Level, SimpleRadiationPerWorld> per : perWorld.entrySet()) {

            Level level = per.getKey();
            SimpleRadiationPerWorld list = per.getValue();

            Object[] entries = list.radiation.entrySet().toArray();
            if (entries.length == 0) continue;

            for (int c = 0; c < chunks; c++) {

                Entry<ChunkPos, Float> randEnt = (Entry<ChunkPos, Float>) entries[level.random.nextInt(entries.length)];

                ChunkPos coords = randEnt.getKey();
                ServerLevel serverLevel = (ServerLevel) level;

                //choose this many random locations within the chunk
                for (int i = 0; i < count; i++) {

                    if(randEnt == null || randEnt.getValue() < threshold) continue;

                    if (serverLevel.hasChunk(coords.x, coords.z)) {

                        for (int a = 0; a < 16; a++) {
                            for (int b = 0; b < 16; b++) {
                                if (level.random.nextInt(3) != 0) continue;

                                int x = coords.getMinBlockX() + a;
                                int z = coords.getMinBlockZ() + b;
                                int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) - level.random.nextInt(2);

                                BlockPos pos = new BlockPos(x, y, z);
                                BlockState state = level.getBlockState(pos);

                                if (state.is(Blocks.GRASS_BLOCK)) {
                                    level.setBlock(pos, ModBlocks.WASTE_EARTH.get().defaultBlockState(), 3);

                                } else if (state.is(Blocks.TALL_GRASS)) {
                                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

                                } else if (state.is(BlockTags.LEAVES)) {
                                    if (level.random.nextInt(7) <= 5) {
                                        level.setBlock(pos, ModBlocks.WASTE_LEAVES.get().defaultBlockState(), 3);
                                    } else {
                                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}