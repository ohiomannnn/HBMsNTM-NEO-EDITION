package com.hbm.render.loader.bakedLoader;

import com.hbm.render.loader.HFRWavefrontObject;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HFRObjModelCache {

    private static final Map<CacheKey, HFRWavefrontObject> CACHE = new ConcurrentHashMap<>();

    private record CacheKey(ResourceLocation location, boolean smoothing) {}

    public static HFRWavefrontObject getOrLoad(ResourceLocation location, boolean smoothing) {
        CacheKey key = new CacheKey(location, smoothing);
        return CACHE.computeIfAbsent(key, k -> new HFRWavefrontObject(location, smoothing));
    }

    public static void clearCache() {
        CACHE.clear();
    }

    public static void invalidate(ResourceLocation location) {
        CACHE.entrySet().removeIf(entry -> entry.getKey().location().equals(location));
    }

    public static int getCacheSize() {
        return CACHE.size();
    }
}