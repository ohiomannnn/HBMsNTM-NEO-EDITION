package com.hbm.network.toclient;

import com.hbm.HBMsNTM;
import com.hbm.explosion.vanillant.standard.ExplosionEffectStandard;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record VanillaExplosionLike(double x, double y, double z, float size, List<BlockPos> affectedBlocks) implements CustomPacketPayload {
    public static final Type<VanillaExplosionLike> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "vanilla_explosion"));

    public static final StreamCodec<FriendlyByteBuf, VanillaExplosionLike> STREAM_CODEC = new StreamCodec<>() {

                @Override
                public VanillaExplosionLike decode(FriendlyByteBuf buf) {
                    double posX = buf.readFloat();
                    double posY = buf.readFloat();
                    double posZ = buf.readFloat();
                    float size = buf.readFloat();

                    int i = buf.readInt();
                    List<BlockPos> affectedBlocks = new ArrayList<>(i);

                    int j = (int) posX;
                    int k = (int) posY;
                    int l = (int) posZ;

                    for (int i1 = 0; i1 < i; ++i1) {
                        int j1 = buf.readByte() + j;
                        int k1 = buf.readByte() + k;
                        int l1 = buf.readByte() + l;
                        affectedBlocks.add(new BlockPos(j1, k1, l1));
                    }

                    return new VanillaExplosionLike(posX, posY, posZ, size, affectedBlocks);
                }

                @Override
                public void encode(FriendlyByteBuf buf, VanillaExplosionLike packet) {
                    buf.writeFloat((float) packet.x);
                    buf.writeFloat((float) packet.y);
                    buf.writeFloat((float) packet.z);
                    buf.writeFloat(packet.size);
                    buf.writeInt(packet.affectedBlocks.size());
                    int i = (int) packet.x;
                    int j = (int) packet.y;
                    int k = (int) packet.z;

                    for (BlockPos blockPos : packet.affectedBlocks) {
                        int l = blockPos.getX() - i;
                        int i1 = blockPos.getY() - j;
                        int j1 = blockPos.getZ() - k;
                        buf.writeByte(l);
                        buf.writeByte(i1);
                        buf.writeByte(j1);
                    }
                }
            };

    public static void handleClient(VanillaExplosionLike packet, IPayloadContext context) {
        context.enqueueWork(() -> ExplosionEffectStandard.performClient(Minecraft.getInstance().level, packet.x,  packet.y, packet.z, packet.size, packet.affectedBlocks));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
