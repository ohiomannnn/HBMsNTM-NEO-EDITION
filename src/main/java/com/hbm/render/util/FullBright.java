package com.hbm.render.util;

public class FullBright {

    private static int lastPacketLight;

    public static void enable() {
        lastPacketLight = RenderContext.light();
        RenderContext.setLight(240);
    }

    public static void disable() {
        RenderContext.setLight(lastPacketLight);
    }
}
