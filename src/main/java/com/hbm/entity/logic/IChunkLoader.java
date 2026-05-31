package com.hbm.entity.logic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

import java.util.Comparator;
import java.util.UUID;

public interface IChunkLoader {

    TicketType<UUID> ENTITY = TicketType.create("entity", Comparator.comparing(UUID::toString), 300);

    void setLoadedChunkPos(ChunkPos pos);
    ChunkPos getLoadedChunkPos();

    default void onAddedToLevel(Entity entity) {

        if(entity.level instanceof ServerLevel serverLevel) {
            this.setLoadedChunkPos(new ChunkPos(entity.blockPosition));

            serverLevel.getChunkSource().addRegionTicket(ENTITY, this.getLoadedChunkPos(), 2, entity.getUUID(), true);
        }
    }
    default void onRemovedFromLevel(Entity entity) {

        if(this.getLoadedChunkPos() != null && entity.level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().removeRegionTicket(ENTITY, this.getLoadedChunkPos(), 2, entity.getUUID(), true);

            this.setLoadedChunkPos(null);
        }
    }

    default void updateChunkTicket(Entity entity) {
        if(entity.level instanceof ServerLevel serverLevel) {

            ChunkPos newPos = new ChunkPos(entity.blockPosition());

            ChunkPos oldPos = this.getLoadedChunkPos();
            if(oldPos != null && !newPos.equals(oldPos)) {
                serverLevel.getChunkSource().removeRegionTicket(ENTITY, oldPos, 2, entity.getUUID(), true);
                serverLevel.getChunkSource().addRegionTicket(ENTITY, newPos, 2, entity.getUUID(), true);

                this.setLoadedChunkPos(newPos);
            }
        }
    }
}
