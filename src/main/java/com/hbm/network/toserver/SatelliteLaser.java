package com.hbm.network.toserver;

import com.hbm.HBMsNTM;
import com.hbm.items.IItemControlReceiver;
import com.hbm.items.ISatChip;
import com.hbm.items.tools.SatelliteInterfaceItem;
import com.hbm.saveddata.SatelliteSavedData;
import com.hbm.saveddata.satellite.Satellite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SatelliteLaser(int x, int z, int freq) implements CustomPacketPayload {
    public static final Type<SatelliteLaser> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "sat_laser"));

    public static final StreamCodec<FriendlyByteBuf, SatelliteLaser> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SatelliteLaser decode(FriendlyByteBuf buf) {
            return new SatelliteLaser(buf.readInt(), buf.readInt(), buf.readInt());
        }

        @Override
        public void encode(FriendlyByteBuf buf, SatelliteLaser packet) {
            buf.writeInt(packet.x);
            buf.writeInt(packet.z);
            buf.writeInt(packet.freq);
        }
    };

    public static void handleServer(SatelliteLaser packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            boolean gotItem =
                    player.getMainHandItem().getItem() instanceof SatelliteInterfaceItem ||
                            player.getOffhandItem().getItem() instanceof SatelliteInterfaceItem;

            if (gotItem) {
                int freq = ISatChip.getFreqS(player.getMainHandItem().isEmpty() ? player.getOffhandItem() : player.getMainHandItem());

                if (freq == packet.freq) {
                    Satellite sat = SatelliteSavedData.get((ServerLevel) context.player().level()).getSatFromFreq(packet.freq);

                    if (sat != null) {
                        sat.onClick(context.player().level(), packet.x, packet.z);
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
