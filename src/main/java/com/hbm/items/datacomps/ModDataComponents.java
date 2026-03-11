package com.hbm.items.datacomps;

import com.hbm.HBMsNTM;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, HBMsNTM.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FluidTypeComponent>> FLUID_TYPE = DATA_COMPONENTS.register(
            "fluid_type",
            () -> DataComponentType.<FluidTypeComponent>builder()
                    .persistent(FluidTypeComponent.CODEC)
                    .networkSynchronized(FluidTypeComponent.STREAM_CODEC)
                    .build()
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> META = DATA_COMPONENTS.register("meta", () -> DataComponentType.<Integer>builder().persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.INT).build());

    public static void register(IEventBus eventBus) { DATA_COMPONENTS.register(eventBus); }
}
