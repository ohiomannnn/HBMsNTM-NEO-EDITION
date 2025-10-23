package com.hbm.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    public final ModConfigSpec.BooleanValue DAMAGE_COMPATIBILITY_MODE;

    public final ModConfigSpec.BooleanValue TAINT_TRAILS;

    public final ModConfigSpec.BooleanValue CRATE_OPEN_HELD;
    public final ModConfigSpec.BooleanValue CRATE_KEEP_CONTENTS;

    ServerConfig(ModConfigSpec.Builder builder) {

        DAMAGE_COMPATIBILITY_MODE = builder
                .comment("???")
                .translation("hbmsntm.configuration.damage_compatibility_mode")
                .define("damage_compatibility_mode", false);

        TAINT_TRAILS = builder
                .comment("When turned on, taint effect will make trail.")
                .translation("hbmsntm.configuration.taint_trails")
                .define("taint_trails", false);

        CRATE_OPEN_HELD = builder
                .comment("When turned on, crates can be opened in hands.")
                .translation("hbmsntm.configuration.crate_open_held")
                .define("crate_open_held", true);

        CRATE_KEEP_CONTENTS = builder
                .comment("When turned on, crate will not drop their contents on break.")
                .translation("hbmsntm.configuration.crate_keep_contents")
                .define("crate_keep_contents", true);
    }
}
