package com.hbm.network.toclient;

import com.hbm.HBMsNTM;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

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

    public static void handleClient(ParticleBurstPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Block block = BuiltInRegistries.BLOCK.get(packet.blockId());
            BlockState state = block.defaultBlockState();
            try {
                Minecraft.getInstance().particleEngine.destroy(packet.pos(), state);
            } catch (Exception ignored) { }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
