package com.hbm.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.neoforged.neoforge.common.world.chunk.TicketController;

import java.util.Comparator;
import java.util.List;

public class WorldUtil {

    private static final TicketType<BlockPos> SPAWN_TICKET = TicketType.create(
            "entity_spawn_temp",
            Comparator.comparingLong(BlockPos::asLong),
            20
    );


    public static void loadAndSpawnEntityInWorld(Entity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {

            ChunkPos chunkPos = entity.chunkPosition();
            BlockPos blockPos = entity.blockPosition();

            serverLevel.getChunkSource().addRegionTicket(
                    SPAWN_TICKET,
                    chunkPos,
                    2,
                    blockPos
            );

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

            biomes.getAndSetUnchecked(localX, 0, localZ, biome);
            biomes.getAndSetUnchecked(localX, 1, localZ, biome);
            biomes.getAndSetUnchecked(localX, 2, localZ, biome);
            biomes.getAndSetUnchecked(localX, 3, localZ, biome);
        }

        chunk.setUnsaved(true);
    }
    public static void flushChunk(ServerLevel level, LevelChunk chunk) {
        level.getChunkSource().chunkMap.resendBiomesForChunks(List.of(chunk));
    }
}
