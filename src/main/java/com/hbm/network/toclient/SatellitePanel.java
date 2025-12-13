package com.hbm.network.toclient;

import com.hbm.HBMsNTM;
import com.hbm.explosion.vanillant.standard.ExplosionEffectStandard;
import com.hbm.items.tools.SatelliteInterfaceItem;
import com.hbm.saveddata.satellite.Satellite;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record SatellitePanel(int satType, CompoundTag tag) implements CustomPacketPayload {
    public static final Type<SatellitePanel> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "sat_panel"));

    public static final StreamCodec<FriendlyByteBuf, SatellitePanel> STREAM_CODEC = new StreamCodec<>() {
                @Override
                public SatellitePanel decode(FriendlyByteBuf buf) {
                    int type = buf.readInt();
                    CompoundTag nbt = buf.readNbt();
                    return new SatellitePanel(type, nbt);
                }

                @Override
                public void encode(FriendlyByteBuf buf, SatellitePanel packet) {
                    buf.writeInt(packet.satType);
                    buf.writeNbt(packet.tag);
                }
            };

    public static void handleClient(SatellitePanel packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            CompoundTag tag = packet.tag;

            SatelliteInterfaceItem.currentSat = Satellite.create(packet.satType);

            if (tag != null) {
                SatelliteInterfaceItem.currentSat.readFromNBT(tag);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
