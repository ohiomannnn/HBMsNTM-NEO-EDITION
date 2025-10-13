package com.hbm.packets.toclient;

import com.hbm.HBMsNTM;
import com.hbm.explosion.vanillant.standard.ExplosionEffectStandard;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

// best name
public record ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket(
        double posX,
        double posY,
        double posZ,
        float size,
        List<BlockPos> affectedBlocks
) implements CustomPacketPayload {

    public static final Type<ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "explosion_vnt_blocks"));

    public static final StreamCodec<FriendlyByteBuf, ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket decode(FriendlyByteBuf buf) {
                    double x = buf.readFloat();
                    double y = buf.readFloat();
                    double z = buf.readFloat();
                    float size = buf.readFloat();

                    int count = buf.readInt();
                    List<BlockPos> list = new ArrayList<>(count);

                    int baseX = (int) x;
                    int baseY = (int) y;
                    int baseZ = (int) z;

                    for (int i = 0; i < count; i++) {
                        int dx = buf.readByte();
                        int dy = buf.readByte();
                        int dz = buf.readByte();
                        list.add(new BlockPos(baseX + dx, baseY + dy, baseZ + dz));
                    }

                    return new ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket(x, y, z, size, list);
                }

                @Override
                public void encode(FriendlyByteBuf buf, ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket pkt) {
                    buf.writeFloat((float) pkt.posX);
                    buf.writeFloat((float) pkt.posY);
                    buf.writeFloat((float) pkt.posZ);
                    buf.writeFloat(pkt.size);
                    buf.writeInt(pkt.affectedBlocks.size());

                    int baseX = (int) pkt.posX;
                    int baseY = (int) pkt.posY;
                    int baseZ = (int) pkt.posZ;

                    for (BlockPos pos : pkt.affectedBlocks) {
                        buf.writeByte(pos.getX() - baseX);
                        buf.writeByte(pos.getY() - baseY);
                        buf.writeByte(pos.getZ() - baseZ);
                    }
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
