package com.hbm.inventory;

import com.hbm.HBMsNTM;
import com.hbm.config.MainConfig;
import com.hbm.items.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.List;

public class RecipesCommon {

    public static ItemStack[] copyStackArray(ItemStack[] array) {

        if (array == null) return null;

        ItemStack[] clone = new ItemStack[array.length];

        for (int i = 0; i < array.length; i++) {

            if (array[i] != null) clone[i] = array[i].copy();
        }

        return clone;
    }

    public static ItemStack[] objectToStackArray(Object[] array) {

        if (array == null) return null;

        ItemStack[] clone = new ItemStack[array.length];

        for (int i = 0; i < array.length; i++) {

            if (array[i] instanceof ItemStack) clone[i] = (ItemStack)array[i];
        }

        return clone;
    }

    public static abstract class AStack implements Comparable<AStack> {

        public int stackSize;

        /**
         * Whether the supplied itemstack is applicable for a recipe (e.g. anvils). Slightly different from {@code isApplicable}.
         * @param stack the ItemStack to check
         * @param ignoreSize whether size should be ignored entirely or if the ItemStack needs to be >at least< the same size as this' size
         */
        public abstract boolean matchesRecipe(ItemStack stack, boolean ignoreSize);

        public abstract AStack copy();
        public abstract AStack copy(int stackSize);

        /**
         * Generates either an ItemStack or an ArrayList of ItemStacks
         */
        public abstract List<ItemStack> extractForJEI();

        public ItemStack extractForCyclingDisplay(int cycle) {
            List<ItemStack> list = extractForJEI();
            cycle *= 50;

            if (list.isEmpty()) return new ItemStack(ModItems.NOTHING.get());
            return list.get((int)(System.currentTimeMillis() % (cycle * list.size()) / cycle));
        }

    }

    public static class ComparableStack extends AStack {

        public Item item;
        //public int meta; meta? i dont know you

        public ComparableStack(ItemStack stack) {
            if (stack.isEmpty()) {
                this.item = ModItems.NOTHING.get();
                this.stackSize = 1;
                return;
            }
            try {
                this.item = stack.getItem();
                if (this.item == null) ModItems.NOTHING.get(); //i'm going to bash some fuckard's head in
                this.stackSize = stack.getCount();
            } catch (Exception ex) {
                this.item = ModItems.NOTHING.get();
                if (!MainConfig.COMMON.ENABLE_SILENT_COMPSTACK_ERRORS.get()) {
                    ex.printStackTrace();
                }
            }
        }

        public ComparableStack makeSingular() {
            stackSize = 1;
            return this;
        }

        public ComparableStack(Item item) {
            this.item = item;
            if (this.item == null) this.item = ModItems.NOTHING.get();
            this.stackSize = 1;
        }

        public ComparableStack(Block item) {
            this.item = item.asItem();
            this.stackSize = 1;
        }

        public ComparableStack(Block item, int stackSize) {
            this.item = item.asItem();
            this.stackSize = stackSize;
        }

        public ComparableStack(Item item, int stackSize) {
            this(item);
            this.stackSize = stackSize;
        }

        public ItemStack toStack() {
            return new ItemStack(item == null ? ModItems.NOTHING.get() : item, stackSize);
        }

        @Override
        public int hashCode() {

            if (item == null) {
                if (!MainConfig.COMMON.ENABLE_SILENT_COMPSTACK_ERRORS.get()) {
                    HBMsNTM.LOGGER.error("ComparableStack has a null item! This is a serious issue!");
                    Thread.dumpStack();
                }
                item = ModItems.NOTHING.get();
            }

            final int prime = 31;
            int result = 1;
            result = prime * result +  BuiltInRegistries.ITEM.getKey(item).hashCode(); //using the int ID will cause fucky-wuckys if IDs are scrambled
            result = prime * result + stackSize;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            ComparableStack other = (ComparableStack) obj;
            if(item == null) {
                if(other.item != null) return false;
            } else if(!item.equals(other.item))
                return false;
            return stackSize == other.stackSize;
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
            return new ComparableStack(item, stackSize);
        }

        @Override
        public ComparableStack copy(int stackSize) {
            return new ComparableStack(item, stackSize);
        }


        @Override
        public boolean matchesRecipe(ItemStack stack, boolean ignoreSize) {

            if (stack == null) return false;
            if (stack.getItem() != this.item) return false;
            if (!ignoreSize && stack.getCount() < this.stackSize) return false;

            return true;
        }

        @Override
        public List<ItemStack> extractForJEI() {
            return Collections.singletonList(this.toStack());
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
                if (other.state != null) return false;
            } else if (!state.equals(other.state)) return false;
            return true;
        }
    }
}
