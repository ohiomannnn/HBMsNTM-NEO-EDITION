package com.hbm.fluids;

import com.hbm.main.NuclearTechMod;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
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
                    .viscosity(3000)
                    .temperature(1300)
                    .motionScale(0.0023333333333333335D)
            ) {

                @Override
                public boolean move(FluidState state, LivingEntity entity, Vec3 travelVector, double d0) {
                    boolean flag = entity.getDeltaMovement().y <= 0.0;
                    double d8 = entity.getY();
                    entity.moveRelative(0.02F, travelVector);
                    entity.move(MoverType.SELF, entity.getDeltaMovement());
                    if(entity.getFluidHeight(FluidTags.LAVA) <= entity.getFluidJumpThreshold()) {
                        entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 0.8F, 0.5));
                        Vec3 vec33 = entity.getFluidFallingAdjustedMovement(d0, flag, entity.getDeltaMovement());
                        entity.setDeltaMovement(vec33);
                    } else {
                        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5));
                    }

                    if(d0 != 0.0) {
                        entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, -d0 / 4.0, 0.0));
                    }

                    Vec3 vec34 = entity.getDeltaMovement();
                    if(entity.horizontalCollision && entity.isFree(vec34.x, vec34.y + 0.6F - entity.getY() + d8, vec34.z)) {
                        entity.setDeltaMovement(vec34.x, 0.3F, vec34.z);
                    }

                    return true;
                }
            }
    );

    public static final DeferredHolder<FluidType, FluidType> RAD_LAVA_TYPE = FLUID_TYPES.register(
            "rad_lava_fluid",
            () -> new FluidType(FluidType.Properties.create()
                    .canSwim(false)
                    .canDrown(false)
                    .pathType(PathType.LAVA)
                    .adjacentPathType(null)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                    .lightLevel(15)
                    .density(3000)
                    .viscosity(3000)
                    .temperature(1300)
                    .motionScale(0.0023333333333333335D)
            ) {

                @Override
                public boolean move(FluidState state, LivingEntity entity, Vec3 travelVector, double d0) {
                    boolean flag = entity.getDeltaMovement().y <= 0.0;
                    double d8 = entity.getY();
                    entity.moveRelative(0.02F, travelVector);
                    entity.move(MoverType.SELF, entity.getDeltaMovement());
                    if(entity.getFluidHeight(FluidTags.LAVA) <= entity.getFluidJumpThreshold()) {
                        entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 0.8F, 0.5));
                        Vec3 vec33 = entity.getFluidFallingAdjustedMovement(d0, flag, entity.getDeltaMovement());
                        entity.setDeltaMovement(vec33);
                    } else {
                        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5));
                    }

                    if(d0 != 0.0) {
                        entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, -d0 / 4.0, 0.0));
                    }

                    Vec3 vec34 = entity.getDeltaMovement();
                    if(entity.horizontalCollision && entity.isFree(vec34.x, vec34.y + 0.6F - entity.getY() + d8, vec34.z)) {
                        entity.setDeltaMovement(vec34.x, 0.3F, vec34.z);
                    }

                    return true;
                }
            }
    );

    public static void register(IEventBus eventBus) { FLUID_TYPES.register(eventBus); }
}
