package com.hbm.blocks;

import com.hbm.main.NuclearTechMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SoundType;

public class ModSoundType extends SoundType {

    protected ModSoundType(SoundEvent placeSound, SoundEvent breakSound, SoundEvent stepSound, SoundEvent hitSound, SoundEvent fallSound, float volume, float pitch) {
        super(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound);
    }

    protected ModSoundType(SoundEvent placeSound, SoundEvent breakSound, SoundEvent stepSound, float volume, float pitch) {
        this(placeSound, breakSound, stepSound, stepSound, stepSound, volume, pitch);
    }

    public ModEnvelopedSoundType enveloped() {
        return new ModEnvelopedSoundType(getPlaceSound(), getBreakSound(), getStepSound(), getHitSound(), getFallSound(), volume, pitch);
    }

    public ModEnvelopedSoundType enveloped(RandomSource random) {
        return new ModEnvelopedSoundType(getPlaceSound(), getBreakSound(), getStepSound(), getHitSound(), getFallSound(), volume, pitch, random);
    }

    public static SoundEvent sound(String id) {
        return SoundEvent.createVariableRangeEvent(ResourceLocation.parse(id));
    }

    public static ModSoundType mod(String soundName, float volume, float pitch) {
        return new ModSoundType(modDig(soundName), modDig(soundName), modStep(soundName), volume, pitch);
    }

    public static ModSoundType customPlace(String soundName, String placeSound, float volume, float pitch) {
        return new ModSoundType(sound(placeSound), modDig(soundName), modStep(soundName), volume, pitch);
    }

    public static ModSoundType customBreak(String soundName, String breakSound, float volume, float pitch) {
        return new ModSoundType(modDig(soundName), sound(breakSound), modStep(soundName), volume, pitch);
    }

    public static ModSoundType customStep(String soundName, String stepSound, float volume, float pitch) {
        return new ModSoundType(modDig(soundName), modDig(soundName), sound(stepSound), volume, pitch);
    }

    public static ModSoundType customDig(String soundName, String digSound, float volume, float pitch) {
        SoundEvent dig = sound(digSound);
        return new ModSoundType(dig, dig, modStep(soundName), volume, pitch);
    }

    public static ModSoundType customPlace(SoundType from, String placeSound, float volume, float pitch) {
        return new ModSoundType(sound(placeSound), from.getBreakSound(), from.getStepSound(), from.getHitSound(), from.getFallSound(), volume, pitch);
    }

    public static ModSoundType customBreak(SoundType from, String breakSound, float volume, float pitch) {
        return new ModSoundType(from.getPlaceSound(), sound(breakSound), from.getStepSound(), from.getHitSound(), from.getFallSound(), volume, pitch);
    }

    public static ModSoundType customStep(SoundType from, String stepSound, float volume, float pitch) {
        return new ModSoundType(from.getPlaceSound(), from.getBreakSound(), sound(stepSound), from.getHitSound(), from.getFallSound(), volume, pitch);
    }

    public static ModSoundType customDig(SoundType from, SoundEvent dig, float volume, float pitch) {
        return new ModSoundType(dig, dig, from.getStepSound(), from.getHitSound(), from.getFallSound(), volume, pitch);
    }

    public static ModSoundType placeBreakStep(String placeSound, String breakSound, String stepSound, float volume, float pitch) {
        return new ModSoundType(sound(placeSound), sound(breakSound), sound(stepSound), volume, pitch);
    }

    private static SoundEvent modDig(String soundName) {
        return sound(NuclearTechMod.MODID + ":dig." + soundName);
    }

    private static SoundEvent modStep(String soundName) {
        return sound(NuclearTechMod.MODID + ":step." + soundName);
    }

    public static class ModEnvelopedSoundType extends ModSoundType {
        private final RandomSource random;

        ModEnvelopedSoundType(SoundEvent placeSound, SoundEvent breakSound, SoundEvent stepSound, SoundEvent hitSound, SoundEvent fallSound, float volume, float pitch, RandomSource random) {
            super(placeSound, breakSound, stepSound, hitSound, fallSound, volume, pitch);
            this.random = random;
        }

        ModEnvelopedSoundType(SoundEvent placeSound, SoundEvent breakSound, SoundEvent stepSound, SoundEvent hitSound, SoundEvent fallSound, float volume, float pitch) {
            this(placeSound, breakSound, stepSound, hitSound, fallSound, volume, pitch, RandomSource.create());
        }

        private SubType probableSubType = SubType.PLACE;

        @Override
        public SoundEvent getPlaceSound() {
            probableSubType = SubType.PLACE;
            return super.getPlaceSound();
        }

        @Override
        public SoundEvent getBreakSound() {
            probableSubType = SubType.BREAK;
            return super.getBreakSound();
        }

        @Override
        public SoundEvent getStepSound() {
            probableSubType = SubType.STEP;
            return super.getStepSound();
        }

        @Override
        public SoundEvent getHitSound() {
            probableSubType = SubType.HIT;
            return super.getHitSound();
        }

        @Override
        public SoundEvent getFallSound() {
            probableSubType = SubType.FALL;
            return super.getFallSound();
        }

        private Envelope volumeEnvelope = null;
        private Envelope pitchEnvelope = null;

        public ModEnvelopedSoundType volumeFunction(Envelope volumeEnvelope) {
            this.volumeEnvelope = volumeEnvelope;
            return this;
        }

        public ModEnvelopedSoundType pitchFunction(Envelope pitchEnvelope) {
            this.pitchEnvelope = pitchEnvelope;
            return this;
        }

        @Override
        public float getVolume() {
            if (volumeEnvelope == null) {
                return super.getVolume();
            } else {
                return volumeEnvelope.compute(super.getVolume(), random, probableSubType);
            }
        }

        @Override
        public float getPitch() {
            if (pitchEnvelope == null) {
                return super.getPitch();
            } else {
                return pitchEnvelope.compute(super.getPitch(), random, probableSubType);
            }
        }

        @FunctionalInterface
        public interface Envelope {
            float compute(float in, RandomSource rand, SubType type);
        }
    }

    public enum SubType {
        PLACE, BREAK, STEP, HIT, FALL
    }
}