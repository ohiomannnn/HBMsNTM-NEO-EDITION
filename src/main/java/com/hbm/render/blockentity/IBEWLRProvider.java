package com.hbm.render.blockentity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;

public interface IBEWLRProvider {
    Item getItemForRenderer();

    default Item[] getItemsForRenderer() {
        return new Item[] { this.getItemForRenderer() };
    }

    BlockEntityWithoutLevelRenderer getRenderer();
}
