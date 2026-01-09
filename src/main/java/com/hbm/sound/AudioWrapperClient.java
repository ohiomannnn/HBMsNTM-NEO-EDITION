package com.hbm.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class AudioWrapperClient extends AudioWrapper {
    AudioDynamic sound;

    public AudioWrapperClient(SoundEvent event, SoundSource source) {
        if (source != null) sound = new AudioDynamic(event, source);
    }

    @Override
    public void setKeepAlive(int keepAlive) {
        if (sound != null) sound.setKeepAlive(keepAlive);
    }

    @Override
    public void keepAlive() {
        if (sound != null) sound.keepAlive();
    }

    @Override
    public void updatePosition(float x, float y, float z) {
        if (sound != null) sound.setPosition(x, y, z);
    }

    @Override
    public void attachTo(Entity e) {
        if (sound != null) sound.attachTo(e);
    }

    @Override
    public void updateVolume(float volume) {
        if (sound != null) sound.setVolume(volume);
    }

    @Override
    public void updateRange(float range) {
        if (sound != null) sound.setRange(range);
    }

    @Override
    public void updatePitch(float pitch) {
        if (sound != null) sound.setPitch(pitch);
    }

    @Override
    public float getVolume() {
        if (sound != null) return sound.getVolume();
        return 1;
    }

    @Override
    public float getPitch() {
        if (sound != null) return sound.getPitch();
        return 1;
    }

    @Override
    public void startSound() {
        if (sound != null) sound.start();
    }

    @Override
    public void stopSound() {
        if (sound != null) {
            sound.doneStopWhatever();
            sound.setKeepAlive(0);
        }
    }

    @Override
    public boolean isPlaying() {
        return sound != null ? sound.isPlaying() : false;
    }
}
