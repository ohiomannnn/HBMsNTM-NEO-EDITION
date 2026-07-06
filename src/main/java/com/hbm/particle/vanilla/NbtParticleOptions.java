package com.hbm.particle.vanilla;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class NbtParticleOptions implements ParticleOptions {

    private final ParticleType<NbtParticleOptions> type;
    public final CompoundTag tag;

    public NbtParticleOptions(ParticleType<NbtParticleOptions> type, CompoundTag tag) {
        this.type = type;
        this.tag = tag;
    }

    @Override
    public ParticleType<NbtParticleOptions> getType() {
        return this.type;
    }

    public static MapCodec<NbtParticleOptions> codec(ParticleType<NbtParticleOptions> type) {
        return CompoundTag.CODEC.xmap(tag -> new NbtParticleOptions(type, tag), option -> option.tag).fieldOf("tag");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, NbtParticleOptions> streamCodec(ParticleType<NbtParticleOptions> type) {
        return ByteBufCodecs.COMPOUND_TAG.map(tag -> new NbtParticleOptions(type, tag), option -> option.tag);
    }
}
