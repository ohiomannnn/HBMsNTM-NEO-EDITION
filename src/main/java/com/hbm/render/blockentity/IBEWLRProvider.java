package com.hbm.render.blockentity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IBEWLRProvider {

    default Item getItemForRenderer() {
        return ItemStack.EMPTY.getItem();
    }

    default Item[] getItemsForRenderer() {
        return new Item[] { this.getItemForRenderer() };
    }

    BlockEntityWithoutLevelRenderer getRenderer();
}
