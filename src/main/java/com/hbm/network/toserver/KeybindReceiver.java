package com.hbm.network.toserver;

import com.hbm.HBMsNTM;
import com.hbm.extprop.HbmPlayerAttachments;
import com.hbm.handler.KeyHandler.EnumKeybind;
import com.hbm.items.IKeybindReceiver;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record KeybindReceiver(EnumKeybind keybind, boolean state) implements CustomPacketPayload {

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
            buf.writeBoolean(packet.state);
        }
    };

    public static void handleServer(KeybindReceiver packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();

            HbmPlayerAttachments props = HbmPlayerAttachments.getData(player);
            props.setKeyPressed(context.player(), packet.keybind(), packet.state());

            for (ItemStack stack : new ItemStack[]{player.getMainHandItem(), player.getOffhandItem()}) {
                if (stack.getItem() instanceof IKeybindReceiver receiver) {
                    if (receiver.canHandleKeybind(player, stack, packet.keybind())) {
                        receiver.handleKeybind(player, stack, packet.keybind(), packet.state());
                    }
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
