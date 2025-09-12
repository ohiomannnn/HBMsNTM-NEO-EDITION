package com.hbm.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;

public class WorldUtil {
    // asbdjklserumerwcer
    public static final BlockState BROWN_MUSHROOM_META10 =
            Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState()
                    .setValue(HugeMushroomBlock.UP, true)
                    .setValue(HugeMushroomBlock.NORTH, true)
                    .setValue(HugeMushroomBlock.SOUTH, true)
                    .setValue(HugeMushroomBlock.EAST, true)
                    .setValue(HugeMushroomBlock.WEST, true)
                    .setValue(HugeMushroomBlock.DOWN, false);
    public static final BlockState RED_MUSHROOM_META10 =
            Blocks.RED_MUSHROOM.defaultBlockState()
                    .setValue(HugeMushroomBlock.UP, true)
                    .setValue(HugeMushroomBlock.NORTH, true)
                    .setValue(HugeMushroomBlock.SOUTH, true)
                    .setValue(HugeMushroomBlock.EAST, true)
                    .setValue(HugeMushroomBlock.WEST, true)
                    .setValue(HugeMushroomBlock.DOWN, false);

    public static void loadAndSpawnEntityInWorld(Entity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {

            int chunkX = entity.chunkPosition().x;
            int chunkZ = entity.chunkPosition().z;

            serverLevel.getChunk(chunkX, chunkZ, ChunkStatus.FULL, true);

            serverLevel.addFreshEntity(entity);
        }
    }
}
