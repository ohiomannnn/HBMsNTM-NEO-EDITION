package com.hbm.inventory;

import com.hbm.NuclearTechMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MetaHelper {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, NuclearTechMod.MODID);
    public static void register(IEventBus eventBus) { DATA_COMPONENTS.register(eventBus); }

    private static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> META = DATA_COMPONENTS.register("meta", () -> DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.INT).build());

    public static int getMeta(ItemStack stack) { return stack.getOrDefault(META.get(), 0); }
    public static void setMeta(ItemStack stack, int meta) { stack.set(META.get(), meta); }

    public static ItemStack metaStack(ItemStack stack, int meta) { stack.set(META.get(), meta); return stack; }
}
