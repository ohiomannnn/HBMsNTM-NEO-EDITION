package com.hbm.packets.toserver;

import com.hbm.HBMsNTM;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record GetRadPacket(UUID targetId) implements CustomPacketPayload {

    public static final Type<GetRadPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "get_rad"));

    public static final StreamCodec<FriendlyByteBuf, GetRadPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public GetRadPacket decode(FriendlyByteBuf buf) {
                    return new GetRadPacket(buf.readUUID());
                }

                @Override
                public void encode(FriendlyByteBuf buf, GetRadPacket packet) {
                    buf.writeUUID(packet.targetId);
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
