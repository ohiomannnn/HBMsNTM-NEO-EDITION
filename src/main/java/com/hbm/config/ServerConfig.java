package com.hbm.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    /// RADIATION ///
    static {
        BUILDER.push("radiation");
    }

    public static final ModConfigSpec.BooleanValue ENABLE_CONTAMINATION = BUILDER
            .comment("enables or disables contamination")
            .translation("config.hbmsntm.enableRad")
            .define("enableRad", true);

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
