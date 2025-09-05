package com.hbm.handler.radiation;


import net.minecraft.world.level.Level;

public class ChunkRadiationHandlerBlank extends ChunkRadiationHandler {

    @Override public float getRadiation(Level level, int x, int y, int z) { return 0; }
    @Override public void setRadiation(Level level, int x, int y, int z, float rad) { }
    @Override public void incrementRad(Level level, int x, int y, int z, float rad) { }
    @Override public void decrementRad(Level level, int x, int y, int z, float rad) { }
    @Override public void updateSystem() { }
    @Override public void clearSystem(Level level) { }
}