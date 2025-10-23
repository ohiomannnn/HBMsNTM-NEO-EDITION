package com.hbm.network.toclient;

import com.hbm.HBMsNTM;
import com.hbm.extprop.LivingProperties;
import com.hbm.handler.gui.GeigerGUI;
import com.hbm.network.toserver.GetRadPacket;
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

public record SendRadPacket(float rad) implements CustomPacketPayload {
    public static final Type<SendRadPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "send_rad"));

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

    public static void handleClient(SendRadPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> GeigerGUI.getRadFromServer(packet.rad()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
