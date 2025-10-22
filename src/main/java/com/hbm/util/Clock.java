package com.hbm.util;

public class Clock {
    private static long timeMs;

    public static void update() {
        timeMs = System.currentTimeMillis();
    }

    public static long getMs() {
        return timeMs;
    }
}