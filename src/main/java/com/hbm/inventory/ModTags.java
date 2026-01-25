package com.hbm.inventory;

import com.hbm.HBMsNTM;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;


// Basically new ore dict and its vanilla
// Registration in datagens
public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> ACTUALLY_STONE = crateTag("actually_stone");
        public static final TagKey<Block> GROUND = crateTag("ground");
        public static final TagKey<Block> LEAVES = crateTag("leaves");

        private static TagKey<Block> crateTag(String name) {
            return BlockTags.create(HBMsNTM.withDefaultNamespaceNT(name));
        }
    }

    public static class Items {

        /*
         * TANKS
         */
        public static final TagKey<Item> UNIVERSAL_TANK = crateTag("ntm_universal_tank");
        public static final TagKey<Item> HAZARD_TANK = crateTag("ntm_hazard_tank");
        public static final TagKey<Item> UNIVERSAL_BARREL = crateTag("ntm_universal_barrel");

        private static TagKey<Item> crateTag(String name) {
            return ItemTags.create(HBMsNTM.withDefaultNamespaceNT(name));
        }
        private static TagKey<Item> commonTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }
}
