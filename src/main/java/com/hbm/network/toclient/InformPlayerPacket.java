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

public record InformPlayerPacket(boolean fancy, String message, Component component, int id, int millis) implements CustomPacketPayload {

    public static final Type<InformPlayerPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "inform_player"));

    public static final StreamCodec<RegistryFriendlyByteBuf, InformPlayerPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public InformPlayerPacket decode(RegistryFriendlyByteBuf buf) {
            int id = buf.readInt();
            int millis = buf.readInt();
            boolean fancy = buf.readBoolean();

            if (!fancy) {
                String msg = buf.readUtf();
                return new InformPlayerPacket(false, msg, null, id, millis);
            } else {
                Component comp = ComponentSerialization.STREAM_CODEC.decode(buf);
                return new InformPlayerPacket(true, null, comp, id, millis);
            }
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, InformPlayerPacket packet) {
            buf.writeInt(packet.id);
            buf.writeInt(packet.millis);
            buf.writeBoolean(packet.fancy);

            if (!packet.fancy) {
                buf.writeUtf(packet.message);
            } else {
                ComponentSerialization.STREAM_CODEC.encode(buf, packet.component);
            }
        }
    };

    public static void handleClient(InformPlayerPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            try {
                if (packet.fancy()) {
                    HBMsNTMClient.displayTooltip(packet.component(), packet.millis(), packet.id());
                } else {
                    HBMsNTMClient.displayTooltip(Component.literal(packet.message()), packet.millis(), packet.id());
                }
            } catch (Exception ignored) {}
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
