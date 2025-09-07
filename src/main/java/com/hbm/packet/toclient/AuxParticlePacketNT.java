package com.hbm.packet.toclient;

import com.hbm.HBMsNTM;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record AuxParticlePacketNT(String particleType, double x, double y, double z, double mX, double mY, double mZ, int color) implements CustomPacketPayload {

    public static final Type<AuxParticlePacketNT> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "aux_particle"));

    public static final StreamCodec<FriendlyByteBuf, AuxParticlePacketNT> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public @NotNull AuxParticlePacketNT decode(FriendlyByteBuf buf) {
                    String type = buf.readUtf();
                    double x = buf.readDouble();
                    double y = buf.readDouble();
                    double z = buf.readDouble();
                    double mX = buf.readDouble();
                    double mY = buf.readDouble();
                    double mZ = buf.readDouble();
                    int color = buf.readInt();
                    return new AuxParticlePacketNT(type, x, y, z, mX, mY, mZ, color);
                }

                @Override
                public void encode(FriendlyByteBuf buf, AuxParticlePacketNT msg) {
                    buf.writeUtf(msg.particleType);
                    buf.writeDouble(msg.x());
                    buf.writeDouble(msg.y());
                    buf.writeDouble(msg.z());
                    buf.writeDouble(msg.mX());
                    buf.writeDouble(msg.mY());
                    buf.writeDouble(msg.mZ());
                    buf.writeInt(msg.color());
                }
            };

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
