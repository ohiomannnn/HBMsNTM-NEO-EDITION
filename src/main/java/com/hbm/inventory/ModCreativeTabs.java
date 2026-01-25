package com.hbm.inventory;

import com.hbm.HBMsNTM;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ModItems;
import com.hbm.items.machine.BatteryPackItem;
import com.hbm.items.machine.FluidIDMultiItem;
import com.hbm.items.machine.FluidIconItem;
import com.hbm.items.machine.FluidTankItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(CREATIVE_MODE_TAB, HBMsNTM.MODID);

    public static final Supplier<CreativeModeTab> MACHINE_ITEMS_AND_FUEL = CREATIVE_MODE_TABS.register(
            "machine_items_and_fuel",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.PELLET_RTG.get()))
                    .title(Component.translatable("creative_tab.hbmsntm.machine_items_and_fuel"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.PARTICLE_DIGAMMA);
                        output.accept(ModItems.PARTICLE_LUTECE);

                        output.accept(ModItems.CELL_ANTIMATTER);

                        output.accept(ModItems.SINGULARITY);
                        output.accept(ModItems.SINGULARITY_COUNTER_RESONANT);
                        output.accept(ModItems.SINGULARITY_SUPER_HEATED);
                        output.accept(ModItems.BLACK_HOLE);
                        output.accept(ModItems.SINGULARITY_SPARK);
                        output.accept(ModItems.PELLET_ANTIMATTER);

                        FluidType[] types = Fluids.getInNiceOrder();
                        // tanks
                        output.accept(ModItems.FLUID_TANK_EMPTY.get());
                        for (int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];

                            if (type.hasNoContainer()) continue;
                            if (type.needsLeadContainer()) continue;
                            output.accept(FluidTankItem.createStack(ModItems.FLUID_TANK_FULL.get(), type));
                        }
                        // lead tanks
                        output.accept(ModItems.FLUID_TANK_LEAD_EMPTY.get());
                        for (int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];

                            if (type.hasNoContainer()) continue;
                            output.accept(FluidTankItem.createStack(ModItems.FLUID_TANK_LEAD_FULL.get(), type));
                        }
                        // barrels
                        output.accept(ModItems.FLUID_BARREL_EMPTY.get());
                        for (int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];

                            if (type.hasNoContainer()) continue;
                            if (type.needsLeadContainer()) continue;
                            output.accept(FluidTankItem.createStack(ModItems.FLUID_BARREL_FULL.get(), type));
                        }
                        // fluid packs
                        output.accept(ModItems.FLUID_PACK_EMPTY.get());
                        for (int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];

                            if (type.hasNoContainer()) continue;
                            if (type.needsLeadContainer()) continue;
                            output.accept(FluidTankItem.createStack(ModItems.FLUID_PACK_FULL.get(), type));
                        }

                        for (int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];

                            output.accept(FluidIconItem.make(type, 1000));
                        }

                        output.accept(BatteryPackItem.makeFullBattery(new ItemStack(ModItems.BATTERY_PACK_REDSTONE.get())));
                        output.accept(BatteryPackItem.makeEmptyBattery(new ItemStack(ModItems.BATTERY_PACK_REDSTONE.get())));

                        output.accept(BatteryPackItem.makeFullBattery(new ItemStack(ModItems.BATTERY_PACK_LEAD.get())));
                        output.accept(BatteryPackItem.makeEmptyBattery(new ItemStack(ModItems.BATTERY_PACK_LEAD.get())));

                        output.accept(BatteryPackItem.makeFullBattery(new ItemStack(ModItems.BATTERY_PACK_LITHIUM.get())));
                        output.accept(BatteryPackItem.makeEmptyBattery(new ItemStack(ModItems.BATTERY_PACK_LITHIUM.get())));

                        output.accept(BatteryPackItem.makeFullBattery(new ItemStack(ModItems.BATTERY_PACK_SODIUM.get())));
                        output.accept(BatteryPackItem.makeEmptyBattery(new ItemStack(ModItems.BATTERY_PACK_SODIUM.get())));

                        output.accept(BatteryPackItem.makeFullBattery(new ItemStack(ModItems.BATTERY_PACK_SCHRABIDIUM.get())));
                        output.accept(BatteryPackItem.makeEmptyBattery(new ItemStack(ModItems.BATTERY_PACK_SCHRABIDIUM.get())));

                        output.accept(BatteryPackItem.makeFullBattery(new ItemStack(ModItems.BATTERY_PACK_QUANTUM.get())));
                        output.accept(BatteryPackItem.makeEmptyBattery(new ItemStack(ModItems.BATTERY_PACK_QUANTUM.get())));
                        output.accept(ModItems.BATTERY_CREATIVE);

                        output.accept(ModItems.REACHER);
                    }).build());

    public static final Supplier<CreativeModeTab> ORES_AND_BLOCKS = CREATIVE_MODE_TABS.register(
            "ores_and_blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.ORE_URANIUM.get()))
                    .title(Component.translatable("creative_tab.hbmsntm.ores_and_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.BRICK_CONCRETE);
                        output.accept(ModBlocks.BRICK_CONCRETE_MOSSY);
                        output.accept(ModBlocks.BRICK_CONCRETE_CRACKED);
                        output.accept(ModBlocks.BRICK_CONCRETE_BROKEN);
                        output.accept(ModBlocks.BRICK_CONCRETE_MARKED);

                        output.accept(ModBlocks.BRICK_CONCRETE_STAIRS);
                        output.accept(ModBlocks.BRICK_CONCRETE_MOSSY_STAIRS);
                        output.accept(ModBlocks.BRICK_CONCRETE_CRACKED_STAIRS);
                        output.accept(ModBlocks.BRICK_CONCRETE_BROKEN_STAIRS);

                        output.accept(ModBlocks.BRICK_CONCRETE_SLAB);
                        output.accept(ModBlocks.BRICK_CONCRETE_MOSSY_SLAB);
                        output.accept(ModBlocks.BRICK_CONCRETE_CRACKED_SLAB);
                        output.accept(ModBlocks.BRICK_CONCRETE_BROKEN_SLAB);

                        output.accept(ModBlocks.WASTE_EARTH);
                        output.accept(ModBlocks.WASTE_LOG);
                        output.accept(ModBlocks.WASTE_LEAVES);
                        output.accept(ModBlocks.LEAVES_LAYER);

                        output.accept(ModBlocks.SELLAFIELD_SLAKED);
                        output.accept(ModBlocks.SELLAFIELD_BEDROCK);
                        output.accept(ModBlocks.ORE_SELLAFIELD_DIAMOND);
                        output.accept(ModBlocks.ORE_SELLAFIELD_EMERALD);
                    }).build());

    public static final Supplier<CreativeModeTab> MACHINES = CREATIVE_MODE_TABS.register(
            "machines",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.PWR_CONTROLLER.get()))
                    .withTabsBefore(HBMsNTM.withDefaultNamespaceNT("ores_and_blocks"))
                    .title(Component.translatable("creative_tab.hbmsntm.machines"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.GAS_RADON);
                        output.accept(ModBlocks.GAS_RADON_DENSE);
                        output.accept(ModBlocks.GAS_RADON_TOMB);
                        output.accept(ModBlocks.GAS_MELTDOWN);
                        output.accept(ModBlocks.GAS_MONOXIDE);
                        output.accept(ModBlocks.GAS_ASBESTOS);
                        output.accept(ModBlocks.GAS_COAL);
                        output.accept(ModBlocks.GAS_FLAMMABLE);
                        output.accept(ModBlocks.GAS_EXPLOSIVE);

                        output.accept(ModBlocks.GEIGER);
                        output.accept(ModBlocks.MACHINE_BATTERY_SOCKET);
                        output.accept(ModBlocks.MACHINE_BATTERY_REDD);
                        output.accept(ModBlocks.CABLE);

                        output.accept(ModBlocks.DECONTAMINATOR);
                        output.accept(ModBlocks.MACHINE_SATLINKER);

                        FluidType[] types = Fluids.getInNiceOrder();
                        // multi identifiers
                        for (int i = 1; i < types.length; ++i) {
                            FluidType type = types[i];

                            output.accept(FluidIDMultiItem.createStack(type));
                        }
                    }).build());

    public static final Supplier<CreativeModeTab> BOMBS = CREATIVE_MODE_TABS.register(
            "bombs",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.NUKE_FAT_MAN.get()))
                    .withTabsBefore(HBMsNTM.withDefaultNamespaceNT("machines"))
                    .title(Component.translatable("creative_tab.hbmsntm.bombs"))
                    .backgroundTexture(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/gui/nuke_tab.png"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.NUKE_LITTLE_BOY);
                        output.accept(ModBlocks.NUKE_FAT_MAN);

                        output.accept(ModBlocks.CRASHED_BOMB_BALEFIRE);
                        output.accept(ModBlocks.CRASHED_BOMB_CONVENTIONAL);
                        output.accept(ModBlocks.CRASHED_BOMB_NUKE);
                        output.accept(ModBlocks.CRASHED_BOMB_SALTED);

                        output.accept(ModBlocks.MINE_AP);
                        output.accept(ModBlocks.MINE_SHRAP);
                        output.accept(ModBlocks.MINE_HE);
                        output.accept(ModBlocks.MINE_FAT);
                        output.accept(ModBlocks.MINE_NAVAL);

                        output.accept(ModBlocks.DET_CORD);
                        output.accept(ModBlocks.DET_CHARGE);
                        output.accept(ModBlocks.DET_NUKE);
                        output.accept(ModBlocks.DET_MINER);

                        output.accept(ModItems.LITTLE_BOY_SHIELDING);
                        output.accept(ModItems.LITTLE_BOY_TARGET);
                        output.accept(ModItems.LITTLE_BOY_BULLET);
                        output.accept(ModItems.LITTLE_BOY_PROPELLANT);
                        output.accept(ModItems.LITTLE_BOY_IGNITER);

                        output.accept(ModItems.EARLY_EXPLOSIVE_LENSES);
                        output.accept(ModItems.FAT_MAN_CORE);
                        output.accept(ModItems.FAT_MAN_IGNITER);

                        output.accept(ModItems.DETONATOR);
                        output.accept(ModItems.MULTI_DETONATOR);
                        output.accept(ModItems.DETONATOR_DEADMAN);
                        output.accept(ModItems.DETONATOR_DE);
                    }).build());

    public static final Supplier<CreativeModeTab> MISSILES = CREATIVE_MODE_TABS.register(
            "missiles",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SATELLITE_LASER.get()))
                    .withTabsBefore(HBMsNTM.withDefaultNamespaceNT("bombs"))
                    .title(Component.translatable("creative_tab.hbmsntm.missiles"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.SATELLITE_RADAR);
                        output.accept(ModItems.SATELLITE_LASER);
                        output.accept(ModItems.SATELLITE_INTERFACE);
                    }).build());

    public static final Supplier<CreativeModeTab> CONSUMABLES_AND_GEAR = CREATIVE_MODE_TABS.register(
            "consumables_and_gear",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BOTTLE_NUKA.get()))
                    .withTabsBefore(HBMsNTM.withDefaultNamespaceNT("missiles"))
                    .title(Component.translatable("creative_tab.hbmsntm.consumables_and_gear"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.GEIGER_COUNTER);
                        output.accept(ModItems.DOSIMETER);
                        output.accept(ModItems.DIGAMMA_DIAGNOSTIC);
                        output.accept(ModItems.KEY);
                        output.accept(ModItems.KEY_KIT);
                        output.accept(ModItems.KEY_FAKE);
                        output.accept(ModItems.PIN);
                        output.accept(ModItems.FLINT_AND_BALEFIRE);
                        output.accept(ModItems.POLAROID);

                        output.accept(ModItems.BOTTLE_EMPTY);
                        output.accept(ModItems.BOTTLE_OPENER);
                        output.accept(ModItems.CAP_NUKA);
                        output.accept(ModItems.BOTTLE_NUKA);
                        output.accept(ModItems.BOTTLE_CHERRY);
                        output.accept(ModItems.CAP_QUANTUM);
                        output.accept(ModItems.BOTTLE_QUANTUM);
                        output.accept(ModItems.CAP_SPARKLE);
                        output.accept(ModItems.BOTTLE_SPARKLE);

                        output.accept(ModItems.SCHRABIDIUM_PICKAXE);

                        output.accept(ModItems.BOMB_CALLER_CARPET);
                        output.accept(ModItems.BOMB_CALLER_NAPALM);
                        output.accept(ModItems.BOMB_CALLER_ATOMIC);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
