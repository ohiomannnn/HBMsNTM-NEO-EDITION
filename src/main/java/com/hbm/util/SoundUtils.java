package com.hbm.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class SoundUtils {

    public static void playAtVec3(Level level, Position pos, SoundEvent event, SoundSource source, float volume, float pitch) {
        level.playSound(null, pos.x(), pos.y(), pos.z(), event, source, volume, pitch);
    }
    public static void playAtVec3(Level level, Position pos, SoundEvent event, SoundSource source) { playAtVec3(level, pos, event, source, 1.0F, 1.0F); }

    // play sound client
    public static void playAtVec3C(Level level, Position pos, SoundEvent event, SoundSource source, float volume, float pitch) {
        level.playLocalSound(pos.x(), pos.y(), pos.z(), event, source, volume, pitch, true);
    }
    public static void playAtVec3C(Level level, Position pos, SoundEvent event, SoundSource source) { playAtVec3C(level, pos, event, source, 1.0F, 1.0F); }


    /** Plays sound at entity */
    public static void playAtEntity(Entity entity, SoundEvent event, SoundSource source, float volume, float pitch) { playAtVec3(entity.level, entity.position(), event, source, volume, pitch); }
    public static void playAtEntity(Entity entity, SoundEvent event, SoundSource source) { playAtVec3(entity.level, entity.position(), event, source); }

    /** Plays sound at block pos */
    public static void playAtBlockPos(Level level, BlockPos pos, SoundEvent event, SoundSource source, float volume, float pitch) {
        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), event, source, volume, pitch);
    }
    public static void playAtBlockPos(Level level, BlockPos pos, SoundEvent event, SoundSource source) { playAtBlockPos(level, pos, event, source, 1.0F, 1.0F); }

    /** Plays sound at block pos center */
    public static void playAtBlockPosC(Level level, BlockPos pos, SoundEvent event, SoundSource source, float volume, float pitch) { playAtVec3(level, pos.getCenter(), event, source, volume, pitch); }
    public static void playAtBlockPosC(Level level, BlockPos pos, SoundEvent event, SoundSource source) { playAtVec3(level, pos.getCenter(), event, source); }
}
