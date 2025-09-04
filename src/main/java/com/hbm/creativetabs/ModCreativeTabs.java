package com.hbm.creativetabs;

import com.hbm.HBMsNTM;
import com.hbm.block.ModBlocks;
import com.hbm.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(CREATIVE_MODE_TAB, HBMsNTM.MODID);

    public static final Supplier<CreativeModeTab> BLOCK_TAB = CREATIVE_MODE_TABS.register("block_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.BRICK_CONCRETE.get()))
                    .title(Component.translatable("creativetab.hbmsntm.block_tab"))
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

                        output.accept(ModBlocks.TEST_BOMB);
                    }).build());
    public static final Supplier<CreativeModeTab> WEAPON_TAB = CREATIVE_MODE_TABS.register("weapon_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.GRENADE.get()))
                    .title(Component.translatable("creativetab.hbmsntm.weapontab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.GRENADE);
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
