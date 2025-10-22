package com.hbm.packets.toserver;

import com.hbm.HBMsNTM;
import com.hbm.handler.HbmKeybinds.EnumKeybind;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record KeybindPacket(EnumKeybind key, boolean pressed) implements CustomPacketPayload {

    public static final Type<KeybindPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "keybind"));

    public static final StreamCodec<FriendlyByteBuf, KeybindPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public KeybindPacket decode(FriendlyByteBuf buf) {
                    EnumKeybind key = buf.readEnum(EnumKeybind.class);
                    boolean pressed = buf.readBoolean();
                    return new KeybindPacket(key, pressed);
                }

                @Override
                public void encode(FriendlyByteBuf buf, KeybindPacket packet) {
                    buf.writeEnum(packet.key());
                    buf.writeBoolean(packet.pressed());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}