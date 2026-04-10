package com.hbm.util;

import net.minecraft.util.RandomSource;

import java.util.Collection;
import java.util.Iterator;

// Legacy 1.7.10
public class WeightedRandom {

    /**
     * Returns the total weight of all items in a collection.
     */
    public static int getTotalWeight(Collection p_76272_0_)
    {
        int i = 0;
        WeightedRandom.Item item;

        for (Iterator iterator = p_76272_0_.iterator(); iterator.hasNext(); i += item.itemWeight)
        {
            item = (WeightedRandom.Item)iterator.next();
        }

        return i;
    }

    /**
     * Returns a random choice from the input items, with a total weight value.
     */
    public static WeightedRandom.Item getRandomItem(RandomSource p_76273_0_, Collection p_76273_1_, int p_76273_2_)
    {
        if (p_76273_2_ <= 0)
        {
            throw new IllegalArgumentException();
        }
        return getItem(p_76273_1_, p_76273_0_.nextInt(p_76273_2_));
    }

    //Forge: Added to allow custom random implementations, Modder is responsible for making sure the
    //'weight' is under the totalWeight of the items.
    public static WeightedRandom.Item getItem(Collection par1Collection, int weight)
    {
        {
            int j = weight;
            Iterator iterator = par1Collection.iterator();
            WeightedRandom.Item item;

            do
            {
                if (!iterator.hasNext())
                {
                    return null;
                }

                item = (WeightedRandom.Item)iterator.next();
                j -= item.itemWeight;
            }
            while (j >= 0);

            return item;
        }
    }

    /**
     * Returns a random choice from the input items.
     */
    public static WeightedRandom.Item getRandomItem(RandomSource p_76271_0_, Collection p_76271_1_)
    {
        /**
         * Returns a random choice from the input items, with a total weight value.
         */
        return getRandomItem(p_76271_0_, p_76271_1_, getTotalWeight(p_76271_1_));
    }



    public static class Item {
        /** The Weight is how often the item is chosen(higher number is higher chance(lower is lower)) */
        public int itemWeight;

        public Item(int itemWeight) {
            this.itemWeight = itemWeight;
        }
    }
}
