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

public record ParticleBurst(BlockPos pos, Block block) implements CustomPacketPayload {

    public static final Type<ParticleBurst> TYPE = new Type<>(HBMsNTM.withDefaultNamespaceNT("particle_burst_packet"));

    public static final StreamCodec<FriendlyByteBuf, ParticleBurst> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public ParticleBurst decode(FriendlyByteBuf buf) {
                    BlockPos pos = buf.readBlockPos();
                    ResourceLocation id = buf.readResourceLocation();
                    Block block = BuiltInRegistries.BLOCK.get(id);
                    return new ParticleBurst(pos, block);
                }

                @Override
                public void encode(FriendlyByteBuf buf, ParticleBurst packet) {
                    buf.writeBlockPos(packet.pos());
                    buf.writeResourceLocation(BuiltInRegistries.BLOCK.getKey(packet.block()));
                }
            };

    public static void handleClient(ParticleBurst packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            BlockState state = packet.block().defaultBlockState();
            Minecraft.getInstance().particleEngine.destroy(packet.pos(), state);
        });
    }

    @Override public Type<? extends CustomPacketPayload> type() {return TYPE; }
}
