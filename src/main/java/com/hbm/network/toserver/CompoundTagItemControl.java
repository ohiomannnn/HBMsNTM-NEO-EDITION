package com.hbm.network.toserver;

import com.hbm.HBMsNTM;
import com.hbm.items.IItemControlReceiver;
import com.hbm.util.InventoryUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record CompoundTagItemControl(CompoundTag tag) implements CustomPacketPayload {

    public static final Type<CompoundTagItemControl> TYPE = new Type<>(HBMsNTM.withDefaultNamespaceNT("compound_tag_item_control"));

    public static final StreamCodec<FriendlyByteBuf, CompoundTagItemControl> STREAM_CODEC = new StreamCodec<>() {
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
            Player player = context.player();

            CompoundTag nbt = packet.tag();
            List<ItemStack> heldStacks = InventoryUtil.getItemsFromBothHands(player);
            for (ItemStack held : heldStacks) {
                if (held.getItem() instanceof IItemControlReceiver receiver) {
                    receiver.receiveControl(held, nbt);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
