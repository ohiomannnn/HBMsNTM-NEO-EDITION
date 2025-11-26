package com.hbm.inventory;

import com.hbm.HBMsNTM;
import com.hbm.blocks.ModBlocks;
import com.hbm.items.ModItems;
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
                    }).build());

    public static final Supplier<CreativeModeTab> BOMBS = CREATIVE_MODE_TABS.register(
            "bombs",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.NUKE_FATMAN.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "ores_and_blocks"))
                    .title(Component.translatable("creative_tab.hbmsntm.bombs"))
                    .backgroundTexture(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/gui/nuke_tab.png"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.MINE_AP);
                        output.accept(ModBlocks.MINE_SHRAP);
                        output.accept(ModBlocks.MINE_HE);
                        output.accept(ModBlocks.MINE_FAT);
                        output.accept(ModBlocks.MINE_NAVAL);
                        output.accept(ModBlocks.CRASHED_BOMB_BALEFIRE);
                        output.accept(ModBlocks.CRASHED_BOMB_CONVENTIONAL);
                        output.accept(ModBlocks.CRASHED_BOMB_NUKE);
                        output.accept(ModBlocks.CRASHED_BOMB_SALTED);
                        output.accept(ModBlocks.DET_NUKE);
                        output.accept(ModBlocks.DET_CHARGE);
                        output.accept(ModBlocks.DET_MINER);
                        output.accept(ModItems.DETONATOR);
                        output.accept(ModItems.MULTI_DETONATOR);
                        output.accept(ModItems.DETONATOR_DEADMAN);
                        output.accept(ModItems.DETONATOR_DE);
                    }).build());

    public static final Supplier<CreativeModeTab> CONSUMABLES_AND_GEAR = CREATIVE_MODE_TABS.register(
            "consumables_and_gear",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BOTTLE_NUKA.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "bombs"))
                    .title(Component.translatable("creative_tab.hbmsntm.consumables_and_gear"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.GEIGER_COUNTER);
                        output.accept(ModItems.DOSIMETER);
                        output.accept(ModItems.REACHER);
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

                        output.accept(ModItems.ALLOY_SWORD);
                        output.accept(ModItems.SCHRABIDIUM_PICKAXE);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
