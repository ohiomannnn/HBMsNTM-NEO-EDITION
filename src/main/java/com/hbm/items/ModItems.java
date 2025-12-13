package com.hbm.items;

import com.hbm.HBMsNTM;
import com.hbm.handler.ability.IToolAreaAbility;
import com.hbm.handler.ability.IToolHarvestAbility;
import com.hbm.handler.ability.IWeaponAbility;
import com.hbm.inventory.ModArmorMaterials;
import com.hbm.inventory.ModTiers;
import com.hbm.items.food.ItemEnergy;
import com.hbm.items.machine.SatChipItem;
import com.hbm.items.special.DangerousDropItem;
import com.hbm.items.special.EntitySpawnerItem;
import com.hbm.items.special.PolaroidItem;
import com.hbm.items.tools.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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

    public static final DeferredItem<Item> GEIGER_COUNTER = ITEMS.register(
            "geiger_counter",
            () -> new GeigerCounterItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> DOSIMETER = ITEMS.register(
            "dosimeter",
            () -> new DosimeterItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> DIGAMMA_DIAGNOSTIC = ITEMS.register(
            "digamma_diagnostic",
            () -> new DigammaDiagnosticItem(new Item.Properties().stacksTo(1)));


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

    public static final DeferredItem<Item> BOTTLE_OPENER = ITEMS.register(
            "bottle_opener",
            () -> new Item(new Item.Properties().stacksTo(1))
    );

    public static final DeferredItem<Item> CAP_NUKA = ITEMS.register(
            "cap_nuka",
            () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> CAP_QUANTUM = ITEMS.register(
            "cap_quantum",
            () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> CAP_SPARKLE = ITEMS.register(
            "cap_sparkle",
            () -> new Item(new Item.Properties())
    );

    public static final DeferredItem<Item> BOTTLE_EMPTY = ITEMS.register(
            "bottle_empty",
            () -> new Item(new Item.Properties())
    );

    public static final DeferredItem<Item> BOTTLE_NUKA = ITEMS.register(
            "bottle_nuka",
            () -> new ItemEnergy(new Item.Properties()).makeBottle(BOTTLE_EMPTY.get(), CAP_NUKA.get())
    );
    public static final DeferredItem<Item> BOTTLE_CHERRY = ITEMS.register(
            "bottle_cherry",
            () -> new ItemEnergy(new Item.Properties()).makeBottle(BOTTLE_EMPTY.get(), CAP_NUKA.get())
    );
    public static final DeferredItem<Item> BOTTLE_QUANTUM = ITEMS.register(
            "bottle_quantum",
            () -> new ItemEnergy(new Item.Properties()).makeBottle(BOTTLE_EMPTY.get(), CAP_QUANTUM.get())
    );
    public static final DeferredItem<Item> BOTTLE_SPARKLE = ITEMS.register(
            "bottle_sparkle",
            () -> new ItemEnergy(new Item.Properties()).makeBottle(BOTTLE_EMPTY.get(), CAP_SPARKLE.get())
    );
    public static final DeferredItem<Item> CHOCOLATE_MILK = ITEMS.register(
            "chocolate_milk",
            () -> new ItemEnergy(new Item.Properties())
    );

    public static final DeferredItem<Item> ALLOY_SWORD = ITEMS.register(
            "alloy_sword",
            () -> new SwordAbilityItem(new Item.Properties().stacksTo(1), ModTiers.ALLOY, 8F, -2.4F)
                    .addAbility(IWeaponAbility.STUN, 0)
    );
    public static final DeferredItem<Item> ALLOY_PICKAXE = ITEMS.register(
            "alloy_pickaxe",
            () -> new ToolAbilityItem(new Item.Properties().stacksTo(1), ModTiers.ALLOY, 5F, -2.8F)
                    .addAbility(IToolAreaAbility.RECURSION, 0)
    );

    public static final DeferredItem<Item> SCHRABIDIUM_PICKAXE = ITEMS.register(
            "schrabidium_pickaxe",
            () -> new ToolAbilityItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), Tiers.NETHERITE, 20F, -2.8F)
                    .addAbility(IWeaponAbility.RADIATION, 0)
                    .addAbility(IToolAreaAbility.HAMMER, 2)
                    .addAbility(IToolAreaAbility.HAMMER_FLAT, 2)
                    .addAbility(IToolAreaAbility.RECURSION, 5)
                    .addAbility(IToolHarvestAbility.LUCK, 1)
    );

    public static final DeferredItem<Item> ALLOY_HELMET = ITEMS.register(
            "alloy_helmet",
            () -> new ArmorItem(ModArmorMaterials.ALLOY, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<Item> ALLOY_CHESTPLATE = ITEMS.register(
            "alloy_chestplate",
            () -> new ArmorItem(ModArmorMaterials.ALLOY, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<Item> ALLOY_LEGGINGS = ITEMS.register(
            "alloy_leggings",
            () -> new ArmorItem(ModArmorMaterials.ALLOY, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final DeferredItem<Item> ALLOY_BOOTS = ITEMS.register(
            "alloy_boots",
            () -> new ArmorItem(ModArmorMaterials.ALLOY, ArmorItem.Type.BOOTS, new Item.Properties()));

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

    public static final DeferredItem<Item> SAT_RADAR = ITEMS.register(
            "sat_radar",
            () -> new SatChipItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> TEST266 = ITEMS.register(
            "test266",
            () -> new SatelliteInterfaceItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}