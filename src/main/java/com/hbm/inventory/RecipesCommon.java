package com.hbm.inventory;

import com.hbm.items.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RecipesCommon {
    public static abstract class AStack implements Comparable<AStack> {

        protected int stackSize;

        public boolean isApplicable(ItemStack stack) {
            return isApplicable(new ComparableStack(stack));
        }

        public boolean isApplicable(ComparableStack comp) {
            if (this instanceof ComparableStack cs) {
                return cs.equals(comp);
            }

            if (this instanceof OreDictStack odStack) {
                return comp.toStack().is(odStack.getTag());
            }

            return false;
        }

        public abstract boolean matchesRecipe(ItemStack stack, boolean ignoreSize);

        public abstract AStack copy();

        public abstract List<ItemStack> extractForDisplay();

        public ItemStack extractForCyclingDisplay(int cycle) {
            List<ItemStack> list = extractForDisplay();
            cycle *= 50;

            if (list.isEmpty()) {
                return new ItemStack(ModItems.NOTHING.get());
            }

            int index = (int) ((System.currentTimeMillis() % ((long) cycle * list.size())) / cycle);
            return list.get(index);
        }

        public int getStackSize() {
            return stackSize;
        }

        public void setStackSize(int size) {
            this.stackSize = size;
        }
    }


    public static class ComparableStack extends AStack {

        public Item item;
        public int meta;

        public ComparableStack(ItemStack stack) {
            if (stack == null || stack.isEmpty()) {
                this.item = ModItems.NOTHING.get();
                this.stackSize = 1;
                this.meta = 0;
                return;
            }
            this.item = stack.getItem();
            this.stackSize = stack.getCount();
            this.meta = stack.getDamageValue();
        }

        public ComparableStack makeSingular() {
            stackSize = 1;
            return this;
        }

        public ComparableStack(Item item) {
            this.item = item == null ? ModItems.NOTHING.get() : item;
            this.stackSize = 1;
            this.meta = 0;
        }

        public ComparableStack(Block block) {
            this(block.asItem());
        }

        public ComparableStack(Item item, int stacksize, int meta) {
            this.item = item == null ? ModItems.NOTHING.get() : item;
            this.stackSize = stacksize;
            this.meta = meta;
        }

        public ItemStack toStack() {
            return new ItemStack(item == null ? ModItems.NOTHING.get() : item, stackSize);
        }

        @Override
        public int hashCode() {
            return Objects.hash(BuiltInRegistries.ITEM.getKey(item), meta, stackSize);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof ComparableStack other)) return false;
            if (!Objects.equals(item, other.item)) return false;
            if (meta != other.meta) return false;
            return stackSize == other.stackSize;
        }

        @Override
        public int compareTo(AStack stack) {
            if (stack instanceof ComparableStack comp) {
                int cmp = BuiltInRegistries.ITEM.getKey(item).compareTo(BuiltInRegistries.ITEM.getKey(comp.item));
                if (cmp != 0) return cmp;
                return Integer.compare(meta, comp.meta);
            }
            return 0;
        }

        @Override
        public AStack copy() {
            return new ComparableStack(item, stackSize, meta);
        }

        @Override
        public List<ItemStack> extractForDisplay() {
            return List.of();
        }

        public ComparableStack copy(int stacksize) {
            return new ComparableStack(item, stacksize, meta);
        }

        @Override
        public boolean matchesRecipe(ItemStack stack, boolean ignoreSize) {
            if (stack.isEmpty()) return false;
            if (stack.getItem() != this.item) return false;
            if (this.meta != -1 && stack.getDamageValue() != this.meta) return false;
            return ignoreSize || stack.getCount() >= this.stackSize;
        }
    }
    public static class OreDictStack extends AStack {
        private final TagKey<Item> tag;

        public OreDictStack(ResourceLocation tagName, int stackSize) {
            this.tag = TagKey.create(Registries.ITEM, tagName);
            this.stackSize = stackSize;
        }

        public OreDictStack(ResourceLocation tagName) {
            this(tagName, 1);
        }

        public TagKey<Item> getTag() {
            return tag;
        }

        @Override
        public boolean matchesRecipe(ItemStack stack, boolean ignoreSize) {
            if (stack.isEmpty()) return false;
            if (!stack.is(tag)) return false;

            if (!ignoreSize && stack.getCount() < this.stackSize) return false;

            return true;
        }

        @Override
        public AStack copy() {
            return new OreDictStack(tag.location(), stackSize);
        }

        @Override
        public List<ItemStack> extractForDisplay() {
            return List.of();
        }

        public List<ItemStack> extractForJEI() {
            return BuiltInRegistries.ITEM
                    .stream()
                    .filter(item -> item.builtInRegistryHolder().is(tag))
                    .map(Item::getDefaultInstance)
                    .peek(stack -> stack.setCount(stackSize))
                    .collect(Collectors.toList());
        }

        @Override
        public int compareTo(RecipesCommon.AStack o) {
            return 0;
        }
    }
}
