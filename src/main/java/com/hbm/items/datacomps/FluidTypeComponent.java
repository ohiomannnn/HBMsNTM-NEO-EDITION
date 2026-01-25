package com.hbm.items.datacomps;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record FluidTypeComponent(int fluidId) {

    public static final Codec<FluidTypeComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.INT.fieldOf("fluid_id").forGetter(FluidTypeComponent::fluidId)).apply(instance, FluidTypeComponent::new)
    );

    public static final StreamCodec<ByteBuf, FluidTypeComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FluidTypeComponent::fluidId,
            FluidTypeComponent::new
    );


    public static FluidType getFluidType(ItemStack stack) {
        FluidTypeComponent component = stack.get(ModDataComponents.FLUID_TYPE.get());
        if (component != null) {
            return Fluids.fromID(component.fluidId());
        }
        return Fluids.NONE;
    }
}