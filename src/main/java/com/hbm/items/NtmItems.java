package com.hbm.items;

import api.hbm.block.IToolable.ToolType;
import com.hbm.blocks.ModBlocks;
import com.hbm.handler.ability.IToolAreaAbility;
import com.hbm.handler.ability.IToolHarvestAbility;
import com.hbm.handler.ability.IWeaponAbility;
import com.hbm.interfaces.Placeholder;
import com.hbm.inventory.ModArmorMaterials;
import com.hbm.inventory.ModTiers;
import com.hbm.inventory.NtmFoods;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.food.ItemEnergy;
import com.hbm.items.machine.*;
import com.hbm.items.special.*;
import com.hbm.items.tools.*;
import com.hbm.items.tools.BombCallerItem.BomberType;
import com.hbm.items.weapon.MissileItem;
import com.hbm.items.weapon.MissileItem.MissileFormFactor;
import com.hbm.items.weapon.MissileItem.MissileFuel;
import com.hbm.items.weapon.MissileItem.MissileTier;
import com.hbm.main.NuclearTechMod;
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

import java.util.List;

import static net.minecraft.world.item.Item.BASE_ATTACK_DAMAGE_ID;
import static net.minecraft.world.item.Item.BASE_ATTACK_SPEED_ID;

public class NtmItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NuclearTechMod.MODID);

    // Ingots
    public static final DeferredItem<Item> INGOT_URANIUM = ITEMS.register("ingot_uranium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_U233 = ITEMS.register("ingot_u233", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_U235 = ITEMS.register("ingot_u235", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_U238 = ITEMS.register("ingot_u238", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_U238M2 = ITEMS.register("ingot_u238m2", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PLUTONIUM = ITEMS.register("ingot_plutonium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PU238 = ITEMS.register("ingot_pu238", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PU239 = ITEMS.register("ingot_pu239", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PU240 = ITEMS.register("ingot_pu240", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PU241 = ITEMS.register("ingot_pu241", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PU_MIX = ITEMS.register("ingot_pu_mix", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_AM241 = ITEMS.register("ingot_am241", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_AM242 = ITEMS.register("ingot_am242", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_AM_MIX = ITEMS.register("ingot_am_mix", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_NEPTUNIUM = ITEMS.register("ingot_neptunium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_POLONIUM = ITEMS.register("ingot_polonium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_TECHNETIUM = ITEMS.register("ingot_technetium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_CO60 = ITEMS.register("ingot_co60", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SR90 = ITEMS.register("ingot_sr90", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_AU198 = ITEMS.register("ingot_au198", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PB209 = ITEMS.register("ingot_pb209", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_RA226 = ITEMS.register("ingot_ra226", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_TITANIUM = ITEMS.register("ingot_titanium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_INDUSTRIAL_COPPER = ITEMS.register("ingot_industrial_copper", () -> new Item(new Item.Properties())); // minecraft already has copper, but its very cheap, we gotta balance that
    public static final DeferredItem<Item> INGOT_RED_COPPER = ITEMS.register("ingot_red_copper", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_ADVANCED_ALLOY = ITEMS.register("ingot_advanced_alloy", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_TUNGSTEN = ITEMS.register("ingot_tungsten", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_ALUMINIUM = ITEMS.register("ingot_aluminium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_STEEL = ITEMS.register("ingot_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_TCALLOY = ITEMS.register("ingot_tcalloy", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_CDALLOY = ITEMS.register("ingot_cdalloy", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BISMUTH_BRONZE = ITEMS.register("ingot_bismuth_bronze", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_ARSENIC_BRONZE = ITEMS.register("ingot_arsenic_bronze", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BSCCO = ITEMS.register("ingot_bscco", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_LEAD = ITEMS.register("ingot_lead", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BISMUTH = ITEMS.register("ingot_bismuth", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_ARSENIC = ITEMS.register("ingot_arsenic", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_CALCIUM = ITEMS.register("ingot_calcium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_CADMIUM = ITEMS.register("ingot_cadmium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_TANTALIUM = ITEMS.register("ingot_tantalium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SILICON = ITEMS.register("ingot_silicon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_NIOBIUM = ITEMS.register("ingot_niobium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BERYLLIUM = ITEMS.register("ingot_beryllium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_COBALT = ITEMS.register("ingot_cobalt", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BORON = ITEMS.register("ingot_boron", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_GRAPHITE = ITEMS.register("ingot_graphite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_FIREBRICK = ITEMS.register("ingot_firebrick", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_DURA_STEEL = ITEMS.register("ingot_dura_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_POLYMER = ITEMS.register("ingot_polymer", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BAKELITE = ITEMS.register("ingot_bakelite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_BIORUBBER = ITEMS.register("ingot_biorubber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_RUBBER = ITEMS.register("ingot_rubber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PC = ITEMS.register("ingot_pc", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PVC = ITEMS.register("ingot_pvc", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_MUD = ITEMS.register("ingot_mud", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_CTF = ITEMS.register("ingot_cft", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SCHRARANIUM = ITEMS.register("ingot_schraranium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SCHRABIDIUM = ITEMS.register("ingot_schrabidium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SCHRABIDATE = ITEMS.register("ingot_schrabidate", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_MAGNETIZED_TUNGSTEN = ITEMS.register("ingot_magnetized_tungsten", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_COMBINE_STEEL = ITEMS.register("ingot_combine_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SOLINIUM = ITEMS.register("ingot_solinium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_GH336 = ITEMS.register("ingot_gh336", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_URANIUM_FUEL = ITEMS.register("ingot_uranium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_THORIUM_FUEL = ITEMS.register("ingot_thorium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_PLUTONIUM_FUEL = ITEMS.register("ingot_plutonium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_NEPTUNIUM_FUEL = ITEMS.register("ingot_neptunium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_MOX_FUEL = ITEMS.register("ingot_mox_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_AMERICIUM_FUEL = ITEMS.register("ingot_americium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SCHRABIDIUM_FUEL = ITEMS.register("ingot_schrabidium_fuel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_HES = ITEMS.register("ingot_hes", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_LES = ITEMS.register("ingot_les", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_AUSTRALIUM = ITEMS.register("ingot_australium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_LANTHANIUM = ITEMS.register("ingot_lanthanium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_ACTINIUM = ITEMS.register("ingot_actinium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_DESH = ITEMS.register("ingot_desh", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_FERROURANIUM = ITEMS.register("ingot_ferrouranium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_STARMETAL = ITEMS.register("ingot_starmetal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_GUNMETAL = ITEMS.register("ingot_gunmetal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_WEAPONSTEEL = ITEMS.register("ingot_weaponsteel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SATURNITE = ITEMS.register("ingot_saturnite", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_EUPHEMIUM = ITEMS.register("ingot_euphemium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_DINEUTRONIUM = ITEMS.register("ingot_dineutronium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_ELECTRONIUM = ITEMS.register("ingot_electronium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INGOT_SMORE = ITEMS.register("ingot_smore", () -> new Item(new Item.Properties().food(NtmFoods.SMORE)));
    public static final DeferredItem<Item> INGOT_OSMIRIDIUM = ITEMS.register("ingot_osmiridium", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DETONATOR = ITEMS.register("detonator", () -> new DetonatorItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR_ITEM = ITEMS.register("detonator_multi", () -> new MultiDetonatorItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR_LASER = ITEMS.register("detonator_laser", () -> new LaserDetonatorItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR_DEADMAN = ITEMS.register("detonator_deadman", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DETONATOR_DE = ITEMS.register("detonator_de", () -> new DangerousDropItem(new Item.Properties().stacksTo(1)));

    @Placeholder(reason = "only for creative tab")
    public static final DeferredItem<Item> PELLET_RTG = ITEMS.register("pellet_rtg", () -> new Item(new Item.Properties()));

    // Breeding Rods
    public static final DeferredItem<Item> ROD_EMPTY = ITEMS.register("rod_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ROD = ITEMS.register("rod", () -> new BreedingRodItem(new Item.Properties()));
    public static final DeferredItem<Item> ROD_DUAL_EMPTY = ITEMS.register("rod_dual_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ROD_DUAL = ITEMS.register("rod_dual", () -> new BreedingRodItem(new Item.Properties()));
    public static final DeferredItem<Item> ROD_QUAD_EMPTY = ITEMS.register("rod_quad_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ROD_QUAD = ITEMS.register("rod_quad", () -> new BreedingRodItem(new Item.Properties()));

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
    public static final DeferredItem<Item> DEFUSER = ITEMS.register("defuser", () -> new ToolingItem(ToolType.DEFUSER, new Item.Properties().durability(100)));

    public static final DeferredItem<Item> GEIGER_COUNTER = ITEMS.register("geiger_counter", () -> new GeigerCounterItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DOSIMETER = ITEMS.register("dosimeter", () -> new DosimeterItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DIGAMMA_DIAGNOSTIC = ITEMS.register("digamma_diagnostic", () -> new DigammaDiagnosticItem(new Item.Properties().stacksTo(1)));

    @Placeholder(reason = "just for compat without textures bruh")
    public static final DeferredItem<Item> SCREWDRIVER = ITEMS.register("screwdriver",
            () -> new Item(new Item.Properties().stacksTo(8)));
    @Placeholder(reason = "ditto")
    public static final DeferredItem<Item> SCREWDRIVER_DESH = ITEMS.register("screwdriver_desh",
            () -> new Item(new Item.Properties().stacksTo(8)));

    public static final DeferredItem<Item> BATTERY_PACK = ITEMS.register("battery_pack", () -> new BatteryPackItem(new Item.Properties()));
    public static final DeferredItem<Item> BATTERY_SC = ITEMS.register("battery_sc", () -> new BatterySCItem(new Item.Properties()));

    public static final DeferredItem<Item> BATTERY_CREATIVE = ITEMS.register("battery_creative", () -> new BatteryCreativeItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> FLUID_TANK_EMPTY = ITEMS.register("fluid_tank_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_TANK_FULL = ITEMS.register("fluid_tank_full", () -> new FluidTankItem(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_TANK_LEAD_EMPTY = ITEMS.register("fluid_tank_lead_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_TANK_LEAD_FULL = ITEMS.register("fluid_tank_lead_full", () -> new FluidTankItem(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_BARREL_EMPTY = ITEMS.register("fluid_barrel_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_BARREL_FULL = ITEMS.register("fluid_barrel_full", () -> new FluidTankItem(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_PACK_EMPTY = ITEMS.register("fluid_pack_empty", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_PACK_FULL = ITEMS.register("fluid_pack_full", () -> new FluidTankItem(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_BARREL_INFINITE = ITEMS.register("fluid_barrel_infinite", () -> new InfiniteFluidItem(new Item.Properties().stacksTo(1), null, 1_000_000_000));
    public static final DeferredItem<Item> INF_WATER = ITEMS.register("inf_water", () -> new InfiniteFluidItem(new Item.Properties().stacksTo(1), Fluids.WATER, 50));
    public static final DeferredItem<Item> INF_WATER_MK2 = ITEMS.register("inf_water_mk2", () -> new InfiniteFluidItem(new Item.Properties().stacksTo(1), Fluids.WATER, 500));

    public static final DeferredItem<Item> CIGARETTE = ITEMS.register("cigarette", () -> new CigaretteItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> CRACKPIPE = ITEMS.register("crackpipe", () -> new CigaretteItem(new Item.Properties().stacksTo(1)));

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

    public static final DeferredItem<Item> PIN = ITEMS.register("pin", () -> new Item(new Item.Properties().stacksTo(8)));
    public static final DeferredItem<Item> KEY = ITEMS.register("key", () -> new KeyItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KEY_RED = ITEMS.register("key_red", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KEY_KIT = ITEMS.register("key_kit", () -> new KeyItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KEY_FAKE = ITEMS.register("key_fake", () -> new KeyItem(new Item.Properties().stacksTo(1)));

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

    public static final DeferredItem<Item> ALLOY_HELMET = ITEMS.register("alloy_helmet",         () -> new ArmorItem(ModArmorMaterials.ALLOY, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> ALLOY_CHESTPLATE = ITEMS.register("alloy_chestplate", () -> new ArmorItem(ModArmorMaterials.ALLOY, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> ALLOY_LEGGINGS = ITEMS.register("alloy_leggings",     () -> new ArmorItem(ModArmorMaterials.ALLOY, ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> ALLOY_BOOTS = ITEMS.register("alloy_boots",           () -> new ArmorItem(ModArmorMaterials.ALLOY, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

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

    public static final DeferredItem<FluidIconItem> FLUID_ICON = ITEMS.register("fluid_icon", () -> new FluidIconItem(new Item.Properties()));

    public static final DeferredItem<Item> POLAROID = ITEMS.register("polaroid", () -> new PolaroidItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BOMB_CALLER_CARPET = ITEMS.register("bomb_caller_carpet", () -> new BombCallerItem(new Item.Properties().stacksTo(1), BomberType.CARPET));
    public static final DeferredItem<Item> BOMB_CALLER_NAPALM = ITEMS.register("bomb_caller_napalm", () -> new BombCallerItem(new Item.Properties().stacksTo(1), BomberType.NAPALM));
    public static final DeferredItem<Item> BOMB_CALLER_ATOMIC = ITEMS.register("bomb_caller_atomic", () -> new BombCallerItem(new Item.Properties().stacksTo(1), BomberType.ATOMIC_BOMB));

    public static final DeferredItem<Item> FLUID_IDENTIFIER_MULTI = ITEMS.register("fluid_identifier_multi", () -> new FluidIDMultiItem(new Item.Properties()));

    public static final DeferredItem<Item> BLOWTORCH = ITEMS.register("blowtorch", () -> new BlowtorchItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> ACETYLENE_TORCH = ITEMS.register("acetylene_torch", () -> new BlowtorchItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> DESIGNATOR = ITEMS.register("designator", () -> new DesignatorItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DESIGNATOR_RANGE = ITEMS.register("designator_range", () -> new DesignatorRangeItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> MISSILE_TAINT =       ITEMS.register("missile_taint",       () -> new MissileItem(MissileFormFactor.MICRO, MissileTier.TIER0));
    public static final DeferredItem<Item> MISSILE_MICRO =       ITEMS.register("missile_micro",       () -> new MissileItem(MissileFormFactor.MICRO, MissileTier.TIER0));
    public static final DeferredItem<Item> MISSILE_BHOLE =       ITEMS.register("missile_bhole",       () -> new MissileItem(MissileFormFactor.MICRO, MissileTier.TIER0));
    public static final DeferredItem<Item> MISSILE_SCHRABIDIUM = ITEMS.register("missile_schrabidium", () -> new MissileItem(MissileFormFactor.MICRO, MissileTier.TIER0));
    public static final DeferredItem<Item> MISSILE_EMP =         ITEMS.register("missile_emp",         () -> new MissileItem(MissileFormFactor.MICRO, MissileTier.TIER0));
    public static final DeferredItem<Item> MISSILE_GENERIC =    ITEMS.register("missile_generic",    () -> new MissileItem(MissileFormFactor.V2, MissileTier.TIER1));
    public static final DeferredItem<Item> MISSILE_INCENDIARY = ITEMS.register("missile_incendiary", () -> new MissileItem(MissileFormFactor.V2, MissileTier.TIER1));
    public static final DeferredItem<Item> MISSILE_CLUSTER =    ITEMS.register("missile_cluster",    () -> new MissileItem(MissileFormFactor.V2, MissileTier.TIER1));
    public static final DeferredItem<Item> MISSILE_BUSTER =     ITEMS.register("missile_buster",     () -> new MissileItem(MissileFormFactor.V2, MissileTier.TIER1));
    public static final DeferredItem<Item> MISSILE_DECOY =      ITEMS.register("missile_decoy",      () -> new MissileItem(MissileFormFactor.V2, MissileTier.TIER1));
    public static final DeferredItem<Item> MISSILE_STRONG =            ITEMS.register("missile_strong",            () -> new MissileItem(MissileFormFactor.STRONG, MissileTier.TIER2));
    public static final DeferredItem<Item> MISSILE_INCENDIARY_STRONG = ITEMS.register("missile_incendiary_strong", () -> new MissileItem(MissileFormFactor.STRONG, MissileTier.TIER2));
    public static final DeferredItem<Item> MISSILE_CLUSTER_STRONG =    ITEMS.register("missile_cluster_strong",    () -> new MissileItem(MissileFormFactor.STRONG, MissileTier.TIER2));
    public static final DeferredItem<Item> MISSILE_BUSTER_STRONG =     ITEMS.register("missile_buster_strong",     () -> new MissileItem(MissileFormFactor.STRONG, MissileTier.TIER2));
    public static final DeferredItem<Item> MISSILE_EMP_STRONG =        ITEMS.register("missile_emp_strong",        () -> new MissileItem(MissileFormFactor.STRONG, MissileTier.TIER2));
    public static final DeferredItem<Item> MISSILE_BURST =   ITEMS.register("missile_burst",   () -> new MissileItem(MissileFormFactor.HUGE, MissileTier.TIER3));
    public static final DeferredItem<Item> MISSILE_INFERNO = ITEMS.register("missile_inferno", () -> new MissileItem(MissileFormFactor.HUGE, MissileTier.TIER3));
    public static final DeferredItem<Item> MISSILE_RAIN =    ITEMS.register("missile_rain",    () -> new MissileItem(MissileFormFactor.HUGE, MissileTier.TIER3));
    public static final DeferredItem<Item> MISSILE_DRILL =   ITEMS.register("missile_drill",   () -> new MissileItem(MissileFormFactor.HUGE, MissileTier.TIER3));
    public static final DeferredItem<Item> MISSILE_NUCLEAR =         ITEMS.register("missile_nuclear",         () -> new MissileItem(MissileFormFactor.ATLAS, MissileTier.TIER4));
    public static final DeferredItem<Item> MISSILE_NUCLEAR_CLUSTER = ITEMS.register("missile_nuclear_cluster", () -> new MissileItem(MissileFormFactor.ATLAS, MissileTier.TIER4));
    public static final DeferredItem<Item> MISSILE_VOLCANO =         ITEMS.register("missile_volcano",         () -> new MissileItem(MissileFormFactor.ATLAS, MissileTier.TIER4));
    public static final DeferredItem<Item> MISSILE_DOOMSDAY =        ITEMS.register("missile_doomsday",        () -> new MissileItem(MissileFormFactor.ATLAS, MissileTier.TIER4));
    public static final DeferredItem<Item> MISSILE_DOOMSDAY_RUSTED = ITEMS.register("missile_doomsday_rusted", () -> new MissileItem(MissileFormFactor.ATLAS, MissileTier.TIER4).notLaunchable());
    public static final DeferredItem<Item> MISSILE_SHUTTLE = ITEMS.register("missile_shuttle", () -> new MissileItem(MissileFormFactor.OTHER, MissileTier.TIER3, MissileFuel.KEROSENE_PEROXIDE));
    public static final DeferredItem<Item> MISSILE_STEALTH = ITEMS.register("missile_stealth", () -> new MissileItem(MissileFormFactor.STRONG, MissileTier.TIER1));

    public static final DeferredItem<Item> SATELLITE_RADAR = ITEMS.register("satellite_radar", () -> new SatChipItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SATELLITE_LASER = ITEMS.register("satellite_laser", () -> new SatChipItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SATELLITE_INTERFACE = ITEMS.register("satellite_interface", () -> new SatelliteInterfaceItem(new Item.Properties().stacksTo(1)));

    // NUKE THINGS
    public static final DeferredItem<Item> BATTERY_SPARK = ITEMS.register("battery_spark", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BATTERY_TRIXITE = ITEMS.register("battery_trixite", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> EARLY_EXPLOSIVE_LENSES = ITEMS.register("early_explosive_lenses", () -> new LoreItem(new Item.Properties()));
    public static final DeferredItem<Item> EXPLOSIVE_LENSES = ITEMS.register("explosive_lenses", () -> new LoreItem(new Item.Properties()));
    public static final DeferredItem<Item> GADGET_WIREING = ITEMS.register("gadget_wireing", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> GADGET_CORE =    ITEMS.register("gadget_core",    () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> LITTLE_BOY_SHIELDING =  ITEMS.register("little_boy_shielding",  () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> LITTLE_BOY_TARGET =     ITEMS.register("little_boy_target",     () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> LITTLE_BOY_BULLET =     ITEMS.register("little_boy_bullet",     () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> LITTLE_BOY_PROPELLANT = ITEMS.register("little_boy_propellant", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> LITTLE_BOY_IGNITER =    ITEMS.register("little_boy_igniter",    () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FAT_MAN_IGNITER = ITEMS.register("fat_man_igniter", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FAT_MAN_CORE =    ITEMS.register("fat_man_core",    () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> IVY_MIKE_CORE =         ITEMS.register("ivy_mike_core",         () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> IVY_MIKE_DEUT =         ITEMS.register("ivy_mike_deut",         () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> IVY_MIKE_COOLING_UNIT = ITEMS.register("ivy_mike_cooling_unit", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> TSAR_BOMBA_CORE = ITEMS.register("tsar_bomba_core", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FLEIJA_IGNITER =    ITEMS.register("fleija_igniter",    () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(ModBlocks.NUKE_FLEIJA.get())));
    public static final DeferredItem<Item> FLEIJA_PROPELLANT = ITEMS.register("fleija_propellant", () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(ModBlocks.NUKE_FLEIJA.get())));
    public static final DeferredItem<Item> FLEIJA_CORE =       ITEMS.register("fleija_core",       () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(ModBlocks.NUKE_FLEIJA.get())));
    public static final DeferredItem<Item> SOLINIUM_IGNITER =    ITEMS.register("solinium_igniter",    () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(ModBlocks.NUKE_FLEIJA.get())));
    public static final DeferredItem<Item> SOLINIUM_PROPELLANT = ITEMS.register("solinium_propellant", () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(ModBlocks.NUKE_FLEIJA.get())));
    public static final DeferredItem<Item> SOLINIUM_CORE =       ITEMS.register("solinium_core",       () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(ModBlocks.NUKE_FLEIJA.get())));
    public static final DeferredItem<Item> N2_CHARGE = ITEMS.register("n2_charge", () -> new UsedInItem(new Item.Properties().stacksTo(1), List.of(ModBlocks.NUKE_N2.get())));
    public static final DeferredItem<Item> EGG_BALEFIRE_SHARD = ITEMS.register("egg_balefire_shard", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> EGG_BALEFIRE = ITEMS.register("egg_balefire", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> NOTHING = ITEMS.register("nothing", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }
}