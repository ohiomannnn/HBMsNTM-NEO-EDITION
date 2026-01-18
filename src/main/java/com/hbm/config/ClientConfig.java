package com.hbm.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

    public final ModConfigSpec.BooleanValue ENABLE_NTM_SKYBOX;

    public final ModConfigSpec.BooleanValue ENABLE_NUKE_HUD_FLASH;
    public final ModConfigSpec.BooleanValue ENABLE_NUKE_HUD_SHAKE;

    public final ModConfigSpec.IntValue INFO_POSITION;
    public final ModConfigSpec.IntValue INFO_OFFSET_HORIZONTAL;
    public final ModConfigSpec.IntValue INFO_OFFSET_VERTICAL;

    public final ModConfigSpec.BooleanValue ENABLE_MAIN_MENU_WACKY_SPLASHES;
    public final ModConfigSpec.BooleanValue ENABLE_TIPS;

    public final ModConfigSpec.IntValue TOOL_HUD_OFFSET_HORIZONTAL;
    public final ModConfigSpec.IntValue TOOL_HUD_OFFSET_VERTICAL;

    ClientConfig(ModConfigSpec.Builder builder) {

        ENABLE_NTM_SKYBOX = builder
                .comment("Toggles ntm skybox.")
                .translation("hbmsntm.configuration.enableNtmSkybox")
                .define("enableNtmSkybox", true);

        ENABLE_NUKE_HUD_FLASH = builder
                .comment("Toggles flash from nuke explosion.")
                .translation("hbmsntm.configuration.enableNukeHudFlash")
                .define("enableNukeHudFlash", true);
        ENABLE_NUKE_HUD_SHAKE = builder
                .comment("Toggles hud shake from nuke explosion.")
                .translation("hbmsntm.configuration.enableNukeHudShake")
                .define("enableNukeHudShake", true);

        INFO_POSITION = builder
                .comment("Info position: 0 - top left, 1 - top right, 2 - next to the crosshair.")
                .translation("hbmsntm.configuration.infoPosition")
                .defineInRange("infoPosition", 0, 0, 2);
        INFO_OFFSET_HORIZONTAL = builder
                .comment("Horizontal info offset.")
                .translation("hbmsntm.configuration.infoOffsetHorizontal")
                .defineInRange("infoOffsetHorizontal", 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
        INFO_OFFSET_VERTICAL = builder
                .comment("Vertical info offset.")
                .translation("hbmsntm.configuration.infoOffsetVertical")
                .defineInRange("infoOffsetVertical", 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);

        ENABLE_MAIN_MENU_WACKY_SPLASHES = builder
                .comment("Toggles wacky splashes in the main menu.")
                .translation("hbmsntm.configuration.mainMenuWackySplashes")
                .define("mainMenuWackySplashes", true);

        ENABLE_TIPS = builder
                .comment("Toggles tips in the world loading screen.")
                .translation("hbmsntm.configuration.tips")
                .define("tips", true);

        TOOL_HUD_OFFSET_HORIZONTAL = builder
                .comment("Tool hud indicator x offset.")
                .translation("hbmsntm.configuration.toolHudIndicatorX")
                .defineInRange("toolHudIndicatorX", 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
        TOOL_HUD_OFFSET_VERTICAL = builder
                .comment("Tool hud indicator x offset.")
                .translation("hbmsntm.configuration.toolHudIndicatorY")
                .defineInRange("toolHudIndicatorY", 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
}
