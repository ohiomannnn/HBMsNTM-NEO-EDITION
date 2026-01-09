package com.hbm.lib;

import com.hbm.HBMsNTM;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, HBMsNTM.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> MUKE_EXPLOSION = registerSoundEvent("muke_explosion");
    public static final DeferredHolder<SoundEvent, SoundEvent> NUCLEAR_EXPLOSION = registerSoundEvent("nuclear_explosion");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_LARGE_NEAR = registerSoundEvent("weapon.explosion_large_near");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_LARGE_FAR = registerSoundEvent("weapon.explosion_large_far");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_SMALL_NEAR = registerSoundEvent("weapon.explosion_small_near");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_SMALL_FAR = registerSoundEvent("weapon.explosion_small_far");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_TINY = registerSoundEvent("weapon.explosion_tiny");

    public static final DeferredHolder<SoundEvent, SoundEvent> FIRE_DISINTEGRATION = registerSoundEvent("weapon.fire.disintegration");

    public static final DeferredHolder<SoundEvent, SoundEvent> FSTBMB_START = registerSoundEvent("weapon.fstbmb_start");

    public static final DeferredHolder<SoundEvent, SoundEvent> DEBRIS = registerSoundEvent("block.debris");

    public static final DeferredHolder<SoundEvent, SoundEvent> GRENADE_BOUNCE = registerSoundEvent("grenade_bounce");

    public static final DeferredHolder<SoundEvent, SoundEvent> DUCK = registerSoundEvent("duck");
    public static final DeferredHolder<SoundEvent, SoundEvent> VOMIT = registerSoundEvent("vomit");
    public static final DeferredHolder<SoundEvent, SoundEvent> COUGH = registerSoundEvent("player.cough");

    public static final DeferredHolder<SoundEvent, SoundEvent> TECH_BOOP = registerSoundEvent("tech_boop");
    public static final DeferredHolder<SoundEvent, SoundEvent> TECH_BLEEP = registerSoundEvent("tech_bleep");

    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER1 = registerSoundEvent("item.geiger1");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER2 = registerSoundEvent("item.geiger2");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER3 = registerSoundEvent("item.geiger3");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER4 = registerSoundEvent("item.geiger4");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER5 = registerSoundEvent("item.geiger5");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER6 = registerSoundEvent("item.geiger6");

    public static final DeferredHolder<SoundEvent, SoundEvent> LOCK_OPEN = registerSoundEvent("lock_open");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIN_UNLOCK = registerSoundEvent("pin_unlock");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIN_BREAK = registerSoundEvent("pin_break");

    public static final DeferredHolder<SoundEvent, SoundEvent> CRATE_CLOSE = registerSoundEvent("crate_close");
    public static final DeferredHolder<SoundEvent, SoundEvent> CRATE_OPEN = registerSoundEvent("crate_open");

    public static final DeferredHolder<SoundEvent, SoundEvent> PLANE_SHOT_DOWN = registerSoundEvent("plane_shot_down");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLANE_CRASH = registerSoundEvent("plane_crash");

    public static final DeferredHolder<SoundEvent, SoundEvent> BOMBER_SMALL_LOOP = registerSoundEvent("bomber_small_loop");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOMBER_LOOP = registerSoundEvent("bomber_loop");

    public static final DeferredHolder<SoundEvent, SoundEvent> BOMB_WHISTLE = registerSoundEvent("bomb_whistle");


    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
