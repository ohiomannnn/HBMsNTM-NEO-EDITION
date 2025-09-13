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

    public static final DeferredHolder<SoundEvent, SoundEvent> GRENADE_BOUNCE = registerSoundEvent("grenade_bounce");

    public static final DeferredHolder<SoundEvent, SoundEvent> DUCK = registerSoundEvent("duck");

    public static final DeferredHolder<SoundEvent, SoundEvent> TECH_BOOP = registerSoundEvent("tech_boop");

    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER1 = registerSoundEvent("item.geiger1");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER2 = registerSoundEvent("item.geiger2");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER3 = registerSoundEvent("item.geiger3");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER4 = registerSoundEvent("item.geiger4");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER5 = registerSoundEvent("item.geiger5");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER6 = registerSoundEvent("item.geiger6");


    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
