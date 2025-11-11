package com.hbm.network.toserver;

import com.hbm.HBMsNTM;
import com.hbm.extprop.PlayerProperties;
import com.hbm.handler.KeyHandler;
import com.hbm.items.IKeybindReceiver;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record KeybindReceiver(KeyHandler.EnumKeybind keybind, boolean state, boolean property) implements CustomPacketPayload {

    public static final Type<KeybindReceiver> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "keybind"));

    public static final StreamCodec<FriendlyByteBuf, KeybindReceiver> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public KeybindReceiver decode(FriendlyByteBuf buf) {
                    KeyHandler.EnumKeybind key = buf.readEnum(KeyHandler.EnumKeybind.class);
                    boolean state = buf.readBoolean();
                    boolean property = buf.readBoolean();
                    return new KeybindReceiver(key, state, property);
                }

                @Override
                public void encode(FriendlyByteBuf buf, KeybindReceiver packet) {
                    buf.writeEnum(packet.keybind);
                    buf.writeBoolean(packet.state);
                    buf.writeBoolean(packet.property);
                }
            };

    public static void handleServer(KeybindReceiver packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            if (packet.property) {
                PlayerProperties props = PlayerProperties.getData(player);
                props.setKeyPressed(packet.keybind(), packet.state());
            }

            if (!packet.property) {
                ItemStack stack = player.getMainHandItem();
                if (stack.getItem() instanceof IKeybindReceiver receiver) {
                    if (receiver.canHandleKeybind(player, stack, packet.keybind())) {
                        receiver.handleKeybind(player, stack, packet.keybind(), packet.state());
                    }
                }

                ItemStack off = player.getOffhandItem();
                if (off.getItem() instanceof IKeybindReceiver receiver) {
                    if (receiver.canHandleKeybind(player, off, packet.keybind())) {
                        receiver.handleKeybind(player, off, packet.keybind(), packet.state());
                    }
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
