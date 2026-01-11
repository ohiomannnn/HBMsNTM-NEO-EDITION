package com.hbm.network.toclient;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.IBufPacketReceiver;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BufPacket(BlockPos pos, byte[] data) implements CustomPacketPayload {

    public static final Type<BufPacket> TYPE = new Type<>(HBMsNTM.withDefaultNamespaceNT("buf_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BufPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public BufPacket decode(RegistryFriendlyByteBuf buf) {
            BlockPos pos = buf.readBlockPos();
            byte[] data = buf.readByteArray();
            return new BufPacket(pos, data);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, BufPacket packet) {
            buf.writeBlockPos(packet.pos);
            buf.writeByteArray(packet.data);
        }
    };

    public static void handleClient(BufPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> performShit(packet, context));
    }

    // why are you CRASHING
    @OnlyIn(Dist.CLIENT)
    public static void performShit(BufPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;

            BlockEntity be = level.getBlockEntity(packet.pos);

            if (be instanceof IBufPacketReceiver receiver) {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(packet.data));
                try {
                    receiver.deserialize(buf, level.registryAccess());
                } catch (Exception e) {
                    HBMsNTM.LOGGER.warn("A ByteBuf packet failed to be read. Buffer underflow or invalid data.");
                    HBMsNTM.LOGGER.warn("Block: {}", be.getBlockState().getBlock().getDescriptionId());
                    HBMsNTM.LOGGER.warn(e.getMessage());
                } finally {
                    buf.release();
                }
            }
        });
    }

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}