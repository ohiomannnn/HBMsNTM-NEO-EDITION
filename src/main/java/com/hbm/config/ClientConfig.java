package com.hbm.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static {
        BUILDER.push("GUI");
    }
    public static final ModConfigSpec.IntValue GEIGER_OFFSET_HORIZONTAL = BUILDER
            .comment("offset geiger horizontal")
            .translation("config.hbmsntm.ge_of_hor")
            .defineInRange("ge_of_hor", 0, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue GEIGER_OFFSET_VERTICAL = BUILDER
            .comment("offset geiger vertical")
            .translation("config.hbmsntm.ge_of_vert")
            .defineInRange("ge_of_vert", 0, 0, Integer.MAX_VALUE);

    static {
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
}
