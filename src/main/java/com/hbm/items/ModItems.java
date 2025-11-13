package com.hbm.items;

import com.hbm.HBMsNTM;
import com.hbm.handler.abilities.IToolAreaAbility;
import com.hbm.handler.abilities.IWeaponAbility;
import com.hbm.inventory.tiers.ModTiers;
import com.hbm.items.special.DangerousDropItem;
import com.hbm.items.special.EntitySpawnerItem;
import com.hbm.items.special.PolaroidItem;
import com.hbm.items.tools.*;
import com.hbm.items.tools.ToolAbilityItem.EnumToolType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HBMsNTM.MODID);

    public static final DeferredItem<Item> NOTHING = ITEMS.register("nothing",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DETONATOR = ITEMS.register("detonator",
            () -> new DetonatorItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MULTI_DETONATOR = ITEMS.register("multi_detonator",
            () -> new MultiDetonatorItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR_DEADMAN = ITEMS.register("detonator_deadman",
            () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR_DE = ITEMS.register("detonator_de",
            () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> CELL_ANTIMATTER = ITEMS.register("cell_antimatter",
            () -> new DangerousDropItem(new Item.Properties()));

    public static final DeferredItem<Item> GEIGER_COUNTER = ITEMS.register("geiger_counter",
            () -> new GeigerCounterItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DOSIMETER = ITEMS.register("dosimeter",
            () -> new DosimeterItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> REACHER = ITEMS.register("reacher",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SCREWDRIVER = ITEMS.register("screwdriver",
            () -> new Item(new Item.Properties().stacksTo(8)));
    public static final DeferredItem<Item> SCREWDRIVER_DESH = ITEMS.register("screwdriver_desh",
            () -> new Item(new Item.Properties().stacksTo(8)));

    public static final DeferredItem<Item> PIN = ITEMS.register(
            "pin",
            () -> new Item(new Item.Properties().stacksTo(8)));
    public static final DeferredItem<Item> KEY = ITEMS.register(
            "key",
            () -> new KeyItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KEY_RED = ITEMS.register(
            "key_red",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KEY_KIT = ITEMS.register(
            "key_kit",
            () -> new KeyItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KEY_FAKE = ITEMS.register(
            "key_fake",
            () -> new KeyItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> ALLOY_SWORD = ITEMS.register(
            "alloy_sword",
            () -> new SwordItem(ModTiers.ALLOY, new Item.Properties().stacksTo(1).attributes(SwordItem.createAttributes(ModTiers.ALLOY, 8, -2.4F))));

    public static final DeferredItem<Item> SCHRAB_PICKAXE = ITEMS.register(
            "schrabidium_pickaxe",
            () -> new ToolAbilityItem(Tiers.NETHERITE, EnumToolType.PICKAXE, new Item.Properties()
                    .stacksTo(1)
                    .attributes(ToolAbilityItem.createAttributes(Tiers.NETHERITE, 20, -2.8F))
                    .rarity(Rarity.RARE))
                    .addAbility(IWeaponAbility.RADIATION, 250).addAbility(IToolAreaAbility.EXPLOSION, 3)
                    .addAbility(IToolAreaAbility.RECURSION, 6));

    public static final DeferredItem<Item> DUCK_SPAWN_EGG = ITEMS.register(
            "duck_spawn_egg",
            () -> new EntitySpawnerItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> BURNT_BARK = ITEMS.register(
            "burnt_bark",
            () -> new EntitySpawnerItem(new Item.Properties()));
    public static final DeferredItem<Item> FLINT_AND_BALEFIRE = ITEMS.register(
            "balefire_and_steel",
            () -> new BalefireAndSteelITem(new Item.Properties()
                    .stacksTo(1)
                    .durability(256)));

    public static final DeferredItem<Item> POLAROID = ITEMS.register(
            "polaroid",
            () -> new PolaroidItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}