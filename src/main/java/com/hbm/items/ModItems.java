package com.hbm.items;

import com.hbm.HBMsNTM;
import com.hbm.handler.ability.IToolAreaAbility;
import com.hbm.handler.ability.IToolHarvestAbility;
import com.hbm.handler.ability.IWeaponAbility;
import com.hbm.interfaces.Placeholder;
import com.hbm.inventory.ModArmorMaterials;
import com.hbm.inventory.ModTiers;
import com.hbm.items.food.ItemEnergy;
import com.hbm.items.machine.*;
import com.hbm.items.machine.BatteryPackItem.BatteryPackType;
import com.hbm.items.special.DangerousDropItem;
import com.hbm.items.special.EntitySpawnerItem;
import com.hbm.items.special.PolaroidItem;
import com.hbm.items.tools.*;
import com.hbm.items.tools.BombCallerItem.BomberType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.minecraft.world.item.Item.BASE_ATTACK_DAMAGE_ID;
import static net.minecraft.world.item.Item.BASE_ATTACK_SPEED_ID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HBMsNTM.MODID);

    public static final DeferredItem<Item> NOTHING = ITEMS.register("nothing", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DETONATOR = ITEMS.register("detonator", () -> new DetonatorItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MULTI_DETONATOR = ITEMS.register("multi_detonator", () -> new MultiDetonatorItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR_DEADMAN = ITEMS.register("detonator_deadman", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR_DE = ITEMS.register("detonator_de", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));

    @Placeholder(reason = "only for creative tab")
    public static final DeferredItem<Item> PELLET_RTG = ITEMS.register("pellet_rtg", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PARTICLE_DIGAMMA = ITEMS.register(
            "particle_digamma",
            () -> new DangerousDropItem(new Item.Properties())
    );
    public static final DeferredItem<Item> PARTICLE_LUTECE = ITEMS.register(
            "particle_lutece",
            () -> new Item(new Item.Properties())
    );

    public static final DeferredItem<Item> CELL_ANTIMATTER = ITEMS.register("cell_antimatter", () -> new DangerousDropItem(new Item.Properties()));
    public static final DeferredItem<Item> PELLET_ANTIMATTER = ITEMS.register("pellet_antimatter", () -> new DangerousDropItem(new Item.Properties()));
    public static final DeferredItem<Item> SINGULARITY = ITEMS.register("singularity", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SINGULARITY_COUNTER_RESONANT = ITEMS.register("singularity_counter_resonant", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SINGULARITY_SUPER_HEATED = ITEMS.register("singularity_super_heated", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BLACK_HOLE = ITEMS.register("black_hole", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SINGULARITY_SPARK = ITEMS.register("singularity_spark", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> REACHER = ITEMS.register("reacher", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> GEIGER_COUNTER = ITEMS.register("geiger_counter", () -> new GeigerCounterItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DOSIMETER = ITEMS.register("dosimeter", () -> new DosimeterItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DIGAMMA_DIAGNOSTIC = ITEMS.register("digamma_diagnostic", () -> new DigammaDiagnosticItem(new Item.Properties().stacksTo(1)));

    @Placeholder(reason = "just for compat without textures bruh")
    public static final DeferredItem<Item> SCREWDRIVER = ITEMS.register("screwdriver",
            () -> new Item(new Item.Properties().stacksTo(8)));
    @Placeholder(reason = "ditto")
    public static final DeferredItem<Item> SCREWDRIVER_DESH = ITEMS.register("screwdriver_desh",
            () -> new Item(new Item.Properties().stacksTo(8)));

    public static final DeferredItem<Item> BATTERY_PACK_REDSTONE = ITEMS.register("battery_pack_redstone",       () -> new BatteryPackItem(new Item.Properties().stacksTo(1), BatteryPackType.BATTERY_REDSTONE));
    public static final DeferredItem<Item> BATTERY_PACK_LEAD = ITEMS.register("battery_pack_lead",               () -> new BatteryPackItem(new Item.Properties().stacksTo(1), BatteryPackType.BATTERY_LEAD));
    public static final DeferredItem<Item> BATTERY_PACK_LITHIUM = ITEMS.register("battery_pack_lithium",         () -> new BatteryPackItem(new Item.Properties().stacksTo(1), BatteryPackType.BATTERY_LITHIUM));
    public static final DeferredItem<Item> BATTERY_PACK_SODIUM = ITEMS.register("battery_pack_sodium",           () -> new BatteryPackItem(new Item.Properties().stacksTo(1), BatteryPackType.BATTERY_SODIUM));
    public static final DeferredItem<Item> BATTERY_PACK_SCHRABIDIUM = ITEMS.register("battery_pack_schrabidium", () -> new BatteryPackItem(new Item.Properties().stacksTo(1), BatteryPackType.BATTERY_SCHRABIDIUM));
    public static final DeferredItem<Item> BATTERY_PACK_QUANTUM = ITEMS.register("battery_pack_quantum",         () -> new BatteryPackItem(new Item.Properties().stacksTo(1), BatteryPackType.BATTERY_QUANTUM));

    public static final DeferredItem<Item> BATTERY_CREATIVE = ITEMS.register("battery_creative", () -> new BatteryCreativeItem(new Item.Properties().stacksTo(1)));

    @Placeholder(reason = "uhh yes?")
    public static final DeferredItem<Item> CAPACITOR_PACK_NIOBIUM = ITEMS.register(
            "capacitor_pack_niobium",
            () -> new BatteryPackItem(new Item.Properties().stacksTo(1), BatteryPackType.CAPACITOR_NIOBIUM));

    public static final DeferredItem<Item> FLUID_BARREL_CREATIVE = ITEMS.register("fluid_barrel_creative", () -> new Item(new Item.Properties().stacksTo(1)));

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
            () -> new ToolAbilityItem(new Item.Properties().stacksTo(1)
                    .component(DataComponents.TOOL, ModTiers.ALLOY.createToolProperties(BlockTags.MINEABLE_WITH_PICKAXE))
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (5F + ModTiers.ALLOY.getAttackDamageBonus()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                            .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.8F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                            .build()
                    ), ModTiers.ALLOY)
                    .addAbility(IToolAreaAbility.RECURSION, 0)
    );

    public static final DeferredItem<Item> SCHRABIDIUM_PICKAXE = ITEMS.register(
            "schrabidium_pickaxe",
            () -> new ToolAbilityItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)
                    .component(DataComponents.TOOL, ModTiers.ALLOY.createToolProperties(BlockTags.MINEABLE_WITH_PICKAXE))
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (5F + Tiers.NETHERITE.getAttackDamageBonus()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                            .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.8F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                            .build()
                    ), Tiers.NETHERITE)
                    .addAbility(IWeaponAbility.RADIATION, 0)
                    .addAbility(IToolAreaAbility.HAMMER, 2)
                    .addAbility(IToolAreaAbility.HAMMER_FLAT, 2)
                    .addAbility(IToolAreaAbility.RECURSION, 5)
                    .addAbility(IToolHarvestAbility.LUCK, 1)
    );

    public static final DeferredItem<Item> ALLOY_HELMET = ITEMS.register("alloy_helmet",         () -> new ArmorItem(ModArmorMaterials.ALLOY, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<Item> ALLOY_CHESTPLATE = ITEMS.register("alloy_chestplate", () -> new ArmorItem(ModArmorMaterials.ALLOY, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<Item> ALLOY_LEGGINGS = ITEMS.register("alloy_leggings",     () -> new ArmorItem(ModArmorMaterials.ALLOY, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final DeferredItem<Item> ALLOY_BOOTS = ITEMS.register("alloy_boots",           () -> new ArmorItem(ModArmorMaterials.ALLOY, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final DeferredItem<Item> DUCK_SPAWN_EGG = ITEMS.register(
            "duck_spawn_egg",
            () -> new EntitySpawnerItem(new Item.Properties().stacksTo(16)));
    @Placeholder(reason = "not used, isnt this removed now?")
    public static final DeferredItem<Item> BURNT_BARK = ITEMS.register(
            "burnt_bark",
            () -> new EntitySpawnerItem(new Item.Properties()));
    public static final DeferredItem<Item> FLINT_AND_BALEFIRE = ITEMS.register(
            "balefire_and_steel",
            () -> new BalefireAndSteelITem(new Item.Properties()
                    .stacksTo(1)
                    .durability(256)));

    @Placeholder(reason = "working but mehh")
    public static final DeferredItem<FluidIconItem> FLUID_ICON = ITEMS.register(
            "fluid_icon",
            () -> new FluidIconItem(new Item.Properties())
    );

    public static final DeferredItem<Item> POLAROID = ITEMS.register("polaroid", () -> new PolaroidItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BOMB_CALLER_CARPET = ITEMS.register("bomb_caller_carpet", () -> new BombCallerItem(new Item.Properties().stacksTo(1), BomberType.CARPET));
    public static final DeferredItem<Item> BOMB_CALLER_NAPALM = ITEMS.register("bomb_caller_napalm", () -> new BombCallerItem(new Item.Properties().stacksTo(1), BomberType.NAPALM));
    public static final DeferredItem<Item> BOMB_CALLER_ATOMIC = ITEMS.register("bomb_caller_atomic", () -> new BombCallerItem(new Item.Properties().stacksTo(1), BomberType.ATOMIC_BOMB));

    public static final DeferredItem<Item> FLUID_IDENTIFIER_MULTI = ITEMS.register("fluid_identifier_multi", () -> new FluidIDMultiItem(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_TANK_EMPTY = ITEMS.register("fluid_tank_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_TANK_FULL = ITEMS.register("fluid_tank_full", () -> new FluidTankItem(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_TANK_LEAD_EMPTY = ITEMS.register("fluid_tank_lead_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_TANK_LEAD_FULL = ITEMS.register("fluid_tank_lead_full", () -> new FluidTankItem(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_BARREL_EMPTY = ITEMS.register("fluid_barrel_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_BARREL_FULL = ITEMS.register("fluid_barrel_full", () -> new FluidTankItem(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_PACK_EMPTY = ITEMS.register("fluid_pack_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_PACK_FULL = ITEMS.register("fluid_pack_full", () -> new FluidTankItem(new Item.Properties()));

    public static final DeferredItem<Item> BLOWTORCH = ITEMS.register("blowtorch", () -> new BlowtorchItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> ACETYLENE_TORCH = ITEMS.register("acetylene_torch", () -> new BlowtorchItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SATELLITE_RADAR = ITEMS.register("satellite_radar", () -> new SatChipItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SATELLITE_LASER = ITEMS.register("satellite_laser", () -> new SatChipItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SATELLITE_INTERFACE = ITEMS.register("satellite_interface", () -> new SatelliteInterfaceItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> EARLY_EXPLOSIVE_LENSES = ITEMS.register("early_explosive_lenses", () -> new LoreItem(new Item.Properties()));

    public static final DeferredItem<Item> LITTLE_BOY_SHIELDING = ITEMS.register("little_boy_shielding", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> LITTLE_BOY_TARGET = ITEMS.register("little_boy_target", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> LITTLE_BOY_BULLET = ITEMS.register("little_boy_bullet", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> LITTLE_BOY_PROPELLANT = ITEMS.register("little_boy_propellant", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> LITTLE_BOY_IGNITER = ITEMS.register("little_boy_igniter", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> FAT_MAN_IGNITER = ITEMS.register("fat_man_igniter", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FAT_MAN_CORE = ITEMS.register("fat_man_core", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }
}