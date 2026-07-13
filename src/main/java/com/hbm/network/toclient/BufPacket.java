package com.hbm.network.toclient;

import com.hbm.blockentity.IBufPacketReceiver;
import com.hbm.main.NuclearTechMod;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.connection.ConnectionType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BufPacket(BlockPos pos, byte[] data) implements CustomPacketPayload {

    public static final Type<BufPacket> TYPE = new Type<>(NuclearTechMod.withDefaultNamespace("buf_packet"));

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

    public static void handleCommon(BufPacket packet, IPayloadContext context) { handleClient(packet, context); }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(BufPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if(level == null) return;

            BlockEntity be = level.getBlockEntity(packet.pos);

            if(be instanceof IBufPacketReceiver receiver) {
                RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(packet.data), level.registryAccess(), ConnectionType.OTHER);
                try {
                    receiver.deserialize(buf);
                } catch(Exception e) {
                    NuclearTechMod.LOGGER.warn("A ByteBuf packet failed to be read and has thrown an error. This normally means that there was a buffer underflow and more data was read than was actually in the packet.\")");
                    NuclearTechMod.LOGGER.warn("Block: {}", be.getBlockState().getBlock().getDescriptionId());
                    NuclearTechMod.LOGGER.warn(e.getMessage());
                } finally {
                    buf.release();
                }
            }
        });
    }

    @Override public Type<BufPacket> type() { return TYPE; }
}