package com.hbm.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class AudioDynamic extends AbstractTickableSoundInstance {

    public float maxVolume = 1;
    public float range;
    public int keepAlive;
    public int timeSinceKA;
    public boolean shouldExpire = false;
    // shitty addition that should make looped sounds on tools and guns work right
    // position updates happen automatically and if the parent is the client player, volume is always on max
    public Entity parentEntity = null;

    protected AudioDynamic(SoundEvent event, SoundSource source) {
        super(event, source, RandomSource.create());
        this.looping = true;
        this.attenuation = Attenuation.NONE;
        this.range = 10;
    }

    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void attachTo(Entity e) {
        this.parentEntity = e;
    }

    @Override
    public void tick() {
        Player player = Minecraft.getInstance().player;
        float f = 0;

        if (parentEntity != null && player != parentEntity) {
            this.setPosition((float) parentEntity.getX(), (float) parentEntity.getY(), (float) parentEntity.getZ());
        }

        // only adjust volume over distance if the sound isn't attached to this entity
        if (player != null && player != parentEntity) {
            f = (float)Math.sqrt(Math.pow(x - player.getX(), 2) + Math.pow(y - player.getY(), 2) + Math.pow(z - player.getZ(), 2));
            volume = func(f);
        } else {
            // shitty hack that prevents stereo weirdness when using 0 0 0
            if (player == parentEntity) this.setPosition((float) parentEntity.getX(), (float) parentEntity.getY() + 10, (float) parentEntity.getZ());
            volume = maxVolume;
        }

        if (this.shouldExpire) {

            if (this.timeSinceKA > this.keepAlive) {
                this.stop();
            }

            this.timeSinceKA++;
        }

    }

    public void start() {
        Minecraft.getInstance().getSoundManager().play(this);
    }

    public void doneStopWhatever() {
        Minecraft.getInstance().getSoundManager().stop(this);
    }

    public void setVolume(float volume) {
        this.maxVolume = volume;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public void setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
        this.shouldExpire = true;
    }

    public void keepAlive() {
        this.timeSinceKA = 0;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float func(float dist) {
        return (dist / range) * -maxVolume + maxVolume;
    }

    public boolean isPlaying() {
        return Minecraft.getInstance().getSoundManager().isActive(this);
    }
}
