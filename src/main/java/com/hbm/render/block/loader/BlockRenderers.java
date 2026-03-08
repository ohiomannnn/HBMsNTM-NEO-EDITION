package com.hbm.render.block.loader;

import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockRenderers {

    private static final Map<Block, BlockRendererProvider> PROVIDERS = new ConcurrentHashMap<>();

    public static <T extends Block> void register(T block, BlockRendererProvider provider) {
        if (PROVIDERS.containsKey(block)) throw new IllegalArgumentException("Duplicate block renderer registration for: " + block);
        PROVIDERS.put(block, provider);
    }

    public static boolean hasRenderer(Block block) {
        return PROVIDERS.containsKey(block);
    }

    public static Map<Block, BlockRendererProvider> getProviders() {
        // you cant modify it, no
        return Collections.unmodifiableMap(PROVIDERS);
    }
}
