package com.hbm.util;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.util.*;

public class SubChunkSnapshot {

    public static final SubChunkSnapshot EMPTY = new SubChunkSnapshot(new Block[]{Blocks.AIR}, null);

    private final Block[] palette;
    private final short[] data;

    private SubChunkSnapshot(Block[] palette, short[] data) {
        this.palette = palette;
        this.data = data;
    }

    public static SubChunkSnapshot getSnapshot(Level level, SubChunkKey key, boolean allowGen) {
        if (!level.hasChunk(key.getChunkXPos(), key.getChunkZPos()) && !allowGen) {
            return EMPTY;
        }
        LevelChunk chunk = level.getChunk(key.getChunkXPos(), key.getChunkZPos());
        LevelChunkSection section = chunk.getSection(key.getSubY());
        if (section == null || section.hasOnlyAir()) {
            return EMPTY;
        }

        short[] data = new short[16 * 16 * 16];
        List<Block> palette = new ArrayList<>();
        palette.add(Blocks.AIR);
        Map<Block, Short> idxMap = new HashMap<>();
        idxMap.put(Blocks.AIR, (short) 0);

        boolean allAir = true;

        for (int y = 0; y < 16; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    BlockState state = section.getBlockState(x, y, z);
                    Block block = state.getBlock();
                    int idx;
                    if (block == Blocks.AIR) {
                        idx = 0;
                    } else {
                        allAir = false;
                        Short e = idxMap.get(block);
                        if (e == null) {
                            idxMap.put(block, (short) palette.size());
                            palette.add(block);
                            idx = palette.size() - 1;
                        } else {
                            idx = e;
                        }
                    }
                    data[(y << 8) | (z << 4) | x] = (short) idx;
                }
            }
        }

        if (allAir) return EMPTY;
        return new SubChunkSnapshot(palette.toArray(new Block[0]), data);
    }

    public Block getBlock(int x, int y, int z) {
        if (this == EMPTY || data == null) return Blocks.AIR;
        short idx = data[(y << 8) | (z << 4) | x];
        return (idx >= 0 && idx < palette.length) ? palette[idx] : Blocks.AIR;
    }
}
