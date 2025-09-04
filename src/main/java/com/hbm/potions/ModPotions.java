package com.hbm.potions;

import com.hbm.HBMsNTM;
import com.hbm.potions.effects.TaintEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModPotions {

    // Создаём DeferredRegister для эффектов
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, HBMsNTM.MODID);

    // Регистрируем каждый эффект как RegistryObject
    public static final Supplier<MobEffect> TAINT = MOB_EFFECTS.register("taint",
            () -> new TaintEffect(MobEffectCategory.HARMFUL, 0x800080));

    public static final Supplier<MobEffect> RADIATION = MOB_EFFECTS.register("radiation",
            () -> new RadiationEffect(MobEffectCategory.HARMFUL, 0x84C128));

    public static final Supplier<MobEffect> BANG = MOB_EFFECTS.register("bang",
            () -> new BangEffect(MobEffectCategory.HARMFUL, 0x111111));

    public static final Supplier<MobEffect> MUTATION = MOB_EFFECTS.register("mutation",
            () -> new HbmEffect(MobEffectCategory.BENEFICIAL, 0x800080)); // Простой эффект без особой логики

    public static final Supplier<MobEffect> RADX = MOB_EFFECTS.register("radx",
            () -> new HbmEffect(MobEffectCategory.BENEFICIAL, 0xBB4B00));

    public static final Supplier<MobEffect> LEAD = MOB_EFFECTS.register("lead",
            () -> new LeadEffect(MobEffectCategory.HARMFUL, 0x767682));

    public static final Supplier<MobEffect> RADAWAY = MOB_EFFECTS.register("radaway",
            () -> new RadawayEffect(MobEffectCategory.BENEFICIAL, 0xBB4B00));

    public static final Supplier<MobEffect> PHOSPHORUS = MOB_EFFECTS.register("phosphorus",
            () -> new PhosphorusEffect(MobEffectCategory.HARMFUL, 0xFFFF00));

    public static final Supplier<MobEffect> STABILITY = MOB_EFFECTS.register("stability",
            () -> new HbmEffect(MobEffectCategory.BENEFICIAL, 0xD0D0D0));

    public static final Supplier<MobEffect> POTIONSICKNESS = MOB_EFFECTS.register("potionsickness",
            () -> new HbmEffect(MobEffectCategory.NEUTRAL, 0xff8080)); // Используем NEUTRAL для таких эффектов

    public static final Supplier<MobEffect> DEATH = MOB_EFFECTS.register("death",
            () -> new HbmEffect(MobEffectCategory.HARMFUL, 0x111111));


    // Метод для вызова в главном классе мода для регистрации
    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}