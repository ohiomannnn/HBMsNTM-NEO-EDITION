package com.hbm.util;

import net.minecraft.world.level.ChunkPos;

public class SubChunkKey {

    private final int chunkXPos;
    private final int chunkZPos;
    private final int subY;
    private final int hash;

    public SubChunkKey(int cx, int cz, int sy) {
        this.chunkXPos = cx;
        this.chunkZPos = cz;
        this.subY = sy;
        this.hash = calcHash(cx, cz, sy);
    }

    public SubChunkKey(ChunkPos pos, int sy) {
        this(pos.x, pos.z, sy);
    }

    private static int calcHash(int cx, int cz, int sy) {
        int result = sy;
        result = 31 * result + cx;
        result = 31 * result + cz;
        return result;
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubChunkKey k)) return false;
        return this.subY == k.subY &&
                this.chunkXPos == k.chunkXPos &&
                this.chunkZPos == k.chunkZPos;
    }

    public int getSubY() {
        return subY;
    }

    public int getChunkXPos() {
        return chunkXPos;
    }

    public int getChunkZPos() {
        return chunkZPos;
    }

    public ChunkPos getPos() {
        return new ChunkPos(this.chunkXPos, this.chunkZPos);
    }
}
