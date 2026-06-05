package com.hbm.fluids;

import com.hbm.main.NuclearTechMod;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NtmFluidTypes {
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, NuclearTechMod.MODID);

    public static final DeferredHolder<FluidType, FluidType> VOLCANIC_LAVA_TYPE = FLUID_TYPES.register(
            "volcanic_lava_fluid",
            () -> new FluidType(FluidType.Properties.create()
                    .canSwim(false)
                    .canDrown(false)
                    .pathType(PathType.LAVA)
                    .adjacentPathType(null)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                    .lightLevel(15)
                    .density(3000)
                    .viscosity(6000)
                    .temperature(1300)
                    .motionScale(0.0023333333333333335D)
            )
    );

    public static void register(IEventBus eventBus) { FLUID_TYPES.register(eventBus); }
}
