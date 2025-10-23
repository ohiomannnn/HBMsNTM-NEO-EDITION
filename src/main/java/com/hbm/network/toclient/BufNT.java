package com.hbm.network.toclient;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.IBufPacketReceiver;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BufNT(BlockPos pos, FriendlyByteBuf payload) implements CustomPacketPayload {
    public static final Type<BufNT> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "buf"));

    public static final StreamCodec<FriendlyByteBuf, BufNT> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public BufNT decode(FriendlyByteBuf buf) {
                    BlockPos pos = buf.readBlockPos();

                    FriendlyByteBuf copy = new FriendlyByteBuf(Unpooled.copiedBuffer(buf.readBytes(buf.readableBytes())));
                    return new BufNT(pos, copy);
                }

                @Override
                public void encode(FriendlyByteBuf buf, BufNT packet) {
                    buf.writeBlockPos(packet.pos);
                    buf.writeBytes(packet.payload);
                }
            };

    public static void handleClient(BufNT packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) return;

            BlockEntity be = level.getBlockEntity(packet.pos());
            if (be instanceof IBufPacketReceiver receiver) {
                try {
                    receiver.deserialize(packet.payload());
                } catch (Exception e) { // if im dumb
                    HBMsNTM.LOGGER.warn("Error reading ByteBuf package: {}", e.getMessage());
                    HBMsNTM.LOGGER.warn("Tile: {}", be.getBlockState().getBlock().getName().getString());
                } finally {
                    packet.payload().release();
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
