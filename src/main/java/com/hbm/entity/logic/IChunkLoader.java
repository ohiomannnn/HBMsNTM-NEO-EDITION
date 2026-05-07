package com.hbm.entity.logic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

public interface IChunkLoader {

    void setLoadedChunkPos(ChunkPos pos);

    ChunkPos getLoadedChunkPos();

    default void onAddedToLevel(Entity entity) {

        if(entity.level instanceof ServerLevel serverLevel) {
            this.setLoadedChunkPos(new ChunkPos(entity.blockPosition));

            serverLevel.getChunkSource().updateChunkForced(this.getLoadedChunkPos(), true);
        }
    }
    default void onRemovedFromLevel(Entity entity) {

        if(entity.level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().updateChunkForced(this.getLoadedChunkPos(), false);

            this.setLoadedChunkPos(null);
        }
    }

    default void updateChunkTicket(Entity entity) {

        if (entity.level instanceof ServerLevel serverLevel) {
            ChunkPos newPos = new ChunkPos(entity.blockPosition);

            if (!newPos.equals(this.getLoadedChunkPos())) {

                serverLevel.getChunkSource().updateChunkForced(this.getLoadedChunkPos(), true);
                this.setLoadedChunkPos(newPos);
            }
        }
    }
}
