package com.hbm.network.toserver;

import com.hbm.HBMsNTM;
import com.hbm.handler.HbmKeybinds.EnumKeybind;
import com.hbm.handler.HbmKeybindsServer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record KeybindReceiver(EnumKeybind keybind, boolean pressed) implements CustomPacketPayload {

    public static final Type<KeybindReceiver> TYPE = new Type<>(HBMsNTM.withDefaultNamespaceNT("keybind"));

    public static final StreamCodec<FriendlyByteBuf, KeybindReceiver> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public KeybindReceiver decode(FriendlyByteBuf buf) {
            EnumKeybind key = buf.readEnum(EnumKeybind.class);
            boolean state = buf.readBoolean();
            return new KeybindReceiver(key, state);
        }

        @Override
        public void encode(FriendlyByteBuf buf, KeybindReceiver packet) {
            buf.writeEnum(packet.keybind);
            buf.writeBoolean(packet.pressed);
        }
    };

    public static void handleServer(KeybindReceiver packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            HbmKeybindsServer.onPressedServer(context.player(), packet.keybind, packet.pressed);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
