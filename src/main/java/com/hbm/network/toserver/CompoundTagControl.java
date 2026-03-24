package com.hbm.network.toserver;

import com.hbm.NuclearTechMod;
import com.hbm.interfaces.IControlReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CompoundTagControl(CompoundTag tag, BlockPos pos) implements CustomPacketPayload {
    public static final Type<CompoundTagControl> TYPE = new Type<>(NuclearTechMod.withDefaultNamespace("compound_tag_control"));

    public static final StreamCodec<FriendlyByteBuf, CompoundTagControl> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public CompoundTagControl decode(FriendlyByteBuf buf) {
            return new CompoundTagControl(buf.readNbt(), buf.readBlockPos());
        }

        @Override
        public void encode(FriendlyByteBuf buf, CompoundTagControl packet) {
            buf.writeNbt(packet.tag);
            buf.writeBlockPos(packet.pos);
        }
    };

    public static void handleServer(CompoundTagControl packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();

            BlockEntity be = player.level.getBlockEntity(packet.pos);

            if (be instanceof IControlReceiver icr) {
                if (icr.hasPermission(player)) {
                    icr.receiveControl(player, packet.tag);
                    icr.receiveControl(packet.tag);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
