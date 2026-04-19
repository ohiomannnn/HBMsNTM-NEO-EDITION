package com.hbm.inventory;

import com.hbm.items.IMetaItem;
import com.hbm.items.NtmItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecipesCommon {

    public static abstract class AStack implements Comparable<AStack> {

        public int stacksize;

        /**
         * Whether the supplied item stack is applicable for a recipe (e.g. anvils). Slightly different from {@code isApplicable}.
         * @param stack the ItemStack to check
         * @param ignoreSize whether stacksize should be ignored entirely or if the ItemStack needs to be >at least< the same stacksize as this' stacksize
         */
        public abstract boolean matchesRecipe(ItemStack stack, boolean ignoreSize);

        public abstract AStack copy();
        public abstract AStack copy(int size);

        /**
         * Generates either an ItemStack or an ArrayList of ItemStacks
         */
        public abstract List<ItemStack> extractForJEI();

        public ItemStack extractForCyclingDisplay(int cycle) {
            List<ItemStack> list = extractForJEI();
            cycle *= 50;

            if (list.isEmpty()) return new ItemStack(NtmItems.NOTHING.get());
            return list.get((int)(System.currentTimeMillis() % (cycle * list.size()) / cycle));
        }

    }

    public static class ComparableStack extends AStack {

        public Item item;
        public int meta;

        public ComparableStack(ItemStack stack) {
            if (stack.isEmpty()) {
                this.item = NtmItems.NOTHING.get();
                this.stacksize = 1;
                return;
            }
            this.item = stack.getItem();
            this.stacksize = stack.getCount();
            this.meta = MetaHelper.getMeta(stack);
        }

        public ComparableStack makeSingular() {
            this.stacksize = 1;
            return this;
        }

        public ComparableStack(Item item) {
            this.item = item;
            this.stacksize = 1;
        }

        public ComparableStack(Block item) {
            this.item = item.asItem();
            this.stacksize = 1;
        }

        public ComparableStack(Block item, int stacksize) {
            this.item = item.asItem();
            this.stacksize = stacksize;
        }

        public ComparableStack(Item item, int stacksize) {
            this(item);
            this.stacksize = stacksize;
        }

        public ComparableStack(Item item, int stacksize, int meta) {
            this(item);
            this.stacksize = stacksize;
            this.meta = meta;
        }

        public ItemStack toStack() {
            return MetaHelper.newStack(item, stacksize, meta);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + BuiltInRegistries.ITEM.getKey(item).hashCode(); //using the int ID will cause fucky-wuckys if IDs are scrambled
            result = prime * result + stacksize;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj) return true;
            if(obj == null) return false;
            if(getClass() != obj.getClass()) return false;
            ComparableStack other = (ComparableStack) obj;
            if (item == null) {
                if (other.item != null) return false;
            } else if (!item.equals(other.item)) {
                return false;
            }
            if(meta != MetaHelper.WILDCARD_VALUE && other.meta != MetaHelper.WILDCARD_VALUE && meta != other.meta) return false;
            if(stacksize != other.stacksize) return false;
            return true;
        }

        @Override
        public int compareTo(AStack stack) {

            if (stack instanceof ComparableStack comp) {
                int thisID = Item.getId(item);
                int thatID = Item.getId(comp.item);

                return Integer.compare(thisID, thatID);
            }

            return 0;
        }

        @Override
        public ComparableStack copy() {
            return new ComparableStack(item, stacksize);
        }

        @Override
        public ComparableStack copy(int stackSize) {
            return new ComparableStack(item, stackSize);
        }

        @Override
        public boolean matchesRecipe(ItemStack stack, boolean ignoreSize) {

            if (stack == null) return false;
            if (stack.getItem() != this.item) return false;
            if (!ignoreSize && stack.getCount() < this.stacksize) return false;

            return true;
        }

        @Override
        public List<ItemStack> extractForJEI() {
            return Collections.singletonList(this.toStack());
        }
    }

    public static class TagStack extends AStack {

        public TagKey<Item> tag;

        public TagStack(TagKey<Item> tag) {
            this.tag = tag;
            this.stacksize = 1;
        }

        public TagStack(TagKey<Item> tag, int stacksize) {
            this(tag);
            this.stacksize = stacksize;
        }

        @Override
        public int compareTo(AStack stack) {

            if(stack instanceof TagStack comp) {
                return this.tag.location().compareTo(comp.tag.location());
            }

            //if compared with a TStack, the ODStack will yield
            if(stack instanceof ComparableStack) return -1;

            return 0;
        }

        @Override
        public AStack copy() {
            return new TagStack(tag, stacksize);
        }

        @Override
        public AStack copy(int stacksize) {
            return new TagStack(tag, stacksize);
        }

        @Override
        public boolean matchesRecipe(ItemStack stack, boolean ignoreSize) {

            if(stack.isEmpty()) return false;

            if(!ignoreSize && stack.getCount() < this.stacksize) return false;

            for(TagKey<Item> tagKey : stack.getTags().toList()) {
                if(tagKey.location().equals(this.tag.location())) return true;
            }

            return false;
        }

        @Override
        public List<ItemStack> extractForJEI() {

            List<ItemStack> itemStacks = new ArrayList<>();

            for (Holder<Item> itemHolder : BuiltInRegistries.ITEM.getTagOrEmpty(this.tag)) {
                Item item = itemHolder.value();

                if(item instanceof IMetaItem) {
                    itemStacks.add(new ItemStack(item, 1));
                }
            }

            return itemStacks;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + this.tag.location().hashCode();
            result = prime * result + this.stacksize;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj)
                return true;
            if(obj == null)
                return false;
            if(getClass() != obj.getClass())
                return false;
            TagStack other = (TagStack) obj;
            if(tag == null) {
                if(other.tag != null)
                    return false;
            } else if(!tag.location().equals(other.tag.location())) {
                return false;
            }
            if(this.stacksize != other.stacksize)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return this.stacksize + "x" + this.tag.location();
        }
    }

    public static class StateBlock {
        public final BlockState state;

        public StateBlock(BlockState state) {
            this.state = state;
        }

        public StateBlock(Block block) {
            this(block.defaultBlockState());
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + BuiltInRegistries.BLOCK.getKey(state.getBlock()).hashCode();
            return result;
        }


        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            StateBlock other = (StateBlock) obj;
            if (state == null) {
                return other.state == null;
            } else return state.equals(other.state);
        }
    }
}
