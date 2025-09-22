package com.hbm.entity.logic;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.TicketType;

public abstract class EntityExplosionChunkloading extends Entity implements IChunkLoader {

    private ChunkPos loadedChunk;

    public static final TicketType<Integer> EXPLOSION_CHUNK =
            TicketType.create("explosion_chunk", Integer::compare, 1);

    public EntityExplosionChunkloading(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            init(serverLevel);
        }
    }

    @Override
    public void init(ServerLevel serverLevel) {
        ChunkPos pos = new ChunkPos(this.blockPosition());
        this.loadedChunk = pos;

        serverLevel.getChunkSource().addRegionTicket(
                EXPLOSION_CHUNK,
                pos,
                1,
                this.getId()
        );
    }

    public void loadChunk(int x, int z) {
        if (this.loadedChunk == null && level() instanceof ServerLevel serverLevel) {
            this.loadedChunk = new ChunkPos(x, z);

            serverLevel.getChunkSource().addRegionTicket(
                    EXPLOSION_CHUNK,
                    this.loadedChunk,
                    1,
                    this.getId()
            );
        }
    }

    public void clearChunkLoader() {
        if (!level().isClientSide && loadedChunk != null && level() instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().removeRegionTicket(
                    EXPLOSION_CHUNK,
                    loadedChunk,
                    1,
                    this.getId()
            );
            this.loadedChunk = null;
        }
    }
}
