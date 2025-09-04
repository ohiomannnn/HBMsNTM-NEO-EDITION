package com.hbm.inventory;

import com.hbm.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Objects;

public class RecipesCommon {
    public static abstract class AStack implements Comparable<AStack> {

        protected int stackSize;

        public boolean isApplicable(ItemStack stack) {
            return isApplicable(new ComparableStack(stack));
        }

        /**
         * Проверяет, подходит ли предмет под сравнение (учитывая тип реализации).
         */
        public boolean isApplicable(ComparableStack comp) {
            if (this instanceof ComparableStack cs) {
                return cs.equals(comp);
            }

            if (this instanceof OreDictStack odStack) {
                // В новых версиях вместо OreDictionary нужно использовать Tags
                // Здесь пример через ForgeRegistries
                List<ItemStack> ores = odStack.toStacks();

                for (ItemStack stack : ores) {
                    if (stack.getItem() == comp.item && stack.getDamageValue() == comp.meta) {
                        return true;
                    }
                }
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
            this(Item.byBlock(block));
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
            return Objects.hash(ForgeRegistries.ITEMS.getKey(item), meta, stackSize);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof ComparableStack other)) return false;
            if (!Objects.equals(item, other.item)) return false;
            if (meta != other.meta) return false; // WILDCARD можно будет отдельно прикрутить
            return stackSize == other.stackSize;
        }

        @Override
        public int compareTo(AStack stack) {
            if (stack instanceof ComparableStack comp) {
                int cmp = ForgeRegistries.ITEMS.getKey(item).compareTo(ForgeRegistries.ITEMS.getKey(comp.item));
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

}
