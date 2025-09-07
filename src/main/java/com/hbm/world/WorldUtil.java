package com.hbm.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

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
        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }

        int chunkX = Mth.floor(entity.getX() / 16.0D);
        int chunkZ = Mth.floor(entity.getZ() / 16.0D);
        int loadRadius = 2;

        for (int x = chunkX - loadRadius; x <= chunkX + loadRadius; x++) {
            for (int z = chunkZ - loadRadius; z <= chunkZ + loadRadius; z++) {
                level.getChunkSource().getChunkNow(x, z);
            }
        }

        EntityJoinLevelEvent event = new EntityJoinLevelEvent(entity, level);

        IEventBus bus = NeoForge.EVENT_BUS;
        bus.post(event);

        if (event.isCanceled()) {
            return;
        }

        level.addFreshEntity(entity);
    }
}
