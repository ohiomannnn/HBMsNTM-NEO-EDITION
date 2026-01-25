package com.hbm.items.datacomps;

import com.hbm.HBMsNTM;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
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

    public static void register(IEventBus eventBus) { DATA_COMPONENTS.register(eventBus); }
}
