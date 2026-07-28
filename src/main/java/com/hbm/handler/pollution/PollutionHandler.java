package com.hbm.handler.pollution;

import com.hbm.config.NtmConfig;
import com.hbm.inventory.NtmTags;
import com.hbm.main.NuclearTechMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

@EventBusSubscriber
public class PollutionHandler {

    public static final String fileName = "hbmpollution.dat";

    public static final HashMap<Level, PollutionPerLevel> perLevel = new HashMap<>();

    /** Baserate of soot generation for a furnace-equivalent machine per second */
    public static final float SOOT_PER_SECOND = 1F / 25F;
    /** Baserate of heavy metal generation, balanced around the soot values of combustion engines */
    public static final float HEAVY_METAL_PER_SECOND = 1F / 50F;
    /** Baserate for poison when spilled */
    public static final float POISON_PER_SECOND = 1F / 50F;
    public static Vec3 targetCoords;

    ///////////////////////
    /// UTILITY METHODS ///
    ///////////////////////
    public static void incrementPollution(Level level, BlockPos toChange, PollutionType type, float amount) {

        if(!NtmConfig.COMMON.ENABLE_POLLUTION.get()) return;

        PollutionPerLevel ppw = perLevel.get(level);
        if(ppw == null) return;
        ChunkPos pos = new ChunkPos(toChange);
        PollutionData data = ppw.pollution.get(pos);
        if(data == null) {
            data = new PollutionData();
            ppw.pollution.put(pos, data);
        }
        data.pollution[type.ordinal()] = Math.clamp((float) (data.pollution[type.ordinal()] + amount * NtmConfig.COMMON.POLLUTION_MULT.get()), 0F, 10_000F);
    }

    public static void decrementPollution(Level level, BlockPos toChange, PollutionType type, float amount) {
        incrementPollution(level, toChange, type, -amount);
    }

    public static void setPollution(Level level, BlockPos toChange, PollutionType type, float amount) {

        if(!NtmConfig.COMMON.ENABLE_POLLUTION.get()) return;

        PollutionPerLevel ppw = perLevel.get(level);
        if(ppw == null) return;
        ChunkPos pos = new ChunkPos(toChange);
        PollutionData data = ppw.pollution.get(pos);
        if(data == null) {
            data = new PollutionData();
            ppw.pollution.put(pos, data);
        }
        data.pollution[type.ordinal()] = amount;
    }

    public static float getPollution(Level level, BlockPos toChange, PollutionType type) {

        if(!NtmConfig.COMMON.ENABLE_POLLUTION.get()) return 0;

        PollutionPerLevel ppw = perLevel.get(level);
        if(ppw == null) return 0F;
        ChunkPos pos = new ChunkPos(toChange);
        PollutionData data = ppw.pollution.get(pos);
        if(data == null) return 0F;
        return data.pollution[type.ordinal()];
    }

    public static PollutionData getPollutionData(Level level, BlockPos at) {

        if(!NtmConfig.COMMON.ENABLE_POLLUTION.get()) return null;

        PollutionPerLevel ppw = perLevel.get(level);
        if(ppw == null) return null;
        ChunkPos pos = new ChunkPos(at);
        return ppw.pollution.get(pos);
    }

