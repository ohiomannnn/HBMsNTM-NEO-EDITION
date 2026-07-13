package com.hbm.network.toclient;

import com.hbm.main.NuclearTechMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record InformPlayer(Component component, int id, int millis) implements CustomPacketPayload {

    public static final Type<InformPlayer> TYPE = new Type<>(NuclearTechMod.withDefaultNamespace("inform_player_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, InformPlayer> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public InformPlayer decode(RegistryFriendlyByteBuf buf) {
            int id = buf.readInt();
            int millis = buf.readInt();
            Component component = ComponentSerialization.STREAM_CODEC.decode(buf);
            return new InformPlayer(component, id, millis);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, InformPlayer packet) {
            buf.writeInt(packet.id);
            buf.writeInt(packet.millis);
            ComponentSerialization.STREAM_CODEC.encode(buf, packet.component);
        }
    };

    public static void handleClient(InformPlayer packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            NuclearTechMod.proxy.displayTooltip(packet.component, packet.millis, packet.id);
        });
    }

    @Override public Type<InformPlayer> type() { return TYPE; }
}
