package com.hbm.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static {
        BUILDER.push("hud");
    }

    public static final ModConfigSpec.BooleanValue NUKE_HUD_FLASH = BUILDER
            .comment("Enables flash from nuke explosion")
            .translation("config.hbmsntm.nukeHudFlash")
            .define("nukeHudFlash", true);
    public static final ModConfigSpec.BooleanValue NUKE_HUD_SHAKE = BUILDER
            .comment("Enables hud shake from nuke explosion")
            .translation("config.hbmsntm.nukeHudShake")
            .define("nukeHudShake", true);

    public static final ModConfigSpec.IntValue INFO_POSITION = BUILDER
            .comment("enables or disables taint trails")
            .translation("config.hbmsntm.infoPosition")
            .defineInRange("infoPosition", 0, 0, 2);

    public static final ModConfigSpec.IntValue INFO_OFFSET_HORIZONTAL = BUILDER
            .comment("enables or disables taint trails")
            .translation("config.hbmsntm.infoOffsetHorizontal")
            .defineInRange("infoOffsetHorizontal", 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue INFO_OFFSET_VERTICAL = BUILDER
            .comment("enables or disables taint trails")
            .translation("config.hbmsntm.infoOffsetVertical")
            .defineInRange("infoOffsetVertical", 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);

    static {
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
}