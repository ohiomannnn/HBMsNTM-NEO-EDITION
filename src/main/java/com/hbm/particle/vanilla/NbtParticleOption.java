package com.hbm.particle.vanilla;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class NbtParticleOption implements ParticleOptions {

    private final ParticleType<NbtParticleOption> type;
    public final CompoundTag tag;

    public NbtParticleOption(ParticleType<NbtParticleOption> type, CompoundTag tag) {
        this.type = type;
        this.tag = tag;
    }

    @Override
    public ParticleType<NbtParticleOption> getType() {
        return this.type;
    }

    public static MapCodec<NbtParticleOption> codec(ParticleType<NbtParticleOption> type) {
        return CompoundTag.CODEC.xmap(tag -> new NbtParticleOption(type, tag), option -> option.tag).fieldOf("tag");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, NbtParticleOption> streamCodec(ParticleType<NbtParticleOption> type) {
        return ByteBufCodecs.COMPOUND_TAG.map(tag -> new NbtParticleOption(type, tag), option -> option.tag);
    }
}
