package com.hbm.world;

import com.hbm.HBMsNTM;
import com.hbm.mixin.LevelChunkSectionAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.status.ChunkStatus;

import java.util.List;

public class WorldUtil {
//    // asbdjklserumerwcer
//    public static final BlockState BROWN_MUSHROOM_META10 =
//            Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState()
//                    .setValue(HugeMushroomBlock.UP, true)
//                    .setValue(HugeMushroomBlock.NORTH, true)
//                    .setValue(HugeMushroomBlock.SOUTH, true)
//                    .setValue(HugeMushroomBlock.EAST, true)
//                    .setValue(HugeMushroomBlock.WEST, true)
//                    .setValue(HugeMushroomBlock.DOWN, false);
//    public static final BlockState RED_MUSHROOM_META10 =
//            Blocks.RED_MUSHROOM.defaultBlockState()
//                    .setValue(HugeMushroomBlock.UP, true)
//                    .setValue(HugeMushroomBlock.NORTH, true)
//                    .setValue(HugeMushroomBlock.SOUTH, true)
//                    .setValue(HugeMushroomBlock.EAST, true)
//                    .setValue(HugeMushroomBlock.WEST, true)
//                    .setValue(HugeMushroomBlock.DOWN, false);

    public static void loadAndSpawnEntityInWorld(Entity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {

            int chunkX = entity.chunkPosition().x;
            int chunkZ = entity.chunkPosition().z;

            serverLevel.getChunk(chunkX, chunkZ, ChunkStatus.FULL, true);

            serverLevel.addFreshEntity(entity);
        }
    }
    public static void setBiome(ServerLevel level, BlockPos pos, ResourceKey<Biome> biomeKey) {
        Registry<Biome> biomeRegistry = level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.BIOME);

        Holder<Biome> biomeHolder = biomeRegistry.getHolder(biomeKey)
                .orElseThrow(() -> new IllegalArgumentException("Biome not found: " + biomeKey));

        setBiome(level, pos, biomeHolder);
    }

    public static void setBiome(ServerLevel level, BlockPos pos, Holder<Biome> biome) {
        LevelChunk chunk = level.getChunkAt(pos);
        int sectionIndex = (pos.getY() - level.getMinBuildHeight()) >> 4;
        LevelChunkSection section = chunk.getSection(sectionIndex);

        PalettedContainer<Holder<Biome>> mutable = section.getBiomes().recreate();

        int x = (pos.getX() & 15) >> 2;
        int y = (pos.getY() & 15) >> 2;
        int z = (pos.getZ() & 15) >> 2;

        mutable.getAndSetUnchecked(x, y, z, biome);

        try {
            if (section instanceof LevelChunkSectionAccessor accessor) {
                accessor.setBiomes(mutable);
            } else {
                HBMsNTM.LOGGER.error("no mixin");
            }
        } catch (Throwable t) {
            HBMsNTM.LOGGER.error("cannot change biome", t);
        }

        chunk.setUnsaved(true);
    }

    public static void setBiomeColumn(ServerLevel level, int blockX, int blockZ, ResourceKey<Biome> biomeKey) {
        Registry<Biome> biomeRegistry = level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.BIOME);
        Holder<Biome> biome = biomeRegistry.getHolderOrThrow(biomeKey);

        LevelChunk chunk = level.getChunk(blockX >> 4, blockZ >> 4);
        int localX = (blockX & 15) >> 2;
        int localZ = (blockZ & 15) >> 2;

        int minQuartY = level.getMinBuildHeight() >> 2;
        int maxQuartY = level.getMaxBuildHeight() >> 2;

        for (int quartY = minQuartY; quartY < maxQuartY; quartY++) {
            int blockY = quartY << 2;
            int sectionIndex = (blockY >> 4) - level.getMinSection();
            if (sectionIndex < 0 || sectionIndex >= chunk.getSectionsCount()) {
                continue;
            }

            LevelChunkSection section = chunk.getSection(sectionIndex);

            int localY = quartY & 3;

            @SuppressWarnings("unchecked")
            PalettedContainer<Holder<Biome>> biomes = (PalettedContainer<Holder<Biome>>) (Object) section.getBiomes();
            biomes.getAndSetUnchecked(localX, localY, localZ, biome);
        }

        chunk.setUnsaved(true);
        level.getChunkSource().chunkMap.resendBiomesForChunks(List.of(chunk));
//        for (int quartY = minQuartY; quartY < maxQuartY; quartY++) {
//            Holder<Biome> b = chunk.getNoiseBiome(blockX >> 2, quartY, blockZ >> 2);
//            HBMsNTM.LOGGER.info("Biome at quartY {} = {}", quartY, b.unwrapKey().get());
//        }
        for (ServerPlayer player : level.getChunkSource().chunkMap.getPlayers(chunk.getPos(), false)) {
            player.connection.send(new ClientboundLevelChunkWithLightPacket(chunk, level.getLightEngine(), null, null));
        }
    }

}
