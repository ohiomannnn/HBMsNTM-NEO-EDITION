package com.hbm.handler.radiation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class ChunkRadiationHandlerPRISM extends ChunkRadiationHandler {

    public ConcurrentHashMap<Level, RadPerWorld> perWorld = new ConcurrentHashMap<>();
    public static int cycles = 0;

    public static final float MAX_RADIATION = 1_000_000;
    private static final String NBT_KEY_CHUNK_RADIATION = "hfr_prism_radiation_";
    private static final String NBT_KEY_CHUNK_RESISTANCE = "hfr_prism_resistance_";
    private static final String NBT_KEY_CHUNK_EXISTS = "hfr_prism_exists_";

    @Override
    public float getRadiation(Level world, int x, int y, int z) {
        RadPerWorld system = perWorld.get(world);
        if(system != null) {
            ChunkPos coords = new ChunkPos(x >> 4, z >> 4);
            int yReg = Mth.clamp(y >> 4, 0, 15);
            SubChunk[] subChunks = system.radiation.get(coords);
            if(subChunks != null) {
                SubChunk rad = subChunks[yReg];
                if(rad != null) return rad.radiation;
            }
        }
        return 0;
    }

    @Override
    public void setRadiation(Level world, int x, int y, int z, float rad) {
        if(Float.isNaN(rad)) rad = 0;
        RadPerWorld system = perWorld.get(world);
        if(system != null) {
            ChunkPos coords = new ChunkPos(x >> 4, z >> 4);
            int yReg = Mth.clamp(y >> 4, 0, 15);
            SubChunk[] subChunks = system.radiation.get(coords);
            if(subChunks == null) {
                subChunks = new SubChunk[16];
                system.radiation.put(coords, subChunks);
            }
            if(subChunks[yReg] == null) subChunks[yReg] = new SubChunk().rebuild(world, x, y, z);
            subChunks[yReg].radiation = Mth.clamp(rad, 0, MAX_RADIATION);
            world.getChunk(coords.x, coords.z).setUnsaved(true);
        }
    }

    @Override
    public void incrementRad(Level world, int x, int y, int z, float rad) {
        setRadiation(world, x, y, z, getRadiation(world, x, y, z) + rad);
    }

    @Override
    public void decrementRad(Level world, int x, int y, int z, float rad) {
        setRadiation(world, x, y, z, getRadiation(world, x, y, z) - rad);
    }

    @Override
    public void receiveWorldLoad(LevelEvent.Load event) {
        if(event.getLevel() instanceof ServerLevel level) {
            perWorld.put(level, new RadPerWorld());
        }
    }

    @Override
    public void receiveWorldUnload(LevelEvent.Unload event) {
        if(event.getLevel() instanceof ServerLevel level) {
            perWorld.remove(level);
        }
    }

    @Override
    public void receiveChunkLoad(ChunkDataEvent.Load event) {
        if(event.getLevel() instanceof ServerLevel level) {
            RadPerWorld radWorld = perWorld.get(level);
            if(radWorld != null) {
                SubChunk[] chunk = new SubChunk[16];
                CompoundTag tag = event.getData();
                ChunkPos pos = event.getChunk().getPos();

                for(int i = 0; i < 16; i++) {
                    if(!tag.getBoolean(NBT_KEY_CHUNK_EXISTS + i)) {
                        chunk[i] = new SubChunk().rebuild(level, pos.getMinBlockX(), i << 4, pos.getMinBlockZ());
                        continue;
                    }
                    SubChunk sub = new SubChunk();
                    chunk[i] = sub;
                    sub.radiation = tag.getFloat(NBT_KEY_CHUNK_RADIATION + i);
                    for(int j = 0; j < 16; j++) sub.xResist[j] = tag.getFloat(NBT_KEY_CHUNK_RESISTANCE + "x_" + j + "_" + i);
                    for(int j = 0; j < 16; j++) sub.yResist[j] = tag.getFloat(NBT_KEY_CHUNK_RESISTANCE + "y_" + j + "_" + i);
                    for(int j = 0; j < 16; j++) sub.zResist[j] = tag.getFloat(NBT_KEY_CHUNK_RESISTANCE + "z_" + j + "_" + i);
                }

                radWorld.radiation.put(pos, chunk);
            }
        }
    }

    @Override
    public void receiveChunkSave(ChunkDataEvent.Save event) {
        if(event.getLevel() instanceof ServerLevel level) {
            RadPerWorld radWorld = perWorld.get(level);
            if(radWorld != null) {
                SubChunk[] chunk = radWorld.radiation.get(event.getChunk().getPos());
                if(chunk != null) {
                    CompoundTag tag = event.getData();
                    for(int i = 0; i < 16; i++) {
                        SubChunk sub = chunk[i];
                        if(sub != null) {
                            float rad = sub.radiation;
                            tag.putFloat(NBT_KEY_CHUNK_RADIATION + i, rad);
                            for(int j = 0; j < 16; j++) tag.putFloat(NBT_KEY_CHUNK_RESISTANCE + "x_" + j + "_" + i, sub.xResist[j]);
                            for(int j = 0; j < 16; j++) tag.putFloat(NBT_KEY_CHUNK_RESISTANCE + "y_" + j + "_" + i, sub.yResist[j]);
                            for(int j = 0; j < 16; j++) tag.putFloat(NBT_KEY_CHUNK_RESISTANCE + "z_" + j + "_" + i, sub.zResist[j]);
                            tag.putBoolean(NBT_KEY_CHUNK_EXISTS + i, true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void receiveChunkUnload(ChunkEvent.Unload event) {
        if(event.getLevel() instanceof ServerLevel level) {
            RadPerWorld radWorld = perWorld.get(level);
            if(radWorld != null) {
                radWorld.radiation.remove(event.getChunk().getPos());
            }
        }
    }

    public static final HashMap<ChunkPos, SubChunk[]> newAdditions = new HashMap<>();

    @Override
    public void updateSystem() {
        cycles++;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }

        for (ServerLevel world : server.getAllLevels()) {
            RadPerWorld system = perWorld.get(world);
            if (system == null) continue;

            int rebuildAllowance = 25;

            // сдвигаем radiation → prevRadiation и обнуляем radiation
            for (Entry<ChunkPos, SubChunk[]> chunk : system.radiation.entrySet()) {
                ChunkPos coord = chunk.getKey();

                for (int i = 0; i < 16; i++) {
                    SubChunk sub = chunk.getValue()[i];
                    boolean hasTriedRebuild = false;

                    if (sub != null) {
                        sub.prevRadiation = sub.radiation;
                        sub.radiation = 0;

                        // перестраиваем некоторые сабчанки
                        if (rebuildAllowance > 0 && sub.needsRebuild) {
                            sub.rebuild(world, coord.x << 4, i << 4, coord.z << 4);
                            if (!sub.needsRebuild) {
                                rebuildAllowance--;
                                hasTriedRebuild = true;
                            }
                        }

                        if (!hasTriedRebuild
                                && Math.abs(coord.x * coord.z) % 5 == cycles % 5
                                && world.hasChunk(coord.x, coord.z)) {

                            LevelChunk c = world.getChunk(coord.x, coord.z);
                            LevelChunkSection[] sections = c.getSections();
                            LevelChunkSection subSection = sections[i];
                            int checksum = 0;

                            if (subSection != null && !subSection.hasOnlyAir()) {
                                var states = subSection.getStates();
                                for (int iX = 0; iX < 16; iX++) {
                                    for (int iY = 0; iY < 16; iY++) {
                                        for (int iZ = 0; iZ < 16; iZ++) {
                                            BlockState bs = states.get(iX, iY, iZ);
                                            checksum += Block.getId(bs);
                                        }
                                    }
                                }
                            }

                            if (checksum != sub.checksum) {
                                sub.rebuild(world, coord.x << 4, i << 4, coord.z << 4);
                            }
                        }
                    }
                }
            }

            Iterator<Entry<ChunkPos, SubChunk[]>> it = system.radiation.entrySet().iterator();
            while (it.hasNext()) {
                Entry<ChunkPos, SubChunk[]> chunk = it.next();
                if (getPrevChunkRadiation(chunk.getValue()) <= 0) continue;

                for (int i = 0; i < 16; i++) {
                    SubChunk sub = chunk.getValue()[i];

                    if (sub != null) {
                        if (sub.prevRadiation <= 0 || Float.isNaN(sub.prevRadiation) || Float.isInfinite(sub.prevRadiation)) continue;

                        float radSpread = 0;
                        for (Direction dir : Direction.values()) {
                            radSpread += spreadRadiation(world, sub, i, chunk.getKey(), chunk.getValue(), system.radiation, dir);
                        }

                        sub.radiation += (sub.prevRadiation - radSpread) * 0.95F;
                        sub.radiation -= 1F;
                        sub.radiation = Mth.clamp(sub.radiation, 0, MAX_RADIATION);
                    }
                }
            }

            system.radiation.putAll(newAdditions);
            newAdditions.clear();
        }
    }
    private float spreadRadiation(ServerLevel world, SubChunk sub, int subY, ChunkPos pos,
                                  SubChunk[] chunkSubs, Map<ChunkPos, SubChunk[]> chunkMap, Direction dir) {
        // соседние координаты чанка
        int dx = dir.getStepX();
        int dy = dir.getStepY();
        int dz = dir.getStepZ();

        int targetY = subY + dy;
        ChunkPos targetPos = new ChunkPos(pos.x + dx, pos.z + dz);

        // ищем массив сабчанков соседа
        SubChunk[] targetSubs = (dx == 0 && dz == 0) ? chunkSubs : chunkMap.get(targetPos);
        if (targetSubs == null) return 0;

        // проверка выхода за границы по Y
        if (targetY < 0 || targetY >= targetSubs.length) return 0;

        SubChunk target = targetSubs[targetY];
        if (target == null) return 0;

        float available = sub.prevRadiation / 6F; // равномерно делим на направления
        if (available <= 0) return 0;

        // коэффициент прозрачности: чем выше resistance, тем меньше проходит
        float resistance = (sub.getTransparency(dir) + target.getTransparency(dir.getOpposite())) / 2F;
        float spread = available * (1F - resistance);

        if (spread > 0) {
            target.radiation += spread;
            return spread;
        }

        return 0;
    }

    private static float getPrevChunkRadiation(SubChunk[] chunk) { float rad = 0; for(SubChunk sub : chunk) if(sub != null) rad += sub.prevRadiation; return rad; }

    @Override
    public void clearSystem(Level world) {
        RadPerWorld system = perWorld.get(world);
        if(system != null) system.radiation.clear();
    }

    public static class RadPerWorld {
        public ConcurrentHashMap<ChunkPos, SubChunk[]> radiation = new ConcurrentHashMap<>();
    }

    public static class SubChunk {

        public float prevRadiation;  // предыдущий тик
        public float radiation;      // текущая радиация
        public float[] xResist = new float[16];
        public float[] yResist = new float[16];
        public float[] zResist = new float[16];
        public boolean needsRebuild = false;
        public int checksum = 0;

        private Level level;
        private int baseX, baseY, baseZ;

        public SubChunk rebuild(Level level, int x, int y, int z) {
            this.level = level;
            this.baseX = (x >> 4) << 4;
            this.baseY = Mth.clamp(y >> 4, 0, 15) << 4;
            this.baseZ = (z >> 4) << 4;

            needsRebuild = false;
            return this;
        }

//        public float getResistanceValue(Library.Direction movement) {
//            // TODO: заменить ForgeDirection на свой аналог
//            return 0;
//        }

        public float getTransparency(Direction dir) {
            if (level == null) return 0;
            int x = baseX + 8;
            int y = baseY + 8;
            int z = baseZ + 8;

            BlockPos pos = new BlockPos(x, y, z).relative(dir);
            BlockState state = level.getBlockState(pos);

            if (state.isAir()) return 0F;
            if (!state.getFluidState().isEmpty()) return 0.5F;
            if (state.is(BlockTags.LEAVES)) return 0.3F;

            int lightBlock = state.getLightBlock(level, pos);
            return Mth.clamp(lightBlock / 15.0F, 0.0F, 1.0F);
        }
    }
}
