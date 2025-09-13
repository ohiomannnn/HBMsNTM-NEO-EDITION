package com.hbm.packets.toclient;

import com.hbm.HBMsNTM;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ParticleBurstPacket(BlockPos pos, ResourceLocation blockId) implements CustomPacketPayload {

    public static final Type<ParticleBurstPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "particle_burst"));


    public static final StreamCodec<FriendlyByteBuf, ParticleBurstPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public ParticleBurstPacket decode(FriendlyByteBuf buf) {
                    return new ParticleBurstPacket(
                            buf.readBlockPos(),
                            buf.readResourceLocation()
                    );
                }

                @Override
                public void encode(FriendlyByteBuf buf, ParticleBurstPacket packet) {
                    buf.writeBlockPos(packet.pos);
                    buf.writeResourceLocation(packet.blockId());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
