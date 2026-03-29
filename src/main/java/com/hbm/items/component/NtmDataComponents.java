package com.hbm.items.component;

import com.hbm.main.NuclearTechMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NtmDataComponents {

    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, NuclearTechMod.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> META = DATA_COMPONENTS.register("meta", () -> DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.INT).build());

    public static void register(IEventBus eventBus) { DATA_COMPONENTS.register(eventBus); }
}
