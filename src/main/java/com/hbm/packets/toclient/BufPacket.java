package com.hbm.packets.toclient;

import com.hbm.HBMsNTM;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record BufPacket(BlockPos pos, FriendlyByteBuf payload) implements CustomPacketPayload {

    public static final Type<BufPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "buf"));

    public static final StreamCodec<FriendlyByteBuf, BufPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public BufPacket decode(FriendlyByteBuf buf) {
                    BlockPos pos = buf.readBlockPos();

                    FriendlyByteBuf copy = new FriendlyByteBuf(Unpooled.copiedBuffer(buf.readBytes(buf.readableBytes())));
                    return new BufPacket(pos, copy);
                }

                @Override
                public void encode(FriendlyByteBuf buf, BufPacket packet) {
                    buf.writeBlockPos(packet.pos);
                    buf.writeBytes(packet.payload);
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
