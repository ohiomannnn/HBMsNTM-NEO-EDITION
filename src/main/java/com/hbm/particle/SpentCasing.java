package com.hbm.particle;

import com.hbm.registry.NtmSoundEvents;
import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * Definition for spent casing particles, what style and color they should use
 * @author uffr, hbm
 */
public class SpentCasing implements Cloneable {

    public static final int COLOR_CASE_BRASS = 0xEBC35E;
    public static final int COLOR_CASE_EQUESTRIAN = 0x957BA0;
    public static final int COLOR_CASE_12GA = 0x757575;
    public static final int COLOR_CASE_4GA = 0xD8D8D8;
    public static final int COLOR_CASE_44 = 0x3E3E3E;
    public static final int COLOR_CASE_16INCH = 0xD89128;
    public static final int COLOR_CASE_16INCH_PHOS = 0xC8C8C8;
    public static final int COLOR_CASE_16INCH_NUKE = 0x495443;
    public static final int COLOR_CASE_40MM = 0x515151;

    public static final HashMap<String, SpentCasing> casingMap = new HashMap<>();

    public enum SpentCasingType {
        STRAIGHT("Straight"),
        BOTTLENECK("Bottleneck"),
        SHOTGUN("Shotgun", "ShotgunCase"); //plastic shell, brass case

        public final String[] partNames;

        SpentCasingType(String... names) {
            this.partNames = names;
        }
    }

    private String registryName = "CHANGEME";
    private float scaleX = 1F;
    private float scaleY = 1F;
    private float scaleZ = 1F;
    private int[] colors;
    private final SpentCasingType type;
    private Supplier<SoundEvent> bounceSound;
    private float bounceYaw = 1F;
    private float bouncePitch = 1F;
    private int maxAge = 240;

    public SpentCasing(SpentCasingType type) {
        this.type = type;

        if(type == SpentCasingType.SHOTGUN) {
            this.setSound(NtmSoundEvents.PLINK_SHELL);
        } else {
            this.setSound(NtmSoundEvents.PLINK_SMALL);
        }
    }

    /** Separated from the ctor to allow for easy creation of new casings from templates that don't need to be registered */
    public SpentCasing register(String name) {
        this.registryName = name;
        casingMap.put(name, this);
        return this;
    }

    public SpentCasing setScale(float scale) {
        return setScale(scale, scale, scale);
    }

    public SpentCasing setScale(float x, float y, float z) {
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
        if(x * y * z >= 3 && this.type != SpentCasingType.SHOTGUN) this.setSound(NtmSoundEvents.PLINK_MEDIUM);
        if(x * y * z >= 100 && this.type != SpentCasingType.SHOTGUN) this.setSound(NtmSoundEvents.PLINK_LARGE);
        return this;
    }

    /** The number of colors has to match the number of objects of the chosen casing type. Brass/metal casing color has to come last to comply with the chorophyte coloring. */
    public SpentCasing setColor(int... color) {
        this.colors = color;
        return this;
    }

    public SpentCasing setSound(Supplier<SoundEvent> bounce) {
        this.bounceSound = bounce;
        return this;
    }

    @Deprecated public SpentCasing setupSmoke(float chance, double lift, int duration, int nodeLife) { return this; }

    public static SpentCasing fromName(String name) {
        return casingMap.get(name);
    }

    /** Multiplier for default standard deviation of 10deg per tick, per bounce w/ full y speed */
    public SpentCasing setBounceMotion(float yaw, float pitch) {
        this.bounceYaw = yaw;
        this.bouncePitch = pitch;
        return this;
    }

    public SpentCasing setMaxAge(int age) {
        this.maxAge = age;
        return this;
    }

    public String getName() { return this.registryName; }
    public float getScaleX() { return this.scaleX; }
    public float getScaleY() { return this.scaleY; }
    public float getScaleZ() { return this.scaleZ; }
    public int[] getColors() { return this.colors; }
    public SpentCasingType getType() { return this.type; }
    public Supplier<SoundEvent> getSound() { return this.bounceSound; }
    public float getBounceYaw() { return this.bounceYaw; }
    public float getBouncePitch() { return this.bouncePitch; }
    public int getMaxAge() { return this.maxAge; }

    @Override
    public SpentCasing clone() {
        try {
            return (SpentCasing) super.clone();
        } catch(CloneNotSupportedException e) {
            return new SpentCasing(this.type);
        }
    }
}
