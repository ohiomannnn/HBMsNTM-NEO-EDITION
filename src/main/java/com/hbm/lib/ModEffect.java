package com.hbm.lib;

import com.hbm.HBMsNTM;
import com.hbm.lib.effects.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffect {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, HBMsNTM.MODID);

    public static final DeferredHolder<MobEffect, TaintEffect> TAINT = MOB_EFFECTS.register("taint",
            () -> new TaintEffect(MobEffectCategory.HARMFUL, 0x800080));

    public static final DeferredHolder<MobEffect, RadiationEffect> RADIATION = MOB_EFFECTS.register("radiation",
            () -> new RadiationEffect(MobEffectCategory.HARMFUL, 0x84C128));

    public static final DeferredHolder<MobEffect, BangEffect> BANG = MOB_EFFECTS.register("bang",
            () -> new BangEffect(MobEffectCategory.HARMFUL, 0x111111));

    public static final DeferredHolder<MobEffect, GenericEffect> MUTATION = MOB_EFFECTS.register("mutation",
            () -> new GenericEffect(MobEffectCategory.BENEFICIAL, 0x800080));

    public static final DeferredHolder<MobEffect, GenericEffect> RADX = MOB_EFFECTS.register("radx",
            () -> new GenericEffect(MobEffectCategory.BENEFICIAL, 0xBB4B00));

    public static final DeferredHolder<MobEffect, LeadEffect> LEAD = MOB_EFFECTS.register("lead",
            () -> new LeadEffect(MobEffectCategory.HARMFUL, 0x767682));

    public static final DeferredHolder<MobEffect, RadawayEffect> RADAWAY = MOB_EFFECTS.register("radaway",
            () -> new RadawayEffect(MobEffectCategory.BENEFICIAL, 0xBB4B00));

    public static final DeferredHolder<MobEffect, PhosphorusEffect> PHOSPHORUS = MOB_EFFECTS.register("phosphorus",
            () -> new PhosphorusEffect(MobEffectCategory.HARMFUL, 0xFFFF00));

    public static final DeferredHolder<MobEffect, GenericEffect> STABILITY = MOB_EFFECTS.register("stability",
            () -> new GenericEffect(MobEffectCategory.BENEFICIAL, 0xD0D0D0));

    public static final DeferredHolder<MobEffect, GenericEffect> POTIONSICKNESS = MOB_EFFECTS.register("potionsickness",
            () -> new GenericEffect(MobEffectCategory.NEUTRAL, 0xff8080));

    public static final DeferredHolder<MobEffect, GenericEffect> DEATH = MOB_EFFECTS.register("death",
            () -> new GenericEffect(MobEffectCategory.HARMFUL, 0x111111));


    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}