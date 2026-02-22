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

    public static final DeferredHolder<SoundEvent, SoundEvent> MUKE_EXPLOSION = reg("muke_explosion");
    public static final DeferredHolder<SoundEvent, SoundEvent> NUCLEAR_EXPLOSION = reg("nuclear_explosion");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_LARGE_NEAR = reg("weapon.explosion_large_near");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_LARGE_FAR = reg("weapon.explosion_large_far");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_SMALL_NEAR = reg("weapon.explosion_small_near");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_SMALL_FAR = reg("weapon.explosion_small_far");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_TINY = reg("weapon.explosion_tiny");

    public static final DeferredHolder<SoundEvent, SoundEvent> FIRE_DISINTEGRATION = reg("weapon.fire.disintegration");

    public static final DeferredHolder<SoundEvent, SoundEvent> FSTBMB_START = reg("weapon.fstbmb_start");

    public static final DeferredHolder<SoundEvent, SoundEvent> DEBRIS = reg("block.debris");

    public static final DeferredHolder<SoundEvent, SoundEvent> GRENADE_BOUNCE = reg("grenade_bounce");

    public static final DeferredHolder<SoundEvent, SoundEvent> DUCK = reg("duck");
    public static final DeferredHolder<SoundEvent, SoundEvent> VOMIT = reg("vomit");
    public static final DeferredHolder<SoundEvent, SoundEvent> COUGH = reg("player.cough");

    public static final DeferredHolder<SoundEvent, SoundEvent> FENSU_HUM = reg("block.fensu_hum");

    public static final DeferredHolder<SoundEvent, SoundEvent> OLD_EXPLOSION = reg("old_explosion");

    public static final DeferredHolder<SoundEvent, SoundEvent> METEORITE_FALLING_LOOP = reg("meteorite_falling_loop");

    public static final DeferredHolder<SoundEvent, SoundEvent> TECH_BOOP = reg("tech_boop");
    public static final DeferredHolder<SoundEvent, SoundEvent> TECH_BLEEP = reg("tech_bleep");

    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER1 = reg("item.geiger1");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER2 = reg("item.geiger2");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER3 = reg("item.geiger3");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER4 = reg("item.geiger4");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER5 = reg("item.geiger5");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER6 = reg("item.geiger6");

    public static final DeferredHolder<SoundEvent, SoundEvent> LOCK_OPEN = reg("lock_open");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIN_UNLOCK = reg("pin_unlock");
    public static final DeferredHolder<SoundEvent, SoundEvent> PIN_BREAK = reg("pin_break");

    public static final DeferredHolder<SoundEvent, SoundEvent> CRATE_CLOSE = reg("crate_close");
    public static final DeferredHolder<SoundEvent, SoundEvent> CRATE_OPEN = reg("crate_open");

    public static final DeferredHolder<SoundEvent, SoundEvent> PLANE_SHOT_DOWN = reg("plane_shot_down");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLANE_CRASH = reg("plane_crash");

    public static final DeferredHolder<SoundEvent, SoundEvent> BOMBER_SMALL_LOOP = reg("bomber_small_loop");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOMBER_LOOP = reg("bomber_loop");

    public static final DeferredHolder<SoundEvent, SoundEvent> BOMB_WHISTLE = reg("bomb_whistle");

    public static final DeferredHolder<SoundEvent, SoundEvent> MISSILE_TAKE_OFF = reg("weapon.missile_take_off");

    public static final DeferredHolder<SoundEvent, SoundEvent> HUNDUS = reg("block.hunduns_magnificent_howl");
    public static final DeferredHolder<SoundEvent, SoundEvent> SQUEAKY_TOY = reg("block.squeaky_toy");

    public static final DeferredHolder<SoundEvent, SoundEvent> DOOR_WGH_STOP = reg("door.wgh_stop");
    public static final DeferredHolder<SoundEvent, SoundEvent> DOOR_WGH_START = reg("door.wgh_start");
    public static final DeferredHolder<SoundEvent, SoundEvent> DOOR_ALARM_6 = reg("door.alarm6");

    private static DeferredHolder<SoundEvent, SoundEvent> reg(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(HBMsNTM.withDefaultNamespaceNT(name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
