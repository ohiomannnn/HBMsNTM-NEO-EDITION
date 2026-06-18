package com.hbm.items.machine;

import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.IMetaItem;
import com.hbm.items.NtmItems;
import com.hbm.util.TagsUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map.Entry;

public class BlueprintsItem extends Item implements IMetaItem {

    public BlueprintsItem(Properties properties) {
        super(properties);
    }

    @Override
    public void getSubItems(Item item, List<ItemStack> stacks) {
        for(Entry<String, List<String>> pool : GenericRecipes.blueprintPools.entrySet()) {
            String poolName = pool.getKey();
            if(!poolName.startsWith(GenericRecipes.POOL_PREFIX_SECRET)) stacks.add(make(poolName));
        }
    }

    public static String grabPool(ItemStack stack) {
        if(stack == null) return null;
        if(stack.getItem() != NtmItems.BLUEPRINTS.get()) return null;
        if(!TagsUtil.hasCData(stack)) return null;

        CompoundTag tag = TagsUtil.getCData(stack);
        if(!tag.contains("pool")) return null;
        return tag.getString("pool");
    }

    public static ItemStack make(String pool) {
        ItemStack stack = new ItemStack(NtmItems.BLUEPRINTS.get());
        CompoundTag tag = new CompoundTag();
        tag.putString("pool", pool);
        TagsUtil.putCData(stack, tag);
        return stack;
    }
}
