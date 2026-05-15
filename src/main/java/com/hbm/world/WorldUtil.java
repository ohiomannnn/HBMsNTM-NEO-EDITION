package com.hbm.world;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;

import java.util.List;

public class WorldUtil {

    public static void loadAndAddFreshEntity(Entity entity) {
        if(entity.level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().addRegionTicket(TicketType.PORTAL, entity.chunkPosition, 2, entity.blockPosition);
            serverLevel.addFreshEntity(entity);
        }
    }

    public static void setBiomeColumn(ServerLevel level, int x, int z, Holder<Biome> biome) {
        LevelChunk chunk = level.getChunk(x >> 4, z >> 4);
        int localX = (x & 15) >> 2;
        int localZ = (z & 15) >> 2;

        LevelChunkSection[] sections = chunk.getSections();
        for (LevelChunkSection section : sections) {
            PalettedContainer<Holder<Biome>> biomes = (PalettedContainer<Holder<Biome>>) section.getBiomes();

            biomes.set(localX, 0, localZ, biome);
            biomes.set(localX, 1, localZ, biome);
            biomes.set(localX, 2, localZ, biome);
            biomes.set(localX, 3, localZ, biome);
        }

        chunk.setUnsaved(true);
    }
    public static void flushChunk(ServerLevel level, LevelChunk chunk) {
        level.getChunkSource().chunkMap.resendBiomesForChunks(List.of(chunk));
    }
}
