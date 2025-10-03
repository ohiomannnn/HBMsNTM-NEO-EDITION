package com.hbm.util;

import com.hbm.HBMsNTM;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> ACTUALLY_STONE = crateTag("actually_stone");
        public static final TagKey<Block> GROUND = crateTag("ground");

        private static TagKey<Block> crateTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, name));
        }
    }
    public static class Items {

        private static TagKey<Item> crateTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, name));
        }
    }
}
