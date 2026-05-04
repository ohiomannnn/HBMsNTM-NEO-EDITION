package com.hbm.registry;

import com.hbm.main.NuclearTechMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NtmSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, NuclearTechMod.MODID);

    /// WEAPONS
    public static final DeferredHolder<SoundEvent, SoundEvent> MISSILE_TAKEOFF = reg("weapon.missile_takeoff"); // PLAYERS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> MUKE_EXPLOSION = reg("weapon.muke_explosion"); // BLOCKS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> ROBIN_EXPLOSION = reg("weapon.robin_explosion"); // PLAYERS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> NUCLEAR_EXPLOSION = reg("weapon.nuclear_explosion"); // BLOCKS CATEGORY, STREAM
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_LARGE_NEAR = reg("weapon.explosion_large_near"); // BLOCKS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_LARGE_FAR =  reg("weapon.explosion_large_far"); // BLOCKS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_SMALL_NEAR = reg("weapon.explosion_small_near"); // BLOCKS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_SMALL_FAR =  reg("weapon.explosion_small_far"); // BLOCKS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_TINY = reg("weapon.explosion_tiny"); // BLOCKS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> FSTBMB_START = reg("weapon.fstbmb_start"); // PLAYERS CATEGORY???
    public static final DeferredHolder<SoundEvent, SoundEvent> FSTBMB_PING = reg("weapon.fstbmb_ping"); // PLAYERS CATEGORY???
    /// FIRE WEAPONS
    public static final DeferredHolder<SoundEvent, SoundEvent> FIRE_DISINTEGRATION = reg("weapon.fire.disintegration"); // PLAYERS CATEGORY
    /// ENTITIES
    public static final DeferredHolder<SoundEvent, SoundEvent> OLD_EXPLOSION = reg("entity.old_explosion"); // AMBIENT CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> BOMB_WHISTLE = reg("entity.bomb_whistle"); // PLAYER CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> BOMBER_LOOP = reg("entity.bomber_loop"); // HOSTILE CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> BOMBER_SMALL_LOOP = reg("entity.bomber_small_loop"); // HOSTILE CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> PLANE_CRASH = reg("entity.plane_crash"); // HOSTILE CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> PLANE_SHOT_DOWN = reg("entity.plane_shot_down"); // HOSTILE CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> DUCC = reg("entity.ducc"); // NEUTRAL CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> METEORITE_FALLING_LOOP = reg("entity.meteorite_falling_loop"); // BLOCKS CATEGORY???
    /// PLAYERS
    public static final DeferredHolder<SoundEvent, SoundEvent> VOMIT = reg("player.vomit"); // PLAYERS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> COUGH = reg("player.cough"); // PLAYERS CATEGORY
    /// BLOCKS
    public static final DeferredHolder<SoundEvent, SoundEvent> PIPE_PLACED = reg("block.pipe_placed"); // BLOCKS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> FENSU_HUM = reg("block.fensu_hum"); // BLOCKS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> DEBRIS = reg("block.debris"); // BLOCKS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> LOCK_OPEN = reg("block.lock_open"); // BLOCKS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> CRATE_CLOSE = reg("crate_close"); // BLOCKS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> CRATE_OPEN = reg("crate_open"); // BLOCKS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> HUNDUNS_MAGNIFICENT_HOWL = reg("block.hunduns_magnificent_howl"); // BLOCKS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> SQUEAKY_TOY = reg("block.squeaky_toy"); // BLOCKS CATEGORY
    /// ITEMS
    public static final DeferredHolder<SoundEvent, SoundEvent> TECH_BLEEP = reg("item.tech_bleep"); // PLAYERS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> TECH_BOOP = reg("item.tech_boop"); // PLAYERS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER1 = reg("item.geiger1"); // PLAYERS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER2 = reg("item.geiger2"); // PLAYERS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER3 = reg("item.geiger3"); // PLAYERS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER4 = reg("item.geiger4"); // PLAYERS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER5 = reg("item.geiger5"); // PLAYERS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER6 = reg("item.geiger6"); // PLAYERS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> PIN_UNLOCK = reg("item.pin_unlock"); // PLAYERS CATEGORY
    public static final DeferredHolder<SoundEvent, SoundEvent> PIN_BREAK = reg("item.pin_break"); // PLAYERS CATEGORY

    private static DeferredHolder<SoundEvent, SoundEvent> reg(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(NuclearTechMod.withDefaultNamespace(name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
