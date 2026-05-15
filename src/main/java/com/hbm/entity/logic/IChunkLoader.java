package com.hbm.entity.logic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

public interface IChunkLoader {

    void setLoadedChunkPos(ChunkPos pos);

    ChunkPos getLoadedChunkPos();

    default void onAddedToLevel(Entity entity) {

        if(entity.level instanceof ServerLevel serverLevel) {
            this.setLoadedChunkPos(new ChunkPos(entity.blockPosition));

            serverLevel.getChunkSource().addRegionTicket(TicketType.PORTAL, this.getLoadedChunkPos(), 3, entity.blockPosition, true);
        }
    }
    default void onRemovedFromLevel(Entity entity) {

        if(this.getLoadedChunkPos() != null && entity.level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().removeRegionTicket(TicketType.PORTAL, this.getLoadedChunkPos(), 3, entity.blockPosition, true);

            this.setLoadedChunkPos(null);
        }
    }

    default void updateChunkTicket(Entity entity) {
        if(entity.level instanceof ServerLevel serverLevel) {
            ChunkPos newPos = new ChunkPos(entity.blockPosition());

            ChunkPos oldPos = this.getLoadedChunkPos();
            if(oldPos != null && !newPos.equals(oldPos)) {
                serverLevel.getChunkSource().removeRegionTicket(TicketType.PORTAL, oldPos, 3, entity.blockPosition, true);
                serverLevel.getChunkSource().addRegionTicket(TicketType.PORTAL, newPos, 3, entity.blockPosition, true);

                this.setLoadedChunkPos(newPos);
            }
        }
    }
}