    //////////////////////
    /// EVENT HANDLING ///
    //////////////////////
    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if(!event.getLevel().isClientSide() && NtmConfig.COMMON.ENABLE_POLLUTION.get() && event.getLevel() instanceof ServerLevel level) {

            String dirPath = getDataDir(level);

            try {
                File pollutionFile = new File(dirPath, fileName);

                if(pollutionFile.exists()) {
                    try(FileInputStream io = new FileInputStream(pollutionFile)) {
                        CompoundTag data = NbtIo.readCompressed(io, NbtAccounter.unlimitedHeap());
                        perLevel.put(level, new PollutionPerLevel(data));
                    }
                } else {
                    perLevel.put(level, new PollutionPerLevel());
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if(!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel level) perLevel.remove(level);
    }

    @SubscribeEvent
    public static void onLevelSave(LevelEvent.Save event) {
        if(!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel level) {
            String dirPath = getDataDir(level);
            File pollutionFile = new File(dirPath, fileName);

            try {
                if(!pollutionFile.getParentFile().exists()) pollutionFile.getParentFile().mkdirs();
                if(!pollutionFile.exists()) pollutionFile.createNewFile();

                PollutionPerLevel ppw = perLevel.get(level);
                if(ppw != null) {
                    CompoundTag data = ppw.writeToNBT();
                    try(FileOutputStream out = new FileOutputStream(pollutionFile)) {
                        NbtIo.writeCompressed(data, out);
                    }
                }
            } catch(Exception ex) {
                NuclearTechMod.LOGGER.info("Failed to write {}", pollutionFile.getAbsolutePath());
                ex.printStackTrace();
            }
        }
    }

    public static String getDataDir(ServerLevel level) {
        Path rootDir = level.getServer().getWorldPath(LevelResource.ROOT);
        Path dimDir = DimensionType.getStorageFolder(level.dimension(), rootDir);

        return dimDir.resolve("data").toAbsolutePath().toString();
    }


    //////////////////////////
    /// SYSTEM UPDATE LOOP ///
    //////////////////////////
    private static int eggTimer = 0;
    @SubscribeEvent
    public static void updateSystem(ServerTickEvent.Post event) {

        handleWorldDestruction();

        eggTimer++;
        if(eggTimer < 60) return;
        eggTimer = 0;

        for(Entry<Level, PollutionPerLevel> entry : perLevel.entrySet()) {
            HashMap<ChunkPos, PollutionData> newPollution = new HashMap<>();

            for(Entry<ChunkPos, PollutionData> chunk : entry.getValue().pollution.entrySet()) {
                int x = chunk.getKey().x;
                int z = chunk.getKey().z;
                PollutionData data = chunk.getValue();

                float[] pollutionForNeightbors = new float[PollutionType.values().length];
                int S = PollutionType.SOOT.ordinal();
                int H = PollutionType.HEAVYMETAL.ordinal();
                int P = PollutionType.POISON.ordinal();

                /* CALCULATION */
                if(data.pollution[S] > 10) {
                    pollutionForNeightbors[S] = data.pollution[S] * 0.05F;
                    data.pollution[S] *= 0.8F;
                }

                data.pollution[S] *= 0.99F;
                data.pollution[H] *= 0.9995F;

                if(data.pollution[P] > 10) {
                    pollutionForNeightbors[P] = data.pollution[P] * 0.025F;
                    data.pollution[P] *= 0.9F;
                } else {
                    data.pollution[P] *= 0.995F;
                }

                /* SPREADING */
                //apply new data to self
                PollutionData newData = newPollution.get(chunk.getKey());
                if(newData == null) newData = new PollutionData();

                boolean shouldPut = false;
                for(int i = 0; i < newData.pollution.length; i++) {
                    newData.pollution[i] += data.pollution[i];
                    if(newData.pollution[i] > 0) shouldPut = true;
                }
                if(shouldPut) newPollution.put(chunk.getKey(), newData);

                //apply neighbor data to neighboring chunks
                int[][] offsets = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
                for(int[] offset : offsets) {
                    ChunkPos offPos = new ChunkPos(x + offset[0], z + offset[1]);
                    PollutionData offsetData = newPollution.get(offPos);
                    if(offsetData == null) offsetData = new PollutionData();

                    shouldPut = false;
                    for(int i = 0; i < offsetData.pollution.length; i++) {
                        offsetData.pollution[i] += pollutionForNeightbors[i];
                        if(offsetData.pollution[i] > 0) shouldPut = true;
                    }
                    if(shouldPut) newPollution.put(offPos, offsetData);
                }
            }

            entry.getValue().pollution.clear();
            entry.getValue().pollution.putAll(newPollution);
        }
    }

    protected static final float DESTRUCTION_THRESHOLD = 15F;
    protected static final int DESTRUCTION_COUNT = 5;

    protected static void handleWorldDestruction() {
        for(Entry<Level, PollutionPerLevel> entry : perLevel.entrySet()) {
            Level level = entry.getKey();
            ServerLevel serverLevel = (ServerLevel) level;

            for(Entry<ChunkPos, PollutionData> pollution : entry.getValue().pollution.entrySet()) {

                float poison = pollution.getValue().pollution[PollutionType.POISON.ordinal()];
                if(poison < DESTRUCTION_THRESHOLD) continue;

                ChunkPos entryPos = pollution.getKey();

                for(int i = 0; i < DESTRUCTION_COUNT; i++) {
                    int x = (entryPos.x << 6) + level.random.nextInt(64);
                    int z = (entryPos.z << 6) + level.random.nextInt(64);

                    if(serverLevel.hasChunk(x >> 4, z >> 4)) {
                        int y = level.getHeight(Types.WORLD_SURFACE, x, z) - level.random.nextInt(3) + 1;
                        BlockPos toChange = new BlockPos(x, y, z);
                        BlockState state = level.getBlockState(toChange);

                        if(state.is(Blocks.GRASS_BLOCK)) {
                            level.setBlock(toChange, Blocks.DIRT.defaultBlockState(), 3);
                        } else if(state.is(NtmTags.Blocks.PLANTS) || state.is(BlockTags.LEAVES)) {
                            level.setBlock(toChange, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }

    //////////////////////
    /// DATA STRUCTURE ///
    //////////////////////
    public static class PollutionPerLevel {
        public HashMap<ChunkPos, PollutionData> pollution = new HashMap<>();

        public PollutionPerLevel() { }

        public PollutionPerLevel(CompoundTag tag) {

            ListTag list = tag.getList("Entries", Tag.TAG_COMPOUND);

            for(int i = 0; i < list.size(); i++) {
                CompoundTag nbt = list.getCompound(i);
                int chunkX = nbt.getInt("ChunkX");
                int chunkZ = nbt.getInt("ChunkZ");
                pollution.put(new ChunkPos(chunkX, chunkZ), PollutionData.fromNBT(nbt));
            }
        }

        public CompoundTag writeToNBT() {

            CompoundTag tag = new CompoundTag();

            ListTag list = new ListTag();

            for(Entry<ChunkPos, PollutionData> entry : pollution.entrySet()) {
                CompoundTag nbt = new CompoundTag();
                nbt.putInt("ChunkX", entry.getKey().x);
                nbt.putInt("ChunkZ", entry.getKey().z);
                entry.getValue().toNBT(nbt);
                list.add(nbt);
            }

            tag.put("Entries", list);

            return tag;
        }
    }

    public static class PollutionData {
        public float[] pollution = new float[PollutionType.values().length];

        public static PollutionData fromNBT(CompoundTag tag) {
            PollutionData data = new PollutionData();

            for(int i = 0; i < PollutionType.values().length; i++) {
                data.pollution[i] = tag.getFloat(PollutionType.values()[i].name().toLowerCase(Locale.US));
            }

            return data;
        }

        public void toNBT(CompoundTag tag) {
            for(int i = 0; i < PollutionType.values().length; i++) {
                tag.putFloat(PollutionType.values()[i].name().toLowerCase(Locale.US), pollution[i]);
            }
        }
    }

    public enum PollutionType {
        SOOT, POISON, HEAVYMETAL, FALLOUT
    }

}
