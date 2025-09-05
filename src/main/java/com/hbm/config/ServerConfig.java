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
            .translation("config.hbmsntm.fograd")
            .defineInRange("fograd", 20, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue FOG_CHANCE = BUILDER
            .comment("1:n chance of fog spawning every second")
            .translation("config.hbmsntm.fogch")
            .defineInRange("fogch", 100, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.BooleanValue WORLD_RAD_EFFECTS = BUILDER
            .comment("Whether high radiation levels should perform changes in the world")
            .translation("config.hbmsntm.fogch")
            .define("enablerad", true);


    public static final ModConfigSpec.BooleanValue ENABLE_CONTAMINATION = BUILDER
            .comment("Toggles player contamination (and negative effects from radiation poisoning)")
            .translation("config.hbmsntm.enablerad")
            .define("enablerad", true);
    public static final ModConfigSpec.BooleanValue ENABLE_CHUNK_RADS = BUILDER
            .comment("Toggles the world radiation system (chunk radiation only, some blocks use an AoE!)")
            .translation("config.hbmsntm.enablerad")
            .define("enablerad", true);
    public static final ModConfigSpec.BooleanValue ENABLE_PRISM_RAD = BUILDER
            .comment("Toggles PRISM radiation system")
            .translation("config.hbmsntm.enablerad")
            .define("enableprismrad", true);

    static {
       BUILDER.pop();
    }

    /// ASBESTOS ///
    static {
        BUILDER.push("asbestos");
    }

    public static final ModConfigSpec.BooleanValue ENABLE_ASBESTOS = BUILDER
            .comment("enables or disables asbestos")
            .translation("config.hbmsntm.enableAsbestos")
            .define("enableAsbestos", true);

    static {
       BUILDER.pop();
    }

    /// BLACK LUNG ///
    static {
        BUILDER.push("blacklung");
    }

    public static final ModConfigSpec.BooleanValue ENABLE_BLACKLUNG = BUILDER
            .comment("enables or disables asbestos")
            .translation("config.hbmsntm.enableBlacklung")
            .define("enableBlacklung", true);

    static {
        BUILDER.pop();
    }
    /// TAINT ///
    static {
        BUILDER.push("taint");
    }

    public static final ModConfigSpec.BooleanValue ENABLE_TAINT_TRAIL = BUILDER
            .comment("enables or disables taint trails")
            .translation("config.hbmsntm.enableBlacklung")
            .define("enableBlacklung", true);

    static {
        BUILDER.pop();
    }


    public static final ModConfigSpec SPEC = BUILDER.build();
}
