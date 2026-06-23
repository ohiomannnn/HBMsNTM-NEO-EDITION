package com.hbm.network.toserver;

import com.hbm.items.ISatChip;
import com.hbm.main.NuclearTechMod;
import com.hbm.saveddata.SatelliteSavedData;
import com.hbm.saveddata.satellite.Satellite;
import com.hbm.util.InventoryUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record SatelliteLaser(int x, int z, int freq) implements CustomPacketPayload {
    public static final Type<SatelliteLaser> TYPE = new Type<>(NuclearTechMod.withDefaultNamespace("satellite_click_packet"));

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

            if(player.level instanceof ServerLevel serverLevel) {
                int freq = 0;

                List<ItemStack> stacks = InventoryUtil.getItemsFromBothHands(player);
                for(ItemStack stack : stacks) {
                    if(stack.getItem() instanceof ISatChip satChip) {
                        freq = satChip.getFreq(stack);
                        break;
                    }
                }

                if(freq == packet.freq) {
                    Satellite sat = SatelliteSavedData.getData(serverLevel).getSatFromFreq(packet.freq);

                    if(sat != null) sat.onClick(serverLevel, packet.x, packet.z);
                }
            }
        });
    }

    @Override public Type<SatelliteLaser> type() { return TYPE; }
}
