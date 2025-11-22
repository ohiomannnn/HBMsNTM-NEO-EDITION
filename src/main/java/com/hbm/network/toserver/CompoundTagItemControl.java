package com.hbm.network.toserver;

import com.hbm.HBMsNTM;
import com.hbm.items.IItemControlReceiver;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CompoundTagItemControl(CompoundTag tag) implements CustomPacketPayload {
    public static final Type<CompoundTagItemControl> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "compound_tag_item_control"));

    public static final StreamCodec<FriendlyByteBuf, CompoundTagItemControl> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public CompoundTagItemControl decode(FriendlyByteBuf buf) {
                    return new CompoundTagItemControl(buf.readNbt());
                }

                @Override
                public void encode(FriendlyByteBuf buf, CompoundTagItemControl packet) {
                    buf.writeNbt(packet.tag);
                }
            };

    public static void handleServer(CompoundTagItemControl packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer serverPlayer)) return;

            CompoundTag nbt = packet.tag();
            if (nbt != null) {
                ItemStack held = serverPlayer.getMainHandItem();
                if (held.getItem() instanceof IItemControlReceiver receiver) {
                    receiver.receiveControl(held, nbt);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
