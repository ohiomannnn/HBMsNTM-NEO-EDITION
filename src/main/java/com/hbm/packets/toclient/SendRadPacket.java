package com.hbm.packets.toclient;

import com.hbm.HBMsNTM;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SendRadPacket(float rad) implements CustomPacketPayload {
    public static final Type<SendRadPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "send_rad"));

    public static final StreamCodec<FriendlyByteBuf, SendRadPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public SendRadPacket decode(FriendlyByteBuf buf) {
                    return new SendRadPacket(buf.readFloat());
                }

                @Override
                public void encode(FriendlyByteBuf buf, SendRadPacket packet) {
                    buf.writeFloat(packet.rad);
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
