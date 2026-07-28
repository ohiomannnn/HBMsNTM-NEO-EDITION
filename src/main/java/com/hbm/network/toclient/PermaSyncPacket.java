package com.hbm.network.toclient;

import com.hbm.main.NuclearTechMod;
import com.hbm.network.PermaSyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PermaSyncPacket implements CustomPacketPayload {

    public static final Type<PermaSyncPacket> TYPE = new Type<>(NuclearTechMod.withDefaultNamespace("perma_sync"));

    private final ServerPlayer player; //server only, for writing
    private final ByteBuf out;         //client only, for reading

    public PermaSyncPacket(ServerPlayer player) {
        this.player = player;
        this.out = null;
    }

    private PermaSyncPacket(ByteBuf out) {
        this.player = null;
        this.out = out;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, PermaSyncPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public PermaSyncPacket decode(RegistryFriendlyByteBuf buf) {
            return new PermaSyncPacket(buf.copy());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PermaSyncPacket packet) {
            PermaSyncHandler.writePacket(buf, packet.player.level, packet.player);
        }
    };

    public static void handleCommon(PermaSyncPacket packet, IPayloadContext context) { handleClient(packet, context); }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(PermaSyncPacket packet, IPayloadContext context) {

        try {
            Player player = Minecraft.getInstance().player;
            if(player == null) return;
            PermaSyncHandler.readPacket(packet.out, player.level(), player);
        } catch (Exception ignored) {

        } finally {
            packet.out.release();
        }
    }

    @Override public Type<PermaSyncPacket> type() { return TYPE; }
}