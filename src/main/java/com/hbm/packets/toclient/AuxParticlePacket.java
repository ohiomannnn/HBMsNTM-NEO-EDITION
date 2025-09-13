package com.hbm.packets.toclient;

import com.hbm.HBMsNTM;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AuxParticlePacket(CompoundTag nbt, double x, double y, double z) implements CustomPacketPayload {

    public static final Type<AuxParticlePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "aux_particle"));


    public static final StreamCodec<FriendlyByteBuf, AuxParticlePacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public AuxParticlePacket decode(FriendlyByteBuf buf) {
                    return new AuxParticlePacket(
                            buf.readNbt(),
                            buf.readDouble(),
                            buf.readDouble(),
                            buf.readDouble()
                    );
                }

                @Override
                public void encode(FriendlyByteBuf buf, AuxParticlePacket packet) {
                    buf.writeNbt(packet.nbt);
                    buf.writeDouble(packet.x);
                    buf.writeDouble(packet.y);
                    buf.writeDouble(packet.z);
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
