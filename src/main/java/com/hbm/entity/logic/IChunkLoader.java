package com.hbm.entity.logic;

import com.hbm.main.NuclearTechMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

import java.util.Comparator;
import java.util.UUID;

@Deprecated
public interface IChunkLoader {

    TicketType<UUID> ENTITY = TicketType.create("entity", Comparator.comparing(UUID::toString));

    void setLoadedChunkPos(ChunkPos pos);
    ChunkPos getLoadedChunkPos();

    default void onAddedToLevel(Entity entity) {

        if(entity.level instanceof ServerLevel serverLevel) {
            this.setLoadedChunkPos(new ChunkPos(entity.blockPosition()));

            serverLevel.setChunkForced(this.getLoadedChunkPos().x, this.getLoadedChunkPos().z, true);
        }
    }
    default void onRemovedFromLevel(Entity entity) {

        if(this.getLoadedChunkPos() != null && entity.level instanceof ServerLevel serverLevel) {
            serverLevel.setChunkForced(this.getLoadedChunkPos().x, this.getLoadedChunkPos().z, false);

            this.setLoadedChunkPos(null);
        }
    }

    default void updateChunkTicket(Entity entity) {
        if(entity.level instanceof ServerLevel serverLevel) {

            ChunkPos newPos = new ChunkPos(entity.blockPosition());

            ChunkPos oldPos = this.getLoadedChunkPos();
            if(oldPos != null && !newPos.equals(oldPos)) {
                serverLevel.setChunkForced(oldPos.x, oldPos.z, false);
                serverLevel.setChunkForced(newPos.x, newPos.z, true);

                this.setLoadedChunkPos(newPos);
            }
        }
    }
}
