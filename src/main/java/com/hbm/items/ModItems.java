package com.hbm.items;

import com.hbm.HBMsNTM;
import com.hbm.items.special.ItemEntSpawner;
import com.hbm.items.tools.ItemGeigerCounter;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HBMsNTM.MODID);

    public static final DeferredItem<Item> NOTHING = ITEMS.register("nothing",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GEIGER_COUNTER = ITEMS.register("geiger_counter",
            () -> new ItemGeigerCounter(new Item.Properties()));
    public static final DeferredItem<Item> REACHER = ITEMS.register("reacher",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> DUCK_SPAWN_EGG = ITEMS.register("duck_spawn_egg",
            () -> new ItemEntSpawner(new Item.Properties().stacksTo(16)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}