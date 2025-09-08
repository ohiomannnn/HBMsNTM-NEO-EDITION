package com.hbm.entity.logic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.TicketType;

//public abstract class EntityExplosionChunkloading extends Entity implements IChunkLoader {
//
//    private ChunkPos loadedChunk;
//
//    public EntityExplosionChunkloading(Level level) {
//        super(level);
//    }
//
//    public void init() {
//        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
//            ChunkPos chunkPos = new ChunkPos(blockPosition());
//            this.loadedChunk = chunkPos;
//            serverLevel.getChunkSource().addRegionTicket(
//                    TicketType.START,
//                    chunkPos,
//                    2,
//                    Unit.INSTANCE
//            );
//        }
//    }
//
//    public void loadChunk(int x, int z) {
//        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
//            ChunkPos chunkPos = new ChunkPos(x, z);
//            this.loadedChunk = chunkPos;
//            serverLevel.getChunkSource().addRegionTicket(
//                    TicketType.START,
//                    chunkPos,
//                    2,
//                    Unit.INSTANCE
//            );
//        }
//    }
//
//    public void clearChunkLoader() {
//        if (!level().isClientSide && level() instanceof ServerLevel serverLevel && this.loadedChunk != null) {
//            serverLevel.getChunkSource().removeRegionTicket(
//                    TicketType.START,
//                    this.loadedChunk,
//                    2,
//                    Unit.INSTANCE
//            );
//            this.loadedChunk = null;
//        }
//    }
//
//    public abstract void onUpdate();
//
//    public abstract void setDead();
//
//    protected abstract void readEntityFromNBT(CompoundTag nbt);
//
//    protected abstract void writeEntityToNBT(CompoundTag nbt);
//}
