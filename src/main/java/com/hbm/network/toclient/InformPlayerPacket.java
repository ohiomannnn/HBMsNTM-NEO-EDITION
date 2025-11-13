package com.hbm.network.toclient;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record InformPlayerPacket(Component component, int id, int millis) implements CustomPacketPayload {

    public static final Type<InformPlayerPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "inform_player"));

    public static final StreamCodec<RegistryFriendlyByteBuf, InformPlayerPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public InformPlayerPacket decode(RegistryFriendlyByteBuf buf) {
            int id = buf.readInt();
            int millis = buf.readInt();
            return new InformPlayerPacket(ComponentSerialization.STREAM_CODEC.decode(buf), id, millis);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, InformPlayerPacket packet) {
            buf.writeInt(packet.id);
            buf.writeInt(packet.millis);
            ComponentSerialization.STREAM_CODEC.encode(buf, packet.component);
        }
    };

    public static void handleClient(InformPlayerPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            HBMsNTMClient.displayTooltip(packet.component(), packet.millis(), packet.id());
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
