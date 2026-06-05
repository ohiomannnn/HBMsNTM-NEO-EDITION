package com.hbm.fluids;

import com.hbm.blocks.NtmBlocks;
import com.hbm.main.NuclearTechMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NtmFluids {
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, NuclearTechMod.MODID);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> VOLCANIC_LAVA = FLUIDS.register("volcanic_lava", () -> new BaseFlowingFluid.Source(volcanicLavaProps()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> VOLCANIC_LAVA_FLOWING = FLUIDS.register("volcanic_lava_flowing", () -> new BaseFlowingFluid.Flowing(volcanicLavaProps()));

    private static BaseFlowingFluid.Properties volcanicLavaProps() {
        return new BaseFlowingFluid.Properties(NtmFluidTypes.VOLCANIC_LAVA_TYPE, NtmFluids.VOLCANIC_LAVA, NtmFluids.VOLCANIC_LAVA_FLOWING)
                .bucket(() -> Items.LAVA_BUCKET)
                .block(NtmBlocks.VOLCANIC_LAVA)
                .levelDecreasePerBlock(2)
                .explosionResistance(500F)
                .tickRate(30);
    }

    public static void register(IEventBus eventBus) { FLUIDS.register(eventBus); }
}
