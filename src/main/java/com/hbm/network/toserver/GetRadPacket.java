package com.hbm.network.toserver;

import com.hbm.HBMsNTM;
import com.hbm.extprop.LivingProperties;
import com.hbm.network.toclient.SendRadPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

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

    public static void handleServer(GetRadPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer sender)) return;

            MinecraftServer server = sender.server;
            UUID target = packet.targetId();

            for (ServerLevel level : server.getAllLevels()) {
                var entity = level.getEntity(target);
                if (entity instanceof LivingEntity living) {
                    float rad = LivingProperties.getRadiation(living);

                    PacketDistributor.sendToPlayer(sender, new SendRadPacket(rad));
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
