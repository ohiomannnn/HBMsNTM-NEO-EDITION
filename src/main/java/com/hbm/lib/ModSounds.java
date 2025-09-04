package com.hbm.lib;

import com.hbm.HBMsNTM;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, HBMsNTM.MODID);

    public static final Supplier<SoundEvent> MUKE_EXPLOSION = registerSoundEvent("muke_explosion");

    public static final Supplier<SoundEvent> GRENADE_BOUNCE = registerSoundEvent("grenade_bounce");

    public static final Supplier<SoundEvent> DUCK = registerSoundEvent("duck");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
