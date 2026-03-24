package com.hbm.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class AudioWrapper {

    public void setKeepAlive(int keepAlive) { }
    public void keepAlive() { }

    public void updatePosition(float x, float y, float z) { }
    public void attachTo(Entity e) { }

    public void updateVolume(float volume) { }
    public void updateRange(float range) { }

    public void updatePitch(float pitch) { }

    public float getVolume() { return 0F; }
    public float getRange() { return 0F; }

    public float getPitch() { return 0F; }

    public void setDoesRepeat(boolean repeats) { }

    public void startSound() { }

    public void stopSound() { }

    public boolean isPlaying() { return false; }

    public static AudioWrapper getLoopedSound(SoundEvent event, SoundSource source, float x, float y, float z, float volume, float range, float pitch) {
        AudioWrapperClient audio = new AudioWrapperClient(event, source);
        audio.updatePosition(x, y, z);
        audio.updateVolume(volume);
        audio.updateRange(range);
        return audio;
    }

    public static AudioWrapper getLoopedSound(SoundEvent event, SoundSource source, float x, float y, float z, float volume, float range, float pitch, int keepAlive) {
        AudioWrapper audio = getLoopedSound(event, source, x, y, z, volume, range, pitch);
        audio.setKeepAlive(keepAlive);
        return audio;
    }
}