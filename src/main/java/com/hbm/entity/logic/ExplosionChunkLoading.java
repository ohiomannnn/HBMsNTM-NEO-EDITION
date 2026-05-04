package com.hbm.entity.logic;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public abstract class ExplosionChunkLoading extends Entity implements IChunkLoader {

    @Nullable private ChunkPos lastLoadedChunk = null;

    protected ExplosionChunkLoading(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override public void setLoadedChunkPos(ChunkPos pos) { this.lastLoadedChunk = pos; }
    @Override public ChunkPos getLoadedChunkPos() { return lastLoadedChunk; }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();

        this.onAddedToLevel(this);
    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();

        this.onRemovedFromLevel(this);
    }

    protected void updateChunkTicket() {
        this.updateChunkTicket(this);
    }
}
