package com.hbm.items;

import com.hbm.HBMsNTM;
import com.hbm.items.special.EntitySpawnerItem;
import com.hbm.items.special.ItemPolaroid;
import com.hbm.items.tools.BalefireAndSteelITem;
import com.hbm.items.tools.DosimeterItem;
import com.hbm.items.tools.GeigerCounterItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Random;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HBMsNTM.MODID);

    public static final DeferredItem<Item> NOTHING = ITEMS.register("nothing",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GEIGER_COUNTER = ITEMS.register("geiger_counter",
            () -> new GeigerCounterItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DOSIMETER = ITEMS.register("dosimeter",
            () -> new DosimeterItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> REACHER = ITEMS.register("reacher",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> DUCK_SPAWN_EGG = ITEMS.register("duck_spawn_egg",
            () -> new EntitySpawnerItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> BURNT_BARK = ITEMS.register("burnt_bark",
            () -> new EntitySpawnerItem(new Item.Properties()));
    public static final DeferredItem<Item> FLINT_AND_BALEFIRE = ITEMS.register("balefire_and_steel",
            () -> new BalefireAndSteelITem(new Item.Properties()
                    .stacksTo(1)
                    .durability(256)));

    public static final DeferredItem<Item> POLAROID = ITEMS.register("polaroid",
            () -> new ItemPolaroid(new Item.Properties()
                    .stacksTo(1)
                    .durability(256)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}