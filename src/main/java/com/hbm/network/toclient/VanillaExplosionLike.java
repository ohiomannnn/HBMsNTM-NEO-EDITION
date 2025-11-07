package com.hbm.network.toclient;

import com.hbm.HBMsNTM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record VanillaExplosionLike(double x, double y, double z, float size, List<BlockPos> affectedBlocks) implements CustomPacketPayload {
    public static final Type<VanillaExplosionLike> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "vanilla_explosion"));

    public static final StreamCodec<FriendlyByteBuf, VanillaExplosionLike> STREAM_CODEC = new StreamCodec<>() {

                @Override
                public VanillaExplosionLike decode(FriendlyByteBuf buf) {
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
                    return new VanillaExplosionLike(x, y, z, size, list);
                }

                @Override
                public void encode(FriendlyByteBuf buf, VanillaExplosionLike packet) {
                    buf.writeFloat((float) packet.x);
                    buf.writeFloat((float) packet.y);
                    buf.writeFloat((float) packet.z);
                    buf.writeFloat(packet.size);
                    buf.writeInt(packet.affectedBlocks.size());

                    int baseX = (int) packet.x;
                    int baseY = (int) packet.y;
                    int baseZ = (int) packet.z;

                    for (BlockPos pos : packet.affectedBlocks) {
                        buf.writeByte(pos.getX() - baseX);
                        buf.writeByte(pos.getY() - baseY);
                        buf.writeByte(pos.getZ() - baseZ);
                    }
                }
            };

    public static void handleClient(VanillaExplosionLike packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;

            if (packet.size >= 2.0F) {
                level.addParticle(ParticleTypes.EXPLOSION_EMITTER, packet.x, packet.y, packet.z, 1.0D, 0.0D, 0.0D);
            } else {
                level.addParticle(ParticleTypes.EXPLOSION, packet.x, packet.y, packet.z, 1.0D, 0.0D, 0.0D);
            }

            int count = packet.affectedBlocks.size();

            for (int i = 0; i < count; i++) {

                BlockPos pos = packet.affectedBlocks.get(i);
                int pX = pos.getX();
                int pY = pos.getY();
                int pZ = pos.getZ();

                double oX = ((float) pX + level.random.nextFloat());
                double oY = ((float) pY + level.random.nextFloat());
                double oZ = ((float) pZ + level.random.nextFloat());
                double dX = oX - packet.x;
                double dY = oY - packet.y;
                double dZ = oZ - packet.z;
                double delta = Mth.sqrt((float) (dX * dX + dY * dY + dZ * dZ)) / 1D /* hehehe */;
                dX /= delta;
                dY /= delta;
                dZ /= delta;
                double mod = 0.5D / (delta / (double) packet.size + 0.1D);
                mod *= (level.random.nextFloat() * level.random.nextFloat() + 0.3F);
                dX *= mod;
                dY *= mod;
                dZ *= mod;
                level.addParticle(ParticleTypes.CLOUD, (oX + packet.x * 1.0D) / 2.0D, (oY + packet.y * 1.0D) / 2.0D, (oZ + packet.z * 1.0D) / 2.0D, dX, dY, dZ);
                level.addParticle(ParticleTypes.SMOKE, oX, oY, oZ, dX, dY, dZ);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
