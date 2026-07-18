package com.hbm.items.machine;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StampItem extends Item {

    public enum StampType {
        FLAT,
        PLATE,
        WIRE,
        CIRCUIT,
        C357,
        C44,
        C50,
        C9,
        PRINTING1,
        PRINTING2,
        PRINTING3,
        PRINTING4,
        PRINTING5,
        PRINTING6,
        PRINTING7,
        PRINTING8
    }

    public final StampType type;
    public static final HashMap<StampType, List<ItemStack>> stamps = new HashMap<>();

    public StampItem(int dura, StampType type) {
        super(new Item.Properties().durability(dura));

        this.type = type;

        if(type != null) {
            this.addStampToList(this, type);
        }
    }

    protected void addStampToList(Item item, StampType type) {
        List<ItemStack> list = stamps.get(type);

        if(list == null) list = new ArrayList<>();

        ItemStack stack = new ItemStack(item, 1);

        list.add(stack);
        stamps.put(type, list);
    }
}
