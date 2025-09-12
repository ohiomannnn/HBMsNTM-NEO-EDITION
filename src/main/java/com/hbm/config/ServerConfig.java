package com.hbm.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    /// RADIATION ///
    static {
        BUILDER.push("radiation");
    }

    public static final ModConfigSpec.IntValue FOG_RAD = BUILDER
            .comment("Radiation in RADs required for fog to spawn")
            .translation("config.hbmsntm.fogRad")
            .defineInRange("fogRad", 20, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue FOG_CHANCE = BUILDER
            .comment("1:n chance of fog spawning every second")
            .translation("config.hbmsntm.fogCh")
            .defineInRange("fogCh", 100, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.BooleanValue WORLD_RAD_EFFECTS = BUILDER
            .comment("Whether high radiation levels should perform changes in the world")
            .translation("config.hbmsntm.worldRadEffects")
            .define("worldRadEffects", true);


    public static final ModConfigSpec.BooleanValue ENABLE_CONTAMINATION = BUILDER
            .comment("Toggles player contamination (and negative effects from radiation poisoning)")
            .translation("config.hbmsntm.enableCont")
            .define("enableCont", true);
    public static final ModConfigSpec.BooleanValue ENABLE_CHUNK_RADS = BUILDER
            .comment("Toggles the world radiation system (chunk radiation only, some blocks use an AoE!)")
            .translation("config.hbmsntm.enableChuRads")
            .define("enableChuRads", true);
    public static final ModConfigSpec.BooleanValue ENABLE_PRISM_RAD = BUILDER
            .comment("Toggles PRISM radiation system")
            .translation("config.hbmsntm.enablePrismRad")
            .define("enablePrismRad", false);

    static {
       BUILDER.pop();
    }

    /// TAINT ///
    static {
        BUILDER.push("taint");
    }

    public static final ModConfigSpec.BooleanValue ENABLE_TAINT_TRAILS = BUILDER
            .comment("enables or disables taint trails")
            .translation("config.hbmsntm.enableTaintTrails")
            .define("enableTaintTrails", false);

    static {
        BUILDER.pop();
    }

    /// ============= HAZARDS ============= ///
    static {
        BUILDER.push("hazards");
    }

    public static final ModConfigSpec.BooleanValue DISABLE_ASBESTOS = BUILDER
            .comment("When turned on, all asbestos hazards are disabled")
            .translation("config.hbmsntm.disableAsbestos")
            .define("HAZ_disableAsbestos", false);
    public static final ModConfigSpec.BooleanValue DISABLE_COAL = BUILDER
            .comment("When turned on, all coal dust hazards are disabled")
            .translation("config.hbmsntm.enableCoalDust")
            .define("disableCoalDust", false);
    public static final ModConfigSpec.BooleanValue DISABLE_HOT = BUILDER
            .comment("When turned on, all hot hazards are disabled")
            .translation("config.hbmsntm.disableHot")
            .define("disableHot", false);
    public static final ModConfigSpec.BooleanValue DISABLE_EXPLOSIVE = BUILDER
            .comment("When turned on, all explosive hazards are disabled")
            .translation("config.hbmsntm.disableExplosive")
            .define("disableExplosive", false);
    public static final ModConfigSpec.BooleanValue DISABLE_HYDROACTIVE = BUILDER
            .comment("When turned on, all hydroactive hazards are disabled")
            .translation("config.hbmsntm.disableHydroactive")
            .define("disableHydroactive", false);
    public static final ModConfigSpec.BooleanValue DISABLE_BLINDING = BUILDER
            .comment("When turned off, all blinding hazards are disabled")
            .translation("config.hbmsntm.disableBlinding")
            .define("disableBlinding", false);

    static {
        BUILDER.pop();
    }

    /// ============= 528 ============= ///
    static {
        BUILDER.push("528");
    }

    public static final ModConfigSpec.BooleanValue ENABLE_528 = BUILDER
            .comment("528 mode, use with caution!")
            .translation("config.hbmsntm.enable528")
            .define("enable528", false);

    static {
        BUILDER.pop();
    }

    /// ============= BOMB ============= ///
    static {
        BUILDER.push("bomb");
    }

    public static final ModConfigSpec.IntValue EXPLOSION_ALGORITHM = BUILDER
            .comment("Configures the algorithm of mk5 explosion. \\n0 = Legacy, 1 = Threaded DDA, 2 = Threaded DDA with damage accumulation.")
            .translation("config.hbmsntm.explosionAlgorithm")
            .defineInRange("explosionAlgorithm", 2, 0, 2);
    public static final ModConfigSpec.BooleanValue ENABLE_CHUNK_LOADING = BUILDER
            .comment("Allows all types of procedural explosions to keep the central chunk loaded and to generate new chunks.")
            .translation("config.hbmsntm.enableChunkLoading")
            .define("enableChunkLoading", true);

    static {
        BUILDER.pop();
    }

    /// ============= CHANCES ============= ///
    static {
        BUILDER.push("chances");
    }

    public static final ModConfigSpec.IntValue SCHRABIDIUM_FROM_URANIUM_CHANCE = BUILDER
            .comment("chance for schrab from uranium")
            .translation("config.hbmsntm.schrabFromUran")
            .defineInRange("schrabFromUran", 20, 0, Integer.MAX_VALUE);

    static {
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
}
