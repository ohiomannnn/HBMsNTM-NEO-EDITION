package com.hbm.render.block.loader;

@FunctionalInterface
public interface BlockRendererProvider {
    BlockRenderer create();
}
