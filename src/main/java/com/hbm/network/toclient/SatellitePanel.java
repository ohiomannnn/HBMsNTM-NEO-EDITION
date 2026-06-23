package com.hbm.network.toclient;

import com.hbm.items.tools.SatelliteInterfaceItem;
import com.hbm.main.NuclearTechMod;
import com.hbm.saveddata.satellite.Satellite;
import com.hbm.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record SatellitePanel(int satType, CompoundTag tag) implements CustomPacketPayload {

    public static final Type<SatellitePanel> TYPE = new Type<>(NuclearTechMod.withDefaultNamespace("satellite_panel_packet"));

    public static final StreamCodec<FriendlyByteBuf, SatellitePanel> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SatellitePanel decode(FriendlyByteBuf buf) {
            int type = buf.readInt();
            CompoundTag tag = buf.readNbt();
            return new SatellitePanel(type, tag);
        }

        @Override
        public void encode(FriendlyByteBuf buf, SatellitePanel packet) {
            buf.writeInt(packet.satType);
            buf.writeNbt(packet.tag);
        }
    };

    public static void handleClient(SatellitePanel packet, IPayloadContext context) {
        context.enqueueWork(() -> {

            Player player = Minecraft.getInstance().player;
            if(player == null) return;

            List<ItemStack> stacks = InventoryUtil.getItemsFromBothHands(player);
            for(ItemStack stack : stacks) {
                if(stack.getItem() instanceof SatelliteInterfaceItem satItem) {
                    CompoundTag tag = packet.tag;
                    satItem.satellite = Satellite.create(packet.satType);
                    satItem.satellite.readAdditional(tag);
                    break;
                }
            }
        });
    }

    @Override public Type<SatellitePanel> type() { return TYPE; }
}
