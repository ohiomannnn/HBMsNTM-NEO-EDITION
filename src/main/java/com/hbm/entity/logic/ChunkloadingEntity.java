package com.hbm.entity.logic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Comparator;
import java.util.UUID;

public abstract class ChunkloadingEntity extends Entity {

    private static final TicketType<UUID> CHUNK_TICKET = TicketType.create("chunkloading_entity", Comparator.comparing(UUID::toString), 0);

    private ChunkPos loadedChunk;

    protected ChunkloadingEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        if (!level().isClientSide && level() instanceof ServerLevel server) {
            this.loadedChunk = new ChunkPos(this.blockPosition());
            server.getChunkSource().addRegionTicket(
                    CHUNK_TICKET,
                    this.loadedChunk,
                    3,
                    this.getUUID()
            );
        }
    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();
        if (!level().isClientSide && loadedChunk != null && level() instanceof ServerLevel server) {
            server.getChunkSource().removeRegionTicket(
                    CHUNK_TICKET,
                    this.loadedChunk,
                    3,
                    this.getUUID()
            );
            this.loadedChunk = null;
        }
    }

    protected void updateChunkTicket() {
        if (!level().isClientSide && level() instanceof ServerLevel server) {
            ChunkPos newPos = new ChunkPos(this.blockPosition());
            if (!newPos.equals(this.loadedChunk)) {
                if (this.loadedChunk != null) {
                    server.getChunkSource().removeRegionTicket(
                            CHUNK_TICKET,
                            this.loadedChunk,
                            3,
                            this.getUUID()
                    );
                }
                this.loadedChunk = newPos;
                server.getChunkSource().addRegionTicket(
                        CHUNK_TICKET,
                        this.loadedChunk,
                        3,
                        this.getUUID()
                );
            }
        }
    }
}
