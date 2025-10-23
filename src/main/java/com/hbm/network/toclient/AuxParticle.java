package com.hbm.network.toclient;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AuxParticle(CompoundTag nbt, double x, double y, double z) implements CustomPacketPayload {
    public static final Type<AuxParticle> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "aux_particle"));

    public static final StreamCodec<FriendlyByteBuf, AuxParticle> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public AuxParticle decode(FriendlyByteBuf buf) {
                    return new AuxParticle(
                            buf.readNbt(),
                            buf.readDouble(),
                            buf.readDouble(),
                            buf.readDouble()
                    );
                }

                @Override
                public void encode(FriendlyByteBuf buf, AuxParticle packet) {
                    buf.writeNbt(packet.nbt);
                    buf.writeDouble(packet.x);
                    buf.writeDouble(packet.y);
                    buf.writeDouble(packet.z);
                }
            };

    public static void handleClient(AuxParticle packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            packet.nbt().putDouble("posX", packet.x());
            packet.nbt().putDouble("posY", packet.y());
            packet.nbt().putDouble("posZ", packet.z());
            if (mc.level != null) {
                HBMsNTMClient.effectNT(packet.nbt());
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
