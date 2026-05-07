package com.hbm.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SoundUtils {

    /** Plays sound at vec3 */
    public static void playAtVec3(Level level, Vec3 vec, SoundEvent event, SoundSource source, float volume, float pitch) {
        level.playSound(null, vec.x, vec.y, vec.z, event, source, volume, pitch);
    }
    public static void playAtVec3(Level level, Vec3 vec, SoundEvent event, SoundSource source) { playAtVec3(level, vec, event, source, 1.0F, 1.0F); }

    /** Plays sound at entity */
    public static void playAtEntity(Entity entity, SoundEvent event, SoundSource source, float volume, float pitch) { playAtVec3(entity.level, entity.position, event, source, volume, pitch); }
    public static void playAtEntity(Entity entity, SoundEvent event, SoundSource source) { playAtVec3(entity.level, entity.position, event, source); }

    /** Plays sound at block pos */
    public static void playAtBlockPos(Level level, BlockPos pos, SoundEvent event, SoundSource source, float volume, float pitch) {
        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), event, source, volume, pitch);
    }
    public static void playAtBlockPos(Level level, BlockPos pos, SoundEvent event, SoundSource source) { playAtBlockPos(level, pos, event, source, 1.0F, 1.0F); }

    /** Plays sound at block pos center */
    public static void playAtBlockPosC(Level level, BlockPos pos, SoundEvent event, SoundSource source, float volume, float pitch) { playAtVec3(level, pos.getCenter(), event, source, volume, pitch); }
    public static void playAtBlockPosC(Level level, BlockPos pos, SoundEvent event, SoundSource source) { playAtVec3(level, pos.getCenter(), event, source); }
}
