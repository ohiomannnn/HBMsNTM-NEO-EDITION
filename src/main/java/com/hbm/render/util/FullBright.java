package com.hbm.render.util;

public class FullBright {

    private static int lastPacketLight;

    public static void enable() {
        lastPacketLight = RenderStateManager.light();
        RenderStateManager.setLight(240);
    }

    public static void disable() {
        RenderStateManager.setLight(lastPacketLight);
    }
}
