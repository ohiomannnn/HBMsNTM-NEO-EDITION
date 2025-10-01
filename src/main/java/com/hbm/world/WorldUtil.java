package com.hbm.world;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.status.ChunkStatus;

import java.util.List;

public class WorldUtil {

    public static boolean isPlant(BlockState state) {
        return state.is(BlockTags.CROPS) || state.is(BlockTags.FLOWERS) || state.is(BlockTags.LEAVES) || state.is(BlockTags.SAPLINGS);
    }

    public static void loadAndSpawnEntityInWorld(Entity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {

            int chunkX = entity.chunkPosition().x;
            int chunkZ = entity.chunkPosition().z;

            serverLevel.getChunk(chunkX, chunkZ, ChunkStatus.FULL, true);

            serverLevel.addFreshEntity(entity);
        }
    }

    public static void setBiomeColumn(ServerLevel level, int blockX, int blockZ, ResourceKey<Biome> biomeKey) {
        Registry<Biome> biomeRegistry = level.registryAccess().registryOrThrow(Registries.BIOME);
        Holder<Biome> biome = biomeRegistry.getHolderOrThrow(biomeKey);

        LevelChunk chunk = level.getChunk(blockX >> 4, blockZ >> 4);
        int localX = (blockX & 15) >> 2;
        int localZ = (blockZ & 15) >> 2;

        int minQuartY = level.getMinBuildHeight() >> 2;
        int maxQuartY = level.getMaxBuildHeight() >> 2;

        for (int quartY = minQuartY; quartY < maxQuartY; quartY++) {
            int blockY = quartY << 2;
            int sectionIndex = (blockY >> 4) - level.getMinSection();
            if (sectionIndex < 0 || sectionIndex >= chunk.getSectionsCount()) continue;

            LevelChunkSection section = chunk.getSection(sectionIndex);
            int localY = quartY & 3;

            PalettedContainer<Holder<Biome>> biomes = (PalettedContainer<Holder<Biome>>) section.getBiomes();

            biomes.getAndSetUnchecked(localX, localY, localZ, biome);
        }
        chunk.setUnsaved(true);
    }

    public static void flushChunk(ServerLevel level, LevelChunk chunk) {
        level.getChunkSource().chunkMap.resendBiomesForChunks(List.of(chunk));
    }
}
